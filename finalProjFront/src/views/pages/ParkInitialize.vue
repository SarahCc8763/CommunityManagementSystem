<template>
    <div>
        <h2>新增車位資料</h2>
        <div>
            <h4>請勾選您社區的車位種類：</h4>
                <div v-for="type in parkingTypes" :key="type.id">
                    <label>
                        <input type="checkbox" :value="type" v-model="selectedTypes" />
                        {{ type.label }}
                    </label>
                </div>
                <p>目前選取的種類：{{ selectedTypes.map(t => t.label).join('、') }}</p>

            </div>
            <div>
                <div class="p-4">
                    <h2 class="text-xl font-bold mb-4">上傳車位 CSV</h2>
                    
                    <!-- 檔案上傳 -->
                    <input type="file" accept=".csv" @change="handleFileUpload" />
                    <div v-if="parkingSlots.length" class="mt-6">
                        <h3 class="text-lg font-semibold mb-2">預覽資料</h3>
                        <table border="1" class="w-full border-collapse">
                            <thead>
                                <tr>
                                    <th>車位代碼</th>
                                    <th>位置</th>
                                    <th>車位種類</th>
                                    <th>車位擁有人</th>
                                    <th>車牌號碼</th>
                                    <th>是否可承租</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(slot, index) in parkingSlots" :key="index">
                                    <td><input v-model="slot.slotNumber" /></td>
                                    <td><input v-model="slot.location" /></td>
                                    <!-- ✅ parking_type 下拉 -->
                                    <td>
                                        <select v-model="slot.parkingTypeId">
                                            <option v-for="type in selectedTypes" :key="type.id" :value="type.id">
                                                {{ type.label }}
                                            </option>
                                        </select>
                                    </td>
                                    <!-- ✅ users_id 下拉 -->
                                    <td>
                                        <select v-model="slot.usersId">
                                            <option v-for="user in users" :key="user.id" :value="user.id">
                                                {{ user.name }}
                                            </option>
                                        </select>
                                    </td>
                                    <td><input v-model="slot.licensePlate" /></td>
                                    <td>
                                        <select v-model="slot.isRentable">
                                            <option :value="true">可承租</option>
                                            <option :value="false">不可承租</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button @click="removeRow(index)">刪除</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <button class="mt-4 bg-green-500 px-4 py-1 rounded" @click="addRow">新增一列</button>
                        <button class="mt-4 ml-4 bg-blue-600 px-4 py-1 rounded" @click="submitData">提交資料</button>
                    </div>
                </div>
            </div>
        </div>
<!-- 
    -> 選擇車位種類 (新增) -> 上傳csv新增車位資料 (管理員)
    承租社區車位 -> 以時間區段、車種查詢可承租車位 -> 點選後進入新增承租頁面 (一般用戶) -> 管理員審核
    -> 搜尋自己的承租資訊 (一般用戶) -> 修改承租資訊 (一般用戶) -> 管理員審核
    -> 模糊搜尋所有承租資訊 (管理員) -> 修改承租資訊 (管理員) -> 2次確認
    臨時停車申請 -> 以時間區段、車種申請臨時停車 (一般住戶) -> 即時顯示是否overlapping -> 管理員審核
    -> 搜尋自己的臨時停車 (一般用戶) -> 修改臨時停車 (一般用戶) -> 管理員審核
    -> 模糊搜尋所有臨時停車 (管理員) -> 修改臨時停車 (管理員) -> 2次確認
    抽籤活動     -> 新增抽籤活動及抽籤車位 (管理員)
    -> 模糊查詢所有抽籤活動 -> 修改抽籤活動 (管理員) -> 2次確認
    -> 模糊查詢所有抽籤活動 -> 申請參與 (一般用戶) / 取消參與
    -> 搜尋自己參與的抽籤 (一般用戶) -> 修改臨時停車 (一般用戶) -> 管理員審核
    
    <button @click="testRead">查詢</button>
    <button @click="testCreate">新增</button>
    <button @click="testUpdate">修改</button>
    <button @click="testDelete">刪除</button>
    -->
</template>

<script setup>
import { ref } from 'vue'

import axios from '@/plugins/axios.js';

const parkingTypes = [
    { id: 1, label: '汽車' },
    { id: 2, label: '機車' },
    { id: 3, label: '電動車' },
    { id: 4, label: '殘障車位' }
]

const selectedTypes = ref([])

// 車位資料陣列
const parkingSlots = ref([])

const users = ref([
    { id: 1, name: '建商' },
    { id: 2, name: '林淑貞' },
    { id: 3, name: '張大明' }
])

// 上傳並解析 CSV 檔案
function handleFileUpload(event) {
    const file = event.target.files[0]
    if (!file) return
    
    const reader = new FileReader()
    reader.onload = (e) => {
    const lines = e.target.result.split('\n').filter(line => line.trim() !== '')
    const result = []

    for (let line of lines.slice(1)) {
        const [slot_number, location, parking_type_id, users_id, license_plate, is_rentable] = line.split(',')
        result.push({
            slotNumber: slot_number.trim(),
            location: location.trim(),
            parkingTypeId: parseInt(parking_type_id),
            usersId: parseInt(users_id),
            licensePlate: license_plate.trim(),
            isRentable: is_rentable.trim().toLowerCase() === 'true'
})

    }    
    parkingSlots.value = result
}

reader.readAsText(file)
}

// 新增空白列
function addRow() {
    parkingSlots.value.push({
        slotNumber: '',
        location: '',
        parkingTypeId: 1,
        usersId: 1,
        licensePlate: '',
        isRentable: false
})

}

// 刪除指定列
function removeRow(index) {
    parkingSlots.value.splice(index, 1)
}

// 提交資料（發送 API）
function submitData() {
    console.log('即將送出的資料:', JSON.stringify(parkingSlots.value, null, 2))
    axios.post('/park/parking-slots/create-all', parkingSlots.value)
    .then(response => {
        console.log('成功:', response.data);
        alert('成功送出車位資料！');
    })
    .catch(error => {
        console.error('送出失敗:', error);
        alert('送出失敗，請查看 console log');
    });
}


// const testCreate = () => {
//     axios.post('/park/parking-types', {
//         type: '殘障車位'
//     })
//     .then(response => {
//         console.log('成功' + response.data);
//     })
//     .catch(error => {
//         console.error(error);
//     });
// }
// const testUpdate = () => {
//     axios.put('/park/parking-types/7', {
//         type: '一般車位'
//     })
//     .then(response => {
//         console.log('成功' + response.data);
//     })
//     .catch(error => {
//         console.error(error);
//     });
// }
// const testDelete = () => {
//     axios.delete('/park/parking-types/7')
//     .then(response => {
//         console.log('成功' + response.data);
//     })
//     .catch(error => {
//         console.error(error);
//     });
// }
// const testRead = () => {
//     axios.get('/park/parking-types').then(response => {
//         console.log(response.data);
//     })
//     .catch(error => {
//         console.error(error);
//     });
// }
</script>

<style>
    
    </style>