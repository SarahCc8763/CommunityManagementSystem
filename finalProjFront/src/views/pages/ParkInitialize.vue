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

const users = ref([
    { id: 1, name: '建商' },
    { id: 2, name: '林淑貞' },
    { id: 3, name: '張大明' }
])

const selectedTypes = ref([])

// 車位資料陣列
const parkingSlots = ref([])

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

// 刪除指定列
function removeRow(index) {
    parkingSlots.value.splice(index, 1)
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
</script>

<style>
    
</style>