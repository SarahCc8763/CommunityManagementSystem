<template>
  <div class="container mt-4">
    <!-- 麵包屑導航 -->
    <nav aria-label="breadcrumb" class="mb-3 ms-1">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('home')" class="text-decoration-none text-light"><i class="bi bi-house-door-fill me-1"></i>首頁</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('adminDashboard')" class="text-decoration-none text-light">後台管理</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('parkingBack')" class="text-decoration-none text-light">停車場</a>
        </li>
        <li class="breadcrumb-item active text-white" aria-current="page">抽籤活動管理</li>
      </ol>
    </nav>
    
    <div class="tag-style px-4 py-2 mb-4">
      <h2 class="mb-0 fw-bold text-primary section-title">抽籤活動管理</h2>
    </div>
    <button class="btn btn-primary d-flex align-items-center gap-2 px-3 py-2 rounded-pill shadow-sm mb-4"
      @click="openCreateModal">
      <i class="bi bi-plus-lg text-white me-2"></i>
      <span class="fw-semibold">新增活動</span>
    </button>
    <div class="row">
      <div class="col-md-4 mb-4" v-for="event in lotteryEvents" :key="event.id">
        <div class="card h-100 shadow-sm bg-dark text-light custom-card">
          <div class="text-center pt-3">
            <i :class="getIcon(event.typeName)" style="font-size: 3rem;"></i>
          </div>
          <div class="card-body">
            <h5 class="card-title">{{ event.title }}</h5>
            <p class="card-text">
              <strong>活動開始申請：</strong> {{ formatDate(event.startedAt) }}
              <br />
              <strong>活動結束申請：</strong> {{ formatDate(event.endedAt) }}
              <br />
              <strong>可承租月份：</strong> {{ formatYearMonth(event.rentalStart) }} ~ {{ formatYearMonth(event.rentalEnd) }}
            </p>
          </div>
          <div class="card-footer d-flex justify-content-between flex-wrap gap-1">
            <button class="btn btn-sm btn-success" @click="drawLots(event.id)" :disabled="event.status">抽籤</button>
            <button class="btn btn-sm btn-info" @click="viewParticipants(event)">查看名單</button>
            <button class="btn btn-sm btn-warning" @click="openEditModal(event)" :disabled="event.status">編輯</button>
          </div>
        </div>
      </div>
    </div>

  </div>

  <!-- 新增修改 Modal -->
  <div class="modal fade" id="slotDetailModal" tabindex="-1" ref="modalRef">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content p-3">
        <div class="modal-header d-flex justify-content-between align-items-center">
          <h5 class="modal-title modal-title-colored mb-0">{{ isEditing ? '編輯活動' : '新增活動' }}</h5>
          <button type="button" class="btn-close btn-close-custom" @click="handleClose">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>
        <div class="form-group">

          <label class="form-label fw-semibold">活動名稱：</label>
          <input v-model="form.title" class="form-control" maxlength="20" placeholder="例如： 114 年度機車未抽籤" />

          <label class="form-label fw-semibold">車位種類：</label>
          <select v-model="form.typeId" class="form-select">
            <option disabled value="">請選擇</option>
            <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
          </select>

          <label class="form-label fw-semibold">起始時間：</label>
          <input type="datetime-local" v-model="form.startedAt" class="form-control" />

          <label class="form-label fw-semibold">結束時間：</label>
          <input type="datetime-local" v-model="form.endedAt" class="form-control" />

          <label class="form-label fw-semibold">承租起始日：</label>
          <input type="month" v-model="form.rentalStart" class="form-control" :min="minMonth" />

          <label class="form-label fw-semibold">承租截止日：</label>
          <input type="month" v-model="form.rentalEnd" class="form-control" :min="minMonth" />

          <!-- 想要抽的數量 -->
          <label class="form-label fw-semibold">想要抽的車位數量：</label>
          <input type="number" class="form-control" v-model.number="desiredSlotCount" min="1" :max="parkingSlots.length"
            :class="{ 'is-invalid': touched && desiredSlotCount < 1 }" @blur="validateInput" />
          <div v-if="touched && desiredSlotCount < 1" class="invalid-feedback">
            車位數量不得小於 1
          </div>

          <div class="d-flex justify-content-between align-items-center mb-2 mt-3">
            <div v-if="parkingSlots.length" class="fw-semibold">已選擇車位：</div>
            <a href="#" class="text-decoration-none link-light small me-3" @click.prevent="startEditSlots">
              <i class="bi bi-pencil me-1"></i>修改
            </a>
          </div>

          <div v-if="parkingSlots.length && !rawSlotIds.length" class="text-muted">請先輸入欲選車位數量</div>
          <!-- 顯示已選車位 -->
          <div v-if="!editingSlots && rawSlotIds.length">
            <ul>
              <li v-for="id in rawSlotIds" :key="id">
                {{ getSlotLabel(id) }}
              </li>
            </ul>

          </div>

          <!-- 顯示 checkbox 編輯車位 -->
          <div v-if="editingSlots && rawSlotIds.length" class="scroll-container">
            <div class="form-check d-flex align-items-center gap-2 mb-2" v-for="slot in parkingSlots" :key="slot.id">
              <input class="form-check-input" type="checkbox" :id="'slot-' + slot.id" :value="slot.id"
                v-model="tempSlotIds" />
              <label class="form-check-label" :for="'slot-' + slot.id">
                {{ slot.slotNumber }} - {{ slot.location }}
              </label>
            </div>
            <div class="d-flex gap-2 mt-3 justify-content-start">
              <button class="btn btn-primary px-4 py-2 rounded-pill shadow-sm fw-semibold" @click="confirmSlotEdit">
                <i class="bi bi-check-lg me-2"></i>確認選擇車位
              </button>
              <button class="btn btn-secondary px-4 py-2 rounded-pill shadow-sm fw-semibold" @click="cancelSlotEdit">
                <i class="bi bi-x-lg me-2"></i>取消
              </button>
            </div>
          </div>


          <div v-if="parkingSlots.length === 0 && !editingSlots" class="text-muted">（請先輸入車位條件或無可選車位）</div>

          <div class="text-end">
            <button v-if="isEditing" class="btn btn-danger me-2" @click="deleteEvent(form.id)">刪除</button>
            <button class="btn btn-primary" @click="submitForm">確認</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 參與名單 Modal -->
  <div class="modal fade" id="participantModal" tabindex="-1" ref="participantModalRef">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content p-3">
        <div class="modal-header d-flex justify-content-between align-items-center">
          <h5 class="mb-3 pb-2">參與名單</h5>
          <button type="button" class="btn-close btn-close-custom" @click="closeParticipantModal">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>
        <div class="modal-body">
          <table class="table-participant">
            <thead class="table-light text-white">
              <tr>
                <th>姓名</th>
                <th>Email</th>
                <th>申請時間</th>
                <th>中籤車位</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="participants.length === 0">
                <td colspan="5" class="text-center text-muted">尚無參與者</td>
              </tr>
              <tr v-for="p in participants" :key="p.id">
                <td>{{ p.userName }}</td>
                <td>{{ p.email }}</td>
                <td>{{ p.appliedAt }}</td>
                <td>
                  <span v-if="p.awardedSlot">{{ p.awardedSlot }}</span>
                  <span v-else-if="lotteryDrawn">未中簽</span>
                  <span v-else>未抽籤</span>
                </td>
                <td>
                  <button class="btn btn-sm btn-danger" @click="removeParticipant(p)"
                    :disabled="lotteryDrawn">移除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { Modal } from 'bootstrap'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const communityId = userStore.communityId
const usersId = userStore.userId

// 從資料庫抓type資料
const parkingTypes = ref([])
const fetchType = async () => {
  const res = await axios.get(`/park/parking-types?communityId=${communityId}`)
  parkingTypes.value = res.data.data.map(t => ({ id: t.id, label: t.type }))
  console.log(parkingTypes.value)
}

// 格式化日期時間
function formatDateTime(datetimeStr) {
  return datetimeStr.replace('T', ' ') + ':00'
}

// 從資料庫抓出可抽籤車位
const parkingSlots = ref([])
const fetchParkingSlots = async () => {
  console.log('typeId' + form.value.typeId)
  if (!form.value.typeId || !form.value.rentalStart || !form.value.rentalEnd) return
  console.log('觸發車位查詢', {
    communityId,
    typeId: form.value.typeId,
    rentalStart: getFirstDayOfMonth(form.value.rentalStart),
    rentalEnd: getLastDayOfMonth(form.value.rentalEnd)
  })
  const res = await axios.post('/park/parking-slots/available', {
    communityId: communityId,
    typeId: form.value.typeId,
    eventStart: getFirstDayOfMonth(form.value.rentalStart),
    eventEnd: getLastDayOfMonth(form.value.rentalEnd),
    limit: 50
  })
  let availableSlots = res.data.data || []

  // 若為編輯模式 ➜ 將原本該活動的 slot 一併加入
  if (isEditing.value && editingReservedSlotIds.value.length) {
    const alreadySelected = availableSlots.map(s => s.id)
    const missingSlots = editingReservedSlotIds.value.filter(id => !alreadySelected.includes(id))

    // 補齊缺失的 slot 資料
    const missingSlotData = await Promise.all(
      missingSlots.map(id => axios.get(`/park/parking-slots/${id}`).then(res => res.data.data))
    )

    availableSlots = [...availableSlots, ...missingSlotData]
  }

  parkingSlots.value = availableSlots
  console.log(parkingSlots.value);
}

// 最小可選月份：本月
const minMonth = new Date().toISOString().slice(0, 7)

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

// 設定新增或編輯模式
const isEditing = ref(false)

// Modal 資料
const form = ref({})

// Modal 車位資料
const rawSlotIds = ref([])  // 真正會送出的選擇

// Modal 選擇車位數量
const desiredSlotCount = ref(null)
const tempSlotIds = ref([])        // 手動選擇用的暫存
const editingSlots = ref(false)    // 是否進入手動選擇模式
const disableAutoSelection = ref(false) // 手動輸入數字時觸發車位自動選擇


// 綁定Modal
const modalRef = ref(null)
let modalInstance = null

// 開啟新增 Modal
function openCreateModal() {
  isEditing.value = false
  form.value = {
    id: null,
    title: '',
    typeId: '',
    startedAt: '',
    endedAt: '',
    rentalStart: '',
    rentalEnd: '',
    usersId,
    createdAt: new Date().toISOString().slice(0, 19).replace('T', ' '),
    parkingSlotIds: [],
    status: false
  }
  rawSlotIds.value = []
  desiredSlotCount.value = null
  modalInstance.show()
}

const editingReservedSlotIds = ref([])

// 開啟編輯 Modal
const ignoreFormWatch = ref(false)
async function openEditModal(event) {
  isEditing.value = true
  editingSlots.value = false
  ignoreFormWatch.value = true

  // 設定 form 與 slots
  form.value = {
    id: event.id,
    title: event.title,
    typeId: event.typeId,
    startedAt: event.startedAt.slice(0, 16),
    endedAt: event.endedAt.slice(0, 16),
    rentalStart: event.rentalStart.slice(0, 7),
    rentalEnd: event.rentalEnd.slice(0, 7),
    usersId,
    createdAt: event.createdAt,
    parkingSlotIds: event.parkingSlotIds,
    status: event.status
  }
  console.log(form.value);

  // ⚠️ 車位先清空，等抓回來後再比對
  rawSlotIds.value = event.parkingSlotIds.map(p => p.parkingSlotId)
  desiredSlotCount.value = rawSlotIds.value.length
  tempSlotIds.value = [...rawSlotIds.value]

  editingReservedSlotIds.value = [...rawSlotIds.value]

  // 先取得當前時段的可用車位（關鍵）
  await fetchParkingSlots()

  // 等渲染完成再解除 watch 防護並顯示 modal
  await nextTick()
  ignoreFormWatch.value = false
  modalInstance.show()
}

// Modal：起始月變更 → 自動修正截止月不得早於 +1 月
watch(() => [form.value.rentalStart, form.value.rentalEnd], ([start, end]) => {
  console.log('觸發watch');

  if (!start) return;

  const startDate = new Date(`${start}-01`);
  const minEndDate = new Date(startDate);
  minEndDate.setMonth(startDate.getMonth()); // 你說不加一 → OK

  const minEndStr = minEndDate.toISOString().slice(0, 7);

  if (!end || end < minEndStr) {
    form.value.rentalEnd = minEndStr;
  }
});


watch(() => [form.value.typeId, form.value.rentalStart, form.value.rentalEnd], () => {
  if (ignoreFormWatch.value) return
  fetchParkingSlots()

  if (rawSlotIds.value && rawSlotIds.value.length > 0) {
    Swal.fire({
      toast: true,
      icon: 'info',
      title: '條件已改變，請重新選擇車位',
      timer: 2000,
      showConfirmButton: false,
      position: 'top-end'
    })
  }

  // 條件變更時清空已選資料，重新讓使用者選擇
  rawSlotIds.value = []
  desiredSlotCount.value = null
  tempSlotIds.value = []
  editingSlots.value = false
})

watch(desiredSlotCount, async (count) => {
  if (!count || parkingSlots.value.length === 0 || disableAutoSelection.value || ignoreFormWatch.value) {
    disableAutoSelection.value = false // 重置為預設 false（只跳過這一次）
    return
  }

  if (count > parkingSlots.value.length) {
    const res = await Swal.fire({
      title: '可抽籤車位不足',
      text: `目前最多僅有 ${parkingSlots.value.length} 個可抽籤車位。是否選擇全部？`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: `選擇全部`,
      cancelButtonText: '取消'
    })
    if (res.isConfirmed) {
      rawSlotIds.value = parkingSlots.value.map(s => s.id)
      desiredSlotCount.value = rawSlotIds.value.length
    }
  } else {
    rawSlotIds.value = parkingSlots.value.slice(0, count).map(s => s.id)
    console.log(rawSlotIds.value);
  }
})

function getSlotLabel(id) {
  console.log(parkingSlots.value);
  const slot = parkingSlots.value.find(s => s.id === id)
  return slot ? `${slot.slotNumber} - ${slot.location}` : `#${id}`
}

function startEditSlots() {
  tempSlotIds.value = [...rawSlotIds.value]
  editingSlots.value = true
}

function confirmSlotEdit() {
  disableAutoSelection.value = true
  rawSlotIds.value = [...tempSlotIds.value]
  desiredSlotCount.value = rawSlotIds.value.length
  editingSlots.value = false
  disableAutoSelection.value = false
}


function cancelSlotEdit() {
  editingSlots.value = false
}

// 送出新增或編輯表單
async function submitForm() {
  if (editingSlots.value) {
    rawSlotIds.value = [...tempSlotIds.value]
    desiredSlotCount.value = rawSlotIds.value.length
  }
  console.log(form.value.typeId);
  if (!form.value.title || !form.value.typeId || !form.value.startedAt || !form.value.endedAt || !form.value.rentalStart || !form.value.rentalEnd) {
    Swal.fire('請填寫所有欄位', '', 'warning')
    return
  }

  if (new Date(form.value.startedAt) >= new Date(form.value.endedAt)) {
    Swal.fire('活動起始時間需早於結束時間', '', 'warning')
    return
  }

  if (new Date(form.value.rentalStart) > new Date(form.value.rentalEnd)) {
    Swal.fire('承租起始時間需早於結束時間', '', 'warning')
    return
  }

  if (rawSlotIds.value.length === 0) {
    Swal.fire('請至少選擇一個車位', '', 'warning')
    return
  }

  const payload = {
    ...form.value,
    startedAt: formatDateTime(form.value.startedAt),
    endedAt: formatDateTime(form.value.endedAt),
    rentalStart: getFirstDayOfMonth(form.value.rentalStart),
    rentalEnd: getLastDayOfMonth(form.value.rentalEnd),
    parkingSlotIds: rawSlotIds.value.map(id => ({ parkingSlotId: id }))
  }

  try {
    console.log(isEditing.value ? '編輯' : '新增');
    if (isEditing.value) {
      console.log("communityId" + communityId);
      console.log('送出 payload：', payload)
      const res = await axios.put(`/park/lottery-event/${payload.id}`, payload)
      console.log(res.data.data);
      Swal.fire('更新成功', '', 'success')
    } else {
      console.log("communityId" + communityId);
      console.log('送出 payload：', payload)
      const res = await axios.post(`/park/lottery-event?communityId=${communityId}`, payload)
      console.log(res.data.data);
      Swal.fire('新增成功', '', 'success')
    }
    handleClose()
    fetchEvents()
  } catch (error) {
    Swal.fire({
      title: '操作失敗',
      text: '請稍後再試',
      icon: 'error',
      customClass: {
        popup: 'swal-z-top'
      }
    })
  }
}

// 搜尋抽籤活動
const lotteryEvents = ref([])
const fetchEvents = async () => {
  const res = await axios.get(`/park/lottery-event?communityId=${communityId}`)
  lotteryEvents.value = res.data.data
  console.log(lotteryEvents.value);
}

// 刪除活動
async function deleteEvent(id) {
  const result = await Swal.fire({
    title: '確認刪除？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '刪除'
  })
  if (!result.isConfirmed) return

  try {
    await axios.delete(`/park/lottery-event/${id}`)
    Swal.fire('已刪除', '', 'success')
    handleClose()
    fetchEvents()
  } catch (err) {
    Swal.fire('刪除失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}

// 關閉新增修改 Modal
function handleClose() {
  modalInstance?.hide()
  touched.value = false
}

// 設定種類對應的 icon
function getIcon(typeName) {
  switch (typeName) {
    case '汽車': return 'bi bi-car-front'
    case '機車': return 'bi bi-scooter'
    case '電動車': return 'bi bi-ev-front'
    case '殘障車位': return 'bi bi-person-wheelchair'
    default: return 'bi bi-question-circle'
  }
}

// 轉為年月格式
function formatYearMonth(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}/${month}`;
}


// 格式化日期
function formatDate(date) {
  return new Date(date).toLocaleString()
}

// 抽籤方法
async function drawLots(eventId) {
  try {
    console.log("eventId" + eventId);
    const res = await axios.put(`/park/lottery-event/draw/${eventId}`)
    if (res.data.data.totalApplicants == 0) {
      Swal.fire('抽籤失敗', '無人申請', 'info')
    } else if (res.data.data.totalSpaces == 0) {
      Swal.fire('抽籤失敗', '未選擇車位', 'info')
    } else {
      fetchEvents()
      await Swal.fire({
      title: '抽籤進行中...',
      html: `
      <div class="d-flex flex-column align-items-center">
        <img src="/images/parking/Fortune wheel.gif" style="width: 240px;" />
        <div class="mt-2">請稍候，正在揭曉結果...</div>
        </div>
        `,
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false,
        timer: 2500
})
      Swal.fire('抽籤完成', res.data.data.winners, 'success')
    }
  } catch (e) {
    Swal.fire('抽籤失敗', e.response?.data?.message || '請稍後再試', 'error')
  }
}

// 開啟參與者 Modal
const participantModalRef = ref(null)
let participantModalInstance = null
const participants = ref([])
const lotteryDrawn = ref(false)
const currentEvent = ref(null)
async function viewParticipants(event) {
  try {
    const res = await axios.get(`/park/lottery-apply/${event.id}/participants`)
    participants.value = res.data.data || []

    currentEvent.value = event
    lotteryDrawn.value = event.status

    participantModalInstance.show()
  } catch (err) {
    Swal.fire('取得失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}

function closeParticipantModal() {
  participantModalInstance?.hide()
}

// 刪除抽檢者
async function removeParticipant(p) {
  const confirm = await Swal.fire({
    title: `確認移除 ${p.userName} 嗎？`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '移除'
  })

  if (!confirm.isConfirmed) return

  try {
    console.log(p.id)
    await axios.delete(`/park/lottery-apply/${p.id}`)
    Swal.fire('已移除', '', 'success')
    viewParticipants(p) // 重新刷新列表
  } catch (err) {
    Swal.fire('移除失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}


onMounted(() => {
  modalInstance = new Modal(modalRef.value)
  participantModalInstance = new Modal(participantModalRef.value)

  fetchType()
  fetchEvents()
})

const touched = ref(false)

function validateInput() {
  touched.value = true
}

watch(desiredSlotCount, (newVal) => {
  if (!newVal || newVal < 1) {
    rawSlotIds.value = [] // ⬅️ 清空已選車位
  }
})

// 麵包屑導航
import { useRouter } from 'vue-router'
const router = useRouter()
const goTo = (target) => {
    switch (target) {
        case 'home':
            router.push('/')
            break
        case 'adminDashboard':
            router.push('/AdminDashboard')
            break
        case 'parkingBack':
            router.push('/pages/park/parking-back')
            break
        }
    }
</script>

<style scoped>
/* 表格滾動容器 */
.table-wrapper {
  position: relative;
  z-index: 0;
}

.table-container {
  max-height: 500px;
  overflow-y: auto;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 0;
}

/* 表格標頭固定 */
.fixed-header-table {
  border-collapse: separate !important;
  border-spacing: 0 !important;
  width: 100%;
}

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

.fixed-header-table tbody tr:hover {
  background-color: #f9fcff;
}

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

/* Modal 樣式 */
.modal-content {
  border-radius: 1rem;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.modal-title {
  font-weight: 700;
  font-size: 1.25rem;
  color: #4b3cc4;
}

.modal-title-colored {
  color: #aebaff;
}

.modal-header .btn-close-custom i {
  display: block;
}

.btn-close-custom {
  background: none;
  border: none;
  color: #f8f9fa;
  font-size: 1.25rem;
  padding: 0;
  line-height: 1;
  transition: color 0.2s ease;
}

.btn-close-custom:hover {
  color: #ffffff;
}

/* Form 表單欄位樣式 */
.form-control,
.form-select {
  border-radius: 0.6rem;
  padding: 0.6rem 1rem;
  font-size: 0.95rem;
}

.form-group label {
  margin-top: 0.75rem;
  font-weight: 600;
}

input::placeholder,
select::placeholder,
textarea::placeholder {
  color: #cbd5e1;
  opacity: 0.9;
}

.form-control::placeholder {
  color: #cbd5e1;
}

.form-check-input {
  vertical-align: middle;
}

/* 深色模式支援 */
.dark-input,
.dark-select,
input.form-control,
select.form-select {
  background-color: #2e2e3e !important;
  color: #f8f9fa !important;
  border: 1px solid #555;
  border-radius: 0.375rem;
  padding: 0.25rem 0.5rem;
  font-size: 0.9rem;
  height: auto;
  appearance: auto;
}

input[type="datetime-local"]::-webkit-calendar-picker-indicator {
  filter: invert(1);
}

input[type="month"]::-webkit-calendar-picker-indicator {
  filter: invert(1);
}

.text-muted {
  color: #ccc !important;
}

/* 按鈕樣式 */
.btn-sm {
  border-radius: 20px;
  font-size: 0.9em;
  font-weight: 600;
  padding: 6px 16px;
  transition: all 0.2s ease;
}

.btn-danger {
  background: linear-gradient(to right, #ff6b6b, #ff8e8e);
  color: white;
  border: none;
}

.btn-danger:hover {
  background: #e05a5a;
}

.btn-primary {
  background: linear-gradient(90deg, #7b5cff, #8e87ff);
  border: none;
  font-weight: 600;
  padding: 0.75rem;
  border-radius: 0.6rem;
  box-shadow: 0 4px 12px rgba(123, 92, 255, 0.4);
}

.btn-primary:hover {
  background: #6d56e6;
}

/* Toast z-index 調整 */
.swal-z-top {
  z-index: 10000 !important;
}

/* table樣式 */
.table-participant {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background-color: transparent;
  color: #f1f1f1;
  font-size: 0.95rem;
  table-layout: fixed;
}

.table-participant thead {
  background-color: rgba(255, 255, 255, 0.05);
}

.table-participant thead th,
.table-participant tbody td {
  padding: 10px 12px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.table-participant tbody tr:hover {
  background-color: rgba(255, 255, 255, 0.03);
}

/* 寬度控制：根據內容做彈性調整 */
.table-participant th:nth-child(1),
.table-participant td:nth-child(1) {
  width: 70px;
  /* 姓名 */
}

.table-participant th:nth-child(2),
.table-participant td:nth-child(2) {
  width: 180px;
  /* Email */
}

.table-participant th:nth-child(3),
.table-participant td:nth-child(3) {
  width: 150px;
  /* 時間 */
}

.table-participant th:nth-child(4),
.table-participant td:nth-child(4) {
  width: 90px;
  /* 中籤車位 */
}

.table-participant th:nth-child(5),
.table-participant td:nth-child(5) {
  width: 80px;
  /* 操作按鈕 */
}

.table-participant .btn-remove {
  font-size: 0.8rem;
  padding: 4px 12px;
  border-radius: 20px;
  background-color: #e57373;
  border: none;
  color: white;
  font-weight: 600;
  transition: background-color 0.2s ease;
}

.table-participant .btn-remove:hover {
  background-color: #ef5350;
}

/* checkbox 卷軸 */
.scroll-container {
  max-height: 300px;
  overflow-y: auto;
  padding-right: 8px;
  /* 可選，避免右側 checkbox 太貼邊 */
}

.scroll-container {
  background-color: rgba(255, 255, 255, 0.05);
  /* 半透明白作為深色背景上區塊 */
  border-radius: 12px;
  padding-left: 1rem;
  max-height: 300px;
  overflow-y: auto;
}

.custom-card .card-footer {
  background-color: #23262e;
  /* 稍微更深的底色區分 footer */
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.custom-card .card-title {
  color: #ffffff;
  font-weight: bold;
}

.breadcrumb-item + .breadcrumb-item::before {
    content: ">";
    color: #ccc; /* 或 text-light 用於深色背景 */
    margin: 0 0.5rem;
}
</style>