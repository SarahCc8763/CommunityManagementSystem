<template>
    <section class="container py-5">
        <div class="card shadow-sm rounded-4 p-4 overflow-auto w-100">
            <h2 class="mb-4 fw-bold text-center">我的預約紀錄</h2>
            <p class="text-end text-muted small mt-2">
                Hi, {{ userStore.name }}，您還有
                <span class="fw-bold text-primary">{{ facilitiesStore.totalBalance }}</span> 點，
                即將在
                <span class="fw-bold text-danger">{{ formatDate(facilitiesStore.expiredAt) }}</span> 到期
            </p>

            <div class="d-flex justify-content-end align-items-center mb-2">
                <input type="text" v-model="facilityKeyword" class="form-control w-auto me-3" placeholder="搜尋設施名稱" />
                <input type="checkbox" id="showApprovedOnly" v-model="showApprovedOnly" class="form-check-input me-2" />
                <label for="showApprovedOnly" class="form-check-label me-4 mb-0">只顯示有效預約</label>
                <input type="checkbox" id="showFutureOnly" v-model="showFutureOnly" class="form-check-input me-2" />
                <label for="showFutureOnly" class="form-check-label me-4 mb-0">只顯示今天(含)以後的預約</label>
                <label for="perPageSelect" class="me-2 mb-0">每頁顯示筆數：</label>
                <select id="perPageSelect" class="form-select w-auto" v-model="perPage">
                    <option v-for="option in perPageOptions" :key="option" :value="option">{{ option }}</option>
                </select>
            </div>
            <ReservationRecordTable :records="pagedRecords" @cancel="handleCancel" />
            <!-- 分頁元件 -->
            <Paginate v-model="currentPage" :page-count="totalPages" :click-handler="handlePageClick" :prev-text="'«'"
                :next-text="'»'" :container-class="'pagination justify-content-center mt-4'" :page-class="'page-item'"
                :page-link-class="'page-link'" :active-class="'active'" :disabled-class="'disabled'" />
        </div>
    </section>
</template>

<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import { useUserStore } from '@/stores/UserStore'
import axios from '@/plugins/axios'
import ReservationRecordTable from '@/components/facilities/ReservationRecordTable.vue'
import Paginate from 'vuejs-paginate-next'
import Swal from 'sweetalert2'

const facilitiesStore = useFacilitiesStore()
const userStore = useUserStore()

const records = ref([])
const currentPage = ref(1)
const perPage = ref(5)
const perPageOptions = [5, 10, 20]

// 分頁資料
const pagedRecords = computed(() => {
    // 先排序，再分頁
    const sorted = [...records.value].sort(
        (a, b) => new Date(a.reservationStartTime) - new Date(b.reservationStartTime)
    )
    const start = (currentPage.value - 1) * perPage.value
    return sorted.slice(start, start + perPage.value)
})

const totalPages = computed(() => {
    return Math.ceil(records.value.length / perPage.value)
})

const handlePageClick = (pageNum) => {
    currentPage.value = pageNum
}

function formatStartTime(str) {
    if (!str) return ''
    const d = new Date(str)
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:00`
}

function formatEndTime(str) {
    if (!str) return ''
    const d = new Date(str)
    return `${pad(d.getHours())}:00`
}

function formatExpireTime(str) {
    const d = new Date(str)
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}

function pad(num) {
    return num.toString().padStart(2, '0')
}


const handleCancel = async (reservation) => {
    const {
        facilityName,
        numberOfUsers,
        reservationStartTime,
        reservationEndTime,
        actualUsedPoints,
        pointExpireAt,
        remark
    } = reservation

    const now = new Date()
    const expired = new Date(pointExpireAt) < now
    const expectedRefund = expired ? 0 : actualUsedPoints

    const htmlContent = `
    <p><strong>設施：</strong>${facilityName}</p>
    <p><strong>人數：</strong>${numberOfUsers ?? ''}</p>
    <p><strong>時間：</strong>${formatStartTime(reservationStartTime)} ~ ${formatEndTime(reservationEndTime)}</p>
    <p><strong>點數效期：</strong>${formatExpireTime(pointExpireAt)} ${expired ? '<span class="text-danger">(點數已過期，不會返點)</span>' : ''}</p>
    <p><strong>預計返點：</strong>${expectedRefund} 點</p>
    <p><strong>備註：</strong>${remark ?? ''}</p>
    <input type="text" id="cancelReason" class="swal2-input" placeholder="請輸入取消原因">
  `

    const result = await Swal.fire({
        title: '確定取消預約？',
        html: htmlContent,
        showCancelButton: true,
        confirmButtonText: '確認取消',
        cancelButtonText: '保留預約',
        preConfirm: () => {
            const reason = document.getElementById('cancelReason').value
            if (!reason) {
                Swal.showValidationMessage('請輸入取消原因')
            }
            return reason
        }
    })

    if (result.isConfirmed) {
        console.log(reservation.reservationId);
        console.log(result.value);

        try {
            await axios.post(`/api/facility-reservation/cancel`, {
                reservationId: reservation.reservationId,
                cancelReason: result.value
            })
            Swal.fire('已取消', '預約已成功取消', 'success')
            await loadRecords()
            await facilitiesStore.refreshAccountInfo()
        } catch (err) {
            console.error('取消失敗', err)
            Swal.fire('取消失敗', '發生錯誤，請稍後再試', 'error')
        }
    }
}

function formatDate(dateStr) {
    if (!dateStr) return '—'
    const date = new Date(dateStr)
    return date.toISOString().slice(0, 10)
}


// 只顯示未來預約及有效預約的checkBox
const showFutureOnly = ref(true)
const showApprovedOnly = ref(true)
const facilityKeyword = ref('')

const loadRecords = async () => {
    const res = await axios.get(`/api/facility-reservations/by-unit/${facilitiesStore.unitId}/simple`)

    const today = new Date()
    const startFilterDate = new Date(today.getFullYear(), today.getMonth() - 1, 1) // 上個月1日

    const todayStart = new Date()
    todayStart.setHours(0, 0, 0, 0)  // 設定成今天凌晨 00:00

    records.value = res.data.filter(item => {
        const start = new Date(item.reservationStartTime)
        const matchesKeyword =
            !facilityKeyword.value ||
            item.facilityName?.includes(facilityKeyword.value)

        return (
            start >= startFilterDate &&
            (!showFutureOnly.value || start >= todayStart) &&
            (!showApprovedOnly.value || item.reservationStatus === 'APPROVED') &&
            matchesKeyword
        )
    })

    currentPage.value = 1
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

onMounted(() => {
    fetchPointAccount()
    loadRecords()
})

watch(perPage, () => {
    currentPage.value = 1 // 每頁筆數變更時重設頁碼
})
watch([showFutureOnly, showApprovedOnly, facilityKeyword], () => {
    loadRecords()
})
</script>

<style scoped>
/* 原本的表格樣式保留 */
table {
    width: 100%;
    min-width: 800px;
    border-collapse: separate;
    border-spacing: 0 12px;
}

thead {
    background-color: #f8f9fa;
    font-weight: bold;
}

th,
td {
    padding: 12px 16px;
    text-align: center;
}

tbody tr {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    transition: transform 0.2s ease;
}

tbody tr:hover {
    transform: scale(1.01);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

button {
    border-radius: 20px;
    padding: 4px 12px;
}
</style>
