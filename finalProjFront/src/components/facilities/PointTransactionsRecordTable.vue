<!-- PointTransactionsRecordTable.vue -->
<template>
    <div class="table-wrapper">
        <table class="table-custom">
            <thead>
                <tr>
                    <th>交易時間</th>
                    <th>類型</th>
                    <th>點數</th>
                    <!-- <th>對象</th> -->
                    <th>備註</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in records" :key="item.transactionId">
                    <td>{{ formatDateTime(item.createdAt) }}</td>
                    <!-- <td>{{ formatDateTime(record.createdAt) }}</td> -->
                    <td>{{ mapType(item.transactionType) }}</td>
                    <td :class="item.amount >= 0 ? 'text-success fw-bold' : 'text-danger fw-bold'">
                        {{ item.amount > 0 ? '+' + item.amount : item.amount }}
                    </td>
                    <!-- <td>{{ item.relatedUnitName || '—' }}</td> -->
                    <td>{{ item.transactionDescription || '—' }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
defineProps({
    records: Array,
    formatDateTime: Function,
})

// function formatDateTime(str) {
//     if (!str) return ''
//     const d = new Date(str)
//     return d.toLocaleString('zh-TW', {
//         year: 'numeric',
//         month: '2-digit',
//         day: '2-digit',
//         hour: '2-digit',
//         minute: '2-digit'
//     })
// }

function mapType(type) {
    switch (type) {
        case 'reservation': return '預約扣點'
        case 'cancel': return '取消退點'
        case 'topup': return '點數加值'
        case 'transfer_in': return '轉入'
        case 'transfer_out': return '轉出'
        default: return type
    }
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

.text-success {
    color: green;
}

.text-danger {
    color: red;
}
</style>
