<template>
    <div class="container mt-4">
        <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">上傳車位資料</h2>
        </div>
        <!-- 社區車位種類選取 -->
        <div ref="containerRef" class="p-4 mb-4 bg-light rounded border">
            <h5 class="fw-bold">請勾選您社區的車位種類：</h5>
            <div class="form-check form-check-inline" v-for="type in parkingTypes" :key="type.id">
                <input class="form-check-input" type="checkbox" :value="type" v-model="selectedTypes" :id="'type' + type.id" @change="markChanged"/>
                <label class="form-check-label" :for="'type' + type.id">{{ type.label }}</label>
            </div>
            <p class="mt-2 text-muted">目前選取的種類：{{ selectedTypes.map(t => t.label).join('、') }}</p>
            <button class="btn btn-sm btn-success mt-2" @click="confirmAndSubmit">確認修改車位種類</button>
        </div>
        
        <!-- 上傳與預覽 -->
        <div class="card shadow-sm p-4" :class="{ 'disabled-area': showTypeWarning }">
            <h5 class="fw-bold mb-3">上傳車位 CSV</h5>
            <div class="mb-3">
                <a href="#" class="btn btn-outline-primary btn-sm" @click.prevent="warnTypeCheck">下載範例 CSV</a>
                <input type="file" accept=".csv" @change="handleFileUpload" class="form-control mt-2 w-auto d-inline-block" :disabled="showTypeWarning" />
            </div>
            
            <!-- 預覽表格 -->
            <div v-if="parkingSlots.length">
                <h6 class="fw-bold mt-3">預覽資料</h6>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr class="text-center">
                                <th><input type="checkbox" @change="showTypeWarning ? warnTypeCheck() : toggleSelectAll($event)" :disabled="showTypeWarning"/></th>
                                <th>車位代碼</th>
                                <th>位置</th>
                                <th>車位種類</th>
                                <th>車位擁有人</th>
                                <th>車牌號碼</th>
                                <th>是否可承租</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(slot, index) in parkingSlots" :key="index">
                                <td class="text-center" @mousedown="guarded(() => startDrag(index, $event))" @mouseover="guarded(() => dragSelect(index, $event))" @click="guarded(() => toggleCheckbox(index, $event))">
                                    <label class="d-block m-0 w-100 h-100">
                                        <input type="checkbox" :value="index" v-model="selectedIndexes" @click.stop :disabled="showTypeWarning"/>
                                    </label>
                                </td>
                                <td><input class="form-control" v-model="slot.slotNumber" @input="guarded(() => updateSelected('slotNumber', slot.slotNumber, index))" :disabled="showTypeWarning"/></td>
                                <td><input class="form-control" v-model="slot.location" @input="guarded(() => updateSelected('location', slot.location, index))" :disabled="showTypeWarning"/></td>
                                <td>
                                    <select class="form-select" v-model="slot.parkingTypeId" @change="guarded(() => updateSelected('parkingTypeId', slot.parkingTypeId, index))" :disabled="showTypeWarning">
                                        <option v-for="type in selectedTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
                                    </select>
                                </td>
                                <td>
                                    <select class="form-select" v-model="slot.usersId" @change="guarded(() => updateSelected('usersId', slot.usersId, index))" :disabled="showTypeWarning">
                                        <option v-for="user in users" :key="user.usersId" :value="user.usersId">{{ user.name }}</option>
                                    </select>
                                </td>
                                <td><input class="form-control" v-model="slot.licensePlate" @input="guarded(() => updateSelected('licensePlate', slot.licensePlate, index))" :disabled="showTypeWarning"/></td>
                                <td>
                                    <select class="form-select" v-model="slot.isRentable" @change="guarded(() => updateSelected('isRentable', slot.isRentable, index))" :disabled="showTypeWarning">
                                        <option :value="true">可承租</option>
                                        <option :value="false">不可承租</option>
                                    </select>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-danger" @click="guarded(() => removeRow(index))" :disabled="showTypeWarning">刪除</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- 操作按鈕 -->
                <div class="mt-3 d-flex gap-2">
                    <button class="btn btn-outline-danger" @click="guarded(removeSelected)" :disabled="showTypeWarning">刪除所選</button>
                    <button class="btn btn-outline-success" @click="guarded(addRow)" :disabled="showTypeWarning">新增一列</button>
                    <button class="btn btn-primary ms-auto" @click="guarded(submitData)" :disabled="showTypeWarning">提交資料</button>
                </div>
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import axios from '@/plugins/axios.js';
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()

const parkingTypes = [
    { id: 1, label: '汽車' },
    { id: 2, label: '機車' },
    { id: 3, label: '電動車' },
    { id: 4, label: '殘障車位' }
]

const users = ref([])

const selectedTypes = ref([])

const communityId = userStore.community  // 請根據實際社區 ID 替換

// async function confirmAndSubmit() {
    //     const confirmed = window.confirm('是否確定要更新這些車位種類？')
    //     if (!confirmed) return
    
    //     try {
        //         const payload = selectedTypes.value.map(type => ({
            //             type: type.label // 傳給後端的欄位名稱要叫 type
            //         }))
            
            //         const response = await axios.post(`/park/parking-types?communityId=${communityId}`, payload)
            
            //         alert(response.data.message || '更新成功')
            //     } catch (error) {
                //         alert(error.response?.data?.message || '更新失敗')
                //     }
                // }
                
                const hasChanged = ref(false)
                const containerRef = ref(null)
                
                const handleClickOutside = (event) => {
                    if (!hasChanged.value) return
                    if (containerRef.value && !containerRef.value.contains(event.target)) {
                        confirmAndSubmit()
        hasChanged.value = false // 重置狀態，避免重複觸發
    }
}



// 車位資料陣列
const parkingSlots = ref([])

// 上傳並解析 CSV 檔案
function handleFileUpload(event) {
    const file = event.target.files[0]
    if (!file) return
    
    const reader = new FileReader()
    reader.onload = (e) => {
        const lines = e.target.result
        .split('\n')
        .map(line => line.trim())
        .filter(line => line)
        
        const result = []
        
        for (let i = 1; i < lines.length; i++) {
            const parts = lines[i].split(',').map(p => p.trim())
            if (parts.length < 6) continue
            
            const [
                slot_number,
                location,
                parking_type_label,
                user_name,
                license_plate,
                is_rentable_text
            ] = parts
            
            const parkingType = selectedTypes.value.find(type => type.label === parking_type_label)
            const user = users.value.find(u => 
            u.name.trim().replace(/\s/g, '').toLowerCase() === 
            user_name.trim().replace(/\s/g, '').toLowerCase()
        )
        console.log(user.usersId)
        const isRentable = is_rentable_text === '是'
        
        result.push({
            slotNumber: slot_number,
            location,
            licensePlate: license_plate,
            isRentable: isRentable,
            parkingTypeId: parkingType?.id ?? null,
            parkingTypeLabel: parkingType?.label ?? null,
            usersId: user ? user.usersId : null
        })
        
    }
    
    parkingSlots.value = result
}

reader.readAsText(file)
}


const selectedIndexes = ref([])

function updateSelected(field, newValue, changedIndex) {
    selectedIndexes.value.forEach(i => {
        if (i !== changedIndex) {
            parkingSlots.value[i][field] = newValue
        }
    })
}

function toggleSelectAll(event) {
    if (event.target.checked) {
        selectedIndexes.value = parkingSlots.value.map((_, i) => i)
    } else {
        selectedIndexes.value = []
    }
}

let isDragging = false
let dragStartIndex = null
let isAdding = true // 新增這個變數判斷是加選還是取消選擇

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


function startDrag(index, event) {
    if (event.buttons !== 1) return
    isDragging = true
    dragStartIndex = index
    isAdding = !selectedIndexes.value.includes(index) // 若未選取就加選，已選取就取消
}

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

document.addEventListener('mouseup', () => {
    isDragging = false
})

// 刪除指定列
function removeRow(index) {
    parkingSlots.value.splice(index, 1)
    selectedIndexes.value = selectedIndexes.value.filter(i => i !== index)
}

function removeSelected() {
    if (selectedIndexes.value.length === 0) {
        alert('請先勾選要刪除的資料列')
        return
    }
    
    if (!confirm('確定要刪除選取的資料列嗎？')) return
    
    // 反向排序後刪除，避免 index 變動影響
    const indexesToRemove = [...selectedIndexes.value].sort((a, b) => b - a)
    indexesToRemove.forEach(index => {
        parkingSlots.value.splice(index, 1)
    })
    
    selectedIndexes.value = [] // 清空已選取的 index
}

// 新增空白列
function addRow() {
    parkingSlots.value.push({
        slotNumber: '',
        location: '',
        parkingTypeId: selectedTypes.value[0]?.id ?? 1,
        parkingTypeLabel: selectedTypes.value[0]?.label ?? '汽車',
        usersId: 1,
        licensePlate: '',
        isRentable: false
    })
}


// 提交資料（發送 API）
function submitData() {
    const payload = parkingSlots.value.map(slot => ({
        slotNumber: slot.slotNumber,
        location: slot.location,
        licensePlate: slot.licensePlate,
        isRentable: slot.isRentable,
        parkingType: slot.parkingTypeLabel ?? null,  // 注意這邊是 label 字串
        usersId: slot.usersId
    }))
    
    console.log("送出資料：", JSON.stringify(payload, null, 2));
    
    axios.post(`/park/parking-slots/batch?communityId=${communityId}`, payload)
    .then(response => {
        alert('成功送出車位資料！')
    })
    .catch(error => {
        console.error('送出失敗:', error.response?.data || error)
        alert(error.response?.data?.message || '送出失敗，請查看 console log')
    })
}

watch(selectedTypes, () => {
    hasChanged.value = true
    
    parkingSlots.value.forEach(slot => {
        // 若 label 有值但 id 缺失，補上 id
        if (!slot.parkingTypeId && slot.parkingTypeLabel) {
            const match = selectedTypes.value.find(t => t.label === slot.parkingTypeLabel.trim())
            if (match) {
                slot.parkingTypeId = match.id
            }
        }
        
        // 若 id 有值但 label 缺失，也補上 label
        if (!slot.parkingTypeLabel && slot.parkingTypeId) {
            const match = selectedTypes.value.find(t => t.id === slot.parkingTypeId)
            if (match) {
                slot.parkingTypeLabel = match.label
            }
        }
    })
})

const fetchUsers = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    users.value = res.data.data
}

// 新增以下 reactive 狀態
const showTypeWarning = ref(true)

function markChanged() {
    hasChanged.value = true
    showTypeWarning.value = true
}

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
        const response = await axios.post(`/park/parking-types?communityId=${communityId}`, payload)
        
        await Swal.fire({
            icon: 'success',
            title: response.data.message || '更新成功',
            confirmButtonText: '好的'
        })
        
        hasChanged.value = false
        showTypeWarning.value = false
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: error.response?.data?.message || '更新失敗',
            confirmButtonText: '關閉'
        })
    }
}


onMounted(() => {
    document.addEventListener('click', handleClickOutside)
    fetchUsers()
})

onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside)
})

import Swal from 'sweetalert2'



function warnTypeCheck() {
    Swal.fire({
        icon: 'warning',
        title: '請先確認車位種類！',
        confirmButtonText: '我知道了'
    })
}

function guarded(action) {
    if (showTypeWarning.value) {
        warnTypeCheck()
    } else {
        action()
    }
}

function alert(msg) {
    Swal.fire({
        icon: 'info',
        title: msg,
        confirmButtonText: '好的'
    })
}

async function confirm(msg) {
    return Swal.fire({
        title: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '確認',
        cancelButtonText: '取消'
    }).then(result => result.isConfirmed)
}
</script>

<style scoped>
.disabled-area {
    opacity: 0.6;
    pointer-events: none;
    user-select: none;
}

input.form-control,
select.form-select {
    min-width: 120px;
    font-size: 0.9rem;
    padding: 0.3rem 0.5rem;
}

th, td {
    vertical-align: middle;
    text-align: center;
}

.table td input,
.table td select {
    width: 100%;
    box-sizing: border-box;
}

button.btn {
    font-size: 0.9rem;
}

td:hover {
    cursor: pointer;
    background-color: #f5f5f5;
}
</style>
