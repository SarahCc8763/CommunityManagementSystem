<template>
  <div class="invoice-container">
    <div class="tag-style px-4 py-2 mb-4">
      <h4 class="mb-0 fw-bold section-title">待繳帳單</h4>
    </div>

    <div v-if="invoices?.length === 0" class="no-invoice">
      <i class="bi bi-emoji-smile"></i> 目前沒有待繳帳單喔！
    </div>
    <div v-else class="invoice-list">
      <div v-for="invoice in invoices" :key="invoice.invoiceId" class="invoice-card">
        <div class="invoice-header">
          <div class="header-main">
            <span class="invoice-id">帳單編號：{{ invoice.invoiceId }}</span>
            <span class="badge ms-2" :class="statusBadgeClass(invoice)">{{ statusText(invoice) }}</span>
          </div>
          <div class="header-amount">
            <span class="amount-due">NT$ {{ invoice.amountDue.toLocaleString() }}</span>
          </div>
        </div>
        <div class="invoice-info-row">
          <div><span class="info-label">費用類型：</span>{{ invoice.feeType?.description || '—' }}</div>
          <div><span class="info-label">期別：</span>{{ invoice.periodName }}</div>
        </div>
        <div class="invoice-info-row">
          <div><span class="info-label">單位數：</span>{{ invoice.unitCount }}</div>
          <div><span class="info-label">單價：</span>NT$ {{ invoice.unitPrice.toLocaleString() }}</div>
        </div>
        <div class="invoice-info-row">
          <div><span class="info-label">繳費截止日：</span>{{ formatDate(invoice.deadline) }}</div>
        </div>
        <button v-if="invoice.paymentStatus === 'unpaid' || invoice.paymentStatus === 'pending'" class="pay-btn"
          @click="openPayModal(invoice)">去繳費</button>
      </div>
      <div class="total-row">
        <span>總金額：</span>
        <span class="total-amount">NT$ {{ totalAmount.toLocaleString() }}</span>
      </div>
    </div>

    <!-- 付款 Modal -->
    <div v-if="showPayModal" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <h4>選擇付款方式</h4>
          <div class="mb-3">
            <select v-model="payMethod" class="form-select">
              <option disabled value="">請選擇付款方式</option>
              <option value="remit">匯款</option>
              <option value="credit">線上刷卡</option>
              <option value="cash">現金</option>
            </select>
          </div>
          <div v-if="payMethod === 'remit'">
            <div class="alert alert-info mb-2">
              請匯款至：00銀行 123123123<br>
              <span class="text-danger">提醒您於 {{ formatDate(currentInvoice.deadline) }} 前完成繳款，匯款後請於下方提供帳號末五碼，以利對帳。</span>
            </div>
            <div v-if="payMsg === '請輸入正確的帳號末五碼'" class="text-danger mb-1" style="font-size:0.98rem;">
              {{ payMsg }}
            </div>
            <div class="mb-2">
              <label>帳號末五碼</label>
              <input v-model="remitCode" class="form-control" maxlength="5" />
            </div>
            <div class="mb-2">
              <label>備註（選填）</label>
              <input v-model="remitNote" class="form-control" />
            </div>
            <button class="btn btn-primary w-100" @click="submitRemit">送出匯款回覆</button>
          </div>
          <div v-else-if="payMethod === 'credit'">
            <div class="alert alert-success">即將導向綠界線上刷卡頁面（模擬）</div>
            <button class="btn btn-success w-100" @click="goCredit">前往刷卡</button>
          </div>
          <div v-else-if="payMethod === 'cash'">
            <div class="alert alert-warning">
              辦公室時間為 9:00~17:00，請於上班時間至管理室櫃檯繳費。<br>
              <span class="text-danger">提醒您於 {{ formatDate(currentInvoice.deadline) }} 前完成繳款，若已繳款，請在此回覆。</span>
            </div>
            <div class="mb-2">
              <label>留言</label>
              <input v-model="remitNote" class="form-control" />
            </div>
            <button class="btn btn-secondary w-100" @click="closePayModal">我知道了</button>
          </div>
          <!-- 這段記得要改成只收留言r -->
          <button class="btn btn-link mt-2 w-100" @click="submitRemit">送出回覆</button>
          <div v-if="payMsg && payMsg !== 'success'" class="alert alert-info mt-2">{{ payMsg }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'


const totalAmount = computed(() =>
  invoices.value.reduce((sum, inv) => sum + Number(inv.amountDue), 0)
)

const showPayModal = ref(false)
const payMethod = ref('')
const remitCode = ref('')
const remitNote = ref('')
const payMsg = ref('')
const invoices = ref([])
let currentInvoice = null

const formatDate = (date) => {
  if (!date) return ''
  if (typeof date === 'string') return date.replace('T', ' ').slice(0, 16)
  return date.toLocaleString()
}

const statusText = (invoice) => {
  if (invoice.paymentStatus === 'unpaid') return '未繳'
  if (invoice.paymentStatus === 'pending') return '審核中'
  if (invoice.paymentStatus === 'paid') return '已繳'
  return '—'
}
const statusBadgeClass = (invoice) => {
  if (invoice.paymentStatus === 'unpaid') return 'badge-primary'
  if (invoice.paymentStatus === 'pending') return 'badge-warning'
  if (invoice.paymentStatus === 'paid') return 'badge-success'
  return 'badge-secondary'
}

const openPayModal = (invoice) => {
  showPayModal.value = true
  payMethod.value = ''
  remitCode.value = ''
  remitNote.value = ''
  payMsg.value = ''
  currentInvoice = invoice
}
const closePayModal = () => {
  showPayModal.value = false
  payMsg.value = ''
}
const submitRemit = async () => {
  if (!remitCode.value.match(/^\d{5}$/)) {
    payMsg.value = '請輸入正確的帳號末五碼'
    return
  }
  try {
    // 模擬後端回覆
    await axios.post(`/finance/invoice-responses?userId=${currentInvoice.users.usersId}`, {
      invoiceId: currentInvoice.invoiceId,
      accountCode: remitCode.value,
      lastResponse: remitNote.value
    })
    currentInvoice.paymentStatus = 'pending'
    showPayModal.value = false
    Swal.fire({
      icon: 'success',
      title: '管理員已收到您的回覆',
      text: '會盡快審核，請耐心等候。',
      confirmButtonText: '確定',
      customClass: { popup: 'swal2-custom' }
    })
  } catch (e) {
    payMsg.value = '送出失敗：' + (e.response?.data?.message || e.message)
  }
}
const goCredit = () => {
  showPayModal.value = false
  Swal.fire({
    icon: 'success',
    title: '管理員已收到您的回覆',
    text: '會盡快審核，請耐心等候。',
    confirmButtonText: '確定',
    customClass: { popup: 'swal2-custom' }
  })
}
// onMounted(fetch 未繳的Invoices)

onMounted(async () => {
  try {
    const res = await axios.get('/finance/invoice/unpaid')
    invoices.value = res.data  // 正常就是一個陣列
  } catch (err) {
    console.error('載入帳單失敗:', err)
  }
})



</script>

<style scoped>
.invoice-container {
  max-width: 520px;
  margin: 48px auto 0 auto;
  padding: 32px 20px 24px 20px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.10);
  min-height: 400px;
}

.serif-title {
  font-family: 'Merriweather', serif;
  font-size: 2rem;
  font-weight: 800;
  color: #2d3748;
  margin-bottom: 32px;
  text-align: center;
}

.no-invoice {
  text-align: center;
  color: #718096;
  font-size: 1.2rem;
  margin-top: 60px;
}

.no-invoice i {
  font-size: 2.2rem;
  color: #a0aec0;
  margin-bottom: 12px;
}

.invoice-list {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.invoice-card {
  background: linear-gradient(135deg, #f8fafc 60%, #e9eafc 100%);
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.08);
  padding: 24px 20px 18px 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  border: 1.5px solid #e2e8f0;
  position: relative;
}

.invoice-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  font-size: 1.1rem;
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 8px;
  gap: 12px;
}

.header-main {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
}

.header-amount {
  font-size: 1.2rem;
  font-weight: 800;
  color: #667eea;
  align-self: flex-end;
}

.invoice-info-row {
  display: flex;
  flex-direction: row;
  gap: 32px;
  font-size: 1rem;
  color: #4a5568;
  margin-bottom: 4px;
}

.info-label {
  font-weight: 600;
  color: #667eea;
  margin-right: 4px;
}

.pay-btn {
  align-self: flex-end;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 10px 28px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.10);
  transition: background 0.2s, transform 0.2s;
}

.pay-btn:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
  transform: translateY(-2px) scale(1.04);
}

.total-row {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 12px;
  font-size: 1.2rem;
  font-weight: 800;
  color: #2d3748;
  border-top: 2px solid #e2e8f0;
  margin-top: 24px;
  padding-top: 16px;
}

.total-amount {
  color: #d53f8c;
  font-size: 1.4rem;
  font-weight: 900;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-wrapper {
  width: 100vw;
  max-width: 400px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 32px rgba(102, 126, 234, 0.18);
  padding: 24px 18px 18px 18px;
}

.modal-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 700;
  margin-left: 8px;
}

.badge-primary {
  background: #e0e7ff;
  color: #3b82f6;
}

.badge-warning {
  background: #fef3c7;
  color: #f59e42;
}

.badge-success {
  background: #d1fae5;
  color: #10b981;
}

.badge-secondary {
  background: #e5e7eb;
  color: #6b7280;
}

@media (max-width: 600px) {
  .invoice-container {
    max-width: 100vw;
    padding: 12px 2vw 16px 2vw;
    border-radius: 10px;
  }

  .serif-title {
    font-size: 1.3rem;
    margin-bottom: 18px;
  }

  .invoice-card {
    padding: 14px 8px 12px 8px;
    border-radius: 8px;
  }

  .invoice-details {
    gap: 12px;
    font-size: 0.95rem;
  }

  .pay-btn {
    padding: 8px 16px;
    font-size: 0.95rem;
    border-radius: 8px;
  }

  .total-row {
    font-size: 1rem;
    padding-top: 8px;
  }

  .total-amount {
    font-size: 1.1rem;
  }
}
</style>
