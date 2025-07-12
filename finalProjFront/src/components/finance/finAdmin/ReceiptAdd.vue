<template>
  <div class="container mt-4 dark-bg">
    <h3>新增收據</h3>
    <!-- 搜尋區塊 -->
    <transition name="expand-search">
      <div class="search-bar mb-3" :class="{ 'expanded': showAdvanced }">
        <div class="row g-2 align-items-end">
          <div class="col-12 col-md-6 mb-2 mb-md-0">
            <input v-model="search.keyword" class="form-control main-search-input" placeholder="請輸入住戶姓名/ID/期別/費用類型" />
          </div>
          <div class="col-12 col-md-auto d-flex gap-2">
            <button class="btn btn-outline-primary" @click="toggleAdvanced">{{ showAdvanced ? '收合進階搜尋' : '進階搜尋' }}</button>
            <button class="btn btn-primary" @click="searchInvoices">查詢</button>
            <button class="btn btn-secondary" @click="clearSearch">清除</button>
          </div>
        </div>
        <transition name="fade">
          <div v-if="showAdvanced" class="advanced-search mt-3">
            <div class="row g-2">
              <div class="col-12 col-md-4">
                <input v-model="search.userName" class="form-control" placeholder="請輸入住戶姓名" />
              </div>
              <div class="col-12 col-md-4">
                <input v-model="search.userId" class="form-control" placeholder="請輸入住戶ID" />
              </div>
              <div class="col-12 col-md-4">
                <select v-model="search.feeType" class="form-select">
                  <option value="">全部費用類型</option>
                  <option v-for="type in feeTypes" :key="type.feeTypeId" :value="type.description">{{ type.description }}</option>
                </select>
              </div>
              <div class="col-12 col-md-4">
                <select v-model="search.periodName" class="form-select">
                  <option value="">全部期別</option>
                  <option v-for="period in periods" :key="period.billingPeriodId" :value="period.periodName">{{ period.periodName }}</option>
                </select>
              </div>
              <div class="col-12 col-md-4">
                <select v-model="search.status" class="form-select">
                  <option value="">全部狀態</option>
                  <option value="unpaid">未繳</option>
                  <option value="pending">待審核</option>
                </select>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </transition>
    <!-- 搜尋結果列表 -->
    <div v-if="searchResults.length > 0" class="search-results mb-4">
      <div class="result-card mb-3" v-for="inv in searchResults" :key="inv.invoiceId">
        <div class="row g-1 align-items-center">
          <div class="col-12 col-md-10">
            <div><b>發票ID：</b>{{ inv.invoiceId }}</div>
            <div><b>住戶：</b>{{ inv.user?.name }} (ID: {{ inv.user?.usersId }})</div>
            <div><b>期別：</b>{{ inv.billingPeriod?.periodName }}　<b>費用類型：</b>{{ inv.feeType?.description }}</div>
            <div><b>金額：</b>{{ inv.amountDue }}　<b>狀態：</b>{{ inv.paymentStatus }}</div>
          </div>
          <div class="col-12 col-md-2 text-end mt-2 mt-md-0">
            <button class="btn btn-outline-success btn-sm w-100" @click="openConfirmModal(inv)">產生收據</button>
          </div>
        </div>
      </div>
    </div>
    <!-- 產生收據確認 Modal -->
    <div v-if="showConfirmModal" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container dark-modal-card">
          <h4 class="mb-3">確認產生收據</h4>
          <div class="mb-2"><b>發票ID：</b>{{ confirmInvoice?.invoiceId }}</div>
          <div class="mb-2"><b>住戶：</b>{{ confirmInvoice?.user?.name }} (ID: {{ confirmInvoice?.user?.usersId }})</div>
          <div class="mb-2"><b>期別：</b>{{ confirmInvoice?.billingPeriod?.periodName }}</div>
          <div class="mb-2"><b>費用類型：</b>{{ confirmInvoice?.feeType?.description }}</div>
          <div class="mb-2"><b>金額：</b>{{ confirmInvoice?.amountDue }}</div>
          <div class="mb-2"><b>狀態：</b>{{ confirmInvoice?.paymentStatus }}</div>
          <div class="d-flex justify-content-end mt-4">
            <button class="btn btn-primary me-2" @click="createReceiptFromInvoice">確認產生</button>
            <button class="btn btn-secondary" @click="closeConfirmModal">取消</button>
          </div>
        </div>
      </div>
    </div>
    <!-- 收據表單 -->
    <form @submit.prevent="submitForm" class="dark-form">
      <div class="mb-3">
        <label class="form-label">發票ID</label>
        <input v-model.number="form.invoiceId" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">付款方式</label>
        <input v-model="form.paymentMethod" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">付款時間</label>
        <input v-model="form.paidAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">入帳時間</label>
        <input v-model="form.debitAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">實付金額</label>
        <input v-model.number="form.amountPay" type="number" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.note" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary w-100">送出</button>
      <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axiosapi from '@/plugins/axios'
import html2pdf from 'html2pdf.js'
import Swal from 'sweetalert2'

const route = useRoute()
const form = ref({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  installments: '',
  note: '',
})
const search = ref({
  keyword: '',
  userName: '',
  userId: '',
  feeType: '',
  periodName: '',
  status: '',
})
const showAdvanced = ref(false)
const searchResults = ref([])
const feeTypes = ref([])
const periods = ref([])
const successMsg = ref('')
const errorMsg = ref('')
const receiptDetail = ref(null)
const communityName = ref('')
const receiptRef = ref(null)
const showConfirmModal = ref(false)
const confirmInvoice = ref(null)

function toggleAdvanced() {
  showAdvanced.value = !showAdvanced.value
}
function clearSearch() {
  search.value = { keyword: '', userName: '', userId: '', feeType: '', periodName: '', status: '' }
  searchResults.value = []
}
async function searchInvoices() {
  // 依條件查詢未繳/待審核 invoice
  try {
    const params = {}
    if (search.value.keyword) params.keyword = search.value.keyword
    if (search.value.userName) params.userName = search.value.userName
    if (search.value.userId) params.userId = search.value.userId
    if (search.value.feeType) params.feeType = search.value.feeType
    if (search.value.periodName) params.periodName = search.value.periodName
    if (search.value.status) params.status = search.value.status
    // 修正 API 呼叫方式，使用 POST 並將 communityId 放在 body
    const res = await axiosapi.post('/finance/invoice/unpaid/by-community', { communityId: 1 })
    // 前端過濾
    let list = res.data
    if (search.value.keyword) {
      const kw = search.value.keyword.toLowerCase()
      list = list.filter(inv =>
        (inv.user?.name || '').toLowerCase().includes(kw) ||
        String(inv.user?.usersId || '').includes(kw) ||
        (inv.billingPeriod?.periodName || '').toLowerCase().includes(kw) ||
        (inv.feeType?.description || '').toLowerCase().includes(kw)
      )
    }
    if (search.value.userName) list = list.filter(inv => (inv.user?.name || '').includes(search.value.userName))
    if (search.value.userId) list = list.filter(inv => String(inv.user?.usersId || '').includes(search.value.userId))
    if (search.value.feeType) list = list.filter(inv => (inv.feeType?.description || '') === search.value.feeType)
    if (search.value.periodName) list = list.filter(inv => (inv.billingPeriod?.periodName || '') === search.value.periodName)
    if (search.value.status) list = list.filter(inv => inv.paymentStatus === search.value.status)
    searchResults.value = list
  } catch (e) {
    errorMsg.value = '查詢失敗：' + (e.response?.data?.message || e.message)
  }
}
function fillForm(inv) {
  form.value.invoiceId = inv.invoiceId
  form.value.amountPay = inv.amountDue
  // 其他欄位可根據需要自動帶入
}
function openConfirmModal(inv) {
  confirmInvoice.value = inv
  showConfirmModal.value = true
}
function closeConfirmModal() {
  showConfirmModal.value = false
  confirmInvoice.value = null
}
async function createReceiptFromInvoice() {
  try {
    const payload = {
      invoiceId: confirmInvoice.value.invoiceId,
      amountPay: confirmInvoice.value.amountDue,
      paymentMethod: '',
      paidAt: '',
      debitAt: '',
      note: ''
    }
    const res = await axiosapi.post('/finance/receipts', payload)
    showConfirmModal.value = false
    // 新增：產生收據後將 invoice 狀態設為 paid
    await axiosapi.put(`/finance/invoice/status/${confirmInvoice.value.invoiceId}?status=paid`)
    // 重新查詢列表
    await searchInvoices()
    // SweetAlert2 成功提示
    Swal.fire({
      icon: 'success',
      title: '收據已產生',
      text: '請至「查看收據」頁面審閱與列印收據',
      confirmButtonText: '知道了'
    })
  } catch (e) {
    errorMsg.value = '產生收據失敗：' + (e.response?.data?.message || e.message)
    showConfirmModal.value = false
  }
}
onMounted(async () => {
  // 若有query參數自動帶入
  if (route.query.invoiceId) form.value.invoiceId = Number(route.query.invoiceId)
  // 取得費用類型、期別下拉
  const feeRes = await axiosapi.get('/finance/fee-types')
  feeTypes.value = feeRes.data
  const periodRes = await axiosapi.get('/finance/billing-periods')
  periods.value = periodRes.data
})

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    const res = await axiosapi.post('/finance/receipts', form.value)
    successMsg.value = '新增成功！'
    // 取得收據詳細資料
    const receiptId = res.data.receiptId || res.data.id
    const detailRes = await axiosapi.get(`/finance/receipts/${receiptId}`)
    receiptDetail.value = detailRes.data
    // 查社區名稱
    if (detailRes.data.communityId) {
      const commRes = await axiosapi.get(`/communitys/${detailRes.data.communityId}`)
      communityName.value = commRes.data.name
    } else {
      communityName.value = ''
    }
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleString()
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
</script>

<style scoped>
.dark-bg {
  background: #181a1b;
  color: #e0e0e0;
}
.search-bar, .advanced-search {
  background: #23272b;
  border-radius: 18px;
  border: 1.5px solid #444;
  padding: 18px 22px 12px 22px;
  color: #e0e0e0;
  margin-bottom: 12px;
}
.form-control, .form-select {
  background: #181a1b;
  color: #e0e0e0;
  border: 1px solid #444;
}
.form-control:focus, .form-select:focus {
  background: #23272b;
  color: #fff;
  border-color: #888;
}
.btn, .btn:focus, .btn:active {
  color: #e0e0e0;
  background: #23272b;
  border: 1px solid #444;
}
.btn-primary {
  background: #1976d2;
  border-color: #1976d2;
  color: #fff;
}
.btn-primary:hover, .btn-primary:active {
  background: #1565c0;
  border-color: #1565c0;
  color: #fff;
}
.btn-outline-primary {
  color: #90caf9;
  border-color: #90caf9;
  background: transparent;
}
.btn-outline-primary:hover, .btn-outline-primary:active {
  background: #1976d2;
  color: #fff;
  border-color: #1976d2;
}
.btn-outline-success {
  color: #b9f6ca;
  border-color: #43a047;
  background: transparent;
}
.btn-outline-success:hover, .btn-outline-success:active {
  background: #388e3c;
  color: #fff;
  border-color: #388e3c;
}
.btn-secondary {
  background: #23272b;
  color: #b0b0b0;
  border: 1px solid #444;
}
.btn-secondary:hover, .btn-secondary:active {
  background: #181a1b;
  color: #fff;
}
.result-card {
  background: #23272b;
  color: #e0e0e0;
  border: 1px solid #444;
  border-radius: 8px;
  padding: 14px 18px;
  margin-bottom: 12px;
}
.dark-form label, .dark-form input, .dark-form select {
  color: #e0e0e0;
}
.container {
  max-width: 720px;
  background: none;
}

.receipt-preview {
  max-width: 420px;
  margin: 0 auto;
  padding: 24px 28px 18px 28px;
  border: 1.5px solid #aaa;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.08);
  font-family: 'Noto Sans TC', 'Microsoft JhengHei', sans-serif;
}

.receipt-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 18px;
  letter-spacing: 2px;
}

.receipt-row {
  margin-bottom: 8px;
  font-size: 1.1rem;
  letter-spacing: 1px;
}

.receipt-footer {
  margin-top: 24px;
  text-align: right;
  font-size: 1.1rem;
}

@media print {
  body * {
    visibility: hidden;
  }

  .receipt-preview,
  .receipt-preview * {
    visibility: visible !important;
  }

  .receipt-preview {
    position: absolute;
    left: 0;
    top: 0;
    width: 100vw;
    background: #fff;
    box-shadow: none;
  }
}
@media (max-width: 768px) {
  .search-bar, .advanced-search {
    padding: 12px 8px 8px 8px;
  }
  .result-card {
    padding: 10px 8px;
  }
}
.search-bar {
  background: #23272b;
  border-radius: 18px;
  border: 1.5px solid #444;
  color: #e0e0e0;
  margin-bottom: 12px;
  padding: 18px 22px 12px 22px;
  max-width: 100%;
  transition: max-height 0.4s cubic-bezier(0.4,0,0.2,1), box-shadow 0.3s, padding 0.3s;
  overflow: hidden;
  box-shadow: 0 2px 16px #0002;
}
.search-bar.expanded {
  padding-bottom: 32px;
  box-shadow: 0 4px 32px #0004;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.fade-enter-to, .fade-leave-from {
  opacity: 1;
}
.expand-search-enter-active, .expand-search-leave-active {
  transition: max-height 0.4s cubic-bezier(0.4,0,0.2,1), box-shadow 0.3s, padding 0.3s;
}
.expand-search-enter-from, .expand-search-leave-to {
  max-height: 80px;
}
.expand-search-enter-to, .expand-search-leave-from {
  max-height: 400px;
}
.advanced-search input.form-control::placeholder {
  color: #b0b0b0;
  opacity: 1;
  font-style: italic;
}
.modal-mask {
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-wrapper {
  min-width: 320px;
  max-width: 420px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.dark-modal-card {
  background: #23272b;
  color: #e0e0e0;
  padding: 32px;
  border-radius: 16px;
  min-width: 320px;
  max-width: 420px;
  box-shadow: 0 4px 32px #000a;
  z-index: 10000;
}
.main-search-input::placeholder {
  color: #b0b0b0;
  opacity: 1;
  font-style: italic;
}
</style>
