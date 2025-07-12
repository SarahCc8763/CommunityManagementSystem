<!-- PointHistoryView.vue -->
<template>
    <section class="container py-5">
        <div class="card shadow-sm rounded-4 p-4 overflow-auto w-100">
            <h2 class="mb-4 fw-bold text-center">點數交易紀錄</h2>
            <p class="text-end text-muted small mt-2">
                Hi, {{ userStore.name }}，您還有
                <span class="fw-bold text-primary">{{ facilitiesStore.totalBalance }}</span> 點，
                即將在
                <span class="fw-bold text-danger">{{ formatDate(facilitiesStore.expiredAt) }}</span> 到期
            </p>

            <div class="d-flex justify-content-end align-items-center mb-2">
                <input type="checkbox" id="showCurrentMonthOnly" v-model="showCurrentMonthOnly"
                    class="form-check-input me-2" />
                <label for="showCurrentMonthOnly" class="form-check-label me-4 mb-0">只顯示本月交易</label>
                <label for="perPageSelect" class="me-2 mb-0">每頁顯示筆數：</label>
                <select id="perPageSelect" class="form-select w-auto" v-model="perPage">
                    <option v-for="option in perPageOptions" :key="option" :value="option">{{ option }}</option>
                </select>
            </div>

            <PointTransactionTable :records="pagedRecords" :formatDateTime="formatDateTime" />

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
import PointTransactionTable from '@/components/facilities/PointTransactionsRecordTable.vue'
import Paginate from 'vuejs-paginate-next'

const facilitiesStore = useFacilitiesStore()
const userStore = useUserStore()

const records = ref([])
const currentPage = ref(1)
const perPage = ref(5)
const perPageOptions = [5, 10, 20]

// 分頁資料（根據 createdAt 排序）
const showCurrentMonthOnly = ref(false)
const pagedRecords = computed(() => {
    let filtered = [...records.value]

    // ✅ 篩選當月份
    if (showCurrentMonthOnly.value) {
        const now = new Date()
        const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)
        const endOfMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59)

        filtered = filtered.filter(item => {
            const created = new Date(item.createdAt)
            return created >= startOfMonth && created <= endOfMonth
        })
    }

    // ✅ 單一排序（降冪排列）
    filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))

    const start = (currentPage.value - 1) * perPage.value
    return filtered.slice(start, start + perPage.value)
})



const totalPages = computed(() => {
    return Math.ceil(records.value.length / perPage.value)
})

const handlePageClick = (pageNum) => {
    currentPage.value = pageNum
}

function formatDate(dateStr) {
    if (!dateStr) return '—'
    const date = new Date(dateStr)
    return date.toISOString().slice(0, 10)
}

function formatDateTime(dateStr) {
    if (!dateStr) return '—'
    const date = new Date(dateStr)
    return date.toLocaleString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false // ✅ 關鍵！使用 24 小時制
    })
}


// 載入點數交易紀錄


const loadRecords = async () => {
    try {
        const res = await axios.get(`/api/pointTransactions/${facilitiesStore.unitId}`)
        records.value = res.data
        currentPage.value = 1
    } catch (err) {
        console.error('載入交易紀錄失敗', err)
    }
}

// 載入點數帳戶(及時用)
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

watch(showCurrentMonthOnly, () => {
    currentPage.value = 1
})
</script>

<style scoped>
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
