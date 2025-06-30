<template>
  <div class="container mt-4">
    <h3>新增費用類型</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">費用代碼</label>
        <input v-model="form.feeCode" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">名稱/描述</label>
        <input v-model="form.description" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">單價</label>
        <input v-model.number="form.amountPerUnit" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">單位</label>
        <input v-model="form.unit" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">頻率</label>
        <input v-model="form.frequency" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.note" class="form-control" />
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
        <label class="form-label">建立者ID</label>
        <input v-model.number="form.createdBy" type="number" class="form-control" />
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
  feeCode: '',
  description: '',
  amountPerUnit: null,
  unit: '',
  frequency: '',
  note: '',
  communityId: null,
  status: true,
  createdBy: null,
  updatedBy: null,
})

const successMsg = ref('')
const errorMsg = ref('')

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    await axios.post('/finance/fee-types', form.value)
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