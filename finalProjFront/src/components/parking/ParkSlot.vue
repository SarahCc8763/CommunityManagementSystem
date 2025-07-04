<template>
    <div class="container mt-4">
        <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">社區車位管理</h2>
        </div>
      <!-- 上方操作列 -->
      <div class="d-flex justify-content-between align-items-center mb-3">
        <button class="btn btn-icon btn-purple" data-bs-toggle="collapse" data-bs-target="#advancedSearch">
          <i class="bi bi-search text-white"></i> 進階搜尋
        </button>
        <button class="btn btn-icon btn-purple" data-bs-toggle="modal" data-bs-target="#slotModal" @click="openAddModal">
          <i class="bi bi-plus-lg text-white"></i> 新增車位
        </button>
      </div>
  
      <!-- 搜尋欄 -->
      <div class="collapse mb-4" id="advancedSearch">
        <div class="card shadow-sm p-4">
          <div class="row g-3">
            <div class="col-md-4">
              <label class="form-label">車位代碼</label>
              <input class="form-control" v-model="filter.slotNumber" />
            </div>
            <div class="col-md-4">
              <label class="form-label">位置</label>
              <input class="form-control" v-model="filter.location" />
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
              <input class="form-control" v-model="filter.licensePlate" />
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
                <th>車位代碼</th>
                <th>位置</th>
                <th>車位種類</th>
                <th>車位擁有人</th>
                <th>登記車牌</th>
                <th>是否可承租</th>
                <th>操作</th>
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
                    <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#slotModal" @click="openEditModal(slot)">修改</button>
                    <button class="btn btn-sm btn-danger" @click="deleteSlot(slot.id)">刪除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
  
      <!-- 共用 Modal（新增/編輯） -->
      <div class="modal fade" id="slotModal" tabindex="-1" aria-labelledby="slotModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content p-3">
            <h5 id="slotModalLabel">{{ isEditMode ? '編輯車位' : '新增車位' }}</h5>
            <div class="form-group">
              <label>車位代碼</label>
              <input v-model="form.slotNumber" class="form-control" :readonly="isEditMode" />
  
              <label class="mt-2">位置</label>
              <input v-model="form.location" class="form-control" />
  
              <label class="mt-2">車位種類</label>
              <select v-model="form.parkingTypeId" class="form-select">
                <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
              </select>
  
              <label class="mt-2">車位擁有人</label>
              <select v-model="form.usersId" class="form-select">
                <option v-for="user in users" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
              </select>
  
              <label class="mt-2">登記車牌</label>
              <input v-model="form.licensePlate" class="form-control" />
  
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
    </div>
  </template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from '@/plugins/axios.js'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore();
const communityId = userStore.community

const isEditMode = ref(false)

const form = ref({
    id: null,
    slotNumber: '',
    location: '',
    parkingTypeId: null,  // 注意這裡
    usersId: null,        // 改為 usersId
    licensePlate: '',
    isRentable: true
})



const slots = ref([])
const editing = ref({})
const parkingTypes = ref([])
const users = ref([])

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
        alert('新增成功')

        // 清空表單
        form.value = {
            communityId,
            slotNumber: '',
            location: '',
            parkingTypeId: null,
            usersId: null,
            licensePlate: '',
            isRentable: true
        }

        fetchSlots()
    } catch (e) {
        console.error('新增失敗', e)
        alert('新增失敗')
    }
}



function openAddModal() {
    isEditMode.value = false
    form.value = {
        id: null,
        slotNumber: '',
        location: '',
        parkingTypeId: null,
        usersId: null,
        licensePlate: '',
        isRentable: true
    }
}

function openEditModal(slot) {
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
}

const fetchType = async () => {
    const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
    // 將 type 改名為 label，保留 id
    parkingTypes.value = res.data.data.map(t => ({
    id: t.id,
    label: t.type
}))
}



const fetchUsers = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    users.value = res.data.data
}

const filter = ref({
    slotNumber: '',
    location: '',
    parkingTypeId: null,
    usersId: null,
    licensePlate: '',
    isRentable: ''
})


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



const fetchSlots = async () => {
    const res = await axios.get(`/park/parking-slots?communityId=${communityId}`)
    slots.value = res.data.data
}



async function saveEdit() {
    try {
        await axios.put(`/park/parking-slots/${form.value.id}`, form.value)
        alert('修改成功')
        fetchSlots()

    } catch (e) {
        console.error('修改失敗', e)
        alert('修改失敗')
    }
}


const deleteSlot = async (id) => {
    if (confirm('確定要刪除？')) {
        await axios.delete(`/park/parking-slots/${id}`)
        alert('刪除成功')
        fetchSlots()
    }
}

onMounted(() => {
    fetchUsers()
    fetchType()
    fetchSlots()
})

</script>

<style scoped>
.table-wrapper {
  position: relative;
  z-index: 0;
}

.table-container {
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  background: white;
  position: relative;
  z-index: 0;
}

.fixed-header-table thead th {
  position: sticky;
  top: 0;
  background-color: #f8f9fa;
  z-index: 10;
  text-align: center;
}

.status-badge {
  font-size: 0.85em;
  padding: 0.4em 0.75em;
  border-radius: 1em;
  font-weight: 600;
  display: inline-block;
  min-width: 80px;
}

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

.btn-icon {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 1rem;
  font-weight: 500;
  border-radius: 0.5rem;
}

.btn-purple {
  background: linear-gradient(90deg, #7b5cff, #8e87ff);
  color: white;
  border: none;
}

.btn-purple:hover {
  background: #6d56e6;
}
</style>
