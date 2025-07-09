<template>
    <div class="resident-packages-container">
        <h1>我的包裹查詢</h1>

        <div class="search-bar">
            <input v-model="residentName" type="text" placeholder="輸入姓名" />
            <input v-model="unitNumber" type="text" placeholder="輸入門牌號" />
            <button @click="searchMyPackages">查詢</button>
        </div>

        <table v-if="displayedPackages.length" class="packages-table">
            <thead>
                <tr>
                    <th>編號</th>
                    <th>包裹描述</th>
                    <th>到達時間</th>
                    <th>領取狀態</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="pkg in displayedPackages" :key="pkg.id">
                    <td>{{ pkg.id }}</td>
                    <td>{{ pkg.description }}</td>
                    <td>{{ pkg.arrivalTime }}</td>
                    <td>{{ pkg.status }}</td>
                </tr>
            </tbody>
        </table>

        <p v-else class="no-data">尚無符合條件的包裹</p>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const residentName = ref('')
const unitNumber = ref('')

const packages = ref([
    { id: 1, resident: '張大明', unit: 'A101', description: 'Amazon 包裹', arrivalTime: '2025-06-30 10:00', status: '未領取' },
    { id: 2, resident: '李小美', unit: 'B202', description: '蝦皮購物', arrivalTime: '2025-06-29 14:30', status: '已領取' },
    { id: 3, resident: '王小明', unit: '10-1', description: '掛號', arrivalTime: '2025-06-28 09:15', status: '未領取' },
    { id: 4, resident: '王小明', unit: '10-1', description: 'momo 包裹', arrivalTime: '2025-06-27 16:45', status: '未領取' },
])

const displayedPackages = ref([])

function searchMyPackages() {
    const name = residentName.value.trim().toLowerCase()
    const unit = unitNumber.value.trim().toLowerCase()

    displayedPackages.value = packages.value.filter(
        pkg =>
            pkg.resident.toLowerCase() === name &&
            pkg.unit.toLowerCase() === unit &&
            pkg.status === '未領取'
    )
}
</script>

<style scoped>
.resident-packages-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

h1 {
    margin-bottom: 20px;
}

.search-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 20px;
}

/* .search-bar input {
    flex: 1;
    min-width: 150px;
    padding: 8px;
}

.search-bar button {
    padding: 8px 12px;
} */

/*
.packages-table {
    width: 100%;
    border-collapse: collapse;
}

.packages-table th,
.packages-table td {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: left;
}

.packages-table th {
    background-color: #f5f5f5;
}
*/

.no-data {
    margin-top: 20px;
    color: #888;
}

.search-bar input {
    flex: 1;
    min-width: 150px;
    padding: 10px 14px;
    border: 1px solid #ccc;
    border-radius: 8px;
    /* 圓角 */
    outline: none;
    transition: border-color 0.3s, box-shadow 0.3s;
}

.search-bar input:focus {
    border-color: #4a90e2;
    /* 聚焦邊框色 */
    box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
    /* 聚焦外光暈 */
}

.search-bar button {
    padding: 10px 20px;
    background: #4a90e2;
    color: #fff;
    border: none;
    border-radius: 8px;
    /* 圓角 */
    cursor: pointer;
    transition: background 0.3s;
}

.search-bar button:hover {
    background: #357ab7;
    /* 滑過時顏色稍深 */
}

.packages-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: 12px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.packages-table thead {
  background-color: #e0e7ef; /* 表頭底色稍微深一些 */
}

.packages-table th,
.packages-table td {
  padding: 16px 20px;
  text-align: left;
  font-size: 20px;
}

.packages-table th {
  font-weight: 600;
  color: #222; /* 表頭文字再深一點 */
  border-bottom: 2px solid #ddd; /* 表頭和內容分隔線稍粗 */
}

.packages-table td {
  color: #555;
  border-bottom: 1px solid #eee; /* 每列間的框線（淡灰色） */
}

.packages-table tbody tr:last-child td {
  border-bottom: none; /* 最後一列去掉底線 */
}

.packages-table tbody tr:hover {
  background-color: #f9fbfd;
  transition: background-color 0.2s;
}


</style>
