<template>
    <div class="resident-packages-container">
        <h1>管理員包裹查詢</h1>

        <div class="search-bar" >
          <label>
    <select v-model="unit1">
      <option disabled value="">請選擇門牌號</option>
      <option v-for="n in 15" :key="'unit1-' + n" :value="n">{{ n }}</option>
    </select> 之
  </label>

  <label>
    <select v-model="unit2">
      <option value=""></option>
      <option v-for="n in 15" :key="'unit2-' + n" :value="n">{{ n }}</option>
    </select> 號
  </label>

  <label>
    <select v-model="floor1">
      <option disabled value="">請選擇樓層</option>
      <option v-for="n in 15" :key="'floor-' + n" :value="n">{{ n }}</option>
    </select> 樓
  </label>
            <button @click="searchAllPackages">查詢</button>
        </div>

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
                    <td> 
                      <select v-model="pkg.status" @change="updateStatus(pkg)">
                        <option value="未領取">未領取</option>
                        <option value="已領取">已領取</option>
                      </select>
                    </td>
                </tr>
            </tbody>
        </table>

        <p v-else class="no-data">尚無符合條件的包裹</p>
    </div>
</template>

<script setup>
import { ref,onMounted, watch } from 'vue'
import axios from '@/plugins/axios';
import Swal from 'sweetalert2'
import dayjs from 'dayjs';
import { useUserStore } from '@/stores/UserStore';
const userStore = useUserStore();
const displayedPackages = ref([])
let stompClient = null

const unit1 = ref('');
const unit2 = ref('');
const floor1 = ref('');

async function searchAllPackages() {
    // const unitId = userStore.unitId; // 從 store 拿
    // console.log(unitId);
    // const payload = {
    //     // ...searchFormData,
    //     unitId: unitId // 放進要送到後端的 DTO
    // };

    // const token = localStorage.getItem('token'); // 如果有 JWT 的話

    displayedPackages.value = [];

    // 檢查是否有選擇門牌號1
    if(!unit1.value){
      Swal.fire({
                    text: "請選擇門牌號1",
                    icon: "warning",
                });
    return;
    }

    // 組合 unit 字串
    let unit = unit1.value;
    if (unit2.value && unit2.value !== '') {
      unit += '-' + unit2.value;
    }
console.log(unit);
    // 檢查樓層
    let floor = floor1.value;
    if (!floor1.value) {
      Swal.fire({
                    text: "請選擇樓層",
                    icon: "warning",
                });
      return;
    }
    floor += 'F';

    const payload = {
      unit,
      floor
    }
    console.log(floor);
    console.log('🚀 payload:', payload);
  try{
    const response = await axios.post(`/packages/unit`,payload);

    console.log(response.data);
    if (response.data) {
    displayedPackages.value = response.data.filter(pkg => pkg.status === '未領取') // 取真正的包裹陣列
    } else {
    Swal.fire({
                    text: "無包裹",
                    icon: "warning",
                });
    console.error('查詢失敗:', response.data.message)

    displayedPackages.value = []
    } 
  } catch(error) {
    Swal.fire({
                  text: "無包裹",
                  icon: "warning",
              });
  }
    
}

async function updateStatus(pkg) {
  console.log('更新包裹 ID:', pkg.packagesId, '新狀態:', pkg.status);

  if (pkg.status === '已領取') {
    try {
      const response = await axios.put(`/packages/pickup/${pkg.packagesId}`);
      if (response.data.success) {
        Swal.fire({
          text: "狀態已更新為已領取！",
          icon: "success"
        });
        searchAllPackages()
      } else {
        Swal.fire({
          text: response.data.message || "更新失敗",
          icon: "error"
        });
        // 如果後端失敗，回滾狀態
        pkg.status = '未領取';
      }
    } catch (error) {
      console.error(error);
      Swal.fire({
        text: "系統錯誤，請稍後再試",
        icon: "error"
      });
      // 回滾
      pkg.status = '未領取';
    }
  } 
  // else {
  //   // 如果使用者把狀態改回未領取，你可以視需求要不要處理
  //   console.log('狀態改回未領取，不執行 PUT');
  // }
}



// onMounted(() => {
//   searchAllPackages()
// })
</script>

<style scoped>
/* .resident-packages-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

h1 {
    margin-bottom: 20px;
}*/

.search-bar {
    display: flex;
    flex-wrap: wrap;
    align-items: center;     /* 垂直置中 */
  justify-content: center; /* 水平置中 */
    gap: 10px;
    /* margin-bottom: 20px; */
    padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.no-data {
    margin-top: 20px;
    color: #888;
}

.search-bar label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  color: #333;
  font-weight: 500;
}

.search-bar select {
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.search-bar select:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
}


.search-bar input {
    flex: 1;
    min-width: 150px;
    padding: 10px 14px;
    border: 1px solid #ccc;
    border-radius: 8px;
    outline: none;
    transition: border-color 0.3s, box-shadow 0.3s;
}

.search-bar input:focus {
    border-color: #4a90e2;
    box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
}

.search-bar button {
  padding: 12px 24px;
  background: #4a90e2;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s, transform 0.2s;
}

.search-bar button:hover {
    background: #357ab7;
    transform: translateY(-1px);
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
  /* color: #555; */
  color: #222;
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
  flex-wrap: nowrap;
  margin-bottom: 2rem;
  align-items: center;
  justify-content: center; /* 水平置中 */
}

.search-bar label {
  /* flex-basis: 100%; */
  flex-basis: auto;
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
  margin: 0;
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
