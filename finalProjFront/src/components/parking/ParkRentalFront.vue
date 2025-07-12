<template>
  <div class="container mt-4">
    <div class="tag-style px-4 py-2 mb-4">
      <h2 class="mb-0 fw-bold text-primary section-title">承租車位</h2>
    </div>

    <!-- ✅ 頁籤列：寬度對齊 container -->
    <ul class="nav nav-tabs mb-4" role="tablist">
      <li class="nav-item">
        <a class="nav-link"
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

    <!-- -------------------------- 承租車位頁 -------------------------- -->
    <div v-if="selectedTab === 'rent'">
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

      <!-- 查詢結果區塊 -->
      <div class="section-title d-flex align-items-center gap-2 mt-4 mb-3 ps-3 border-start border-4 border-primary">
        <i class="bi bi-list-task fs-4 text-primary"></i>
        <h3 class="fw-bold mb-0 text-primary-emphasis">查詢結果</h3>
      </div>
      <div v-if="filteredSlots.length" class="row g-3">
        <div class="col-md-6 col-lg-4" v-for="slot in filteredSlots" :key="slot.id">
          <!-- card 本體 -->
          <div class="card shadow-sm border-0 h-100 rounded-4">
            <div class="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 class="card-title fw-bold text-primary-emphasis mb-2">
                  {{ slot.slotNumber }}
                  <span class="badge bg-secondary ms-2">{{ slot.type }}</span>
                </h5>
                <p class="card-text mb-1">
                  <i class="bi bi-geo-alt-fill me-1 text-muted"></i>
                  位置：{{ slot.location }}
                </p>
              </div>
              <div class="text-end mt-3">
                <button class="btn rent-btn px-4 py-2" @click="openRentalModal(slot)">
                  我要承租
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <p v-else class="text-muted">尚無可承租車位。</p>
    </div>

    <!-- -------------------------- 歷史承租頁 -------------------------- -->
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
                <td class="d-flex justify-content-center flex-wrap gap-2">
                  <button class="btn btn-primary btn-sm rounded-pill action-btn" :disabled="!record.approved || !record.canExtend" @click="openExtendModal(record)">
                    續租
                  </button>
                  <button class="btn btn-danger btn-sm rounded-pill action-btn" @click="deleteRecord(record)" :disabled="record.status && record.approved">
                    取消承租
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <p v-if="!rentalHistory.length" class="text-muted text-center my-3">尚無歷史紀錄。</p>
        </div>
      </div>
    </div>
  </div>
  <!-- 我要承租 Modal -->
  <div class="modal fade" id="rentalModal" tabindex="-1" aria-hidden="true" ref="modalElement">
    <div class="modal-dialog">
      <div class="modal-content p-3">
        <div class="modal-header">
          <h5 class="modal-title">承租車位</h5>
          <button type="button" class="btn-close" @click="handleClose"></button>
        </div>
        <p><strong>承租者：</strong>{{ userName }}</p>
        <p><strong>車位代碼：</strong>{{ rentalSlot.slotNumber }}</p>
        <p><strong>車位區域：</strong>{{ rentalSlot.location }}</p>
        <label class="form-label mt-2">承租起始年月：</label>
        <input type="month" class="form-control" v-model="rentStartMonth" :min="minMonth" />
        <input type="month" class="form-control" v-model="rentEndMonth" :min="rentStartMonth" />
        <label class="form-label mt-2">登記車牌：</label>
        <input type="text" class="form-control" v-model="licensePlate" placeholder="僅限英數，不可含中文" />
        <div class="text-end mt-3">
          <button class="btn btn-success" @click="submitRental">送出承租</button>
        </div>
      </div>
    </div>
  </div>
  <!-- 續租 Modal -->
  <div class="modal fade" id="extendModal" tabindex="-1" ref="extendModalRef">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">續租設定</h5>
          <button type="button" class="btn-close" @click="handleCloseExtendModal"></button>
        </div>
        <div class="modal-body">
          <p><strong>車位編號：</strong>{{ selectedSlot.slotNumber }}</p>
          <p><strong>車位種類：</strong>{{ selectedSlot.parkingType }}</p>
          <p><strong>位置：</strong>{{ selectedSlot.location }}</p>
          <label class="form-label">登記車牌：</label>
          <input type="text" class="form-control mb-3" v-model="selectedSlot.licensePlate" />
          <p><strong>承租起始：</strong>{{ extendStart }}</p>
          <p><strong>承租截止：</strong>{{ extendEnd }}</p>
          <label class="form-label">續租月數</label>
          <select class="form-select" v-model="extendMonths">
            <option v-for="m in 12" :value="m">{{ m }} 個月</option>
          </select>
        </div>
        <div class="modal-footer">
          <button class="btn btn-success" @click="submitExtend">確認續租</button>
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

// 從 UserStore 取出社區 ID
const userStore = useUserStore()
const communityId = userStore.community

const userName = userStore.name

// 從資料庫抓user資料
const allUsers = ref([])
const fetchUserOptions = async () => {
    const res = await axios.get(`/users?communityId=${communityId}`)
    allUsers.value = res.data.data.map(user => ({ usersId: user.usersId, name: user.name }))
    console.log(allUsers.value)
}

// 從資料庫比對userName，取得userId
const usersId = computed(() => {
  console.log(userName);
  const match = allUsers.value.find(user => user.name === userName)
  return match ? match.usersId : null
})

// ----------------------------我要承租頁籤 -----------------------------

// 頁籤
const selectedTab = ref('rent')

// 車位種類
const selectedType = ref(null)
const parkingTypes = ref([])

// 載入車位種類
const fetchType = async () => {
  console.log("communityId: " + communityId.value)
  const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
  parkingTypes.value = res.data.data.map(t => ({
    id: t.id,
    label: t.type
  }))
  console.log(parkingTypes.value)
  
  // 設定預設 selectedType 為第一筆
  if (parkingTypes.value.length > 0) {
    selectedType.value = parkingTypes.value[0].id
  }
}

// 承租月份查詢
const queryStartMonth = ref('')
const queryEndMonth = ref('')
// 最小可選月份：本月
const minMonth = new Date().toISOString().slice(0, 7)

// 搜尋區：起始月變更 → 自動修正截止月不得早於 +1 月
watch([queryStartMonth, queryEndMonth], ([start, end]) => {
  if (!start) return
  const startDate = new Date(start)
  const minEndDate = new Date(startDate)
  minEndDate.setMonth(startDate.getMonth())
  const minEndStr = minEndDate.toISOString().slice(0, 7)
  
  if (!end || end < minEndStr) {
    queryEndMonth.value = minEndStr
  }
})

// 可承租車位
const availableSlots = ref([])

// 查詢可承租車位
async function fetchAvailableSlots() {
  console.log("selectedType: " + selectedType.value + ", queryStartMonth: " + queryStartMonth.value + ", queryEndMonth: " + queryEndMonth.value)
  if (!queryStartMonth.value || !queryEndMonth.value || !selectedType.value) return
  console.log(getLastDayOfMonth(queryEndMonth.value));
  const res = await axios.get(`/park/parking-rentals/available-slots`, {
    params: {
      parkingTypeId: selectedType.value,
      start: getFirstDayOfMonth(queryStartMonth.value),
      end: getLastDayOfMonth(queryEndMonth.value)
    }
  })
  availableSlots.value = res.data.data
  console.log(availableSlots.value)
}

// 補足日期為每月 1 號
function getFirstDayOfMonth(input) {
  let year, month

  if (typeof input === 'string') {
    [year, month] = input.split('-').map(Number)
  } else if (input instanceof Date) {
    year = input.getFullYear()
    month = input.getMonth() + 1
  } else {
    return '-'
  }

  const yyyy = year
  const mm = String(month).padStart(2, '0')
  return `${yyyy}-${mm}-01`
}


// 補足日期為每月最後一天
function getLastDayOfMonth(input) {
  let year, month

  if (typeof input === 'string') {
    [year, month] = input.split('-').map(Number)
  } else if (input instanceof Date) {
    year = input.getFullYear()
    month = input.getMonth() + 1 // getMonth() 是 0-based，要補回來
  } else {
    return '-'
  }

  const date = new Date(year, month, 0) // 該月最後一天
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}


// 搜尋區域
const searchKeyword = ref('')

// 查了之後才篩區域
const filteredSlots = computed(() => {
  if (!searchKeyword.value.trim()) return availableSlots.value
  return availableSlots.value.filter(slot =>
    slot.location.toLowerCase().includes(searchKeyword.value.trim().toLowerCase())
  )
})

// 自動偵測查詢欄位變動 → 查詢車位
watch([selectedType, queryStartMonth, queryEndMonth], ([type, start, end]) => {
  if (!type || !start || !end) return;  
  fetchAvailableSlots();
});

// ---------------------------- 承租 Modal -----------------------------

// 點選「我要承租」
const modalElement = ref(null)
let rentalModalInstance = null

// 承租 Modal：車位資訊
const rentalSlot = ref({})
// 承租日期
const rentStartMonth = ref('')
const rentEndMonth = ref('')
const licensePlate = ref('')

// 打開承租 Modal
function openRentalModal(slot) {
  rentalSlot.value = { ...slot }
  rentStartMonth.value = queryStartMonth.value
  rentEndMonth.value = queryEndMonth.value
  licensePlate.value = ''

  rentalModalInstance?.show()
}

// 關閉承租 Modal
function handleClose() {
  rentalModalInstance?.hide()
}

modalElement.value?.addEventListener('hidden.bs.modal', () => {
  rentalSlot.value = {}
  licensePlate.value = ''
  rentStartMonth.value = ''
  rentEndMonth.value = ''
})

// Modal：起始月變更 → 自動修正截止月不得早於 +1 月
watch([rentStartMonth, rentEndMonth], ([start, end]) => {
    const startDate = new Date(start)
    const minEndDate = new Date(startDate)
    minEndDate.setMonth(startDate.getMonth())
    const minEndStr = minEndDate.toISOString().slice(0, 7)

    if (!end || end < minEndStr) {
        rentEndMonth.value = minEndStr
    }
})

// 轉換日期格式 (年月日)
function formatDateOnly(dateStr) {
  const d = new Date(dateStr)
  return d.toISOString().slice(0, 10) // yyyy-MM-dd
}

// 日期時間格式化（含時分秒）
function formatDateTime2(datetimeStr) {
    return datetimeStr.replace('T', ' ').slice(0, 19)
}

// 送出承租申請
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
    userName,
    usersId: usersId.value,
    slotNumber: rentalSlot.value.slotNumber,
    rentBuyStart: formatDateOnly(getFirstDayOfMonth(rentStartMonth.value)), // yyyy-MM-dd
    rentEnd: formatDateOnly(getLastDayOfMonth(rentEndMonth.value)),       // yyyy-MM-dd
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
    handleClose()

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

// 驗證車牌格式
function plateOK(plate) {
    return /^[A-Za-z0-9-]+$/.test(plate)
}

// ---------------------------- 承租歷史頁籤 -----------------------------

// 監控頁籤轉換
watch(selectedTab, (tab) => {
  if (tab === 'history') fetchRentalHistory()
})

// 承租歷史
const rentalHistory = ref([])

async function fetchRentalHistory() {
  console.log("usersId: " + usersId.value)
  const res = await axios.get(`/park/parking-rentals/user?usersId=${usersId.value}`)
  const records = res.data.data || []
  console.log(records)

  // 加上是否可續租欄位
  for (const r of records) {
    r.canExtend = await canExtendSlot(r)
  }

  rentalHistory.value = records
}

// 是否可續租
async function canExtendSlot(record) {
  const nextStart = new Date(record.rentEnd)
  nextStart.setDate(nextStart.getDate() + 1)
  const nextEnd = new Date(nextStart)
  nextEnd.setMonth(nextStart.getMonth() + 1)

  try {
    const payload = {
      id: record.id, // 當前承租紀錄的 ID，避免與自己重疊
      rentBuyStart: nextStart.toISOString().slice(0, 10),
      rentEnd: nextEnd.toISOString().slice(0, 10),
      parkingSlot: {
        id: record.slotId // 確保你的 record 裡有 slotId，對應後端的 getParkingSlot().getId()
      }
    }
    console.log(payload);
    const res = await axios.post('/park/parking-rentals/overlap', payload)

    // 如果重疊，則不能續租 → 回傳 false；否則可以續租
    return !res.data
  } catch (err) {
    console.error('檢查續租重疊失敗', err)
    return false
  }
}

// ---------------------------- 續租 Modal -----------------------------

// 初始化續租 Modal
const extendModalRef = ref(null)
let extendModalInstance = null

// 續租 Modal 資料
const selectedSlot = ref({})
const extendMonths = ref(1)
const extendStart = ref('')
const extendEnd = ref('')

// 開啟續租 Modal
function openExtendModal(slot) {
  selectedSlot.value = { ...slot }
  extendMonths.value = 1
  const start = new Date(selectedSlot.value.rentEnd)
  start.setDate(start.getDate() + 1)
  const end = new Date(start)
  end.setMonth(end.getMonth() + extendMonths.value - 1)
  extendStart.value = getFirstDayOfMonth(start)
  extendEnd.value = getLastDayOfMonth(end)
  extendModalInstance.show()
}

// 即時修正續租月份
watch(extendMonths, () => {
  if (!selectedSlot.value.rentEnd) return
  const start = new Date(selectedSlot.value.rentEnd)
  start.setDate(start.getDate() + 1)
  const end = new Date(start)
  end.setMonth(end.getMonth() + extendMonths.value - 1)
  extendStart.value = getFirstDayOfMonth(start)
  extendEnd.value = getLastDayOfMonth(end)
})

// 送出續租
const submitExtend = async () => {
  const result = await Swal.fire({
    icon: 'question',
    title: '確認續租？',
    showCancelButton: true,
    confirmButtonText: '是',
    cancelButtonText: '否'
  })
  if (!result.isConfirmed) return

  const payload = {
    ...selectedSlot.value,
    rentBuyStart: extendStart.value,
    rentEnd: extendEnd.value,
    approved: false,
    status: false,
    approverName: null,
    id: null
  }

  try {
    console.log(payload)
    await axios.post(`/park/parking-rentals?communityId=${userStore.community}`, payload)
    await Swal.fire('續租成功', '', 'success')
    fetchRentalHistory()
    handleCloseExtendModal()
  } catch (error) {
    console.error('續租失敗:', error)
    await Swal.fire({
      icon: 'error',
      title: '續租失敗',
      text: error?.response?.data?.message || '發生未知錯誤，請稍後再試',
      confirmButtonText: '關閉'
    })
  }
}

// 關閉續租 Modal
function handleCloseExtendModal() {
  extendModalInstance.hide()
}

// 刪除紀錄
async function deleteRecord(record) {
  console.log(record)
  const result = await Swal.fire({
    title: '確定要刪除嗎？',
    text: '此操作無法還原！',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#6c757d',
    confirmButtonText: '刪除',
    cancelButtonText: '取消'
  })
  
  if (!result.isConfirmed) return
  try {
    const res = await axios.delete(`/park/parking-rentals/${record.id}`)
    await Swal.fire({
      icon: 'success',
      title: '刪除成功',
      showConfirmButton: false,
      timer: 1000
    })
    fetchRentalHistory()
    console.log(res.data.data)
    
  } catch (e) {
    await Swal.fire({
      icon: 'error',
      title: '刪除失敗',
      text: e.response?.data?.message || '請稍後再試',
    })
  }
}

// 切換為承租頁籤時，重新取得可租車位
watch(selectedTab, (tab) => {
  if (tab === 'rent') fetchAvailableSlots()
})

onMounted(async () => {
  const today = new Date()
  const year = today.getFullYear()
  const isFirstDay = today.getDate() === 1
  const month = today.getMonth() + 1

  const startDate = new Date(year, isFirstDay ? month - 1 : month, 1)
  const endDate = new Date(startDate)
  endDate.setMonth(startDate.getMonth())

  queryStartMonth.value = startDate.toISOString().slice(0, 7)
  queryEndMonth.value = endDate.toISOString().slice(0, 7)

  rentalModalInstance = new Modal(modalElement.value)
  extendModalInstance = new Modal(extendModalRef.value)

  fetchType()
  fetchAvailableSlots()
  fetchUserOptions()
})


</script>
  
<style scoped>
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

.btn-disabled {
  opacity: 0.5;
  pointer-events: none;
  cursor: not-allowed;
}

td .btn {
  min-width: 80px;
  font-weight: 500;
}
td .btn-sm {
  min-width: 100px;
  font-weight: 500;
  border-radius: 999px;
}


/* 共用圓角按鈕樣式 */
.action-btn {
  padding: 4px 12px;
  font-size: 0.85rem;
  font-weight: 600;
  white-space: nowrap;
  border: none;
  transition: all 0.2s ease;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  box-shadow: none;
}

/* hover 效果：讓每顆都有亮感 */
.btn-success:hover:not(:disabled),
.btn-primary:hover:not(:disabled),
.btn-danger:hover:not(:disabled) {
  filter: brightness(1.05);
  box-shadow: 0 0 5px rgba(0,0,0,0.1);
}

/* 圓角外觀精緻 */
.btn-sm {
  border-radius: 999px !important;
  padding: 4px 12px !important;
}
</style>