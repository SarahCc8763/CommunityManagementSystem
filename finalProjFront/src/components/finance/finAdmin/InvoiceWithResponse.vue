<template>
  <div style="width: 60vw; max-width: 1200px; margin-left: 2rem; margin-right: 2rem; margin: auto;">
    <!-- 麵包屑導航 -->
    <nav aria-label="breadcrumb" class="mb-3 ms-1">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('home')" class="text-decoration-none text-light"><i
              class="bi bi-house-door-fill me-1"></i>首頁</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('adminDashboard')" class="text-decoration-none text-light">後台管理</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('finBack')" class="text-decoration-none text-light">財務後台</a>
        </li>
        <li class="breadcrumb-item active text-white" aria-current="page">繳費通知審核</li>
      </ol>
    </nav>
    <BannerImage :imageSrc="bannerImg" heading="收款確認處理" subtext=" 確認住戶繳費狀況，審核收款記錄並為已繳費用戶開立收款證明。" textAlign="left" />
  </div>
  <div class="center-wrapper">
    <!-- 查詢區塊 -->
    <div class="filter-bar mb-3">
      <label class="me-2">期別：</label>
      <input v-model="filter.periodName" class="form-control d-inline-block w-auto me-3" placeholder="ex:2025年10月" />
      <label class="me-2">費用類別：</label>
      <input v-model="filter.feeType" class="form-control d-inline-block w-auto me-3" placeholder="ex:清潔費" />
      <label class="me-2">逾期：</label>
      <select v-model="filter.overdue" class="form-select d-inline-block w-auto me-3">
        <option value="">全部</option>
        <option value="true">僅顯示逾期</option>
        <option value="false">僅顯示未逾期</option>
      </select>
      <button class="btn btn-outline-primary btn-sm" @click="clearFilter">清除</button>
    </div>

    <div class="d-flex align-items-center mb-2" v-if="filteredInvoices.length > 0">
      <input type="checkbox" v-model="allChecked" @change="toggleAll" class="form-check-input me-2" />
      <span>全選</span>
      <button class="btn btn-success btn-sm ms-3" :disabled="checkedInvoices.length === 0"
        @click="batchCreateReceipts">一鍵產生收據</button>
    </div>

    <div v-for="inv in filteredInvoices" :key="inv.invoiceId" class="invoice-card mb-4">
      <div class="d-flex align-items-center mb-2">
        <input type="checkbox" class="form-check-input me-2" :value="inv.invoiceId" v-model="checkedInvoices" />
        <h3 class="mb-0">帳單資訊</h3>
        <button class="btn btn-outline-primary btn-sm ms-auto"
          @click="openReceiptModal(inv, inv.user?.usersId)">審核已付款</button>
      </div>
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
            <div class="flex-grow-1">
              <div>留言：{{ res.lastResponse }}</div>
              <div>末五碼：{{ res.accountCode }}</div>
              <div>時間：{{ res.lastResponseTime }}</div>

              <div v-if="isOverdue(inv.deadline)"><span class="badge bg-danger">逾期</span></div>
            </div>
          </div>
        </div>
        <div v-else>尚無回覆</div>
      </div>
    </div>
    <!-- 收據 modal 區塊（兩階段：送出表單/正式收據卡片） -->
    <div v-if="showReceiptModal"
      style="position:fixed;top:0;left:0;width:100vw;height:100vh;z-index:9999;display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.7);">
      <div class="dark-modal-card">
        <template v-if="!receiptDetail">
          <h4>產生收據</h4>
          <div>發票ID：{{ receiptForm.invoiceId }}</div>
          <div>金額：{{ receiptForm.amountPay }}</div>
          <div>住戶姓名：{{ selectedInvoice.user?.name || '-' }}</div>
          <div>地址：{{ selectedInvoice.user?.address || '-' }}</div>
          <div>費用類型：{{ selectedInvoice.feeType?.description || '-' }}</div>
          <div>期別：{{ selectedInvoice.billingPeriod?.periodName || '-' }}</div>
          <div>備註：{{ selectedInvoice.note || '-' }}</div>
          <!-- 其餘表單內容可擴充 -->
          <button class="btn btn-primary me-2" @click="submitReceipt">送出收據</button>
          <button class="btn btn-secondary" @click="closeReceiptModal">取消</button>
        </template>
        <template v-else>
          <div class="receipt-title" style="text-align:center;font-size:1.5rem;font-weight:700;margin-bottom:18px;">{{
            communityName }} 收據</div>
          <div class="receipt-row"><span>收據號碼：</span>{{ receiptDetail.receiptNum || receiptDetail.receiptId }}</div>
          <div class="receipt-row"><span>住戶姓名：</span>{{ receiptDetail.userName }}</div>
          <div class="receipt-row"><span>地址：</span>{{ receiptDetail.address }}</div>
          <div class="receipt-row"><span>發票ID：</span>{{ receiptDetail.invoiceId }}</div>
          <div class="receipt-row"><span>費用類型：</span>{{ receiptDetail.feeType }}</div>
          <div class="receipt-row"><span>期別：</span>{{ receiptDetail.periodName }}</div>
          <div class="receipt-row"><span>實付金額：</span><b class="text-danger">NT$ {{
            receiptDetail.amountPay?.toLocaleString() }}</b></div>
          <div class="receipt-row"><span>付款方式：</span>{{ receiptDetail.paymentMethod }}</div>
          <div class="receipt-row"><span>付款時間：</span>{{ formatDate(receiptDetail.paidAt) }}</div>
          <div class="receipt-row"><span>扣款時間：</span>{{ formatDate(receiptDetail.debitAt) }}</div>
          <div class="receipt-row"><span>備註：</span>{{ receiptDetail.note }}</div>
          <div class="receipt-row"><span>經手人：</span>{{ receiptDetail.createdBy || '管理員' }}</div>
          <div class="receipt-footer" style="margin-top:24px;text-align:right;font-size:1.1rem;">收款日期：{{
            formatDate(receiptDetail.createdAt) }}　　收款人簽章：</div>
          <div class="mt-3 d-flex justify-content-end">
            <button class="btn btn-outline-primary me-2" @click="printReceipt">列印收據</button>
            <button class="btn btn-outline-success me-2" @click="downloadPDF">下載PDF</button>
            <button class="btn btn-secondary" @click="closeReceiptModal">關閉</button>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>

import BannerImage from '@/components/forAll/BannerImage.vue'
import bannerImg from '@/assets/images/main/adminBanner.jpg'

import axiosapi from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
import html2pdf from 'html2pdf.js'
import Swal from 'sweetalert2'
import { computed, onMounted, reactive, ref } from 'vue'
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
  // 只顯示 paymentStatus 為 unpaid 或 pending，且有回覆的 invoice
  return invoices.value.filter(inv => {
    const statusOk = ['unpaid', 'pending'].includes(inv.paymentStatus)
    const hasResponse = Array.isArray(inv.invoiceResponses) && inv.invoiceResponses.length > 0
    // 篩選期別（模糊比對）
    const matchPeriod = filter.periodName === '' || (inv.billingPeriod?.periodName || '').includes(filter.periodName)
    // 篩選費用類別（模糊比對）
    const matchFeeType = filter.feeType === '' || (inv.feeType?.description || '').includes(filter.feeType)
    // 篩選逾期
    const matchOverdue =
      filter.overdue === '' || (filter.overdue === 'true' && isOverdue(inv.deadline)) || (filter.overdue === 'false' && !isOverdue(inv.deadline))
    return statusOk && hasResponse && matchPeriod && matchFeeType && matchOverdue
  })
})

// checkedInvoices, allChecked, toggleAll 基於 invoice 操作
const checkedInvoices = ref([])
const allChecked = ref(false)
function toggleAll() {
  if (allChecked.value) {
    checkedInvoices.value = filteredInvoices.value.map(inv => inv.invoiceId)
  } else {
    checkedInvoices.value = []
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
  console.log('userStore.communityId =', userStore.communityId)
  const communityId = typeof userStore.communityId === 'object'
    ? userStore.communityId?.communityId
    : userStore.communityId

  const res = await axiosapi.get('/finance/invoice-responses/with-response/unpaid', {
    params: { communityId }
  })

  console.log('API 回應：', res.data)
  invoices.value = res.data
  console.log('載入完成，共', invoices.value?.length || 0, '筆資料')
}


function openReceiptModal(invoice, userId) {
  console.log('openReceiptModal 被呼叫', invoice, userId)
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
  receiptForm.users_id = invoice.user?.usersId || null
  successMsg.value = ''
  errorMsg.value = ''
  receiptDetail.value = null
  console.log(selectedInvoice.value);
  console.log('invoice.user =', invoice.user);
}

function closeReceiptModal() {
  showReceiptModal.value = false
  errorMsg.value = ''
  successMsg.value = ''
}

async function submitReceipt() {
  successMsg.value = ''
  errorMsg.value = ''
  // 取得該 invoice 的第一筆 response 的 lastResponseTime
  let paidAt = null
  if (selectedInvoice.value && Array.isArray(selectedInvoice.value.invoiceResponses) && selectedInvoice.value.invoiceResponses.length > 0) {
    paidAt = selectedInvoice.value.invoiceResponses[0].lastResponseTime || null
  }
  const payload = {
    users_id: receiptForm.users_id || null,
    updated_by: userStore.userId,
    invoiceId: receiptForm.invoiceId,
    paymentMethod: receiptForm.paymentMethod,
    paidAt: paidAt,
    debitAt: new Date().toISOString(),
    amountPay: receiptForm.amountPay,
    installments: receiptForm.installments,
    note: receiptForm.note,
    createdBy: userStore.userId,           // ✅ 管理員的 userId
    createdAt: new Date().toISOString(),
    communityId: userStore.communityId,
  }
  console.log('submitReceipt payload', payload)
  try {
    // 1. 新增收據
    const res = await axiosapi.post('/finance/receipts', payload)
    console.log('createReceipt 回傳', res.data)
    const receiptId = res.data.receiptId || res.data.id
    console.log('receiptId', receiptId)
    if (!receiptId) {
      Swal.fire('收據產生失敗', '無法取得收據ID', 'error')
      return
    }
    successMsg.value = '新增成功！'
    errorMsg.value = ''
    // 2. 取得收據詳細資料
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
    closeReceiptModal()
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
  if (!checkedInvoices.value.length) return
  let successCount = 0
  let failCount = 0
  let failList = []
  for (const invId of checkedInvoices.value) {
    const invoice = invoices.value.find(inv => inv.invoiceId === invId)
    if (!invoice) {
      failCount++
      failList.push({ invId, reason: '找不到對應發票資料' })
      continue
    }
    const invoiceId = invoice.invoiceId
    let amountPay = invoice.amountDue
    if (invoiceId == null) {
      failCount++
      failList.push({ invId, reason: 'invoiceId 為空' })
      continue
    }
    if (amountPay == null || isNaN(amountPay) || Number(amountPay) < 0) {
      failCount++
      failList.push({ invId, reason: '金額為空或負數' })
      continue
    }
    amountPay = Number(amountPay)
    const payload = {
      invoiceId,
      paymentMethod: '',
      paidAt: null,
      debitAt: null,
      amountPay,
      installments: '',
      note: ''
    }
    console.log('產生收據 payload', JSON.stringify(payload))
    try {
      await axiosapi.post('/finance/receipts', payload)
      await axiosapi.put(`/finance/invoice/status/${invoiceId}?status=paid`)
      successCount++
    } catch (e) {
      failCount++
      failList.push({ invId, reason: e.response?.data?.message || e.message })
    }
  }
  await reloadInvoices()
  checkedInvoices.value = []
  allChecked.value = false
  if (successCount > 0) {
    Swal.fire('成功', `成功產生 ${successCount} 筆收據`, 'success')
  }
  if (failCount > 0) {
    const msg = failList.map(f => `ID:${f.invId} ${f.reason}`).join('\n')
    Swal.fire('失敗', `有 ${failCount} 筆收據產生失敗\n${msg}`, 'error')
  }
}

// 麵包屑導航
import { useRouter } from 'vue-router'
const router = useRouter()
const goTo = (target) => {
  switch (target) {
    case 'home':
      router.push('/')
      break
    case 'adminDashboard':
      router.push('/AdminDashboard')
      break
    case 'finBack':
      router.push('/finance/admin-dashboard')
      break
  }
}
</script>


<style scoped>
.center-wrapper {
  display: flex;
  flex-wrap: wrap;
  margin: 0 auto;
  justify-content: center;
  /* ✅ 將內容物整體置中 */
  align-items: flex-start;
  width: 62%;
  /* ❗讓整個容器填滿畫面寬度 */
  gap: 24px;
  /* 卡片間距 */
  padding: 0 12px;
  box-sizing: border-box;
}



body,
.dark-bg {
  background: #181a1b;
  color: #e0e0e0;
}

.invoice-card {
  width: 48%;
  box-sizing: border-box;
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
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  /* ✅ 讓內容換行 */
  gap: 12px;
  margin-bottom: 0;
  /* 自己控制 margin，不交給 Bootstrap 的 mb-3 */
  align-items: center;
  background: #23272b;
  border-radius: 6px;
  padding: 10px 16px;
  color: #e0e0e0;
}

.d-flex.align-items-center.mb-2 {
  width: 100%;
  margin-top: 12px;
  /* ✅ 替代剛剛 filter-bar 的 margin-bottom */
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

input::placeholder {
  color: #808080;

  opacity: 1;

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

.dark-modal-card {
  background: #23272b;
  color: #e0e0e0;
  padding: 32px;
  border-radius: 16px;
  min-width: 340px;
  max-width: 440px;
  box-shadow: 0 4px 32px #000a;
  z-index: 10000;
}

.dark-modal-card h4 {
  color: #90caf9;
}

.dark-modal-card .btn {
  background: #23272b;
  color: #e0e0e0;
  border: 1px solid #444;
  transition: background 0.2s, color 0.2s;
}

.dark-modal-card .btn-primary {
  background: #1976d2;
  border-color: #1976d2;
  color: #fff;
}

.dark-modal-card .btn-primary:hover,
.dark-modal-card .btn-primary:active {
  background: #1565c0;
  border-color: #1565c0;
  color: #fff;
}

.dark-modal-card .btn-secondary {
  background: #23272b;
  color: #b0b0b0;
  border: 1px solid #444;
}

.dark-modal-card .btn-secondary:hover,
.dark-modal-card .btn-secondary:active {
  background: #181a1b;
  color: #fff;
}

.dark-modal-card .btn-outline-primary {
  color: #90caf9;
  border-color: #90caf9;
  background: transparent;
}

.dark-modal-card .btn-outline-primary:hover,
.dark-modal-card .btn-outline-primary:active {
  background: #1976d2;
  color: #fff;
  border-color: #1976d2;
}

.dark-modal-card .btn-outline-success {
  color: #b9f6ca;
  border-color: #43a047;
  background: transparent;
}

.dark-modal-card .btn-outline-success:hover,
.dark-modal-card .btn-outline-success:active {
  background: #388e3c;
  color: #fff;
  border-color: #388e3c;
}


/* 麵包屑 */
.breadcrumb-item+.breadcrumb-item::before {
  content: ">";
  color: #ccc;
  /* 或 text-light 用於深色背景 */
  margin: 0 0.5rem;
}
</style>