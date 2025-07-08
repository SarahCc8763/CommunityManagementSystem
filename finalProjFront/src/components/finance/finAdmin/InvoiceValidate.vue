<template>
  <div class="container mt-4">
    <h3 class="mb-4 fw-bold text-primary">請款單審核</h3>
    <!-- 查詢區塊 -->
    <div class="row mb-3 g-2 align-items-end flex-wrap">
      <div class="col-md-4 col-12">
        <label class="form-label mb-1">費用類型</label>
        <select v-model="filter.feeTypeId" class="form-select">
          <option value="">全部</option>
          <option v-for="ft in feeTypes" :key="ft.feeTypeId" :value="ft.feeTypeId">{{ ft.description }}</option>
        </select>
      </div>
      <div class="col-md-4 col-12">
        <label class="form-label mb-1">期別</label>
        <select v-model="filter.billingPeriodId" class="form-select">
          <option value="">全部</option>
          <option v-for="bp in billingPeriods" :key="bp.billingPeriodId" :value="bp.billingPeriodId">{{ bp.periodName }}
          </option>
        </select>
      </div>
      <div class="col-md-4 col-12 d-flex align-items-end">
        <button class="btn btn-outline-primary me-2" @click="clearFilter">清除</button>
      </div>
    </div>
    <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>
    <div v-if="successMsg" class="alert alert-success">{{ successMsg }}</div>
    <div v-if="filteredInvoices.length === 0" class="alert alert-info">目前沒有待審核的請款單</div>
    <form v-if="filteredInvoices.length > 0" @submit.prevent="batchValidate">
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
          <tr v-for="inv in filteredInvoices" :key="inv.invoiceId">
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
        <button type="button" class="btn btn-secondary ms-2" @click="checkedIds = []">取消全選</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axiosapi from '@/plugins/axios'

const pendingInvoices = ref([])
const checkedIds = ref([])
const allChecked = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

const feeTypes = ref([])
const billingPeriods = ref([])
const filter = ref({ feeTypeId: '', billingPeriodId: '' })

const filteredInvoices = computed(() => {
  return pendingInvoices.value.filter(inv => {
    let match = true
    if (filter.value.feeTypeId && inv.feeType?.feeTypeId != filter.value.feeTypeId) match = false
    if (filter.value.billingPeriodId && inv.billingPeriod?.billingPeriodId != filter.value.billingPeriodId) match = false
    return match
  })
})

onMounted(async () => {
  await fetchPending()
  try {
    const res1 = await axiosapi.get('/finance/fee-types')
    feeTypes.value = res1.data
    const res2 = await axiosapi.get('/finance/billing-periods')
    billingPeriods.value = res2.data
  } catch (e) {
    errorMsg.value = '載入選單失敗：' + (e.response?.data?.message || e.message)
  }
})

function clearFilter() {
  filter.value = { feeTypeId: '', billingPeriodId: '' }
}

async function fetchPending() {
  try {
    const res = await axiosapi.get('/finance/invoice/pending-validate')
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
    await axiosapi.post('/finance/invoice/batch-validate', checkedIds.value)
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
.table th,
.table td {
  text-align: center;
}
</style>