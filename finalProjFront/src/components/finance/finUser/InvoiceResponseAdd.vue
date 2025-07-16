<template>
  <div class="container mt-4">
    <h3>發票回覆（匯款回報）</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">發票ID</label>
        <input v-model.number="form.invoiceId" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">用戶ID</label>
        <input v-model.number="form.userId" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">帳號末五碼</label>
        <input v-model="form.accountCode" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.lastResponse" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary">送出</button>
      <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/plugins/axios'

const form = ref({
  invoiceId: null,
  userId: null,
  accountCode: '',
  lastResponse: '',
})

const successMsg = ref('')
const errorMsg = ref('')

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post(`/finance/invoice-responses?userId=${form.value.userId}`, form.value)
    successMsg.value = '回覆成功！'
    Object.keys(form.value).forEach(k => form.value[k] = null)
  } catch (e) {
    errorMsg.value = '回覆失敗：' + (e.response?.data?.message || e.message)
  }
}
</script>

<style scoped>
.container {
  max-width: 500px;
}
</style>