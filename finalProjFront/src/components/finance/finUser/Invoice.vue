<template>
  <div class="invoice-container">
    <div class="tag-style px-4 py-2 mb-4">
      <h4 class="mb-0 fw-bold section-title">待繳帳單</h4>
    </div>

    <div v-if="invoices?.length === 0" class="no-invoice text-start my-5">
      <div class="fs-5 mb-3">目前沒有待繳帳單喔！</div>
      <div class="d-flex gap-3">
        <router-link to="/" class="btn btn-outline-primary">回首頁</router-link>
        <router-link to="/finUser" class="btn btn-outline-success">帳務總覽</router-link>
      </div>
    </div>
    <div v-else>
      <div class="invoice-list-bg">
        <div class="invoice-list grid-list">
          <div v-for="invoice in invoices" :key="invoice.invoiceId" class="invoice-card pro-card horizontal-card">
            <div class="card-left">
              <div class="pro-header">
                <div>
                  <span class="pro-id">#{{ invoice.invoiceId }}</span>
                  <span class="badge ms-2" :class="statusBadgeClass(invoice)">{{ statusText(invoice) }}</span>
                </div>
                <div class="pro-amount">
                  <span>NT$</span>
                  <span class="pro-amount-num">{{ invoice.amountDue.toLocaleString() }}</span>
                  <span v-if="isOverdue(invoice)" class="overdue-label">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" style="vertical-align:middle;">
                      <circle cx="12" cy="12" r="10" fill="#f87171" />
                      <path d="M12 7v5" stroke="#fff" stroke-width="2" stroke-linecap="round" />
                      <circle cx="12" cy="16" r="1.2" fill="#fff" />
                    </svg>
                    <span class="overdue-text">逾期</span>
                  </span>
                </div>
              </div>
              <div class="pro-info">
                <div><b>{{ invoice.feeType?.description || '—' }}</b>（{{ invoice.periodName }}）</div>
                <div>單位數：{{ invoice.unitCount }}　單價：NT$ {{ invoice.unitPrice.toLocaleString() }}</div>
                <div>截止日：<span class="pro-deadline">{{ formatDate(invoice.deadline) }}</span></div>
              </div>
            </div>
            <div class="card-right">
              <button v-if="invoice.paymentStatus === 'unpaid' || invoice.paymentStatus === 'pending'"
                class="pay-btn pro-pay-btn small-pay-btn" @click="openPayModal(invoice)">立即繳費</button>
            </div>
          </div>
        </div>
        <div class="total-row">
          <span>總金額：</span>
          <span class="total-amount">NT$ {{ totalAmount.toLocaleString() }}</span>
        </div>
      </div>
    </div>

    <!-- 付款 Modal -->
    <div v-if="showPayModal" class="modal-mask" @click.self="closePayModal">
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
              <span class="text-danger">提醒您於 {{ formatDate(currentInvoice.deadline) }} 前完成繳款，匯款後可於下方留言。</span>
            </div>
            <div class="mb-2">
              <label>匯款帳號末五碼（五位數字）</label>
              <input v-model="remitCode" class="form-control" maxlength="5" placeholder="請輸入五位數字" />
            </div>
            <div class="mb-2">
              <label>留言（可填其他資訊）</label>
              <input v-model="remitNote" class="form-control" placeholder="可選填" />
            </div>
            <div class="d-flex gap-2">
              <button class="btn btn-primary flex-fill" @click="submitRemit">送出回覆</button>
              <button class="btn btn-secondary flex-fill" @click="closePayModal">我知道了</button>
            </div>
          </div>
          <div v-else-if="payMethod === 'credit'">
            <div class="alert alert-success">即將導向綠界線上刷卡頁面（模擬）</div>
            <div class="mb-2">
              <label>留言（選填）</label>
              <input v-model="remitNote" class="form-control" />
            </div>
            <button class="btn btn-success w-100" @click="goCredit">前往刷卡</button>
            <button class="btn btn-primary w-100 mt-2" @click="submitRemit">送出回覆</button>
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
            <div class="d-flex gap-2">
              <button class="btn btn-primary flex-fill" @click="submitRemit">送出回覆</button>
              <button class="btn btn-secondary flex-fill" @click="closePayModal">我知道了</button>
            </div>
          </div>
          <div v-if="payMsg && payMsg !== 'success'"
            class="alert alert-info mt-2 d-flex justify-content-between align-items-center">
            <span>{{ payMsg }}</span>
            <button class="btn btn-sm btn-outline-secondary ms-2" @click="closePayModal">關閉</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axiosapi from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()

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
  try {
    return new Date(date).toLocaleString()
  } catch {
    return String(date)
  }
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
  if (!remitCode.value.trim() && !remitNote.value.trim()) {
    payMsg.value = '請輸入匯款帳號末五碼或留言（至少填一項）'
    return
  }

  if (remitCode.value && !/^\d{5}$/.test(remitCode.value)) {
    payMsg.value = '匯款帳號末五碼必須為五位數字'
    return
  }

  try {
    const payload = {
      invoice: {
        invoiceId: currentInvoice.invoiceId,
      },
      user: {
        usersId: userStore.userId,
      },
      communityId: userStore.communityId,
      createdBy: userStore.name,
      createdAt: new Date().toISOString(),
      paymentMethod: payMethod.value,
      lastResponse: remitNote.value,
      accountCode: remitCode.value
    }

    console.log(payload)
    await axiosapi.post(`/finance/invoice-responses`, payload)

    // ✅ SweetAlert 成功提示
    await Swal.fire({
      icon: 'success',
      title: '管理員已收到您的回覆',
      text: '會盡快審核，請耐心等候。',
      confirmButtonText: '關閉',
      customClass: {
        popup: 'swal-on-top'
      }
    })

    payMsg.value = '' // 清除訊息
  } catch (e) {
    payMsg.value = '送出失敗：' + (e.response?.data?.message || e.message)

    // ❌ SweetAlert 錯誤提示
    Swal.fire({
      icon: 'error',
      title: '發送失敗',
      text: payMsg.value,
      customClass: {
        popup: 'swal-on-top'
      }
    })
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

function isOverdue(invoice) {
  if (!invoice.deadline) return false
  const now = new Date()
  const deadline = new Date(invoice.deadline)
  return invoice.paymentStatus === 'unpaid' && deadline < now
}

// onMounted(fetch 未繳的Invoices)

onMounted(async () => {
  try {
    const res = await axiosapi.post('/finance/invoice/unpaid/by-user', { userId: userStore.userId })
    console.log('API 回傳資料', res.data)
    invoices.value = Array.isArray(res.data) ? res.data : []
    console.log(invoices.value);

  } catch (e) {
    invoices.value = []
  }
  console.log('userId', userStore.userId)
  console.log('invoices', invoices.value)
})

</script>

<style scoped>
/* 把SweetAlert放上面 */
.swal-on-top {
  z-index: 99999 !important;
}

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
  text-align: left;
  color: #718096;
  font-size: 1.2rem;
  margin-top: 60px;
  padding-left: 8px;
}

.no-invoice i {
  font-size: 2.2rem;
  color: #a0aec0;
  margin-bottom: 12px;
}

.invoice-list.grid-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px 24px;
}

.horizontal-card {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  max-width: 820px;
  min-width: 420px;
  width: 100%;
  margin: 0 auto;
  padding: 28px 32px 18px 32px;
  gap: 0 32px;
}

.card-left {
  flex: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
}

.card-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 120px;
}

@media (max-width: 900px) {
  .invoice-list-bg {
    padding: 24px 8px 16px 8px;
  }

  .invoice-list.grid-list {
    grid-template-columns: 1fr;
    gap: 24px 0;
  }

  .card-right {
    justify-content: flex-start;
    min-width: 0;
  }
}

.pro-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.10);
  border: 1.5px solid #e2e8f0;
  padding: 28px 22px 18px 22px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  transition: box-shadow 0.2s, border 0.2s;
  position: relative;
}

.pro-card:hover {
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.18);
  border: 1.5px solid #a5b4fc;
}

.pro-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 6px;
}

.pro-id {
  font-size: 1.08rem;
  font-weight: 700;
  color: #6366f1;
  letter-spacing: 1px;
}

.pro-amount {
  font-size: 1.18rem;
  font-weight: 700;
  color: #d53f8c;
  display: flex;
  align-items: center;
  gap: 4px;
}

.pro-amount-num {
  font-size: 1.45rem;
  font-weight: 900;
  color: #d53f8c;
  margin: 0 2px;
}

.pro-info {
  font-size: 1.08rem;
  color: #374151;
  line-height: 1.7;
  margin-bottom: 2px;
}

.pro-info b {
  color: #6366f1;
  font-weight: 700;
  font-size: 1.12rem;
}

.pro-deadline {
  color: #f59e42;
  font-weight: 700;
}

.pro-pay-btn {
  margin-top: 8px;
  font-size: 1.08rem;
  font-weight: 800;
  padding: 10px 0;
  border-radius: 10px;
  background: linear-gradient(90deg, #6366f1 0%, #d53f8c 100%);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.10);
  border: none;
  color: #fff;
  letter-spacing: 2px;
  transition: background 0.2s, transform 0.2s;
}

.pro-pay-btn:hover {
  background: linear-gradient(90deg, #4f46e5 0%, #be185d 100%);
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

.overdue-label {
  display: inline-flex;
  align-items: center;
  margin-left: 10px;
  font-weight: 700;
  color: #f87171;
  font-size: 1.05rem;
  background: #fff0f0;
  border-radius: 8px;
  padding: 2px 8px 2px 4px;
  box-shadow: 0 1px 4px rgba(248, 113, 113, 0.08);
}

.overdue-text {
  margin-left: 4px;
  color: #f87171;
  font-size: 1.05rem;
}

.small-pay-btn {
  font-size: 0.95rem;
  padding: 6px 18px;
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