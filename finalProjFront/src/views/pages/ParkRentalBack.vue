<template>
    <h2>查詢可承租車位</h2>
    <div>
        <label>起始時間：<input type="datetime-local" v-model="start" /></label><br />
        <label>結束時間：<input type="datetime-local" v-model="end" /></label><br />
        <label>車位種類：
            <select v-model="selectedType">
                <option v-for="type in parkingTypes" :key="type.id" :value="type.id">
                    {{ type.label }}
                </option>
            </select>
        </label>
        <button @click="fetchAvailableSlots">查詢</button>
        <h3>查詢結果：</h3>
        <table v-if="availableSlots.length" border="1">
            <thead>
                <tr>
                    <th>流水號</th>
                    <th>車位編號</th>
                    <th>位置</th>
                    <th>車牌</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="slot in availableSlots" :key="slot.id">
                    <td>{{ slot.id }}</td>
                    <td>{{ slot.slotNumber }}</td>
                    <td>{{ slot.location }}</td>
                    <td>{{ slot.licensePlate || '無車牌' }}</td>
                    <td><!-- 加上 data-bs-toggle / data-bs-target -->
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#slotDetailModal"
                            @click="viewDetails(slot)">
                            查看詳情
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>尚無可承租車位。</p>
    </div>

    <!-- Bootstrap Modal -->
    <div class="modal fade" id="slotDetailModal" tabindex="-1" aria-labelledby="slotDetailModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="slotDetailModalLabel">車位詳細資訊</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" v-if="selectedSlot">
                    <p>流水號：{{ selectedSlot.id }}</p>
                    <p>車位編號：{{ selectedSlot.slotNumber }}</p>
                    <p>位置：{{ selectedSlot.location }}</p>
                    <p>車牌：{{ selectedSlot.licensePlate || '無車牌' }}</p>
                    <div class="mb-3">
                        <label for="historyRange" class="form-label">顯示歷史紀錄範圍：</label>
                        <select id="historyRange" class="form-select" v-model="selectedRange" @change="fetchHistory">
                            <option value="1">近一年</option>
                            <option value="3">近三年</option>
                            <option value="5">近五年</option>
                            <option value="all">全部</option>
                        </select>
                    </div>

                    <table v-if="rentalHistory.length" class="table table-bordered table-striped mt-3">
                        <thead>
                            <tr>
                                <th>承租人</th>
                                <th>起始日期</th>
                                <th>結束日期</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(record, index) in rentalHistory" :key="index">
                                <td>{{ record.userName }}</td>
                                <td>{{ record.startDate }}</td>
                                <td>{{ record.endDate }}</td>
                            </tr>
                        </tbody>
                    </table>

                    <p v-else class="mt-2">無歷史紀錄。</p>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
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

function formatDateTime(datetimeStr) {
    return datetimeStr.replace('T', ' ') + ':00'
}

const availableSlots = ref([])

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
    console.log(availableSlots.value);
}

const selectedSlot = ref(null)
const selectedRange = ref('1') // 初始為近一年
const rentalHistory = ref([])
const modalInstance = ref(null)

async function fetchHistory() {
    if (!selectedSlot.value) return
    try {
        const res = await axios.get(`/park/parking-rentals/${selectedSlot.value.id}/history`, {
            params: {
                range: selectedRange.value // 可為 1, 3, 5, all
            }
        })
        rentalHistory.value = res.data
    } catch (error) {
        console.error('載入歷史紀錄失敗', error)
        rentalHistory.value = []
    }
}

function viewDetails(slot) {
    selectedSlot.value = slot
    selectedRange.value = '1'
    fetchHistory()

    if (modalInstance.value) {
        modalInstance.value.show()
    }
}

onMounted(() => {
    const now = new Date()
    const isoStr = now.toISOString().slice(0, 16)
    start.value = isoStr
    const later = new Date(now.getTime() + 60 * 60 * 1000)
    end.value = later.toISOString().slice(0, 16)
    fetchAvailableSlots()
})

watch([start, end, selectedType], ([newStart, newEnd, newType]) => {
    if (newStart && newEnd && newType) {
        fetchAvailableSlots()
    }
})

</script>