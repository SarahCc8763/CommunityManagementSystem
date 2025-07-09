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
            <div class="search-wrapper d-flex align-items-center gap-2 flex-wrap w-100">
  <!-- 車位種類 -->
  <div class="input-box d-flex align-items-center px-3 py-2 flex-fill">
    <i class="bi bi-sliders2 me-2 text-secondary"></i>
    <select class="form-select border-0 shadow-none" v-model="selectedType">
      <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
    </select>
  </div>

  <!-- 日期 -->
  <div class="input-box d-flex align-items-center px-3 py-2 flex-fill">
    <i class="bi bi-calendar3 me-2 text-secondary"></i>
    <input type="month" class="form-control border-0 shadow-none" v-model="queryStartMonth" :min="minMonth" />
    <span class="mx-2">—</span>
    <input type="month" class="form-control border-0 shadow-none" v-model="queryEndMonth" :min="minMonth" />
  </div>

  <!-- 區域 -->
  <div class="input-box d-flex align-items-center px-3 py-2 flex-fill">
    <i class="bi bi-car-front me-2 text-secondary"></i>
    <input type="text" class="form-control border-0 shadow-none" placeholder="搜尋樓層或區域" v-model="searchKeyword" />
  </div>
</div>
    </div>




<!-- 美化後的查詢結果區塊 -->
<div class="section-title d-flex align-items-center gap-2 mt-4 mb-3 ps-3 border-start border-4 border-primary">
  <i class="bi bi-list-task fs-4 text-primary"></i>
  <h3 class="fw-bold mb-0 text-primary-emphasis">查詢結果</h3>
</div>


<div v-if="filteredSlots.length" class="row g-3">
    <div class="col-md-6 col-lg-4" v-for="slot in filteredSlots" :key="slot.id">
    <div class="card shadow-sm border-0 h-100 rounded-4">
      <div class="card-body d-flex flex-column justify-content-between">
        <div>
          <h5 class="card-title fw-bold text-primary-emphasis mb-2">
            {{ slot.slotNumber }}
            <span class="badge bg-secondary ms-2">{{ slot.type }}</span>
          </h5>
          <p class="card-text mb-1"><i class="bi bi-geo-alt-fill me-1 text-muted"></i>位置：{{ slot.location }}</p>
        </div>
        <div class="text-end mt-3">
          <button
            class="btn rent-btn px-4 py-2"
            @click="openRentalModal(slot)"
          >
            我要承租
          </button>
        </div>
      </div>
    </div>
  </div>
</div>

<p v-else class="text-muted">尚無可承租車位。</p>


    <!-- Bootstrap Modal -->
    <div class="modal fade" id="rentalModal" tabindex="-1" aria-hidden="true" ref="modalElement">
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





    <div v-if="selectedTab === 'history'" class="container">
  <h3 class="fw-bold mb-3">歷史承租紀錄</h3>

  <div class="table-wrapper">
    <div class="table-container">
      <table class="table table-hover table-bordered fixed-header-table">
        <thead class="table-light text-center">
          <tr>
            <th>車位編號</th>
            <th>種類</th>
            <th>位置</th>
            <th>登記車牌</th>
            <th>起始</th>
            <th>結束</th>
            <th>繳費</th>
            <th>審核</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in rentalHistory" :key="record.id" class="text-center">
            <td>{{ record.slotNumber }}</td>
            <td>{{ record.parkingType }}</td>
            <td>{{ record.location }}</td>
            <td>{{ record.licensePlate }}</td>
            <td>{{ record.rentBuyStart }}</td>
            <td>{{ record.rentEnd }}</td>

            <td>
              <span :class="['badge status-badge', record.status ? 'status-yes' : 'status-no']">
                {{ record.status ? '已繳費' : '未繳費' }}
              </span>
            </td>
            <td>
              <span :class="['badge status-badge', record.approved ? 'status-yes' : 'status-no']">
                {{ record.approved ? '已審核' : '待審核' }}
              </span>
            </td>
            <td>
              <button class="btn btn-primary btn-sm" @click="viewDetails(record)">點我繳費</button>
              <button class="btn btn-primary btn-sm" @click="viewDetails(record)">續租</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="!rentalHistory.length" class="text-muted text-center my-3">尚無歷史紀錄。</p>
    </div>
  </div>
</div>

    </div>
</template>
    
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from '@/plugins/axios.js'
import Swal from 'sweetalert2'
import { Modal } from 'bootstrap'
import { useUserStore } from '@/stores/UserStore'

const modalElement = ref(null)
let rentalModalInstance = null


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

// 從資料庫抓user資料
const allUsers = ref([])
const fetchUserOptions = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    allUsers.value = res.data.data.map(user => ({ usersId: user.usersId, name: user.name }))
    console.log(allUsers.value)
}

const usersId = computed(() => {
  const match = allUsers.value.find(user => user.name === userStore.email)
  return match ? match.usersId : null
})


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
    console.log(availableSlots.value)
}

// 點選「我要承租」
const rentInitialized = ref(false)

const filteredSlots = computed(() => {
  if (!searchKeyword.value.trim()) return availableSlots.value
  return availableSlots.value.filter(slot =>
    slot.location.toLowerCase().includes(searchKeyword.value.trim().toLowerCase())
  )
})

function openRentalModal(slot) {
  prepareRental(slot)
  rentalModalInstance?.show()
}


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
    await Swal.fire({
      icon: 'warning',
      title: '車牌格式錯誤',
      text: '車牌不可含中文，僅能輸入英數字與 -',
      confirmButtonText: '關閉'
    });
    return;
  }

  const payload = {
    userName: userStore.email,
    usersId: usersId.value,
    slotNumber: rentalSlot.value.slotNumber,
    rentBuyStart: formatDateOnly(getFirstDayOfMonth(rentStartMonth.value)), // yyyy-MM-dd
    rentEnd: formatDateOnly(getFirstDayOfMonth(rentEndMonth.value)),       // yyyy-MM-dd
    licensePlate: licensePlate.value,
    approved: false,
    approverName: null,
    status: false,
    updatedAt: formatDateTime2(new Date().toISOString()),
    createdAt: formatDateTime2(new Date().toISOString())
  }

  console.log(payload)

  try {
    const res = await axios.post(`/park/parking-rentals?communityId=${communityId}`, payload)
    if (!res.data.data || Object.keys(res.data.data).length === 0) {
      await Swal.fire({
        icon: 'error',
        title: '承租失敗',
        text: '後端回傳為空，請稍後再試',
        confirmButtonText: '關閉'
      });
      return
    }

    await fetchAvailableSlots()
    rentalModalInstance?.hide()

    await Swal.fire({
      icon: 'success',
      title: '承租成功！',
      confirmButtonText: '確定'
    });

    console.log('後端回傳：', res.data.data)

  } catch (e) {
    console.error(e)
    await Swal.fire({
      icon: 'error',
      title: '承租失敗',
      text: e?.response?.data?.message || '請稍後再試',
      confirmButtonText: '關閉'
    });
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

    if (modalElement.value) {
    rentalModalInstance = new Modal(modalElement.value)
  }

    fetchAvailableSlots()
    fetchType()
    fetchUserOptions()
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
  const res = await axios.get(`/park/parking-rentals/user?usersId=${usersId.value}`)
  rentalHistory.value = res.data.data || []
}
watch(selectedTab, (tab) => {
  if (tab === 'history') fetchRentalHistory()
})
const searchKeyword = ref('')
// 自動偵測查詢欄位變動 → 查詢車位
watch([selectedType, queryStartMonth, queryEndMonth, searchKeyword], ([type, start, end, keyword]) => {
  if (!type || !start || !end) return;
  fetchAvailableSlots();
});

</script>

    
<style>
.search-wrapper {
  border-radius: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 16px;
  background: linear-gradient(to right, #ecebff, #e7e9ff);
  border: 1px solid #d0d3e7;
}

.input-box {
  border: 1px solid #d0d3e7;
  border-radius: 12px;
  background-color: white;
  min-width: 180px;
}

.rent-btn {
  background: linear-gradient(to right, #6a11cb, #2575fc);
  border: none;
  color: white;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.rent-btn:hover {
  background: linear-gradient(to right, #5c0ec1, #1f66e2);
  box-shadow: 0 0 8px rgba(100, 100, 255, 0.3);
}

.rent-btn {
  background: linear-gradient(to right, #6a11cb, #2575fc);
  border: none;
  color: white;
  border-radius: 30px;
  transition: all 0.3s ease;
  font-weight: 600;
}

.rent-btn:hover {
  background: linear-gradient(to right, #5c0ec1, #1f66e2);
  box-shadow: 0 0 8px rgba(100, 100, 255, 0.3);
}

/* 外層包住 scroll 或修正定位問題 */
.table-wrapper {
    position: relative;
    z-index: 0;
}

/* 滾動表格區塊 */
.table-container {
    max-height: 500px;
    overflow-y: auto;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    padding: 0;
}

/* 表格固定表頭設定 */
.fixed-header-table {
    border-collapse: separate !important;
    border-spacing: 0 !important;
    margin: 0 !important;
    width: 100%;
}

/* 表頭 sticky 並美化背景 */
.fixed-header-table thead th {
    position: sticky;
    top: 0;
    background-color: #f0f4f8;
    z-index: 10;
    text-align: center;
    font-weight: 700;
    color: #333;
    border-bottom: 2px solid #dee2e6;
    padding: 12px;
}

/* 表格列 hover 效果 */
.fixed-header-table tbody tr:hover {
    background-color: #f9fcff;
}

/* 表格儲存格樣式 */
.fixed-header-table td {
    padding: 10px;
    vertical-align: middle;
    color: #333;
    font-size: 15px;
}

/* 狀態標籤 */
.status-badge {
    font-size: 0.85em;
    padding: 0.4em 0.75em;
    border-radius: 1em;
    font-weight: 600;
    display: inline-block;
    min-width: 80px;
}

/* 承租狀態顏色 */
.status-yes {
    background-color: #e6f4ea;
    color: #2e7d32;
    border: 1px solid #c1e1c1;
}

.status-no {
    background-color: #fdecea;
    color: #c62828;
    border: 1px solid #f5c6cb;
}

/* 操作按鈕樣式一致化 */
.btn-sm {
    border-radius: 20px;
    font-size: 0.9em;
    font-weight: 600;
    padding: 6px 16px;
    transition: all 0.2s ease;
}

</style>