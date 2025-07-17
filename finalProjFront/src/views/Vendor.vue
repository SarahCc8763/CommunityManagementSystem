<template>
  <div class="container py-4">
    <h2 class="mb-4 text-light">🏢 廠商總覽</h2>

    <!-- 🔹 新增廠商表單 -->
    <div class="card p-4 mb-4 shadow-sm bg-dark text-light">
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

    <!-- 🔹 廠商卡片列表與分頁 -->
    <div class="vendor-list-wrapper position-relative d-flex mt-4">
      <div class="flex-grow-1">
        <div
          class="vendor-card card mb-3 p-3 text-light bg-dark border-light"
          v-for="vendor in paginatedVendors"
          :key="vendor.vendorID"
          @click="toggleExpanded(vendor.vendorID)"
        >
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <h5 class="mb-1">{{ vendor.vendorName }}</h5>
              <p class="mb-0">👤 {{ vendor.contactPerson }}　📞 {{ vendor.phoneNumber }}</p>
            </div>
            <span class="badge bg-secondary">#{{ vendor.vendorID }}</span>
          </div>

          <div v-if="expandedId === vendor.vendorID" class="mt-3 border-top pt-3">
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

      <div class="vertical-pagination">
        <button
          v-for="page in totalPages"
          :key="page"
          class="page-btn"
          :class="{ active: currentPage === page }"
          @click="goToPage(page)"
        >
          {{ page }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from '@/plugins/axios'

const vendors = ref([])
const expandedId = ref(null)
const editingId = ref(null)
const editableVendor = ref({})

const currentPage = ref(1)
const pageSize = 3

const paginatedVendors = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return vendors.value.slice(start, start + pageSize)
})

const totalPages = computed(() => {
  return Math.ceil(vendors.value.length / pageSize)
})

function goToPage(page) {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const newVendor = ref({
  vendorName: '',
  contactPerson: '',
  phoneNumber: '',
  address: '',
  note: ''
})

async function fetchVendors() {
  try {
    const res = await axios.get('/vendors')
    vendors.value = res.data
  } catch (err) {
    console.error('❌ 載入失敗', err)
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
      note: ''
    })
  } catch (err) {
    console.error('❌ 新增失敗', err)
  }
}

function toggleExpanded(id) {
  expandedId.value = expandedId.value === id ? null : id
  editingId.value = null
}

function startEditing(vendor) {
  editingId.value = vendor.vendorID
  editableVendor.value = { ...vendor }
}

function cancelEdit() {
  editingId.value = null
  editableVendor.value = {}
}

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

.vendor-list-wrapper {
  position: relative;
  display: flex;
}

.vertical-pagination {
  position: absolute;
  top: 0;
  right: -60px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.page-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  font-weight: bold;
  background-color: #2c2f36;
  color: white;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
}

.page-btn.active {
  background-color: #4e6ef2;
}

.page-btn:hover {
  background-color: #444;
}
</style>
