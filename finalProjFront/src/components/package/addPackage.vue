<template>
    <div class="resident-packages-container" :class="{ 'dark-mode': isDarkMode }">
        <h1>管理員新增包裹</h1>

        <div class="search-bar" >
          <div class="search-row">
          <label>
            <select v-model="unit1">
              <option disabled value="">請選擇門牌號</option>
              <option v-for="n in [10,12,14,16,18,20,22,24,26,28]" :key="'unit1-' + n" :value="n">{{ n }}</option>
            </select> 之
          </label>

          <label>
            <select v-model="unit2">
              <option value=""></option>
              <option value="1">1</option>
            </select> 號
          </label>

          <label>
            <select v-model="floor1">
              <option disabled value="">請選擇樓層</option>
              <option v-for="n in 12" :key="'floor-' + n" :value="n + 1">{{ n + 1 }}</option>
            </select> 樓
          </label>
        </div>

        <div class="search-row2">
          <!-- 種類 -->
          <label>
            <select v-model="type">
              <option disabled value="">選擇種類</option>
              <option value="包裹">包裹</option>
              <option value="掛號">掛號</option>
              <option value="冷凍包裹">冷凍包裹</option>
            </select>
          </label>

          <!-- 件數 -->
          <label>
            <input
              type="number"
              v-model="piece"
              min="1"
              max="10"
              placeholder="件數 (1-10)"
            /> 件
          </label>

          <!-- 放置位置 -->
          <label class="place">
            <select v-model="place">
              <option disabled value="">放置位置</option>
              <option value="管理室">管理室</option>
              <option value="管理室冰箱">管理室冰箱</option>
            </select>
          </label>
          
          <button @click="addPackages">新增包裹</button>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref,onMounted, watch } from 'vue'
import axios from '@/plugins/axios';
import Swal from 'sweetalert2'
import dayjs from 'dayjs';
import { useUserStore } from '@/stores/UserStore';
const userStore = useUserStore();
let stompClient = null

const unit1 = ref('');
const unit2 = ref('');
const floor1 = ref('');
const piece = ref(1);
const type = ref('');
const place = ref('');
const status = ref('');

async function addPackages() {

    // 檢查是否有選擇門牌號1
    if(!unit1.value){
      Swal.fire({
                    text: "請選擇門牌號1", icon: "warning" });
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
                    text: "請選擇樓層", icon: "warning" });
      return;
    }
    floor += 'F';

    if (!piece.value || piece.value < 1) {
      Swal.fire({ text: "請輸入有效件數", icon: "warning" });
      return;
    }
    if (!type.value) {
      Swal.fire({ text: "請選擇種類", icon: "warning" });
      return;
    }
    if (!place.value) {
      Swal.fire({ text: "請選擇放置位置", icon: "warning" });
      return;
    }

    const payload = {
      unit,
      floor,
      type: type.value,
      piece: piece.value,
      place: place.value,
      status: status.value,
      communityId: userStore.communityId,
    }
    console.log(floor);
    console.log('🚀 payload:', payload);
  try{
    const res = await axios.post(`/packages`,payload);
    if(res.data.success){
    console.log(res.data.success);
    Swal.fire({ text: "新增成功", icon: "success" });
    unit1.value = '';
    unit2.value = '';
    floor1.value = '';
    piece.value = 1;
    type.value = '';
    place.value = '';
    status.value = '';
    }else
    Swal.fire({ text: "無此門牌", icon: "warning" });
  } catch(error) {
    Swal.fire({
                  text: "新增錯誤",
                  icon: "error",
              });
  }
    
}

</script>

<style scoped>


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

.search-bar {
  display: flex;
  flex-direction: column;
  align-items: flex-start;   /* 左對齊每一行 */
  gap: 20px;
  margin-bottom: 2rem;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}


.search-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;      /* 垂直置中 */
  justify-content: flex-start; /* 這裡改為左對齊 */
  gap: 20px;
  width: 100%; /* 讓對齊效果一致 */
}

.search-row2 {
  display: flex;
  flex-wrap: wrap;
  align-items: center;      /* 垂直置中 */
  justify-content: flex-start; /* 這裡改為左對齊 */
  gap: 45px;
  width: 100%; /* 讓對齊效果一致 */
}

.search-bar label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  color: #333;
  font-weight: 500;
}

.search-bar select,
.search-bar input {
  width: 150px;         
  box-sizing: border-box;  /* 包含 padding */
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.search-bar select:focus,
.search-bar input:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
}

.search-bar input[type="number"] {
  width: 150px;
}

.place {
  margin-left: -25px;
}

.search-bar button {
  min-width: 150px;   /* 按鈕統一寬度 */
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

.no-data {
  text-align: center;
  color: #888;
  margin-top: 1rem;
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

/* dark mode */

.dark-mode .resident-packages-container {
  background: #1e1e1e;
  border: 1px solid #444;
  color: #f0f0f0;
}

.dark-mode .resident-packages-container h1 {
  color: #ffffff;
}

.dark-mode .search-bar {
  background: #2a2a2a;
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.05);
}

.dark-mode .search-bar label {
  color: #ddd;
}

.dark-mode .search-bar select,
.dark-mode .search-bar input {
  background-color: #333;
  color: #f0f0f0;
  border: 1px solid #555;
}

.dark-mode .search-bar select:focus,
.dark-mode .search-bar input:focus {
  border-color: #66afe9;
  box-shadow: 0 0 0 3px rgba(102, 175, 233, 0.2);
}

.dark-mode .search-bar button {
  background: #4a90e2;
  color: #fff;
}

.dark-mode .search-bar button:hover {
  background: #2f72c1;
}

.dark-mode .no-data {
  color: #bbb;
}

.dark-mode .packages-table {
  background: #2a2a2a;
  color: #e0e0e0;
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.05);
}

.dark-mode .packages-table thead {
  background-color: #3a3a3a;
}

.dark-mode .packages-table th {
  color: #ffffff;
  border-bottom: 2px solid #666;
}

.dark-mode .packages-table td {
  color: #e0e0e0;
  border-bottom: 1px solid #444;
}

.dark-mode .packages-table tbody tr:hover {
  background-color: #444;
}



</style>
