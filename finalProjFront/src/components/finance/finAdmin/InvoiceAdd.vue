<template>
  <div class="container mt-4" style="max-width: 600px;">
    <h3 class="mb-4 fw-bold text-primary">新增繳款單</h3>
    <form @submit.prevent="submitForm" novalidate>

      <!-- 費用類型 -->
      <div class="mb-3">
        <label class="form-label">費用類型 <span class="text-danger">*</span></label>
        <select v-model="selectedDescription" @change="onDescriptionChange" class="form-select" required>
          <option disabled value="">請選擇費用類型</option>
          <option v-for="ft in feeTypes" :key="ft.feeTypeId" :value="ft.description">
            {{ ft.description }}
          </option>
        </select>
      </div>

      <!-- 單位 / 單價 -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label class="form-label">單位</label>
          <input v-model="form.unit" class="form-control" disabled />
        </div>
        <div class="col-md-6">
          <label class="form-label">每單位費用</label>
          <input v-model="form.unitPrice" class="form-control" disabled />
        </div>
      </div>

      <!-- 單位數 / 總金額 -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label class="form-label">單位數</label>
          <input v-model="form.unitCount" class="form-control" disabled />
        </div>
        <div class="col-md-6">
          <label class="form-label">應繳金額</label>
          <input v-model="form.amountDue" class="form-control" disabled />
        </div>
      </div>

      <!-- 繳費期別 -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label class="form-label">期別名稱</label>
          <input v-model="form.periodName" class="form-control" placeholder="例如：2025Q3" required />
        </div>
        <div class="col-md-6">
          <label class="form-label">期別 ID</label>
          <input v-model.number="form.billingPeriodId" class="form-control" type="number" required />
        </div>
      </div>

      <!-- 繳費截止日 -->
      <div class="mb-3">
        <label class="form-label">繳費截止日</label>
        <input v-model="form.deadline" type="datetime-local" class="form-control" required />
      </div>

      <!-- 付款方案 -->
      <div class="mb-3">
        <label class="form-label">付款方案</label>
        <input v-model="form.paymentPlan" class="form-control" placeholder="可選填，例如：分期繳款" />
      </div>

      <!-- 成功或錯誤提示 -->
      <div v-if="successMsg" class="alert alert-success">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>

      <button type="submit" class="btn btn-primary w-100">送出繳款單</button>
    </form>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import dayjs from 'dayjs'  // 如果你沒裝，可用 new Date().toISOString().slice(0,16) 代替

const form = ref({
  feeTypeId: null,
  unit: '',
  unitPrice: '',
  unitCount: '',
  amountDue: '',
  periodName: '',
  deadline: dayjs().add(14, 'day').format('YYYY-MM-DDTHH:mm'),  // 預設加14天
  paymentPlan: '',
  totalAmount: '',
  billingPeriodId: null,
})

const feeTypes = ref([])
const selectedDescription = ref('')
const successMsg = ref('')
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await axios.get('/finance/fee-types')
    feeTypes.value = res.data
  } catch (e) {
    errorMsg.value = '載入費用類別失敗：' + (e.response?.data?.message || e.message)
  }
})

const onDescriptionChange = async () => {
  const selected = feeTypes.value.find(ft => ft.description === selectedDescription.value)
  if (selected) {
    form.value.feeTypeId = selected.feeTypeId
    form.value.unit = selected.unit
    form.value.unitPrice = selected.amountPerUnit
    await updateUnitCountAndAmountDue()
  }
}

const updateUnitCountAndAmountDue = async () => {
  if (form.value.feeTypeId) {
    try {
      const res = await axios.get('/finance/invoice/unit-count', {
        params: { usersId: 0, feeTypeId: form.value.feeTypeId }  // 如果後端允許固定 usersId
      })
      form.value.unitCount = res.data
      const total = Number(form.value.unitCount) * Number(form.value.unitPrice)
      form.value.amountDue = total.toFixed(2)
      form.value.totalAmount = total.toFixed(2)
    } catch (e) {
      form.value.unitCount = ''
      form.value.amountDue = ''
      form.value.totalAmount = ''
    }
  }
}

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  if (!form.value.feeTypeId || !form.value.billingPeriodId || !form.value.periodName) {
    errorMsg.value = '請填寫所有必填欄位'
    return
  }
  try {
    await axios.post('/finance/invoice-generator/generate', form.value)
    successMsg.value = '新增成功！'
    Object.keys(form.value).forEach(k => form.value[k] = '')
    form.value.deadline = dayjs().add(14, 'day').format('YYYY-MM-DDTHH:mm')
    selectedDescription.value = ''
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}
</script>



<style scoped>
.container {
  max-width: 500px;
}
</style>