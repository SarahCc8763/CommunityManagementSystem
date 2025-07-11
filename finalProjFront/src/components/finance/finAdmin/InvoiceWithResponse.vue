<template>
  <div>

    <!-- 查詢區塊 -->
    <div class="filter-bar mb-3">
      <label class="me-2">期別：</label>
      <input v-model="filter.periodName" class="form-control d-inline-block w-auto me-3" placeholder="請輸入期別" />
      <label class="me-2">費用類別：</label>
      <input v-model="filter.feeType" class="form-control d-inline-block w-auto me-3" placeholder="請輸入費用類別" />
      <label class="me-2">逾期：</label>
      <select v-model="filter.overdue" class="form-select d-inline-block w-auto me-3">
        <option value="">全部</option>
        <option value="true">僅顯示逾期</option>
        <option value="false">僅顯示未逾期</option>
      </select>
      <button class="btn btn-outline-primary btn-sm" @click="clearFilter">清除</button>
    </div>

    <div class="d-flex align-items-center mb-2" v-if="allInvoiceResponses.length > 0">
      <input type="checkbox" v-model="allChecked" @change="toggleAll" class="form-check-input me-2" />
      <span>全選</span>
      <button class="btn btn-success btn-sm ms-3" :disabled="checkedResponses.length === 0"
        @click="batchCreateReceipts">一鍵產生收據</button>
    </div>

    <div v-for="inv in filteredInvoices" :key="inv.invoiceId" class="invoice-card mb-4">
      <h3>發票資訊</h3>
      <h4>發票 ID：{{ inv.invoiceId }}　| 狀態：{{ inv.paymentStatus }}</h4>
      <div>住戶：{{ inv.user?.name }}</div>
      <div>費用類型：{{ inv.feeType?.description }}</div>
      <div>期別：{{ inv.billingPeriod?.periodName }}</div>
      <div>金額：{{ inv.amountDue }}</div>
      <div>截止日：{{ inv.deadline }}</div>
      <div>備註：{{ inv.note }}</div>

      <div class="mt-2">
        <h5>回覆紀錄：</h5>
        <div v-if="Array.isArray(inv.invoiceResponses) && inv.invoiceResponses.length > 0">
          <div v-for="res in inv.invoiceResponses" :key="res.invoiceResponseId"
            class="response-card d-flex align-items-center mb-2">
            <input type="checkbox" class="form-check-input me-2" :value="res.invoiceResponseId"
              v-model="checkedResponses" />
            <div class="flex-grow-1">
              <div>留言：{{ res.lastResponse }}</div>
              <div>末五碼：{{ res.accountCode }}</div>
              <div>時間：{{ res.lastResponseTime }}</div>
              <div>是否審核：{{ res.verified ? '✅' : '❌' }}</div>
              <div v-if="isOverdue(inv.deadline)"><span class="badge bg-danger">逾期</span></div>
            </div>
            <button class="btn btn-outline-primary btn-sm ms-2"
              @click="goToReceiptAdd(inv.invoiceId, res.userId)">審核已付款</button>
          </div>
        </div>
        <div v-else>尚無回覆</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import axiosapi from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
import Swal from 'sweetalert2'
import html2pdf from 'html2pdf.js'
const userStore = useUserStore()
const invoices = ref([])
const receiptDetail = ref(null)
const communityName = ref('')
const receiptRef = ref(null)
const showReceiptModal = ref(false)
const receiptForm = reactive({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  installments: '',
  note: '',
  status: false
})
const successMsg = ref('')
const errorMsg = ref('')
const selectedInvoice = ref(null)
const selectedUserId = ref(null)

// 避免 filter 未初始化導致 periodName undefined
const filter = reactive({
  periodName: '',
  feeType: '',
  overdue: ''
})

const formatDate = (date) => {
  if (!date) return ''
  try {
    return new Date(date).toLocaleString()
  } catch {
    return String(date)
  }
}

const filteredInvoices = computed(() => {
  if (!Array.isArray(invoices.value)) return []

  return invoices.value.filter(inv => {
    // 篩選期別（模糊比對）
    const matchPeriod = filter.periodName === '' ||
      (inv.billingPeriod?.periodName || '').includes(filter.periodName)

    // 篩選費用類別（模糊比對）
    const matchFeeType = filter.feeType === '' ||
      (inv.feeType?.description || '').includes(filter.feeType)

    // 篩選逾期
    const matchOverdue =
      filter.overdue === '' ||
      (filter.overdue === 'true' && isOverdue(inv.deadline)) ||
      (filter.overdue === 'false' && !isOverdue(inv.deadline))

    return matchPeriod && matchFeeType && matchOverdue
  })
})

// 找出 paymentStatus 為 pending 且有回覆的所有 response
const filteredResponses = computed(() => {
  if (!Array.isArray(invoices.value)) return []
  // 只取 paymentStatus=pending 且有回覆的 invoice
  return invoices.value
    .filter(inv => inv.paymentStatus === 'pending' && Array.isArray(inv.invoiceResponses) && inv.invoiceResponses.length > 0)
    .flatMap(inv => inv.invoiceResponses.map(res => ({ ...res, invoiceId: inv.invoiceId, invoice })))
})

// 新增一個 allInvoiceResponses 計算所有回覆的 id 陣列
const allInvoiceResponses = computed(() => {
  if (!Array.isArray(invoices.value)) return []
  return invoices.value.flatMap(inv =>
    Array.isArray(inv.invoiceResponses) ? inv.invoiceResponses.map(res => res.invoiceResponseId) : []
  )
})

// checkedResponses, allChecked, toggleAll 保持原有邏輯
const checkedResponses = ref([])
const allChecked = ref(false)
function toggleAll() {
  if (allChecked.value) {
    checkedResponses.value = allInvoiceResponses.value.slice()
  } else {
    checkedResponses.value = []
  }
}

// 判斷是否逾期
function isOverdue(deadline) {
  if (!deadline) return false
  const now = new Date()
  const due = new Date(deadline)
  return due < now
}

onMounted(async () => {
  await reloadInvoices()
})

async function reloadInvoices() {
  // 查詢所有 paymentStatus='unpaid' 且有回覆的 invoice（新版API）
  const res = await axiosapi.get('/finance/invoice-responses/all', { params: { communityId: userStore.communityId } })
  console.log(res);
  invoices.value = res.data
  console.log("✅ 載入完成 filteredInvoices：", filteredInvoices.value)
}

function openReceiptModal(invoice, userId) {
  showReceiptModal.value = true
  selectedInvoice.value = invoice
  selectedUserId.value = userId
  receiptForm.invoiceId = invoice.invoiceId
  receiptForm.paymentMethod = ''
  receiptForm.paidAt = ''
  receiptForm.debitAt = ''
  receiptForm.amountPay = invoice.amountDue
  receiptForm.installments = ''
  receiptForm.note = ''
  receiptForm.status = false
  successMsg.value = ''
  errorMsg.value = ''
  receiptDetail.value = null
}

async function submitReceipt() {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    // 1. 新增收據
    const res = await axiosapi.post('/finance/receipts', { ...receiptForm, status: false })
    successMsg.value = '新增成功！'
    // 2. 取得收據詳細資料
    const receiptId = res.data.receiptId || res.data.id
    const detailRes = await axiosapi.get(`/finance/receipts/${receiptId}`)
    receiptDetail.value = detailRes.data
    // 3. 查社區名稱
    if (detailRes.data.communityId) {
      const commRes = await axiosapi.get(`/communitys/${detailRes.data.communityId}`)
      communityName.value = commRes.data.name
    } else {
      communityName.value = ''
    }
    // 4. 將 invoice 狀態設為 paid
    await axiosapi.put(`/finance/invoice/status/${receiptForm.invoiceId}?status=paid`)
    await reloadInvoices()
    showReceiptModal.value = false
    Swal.fire('收據已產生並設為已繳', '', 'success')
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
    Swal.fire('產生失敗', errorMsg.value, 'error')
  }
}

function printReceipt() {
  const printContent = receiptRef.value
  const win = window.open('', '', 'width=600,height=800')
  win.document.write('<html><head><title>列印收據</title>')
  win.document.write('<style>body{font-family:sans-serif;} .receipt-preview{max-width:420px;margin:0 auto;padding:24px;border:1.5px solid #aaa;border-radius:12px;} .receipt-title{text-align:center;font-size:1.5rem;font-weight:700;margin-bottom:18px;} .receipt-row{margin-bottom:8px;font-size:1.1rem;} .receipt-footer{margin-top:24px;text-align:right;font-size:1.1rem;} @media print{body{background:#fff;}}</style>')
  win.document.write('</head><body>')
  win.document.write(printContent.outerHTML)
  win.document.write('</body></html>')
  win.document.close()
  win.focus()
  setTimeout(() => { win.print(); win.close() }, 500)
}

function downloadPDF() {
  html2pdf(receiptRef.value, {
    margin: 10,
    filename: '收據.pdf',
    image: { type: 'jpeg', quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: 'mm', format: 'a5', orientation: 'portrait' }
  })
}

async function batchCreateReceipts() {
  if (!checkedResponses.value.length) return
  let successCount = 0
  let failCount = 0
  let failList = []
  for (const resId of checkedResponses.value) {
    // 找到對應的 invoiceId/userId
    let found = null
    for (const inv of invoices.value) {
      if (Array.isArray(inv.invoiceResponses)) {
        const res = inv.invoiceResponses.find(r => r.invoiceResponseId === resId)
        if (res) {
          found = { invoice: inv, response: res }
          break
        }
      }
    }
    if (!found) {
      failCount++
      failList.push({ resId, reason: '找不到對應資料' })
      continue
    }
    // 防呆：檢查 invoiceId 與 amountPay
    const invoiceId = found.invoice.invoiceId
    let amountPay = found.invoice.amountDue
    if (invoiceId == null) {
      failCount++
      failList.push({ resId, reason: 'invoiceId 為空' })
      continue
    }
    if (amountPay == null || isNaN(amountPay) || Number(amountPay) < 0) {
      failCount++
      failList.push({ resId, reason: '金額為空或負數' })
      continue
    }
    amountPay = Number(amountPay)
    // 組成收據資料
    const payload = {
      invoiceId,
      paymentMethod: found.response.paymentMethod || '',
      paidAt: found.response.paidAt || null,
      debitAt: found.response.debitAt || null,
      amountPay,
      installments: found.response.installments || '',
      note: found.response.lastResponse || ''
    }
    try {
      await axiosapi.post('/finance/receipts', payload)
      await axiosapi.put(`/finance/invoice/status/${invoiceId}?status=paid`)
      successCount++
    } catch (e) {
      failCount++
      failList.push({ resId, reason: e.response?.data?.message || e.message })
    }
  }
  await reloadInvoices()
  checkedResponses.value = []
  allChecked.value = false
  if (successCount > 0) {
    Swal.fire('成功', `成功產生 ${successCount} 筆收據`, 'success')
  }
  if (failCount > 0) {
    const msg = failList.map(f => `ID:${f.resId} ${f.reason}`).join('\n')
    Swal.fire('失敗', `有 ${failCount} 筆收據產生失敗\n${msg}`, 'error')
  }
}
</script>


<style scoped>
body,
.dark-bg {
  background: #181a1b;
  color: #e0e0e0;
}

.invoice-card {
  border: 1px solid #333;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 24px;
  background: #23272b;
  color: #e0e0e0;
}

.response-card {
  border: 1px solid #444;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 12px;
  background: #23272b;
  color: #e0e0e0;
}

.filter-bar {
  background: #23272b;
  border-radius: 6px;
  padding: 10px 16px;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  color: #e0e0e0;
}

input.form-control,
select.form-select {
  background: #181a1b;
  color: #e0e0e0;
  border: 1px solid #444;
}

input.form-control:focus,
select.form-select:focus {
  background: #23272b;
  color: #fff;
  border-color: #888;
}

.btn,
.btn:focus,
.btn:active {
  color: #e0e0e0;
  background: #23272b;
  border: 1px solid #444;
}

.btn-success {
  background: #198754;
  border-color: #198754;
}

.btn-outline-primary {
  color: #90caf9;
  border-color: #90caf9;
}

.btn-outline-primary:hover {
  background: #1976d2;
  color: #fff;
  border-color: #1976d2;
}

.alert-success {
  background: #223a29;
  color: #b9f6ca;
  border-color: #388e3c;
}

.alert-danger {
  background: #3a2222;
  color: #ff8a80;
  border-color: #d32f2f;
}

.badge.bg-danger {
  background: #d32f2f;
  color: #fff;
}

input[type="checkbox"].form-check-input {
  accent-color: #90caf9;
  background: #23272b;
  border: 1px solid #90caf9;
}

input[type="checkbox"].form-check-input:checked {
  background: #1976d2;
  border-color: #1976d2;
}

label,
h3,
h4,
span,
div {
  color: #e0e0e0;
}
</style>