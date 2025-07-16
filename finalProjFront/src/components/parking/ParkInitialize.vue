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
                <li class="breadcrumb-item active text-white" aria-current="page">社區車位建置</li>
            </ol>
        </nav>

        <!-- 社區初始化標頭 -->
        <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">社區車位建置</h2>
        </div>

        <!-- 社區車位種類選取 -->
        <div class="p-4 mb-4 bg-light rounded border">
            <h5 class="fw-bold">請勾選您社區的車位種類：</h5>
            <!-- 種類checkbox -->
            <div class="form-check form-check-inline" v-for="type in parkingTypes" :key="type.id">
                <input class="form-check-input" type="checkbox" :value="type" v-model="selectedTypes" :id="'type' + type.id" />
                <label class="form-check-label" :for="'type' + type.id">{{ type.label }}</label>
            </div>
            <!-- 目前種類選擇 -->
            <p class="mt-2 text-muted">目前選取的種類：{{ selectedTypes.map(t => t.label).join('、') }}</p>
            <button class="btn btn-sm btn-success mt-2" @click="confirmAndSubmit">確認修改車位種類</button>
        </div>

        <!-- 上傳與預覽 -->
        <div class="p-4 border rounded bg-white" :class="{ 'disabled-area': showTypeWarning }">
            <!-- 樣式下載與上傳區塊 -->
            <div class="mb-4">
                <h5 class="fw-bold mb-3">社區車位上傳與預覽</h5>

                <!-- 範例下載 -->
                <div class="d-flex align-items-center gap-2 mb-3">
                    <i class="bi bi-file-earmark-arrow-down-fill text-primary fs-5"></i>
                    <span class="fw-semibold me-1">範例下載：</span>
                    <a href="#" @click.prevent="downloadSampleExcel" class="text-decoration-underline text-primary">下載範例 Excel</a>
                </div>
                
                <!-- 檔案上傳 -->
                <div class="d-flex align-items-center gap-2 mb-2">
                    <i class="bi bi-file-earmark-arrow-up-fill text-danger fs-5"></i>
                    <span class="fw-semibold me-1">上傳檔案：</span>
                    
                    <label class="btn btn-gradient px-3 py-2">
                        選擇檔案
                        <input type="file" accept=".xlsx" @change="handleFileUpload" class="d-none" />
                    </label>
                    
                    <small class="text-muted" v-if="fileName"> {{ fileName }}</small>
                </div>
            </div>
            
            <!-- 預覽表格 -->
            <div v-if="parkingSlots.length">
                <h6 class="fw-bold mt-3">預覽資料</h6>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr class="text-center">
                                <th>
                                    <input type="checkbox" @change="toggleSelectAll($event)"/>
                                </th>
                                <th class="slot-code">車位代碼</th>
                                <th class="location">位置</th>
                                <th class="type">車位種類</th>
                                <th class="owner">車位擁有人</th>
                                <th class="unit">擁有人戶號</th>
                                <th class="plate">車牌號碼</th>
                                <th class="rentable">是否可承租</th>
                                <th class="actions">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(slot, index) in parkingSlots" :key="index">
                                <td class="text-center no-select" @mousedown="startDrag(index, $event)" @mouseover="dragSelect(index, $event)" @click="toggleCheckbox(index, $event)">
                                    <label class="d-block m-0 w-100 h-100">
                                        <input type="checkbox" :value="index" v-model="selectedIndexes" @click.stop />
                                    </label>
                                </td>
                                <td class="slot-code">
                                    <input class="form-control dark-input" v-model="slot.slotNumber" @input="updateSelected('slotNumber', slot.slotNumber, index)" @blur="cleanInvalidChars(slot, 'slotNumber')" maxlength="10" />
                                </td>
                                <td class="location">
                                    <input class="form-control dark-input" v-model="slot.location" @input="updateSelected('location', slot.location, index)" maxlength="10" />
                                </td>
                                <td class="type">
                                    <select class="form-select dark-select" v-model="slot.parkingTypeId" @change="updateSelected('parkingTypeId', slot.parkingTypeId, index)">
                                        <option v-for="type in selectedTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
                                    </select>
                                </td>
                                <td class="owner">
                                    <select class="form-select dark-select" v-model="slot.usersId" @change="() => handleUserChange(index)">
                                        <option v-for="user in users" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
                                    </select>
                                </td>
                                <td class="unit">
                                    <select class="form-select dark-select" v-model="slot.unitsId" :disabled="getAvailableUnits(slot.usersId).length === 0" @change="() => { onUnitChange(index); updateSelected('unitsId', slot.unitsId, index)}">
                                        <option v-if="getAvailableUnits(slot.usersId).length === 0" disabled>無對應戶號</option>
                                        <option v-for="unit in getAvailableUnits(slot.usersId)" :key="unit.unitsId" :value="unit.unitsId">
                                            {{ unit.building + '-' + unit.floor + '-' + unit.unit }}
                                        </option>
                                    </select>
                                </td>
                                <td class="plate">
                                    <input class="form-control dark-input" v-model="slot.licensePlate" @input="updateSelected('licensePlate', slot.licensePlate, index)" @blur="cleanInvalidChars(slot, 'licensePlate')" maxlength="10" />
                                </td>
                                <td class="rentable">
                                    <select class="form-select dark-select" v-model="slot.isRentable" @change="updateSelected('isRentable', slot.isRentable, index)">
                                        <option :value="true">可承租</option>
                                        <option :value="false">不可承租</option>
                                    </select>
                                </td>
                                <td class="text-center align-middle actions">
                                    <button class="btn btn-outline-danger btn-sm text-nowrap" @click="removeRow(index)">刪除</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <!-- 操作按鈕 -->
                <div class="mt-3 d-flex gap-2">
                    <button class="btn btn-gradient btn-danger" @click="removeSelected">刪除所選</button>
                    <button class="btn btn-gradient btn-success" @click="addRow">新增一列</button>
                    <button class="btn btn-gradient btn-primary ms-auto" @click="submitData">提交資料</button>
                </div>
            </div>
        </div>
    </div>
</template>
    
<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import Swal from 'sweetalert2'
import axios from '@/plugins/axios.js';
import { useUserStore } from '@/stores/UserStore'
import * as XLSX from 'xlsx'

// 從UserStore取出登入資訊
const userStore = useUserStore()
const communityId = userStore.communityId

// 車種選項
const parkingTypes = [
    { id: 1, label: '汽車' },
    { id: 2, label: '機車' },
    { id: 3, label: '電動車' },
    { id: 4, label: '殘障車位' }
]

// 勾選車種
const selectedTypes = ref([])

// 從資料庫抓出user資料
const users = ref([])
const fetchUsers = async () => {
    console.log("communityId: " + communityId)
    const res = await axios.get(`/users?communityId=${communityId}`)
    users.value = res.data.data.map(user => ({
        ...user,
        unitsId: user.unit?.unitsId // 確保有此欄位
    }))
    console.log("users: " + users.value)
}

// 從資料庫抓出unit資料
const units = ref([])
const fetchUnits = async () => {
    console.log("communityId: " + communityId)
    const res = await axios.get(`/units?communityId=${communityId}`)
    units.value = res.data.data
    console.log(res.data);
    console.log("units: " + units.value)
}

// 從資料庫抓出types資料
let originalTypes = ref([])

// 取得初始資料（資料庫中的選擇）
const fetchSelectedTypes = async () => {
    try {
        const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
        const fetchedLabels = res.data.data.map(t => t.type)
        const matchedTypes = parkingTypes.filter(t => fetchedLabels.includes(t.label))
        selectedTypes.value = matchedTypes
        
        // 存下原始 id 陣列
        originalTypes.value = matchedTypes.map(t => t.id).sort()
        updateChangeStatus()
    } catch (err) {
        console.error("載入車位種類失敗", err)
    }
}

// 車種修改後須submit
const hasChanged = ref(false)
const showTypeWarning = ref(true)

// 比較現在是否與原始一致
function updateChangeStatus() {
    const currentIds = selectedTypes.value.map(t => t.id).slice().sort() // 要用 slice() 複製一份
    
    const isSame =
    currentIds.length === originalTypes.value.length &&
    currentIds.every((id, i) => id === originalTypes.value[i])
    
    hasChanged.value = !isSame
    showTypeWarning.value = !isSame
}

// 車種確認update
async function confirmAndSubmit() {
    const result = await Swal.fire({
        title: '是否確定要更新這些車位種類？',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '確認',
        cancelButtonText: '取消'
    })
    
    if (!result.isConfirmed) return
    
    try {
        const payload = selectedTypes.value.map(type => ({ type: type.label }))
        console.log("communityId: " + communityId)
        console.log("payload: " + payload)
        const response = await axios.post(`/park/parking-types?communityId=${communityId}`, payload)
        
        await Swal.fire({
            icon: 'success',
            title: response.data.message || '更新成功',
            confirmButtonText: '好的'
        })
        
        hasChanged.value = false
        showTypeWarning.value = false

        console.log("hasChanged: " + hasChanged.value)
        console.log("showTypeWarning: " + showTypeWarning.value)
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: error.response?.data?.message || '更新失敗',
            confirmButtonText: '關閉'
        })
    }
}

// 深度監聽 selectedTypes，每次變化就重新比對
watch(selectedTypes, updateChangeStatus, { deep: true })


// 動態篩選 user/unit 的選項
function getAvailableUnits(usersId) {
    if (!usersId) return []
    const user = users.value.find(u => u.usersId === usersId)
    return Array.isArray(user?.unit) ? user.unit : []
}

function getAvailableUsers(unitsId) {
    if (!unitsId) return []
    return users.value.filter(user =>
    Array.isArray(user.unit) && user.unit.some(unit => unit.unitsId === unitsId)
)
}

function getAvailableUsersForUnit(unitsId) {
    return users.value.filter(user => user.unitsId === unitsId)
}

const getAvailableUnitsForUser = user =>
    Array.isArray(user?.unit) ? user.unit : []

const filteredUsers = ref({})
const filteredUnits = ref({})

function onUnitChange(index) {
    const selectedUnitId = parkingSlots.value[index].unitsId
    const matchedUsers = getAvailableUsers(selectedUnitId)
    if (!matchedUsers.some(user => user.usersId === parkingSlots.value[index].usersId)) {
        parkingSlots.value[index].usersId = matchedUsers[0]?.usersId ?? null
    }
}

function onUserChange(index) {
    const selectedUserId = parkingSlots.value[index].usersId
    const matchedUnits = getAvailableUnits(selectedUserId)
    if (!matchedUnits.some(unit => unit.unitsId === parkingSlots.value[index].unitsId)) {
        parkingSlots.value[index].unitsId = matchedUnits[0]?.unitsId ?? null
    }
}

function downloadSampleExcel() {
  const headers = ['車位編號', '區域', '車位種類', '擁有人戶號', '車位擁有人', '登記車牌號', '是否可承租']
  const data = [
    headers,
    ['B1-001', 'B1 A區', '汽車', 'B棟-5F-10', '陳小芳', 'ABC-1234', '否']
  ]

  const worksheet = XLSX.utils.aoa_to_sheet(data)
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, '範例車位')

  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' })
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' })

  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = 'parking-slot-sample.xlsx'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

// 車位資料陣列
const parkingSlots = ref([])

const fileName = ref("未選擇檔案");

function handleFileUpload(event) {
  const file = event.target.files[0]
  if (!file) return
  fileName.value = file.name

  const reader = new FileReader()
  reader.onload = (e) => {
    const data = new Uint8Array(e.target.result)
    const workbook = XLSX.read(data, { type: 'array' })
    const sheetName = workbook.SheetNames[0]
    const worksheet = workbook.Sheets[sheetName]

    // 將 worksheet 轉為 JSON 陣列（每列是物件）
    const json = XLSX.utils.sheet_to_json(worksheet, { defval: '' }) // defval 空白補空欄位

    const result = []
    json.forEach(row => {
      const slot_number = row['車位編號']?.trim()
      const location = row['區域']?.trim()
      const parking_type_label = row['車位種類']?.trim()
      const unit_info = row['擁有人戶號']?.trim()
      const user_name = row['車位擁有人']?.trim()
      const license_plate = row['登記車牌號']?.trim()
      const is_rentable_text = row['是否可承租']?.trim()
      
      const parkingType = selectedTypes.value.find(type => type.label === parking_type_label)

      let building = null, floor = null, unit = null
      if (unit_info) {
        const segments = unit_info.split('-')
        if (segments.length === 3) {
          [building, floor, unit] = segments.map(s => s.trim())
        }
      }

      const user = users.value.find(u =>
        u.name.trim().replace(/\s/g, '').toLowerCase() ===
        user_name?.trim().replace(/\s/g, '').toLowerCase()
      )

      let unitsId = null
      if (building && floor && unit) {
        const matchedUnit = units.value.find(u =>
          u.building === building && u.floor === floor && u.unit === unit
        )
        unitsId = matchedUnit?.unitsId ?? null
      }

      const isRentable = is_rentable_text === '是'

      result.push({
        slotNumber: slot_number,
        location,
        licensePlate: license_plate,
        isRentable,
        parkingTypeId: parkingType?.id ?? null,
        parkingTypeLabel: parkingType?.label ?? null,
        usersId: user?.usersId ?? null,
        building,
        floor,
        unit,
        unitsId
      })
    })

    parkingSlots.value = result

    result.forEach((slot, index) => {
      filteredUsers.value[index] = getAvailableUsersForUnit(slot.unitsId)
      filteredUnits.value[index] = getAvailableUnitsForUser(slot.usersId)
    })
  }

  reader.readAsArrayBuffer(file) // ✅ 注意：xlsx 檔案需用 ArrayBuffer 讀取
}


const selectedIndexes = ref([])
function toggleSelectAll(event) {
    if (event.target.checked) {
        selectedIndexes.value = parkingSlots.value.map((_, i) => i)
        console.log(selectedIndexes)
    } else {
        selectedIndexes.value = []
        console.log("false" + selectedIndexes.value)
    }
}

// 拖曳全選
let isDragging = false // 是否在拖曳
let dragStartIndex = null // 選擇的內容
let isAdding = true // 新增這個變數判斷是加選還是取消選擇
// 開始拖曳
function startDrag(index, event) {
    if (event.buttons !== 1) return
    isDragging = true
    dragStartIndex = index
    isAdding = !selectedIndexes.value.includes(index) // 若未選取就加選，已選取就取消
}
// 結束拖曳
function dragSelect(index, event) {
    if (!isDragging) return
    const [start, end] = [dragStartIndex, index].sort((a, b) => a - b)
    
    // 根據 isAdding 判斷是加入還是移除
    const newSelection = [...selectedIndexes.value]
    
    for (let i = start; i <= end; i++) {
        const idx = newSelection.indexOf(i)
        if (isAdding && idx === -1) {
            newSelection.push(i)
        } else if (!isAdding && idx !== -1) {
            newSelection.splice(idx, 1)
        }
    }
    
    selectedIndexes.value = newSelection
}
// 點選checkbox
function toggleCheckbox(index, event) {
    // 避免重複點選 checkbox 本身
    const tag = event.target.tagName.toLowerCase()
    if (tag === 'input' || tag === 'label') return
    
    const i = selectedIndexes.value.indexOf(index)
    if (i === -1) {
        selectedIndexes.value.push(index)
    } else {
        selectedIndexes.value.splice(i, 1)
    }
}

// 表單修改
function updateSelected(field, newValue, changedIndex) {
    if (!selectedIndexes.value.includes(changedIndex)) return
    selectedIndexes.value.forEach(i => {
        if (i !== changedIndex) {
            parkingSlots.value[i][field] = newValue
        }
    })
}

function handleUserChange(index) {
    onUserChange(index) // 更新 filteredUnits
    
    const availableUnits = getAvailableUnits(parkingSlots.value[index].usersId)
    const firstUnit = availableUnits[0] || {}
    
    // 設定 slot 中的 unitsId 以避免出現空白
    parkingSlots.value[index].unitsId = firstUnit.unitsId ?? null
    
    // 再觸發同步更新
    updateSelected('usersId', parkingSlots.value[index].usersId, index)
    updateSelected('unitsId', parkingSlots.value[index].unitsId, index)
}


// 刪除指定列
function removeRow(index) {
    parkingSlots.value.splice(index, 1)
    selectedIndexes.value = selectedIndexes.value.filter(i => i !== index)

    delete filteredUsers.value[index]
    delete filteredUnits.value[index]
}

// 刪除多列
async function removeSelected() {
    if (selectedIndexes.value.length === 0) {
        await Swal.fire({
            icon: 'warning',
            title: '請先勾選要刪除的資料列',
            confirmButtonText: '好的'
        })
        return
    }
    
    const result = await Swal.fire({
        title: '確定要刪除選取的資料列嗎？',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '刪除',
        cancelButtonText: '取消'
    })
    
    if (!result.isConfirmed) return
    
    // 反向排序後刪除，避免 index 變動影響
    const indexesToRemove = [...selectedIndexes.value].sort((a, b) => b - a)
    indexesToRemove.forEach(index => {
        parkingSlots.value.splice(index, 1)
        delete filteredUsers.value[index]
        delete filteredUnits.value[index]
    })
    
    selectedIndexes.value = []
    
    await Swal.fire({
        icon: 'success',
        title: '刪除成功',
        text: `已刪除 ${indexesToRemove.length} 筆資料`,
        confirmButtonText: '好的'
    })
}


// 新增空白列
function addRow() {
    const defaultUnitId = units.value[0]?.unitsId ?? null
    const filteredUserList = getAvailableUsersForUnit(defaultUnitId)
    parkingSlots.value.push({
        slotNumber: '',
        location: '',
        parkingTypeId: selectedTypes.value[0]?.id ?? 1,
        parkingTypeLabel: selectedTypes.value[0]?.label ?? '汽車',
        unitsId: defaultUnitId,
        usersId: filteredUserList[0]?.usersId ?? null,
        licensePlate: '',
        isRentable: false,
        building: '',
        floor: '',
        unit: ''
    })

    const index = parkingSlots.value.length - 1
    filteredUsers.value[index] = filteredUserList
    filteredUnits.value[index] = getAvailableUnitsForUser(filteredUserList[0] ?? null)
}


// 提交資料（發送 API）
function submitData() {
    const payload = parkingSlots.value.map(slot => ({
        slotNumber: slot.slotNumber,
        location: slot.location,
        licensePlate: slot.licensePlate,
        isRentable: slot.isRentable,
        parkingType: slot.parkingTypeLabel ?? null,
        usersId: slot.usersId,
        building: slot.building,
        floor: slot.floor,
        unit: slot.unit
    }))
    
    console.log("送出資料：", JSON.stringify(payload, null, 2))

    axios.post(`/park/parking-slots/batch?communityId=${communityId}`, payload)
    .then(response => {
        const uploadedCount = Array.isArray(response.data.data) ? response.data.data.length : 0

        Swal.fire({
            icon: 'success',
            title: '成功送出車位資料',
            html: `共成功送出 <strong>${uploadedCount}</strong> 筆資料！`,
            confirmButtonText: '好的'
        })

        console.log(response.data.data)
    })
    .catch(error => {
        console.error('送出失敗:', error.response?.data || error)
        Swal.fire({
            icon: 'error',
            title: '送出失敗',
            text: error.response?.data?.message || '發生未知錯誤，請查看 console log',
            confirmButtonText: '關閉'
        })
    })
}


// 監控拖曳事件
function stopDrag() {
    isDragging = false
}

onMounted(async ()=>{
    await fetchUsers()
    await fetchUnits()
    await fetchSelectedTypes()
    
    parkingSlots.value.forEach((slot, index) => {
        filteredUsers.value[index] = getAvailableUsersForUnit(slot.unitsId)
        filteredUnits.value[index] = getAvailableUnitsForUser(slot.usersId)
    })
    
    window.addEventListener('mouseup', stopDrag)
    console.log(selectedTypes.value)
    console.log("hasChanged: " + hasChanged.value)
    console.log("showTypeWarning: " + showTypeWarning.value)
})

onBeforeUnmount(() => {
    window.removeEventListener('mouseup', stopDrag)
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
.disabled-area {
    opacity: 0.6;
    pointer-events: none;
    user-select: none;
    filter: grayscale(20%);
    transition: all 0.2s ease;
}

/* 基本表格設定 */
table {
    table-layout: fixed;
    width: 100%;
}
table td,
table th {
    vertical-align: middle !important;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    border-color: #444;
    color: #f0f0f0;
}

/* 欄寬比例設定 */
th.slot-code, td.slot-code { width: 10%; }
th.location, td.location { width: 10%; }
th.type, td.type { width: 10%; }
th.owner, td.owner { width: 15%; }
th.unit, td.unit { width: 15%; }
th.plate, td.plate { width: 15%; }
th.rentable, td.rentable { width: 10%; }
th.actions, td.actions { width: 10%; }

/* 表單欄位統一樣式 */
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
.dark-input::placeholder {
    color: #aaa;
}

.dark-select option,
select option {
    background-color: #2e2e3e;
    color: #f8f9fa;
}

/* focus 狀態強調 */
.dark-select:focus {
    border-color: #6a11cb;
    box-shadow: 0 0 0 0.2rem rgba(106, 17, 203, 0.3);
    outline: none;
}

/* 取消預設選取 */
.no-select {
    user-select: none;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
}

/* 按鈕大小一致 */
table td .btn {
    white-space: nowrap;
    padding: 0.3rem 0.75rem;
    font-size: 0.875rem;
    min-width: 64px;
}

/* 深色背景卡片區塊 */
.bg-white, .bg-light, .disabled-area {
    background-color: #1e1e2f !important;
    color: #f8f9fa !important;
    border: 1px solid #444;
}

/* 表頭樣式 */
.table-light {
    background-color: #2b2b3c !important;
    color: #f8f9fa;
}

/* Tag 樣式 */
.tag-style {
    background-color: #2b2b3c;
    border-left: 6px solid #6a11cb;
    border-radius: 0.5rem;
    color: #f8f9fa;
}

/* 漸層按鈕樣式 */
.btn-gradient {
    background-image: linear-gradient(to right, #6a11cb, #2575fc);
    color: white !important;
    border: none;
    transition: background 0.3s;
}
.btn-gradient:hover {
    background-image: linear-gradient(to right, #2575fc, #6a11cb);
}
/* 深色背景下調整 .text-muted 顏色 */
.text-muted {
  color: #ccc !important; /* 或你要的亮灰色，可調亮一點 */
}

.breadcrumb-item + .breadcrumb-item::before {
    content: ">";
    color: #ccc; /* 或 text-light 用於深色背景 */
    margin: 0 0.5rem;
}

</style>