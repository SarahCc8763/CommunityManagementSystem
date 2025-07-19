<template>
  <div style="width: 60vw; max-width: 1200px; margin: 2rem auto 0;">
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
        <li class="breadcrumb-item active text-white" aria-current="page">收據管理中心</li>
      </ol>
    </nav>
    <BannerImage :imageSrc="OO" heading="收據管理中心" subtext="開立各類收據，快速查找特定繳費記錄及其處理狀態。" textAlign="left" />
  </div>

  <div class="container mt-4 dark-bg">
    <h3 class="mb-4">新增收據</h3>
    <div class="receipt-flex-wrap">
      <!-- 左側：搜尋區塊 -->
      <div class="receipt-panel search-panel">
        <div class="search-section">
          <div class="search-bar" :class="{ 'expanded': showAdvanced }">
            <!-- 搜尋欄位與按鈕 -->
            <div class="search-row">
              <input v-model="search.keyword" class="form-control main-search-input" placeholder="請輸入住戶姓名/ID/期別/費用類型" />
              <div class="search-btn-group">
                <button class="btn btn-outline-primary" @click="toggleAdvanced">
                  {{ showAdvanced ? '收合' : '進階' }}
                </button>
                <button class="btn btn-primary" @click="searchInvoices">查詢</button>
                <button class="btn btn-secondary" @click="clearSearch">清除</button>
              </div>
            </div>

            <!-- 進階搜尋 -->
            <transition name="fade">
              <div v-if="showAdvanced" class="advanced-search mt-3">
                <div class="row g-2">
                  <div class="col-12 col-md-6">
                    <input v-model="search.userName" class="form-control" placeholder="住戶姓名" />
                  </div>
                  <div class="col-12 col-md-6">
                    <input v-model="search.userId" class="form-control" placeholder="住戶ID" />
                  </div>
                  <div class="col-12 col-md-6">
                    <select v-model="search.feeType" class="form-select">
                      <option value="">全部費用類型</option>
                      <option v-for="type in feeTypes" :key="type.feeTypeId" :value="type.description">
                        {{ type.description }}
                      </option>
                    </select>
                  </div>
                  <div class="col-12 col-md-6">
                    <select v-model="search.periodName" class="form-select">
                      <option value="">全部期別</option>
                      <option v-for="period in periods" :key="period.billingPeriodId" :value="period.periodName">
                        {{ period.periodName }}
                      </option>
                    </select>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>

        <!-- 搜尋結果 -->
        <div class="search-results" v-if="searchResults.length > 0">
          <div class="result-card" v-for="inv in searchResults" :key="inv.invoiceId">
            <div class="result-info">
              <div><strong>發票ID：</strong>{{ inv.invoiceId }}</div>
              <div><strong>住戶：</strong>{{ inv.user?.name }} (ID: {{ inv.user?.usersId }})</div>
              <div><strong>期別：</strong>{{ inv.billingPeriod?.periodName }}</div>
              <div><strong>費用類型：</strong>{{ inv.feeType?.description }}</div>
              <div><strong>金額：</strong>{{ inv.amountDue }}</div>
              <div><strong>狀態：</strong>{{ inv.paymentStatus }}</div>
            </div>
            <div class="result-actions">
              <button class="btn btn-outline-success btn-sm" @click="openConfirmModal(inv)">
                產生收據
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="searchResults.length === 0 && hasSearched" class="no-results">
          <p class="text-muted">未找到符合條件的記錄</p>
        </div>
      </div>

      <!-- 右側：收據表單 -->
      <div class="receipt-panel">
        <form @submit.prevent="submitForm" class="receipt-form">
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">發票ID</label>
              <input v-model.number="form.invoiceId" type="number" class="form-control" required placeholder="ex:275" />
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">付款方式</label>
              <input v-model="form.paymentMethod" class="form-control" placeholder="ex:現金" />
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">付款時間</label>
              <input v-model="form.paidAt" type="datetime-local" class="form-control" />
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">入帳時間</label>
              <input v-model="form.debitAt" type="datetime-local" class="form-control" />
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">實付金額</label>
              <input v-model.number="form.amountPay" type="number" class="form-control" />
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">備註</label>
              <input v-model="form.note" class="form-control" />
            </div>
          </div>
          <button type="submit" class="btn btn-primary w-100">送出</button>
          <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
          <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
        </form>
      </div>
    </div>

    <!-- 確認模態框 -->
    <div v-if="showConfirmModal" class="modal-mask" @click="closeConfirmModal">
      <div class="modal-wrapper" @click.stop>
        <div class="dark-modal-card">
          <h5>確認產生收據</h5>
          <p>您確定要為以下發票產生收據嗎？</p>
          <div v-if="confirmInvoice" class="mb-3">
            <p><strong>發票ID：</strong>{{ confirmInvoice.invoiceId }}</p>
            <p><strong>住戶：</strong>{{ confirmInvoice.user?.name }}</p>
            <p><strong>金額：</strong>{{ confirmInvoice.amountDue }}</p>
          </div>
          <div class="d-flex gap-2">
            <button class="btn btn-primary" @click="createReceiptFromInvoice">確認</button>
            <button class="btn btn-secondary" @click="closeConfirmModal">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axiosapi from '@/plugins/axios'
import Swal from 'sweetalert2'
import BannerImage from '@/components/forAll/BannerImage.vue'
import OO from '@/assets/images/main/adminBanner.jpg'

const route = useRoute()
const router = useRouter()

// 表單數據
const form = ref({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  note: '',
})

// 搜尋數據
const search = ref({
  keyword: '',
  userName: '',
  userId: '',
  feeType: '',
  periodName: '',
})

// 狀態
const showAdvanced = ref(false)
const searchResults = ref([])
const hasSearched = ref(false)
const feeTypes = ref([])
const periods = ref([])
const successMsg = ref('')
const errorMsg = ref('')
const showConfirmModal = ref(false)
const confirmInvoice = ref(null)

// 方法
function toggleAdvanced() {
  showAdvanced.value = !showAdvanced.value
}

function clearSearch() {
  search.value = { keyword: '', userName: '', userId: '', feeType: '', periodName: '' }
  searchResults.value = []
  hasSearched.value = false
}

async function searchInvoices() {
  try {
    const res = await axiosapi.post('/finance/invoice/unpaid/by-community', { communityId: 1 })
    let list = res.data

    // 前端過濾
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

    searchResults.value = list
    hasSearched.value = true
  } catch (e) {
    errorMsg.value = '查詢失敗：' + (e.response?.data?.message || e.message)
  }
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

    await axiosapi.post('/finance/receipts', payload)
    await axiosapi.put(`/finance/invoice/status/${confirmInvoice.value.invoiceId}?status=paid`)

    showConfirmModal.value = false
    await searchInvoices()

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

async function submitForm() {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axiosapi.post('/finance/receipts', form.value)
    Swal.fire({
      icon: 'success',
      title: '收據已產生',
      text: '請至「查看收據」頁面審閱與列印收據',
      confirmButtonText: '知道了'
    })
    // 重置表單
    form.value = {
      invoiceId: null,
      paymentMethod: '',
      paidAt: '',
      debitAt: '',
      amountPay: null,
      note: '',
    }
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}

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

onMounted(async () => {
  if (route.query.invoiceId) {
    form.value.invoiceId = Number(route.query.invoiceId)
  }

  try {
    const [feeRes, periodRes] = await Promise.all([
      axiosapi.get('/finance/fee-types'),
      axiosapi.get('/finance/billing-periods')
    ])
    feeTypes.value = feeRes.data
    periods.value = periodRes.data
  } catch (e) {
    console.error('載入基礎數據失敗:', e)
  }
})
</script>

<style scoped>
.dark-bg {
  background: #181a1b;
  color: #e0e0e0;
}

.container {
  max-width: 100%;
  background: none;
}

.receipt-flex-wrap {
  display: flex;
  gap: 2rem;
  width: 60vw;
  max-width: 1200px;
  margin: 0 auto;
}

.receipt-panel {
  flex: 1;
  background: #23272b;
  border-radius: 18px;
  padding: 24px;
  border: 2px solid #3a3a3a;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.3);
  height: fit-content;
  max-height: 80vh;
  overflow-y: auto;
}

.search-panel {
  display: flex;
  flex-direction: column;
}

.search-section {
  flex-shrink: 0;
  margin-bottom: 1rem;
}

.search-bar {
  background: #2c3136;
  border-radius: 12px;
  border: 1px solid #444;
  padding: 18px;
  transition: all 0.3s ease;
}

.search-bar.expanded {
  border-color: #666;
}

.search-row {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.main-search-input {
  flex: 2;
  background: #181a1b;
  color: #e0e0e0;
  border: 1px solid #555;
  border-radius: 6px;
  padding: 8px 12px;
}

.search-btn-group {
  display: flex;
  gap: 0.5rem;
}

.search-btn-group .btn {
  padding: 8px 16px;
  font-size: 0.9rem;
  white-space: nowrap;
}

.advanced-search {
  padding-top: 1rem;
  border-top: 1px solid #444;
}

.search-results {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

.result-card {
  background: #2c3136;
  border: 1px solid #444;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.result-info {
  flex: 1;
}

.result-info div {
  margin-bottom: 4px;
  font-size: 0.9rem;
}

.result-actions {
  flex-shrink: 0;
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: #888;
}

.form-control,
.form-select {
  background: #181a1b;
  color: #e0e0e0;
  border: 1px solid #555;
}

.form-control:focus,
.form-select:focus {
  background: #23272b;
  color: #fff;
  border-color: #888;
  box-shadow: 0 0 0 0.2rem rgba(255, 255, 255, 0.1);
}

.btn {
  border-radius: 6px;
  font-weight: 500;
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

.btn-outline-success {
  color: #81c784;
  border-color: #4caf50;
  background: transparent;
}

.btn-outline-success:hover {
  background: #4caf50;
  color: #fff;
  border-color: #4caf50;
}

.btn-secondary {
  background: #424242;
  border-color: #424242;
  color: #e0e0e0;
}

.btn-secondary:hover {
  background: #616161;
  border-color: #616161;
}

.modal-mask {
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-wrapper {
  min-width: 320px;
  max-width: 420px;
  width: 90%;
}

.dark-modal-card {
  background: #23272b;
  color: #e0e0e0;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 4px 32px rgba(0, 0, 0, 0.6);
}

.breadcrumb-item+.breadcrumb-item::before {
  content: ">";
  color: #ccc;
  margin: 0 0.5rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.alert {
  border-radius: 8px;
  border: none;
}

.alert-success {
  background: #2e7d32;
  color: #fff;
}

.alert-danger {
  background: #c62828;
  color: #fff;
}

@media (max-width: 900px) {
  .receipt-flex-wrap {
    flex-direction: column;
    width: 90vw;
  }

  .search-row {
    flex-direction: column;
    gap: 0.5rem;
  }

  .search-btn-group {
    width: 100%;
    justify-content: space-between;
  }
}

input::placeholder,
textarea::placeholder,
select::placeholder {
  color: #999;
  /* ← 請替換你想要的顏色 */
  opacity: 1;
  /* 修正某些瀏覽器預設半透明 */
}
</style>