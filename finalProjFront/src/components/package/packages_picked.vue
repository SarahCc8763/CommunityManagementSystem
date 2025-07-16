<template>
    <div class="resident-packages-container">
        <h1>我的包裹領取紀錄</h1>

        <!-- <div class="search-bar">
            <input v-model="residentName" type="text" placeholder="輸入姓名" />
            <input v-model="unitNumber" type="text" placeholder="輸入門牌號" />
            <button @click="searchMyPackages">查詢</button>
        </div> -->

        <table v-if="displayedPackages.length" class="packages-table">
            <thead>
                <tr>
                    <th>編號</th>
                    <th>件數</th>
                    <th>包裹描述</th>
                    <th>到達時間</th>
                    <th>放置地點</th>
                    <th>領取狀態</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="pkg in displayedPackages" :key="pkg.id">
                    <td>{{ pkg.packagesId }}</td>
                    <td>{{ pkg.piece }}</td>
                    <td>{{ pkg.type }}</td>
                    <td>{{ dayjs(pkg.arrivalTime).format('YYYY-MM-DD HH:mm:ss') }}</td>
                    <td>{{ pkg.place }}</td>
                    <td>{{ pkg.status }}</td>
                </tr>
            </tbody>
        </table>

        <p v-else class="no-data">尚無符合條件的包裹</p>
    </div>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import axios from '@/plugins/axios';
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/UserStore';
const userStore = useUserStore();
const displayedPackages = ref([])

async function searchMyPackages() {
    const unitId = userStore.unitId; // 從 store 拿
    console.log(unitId);
    const payload = {
        // ...searchFormData,
        unitId: unitId // 👈 放進要送到後端的 DTO
    };

    // const token = localStorage.getItem('token'); // 如果有 JWT 的話

    const response = await axios.post('/packages/search', payload);

    console.log(response.data);
    if (response.data.success) {
    displayedPackages.value = response.data.data.filter(pkg => pkg.status === '已領取') // 取真正的包裹陣列
  } else {
    console.error('查詢失敗:', response.data.message)
    displayedPackages.value = []
  }
}

onMounted(() => {
  searchMyPackages()
})
</script>

<style scoped>

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
  background-color: #e0e7ef; 
}

.packages-table th,
.packages-table td {
  padding: 16px 20px;
  text-align: left;
  font-size: 20px;
}

.packages-table th {
  font-weight: 600;
  color: #222; 
  border-bottom: 2px solid #ddd; 
}

.packages-table td {
  color: #555;
  border-bottom: 1px solid #eee; 
}

.packages-table tbody tr:last-child td {
  border-bottom: none; 
}

.packages-table tbody tr:hover {
  background-color: #f9fbfd;
  transition: background-color 0.2s;
} 

.resident-packages-container {
  display: block;
  width: 100%;
  max-width: 1000px;
  min-width: 600px;  /* 撐住 */
  margin: 80px auto;
  padding: 3rem;
  background: #fefefe;
  border: 1px solid #ddd;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
}

.resident-packages-container h1 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
  color: #222;
}

/* 搜尋欄改卡片風格，像 form-group */
.search-bar {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 2rem;
}

.search-bar label {
  flex-basis: 100%;
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.search-bar input {
  flex: 1;
  min-width: 200px;
  padding: 12px 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.search-bar input:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.search-bar button {
  padding: 12px 24px;
  background: #4a90e2;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background 0.3s;
}

.search-bar button:hover {
  background: #357ab7;
}

.no-data {
  text-align: center;
  color: #888;
  margin-top: 1rem;
}


</style>
