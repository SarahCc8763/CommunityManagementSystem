<template>
    <h2>可承租車位</h2>
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
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="slot in availableSlots" :key="slot.id">
                    <td>{{ slot.id }}</td>
                    <td>{{ slot.slotNumber }}</td>
                    <td>{{ slot.location }}</td>
                    <td>
                        <!-- data-bs-toggle / data-bs-target 交給 Bootstrap 處理開窗 -->
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#rentalModal"
                            @click="prepareRental(slot)">
                            我要承租
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>尚無可承租車位。</p>
    </div>

    <!-- Bootstrap Modal -->
    <div class="modal fade" id="rentalModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content p-3">
                <div class="modal-header">
                    <h5 class="modal-title">承租車位</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- v-if：只有在資料備妥才顯示 -->
                <div class="modal-body" v-if="rentalSlot">
                    <p><strong>承租者：</strong>{{ userName }}</p>
                    <p><strong>車位代碼：</strong>{{ rentalSlot.slotNumber }}</p>
                    <p><strong>車位區域：</strong>{{ rentalSlot.location }}</p>

                    <label class="form-label mt-2">承租起始日：</label>
                    <input type="datetime-local" class="form-control" v-model="rentStart" :min="minStartDate" />

                    <label class="form-label mt-2">承租截止日：</label>
                    <input type="datetime-local" class="form-control" v-model="rentEnd" :min="minEndDate" />

                    <label class="form-label mt-2">登記車牌：</label>
                    <input type="text" class="form-control" v-model="licensePlate" placeholder="僅限英數，不可含中文" />

                    <div class="text-end mt-3">
                        <button class="btn btn-success" @click="submitRental">送出承租</button>
                    </div>
                </div>
            </div>
        </div>
    </div>


</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from '@/plugins/axios.js'
import useUserStore from '@/stores/user.js';

const userStore = useUserStore();
const communityId = userStore.community

const start = ref('')
const end = ref('')
const parkingTypes = ref([])
const selectedType = ref(1)

const fetchType = async () => {
    const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
    parkingTypes.value = res.data.data.map(t => ({
        id: t.id,
        label: t.type
    }))
}

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

const rentalSlot = ref(null)
const userName = ref('')
const rentStart = ref('')
const rentEnd = ref('')
const licensePlate = ref('')

const minStartDate = new Date().toISOString().slice(0, 16)
const minEndDate = computed(() => {
    if (!rentStart.value) return ''
    const d = new Date(rentStart.value)
    d.setMonth(d.getMonth() + 1)      // 至少 +1 月
    return d.toISOString().slice(0, 16)
})

/* -------- 點「我要承租」時呼叫 -------- */
function prepareRental(slot) {
    rentalSlot.value = slot
    // 取得登入者姓名（假設 localStorage 暫存 userId）
    //   fetchUserName()

    rentStart.value = minStartDate
    rentEnd.value = minEndDate.value
    licensePlate.value = ''
}

/* -------- 後端撈取使用者名稱 -------- */
// async function fetchUserName () {
//   try {
//     const uid = JSON.parse(localStorage.getItem('user'))?.id
//     const { data } = await axios.get(`/users/${uid}`)
//     userName.value = data.name
//   } catch (e) {
//     console.error('取得使用者名稱失敗', e)
//     userName.value = '(未知)'
//   }
// }

/* -------- 檢查車牌 & 送出 -------- */
function plateOK(plate) {
    return /^[A-Za-z0-9-]+$/.test(plate)   // 僅英數與 dash
}

async function submitRental() {
    if (!plateOK(licensePlate.value)) {
        alert('車牌不可含中文，僅能輸入英數字與 -')
        return
    }

    try {
        const payload = {
            // usersId: JSON.parse(localStorage.getItem('user')).id,
            usersId: 2,
            parkingSlotId: rentalSlot.value.id,
            rentBuyStart: rentStart.value.replace('T', ' ') + ':00',
            rentEnd: rentEnd.value.replace('T', ' ') + ':00',
            licensePlate: licensePlate.value,
            status: false
        }

        const res = await axios.post('/park/parking-rentals', payload)

        // 判斷回傳資料是否為空或無效
        if (!res.data || Object.keys(res.data).length === 0) {
            alert('承租失敗，請稍後再試')
            console.warn('後端回傳為空：', res)
            return
        }

        alert('承租成功！')
        console.log('後端回傳：', res.data)
    } catch (e) {
        console.error(e)
        alert('承租失敗，請稍後再試')
    }
}


onMounted(() => {
    const now = new Date()
    const isoStr = now.toISOString().slice(0, 16)
    start.value = isoStr
    const later = new Date(now.getTime() + 60 * 60 * 1000)
    end.value = later.toISOString().slice(0, 16)
    fetchAvailableSlots()
    fetchType()
})

watch([start, end, selectedType], ([newStart, newEnd, newType]) => {
    if (newStart && newEnd && newType) {
        fetchAvailableSlots()
    }
})

</script>