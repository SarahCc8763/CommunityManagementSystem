<template>
    <div class="container py-5 reservation-wrapper">
        <transition name="fade-slide">
            <div class="reservation-content" v-if="showContent">
                <h2 class="mb-4 fw-bold">{{ facility.facilityName }}</h2>
                <div class="row">
                    <!-- 左側：可預約時段表格 -->
                    <div class="col-md-8 col-12 mb-4">
                        <AvailableSlotsTable :facility="facility" :slots="availableSlots" :selectedSlots="selectedSlots"
                            @update:selectedSlots="handleSlotSelection" />
                    </div>

                    <!-- 右側：圖片 + 表單 -->
                    <div class="col-md-4 col-12">
                        <div class="facility-image-container position-relative mb-3">
                            <!-- 驚嘆號按鈕 -->
                            <button class="info-btn" @click="showInfo = true">ⓘ</button>
                            <!-- 彈出窗 -->
                            <div class="info-popup" v-if="showInfo">
                                <button class="close-btn" @click="showInfo = false">×</button>
                                <h5 class="fw-bold mb-2">{{ facility.facilityName }}</h5>
                                <p class="mb-1">📍 地點：{{ facility.facilityLocation }}</p>
                                <p class="mb-1">👥 最大人數：{{ facility.maxUsers || '不限' }}</p>
                                <p class="mb-1">⏳ 最長預約時數：{{ facility.reservableDuration }} 分鐘</p>
                                <p class="mb-0">💰 每單位時間扣點：{{ facility.fee }} 點</p>
                            </div>
                            <!-- 公設圖片元件 -->
                            <FacilityImageGallery :images="facility.images" class="facility-image-small" />
                        </div>

                        <div class="card p-3 mt-4 shadow-sm">
                            <h5 class="fw-bold mb-3">我的預約單</h5>

                            <!-- 1. 設施切換 -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <label class="mb-0 fw-bold">選擇設施：</label>
                                <select class="form-select ms-2 w-50" v-model="selectedFacilityId"
                                    @change="onFacilityChange">
                                    <option v-for="f in facilityList" :key="f.facilityId" :value="f.facilityId">
                                        {{ f.facilityName }}
                                    </option>
                                </select>
                            </div>

                            <!-- 2. 預約單位 -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <label class="mb-0 fw-bold">預約單位：</label>
                                <input type="text" class="form-control ms-2 w-50"
                                    :value="`${facilitiesStore.unit}-${facilitiesStore.floor}`" disabled />
                            </div>

                            <!-- 3. 預約時段 -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <label class="mb-0 fw-bold">預約時段：</label>
                                <div class="text-end w-50 ms-2">
                                    <div v-if="selectedSlotDisplay">
                                        {{ selectedSlotDisplay }}
                                        <button class="btn btn-link p-0 ps-2 text-danger"
                                            @click="handleSlotSelection([])">取消</button>
                                    </div>
                                    <div v-else class="text-muted">請從左側格子選擇</div>
                                </div>
                            </div>

                            <!-- 4. 人數 -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <label class="mb-0 fw-bold">人數：</label>
                                <div class="input-group w-50 ms-2">
                                    <button class="btn btn-outline-secondary" @click="decreaseUsers"
                                        :disabled="!isPeopleEditable">-</button>
                                    <input type="number" class="form-control text-center" v-model="form.numberOfUsers"
                                        :disabled="!isPeopleEditable" :min="1" @change="validateUserCount"
                                        :class="{ 'bg-light text-muted': !isPeopleEditable }" />
                                    <button class="btn btn-outline-secondary" @click="increaseUsers"
                                        :disabled="!isPeopleEditable">+</button>
                                </div>
                            </div>

                            <!-- 5. 可用點數 -->
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <label class="mb-0 fw-bold">可用點數：</label>
                                <span class="text-end">
                                    {{ facilitiesStore.totalBalance }}點
                                </span>
                            </div>

                            <!-- 6. 預計扣點 -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <label class="mb-0 fw-bold">預計扣除：</label>
                                <strong class="text-danger text-end">{{ calculatedFee }} 點</strong>
                            </div>

                            <!-- 7. 備註 -->
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <label class="mb-0 fw-bold mt-2">備註：</label>
                                <textarea class="form-control w-50 ms-2" v-model="form.remarks" rows="2" />
                            </div>

                            <button class="btn btn-primary w-100" @click="submitReservation">送出預約</button>
                        </div>
                    </div>
                </div>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { format, parse, addHours } from 'date-fns'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import { useUserStore } from '@/stores/UserStore'
const facilitiesStore = useFacilitiesStore()
const userStore = useUserStore()

import Swal from 'sweetalert2'
import axios from '@/plugins/axios'
import AvailableSlotsTable from '@/components/facilities/AvailableSlotsTable.vue'
import FacilityImageGallery from '@/components/facilities/FacilityImageGallery.vue'

// Router
const route = useRoute()
const router = useRouter()

// 資料
const facility = ref({})
const facilityList = ref([])
const selectedFacilityId = ref(route.params.facilityId)
const availableSlots = ref([])
const selectedSlot = ref(null)
const showContent = ref(false)

// 表單用
const form = ref({ numberOfUsers: 1, remarks: '' })

// 預設顯示設施詳細資訊
const showInfo = ref(false)

// 載入設施詳細資訊
const loadFacility = async (id) => {
    try {
        showContent.value = false
        const res = await axios.get(`/api/facilities/${id}`)
        facility.value = res.data
        setTimeout(() => (showContent.value = true), 10)
    } catch (err) {
        console.error('載入設施失敗', err)
    }
}

// 載入所有設施清單
const loadAllFacilities = async () => {
    try {
        const res = await axios.get('/api/facilities')
        facilityList.value = res.data
    } catch (err) {
        console.error('載入設施清單失敗', err)
    }
}

// 載入該設施的可預約時段
const loadAvailableSlots = async (id) => {
    try {
        const res = await axios.get(`/api/facility-reservation/available-slots/${id}`)
        facility.value.slotList = res.data  // ✅ 關鍵：加這行才能顯示格子
    } catch (err) {
        console.error('載入可預約時段失敗', err)
    }
}


// 切換設施
const onFacilityChange = () => {
    router.push(`/reservations/book/${selectedFacilityId.value}`)
}

// 可從子元件接收 slot
const handleSlotSelected = (slot) => {
    selectedSlot.value = slot
}

// 剩餘點數，把剩餘點數使用computed追蹤，若發生扣點才會被vue追蹤
//const totalPoints = computed(() => facilitiesStore.totalBalance)

// 預計扣點
const calculatedFee = computed(() => {
    if (!selectedSlots.value.length || !facility.value.fee) return 0

    const totalHours = selectedSlots.value.length // 每格 1 小時
    const people = form.value.numberOfUsers
    const feePerHour = facility.value.fee

    return totalHours * people * feePerHour
})

// 人數控制
const isPeopleEditable = computed(() => {
    return facility.value.facilityId === 2 || facility.value.facilityId === 3
})

function decreaseUsers() {
    if (form.value.numberOfUsers > 1) {
        form.value.numberOfUsers--
    }
}
const increaseUsers = () => {
    form.value.numberOfUsers++
}

function validateUserCount() {
    if (form.value.numberOfUsers < 1) {
        form.value.numberOfUsers = 1
    }
}

const selectedSlotDisplay = ref('')
const selectedSlots = ref([])

const handleSlotSelection = (slots) => {
    selectedSlots.value = slots

    if (slots.length === 0) {
        form.value.reservationStartTime = ''
        form.value.reservationEndTime = ''
        selectedSlotDisplay.value = ''
        return
    }

    const sorted = slots.slice().sort((a, b) => {
        const keyA = `${a.date} ${a.time}`
        const keyB = `${b.date} ${b.time}`
        return keyA.localeCompare(keyB)
    })

    const start = parse(`${sorted[0].date} ${sorted[0].time}`, 'yyyy-MM-dd HH:mm:ss', new Date())
    const end = addHours(start, sorted.length)

    // form.value.reservationStartTime = format(start, 'yyyy-MM-dd HH:mm:ss')
    // form.value.reservationEndTime = format(end, 'yyyy-MM-dd HH:mm:ss')

    form.value.reservationStartTime = format(start, "yyyy-MM-dd'T'HH:mm:ss")
    form.value.reservationEndTime = format(end, "yyyy-MM-dd'T'HH:mm:ss")

    selectedSlotDisplay.value = `${format(start, 'MM/dd HH:mm')} ~ ${format(end, 'HH:mm')}`
}

// 提交預約

const reloadPointAndSlots = async () => {
    await loadAvailableSlots(selectedFacilityId.value)
    await facilitiesStore.refreshAccountInfo()
}


const submitReservation = async () => {
    const maxSelectable = facility.value.reservableDuration / 60
    const selectedCount = selectedSlots.value.length
    if (selectedCount === 0) {
        Swal.fire({
            icon: 'warning',
            title: '尚未選擇時段',
            text: '請至少選擇 1 個時段進行預約',
        })
        return
    }

    if (selectedCount > maxSelectable) {
        Swal.fire({
            icon: 'warning',
            title: '選取時段過多',
            text: `此設施最多只能預約 ${maxSelectable} 小時，請重新選擇`,
        })
        return
    }

    const payload = {
        facilityId: selectedFacilityId.value,
        unitId: facilitiesStore.unitId,               // 從登入使用者資訊帶入
        username: facilitiesStore.username,         // 帳號或 email
        accountId: facilitiesStore.accountId,        // 使用者點數帳戶 ID        
        reservationStartTime: form.value.reservationStartTime,
        reservationEndTime: form.value.reservationEndTime,
        numberOfUsers: form.value.numberOfUsers,
        deductAmount: calculatedFee.value,
        isAdmin: false,                    // 除非你有管理員身份檢查
        remark: form.value.remarks
    }

    const isCountFacility = facility.value.facilityId === 2 || facility.value.facilityId === 3
    let currentSlotUsers = 0
    if (selectedSlots.value.length > 0 && facility.value.slotList) {
        const selectedKeys = selectedSlots.value.map(
            s => `${s.date}_${s.time}`
        )

        currentSlotUsers = Math.max(
            ...facility.value.slotList
                .filter(s => selectedKeys.includes(`${s.date}_${s.time}`))
                .map(s => Number(s.reservedUsers) || 0)
        )
    }



    const result = await Swal.fire({
        title: '確認預約？',
        html: `
            <div class="text-start">                
                <p><strong>設施：</strong>${facility.value.facilityName}</p>
                <p><strong>時間：</strong>${form.value.reservationStartTime} ~ ${form.value.reservationEndTime}</p>
                <p><strong>預約單位：</strong>${facilitiesStore.unit}-${facilitiesStore.floor}</p>
                ${isCountFacility
                ? `<p><strong>人數：</strong>${form.value.numberOfUsers} 人（目前已預約 ${currentSlotUsers} 人，上限 ${facility.value.maxUsers ?? '未設定'} 人）</p>`
                : ''
            }        
                <p><strong>剩餘點數：</strong>${facilitiesStore.totalBalance} 點</p>
                <p><strong>預計扣點：</strong>${calculatedFee.value} 點</p>              
                <p><strong>備註：</strong>${form.value.remarks || '（無）'}</p>
            </div>
        `,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '確認預約',
        cancelButtonText: '取消'
    })

    if (!result.isConfirmed) return

    try {
        await axios.post('/api/facility-reservation/reserve', payload)

        const result = await Swal.fire({
            icon: 'success',
            title: '預約成功',
            text: '您的預約已完成並扣點',
            showCancelButton: true,
            confirmButtonText: '前往預約頁',
            cancelButtonText: '確認',
        })
        if (result.isConfirmed) {
            router.push('/reservations/history').then(() => {
                window.scrollTo({ top: 0, behavior: 'smooth' })
            })
        }

        selectedSlots.value = []
        // ✅ 清空選取的時段
        selectedSlot.value = null
        selectedSlots.value = []
        selectedSlotDisplay.value = ''

        form.value.numberOfUsers = 1
        form.value.remarks = ''
        // 這裡可觸發重新載入點數與 slots：
        await reloadPointAndSlots()
    } catch (err) {
        Swal.fire({
            icon: 'error',
            title: '預約失敗',
            text: err.response?.data?.message || '請稍後再試',
        })
    }
}


const fetchPointAccount = async () => {
    try {
        const unitId = userStore.unitId
        const res = await axios.get(`/api/pointAccount/unit/${unitId}`)
        facilitiesStore.totalBalance = res.data.totalBalance
        facilitiesStore.expiredAt = res.data.expiredAt
    } catch (err) {
        console.error('載入帳戶點數失敗', err)
    }
}

// 初始載入
onMounted(async () => {
    await fetchPointAccount()
    await loadAllFacilities()
    await loadFacility(selectedFacilityId.value)
    await loadAvailableSlots(selectedFacilityId.value)
    await facilitiesStore.refreshAccountInfo()
})

// route 變更時自動刷新
watch(() => route.params.facilityId, async (newId) => {
    selectedFacilityId.value = newId

    // 重設選取格子與顯示用欄位
    selectedSlot.value = null
    selectedSlots.value = []
    selectedSlotDisplay.value = ''

    // 重設表單人數與備註
    form.value.numberOfUsers = 1
    form.value.remarks = ''

    await loadFacility(newId)
    await loadAvailableSlots(newId)
    await facilitiesStore.refreshAccountInfo()
})
</script>

<style scoped>
.facility-image-small img {
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
}


.reservation-wrapper {
    background-color: #f8f9fa;
    min-height: 100vh;
}

.reservation-content {
    background-color: #fff;
    padding: 32px;
    border-radius: 20px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05);
}

.fade-slide-enter-active {
    animation: fadeSlideIn 0.5s ease-out both;
}

@keyframes fadeSlideIn {
    0% {
        opacity: 0;
        transform: translateY(-20px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 關閉人數選單的上下箭頭 */
input[type=number]::-webkit-inner-spin-button,
input[type=number]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}


/* 以下公設資訊 */
/* 浮動圓形「!」按鈕 */
.info-btn {
    position: absolute;
    top: 12px;
    right: 12px;
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background-color: #e0e0e0;
    color: #333;
    font-size: 16px;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    border: none;
    cursor: pointer;
    z-index: 99;
    transition: background-color 0.2s ease;
}

.info-btn:hover {
    background-color: #ccc;
}

/* 彈出視窗 */
.info-popup {
    position: absolute;
    top: 40px;
    right: 0;
    width: 260px;
    background-color: white;
    border: 1px solid #ccc;
    padding: 16px;
    border-radius: 10px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    z-index: 10;
}

/* 關閉按鈕 */
.close-btn {
    position: absolute;
    top: 6px;
    right: 10px;
    border: none;
    background: none;
    font-size: 18px;
    color: #999;
    cursor: pointer;
}
</style>