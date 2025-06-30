<template>
  <div class="container mt-4">
    <h3>新增收據</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">發票ID</label>
        <input v-model.number="form.invoiceId" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">付款方式</label>
        <input v-model="form.paymentMethod" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">付款時間</label>
        <input v-model="form.paidAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">扣款時間</label>
        <input v-model="form.debitAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">實付金額</label>
        <input v-model.number="form.amountPay" type="number" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">分期資訊</label>
        <input v-model="form.installments" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.note" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary">送出</button>
      <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const form = ref({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  installments: '',
  note: '',
})

const successMsg = ref('')
const errorMsg = ref('')

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post('/finance/receipts', form.value)
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