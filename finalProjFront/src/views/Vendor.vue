<template>
  <div class="container py-4">
    <h2 class="mb-4 text-light">🏢 廠商總覽</h2>

    <!-- 🔹 新增廠商表單 -->
    <div class="card p-4 mb-4 shadow-sm text-dark">
      <h5>➕ 新增廠商</h5>
      <div class="row">
        <div class="col-md-4 mb-2">
          <label class="form-label">名稱</label>
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

    <!-- 🔹 廠商卡片列表 -->
    <div class="mt-4">
      <div
        class="vendor-card card mb-3 p-3 text-light bg-dark border-light"
        v-for="vendor in vendors"
        :key="vendor.vendorID"
        @click="toggleExpanded(vendor.vendorID)"
      >
        <!-- 🔸 基本資訊 -->
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <h5 class="mb-1">{{ vendor.vendorName }}</h5>
            <p class="mb-0">👤 {{ vendor.contactPerson }}　📞 {{ vendor.phoneNumber }}</p>
          </div>
          <span class="badge bg-secondary">#{{ vendor.vendorID }}</span>
        </div>

        <!-- 🔸 展開區塊 -->
        <div v-if="expandedId === vendor.vendorID" class="mt-3 border-top pt-3">
          <!-- 編輯中 -->
          <div v-if="editingId === vendor.vendorID" @click.stop>
            <div class="mb-2">
              <label class="form-label">地址</label>
              <input v-model="editableVendor.address" class="form-control" />
            </div>
            <div class="mb-2">
              <label class="form-label">備註</label>
              <input v-model="editableVendor.notes" class="form-control" />
            </div>
            <div class="text-end mt-3">
              <button class="btn custom-save-btn me-2" @click.stop="saveVendor(vendor.vendorID)">💾 儲存</button>
              <button class="btn custom-close-btn" @click.stop="cancelEdit">取消</button>
            </div>
          </div>
          <!-- 查看中 -->
          <div v-else>
            <p class="mb-1">🏠 地址：{{ vendor.address || '（無）' }}</p>
            <p class="mb-1">📝 備註：{{ vendor.notes || '（無）' }}</p>
            <div class="text-end mt-3">
              <button class="btn custom-edit-btn" @click.stop="startEditing(vendor)">✏️ 編輯</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios'

const vendors = ref([])
const expandedId = ref(null)
const editingId = ref(null)
const editableVendor = ref({})

const newVendor = ref({
  vendorName: '',
  contactPerson: '',
  phoneNumber: '',
  address: '',
  note: ''
})

// 🔄 讀取列表
async function fetchVendors() {
  try {
    const res = await axios.get('/vendors')
    vendors.value = res.data
  } catch (err) {
    console.error('❌ 載入失敗', err)
  }
}

// ➕ 新增廠商
async function createVendor() {
  try {
    await axios.post('/vendors', newVendor.value)
    await fetchVendors()
    Object.assign(newVendor.value, {
      vendorName: '',
      contactPerson: '',
      phoneNumber: '',
      address: '',
      note: ''
    })
  } catch (err) {
    console.error('❌ 新增失敗', err)
  }
}

// 展開或收合
function toggleExpanded(id) {
  expandedId.value = expandedId.value === id ? null : id
  editingId.value = null
}

// 開始編輯
function startEditing(vendor) {
  editingId.value = vendor.vendorID
  editableVendor.value = { ...vendor }
}

// 取消編輯
function cancelEdit() {
  editingId.value = null
  editableVendor.value = {}
}

// 儲存
async function saveVendor(id) {
  try {
    await axios.put(`/vendors/${id}`, editableVendor.value)
    editingId.value = null
    await fetchVendors()
  } catch (err) {
    console.error('❌ 儲存失敗', err)
  }
}

onMounted(fetchVendors)
</script>

<style scoped>
.vendor-card {
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 12px;
}
.vendor-card:hover {
  background-color: #1e1e1e;
}

/* 🔘 按鈕樣式 */
.custom-save-btn {
  background: linear-gradient(to right, #00c9a7, #007d77);
  color: white;
  border-radius: 20px;
  padding: 6px 16px;
  border: none;
}

.custom-close-btn {
  background-color: #2c2f36;
  color: white;
  border-radius: 20px;
  padding: 6px 16px;
  border: none;
}

.custom-edit-btn {
  background: linear-gradient(to right, #6a6ff2, #937efb);
  color: white;
  border-radius: 20px;
  padding: 6px 16px;
  border: none;
}
</style>
