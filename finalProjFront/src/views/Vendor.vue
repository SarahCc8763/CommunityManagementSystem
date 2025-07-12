<template>
  <div class="container py-4">
    <h2 class="mb-4">🏢 廠商總覽</h2>

    <!-- 新增廠商表單 -->
    <div class="card p-4 mb-4 shadow-sm">
      <h5>➕ 新增廠商</h5>
      <div class="row">
        <div class="col-md-4 mb-2">
          <label class="form-label">廠商名稱</label>
          <input v-model="newVendor.vendorName" class="form-control" />
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">聯絡人</label>
          <input v-model="newVendor.contactPerson" class="form-control" />
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">電話</label>
          <input v-model="newVendor.phoneNumber" class="form-control" />
        </div>
        <div class="col-md-8 mb-2">
          <label class="form-label">地址</label>
          <input v-model="newVendor.address" class="form-control" />
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">備註</label>
          <input v-model="newVendor.note" class="form-control" />
        </div>
      </div>
      <div class="text-end">
        <button class="btn btn-primary" @click="createVendor">新增</button>
      </div>
    </div>

    <!-- 廠商列表 -->
    <div class="card p-3 shadow-sm">
      <h5>📋 廠商列表</h5>
      <table class="table table-bordered table-hover mt-3">
        <thead>
          <tr>
            <th>#</th>
            <th>名稱</th>
            <th>聯絡人</th>
            <th>電話</th>
            <th>地址</th>
            <th>備註</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vendor in vendors" :key="vendor.vendorID">
            <td>{{ vendor.vendorID }}</td>
            <td>
              <input v-model="vendor.vendorName" class="form-control form-control-sm" />
            </td>
            <td>
              <input v-model="vendor.contactPerson" class="form-control form-control-sm" />
            </td>
            <td>
              <input v-model="vendor.phoneNumber" class="form-control form-control-sm" />
            </td>
            <td>
              <input v-model="vendor.address" class="form-control form-control-sm" />
            </td>
            <td>
              <input v-model="vendor.notes" class="form-control form-control-sm" />
            </td>
            <td>
              <button class="btn btn-sm btn-success me-1" @click="updateVendor(vendor)">儲存</button>
              <button class="btn btn-sm btn-danger" @click="deleteVendor(vendor.vendorID)">刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios'

const vendors = ref([])
const newVendor = ref({
  vendorName: '',
  contactPerson: '',
  phoneNumber: '',
  address: '',
  note: null
})

async function fetchVendors() {
  try {
    const res = await axios.get('/vendors')
    vendors.value = res.data
  } catch (err) {
    console.error('❌ 載入廠商失敗', err)
  }
}

async function createVendor() {
  try {
    await axios.post('/vendors', newVendor.value)
    await fetchVendors()
    Object.assign(newVendor.value, {
      vendorName: '',
      contactPerson: '',
      phoneNumber: '',
      address: '',
      note: null
    })
  } catch (err) {
    console.error('❌ 新增廠商失敗', err)
  }
}

async function updateVendor(vendor) {
  try {
    await axios.put(`/vendors/${vendor.vendorID}`, vendor)
    await fetchVendors()
  } catch (err) {
    console.error('❌ 更新廠商失敗', err)
  }
}

async function deleteVendor(id) {
  try {
    await axios.delete(`/vendors/${id}`)
    await fetchVendors()
  } catch (err) {
    console.error('❌ 刪除廠商失敗', err)
  }
}

onMounted(() => {
  fetchVendors()
})
</script>

<style scoped>
th,
td {
  vertical-align: middle;
}
</style>
