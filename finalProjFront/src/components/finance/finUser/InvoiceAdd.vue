<template>
  <div class="container mt-4">
    <h3>新增發票</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">費用類型</label>
        <select v-model="selectedFeeType" @change="onFeeTypeChange" class="form-select" required>
          <option v-for="ft in feeTypes" :key="ft.feeTypeId" :value="ft">{{ ft.description }}</option>
        </select>
      </div>
      <div class="mb-3">
        <label class="form-label">單位</label>
        <input v-model="form.unit" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">每單位費用</label>
        <input v-model="form.unitPrice" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">用戶ID</label>
        <input v-model.number="form.usersId" type="number" class="form-control" @change="onUserIdChange" required />
      </div>
      <div class="mb-3">
        <label class="form-label">單位數</label>
        <input v-model="form.unitCount" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">應繳金額</label>
        <input v-model="form.amountDue" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">期別名稱</label>
        <input v-model="form.periodName" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">繳費截止日</label>
        <input v-model="form.deadline" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">付款方案</label>
        <input v-model="form.paymentPlan" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">總金額</label>
        <input v-model="form.totalAmount" class="form-control" disabled />
      </div>
      <div class="mb-3">
        <label class="form-label">繳費期別ID</label>
        <input v-model.number="form.billingPeriodId" type="number" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary">送出</button>
      <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'

const form = ref({
  feeTypeId: null,
  unit: '',
  unitPrice: '',
  usersId: null,
  unitCount: '',
  amountDue: '',
  periodName: '',
  deadline: '',
  paymentPlan: '',
  totalAmount: '',
  billingPeriodId: null,
})
const feeTypes = ref([])
const selectedFeeType = ref(null)
const successMsg = ref('')
const errorMsg = ref('')

const fetchFeeTypes = async () => {
  const res = await axios.get('/finance/fee-types')
  feeTypes.value = res.data
}

const onFeeTypeChange = async () => {
  if (selectedFeeType.value) {
    form.value.feeTypeId = selectedFeeType.value.feeTypeId
    form.value.unit = selectedFeeType.value.unit
    form.value.unitPrice = selectedFeeType.value.amountPerUnit
    await updateUnitCountAndAmountDue()
  }
}

const onUserIdChange = async () => {
  await updateUnitCountAndAmountDue()
}

const updateUnitCountAndAmountDue = async () => {
  if (form.value.usersId && form.value.feeTypeId) {
    try {
      const res = await axios.get('/finance/invoices/unit-count', {
        params: { usersId: form.value.usersId, feeTypeId: form.value.feeTypeId }
      })
      form.value.unitCount = res.data
      form.value.amountDue = (Number(form.value.unitCount) * Number(form.value.unitPrice)).toFixed(2)
      form.value.totalAmount = form.value.amountDue
    } catch (e) {
      form.value.unitCount = ''
      form.value.amountDue = ''
      form.value.totalAmount = ''
    }
  } else {
    form.value.unitCount = ''
    form.value.amountDue = ''
    form.value.totalAmount = ''
  }
}

onMounted(fetchFeeTypes)

watch(() => form.value.unitPrice, updateUnitCountAndAmountDue)

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post('/finance/invoices', form.value)
    successMsg.value = '新增成功！'
    Object.keys(form.value).forEach(k => form.value[k] = null)
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}
</script>

<style scoped>
.container { max-width: 500px; }
</style> 