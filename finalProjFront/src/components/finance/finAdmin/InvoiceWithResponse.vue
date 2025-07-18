<template>
  <div style="width: 60vw; max-width: 1200px; margin-left: 2rem; margin-right: 2rem; margin: auto;">
    <!-- 麵包屑導航 -->
    <nav aria-label="breadcrumb" class="mb-3 ms-1">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('home')" class="text-decoration-none text-light">
            <i class="bi bi-house-door-fill me-1"></i>首頁
          </a>
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
    <BannerImage :imageSrc="bannerImg" heading="收款確認處理" subtext="確認住戶繳費狀況，審核收款記錄並為已繳費用戶開立收款證明。" textAlign="left" />
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

    <!-- 批次操作區塊 -->
    <div class="d-flex align-items-center mb-2" v-if="filteredInvoices.length > 0">
      <input type="checkbox" v-model="allChecked" @change="toggleAll" class="form-check-input me-2" />
      <span>全選</span>
      <button class="btn btn-success btn-sm ms-3" :disabled="checkedInvoices.length === 0" @click="batchCreateReceipts">
        一鍵產生收據
      </button>
    </div>

    <!-- 發票列表 -->
    <div v-for="inv in filteredInvoices" :key="inv.invoiceId" class="invoice-card mb-4">
      <div class="d-flex align-items-center mb-2">
        <input type="checkbox" class="form-check-input me-2" :value="inv.invoiceId" v-model="checkedInvoices" />
        <h3 class="mb-0">帳單資訊</h3>
        <button class="btn btn-outline-primary btn-sm ms-auto" @click="openReceiptModal(inv, inv.user?.usersId)">
          審核已付款
        </button>
      </div>

      <h4>發票 ID：{{ inv.invoiceId }} | 狀態：{{ inv.paymentStatus }}</h4>
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
              <div v-if="isOverdue(inv.deadline)">
                <span class="badge bg-danger">逾期</span>
              </div>
            </div>
          </div>
        </div>
        <div v-else>尚無回覆</div>
      </div>
    </div>

    <!-- 收據 Modal -->
    <div v-if="showReceiptModal" class="modal-overlay" @click.self="closeReceiptModal">
      <div class="dark-modal-card">
        <h4>產生收據</h4>
        <div class="receipt-info">
          <div>發票ID：{{ receiptForm.invoiceId }}</div>
          <div>金額：{{ receiptForm.amountPay }}</div>
          <div>住戶姓名：{{ selectedInvoice?.user?.name || '-' }}</div>
          <div>地址：{{ selectedInvoice?.user?.address || '-' }}</div>
          <div>費用類型：{{ selectedInvoice?.feeType?.description || '-' }}</div>
          <div>期別：{{ selectedInvoice?.billingPeriod?.periodName || '-' }}</div>
          <div>備註：{{ selectedInvoice?.note || '-' }}</div>
        </div>

        <!-- 成功/錯誤訊息 -->
        <div v-if="successMsg" class="alert alert-success mt-2">{{ successMsg }}</div>
        <div v-if="errorMsg" class="alert alert-danger mt-2">{{ errorMsg }}</div>

        <div class="modal-actions mt-3">
          <button class="btn btn-primary me-2" @click="submitReceipt">送出收據</button>
          <button class="btn btn-secondary" @click="closeReceiptModal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import BannerImage from '@/components/forAll/BannerImage.vue'
import bannerImg from '@/assets/images/main/adminBanner.jpg'
import axiosapi from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import { computed, onMounted, reactive, ref } from 'vue'

const userStore = useUserStore()
const router = useRouter()

// 響應式數據
const invoices = ref([])
const showReceiptModal = ref(false)
const selectedInvoice = ref(null)
const selectedUserId = ref(null)
const successMsg = ref('')
const errorMsg = ref('')
const checkedInvoices = ref([])
const allChecked = ref(false)

onMounted(async () => {
  await reloadInvoices()
})
// 篩選條件
const filter = reactive({
  periodName: '',
  feeType: '',
  overdue: ''
})

// 收據表單
const receiptForm = reactive({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  installments: '',
  note: '',
  status: false,
  users_id: null
})

// 計算屬性
const filteredInvoices = computed(() => {
  if (!Array.isArray(invoices.value)) return []

  return invoices.value.filter(inv => {
    const statusOk = ['unpaid', 'pending'].includes(inv.paymentStatus)
    const hasResponse = Array.isArray(inv.invoiceResponses) && inv.invoiceResponses.length > 0
    const matchPeriod = filter.periodName === '' || (inv.billingPeriod?.periodName || '').includes(filter.periodName)
    const matchFeeType = filter.feeType === '' || (inv.feeType?.description || '').includes(filter.feeType)
    const matchOverdue = filter.overdue === '' ||
      (filter.overdue === 'true' && isOverdue(inv.deadline)) ||
      (filter.overdue === 'false' && !isOverdue(inv.deadline))

    return statusOk && hasResponse && matchPeriod && matchFeeType && matchOverdue
  })
})

// 工具函數
const formatDate = (date) => {
  if (!date) return ''
  try {
    return new Date(date).toLocaleString()
  } catch {
    return String(date)
  }
}

const isOverdue = (deadline) => {
  if (!deadline) return false
  const now = new Date()
  const due = new Date(deadline)
  return due < now
}

// 篩選功能
const clearFilter = () => {
  filter.periodName = ''
  filter.feeType = ''
  filter.overdue = ''
}

// 全選功能
const toggleAll = () => {
  if (allChecked.value) {
    checkedInvoices.value = filteredInvoices.value.map(inv => inv.invoiceId)
  } else {
    checkedInvoices.value = []
  }
}

// 資料載入
const reloadInvoices = async () => {

  try {
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
  } catch (error) {
    console.error('載入發票資料失敗：', error)
    Swal.fire('載入失敗', '無法取得發票資料', 'error')
  }
}

// Modal 操作
const openReceiptModal = (invoice, userId) => {
  console.log('openReceiptModal 被呼叫', invoice, userId)
  console.log('invoice.user?.usersId:', invoice.user?.usersId)
  console.log('傳入的 userId:', userId)

  showReceiptModal.value = true
  selectedInvoice.value = invoice
  selectedUserId.value = userId

  // 重置表單，確保正確設定 users_id
  Object.assign(receiptForm, {
    invoiceId: invoice.invoiceId,
    paymentMethod: '',
    paidAt: '',
    debitAt: '',
    amountPay: invoice.amountDue,
    installments: '',
    note: '',
    status: false,
    users_id: invoice.user?.usersId || userId || null // 優先使用 invoice.user.usersId
  })

  // 清空訊息
  successMsg.value = ''
  errorMsg.value = ''

  console.log('設定的 receiptForm.users_id:', receiptForm.users_id)
  console.log('selectedInvoice.value:', selectedInvoice.value)
}

const closeReceiptModal = () => {
  showReceiptModal.value = false
  selectedInvoice.value = null
  selectedUserId.value = null
  errorMsg.value = ''
  successMsg.value = ''
}

// 提交收據
const submitReceipt = async () => {
  try {
    successMsg.value = ''
    errorMsg.value = ''

    // 取得該 invoice 的第一筆 response 的 lastResponseTime
    let paidAt = null
    if (selectedInvoice.value &&
      Array.isArray(selectedInvoice.value.invoiceResponses) &&
      selectedInvoice.value.invoiceResponses.length > 0) {
      paidAt = selectedInvoice.value.invoiceResponses[0].lastResponseTime || null
    }

    // 確保取得正確的 userId
    const userId = selectedInvoice.value?.user?.usersId || selectedUserId.value || null

    const payload = {
      users_id: userId,
      updated_by: userStore.userId,
      invoiceId: receiptForm.invoiceId,
      paymentMethod: receiptForm.paymentMethod,
      paidAt: paidAt,
      debitAt: new Date().toISOString(),
      amountPay: receiptForm.amountPay,
      installments: receiptForm.installments,
      note: receiptForm.note,
      createdBy: userStore.userId,
      createdAt: new Date().toISOString(),
      communityId: userStore.communityId,
    }

    console.log('submitReceipt payload', payload)
    console.log('使用的 userId:', userId)

    // 1. 新增收據
    const res = await axiosapi.post('/finance/receipts', payload)
    console.log('createReceipt 回傳', res.data)

    const receiptId = res.data.receiptId || res.data.id
    console.log('receiptId', receiptId)
    closeReceiptModal()
    if (!receiptId) {
      throw new Error('無法取得收據ID')
    }

    // 2. 將 invoice 狀態設為 paid
    await axiosapi.put(`/finance/invoice/status/${receiptForm.invoiceId}?status=paid`)

    // 3. 重新載入資料
    await reloadInvoices()

    // 4. 顯示成功訊息並關閉modal - 設定高 z-index 讓 SweetAlert 顯示在 modal 上方
    Swal.fire({
      title: '收據已產生並設為已繳',
      text: '',
      icon: 'success',
      zIndex: 99999, // 確保 SweetAlert 顯示在 modal 上方
      backdrop: false // 不要額外的背景遮罩
    })

  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message || '未知錯誤'
    errorMsg.value = '新增失敗：' + errorMessage

    // 錯誤訊息也要設定高 z-index
    Swal.fire({
      title: '產生失敗',
      text: errorMsg.value,
      icon: 'error',
      zIndex: 99999,
      backdrop: false
    })
  }
}

// 批次產生收據
const batchCreateReceipts = async () => {
  if (!checkedInvoices.value.length) return

  let successCount = 0
  let failCount = 0
  const failList = []

  for (const invId of checkedInvoices.value) {
    const invoice = invoices.value.find(inv => inv.invoiceId === invId)
    if (!invoice) {
      failCount++
      failList.push({ invId, reason: '找不到對應發票資料' })
      continue
    }

    const { invoiceId, amountDue } = invoice

    if (invoiceId == null) {
      failCount++
      failList.push({ invId, reason: 'invoiceId 為空' })
      continue
    }

    if (amountDue == null || isNaN(amountDue) || Number(amountDue) < 0) {
      failCount++
      failList.push({ invId, reason: '金額為空或負數' })
      continue
    }

    const payload = {
      invoiceId,
      paymentMethod: '',
      paidAt: null,
      debitAt: null,
      amountPay: Number(amountDue),
      installments: '',
      note: '',
      users_id: invoice.user?.usersId || null,
      createdBy: userStore.userId,
      communityId: userStore.communityId,
    }

    try {
      await axiosapi.post('/finance/receipts', payload)
      await axiosapi.put(`/finance/invoice/status/${invoiceId}?status=paid`)
      successCount++
    } catch (error) {
      failCount++
      failList.push({ invId, reason: error.response?.data?.message || error.message })
    }
  }

  // 重新載入資料並清空選擇
  await reloadInvoices()
  checkedInvoices.value = []
  allChecked.value = false

  // 顯示結果
  if (successCount > 0) {
    Swal.fire('成功', `成功產生 ${successCount} 筆收據`, 'success')
  }
  if (failCount > 0) {
    const msg = failList.map(f => `ID:${f.invId} ${f.reason}`).join('\n')
    Swal.fire('失敗', `有 ${failCount} 筆收據產生失敗\n${msg}`, 'error')
  }
}

// 導航功能


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






// 生命週期
</script>

<style scoped>
.center-wrapper {
  display: flex;
  flex-wrap: wrap;
  margin: 0 auto;
  justify-content: center;
  align-items: flex-start;
  width: 62%;
  gap: 24px;
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
  gap: 12px;
  margin-bottom: 0;
  align-items: center;
  background: #23272b;
  border-radius: 6px;
  padding: 10px 16px;
  color: #e0e0e0;
}

.d-flex.align-items-center.mb-2 {
  width: 100%;
  margin-top: 12px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.7);
}

.dark-modal-card {
  background: #23272b;
  color: #e0e0e0;
  padding: 32px;
  border-radius: 16px;
  min-width: 340px;
  max-width: 440px;
  box-shadow: 0 4px 32px rgba(0, 0, 0, 0.6);
  z-index: 10000;
  position: relative;
  /* 確保 modal 內容有正確的層級 */
}

/* 確保 SweetAlert 顯示在最上層 */
.swal2-container {
  z-index: 99999 !important;
}

.swal2-popup {
  z-index: 99999 !important;
}

.receipt-info {
  margin: 16px 0;
}

.receipt-info>div {
  margin-bottom: 8px;
}

.modal-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

/* 表單樣式 */
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

/* 按鈕樣式 */
.btn {
  color: #e0e0e0;
  background: #23272b;
  border: 1px solid #444;
  transition: all 0.2s;
}

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

.btn-success:hover {
  background: #157347;
  border-color: #157347;
}

.btn-outline-primary {
  color: #90caf9;
  border-color: #90caf9;
  background: transparent;
}

.btn-outline-primary:hover {
  background: #1976d2;
  color: #fff;
  border-color: #1976d2;
}

.btn-primary {
  background: #1976d2;
  border-color: #1976d2;
  color: #fff;
}

.btn-primary:hover {
  background: #1565c0;
  border-color: #1565c0;
}

.btn-secondary {
  background: #23272b;
  color: #b0b0b0;
  border: 1px solid #444;
}

.btn-secondary:hover {
  background: #181a1b;
  color: #fff;
}

/* 警告訊息 */
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

/* 複選框 */
input[type="checkbox"].form-check-input {
  accent-color: #90caf9;
  background: #23272b;
  border: 1px solid #90caf9;
}

input[type="checkbox"].form-check-input:checked {
  background: #1976d2;
  border-color: #1976d2;
}

/* 文字顏色 */
label,
h3,
h4,
h5,
span,
div {
  color: #e0e0e0;
}

/* 麵包屑 */
.breadcrumb-item+.breadcrumb-item::before {
  content: ">";
  color: #ccc;
  margin: 0 0.5rem;
}
</style>