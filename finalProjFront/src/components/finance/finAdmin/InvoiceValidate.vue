<template>
  <div class="container mt-4">
    <h3 class="mb-4 fw-bold text-primary">請款單審核</h3>
    <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>
    <div v-if="successMsg" class="alert alert-success">{{ successMsg }}</div>
    <div v-if="pendingInvoices.length === 0" class="alert alert-info">目前沒有待審核的請款單</div>
    <form v-if="pendingInvoices.length > 0" @submit.prevent="batchValidate">
      <table class="table table-bordered align-middle">
        <thead>
          <tr>
            <th><input type="checkbox" v-model="allChecked" @change="toggleAll" /></th>
            <th>用戶ID</th>
            <th>費用類型</th>
            <th>期別</th>
            <th>單位數</th>
            <th>單價</th>
            <th>金額</th>
            <th>繳費截止日</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inv in pendingInvoices" :key="inv.invoiceId">
            <td><input type="checkbox" v-model="checkedIds" :value="inv.invoiceId" /></td>
            <td>{{ inv.users?.usersId }}</td>
            <td>{{ inv.feeType?.description }}</td>
            <td>{{ inv.billingPeriod?.periodName }}</td>
            <td>{{ inv.unitCount }}</td>
            <td>{{ inv.unitPrice }}</td>
            <td>{{ inv.amountDue }}</td>
            <td>{{ formatDate(inv.deadline) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="mb-3">
        <button type="submit" class="btn btn-success" :disabled="checkedIds.length === 0">審核通過</button>
        <button type="button" class="btn btn-secondary ms-2" @click="checkedIds=[]">取消全選</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const pendingInvoices = ref([])
const checkedIds = ref([])
const allChecked = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

onMounted(async () => {
  await fetchPending()
})

async function fetchPending() {
  try {
    const res = await axios.get('/finance/invoice/pending-validate')
    pendingInvoices.value = res.data
    checkedIds.value = []
    allChecked.value = false
  } catch (e) {
    errorMsg.value = '載入失敗：' + (e.response?.data?.message || e.message)
  }
}

function toggleAll() {
  if (allChecked.value) {
    checkedIds.value = pendingInvoices.value.map(inv => inv.invoiceId)
  } else {
    checkedIds.value = []
  }
}

async function batchValidate() {
  if (checkedIds.value.length === 0) return
  try {
    await axios.post('/finance/invoice/batch-validate', checkedIds.value)
    successMsg.value = '審核成功！'
    await fetchPending()
  } catch (e) {
    errorMsg.value = '審核失敗：' + (e.response?.data?.message || e.message)
  }
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString()
}
</script>

<style scoped>
.table th, .table td { text-align: center; }
</style> 