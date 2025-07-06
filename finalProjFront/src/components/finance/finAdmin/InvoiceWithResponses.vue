<template>
  <div>
    <div v-if="invoice" class="invoice-card">
      <h3>發票資訊</h3>
      <div>發票ID：{{ invoice.invoiceId }}</div>
      <div>住戶：{{ invoice.user?.name }}</div>
      <div>費用類型：{{ invoice.feeType?.description }}</div>
      <div>期別：{{ invoice.billingPeriod?.periodName }}</div>
      <div>金額：{{ invoice.amountDue }}</div>
      <div>截止日：{{ invoice.deadline }}</div>
      <div>狀態：{{ invoice.paymentStatus }}</div>
      <div>備註：{{ invoice.note }}</div>
    </div>
    <div v-else>載入中...</div>

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

    <div v-if="filteredResponses.length > 0">
      <div class="d-flex align-items-center mb-2">
        <input type="checkbox" v-model="allChecked" @change="toggleAll" class="form-check-input me-2" />
        <span>全選</span>
        <button class="btn btn-success btn-sm ms-3" :disabled="checkedResponses.length===0" @click="batchCreateReceipts">一鍵產生收據</button>
      </div>
      <h4>回覆紀錄</h4>
      <div v-for="response in filteredResponses" :key="response.invoiceResponseId" class="response-card d-flex align-items-center">
        <input type="checkbox" class="form-check-input me-2" :value="response.invoiceResponseId" v-model="checkedResponses" />
        <div class="flex-grow-1">
          <div>用戶ID：{{ response.userId }}</div>
          <div>留言：{{ response.lastResponse }}</div>
          <div>末五碼：{{ response.accountCode }}</div>
          <div>回覆時間：{{ response.lastResponseTime }}</div>
          <div>審核狀態：<span v-if="response.verified">已審核</span><span v-else>未審核</span></div>
          <div v-if="isOverdue(invoice.deadline)"><span class="badge bg-danger">逾期</span></div>
        </div>
        <button class="btn btn-outline-primary btn-sm ms-2" @click="goToReceiptAdd(invoice.invoiceId, response.userId)">審核已付款</button>
      </div>
    </div>
    <div v-else>尚無回覆紀錄</div>
    <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
    <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
  </div>
</template>

<script setup>
// 管理員查看單一發票及所有回覆元件
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
const route = useRoute()
const router = useRouter()
const invoice = ref(null)
const responses = ref([])
const checkedResponses = ref([])
const successMsg = ref('')
const errorMsg = ref('')
const filter = ref({ periodName: '', feeType: '', overdue: '' })

const allChecked = computed({
  get: () => checkedResponses.value.length === filteredResponses.value.length && filteredResponses.value.length > 0,
  set: val => {
    checkedResponses.value = val ? filteredResponses.value.map(r => r.invoiceResponseId) : []
  }
})

onMounted(async () => {
  const invoiceId = route.params.invoiceId
  // 取得發票詳細資料
  invoice.value = await fetch(`/finance/invoice/${invoiceId}`).then(r => r.json())
  // 取得該發票所有回覆
  responses.value = await fetch(`/finance/invoice-responses/by-invoice?invoiceId=${invoiceId}`).then(r => r.json())
})

const filteredResponses = computed(() => {
  return responses.value.filter(r => {
    let match = true
    if (filter.value.periodName && invoice.value?.billingPeriod?.periodName) {
      match = match && invoice.value.billingPeriod.periodName.includes(filter.value.periodName)
    }
    if (filter.value.feeType && invoice.value?.feeType?.description) {
      match = match && invoice.value.feeType.description.includes(filter.value.feeType)
    }
    if (filter.value.overdue !== '') {
      const overdue = isOverdue(invoice.value.deadline)
      match = match && ((filter.value.overdue === 'true' && overdue) || (filter.value.overdue === 'false' && !overdue))
    }
    return match
  })
})

function isOverdue(deadline) {
  if (!deadline) return false
  return new Date(deadline) < new Date()
}

function goToReceiptAdd(invoiceId, userId) {
  // 跳轉到ReceiptAdd，帶入invoiceId與userId
  router.push({ name: 'ReceiptAdd', query: { invoiceId, userId } })
}

function toggleAll() {
  allChecked.value = !allChecked.value
}

async function batchCreateReceipts() {
  successMsg.value = ''
  errorMsg.value = ''
  const selected = filteredResponses.value.filter(r => checkedResponses.value.includes(r.invoiceResponseId))
  if (selected.length === 0) return
  try {
    await Promise.all(selected.map(r =>
      axios.post('/finance/receipts', {
        invoiceId: r.invoiceId,
        amountPay: invoice.value.amountDue,
        paymentMethod: '匯款',
        paidAt: new Date().toISOString(),
        debitAt: new Date().toISOString(),
        installments: '',
        note: '系統自動產生'
      })
    ))
    successMsg.value = '已批次產生收據！'
    checkedResponses.value = []
  } catch (e) {
    errorMsg.value = '產生收據失敗：' + (e.response?.data?.message || e.message)
  }
}

function clearFilter() {
  filter.value = { periodName: '', feeType: '', overdue: '' }
}
</script>

<style scoped>
body, .dark-bg {
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
input.form-control, select.form-select {
  background: #181a1b;
  color: #e0e0e0;
  border: 1px solid #444;
}
input.form-control:focus, select.form-select:focus {
  background: #23272b;
  color: #fff;
  border-color: #888;
}
.btn, .btn:focus, .btn:active {
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
label, h3, h4, span, div {
  color: #e0e0e0;
}
</style> 