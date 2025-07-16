<template>
    <div class="container mt-4">
        <!-- 麵包屑導航 -->
        <nav aria-label="breadcrumb" class="mb-3 ms-1">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item">
                    <a href="#" @click="goTo('home')" class="text-decoration-none text-light"><i class="bi bi-house-door-fill me-1"></i>首頁</a>
                </li>
                <li class="breadcrumb-item">
                    <a href="#" @click="goTo('adminDashboard')" class="text-decoration-none text-light">後台管理</a>
                </li>
                <li class="breadcrumb-item">
                    <a href="#" @click="goTo('parkingBack')" class="text-decoration-none text-light">停車場</a>
                </li>
                <li class="breadcrumb-item active text-white" aria-current="page">承租紀錄管理</li>
            </ol>
        </nav>

        <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">承租紀錄管理</h2>
        </div>
        
        <!-- 上方操作列 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <button class="btn btn-icon btn-purple" @click="toggleCollapse">
                <i class="bi bi-search text-white me-2"></i>進階搜尋
            </button>
            <button class="btn btn-primary d-flex align-items-center gap-2 px-3 py-2 rounded-pill shadow-sm" @click="openAddModal">
                <i class="bi bi-plus-lg text-white me-2"></i>
                <span class="fw-semibold">新增承租紀錄</span>
            </button>
        </div>
        
        <!-- Collapse 搜尋欄 -->
        <div class="collapse mb-4" id="advancedSearch" ref="collapseRef">
            <div class="shadow-sm p-4">
                
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">車位代碼</label>
                        <input class="form-control" v-model="filter.slotNumber" maxlength="10" placeholder="例如：B1-001" />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">車位種類</label>
                        <select class="form-select" v-model="filter.parkingType">
                            <option value="">所有種類</option>
                            <option v-for="type in parkingTypes" :key="type.id" :value="type.label">{{ type.label }}</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">位置</label>
                        <input class="form-control" v-model="filter.location" maxlength="10" placeholder="例如：A區" />
                    </div>
                </div>
                
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label class="form-label">承租者</label>
                        <select class="form-select" v-model="filter.usersId">
                            <option value="">所有擁有人</option>
                            <option v-for="user in allUsers" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">登記車牌</label>
                        <input class="form-control" v-model="filter.licensePlate" maxlength="10" placeholder="例如：ABC-123" />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">起始時間（租用）</label>
                        <input type="date" class="form-control" v-model="filter.startTime" />
                    </div>
                </div>
                
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label class="form-label">結束時間（租用）</label>
                        <input type="date" class="form-control" v-model="filter.endTime" />
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">繳費狀態</label>
                        <select class="form-select" v-model="filter.status">
                            <option value="">全部</option>
                            <option value="true">已繳費</option>
                            <option value="false">未繳費</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">審核狀態</label>
                        <select class="form-select" v-model="filter.approved">
                            <option value="">全部</option>
                            <option value="true">已審核</option>
                            <option value="false">待審核</option>
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
                            <th>車位編號</th>
                            <th>種類</th>
                            <th>位置</th>
                            <th>承租者</th>
                            <th>登記車牌</th>
                            <th>起始</th>
                            <th>結束</th>
                            <th>繳費</th>
                            <th>審核</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="record in filteredRecords" :key="record.id" class="text-center">
                            <td>{{ record.slotNumber }}</td>
                            <td>{{ record.parkingType }}</td>
                            <td>{{ record.location }}</td>
                            <td>{{ record.userName }}</td>
                            <td>{{ record.licensePlate }}</td>
                            <td>{{ record.rentBuyStart }}</td>
                            <td>{{ record.rentEnd }}</td>
                            
                            <td>
                                <span :class="['badge status-badge', record.status ? 'status-yes' : 'status-no']">
                                    {{ record.status ? '已繳費' : '未繳費' }}
                                </span>
                            </td>
                            <td>
                                <span :class="['badge status-badge', record.approved ? 'status-yes' : 'status-no']">
                                    {{ record.approved ? '已審核' : '待審核' }}
                                </span>
                            </td>
                            <td>
                                <button class="btn btn-primary btn-sm" @click="viewDetails(record)">查看詳情</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- 詳細 Modal -->
        <div class="modal fade" id="slotDetailModal" tabindex="-1" ref="modalRef">
            <div class="modal-dialog modal-lg">
                <div class="modal-content p-4">
                    <div class="modal-header d-flex justify-content-between align-items-center">
                        <h5 class="modal-title modal-title-colored mb-0" id="slotModalLabel">{{ isEditMode ? '新增承租紀錄' : '承租紀錄詳情' }}</h5>
                        <button type="button" class="btn-close btn-close-custom" @click="handleModalClose" aria-label="Close">
                            <i class="bi bi-x-lg"></i>
                        </button>
                    </div>
                    <div v-if="selectedRecord" class="form-group">
                        <div class="container-fluid">
                            <!-- 車位種類 -->
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">車位種類</label>
                                    <select class="form-select" v-model="selectedRecord.parkingType" :disabled="!isEditMode">
                                        <option disabled value="">請選擇</option>
                                        <option v-for="type in parkingTypes" :key="type.id" :value="type.label">{{ type.label }}</option>
                                    </select>
                                </div>
                                <!-- 車位編號 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">車位編號</label>
                                    <select class="form-select" v-model="selectedRecord.slotNumber" :disabled="!isEditMode">
                                        <option disabled value="">請選擇</option>
                                        <option v-for="slot in filteredSlots" :key="slot.slotNumber" :value="slot.slotNumber">{{ slot.slotNumber }}</option>
                                    </select>
                                </div>

                                <!-- 位置 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">位置</label>
                                    <input class="form-control" v-model="selectedRecord.location" placeholder="請先選擇種類及編號" readonly />
                                </div>

                                <!-- 承租者 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">承租者</label>
                                    <select class="form-select" v-model="selectedRecord.usersId" :disabled="!isEditMode">
                                        <option disabled value="">請選擇</option>
                                        <option v-for="user in allUsers" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
                                    </select>
                                </div>

                                <!-- 登記車牌 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">登記車牌</label>
                                    <input class="form-control" v-model="selectedRecord.licensePlate" :readonly="!isEditMode" @blur="cleanInvalidChars(selectedRecord, 'licensePlate')" maxlength="10" placeholder="例如：ABC-123" />
                                </div>
                                
                                <!-- 繳費狀態 -->
                                <div class="col-md-4" v-if="!isAddMode">
                                    <label class="form-label fw-semibold">繳費狀態</label>
                                    <select class="form-select" v-model="selectedRecord.status" :disabled="!isEditMode">
                                        <option disabled value="">請選擇</option>
                                        <option value="true">已繳費</option>
                                        <option value="false">未繳費</option>
                                    </select>
                                </div>

                                <!-- 起始時間 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">起始時間</label>
                                    <input type="date" class="form-control" v-model="selectedRecord.rentBuyStart" :readonly="!isEditMode" />
                                </div>

                                <!-- 結束時間 -->
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">結束時間</label>
                                    <input type="date" class="form-control" v-model="selectedRecord.rentEnd" :readonly="!isEditMode" />
                                </div>
                            </div>
                            <!-- 操作按鈕 -->
                            <div class="d-flex justify-content-end gap-2 mt-4">
                                <button v-if="isAddMode" class="btn btn-success" @click="addRecord">送出新增</button>
                                <template v-else>
                                    <button class="btn btn-primary" v-if="isEditMode" @click="saveRecordEdit">儲存修改</button>
                                    <button class="btn btn-warning" @click="isEditMode = !isEditMode">{{ isEditMode ? '取消修改' : '修改' }}</button>
                                    <button class="btn btn-success" v-if="!selectedRecord.approved" @click="approveRecord">審核</button>                                      <button class="btn btn-danger" @click="deleteRecord">刪除</button>
                                </template>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'
import { Modal, Collapse } from 'bootstrap'

const userStore = useUserStore()
const communityId = userStore.communityId
const currentUser = userStore.userId

// 從資料庫抓type資料
const parkingTypes = ref([])
const fetchType = async () => {
    const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
    parkingTypes.value = res.data.data.map(t => ({ id: t.id, label: t.type }))
    console.log(parkingTypes.value)
}

// 從資料庫抓user資料
const allUsers = ref([])
const fetchUserOptions = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    allUsers.value = res.data.data.map(user => ({ usersId: user.usersId, name: user.name }))
    console.log(allUsers.value)
}

// 從資料庫抓rental資料
const rentalRecords = ref([])
const fetchAllRecords = async () => {
    const res = await axios.get(`/park/parking-rentals?communityId=${communityId}`)
    rentalRecords.value = res.data.data
        .slice() // 拷貝以免改到原陣列
        .sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt)) // 按 updatedAt 降冪排序
    console.log(rentalRecords.value)
}


// 從資料庫抓slot資料
const allSlots = ref([])
const fetchSlotOptions = async () => {
    const res = await axios.get(`/park/parking-slots?communityId=${communityId}`)
    allSlots.value = res.data.data
    console.log(allSlots.value)
}

// Collapse控制
const collapseRef = ref(null)
let collapseInstance = null

function toggleCollapse() {
    collapseInstance?.toggle()
}

// 綁定Modal
const modalRef = ref(null)
let modalInstance = null

// 判斷是新增或修改
const isEditMode = ref(false)
const isAddMode = ref(false)
const selectedRecord = ref(null)

// 開啟新增 modal
async function openAddModal() {
    isEditMode.value = true
    isAddMode.value = true
    selectedRecord.value = {
        slotNumber: '',
        parkingType: '',
        location: '',
        usersId: '',
        licensePlate: '',
        rentBuyStart: '',
        rentEnd: '',
        status: '',
        approved: false
    }

    await nextTick()
    modalInstance?.show()
}

// 篩選器
const filter = ref({
    slotNumber: '',
    parkingType: '',
    location: '',
    usersId: '',
    licensePlate: '',
    startTime: '',
    endTime: '',
    approved: '',
    status: ''
})

// 顯示篩選後的slot
const filteredRecords = computed(() => {
    return rentalRecords.value.filter(record => {
        const recordStart = new Date(record.rentBuyStart)
        const recordEnd = new Date(record.rentEnd)
        const filterStart = filter.value.startTime ? new Date(filter.value.startTime) : null
        const filterEnd = filter.value.endTime ? new Date(filter.value.endTime) : null
        
        const timeMatch =
        (!filterStart || recordEnd >= filterStart) &&
        (!filterEnd || recordStart <= filterEnd)
        
        
        return (
            (!filter.value.slotNumber || record.slotNumber?.includes(filter.value.slotNumber)) &&
            (!filter.value.parkingType || record.parkingType === filter.value.parkingType) &&
            (!filter.value.location || record.location?.includes(filter.value.location)) &&
            (!filter.value.usersId || record.usersId === Number(filter.value.usersId)) &&
            (!filter.value.licensePlate || record.licensePlate?.includes(filter.value.licensePlate)) &&
            (filter.value.approved === '' || String(record.approved) === filter.value.approved) &&
            (filter.value.status === '' || String(record.status) === filter.value.status) &&
            timeMatch
        )
    })
})

// 顯示詳細表單
async function viewDetails(record) {
    isEditMode.value = false
    isAddMode.value = false
    selectedRecord.value = { ...record }

    await nextTick()
    modalInstance?.show()
}

// 監控type變化，自動帶入相對應的slotNumber和location
const filteredSlots = computed(() => {
    if (!selectedRecord.value?.parkingType) return []
    return allSlots.value.filter(slot =>
    slot.parkingTypeName === selectedRecord.value.parkingType &&
    slot.isRentable === true
)
})



// 監控slotNumber變化，自動帶入相對應的parkingType和location
watch(() => selectedRecord.value?.slotNumber, (newSlotNumber) => {
    if (!newSlotNumber) return
    const slot = allSlots.value.find(s => s.slotNumber === newSlotNumber)
    if (slot) {
        selectedRecord.value.location = slot.location || ''
    }
})


// 送出新增
async function addRecord() {
    try {
        console.log(selectedRecord.value)
        const res = await axios.post(`/park/parking-rentals?communityId=${communityId}`, selectedRecord.value)
        await Swal.fire({
            icon: 'success',
            title: '新增成功',
            showConfirmButton: false,
            timer: 1000
        })
        fetchAllRecords()
        console.log(res.data.data)

        handleModalClose()
    } catch (e) {
        await Swal.fire({
            icon: 'error',
            title: '新增失敗',
            text: e.response?.data?.message || '請稍後再試',
        })
    }
}

// 關閉Modal後初始化
function closeModal() {
    selectedRecord.value = null
    isEditMode.value = false
    isAddMode.value = false
}

// 編輯資料
async function saveRecordEdit() {
    try {
        selectedRecord.value.approved = false
        selectedRecord.value.approverId = null
        console.log(selectedRecord.value)
        const res = await axios.put(`/park/parking-rentals/${selectedRecord.value.id}?communityId=${communityId}`, selectedRecord.value)
        await Swal.fire({
            icon: 'success',
            title: '修改成功',
            showConfirmButton: false,
            timer: 1000
        })
        fetchAllRecords()
        console.log(res.data.data)

        handleModalClose()
    } catch (e) {
        await Swal.fire({
            icon: 'error',
            title: '修改失敗',
            text: e.response?.data?.message || '請稍後再試',
        })
    }
}

// 審核
async function approveRecord() {
    selectedRecord.value.approved = true
    selectedRecord.value.approverId = currentUser

    console.log(selectedRecord.value)

    try {
        const res = await axios.put(`/park/parking-rentals/${selectedRecord.value.id}?communityId=${communityId}`, selectedRecord.value)
        await Swal.fire({
            icon: 'success',
            title: '審核成功',
            showConfirmButton: false,
            timer: 1000
        })
        fetchAllRecords()
        console.log(res.data.data)

        handleModalClose()
    } catch (e) {
        await Swal.fire({
            icon: 'error',
            title: '審核失敗',
            text: e.response?.data?.message || '請稍後再試',
        })
    }
}

// 刪除紀錄
async function deleteRecord() {
    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        text: '此操作無法還原！',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '刪除',
        cancelButtonText: '取消'
    })
    
    if (!result.isConfirmed) return
    try {
        const res = await axios.delete(`/park/parking-rentals/${selectedRecord.value.id}`)
        await Swal.fire({
            icon: 'success',
            title: '刪除成功',
            showConfirmButton: false,
            timer: 1000
        })
        fetchAllRecords()
        console.log(res.data.data)

        handleModalClose()
    } catch (e) {
        await Swal.fire({
            icon: 'error',
            title: '刪除失敗',
            text: e.response?.data?.message || '請稍後再試',
        })
    }
}

// 關閉Modal叉叉
function handleModalClose() {
    modalRef.value.addEventListener(
        'hidden.bs.modal',
        function onHidden() {
            closeModal();
            modalRef.value.removeEventListener('hidden.bs.modal', onHidden); // 解除綁定
        }
    );
    modalInstance?.hide();
}


onMounted(() => {
    collapseInstance = new Collapse(collapseRef.value, { toggle: false })

    modalInstance = new Modal(modalRef.value, {
        backdrop: 'static',
        keyboard: false
    })

    fetchType()
    fetchUserOptions()
    fetchAllRecords()
    fetchSlotOptions()
})

// 清除非法字元（例如貼上或有預設值）
function cleanInvalidChars(slot, field) {
    const cleaned = (slot[field] || '').replace(/[^A-Za-z0-9-]/g, '').slice(0, 10)
    slot[field] = cleaned
}

// 麵包屑導航
import { useRouter } from 'vue-router'
const router = useRouter()
const goTo = (target) => {
    switch (target) {
        case 'home':
            router.push('/')
            break
        case 'adminDashboard':
            router.push('/AdminDashboard')
            break
        case 'parkingBack':
            router.push('/pages/park/parking-back')
            break
        }
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

/* collapse區塊 */
#advancedSearch {
    border-radius: 12px;
}
.form-label {
    font-weight: 600;
}
.form-control, .form-select {
    border-radius: 8px;
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

input[type="date"]::-webkit-calendar-picker-indicator {
    filter: invert(1);
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

.breadcrumb-item + .breadcrumb-item::before {
    content: ">";
    color: #ccc; /* 或 text-light 用於深色背景 */
    margin: 0 0.5rem;
}
</style>