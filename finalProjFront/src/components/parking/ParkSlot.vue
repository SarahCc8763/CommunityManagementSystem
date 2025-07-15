<template>
  <div class="container mt-4">
    <div class="tag-style px-4 py-2 mb-4">
      <h2 class="mb-0 fw-bold text-primary section-title">社區車位管理</h2>
    </div>
    <!-- 上方操作列 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
      <button class="btn btn-icon btn-purple" @click="toggleCollapse">
        <i class="bi bi-search text-white me-2"></i>進階搜尋
      </button>
      <button class="btn btn-primary d-flex align-items-center gap-2 px-3 py-2 rounded-pill shadow-sm" @click="openAddModal">
        <i class="bi bi-plus-lg text-white me-2"></i>
        <span class="fw-semibold">新增車位</span>
      </button>
    </div>

    <!-- Collapse 搜尋欄 -->
    <div class="collapse mb-4" id="advancedSearch" ref="collapseRef">
      <div class="shadow-sm p-4">
        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label">車位代碼</label>
            <input class="form-control" v-model="filter.slotNumber" @blur="cleanInvalidChars(filter, 'slotNumber')" maxlength="10" placeholder="例如：B1-001"/>
          </div>
          <div class="col-md-4">
            <label class="form-label">位置</label>
            <input class="form-control" v-model="filter.location" maxlength="10" placeholder="例如：A區"/>
          </div>
          <div class="col-md-4">
            <label class="form-label">車位種類</label>
            <select class="form-select" v-model="filter.parkingTypeId">
              <option value="">所有種類</option>
              <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
            </select>
          </div>
          <div class="col-md-4">
            <label class="form-label">車位擁有人</label>
            <select class="form-select" v-model="filter.usersId">
              <option value="">所有擁有人</option>
              <option v-for="user in users" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
            </select>
          </div>
          <div class="col-md-4">
            <label class="form-label">登記車牌</label>
            <input class="form-control" v-model="filter.licensePlate" @blur="cleanInvalidChars(filter, 'licensePlate')" maxlength="10" placeholder="例如：ABC-123"/>
          </div>
          <div class="col-md-4">
            <label class="form-label">是否可承租</label>
            <select class="form-select" v-model="filter.isRentable">
              <option value="">全部</option>
              <option value="true">是</option>
              <option value="false">否</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- 表格區塊 -->
    <div class="table-wrapper">
      <div class="table-container">
        <table class="table table-hover table-bordered fixed-header-table">
          <thead class="table-light text-center">
            <tr>
              <th style="width: 12%;">車位代碼</th>
              <th style="width: 12%;">位置</th>
              <th style="width: 12%;">車位種類</th>
              <th style="width: 14%;">車位擁有人</th>
              <th style="width: 18%;">登記車牌</th>
              <th style="width: 12%;">是否可承租</th>
              <th style="width: 20%;">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="slot in filteredSlots" :key="slot.id" class="text-center">
              <td>{{ slot.slotNumber }}</td>
              <td>{{ slot.location }}</td>
              <td>{{ slot.parkingTypeName || '未知' }}</td>
              <td>{{ slot.userName || '未知' }}</td>
              <td>{{ slot.licensePlate || '無' }}</td>
              <td>
                <span :class="['badge status-badge', slot.isRentable ? 'status-yes' : 'status-no']">
                  {{ slot.isRentable ? '可承租' : '不可承租' }}
                </span>
              </td>
              <td>
                <div class="d-flex justify-content-center gap-2">
                  <button class="btn btn-sm btn-warning" @click="openEditModal(slot)">修改</button>
                  <button class="btn btn-sm btn-danger" @click="deleteSlot(slot.id)">刪除</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  
  <!-- 共用 Modal（新增/編輯） -->
  <div class="modal fade" id="slotModal" tabindex="-1" aria-labelledby="slotModalLabel" aria-hidden="true" ref="modalRef">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content p-3">
        <div class="modal-header d-flex justify-content-between align-items-center">
          <h5 class="modal-title modal-title-colored mb-0">
            {{ isEditMode ? '編輯車位' : '新增車位' }}
          </h5>
          <button
          type="button"
          class="btn-close btn-close-custom"
          @click="modalInstance?.hide()"
          aria-label="Close"
          >
          <i class="bi bi-x-lg"></i>
        </button>
      </div>
      <div class="form-group">
        <label>車位代碼</label>
        <input v-model="form.slotNumber" class="form-control" @blur="cleanInvalidChars(form, 'slotNumber')" maxlength="10" placeholder="例如：B1-001"/>
        
        <label class="mt-2">位置</label>
        <input v-model="form.location" class="form-control" maxlength="10" placeholder="例如：B1 A區"/>
        
        <label class="mt-2">車位種類</label>
        <select v-model="form.parkingTypeId" class="form-select">
          <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
        </select>
        
        <label class="mt-2">車位擁有人</label>
          <select v-model="form.usersId" class="form-select">
            <option v-for="user in users" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
          </select>

          <label class="mt-2">登記車牌</label>
          <input v-model="form.licensePlate" class="form-control"  @blur="cleanInvalidChars(form, 'licensePlate')" maxlength="10" placeholder="例如：ABC-123"/>
          
          <label class="mt-2">是否可承租</label>
          <select v-model="form.isRentable" class="form-select">
            <option :value="true">是</option>
            <option :value="false">否</option>
          </select>

          <button class="btn btn-primary w-100 mt-3" @click="isEditMode ? saveEdit() : submitNewSlot()">儲存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { Modal, Collapse } from 'bootstrap'

import '@sweetalert2/theme-dark/dark.css'
import Swal from 'sweetalert2'

import axios from '@/plugins/axios.js'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore();
const communityId = userStore.community

// 從資料庫抓type資料
const parkingTypes = ref([])
const fetchType = async () => {
  console.log("communityId: " + communityId)
  const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
  // 將 type 改名為 label，保留 id
  parkingTypes.value = res.data.data.map(t => ({
    id: t.id,
    label: t.type
  }))
  console.log(parkingTypes.value)
}

// 從資料庫抓user資料
const users = ref([])
const fetchUsers = async () => {
  console.log("communityId: " + communityId)
  const res = await axios.get(`/users?communityId=${communityId}`)
  users.value = res.data.data
  console.log(users.value)
}

// 從資料庫抓slot資料
const slots = ref([])
const fetchSlots = async () => {
  console.log("communityId: " + communityId)
  const res = await axios.get(`/park/parking-slots?communityId=${communityId}`)
  slots.value = res.data.data
  console.log(slots.value)
}

// 綁定Modal
const modalRef = ref(null)
let modalInstance = null

// 判斷是新增或修改
const isEditMode = ref(false)
const form = ref({})

// 開啟新增 modal
async function openAddModal() {
  isEditMode.value = false
  form.value = {
    id: null,
    slotNumber: '',
    location: '',
    parkingTypeId: null,
    usersId: null,
    licensePlate: '',
    isRentable: false
  }

  await nextTick()
  modalInstance?.show()
}

// 開啟修改 modal
async function openEditModal(slot) {
    isEditMode.value = true
    form.value = {
        id: slot.id,
        slotNumber: slot.slotNumber,
        location: slot.location,
        parkingTypeId: slot.parkingTypeId,
        usersId: slot.usersId,
        licensePlate: slot.licensePlate,
        isRentable: slot.isRentable
    }

    await nextTick()
    modalInstance?.show()
}

// 送出新增
async function submitNewSlot() {
  console.log('送出資料：', form.value)
  try {
    const res = await axios.post(`/park/parking-slots?communityId=${communityId}`, {
      slotNumber: form.value.slotNumber?.trim() || null,
      location: form.value.location?.trim() || null,
      licensePlate: form.value.licensePlate?.trim() || null,
      isRentable: form.value.isRentable === true,
      parkingType: parkingTypes.value.find(t => t.id === Number(form.value.parkingTypeId))?.label || null,
      usersId: form.value.usersId !== '' ? Number(form.value.usersId) : null
    })

    await Swal.fire({
      icon: 'success',
      title: '新增成功',
      confirmButtonText: '確定',
    })

    // 清空表單
    // form.value = {
    //   communityId,
    //   slotNumber: '',
    //   location: '',
    //   parkingTypeId: null,
    //   usersId: null,
    //   licensePlate: '',
    //   isRentable: true
    // }
    console.log(res.data)
    fetchSlots()

    modalInstance?.hide()

  } catch (e) {
    console.error('新增失敗', e)
    await Swal.fire({
      icon: 'error',
      title: '新增失敗',
      text: e?.response?.data?.message || '請稍後再試',
      confirmButtonText: '關閉',
    })
  }
}


// 送出修改
async function saveEdit() {
  try {
    console.log(form.value)
    const res = await axios.put(`/park/parking-slots/${form.value.id}`, form.value)

    await Swal.fire({
      icon: 'success',
      title: '修改成功',
      confirmButtonText: '確定',
    })
    
    console.log(res.data)
    fetchSlots()

    modalInstance?.hide()

  } catch (e) {
    console.error('修改失敗', e)
    await Swal.fire({
      icon: 'error',
      title: '修改失敗',
      text: e?.response?.data?.message || '請稍後再試',
      confirmButtonText: '關閉',
    })
  }
}

// Collapse控制
const collapseRef = ref(null)
let collapseInstance = null

function toggleCollapse() {
  collapseInstance?.toggle()
}

// 篩選器
const filter = ref({
    slotNumber: '',
    location: '',
    parkingTypeId: '',
    usersId: '',
    licensePlate: '',
    isRentable: ''
  })
  
// 顯示篩選後的slot
const filteredSlots = computed(() => {
  return slots.value.filter(slot => {
    const filterUserId = filter.value.usersId !== null && filter.value.usersId !== '' ? Number(filter.value.usersId) : null
    const filterTypeId = filter.value.parkingTypeId !== null && filter.value.parkingTypeId !== '' ? Number(filter.value.parkingTypeId) : null
    
    return (
      (!filter.value.slotNumber || slot.slotNumber.includes(filter.value.slotNumber)) &&
      (!filter.value.location || slot.location.includes(filter.value.location)) &&
      (!filterTypeId || slot.parkingTypeId === filterTypeId) &&
      (!filterUserId || slot.usersId === filterUserId) &&
      (!filter.value.licensePlate || (slot.licensePlate || '').includes(filter.value.licensePlate)) &&
      (filter.value.isRentable === '' || slot.isRentable === (filter.value.isRentable === 'true'))
    )
  })
})

// 刪除車位
const deleteSlot = async (id) => {
  const result = await Swal.fire({
    title: '確定要刪除嗎？',
    text: '此操作無法還原！',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#6c757d',
    confirmButtonText: '刪除',
    cancelButtonText: '取消',
  })

  if (result.isConfirmed) {
    try {
      await axios.delete(`/park/parking-slots/${id}`)
      await Swal.fire({
        icon: 'success',
        title: '刪除成功',
        showConfirmButton: false,
        timer: 1000,
      })
      fetchSlots()
    } catch (e) {
      console.error('刪除失敗', e)
      await Swal.fire({
        icon: 'error',
        title: '刪除失敗',
        text: e?.response?.data?.message || '請稍後再試',
        confirmButtonText: '關閉',
      })
    }
  }
}

// 初始化 Modal
onMounted(() => {
  modalInstance = new Modal(modalRef.value)

  collapseInstance = new Collapse(collapseRef.value, {
    toggle: false // 預設不要自動展開
  })

  fetchType()
  fetchUsers()
  fetchSlots()
})

// 清除非法字元（例如貼上或有預設值）
function cleanInvalidChars(slot, field) {
    const cleaned = (slot[field] || '').replace(/[^A-Za-z0-9-]/g, '').slice(0, 10)
    slot[field] = cleaned
}
</script>

<style scoped>
/* 包住 scroll 或修正定位問題 */
.table-wrapper {
  position: relative;
  z-index: 0;
}
/* 滾動表格 */
.table-container {
  max-height: 500px;
  overflow-y: auto;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 0;
}
/* 固定表頭 */
.fixed-header-table {
  border-collapse: separate !important;
  border-spacing: 0 !important;
  margin: 0 !important;
  width: 100%;
}
/* 表頭 sticky 並美化背景 */
.fixed-header-table thead th {
  position: sticky;
  top: 0;
  background-color: #f0f4f8;
  z-index: 10;
  text-align: center;
  font-weight: 700;
  color: #333;
  border-bottom: 2px solid #dee2e6;
  padding: 12px;
}
/* 表格列 hover 效果 */
.fixed-header-table tbody tr:hover {
  background-color: #f9fcff;
}
/* 表格儲存格 padding 微調 */
.fixed-header-table td {
  padding: 10px;
  vertical-align: middle;
  color: #333;
  font-size: 15px;
}
/* 狀態標籤 */
.status-badge {
  font-size: 0.85em;
  padding: 0.4em 0.75em;
  border-radius: 1em;
  font-weight: 600;
  display: inline-block;
  min-width: 80px;
}
/* 承租狀態顏色 */
.status-yes {
  background-color: #e6f4ea;
  color: #2e7d32;
  border: 1px solid #c1e1c1;
}
.status-no {
  background-color: #fdecea;
  color: #c62828;
  border: 1px solid #f5c6cb;
}
/* 操作按鈕一致風格 */
.btn-sm {
  border-radius: 20px;
  font-size: 0.9em;
  font-weight: 600;
  padding: 6px 16px;
  transition: all 0.2s ease;
}
/* 刪除按鈕 */
.btn-danger {
  background: linear-gradient(to right, #ff6b6b, #ff8e8e);
  color: white;
  border: none;
}
.btn-danger:hover {
  background: #e05a5a;
}
/* Modal圓角 */
.modal-content {
  border-radius: 1rem;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}
/* Modal標題 */
.modal-title {
  font-weight: 700;
  font-size: 1.25rem;
  color: #4b3cc4; /* 或你主色 */
}
/* Modal input */
.form-control,
.form-select {
  border-radius: 0.6rem;
  padding: 0.6rem 1rem;
  font-size: 0.95rem;
}
/* 欄位上下間距 */
.form-group label {
  margin-top: 0.75rem;
  font-weight: 600;
}
/* 底部儲存按鈕強化 */
.btn-primary {
  background: linear-gradient(90deg, #7b5cff, #8e87ff);
  border: none;
  font-weight: 600;
  padding: 0.75rem;
  border-radius: 0.6rem;
  box-shadow: 0 4px 12px rgba(123, 92, 255, 0.4);
}
.btn-primary:hover {
  background: #6d56e6;
}
input::placeholder,
select::placeholder,
textarea::placeholder {
  color: #cbd5e1; /* 比 #888 更亮，更適合深色背景 */
  opacity: 0.9;
}
.form-control::placeholder {
  color: #cbd5e1;
}
/* Modal title */
.modal-title-colored {
  color: #aebaff; /* 淡藍或你指定的主題紫/藍色系 */
}
/* 標題字顏色 */
.modal-title-colored {
  color: #aebaff;
}

/* 關閉按鈕 icon（乾淨、右上角） */
.btn-close-custom {
  background: none;
  border: none;
  color: #f8f9fa;
  font-size: 1.25rem;
  padding: 0;
  line-height: 1;
  transition: color 0.2s ease;
}
.btn-close-custom:hover {
  color: #ffffff;
}

/* 讓 icon 垂直置中對齊標題 */
.modal-header .btn-close-custom i {
  display: block;
}

.dark-input,
.dark-select,
input.form-control,
select.form-select {
    background-color: #2e2e3e !important;
    color: #f8f9fa !important;
    border: 1px solid #555;
    border-radius: 0.375rem;
    padding: 0.25rem 0.5rem;
    font-size: 0.9rem;
    height: auto;
    appearance: auto;
    -webkit-appearance: auto;
    -moz-appearance: auto;
}
.swal2-popup.swal2-toast,
.swal2-popup.swal2-modal {
  background: #1e1e2f !important;
  color: #ddd !important;
  border-radius: 1rem;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.6);
}

.swal2-title {
  color: #aebaff !important;
  font-weight: 700;
}

:deep(.swal2-html-container) {
  color: #ccc !important;
  background: #ffffff !important;
}

.swal2-icon.swal2-warning {
  border-color: #ffb74d !important;
  color: #ffb74d !important;
}

/* 按鈕 */
.swal2-confirm.btn {
  background: linear-gradient(90deg, #ff6b6b, #ff8e8e) !important;
  color: #fff !important;
  font-weight: bold;
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 1.5rem;
}

.swal2-cancel.btn {
  background-color: #6c757d !important;
  color: #fff !important;
  font-weight: bold;
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 1.5rem;
}


</style>
