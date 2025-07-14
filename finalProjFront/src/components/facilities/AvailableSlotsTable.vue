<template>
    <div class="mt-4">
        <h4 class="fw-bold">14天內可約時段一覽表</h4>

        <!-- 🔄 分頁按鈕 -->
        <Paginate v-model="currentPage" :page-count="3" :click-handler="handlePageChange" :prev-text="'<'"
            :next-text="'>'" :container-class="'pagination justify-content-center my-3'" :page-class="'page-item'"
            :page-link-class="'page-link'" />

        <table class="table table-bordered text-center align-middle">
            <thead class="table-light">
                <tr>
                    <th>時段 \ 日期</th>
                    <th v-for="day in weekDays" :key="day.date">
                        {{ day.label }}
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="slot in timeSlots" :key="slot">
                    <th>{{ formatTimeSlot(slot) }}</th>
                    <td v-for="day in weekDays" :key="day.date" :class="[
                        getSlotClass(day.date, slot),
                        isSelected(day.date, slot) ? 'selected-slot' : ''
                    ]" @click="toggleSlot(day.date, slot)">
                        <span v-if="facility.facilityId === 2 || facility.facilityId === 3" class="slot-meta">
                            {{ getReservedText(day.date, slot) }}
                        </span>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { format, addDays, addHours, subHours, parse } from 'date-fns'
import { zhTW } from 'date-fns/locale'
import Paginate from 'vuejs-paginate-next'

// Props：父元件傳入的單一設施資料（含 openTime、closeTime、slotList）
const props = defineProps({
    facility: {
        type: Object,
        required: true,
    },
    selectedSlots: {
        type: Array,
        default: () => []
    },
})


// 🌀 頁面狀態：目前第幾週（1 ~ 3）
const currentPage = ref(1)

// 本頁起始日（週日）
const startDate = computed(() => {
    const today = new Date()
    const dayOfWeek = today.getDay()
    const sunday = new Date(today)
    sunday.setDate(today.getDate() - dayOfWeek + (currentPage.value - 1) * 7)
    return sunday
})

// 本週的七天（週日～週六）
const weekDays = computed(() => {
    return Array.from({ length: 7 }).map((_, i) => {
        const date = addDays(startDate.value, i)
        const label = format(date, 'MM/dd（EEE）', { locale: zhTW })
        return { date: format(date, 'yyyy-MM-dd'), label }
    })
})

// 每小時一格時段（考慮 closeTime = 23:59 特例）
const timeSlots = computed(() => {
    const slots = []
    let time = parse(props.facility.openTime, 'HH:mm:ss', new Date())
    const closeTime = parse(props.facility.closeTime, 'HH:mm:ss', new Date())
    let end = subHours(closeTime, 1)

    while (time <= end) {
        slots.push(new Date(time))
        time = addHours(time, 1)
    }

    const closeTimeStr = format(closeTime, 'HH:mm:ss')
    if (closeTimeStr === '23:59:00' || closeTimeStr === '23:59:59') {
        slots.push(parse('23:00:00', 'HH:mm:ss', new Date()))
    }

    return slots
})

// 時段文字 e.g. 08:00~09:00
const formatTimeSlot = (startTime) => {
    const endTime = addHours(startTime, 1)
    return `${format(startTime, 'HH:mm')}~${format(endTime, 'HH:mm')}`
}

// slotList 轉 Map 查找快
const slotMap = computed(() => {
    const map = new Map()
    if (props.facility.slotList) {
        for (const s of props.facility.slotList) {
            const key = `${s.date} ${s.time}` // e.g. 2025-07-02 17:00:00
            map.set(key, s.available)
        }
    }
    return map
})

// 判斷 slot 是否可預約
const getSlotAvailability = (dateStr, time) => {
    const key = `${dateStr} ${format(time, 'HH:mm:ss')}`
    return slotMap.value.get(key) === true
}

// 分頁切換
const handlePageChange = (page) => {
    currentPage.value = page
}

const getReservedText = (dateStr, time) => {
    const slot = props.facility.slotList?.find(
        (s) => s.date === dateStr && s.time === format(time, 'HH:mm:ss')
    )
    if (!slot) return ''
    const reserved = slot.reservedUsers ?? 0
    const max = props.facility.maxUsers ?? '?'
    return `${reserved} / ${max}`
}

const getSlotClass = (dateStr, time) => {
    const nowPlusOneHour = addHours(new Date(), 1)
    const slotDateTime = parse(`${dateStr} ${format(time, 'HH:mm:ss')}`, 'yyyy-MM-dd HH:mm:ss', new Date())

    const slot = props.facility.slotList?.find(
        (s) => s.date === dateStr && s.time === format(time, 'HH:mm:ss')
    )

    if (!slot) return 'unavailable-cell'
    if (slotDateTime < nowPlusOneHour) return 'unavailable-cell'

    // ✅ 判斷不可預約但有人預約 → 顯示紅色
    if (!slot.available) {
        if (!slot.maxUsers && slot.reservedUsers > 0) return 'full-cell'
        if (slot.maxUsers && slot.reservedUsers >= slot.maxUsers) return 'full-cell'
        return 'unavailable-cell' // 否則是灰色
    }

    return 'available-cell'
}

const selectedSlots = ref([])

const isSelectable = (dateStr, time) => {
    const className = getSlotClass(dateStr, time)
    return className === 'available-cell'
}

const isSameDay = (d1, d2) => d1 === d2

const isConsecutive = (slot1, slot2) => {
    const t1 = parse(slot1.time, 'HH:mm:ss', new Date())
    const t2 = parse(slot2.time, 'HH:mm:ss', new Date())
    const diff = Math.abs(t1.getTime() - t2.getTime())
    return diff === 60 * 60 * 1000 // 相差一小時
}

const toggleSlot = (dateStr, time) => {
    if (!isSelectable(dateStr, time)) return

    const timeStr = format(time, 'HH:mm:ss')
    const key = `${dateStr}_${timeStr}`

    const idx = selectedSlots.value.findIndex(s => s.key === key)
    if (idx !== -1) {
        selectedSlots.value.splice(idx, 1)
        emit('update:selectedSlots', selectedSlots.value)
        return
    }

    // 新點選
    const maxSelectableSlots = Math.floor((props.facility.reservableDuration || 60) / 60)
    if (selectedSlots.value.length >= maxSelectableSlots) return

    if (selectedSlots.value.length === 1) {
        const existing = selectedSlots.value[0]
        const isSame = isSameDay(existing.date, dateStr)
        const isAdj = isConsecutive(existing, { date: dateStr, time: timeStr })
        if (!isSame || !isAdj) return
    }

    selectedSlots.value.push({ key, date: dateStr, time: timeStr })
    emit('update:selectedSlots', selectedSlots.value)
}

const isSelected = (dateStr, time) => {
    const key = `${dateStr}_${format(time, 'HH:mm:ss')}`
    return selectedSlots.value.some(s => s.key === key)
}

const emit = defineEmits(['update:selectedSlots'])


watch(() => props.selectedSlots, (newVal) => {
    selectedSlots.value = [...newVal]
})
</script>

<style scoped>
.available-cell {
    background-color: #e6f9e6;
    /* 淺綠 */
}

.unavailable-cell {
    background-color: #cccccc;
    /* 深灰 */
}

.full-cell {
    background-color: #f44336;
    /* 鮮紅 */
    color: white;
}

.slot-meta {
    position: absolute;
    bottom: 2px;
    right: 4px;
    font-size: 10px;
    color: #333;
}

.table td {
    position: relative;
    height: 48px;
    /* 或依需求調整高度 */
    padding: 4px;
}

.selected-slot {
    background-color: #ffc107 !important;
    /* 橘黃色 */
    border: 2px solid #ff9800;
}
</style>
