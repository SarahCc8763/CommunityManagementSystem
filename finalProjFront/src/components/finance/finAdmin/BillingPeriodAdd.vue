<template>
  <div class="container mt-4">
    <h3>新增繳費期別</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">期別名稱</label>
        <input v-model="form.periodName" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">期別代碼</label>
        <input v-model="form.periodCode" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">開始日期</label>
        <input v-model="form.startDate" type="date" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">結束日期</label>
        <input v-model="form.endDate" type="date" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">繳費截止日</label>
        <input v-model="form.dueDate" type="datetime-local" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.note" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">費用類型ID</label>
        <input v-model.number="form.feeTypeId" type="number" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">社區ID</label>
        <input v-model.number="form.communityId" type="number" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">狀態</label>
        <select v-model="form.status" class="form-select">
          <option :value="true">啟用</option>
          <option :value="false">停用</option>
        </select>
      </div>
      <div class="mb-3">
        <label class="form-label">建立者</label>
        <input v-model="form.createdBy" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">更新者ID</label>
        <input v-model.number="form.updatedBy" type="number" class="form-control" />
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
  periodName: '',
  periodCode: '',
  startDate: '',
  endDate: '',
  dueDate: '',
  note: '',
  feeTypeId: null,
  communityId: null,
  status: true,
  createdBy: '',
  updatedBy: null,
})

const successMsg = ref('')
const errorMsg = ref('')

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post('/finance/billing-periods/create', form.value)
    successMsg.value = '新增成功！'
    Object.keys(form.value).forEach(k => form.value[k] = (typeof form.value[k] === 'boolean' ? true : null))
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}
</script>

<style scoped>
.container { max-width: 500px; }
</style> 