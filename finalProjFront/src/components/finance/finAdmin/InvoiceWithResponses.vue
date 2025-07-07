<template>
  <div>
    <div v-if="invoices.length === 0">目前沒有待審核的帳單</div>
    <div v-else>
      <div v-for="inv in invoices" :key="inv.invoiceId" class="invoice-card mb-4">
        <h4>發票ID：{{ inv.invoiceId }}</h4>
        <div>住戶：{{ inv.users?.name }}</div>
        <div>金額：{{ inv.amountDue }}</div>
        <div>狀態：{{ inv.paymentStatus }}</div>
        <div>期別：{{ inv.periodName }}</div>
        <div>截止日：{{ formatDate(inv.deadline) }}</div>
        <div v-if="inv.invoiceResponses && inv.invoiceResponses.length > 0">
          <h5>留言紀錄：</h5>
          <div v-for="resp in inv.invoiceResponses" :key="resp.invoiceResponseId" class="response-card">
            <div>用戶ID：{{ resp.userId }}</div>
            <div>留言：{{ resp.lastResponse }}</div>
            <div>末五碼：{{ resp.accountCode }}</div>
            <div>回覆時間：{{ resp.lastResponseTime }}</div>
            <div>審核狀態：<span v-if="resp.verified">已審核</span><span v-else>未審核</span></div>
          </div>
        </div>
        <div v-else>尚無留言</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosapi from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
const userStore = useUserStore()
const invoices = ref([])

const formatDate = (date) => {
  if (!date) return ''
  try {
    return new Date(date).toLocaleString()
  } catch {
    return String(date)
  }
}

onMounted(async () => {
  // 查詢所有 paymentStatus='unpaid' 且有回覆的 invoice
  const res = await axiosapi.get('/api/finance/invoice/unpaid-with-response', { params: { communityId: userStore.communityId } })
  invoices.value = res.data
})
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