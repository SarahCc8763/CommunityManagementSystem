<template>
    <h2>查詢可承租車位</h2>
    <div>
        
        <label>起始時間：<input type="datetime-local" v-model="start" /></label><br>
        <label>結束時間：<input type="datetime-local" v-model="end" /></label><br>
        <label>車位種類：
            <select v-model="selectedType">
                <option v-for="type in parkingTypes" :key="type.id" :value="type.id">
                    {{ type.label }}
                </option>
            </select>
        </label>
        
        <button @click="fetchAvailableSlots">查詢</button>
        
        <h3>查詢結果：</h3>
        <ul>
            <li v-for="slot in availableSlots" :key="slot.id">
                {{ slot.slotNumber }} - {{ slot.location }} - {{ slot.licensePlate || '無車牌' }}
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios.js'

const start = ref('')
const end = ref('')
const selectedType = ref(1)

const parkingTypes = [
    { id: 1, label: '汽車' },
    { id: 2, label: '機車' },
    { id: 3, label: '電動車' },
    { id: 4, label: '殘障車位' }
    ]
    
    const availableSlots = ref([])
    
    function formatDateTime(datetimeStr) {
        return datetimeStr.replace('T', ' ') + ':00'  // 轉成 "2025-06-25 09:30:00"
    }

    async function fetchAvailableSlots() {
        if (!start.value || !end.value || !selectedType.value) return
        const res = await axios.get('/park/parking-rentals/available-slots', {
            params: {
                parkingTypeId: selectedType.value,
                start: formatDateTime(start.value),
                end: formatDateTime(end.value)
            }
        })
        availableSlots.value = res.data
    }
    
    // 頁面初次載入自動查詢（可給定預設值）
    onMounted(() => {
        const now = new Date()
        const isoStr = now.toISOString().slice(0, 16)
        start.value = isoStr
        const later = new Date(now.getTime() + 60 * 60 * 1000)
        end.value = later.toISOString().slice(0, 16)
        fetchAvailableSlots()
    })
</script>