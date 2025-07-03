<template>
    <h2>社區承租紀錄</h2>

    <div class="container">
        <!-- 上方操作列：進階搜尋 + 新增 -->
        <div class="d-flex justify-content-between align-items-center mb-2">
            <!-- 左側進階搜尋按鈕 -->
            <button class="btn btn-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#advancedSearch">
                進階搜尋
            </button>
            
            <!-- 右側新增按鈕 -->
            <button
            class="btn btn-success float-end mb-2"
            data-bs-toggle="modal"
            data-bs-target="#slotDetailModal"
            @click="openAddModal"
            >
            新增承租紀錄
        </button>
        
    </div>
    
    <!-- 搜尋條件區塊 -->
    <div class="collapse" id="advancedSearch">
        <!-- 進階搜尋 -->
        <div class="card card-body mb-3">
            <div class="row">
                <div class="col-md-3 mb-3">
                    <label class="form-label">車位代碼</label>
                    <input class="form-control" v-model="filter.slotNumber" />
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">車位種類</label>
                    <select class="form-select" v-model="filter.parkingType">
                        <option value="">所有種類</option>
                        <option v-for="type in parkingTypes" :key="type.id" :value="type.label">{{ type.label }}</option>
                    </select>
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">承租者</label>
                    <input class="form-control" v-model="filter.userName" />
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">登記車牌</label>
                    <input class="form-control" v-model="filter.licensePlate" />
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">審核狀態</label>
                    <select class="form-select" v-model="filter.approved">
                        <option value="">全部</option>
                        <option value="true">通過</option>
                        <option value="false">未通過</option>
                    </select>
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">繳費狀態</label>
                    <select class="form-select" v-model="filter.status">
                        <option value="">全部</option>
                        <option value="true">已繳費</option>
                        <option value="false">未繳費</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
    
    <h3>查詢結果：</h3>
    <table v-if="availableSlots.length" border="1">
        <thead>
            <tr>
                <th>流水號</th>
                <th>車位編號</th>
                    <th>車位種類</th>
                    <th>車位區域</th>
                    <th>承租者</th>
                    <th>登記車牌</th>
                    <th>承租起始時間</th>
                    <th>承租結束時間</th>
                    <th>繳費狀態</th>
                    <th>申請時間</th>
                    <th>最後更新時間</th>
                    <th>審核狀態</th>
                    <th>審核人</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in filteredRecords" :key="record.id">
                    <td>{{ record.id }}</td>
                    <td>{{ record.slotNumber }}</td>
                    <td>{{ record.parkingType }}</td>
                    <td>{{ record.location }}</td>
                    <td>{{ record.userName }}</td>
                    <td>{{ record.licensePlate}}</td>
                    <td>{{ record.rentBuyStart }}</td>
                    <td>{{ record.rentEnd }}</td>
                    <td>{{ record.status? '已繳費' : '未繳費' }}</td>
                    <td>{{ record.createdAt }}</td>
                    <td>{{ record.updatedAt}}</td>
                    <td>{{ record.approved? '已審核' : '待審核'}}</td>
                    <td>{{ record.approverName}}</td>
                    <td><!-- 加上 data-bs-toggle / data-bs-target -->
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#slotDetailModal"
                        @click="viewDetails(record)">
                        查看詳情
                    </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>尚無可承租車位。</p>
    </div>
    
    <!-- 詳細資料 Modal -->
    <div class="modal fade" id="slotDetailModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content p-4">
                <h5>{{ isAddMode ? '新增承租紀錄' : '承租紀錄詳情' }}</h5>

                
                <div v-if="selectedRecord">
                    <div class="mb-2">
                        <label>車位編號：
                            <select class="form-select" v-model="selectedRecord.slotNumber" :disabled="!isEditMode">
                                <option disabled value="">請選擇</option>
                                <option v-for="slot in allSlots" :key="slot.slotNumber" :value="slot.slotNumber">
                                    {{ slot.slotNumber }}
                                </option>
                            </select>
                        </label>
                    </div>
                    <div class="mb-2">
                        <label>車位種類：<input class="form-control" v-model="selectedRecord.parkingType" readonly /></label>
                    </div>
                    <div class="mb-2">
                        <label>區域：<input class="form-control" v-model="selectedRecord.location" readonly /></label>
                    </div>
                    <div class="mb-2">
                        <label>承租者：
                            <select class="form-select" v-model="selectedRecord.userName" :disabled="!isEditMode">
                                <option disabled value="">請選擇</option>
                                <option v-for="user in allUsers" :key="user.usersId" :value="user.name">
                                    {{ user.name }}
                                </option>
                            </select>
                        </label>
                    </div>
                    <div class="mb-2">
                        <label>登記車牌：<input class="form-control" v-model="selectedRecord.licensePlate" :readonly="!isEditMode" /></label>
                    </div>
                    <div class="mb-2">
                        <label>起始日：<input type="datetime" class="form-control" v-model="selectedRecord.rentBuyStart" :readonly="!isEditMode" /></label>
                    </div>
                    <div class="mb-2">
                        <label>結束日：<input type="datetime" class="form-control" v-model="selectedRecord.rentEnd" :readonly="!isEditMode" /></label>
                    </div>
                    
                    <!-- 繳費狀態 -->
<div class="mb-2" v-if="!isAddMode">
    <label>繳費狀態：<input class="form-control" v-model="selectedRecord.status" :readonly="!isEditMode" /></label>
</div>

<!-- 申請時間 -->
<div class="mb-2" v-if="!isAddMode">
    <label>申請時間：<input class="form-control" v-model="selectedRecord.createdAt" readonly /></label>
</div>

<!-- 最後更新時間 -->
<div class="mb-2" v-if="!isAddMode">
    <label>最後更新時間：<input class="form-control" v-model="selectedRecord.updatedAt" readonly /></label>
</div>

<!-- 審核狀態 -->
<div class="mb-2" v-if="!isAddMode">
    <label>審核狀態：<input class="form-control" :value="selectedRecord.approved ? '已審核' : '未審核'" readonly /></label>
</div>

<!-- 審核人 -->
<div class="mb-2" v-if="!isAddMode">
    <label>審核人：<input class="form-control" v-model="selectedRecord.approverName" readonly /></label>
</div>
                    

                        <div class="mt-3 d-flex gap-2">
    <button v-if="isAddMode" class="btn btn-success" @click="addRecord">送出新增</button>
    
    <template v-else>
        <button class="btn btn-warning" @click="isEditMode = !isEditMode">
            {{ isEditMode ? '取消修改' : '修改' }}
        </button>
        <button class="btn btn-primary" v-if="isEditMode" @click="saveRecordEdit">儲存修改</button>
        <button class="btn btn-success" v-if="!selectedRecord.approved" @click="approveRecord">審核</button>
        <button class="btn btn-danger" @click="deleteRecord">刪除</button>
    </template>

    <button class="btn btn-secondary" data-bs-dismiss="modal" @click="closeModal">退出</button>
</div>

                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import axios from '@/plugins/axios.js'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore();
const communityId = userStore.community

// const start = ref('')
// const end = ref('')
const selectedType = ref(1)
const parkingTypes = ref([])

const fetchType = async () => {
    const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
    parkingTypes.value = res.data.data.map(t => ({
        id: t.id,
        label: t.type
    }))
}

const availableSlots = ref([])

// function formatDateTime(datetimeStr) {
//     return datetimeStr.replace('T', ' ') + ':00'
// }

async function fetchAllSlots() {
    if (!selectedType.value) return
    const res = await axios.get(`/park/parking-rentals?communityId=${communityId}`)
    availableSlots.value = res.data.data
    console.log(availableSlots.value);
}

const filter = ref({
    slotNumber: '',
    parkingType: '',
    userName: '',
    licensePlate: '',
    approved: '',
    status: ''
})

const filteredRecords = computed(() => {
    return availableSlots.value.filter(record => {
        return (
            (!filter.value.slotNumber || record.slotNumber?.includes(filter.value.slotNumber)) &&
            (!filter.value.parkingType || record.parkingType === filter.value.parkingType) &&
            (!filter.value.userName || record.userName?.includes(filter.value.userName)) &&
            (!filter.value.licensePlate || record.licensePlate?.includes(filter.value.licensePlate)) &&
            (filter.value.approved === '' || String(record.approved) === filter.value.approved) &&
            (filter.value.status === '' || String(record.status) === filter.value.status)
        )
    })
})

// Modal -------------------------------------------------------
const selectedRecord = ref(null)
const isEditMode = ref(false)

const allSlots = ref([])
const allUsers = ref([])

const fetchSlotOptions = async () => {
    const res = await axios.get(`/park/parking-slots?communityId=${communityId}`)
    allSlots.value = res.data.data
}

const fetchUserOptions = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    allUsers.value = res.data.data
}

// Modal 初始化
function viewDetails(record) {
    isEditMode.value = false
    isAddMode.value = false
    selectedRecord.value = {}
    fetchSlotOptions()
    fetchUserOptions().then(() => {
        selectedRecord.value = JSON.parse(JSON.stringify(record))
    })
}



watch(() => selectedRecord.value?.slotNumber, (newSlotNumber) => {
    if (!newSlotNumber) return
    const slot = allSlots.value.find(s => s.slotNumber === newSlotNumber)
    if (slot) {
        // 只有當欄位存在才覆蓋
        if (slot.parkingTypeName) selectedRecord.value.parkingType = slot.parkingTypeName
        if (slot.location) selectedRecord.value.location = slot.location
    }
})



const currentUser = userStore.email || null

function formatDateTime(datetimeStr) {
    return datetimeStr.replace('T', ' ') + ':00'
}

async function approveRecord() {
    selectedRecord.value.approved = true
    selectedRecord.value.approverName = currentUser
    selectedRecord.value.updatedAt = formatDateTime(new Date().toISOString())

    console.log(JSON.stringify(selectedRecord.value, null, 2))

    try {
        await axios.put(`/park/parking-rentals/${selectedRecord.value.id}?communityId=${communityId}`, selectedRecord.value)
        alert('審核成功')
        fetchAllSlots()
    } catch (e) {
        console.error('審核失敗', e)
        alert('審核失敗')
    }
}

async function saveRecordEdit() {
    selectedRecord.value.updatedAt = formatDateTime(new Date().toISOString())
    selectedRecord.value.approved = false
    selectedRecord.value.approverName = null
    console.log(`修改送出的資料為${selectedRecord.value}`)
    try {
        await axios.put(`/park/parking-rentals/${selectedRecord.value.id}?communityId=${communityId}`, selectedRecord.value)
        alert('修改成功')
        fetchAllSlots()
    } catch (e) {
        console.error('修改失敗', e)
        alert('修改失敗')
    }
}

async function deleteRecord() {
    if (!confirm('確定要刪除？')) return
    try {
        await axios.delete(`/park/parking-rentals/${selectedRecord.value.id}`)
        alert('刪除成功')
        fetchAllSlots()
    } catch (e) {
        console.error('刪除失敗', e)
        alert('刪除失敗')
    }
}

// 新增------------------------------------------------

const isAddMode = ref(false)

function openAddModal() {
    isEditMode.value = true
    isAddMode.value = true

    selectedRecord.value = {
        slotNumber: '',
        parkingType: '',
        location: '',
        userName: '',
        licensePlate: '',
        rentBuyStart: '',
        rentEnd: ''
    }

    fetchSlotOptions()
    fetchUserOptions()
}


async function addRecord() {
    selectedRecord.value.status = false
    selectedRecord.value.rentBuyStart = formatDateTime(selectedRecord.value.rentBuyStart)
    selectedRecord.value.rentEnd = formatDateTime(selectedRecord.value.rentEnd)
    selectedRecord.value.updatedAt = formatDateTime(new Date().toISOString())
    selectedRecord.value.createdAt = formatDateTime(new Date().toISOString())
    selectedRecord.value.approved = false
    selectedRecord.value.approverName = null


    console.log(selectedRecord.value)
    try {
        const res = await axios.post(`/park/parking-rentals?communityId=${communityId}`, selectedRecord.value)
        console.log(res.data.data)
        alert('新增成功')
        fetchAllSlots()
        isAddMode.value = false
        selectedRecord.value = null
    } catch (e) {
        console.error('新增失敗', e)
        alert('新增失敗')
    }
}

function closeModal() {
    selectedRecord.value = null
    isEditMode.value = false
    isAddMode.value = false
}


onMounted(() => {
    fetchType()
    fetchAllSlots()
})
</script>
    
<style>
    
</style>