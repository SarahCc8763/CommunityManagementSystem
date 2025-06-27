<template>
    <h2>社區車位管理</h2>
    <div class="container">
        <!-- 上方操作列：進階搜尋 + 新增 -->
        <div class="d-flex justify-content-between align-items-center mb-2">
            <!-- 左側進階搜尋按鈕 -->
            <button class="btn btn-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#advancedSearch">
                進階搜尋
            </button>
            
            <!-- 右側新增按鈕 -->
            <button class="btn btn-success float-end mb-2"
            data-bs-toggle="modal"
            data-bs-target="#slotModal"
            @click="openAddModal">
            新增車位
        </button>
    </div>
    
    <!-- 搜尋條件區塊 -->
    <div class="collapse" id="advancedSearch">
        <div class="card card-body mb-3">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">車位代碼</label>
                    <input class="form-control" v-model="filter.slotNumber" />
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">位置</label>
                    <input class="form-control" v-model="filter.location" />
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">車位種類</label>
                    <select class="form-select" v-model="filter.parkingTypeId">
                        <option value="">所有種類</option>
                        <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">車位擁有人</label>
                    <select class="form-select" v-model="filter.usersId">
                        <option value="">所有擁有人</option>
                        <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">登記車牌</label>
                    <input class="form-control" v-model="filter.licensePlate" />
                </div>
                <div class="col-md-4 mb-3">
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
    
    
    <table class="table table-bordered">
        <thead>
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
            <tr v-for="slot in filteredSlots" :key="slot.id">
                <td>{{ slot.slotNumber }}</td>
                <td>{{ slot.location }}</td>
                <td>{{ getParkingTypeLabel(slot.parkingTypeId) }}</td>
                <td>{{ getUserName(slot.usersId) }}</td>
                <td>{{ slot.licensePlate || '無' }}</td>
                <td>{{ slot.isRentable ? '是' : '否' }}</td>
                <td>
                    <button class="btn btn-sm btn-warning"
                    data-bs-toggle="modal"
                    data-bs-target="#slotModal"
                    @click="openEditModal(slot)">
                    修改
                </button>
                <button class="btn btn-sm btn-danger" @click="deleteSlot(slot.id)">刪除</button>
            </td>
        </tr>
    </tbody>
</table>

<!-- 編輯車位 Modal -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content p-3">
            <h5>編輯車位</h5>
            <div>
                <label>位置：<input v-model="editing.location" class="form-control" /></label>
                <label>車牌：<input v-model="editing.licensePlate" class="form-control" /></label>
                <label>是否可承租：
                    <select v-model="editing.isRentable" class="form-select">
                        <option :value="true">是</option>
                        <option :value="false">否</option>
                    </select>
                </label>
            </div>
            <button class="btn btn-primary mt-2" @click="saveEdit">儲存</button>
        </div>
    </div>
</div>
</div>

<!-- 共用 Modal -->
<div class="modal fade" id="slotModal" tabindex="-1" aria-labelledby="slotModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content p-3">
            <h5 id="slotModalLabel">{{ isEditMode ? '編輯車位' : '新增車位' }}</h5>
            <div>
                <label>車位代碼：
                    <input v-model="form.slotNumber" class="form-control" :readonly="isEditMode" />
                </label>
                <label>位置：
                    <input v-model="form.location" class="form-control" />
                </label>
                <label>車位種類：
                    <select v-model="form.parkingTypeId" class="form-select">
                        <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
                    </select>
                </label>
                <label>車位擁有人：
                    <select v-model="form.usersId" class="form-select">
                        <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
                    </select>
                </label>
                <label>車牌：
                    <input v-model="form.licensePlate" class="form-control" />
                </label>
                <label>是否可承租：
                    <select v-model="form.isRentable" class="form-select">
                        <option :value="true">是</option>
                        <option :value="false">否</option>
                    </select>
                </label>
            </div>
            <button class="btn btn-primary mt-2" @click="isEditMode ? saveEdit() : submitNewSlot()">儲存</button>
        </div>
    </div>
</div>


</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from '@/plugins/axios.js'

const isEditMode = ref(false)

const form = ref({
    id: null,
    communityId,
    slotNumber: '',
    location: '',
    parkingTypeId: '',
    usersId: '',
    licensePlate: '',
    isRentable: true
})


const slots = ref([])
const editing = ref({})
// const communityId = JSON.parse(localStorage.getItem('user')).communityId
const communityId = 1

const parkingTypes = [
    { id: 1, label: '汽車' },
    { id: 2, label: '機車' },
    { id: 3, label: '電動車' },
    { id: 4, label: '殘障車位' }
]

const users = ref([
    { id: 1, name: '建商' },
    { id: 2, name: '林淑貞' },
    { id: 3, name: '張大明' }
])

const newSlot = ref({
    communityId,
    slotNumber: '',
    location: '',
    parkingTypeId: '',
    usersId: '',
    licensePlate: '',
    isRentable: true
})

async function submitNewSlot() {
    try {
        const res = await axios.post('/park/parking-slots/create', newSlot.value)
        alert('新增成功')
        // 清空欄位
        newSlot.value = {
            communityId,
            slotNumber: '',
            location: '',
            parkingTypeId: '',
            usersId: '',
            licensePlate: '',
            isRentable: true
        }
        fetchSlots()
        // Bootstrap 會自動根據 data-bs-dismiss 關閉 Modal
    } catch (e) {
        console.error('新增失敗', e)
        alert('新增失敗')
    }
}

function openAddModal() {
    isEditMode.value = false
    form.value = {
        id: null,
        communityId,
        slotNumber: '',
        location: '',
        parkingTypeId: '',
        usersId: '',
        licensePlate: '',
        isRentable: true
    }
}

function openEditModal(slot) {
    isEditMode.value = true
    form.value = { ...slot }
}

function getParkingTypeLabel(id) {
    const type = parkingTypes.find(t => t.id === id)
    return type ? type.label : `未知(${id})`
}

function getUserName(id) {
    const user = users.value.find(u => u.id === id)
    return user ? user.name : `未知(${id})`
}

const filter = ref({
    slotNumber: '',
    location: '',
    parkingTypeId: '',
    usersId: '',
    licensePlate: '',
    isRentable: ''
})

const filteredSlots = computed(() => {
    return slots.value.filter(slot => {
        return (
            (!filter.value.slotNumber || slot.slotNumber.includes(filter.value.slotNumber)) &&
            (!filter.value.location || slot.location.includes(filter.value.location)) &&
            (!filter.value.parkingTypeId || slot.parkingTypeId === parseInt(filter.value.parkingTypeId)) &&
            (!filter.value.usersId || slot.usersId === parseInt(filter.value.usersId)) &&
            (!filter.value.licensePlate || (slot.licensePlate || '').includes(filter.value.licensePlate)) &&
            (filter.value.isRentable === '' || slot.isRentable === (filter.value.isRentable === 'true'))
        )
    })
})

const fetchSlots = async () => {
    const res = await axios.get(`/park/parking-slots?communityId=${communityId}`)
    slots.value = res.data
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

onMounted(fetchSlots)
</script>