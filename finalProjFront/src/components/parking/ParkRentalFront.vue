<template>
    <div class="container mt-4">
        <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">承租車位</h2>
        </div>
    </div>
    <div class="container mt-4">
    <!-- ✅ 頁籤列：寬度對齊 container -->
    <ul class="nav nav-tabs mb-4" role="tablist">
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: selectedTab === 'rent' }"
          href="#"
          @click.prevent="selectedTab = 'rent'"
        >我要承租車位</a>
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: selectedTab === 'history' }"
          href="#"
          @click.prevent="selectedTab = 'history'"
        >查看歷史紀錄</a>
      </li>
    </ul>
    
    <div v-if="selectedTab === 'rent'">
        <div>
        <!-- 查詢區塊 -->
<label class="form-label mt-2">承租起始年月：</label>
<input type="month" class="form-control" v-model="queryStartMonth" :min="minMonth" />

<label class="form-label mt-2">承租截止年月：</label>
<input type="month" class="form-control" v-model="queryEndMonth" :min="minMonth" />


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

                    <label class="form-label mt-2">承租起始年月：</label>
<input type="month" class="form-control" v-model="rentStartMonth" :min="minMonth" />

<label class="form-label mt-2">承租截止年月：</label>
<input type="month" class="form-control" v-model="rentEndMonth" :min="rentStartMonth" />


                    <label class="form-label mt-2">登記車牌：</label>
                    <input type="text" class="form-control" v-model="licensePlate" placeholder="僅限英數，不可含中文" />

                    <div class="text-end mt-3">
                        <button class="btn btn-success" @click="submitRental">送出承租</button>
                    </div>
                </div>
            </div>
        </div>
    </div></div>
    <div v-if="selectedTab === 'history'">
  <div class="container">
    <h3 class="fw-bold mb-3">歷史承租紀錄</h3>
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>車位編號</th>
          <th>區域</th>
          <th>起始</th>
          <th>結束</th>
          <th>登記車牌</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in rentalHistory" :key="record.id">
          <td>{{ record.slotNumber }}</td>
          <td>{{ record.location }}</td>
          <td>{{ record.rentBuyStart }}</td>
          <td>{{ record.rentEnd }}</td>
          <td>{{ record.licensePlate }}</td>
        </tr>
      </tbody>
    </table>
    <p v-if="!rentalHistory.length">尚無歷史紀錄。</p>
  </div>
</div>
    </div>
</template>
    
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from '@/plugins/axios.js'
import { useUserStore } from '@/stores/UserStore'

// 使用者資訊與社區 ID
const userStore = useUserStore()
const communityId = userStore.community

const selectedTab = ref('rent') // 'rent' or 'history'


// 查詢條件
const selectedType = ref(1)
const parkingTypes = ref([])

const queryStartMonth = ref('')
const queryEndMonth = ref('')
const rentStartMonth = ref('')
const rentEndMonth = ref('')
const licensePlate = ref('')

// 車位資料
const availableSlots = ref([])
const rentalSlot = ref(null)
const userName = ref('')

// 最小可選月份：本月
const minMonth = new Date().toISOString().slice(0, 7)

// 補足日期為每月 1 號
function getFirstDayOfMonth(monthStr) {
    return `${monthStr}-01`
}

// 載入車位種類
const fetchType = async () => {
    const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
    parkingTypes.value = res.data.data.map(t => ({
        id: t.id,
        label: t.type
    }))
}

// 查詢可承租車位
async function fetchAvailableSlots() {
    if (!queryStartMonth.value || !queryEndMonth.value || !selectedType.value) return

    const res = await axios.get(`/park/parking-rentals/available-slots?communityId=${communityId}`, {
        params: {
            parkingTypeId: selectedType.value,
            start: getFirstDayOfMonth(queryStartMonth.value),
            end: getFirstDayOfMonth(queryEndMonth.value)
        }
    })
    availableSlots.value = res.data.data
}

// 點選「我要承租」
const rentInitialized = ref(false)



function prepareRental(slot) {
    rentalSlot.value = slot
    userName.value = userStore.email

    // 先暫停 watch
    rentInitialized.value = false

    // 設定初始值
    rentStartMonth.value = queryStartMonth.value
    rentEndMonth.value = queryEndMonth.value
    licensePlate.value = ''

    // 下一個 tick 再啟用 watch（避免立即觸發）
    setTimeout(() => {
        rentInitialized.value = true
    }, 0)
}

// 日期時間格式化（含時分秒）
function formatDateTime2(datetimeStr) {
    return datetimeStr.replace('T', ' ').slice(0, 19)
}

function formatDateOnly(dateStr) {
  const d = new Date(dateStr)
  return d.toISOString().slice(0, 10) // yyyy-MM-dd
}


// 驗證車牌格式
function plateOK(plate) {
    return /^[A-Za-z0-9-]+$/.test(plate)
}

// 提交承租申請
async function submitRental() {
    if (!plateOK(licensePlate.value)) {
        alert('車牌不可含中文，僅能輸入英數字與 -')
        return
    }

    const payload = {
  userName: userName.value,
  slotNumber: rentalSlot.value.slotNumber,
  rentBuyStart: formatDateOnly(getFirstDayOfMonth(rentStartMonth.value)), // yyyy-MM-dd
  rentEnd: formatDateOnly(getFirstDayOfMonth(rentEndMonth.value)),       // yyyy-MM-dd
  licensePlate: licensePlate.value,

  approved: false,
  approverName: null,
  status: false,
  updatedAt: formatDateTime2(new Date().toISOString()),  // yyyy-MM-dd HH:mm:ss
  createdAt: formatDateTime2(new Date().toISOString())
}


    console.log(payload)
    try {
        const res = await axios.post(`/park/parking-rentals?communityId=${communityId}`, payload)
        if (!res.data.data || Object.keys(res.data.data).length === 0) {
            alert('承租失敗，請稍後再試')
            console.warn('後端回傳為空：', res.data)
            return
        }
        alert('承租成功！')
        console.log('後端回傳：', res.data.data)
    } catch (e) {
        console.error(e)
        alert('承租失敗，請稍後再試')
    }
}

// 畫面載入時預設月份
onMounted(() => {
    const today = new Date()
    const year = today.getFullYear()
    const month = today.getMonth() + 1
    const isFirstDay = today.getDate() === 1

    const startDate = new Date(year, isFirstDay ? month - 1 : month, 1)
    const endDate = new Date(startDate)
    endDate.setMonth(startDate.getMonth() + 1)

    queryStartMonth.value = startDate.toISOString().slice(0, 7)
    queryEndMonth.value = endDate.toISOString().slice(0, 7)

    fetchAvailableSlots()
    fetchType()
})

// 搜尋區：起始月變更 → 自動修正截止月不得早於 +1 月
watch([queryStartMonth, queryEndMonth], ([start, end]) => {
    if (!start) return
    const startDate = new Date(start)
    const minEndDate = new Date(startDate)
    minEndDate.setMonth(startDate.getMonth() + 1)
    const minEndStr = minEndDate.toISOString().slice(0, 7)

    if (!end || end < minEndStr) {
        queryEndMonth.value = minEndStr
    }
})


// Modal：起始月變更 → 自動修正截止月不得早於 +1 月
watch([rentStartMonth, rentEndMonth], ([start, end]) => {
    if (!rentInitialized.value || !start) return

    const startDate = new Date(start)
    const minEndDate = new Date(startDate)
    minEndDate.setMonth(startDate.getMonth() + 1)
    const minEndStr = minEndDate.toISOString().slice(0, 7)

    if (!end || end < minEndStr) {
        rentEndMonth.value = minEndStr
    }
})
const rentalHistory = ref([])

async function fetchRentalHistory() {
  const res = await axios.get(`/park/parking-rentals/user/${userStore.email}?communityId=${communityId}`)
  rentalHistory.value = res.data.data || []
}
watch(selectedTab, (tab) => {
  if (tab === 'history') fetchRentalHistory()
})


</script>

    
<style>
    
</style>