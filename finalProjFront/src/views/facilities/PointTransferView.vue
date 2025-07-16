<template>
    <section class="container py-5">
        <div class="card shadow-sm rounded-4 p-4 overflow-auto w-100">
            <h2 class="mb-4 fw-bold text-center">點數轉移</h2>
            <p class="text-end text-muted small mt-2">
                Hi, {{ userStore.name }}，您還有
                <span class="fw-bold text-primary">{{ facilitiesStore.totalBalance }}</span> 點，
                即將在
                <span class="fw-bold text-danger">{{ formatDate(facilitiesStore.expiredAt) }}</span> 到期
            </p>

            <PointTransferForm />
        </div>
    </section>
</template>

<script setup>
import { onMounted } from 'vue'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import { useUserStore } from '@/stores/UserStore'
import axios from '@/plugins/axios'
import PointTransferForm from '@/components/facilities/PointTransferForm.vue'

const facilitiesStore = useFacilitiesStore()
const userStore = useUserStore()

function formatDate(dateStr) {
    if (!dateStr) return '—'
    const date = new Date(dateStr)
    return date.toISOString().slice(0, 10)
}

// 載入點數帳戶資料
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
})
</script>

<style scoped>
button {
    border-radius: 20px;
    padding: 4px 12px;
}
</style>