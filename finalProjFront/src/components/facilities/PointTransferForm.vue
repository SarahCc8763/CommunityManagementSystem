<template>
    <div class="mt-4 border-top pt-4">
        <form @submit.prevent="handleSubmit" class="row g-3">
            <!-- 自動帶入：目前單位 -->
            <div class="col-md-6">
                <label class="form-label">轉出單位</label>
                <input type="text" class="form-control" :value="unitDisplay" readonly />
            </div>

            <!-- 顯示：目前可用點數 -->
            <div class="col-md-6">
                <label class="form-label">可用點數</label>
                <input type="text" class="form-control" :value="facilitiesStore.totalBalance" readonly />
            </div>
            <div class="col-md-6">
                <label for="targetUnit" class="form-label">轉入單位</label>
                <select v-model="targetUnitId" id="targetUnit" class="form-select" required>
                    <option disabled value="">請選擇單位</option>
                    <option v-for="option in unitOptions" :key="option.id" :value="option.id">
                        {{ option.label }}
                    </option>
                </select>
            </div>
            <!-- 顯示：轉入使用者名稱 -->
            <div class="col-md-6">
                <label class="form-label">轉入使用者</label>
                <input type="text" class="form-control" :value="selectedUserName" readonly />
            </div>

            <!-- 輸入：欲轉出點數 -->
            <div class="col-md-6">
                <label for="transferAmount" class="form-label">欲轉出點數</label>
                <input v-model.number="transferAmount" id="transferAmount" type="number" class="form-control" :min="1"
                    :max="facilitiesStore.totalBalance" required />
                <div v-if="transferAmount > facilitiesStore.totalBalance" class="text-danger small mt-1">
                    點數不足，請重新輸入
                </div>
            </div>

            <!-- 顯示：目前點數效期 -->
            <div class="col-md-6">
                <label class="form-label">效期</label>
                <input type="text" class="form-control" :value="formatDate(facilitiesStore.expiredAt)" readonly />
            </div>

            <!-- 送出按鈕 -->
            <div class="col-12 text-end mt-3">
                <button type="submit" class="btn btn-primary px-4" :disabled="!isFormValid">確認轉移</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useUserStore } from '@/stores/UserStore'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import Swal from 'sweetalert2'
import axios from '@/plugins/axios'

const userStore = useUserStore()
const facilitiesStore = useFacilitiesStore()

const targetDoorplate = ref('')
const transferAmount = ref(1)



// 顯示目前單位門牌號-樓層
const unitDisplay = computed(() => {
    return `${facilitiesStore.unit}號-${facilitiesStore.floor}`
})


const isFormValid = computed(() => {
    return (
        transferAmount.value >= 1 &&
        transferAmount.value <= facilitiesStore.totalBalance &&
        targetUnitId.value !== ''
    )
})

function formatDate(dateStr) {
    if (!dateStr) return '—'
    const d = new Date(dateStr)
    return d.toISOString().slice(0, 10)
}

const getTargetLabel = (id) => {
    const option = unitOptions.value.find(opt => opt.id === id)
    return option ? option.label : '未知單位'
}

const handleSubmit = async () => {
    const confirmResult = await Swal.fire({
        title: '確認轉移？',
        html: `確定要將 <strong>${transferAmount.value} 點</strong><br/>
        轉移給 <strong>${getTargetLabel(targetUnitId.value)}</strong><br/>
        使用者：<strong>${selectedUserName.value}</strong> 嗎？`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '確認',
        cancelButtonText: '取消'
    })

    if (!confirmResult.isConfirmed) return

    try {
        const payload = {
            fromUnitId: userStore.unitId,
            toUnitId: targetUnitId.value,
            amount: transferAmount.value
        }

        const res = await axios.post('/api/point-transfer', payload)

        await facilitiesStore.refreshAccountInfo()
        Swal.fire('轉移成功', res.data.message || `成功轉移 ${transferAmount.value} 點`, 'success')

        targetUnitId.value = ''
        transferAmount.value = 1
    } catch (err) {
        console.error('轉移失敗', err)
        Swal.fire('錯誤', err.response?.data?.message || '轉移失敗', 'error')
    }
}

const unitOptions = ref([])
const targetUnitId = ref("")  // 下拉選的目標單位 id
const selectedUserName = ref('') // 顯示在右方 input 欄位的使用者名稱

const fetchSelectableUnits = async () => {
    try {
        const res = await axios.get('/api/units/selectable', {
            params: {
                communityId: facilitiesStore.communityId,
                excludeUnitId: facilitiesStore.unitId
            }
        })
        console.log('unitOptions 原始資料：', res.data)
        unitOptions.value = res.data
    } catch (err) {
        console.error("載入轉入單位清單失敗", err)
    }
}



onMounted(() => {
    fetchSelectableUnits()
})
watch(targetUnitId, (newId) => {
    const selected = unitOptions.value.find(opt => opt.id === newId)
    selectedUserName.value = selected?.userName || '—'
})
</script>

<style scoped>
input[readonly] {
    background-color: #f9f9f9;
}
</style>
