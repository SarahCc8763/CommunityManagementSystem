<!-- ReservationRecordTable.vue -->
<template>
    <div class="table-wrapper">
        <table class="table-custom">
            <thead>
                <tr>
                    <th>設施</th>
                    <th>人數</th>
                    <th>預約日期</th>
                    <th>時段</th>
                    <th>扣點</th>
                    <th>點數效期</th>
                    <th>備註</th>
                    <th>狀態</th>
                    <th>取消原因</th>
                    <th>預約/取消時間</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in records" :key="item.reservationId">
                    <td>{{ item.facilityName }}</td>
                    <td>{{ item.numberOfUsers ?? '' }}</td>
                    <td>{{ formatDate(item.reservationStartTime) }}</td>
                    <td>{{ formatTimeRange(item.reservationStartTime, item.reservationEndTime) }}</td>
                    <td>{{ item.actualUsedPoints }}</td>
                    <td>{{ formatExpireTime(item.pointExpireAt) }}</td>
                    <td>{{ item.remark }}</td>
                    <td>{{ item.reservationStatus }}</td>
                    <td>{{ item.cancelReason || '' }}</td>
                    <td>{{ formatDateTime(item.finalActionTime) }}</td>
                    <td>
                        <button class="btn btn-sm" :disabled="!canCancel(item)" :class="{
                            'btn-secondary': !canCancel(item),
                            'btn-danger': canCancel(item)
                        }" @click="canCancel(item) && $emit('cancel', item)">
                            申請取消
                        </button>

                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
defineProps({
    records: Array
})

function formatDateTime(str) {
    if (!str) return ''
    const d = new Date(str)
    return d.toLocaleString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
    })
}

function formatDate(str) {
    if (!str) return ''
    const d = new Date(str)
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
}

function formatTimeRange(startStr, endStr) {
    const start = new Date(startStr)
    const end = new Date(endStr)
    return `${pad(start.getHours())}:00 ~ ${pad(end.getHours())}:00`
}

function formatExpireTime(str) {
    if (!str) return ''
    const d = new Date(str)
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}

function pad(num) {
    return num.toString().padStart(2, '0')
}

function canCancel(item) {
    if (item.reservationStatus === 'CANCELLED') return false  // ✅ 已取消不可再按

    const now = new Date()
    const cutoff = new Date()
    cutoff.setHours(now.getMinutes() > 0 ? now.getHours() + 1 : now.getHours())
    cutoff.setMinutes(0, 0, 0)  // 下一個整點
    const start = new Date(item.reservationStartTime)
    return start > cutoff
}

</script>

<style scoped>
.table-custom {
    font-size: 14px;
    border-collapse: separate;
    border-spacing: 0;
    width: 100%;
}

.table-custom thead {
    background-color: #f0f2f5;
    font-weight: bold;
}

.table-custom th,
.table-custom td {
    padding: 12px 10px;
    text-align: left;
    white-space: nowrap;
    border-bottom: 1px solid #e0e0e0;
}

.table-custom tr:nth-child(even) {
    background-color: #fafafa;
}

.table-custom tbody tr:hover {
    background-color: #f1f5ff;
}

.table-wrapper {
    overflow-x: auto;
}
</style>