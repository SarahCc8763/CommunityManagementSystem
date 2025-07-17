<template>
  <div class="container mt-4">
    <div class="tag-style px-4 py-2 mb-4">
            <h2 class="mb-0 fw-bold text-primary section-title">我的車位</h2>
        </div>
    <div class="row g-4">
      <div class="col-md-4" v-for="slot in userSlotsFiltered" :key="slot.slotNumber">
        <div class="card h-100 shadow-sm border-0" @click="openModal(slot)" style="cursor: pointer;">
          <div class="card-header d-flex justify-content-between align-items-center bg-light">
            <span class="fw-bold">{{ slot.slotNumber }}</span>
            <span class="badge" :class="slot.isRented ? 'bg-warning text-dark' : 'bg-success'">
              <i class="bi" :class="slot.isRented ? 'bi-person-lines-fill' : 'bi-house-door-fill'" style="margin-right: 4px;"></i>
              {{ slot.isRented ? '承租' : '擁有' }}
            </span>
          </div>
          <div class="card-body">
            <div class="mb-2">
              <span class="badge bg-primary me-2">{{ slot.parkingType }}</span>
              <span class="badge bg-info text-dark">{{ slot.location }}</span>
            </div>
            <p class="mb-1"><strong>登記車牌：</strong>{{ slot.licensePlate || '未登記' }}</p>
            <template v-if="slot.isRented">
              <p class="mb-0" v-if="slot.rentBuyStart">
                <strong>承租期間：</strong>{{ getFirstDayOfMonth(slot.rentBuyStart) }} ~ {{ getLastDayOfMonth(slot.rentEnd) }}
              </p>
              <p class="mb-0"><strong>審核狀態：</strong>{{ getApprovalText(slot.approved) }}</p>
              <p class="mb-0"><strong>繳費狀態：</strong>{{ getStatusText(slot.status) }}</p>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 登記車位 Modal -->
    <div class="modal fade" id="plateModal" tabindex="-1" ref="plateModalRef">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">登記車牌修改</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <label class="form-label">車牌號碼：</label>
            <input type="text" class="form-control" v-model="selectedSlot.licensePlate" @blur="cleanInvalidChars(selectedSlot, 'licensePlate')" placeholder="請輸入新車牌" maxlength="10"/>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            <button class="btn btn-primary" @click="updatePlate">儲存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 承租資料 Modal -->
    <div class="modal fade" id="rentalModal" tabindex="-1" ref="rentalModalRef">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">承租資料</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p><strong>車位編號：</strong>{{ selectedSlot.slotNumber }}</p>
            <p><strong>車位種類：</strong>{{ selectedSlot.parkingType }}</p>
            <p><strong>位置：</strong>{{ selectedSlot.location }}</p>
            <p><strong>登記車牌：</strong>{{ selectedSlot.licensePlate || '未登記' }}</p>
            <p><strong>承租起始：</strong>{{ getFirstDayOfMonth(selectedSlot.rentBuyStart) }}</p>
            <p><strong>承租截止：</strong>{{ getLastDayOfMonth(selectedSlot.rentEnd) }}</p>
            <p><strong>審核狀態：</strong>{{ getApprovalText(selectedSlot.approved) }}</p>
            <p><strong>繳費狀態：</strong>{{ getStatusText(selectedSlot.status) }}</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            <button class="btn btn-outline-primary" @click="editRentalPlateFromModal">編輯車牌</button>
            <button class="btn btn-danger" @click="cancelRental">取消承租</button>
            <button class="btn btn-success" @click="openExtendModal">續租</button>
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
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p><strong>車位編號：</strong>{{ selectedSlot.slotNumber }}</p>
            <p><strong>車位種類：</strong>{{ selectedSlot.parkingType }}</p>
            <p><strong>位置：</strong>{{ selectedSlot.location }}</p>
            <label class="form-label">登記車牌：</label>
            <input type="text" class="form-control mb-3" v-model="selectedSlot.licensePlate" @blur="cleanInvalidChars(selectedSlot, 'licensePlate')" placeholder="請輸入新車牌" maxlength="10"/>
            <p><strong>承租起始：</strong>{{ extendStart }}</p>
            <p><strong>承租截止：</strong>{{ extendEnd }}</p>
            <label class="form-label">續租月數</label>
            <select class="form-select" v-model="extendMonths">
              <option v-for="m in 12" :value="m">{{ m }} 個月</option>
            </select>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button class="btn btn-success" @click="submitExtend">確認續租</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/userStore'
import { Modal } from 'bootstrap'

const userStore = useUserStore()
const userSlots = ref([])
const selectedSlot = ref({})
const extendMonths = ref(1)
const extendStart = ref('-')
const extendEnd = ref('-')
const plateModalRef = ref(null)
const rentalModalRef = ref(null)
const extendModalRef = ref(null)
let plateModalInstance, rentalModalInstance, extendModalInstance
const usersId = userStore.userId
const today = new Date()

const userSlotsFiltered = computed(() => {
  return userSlots.value.filter((slot) => {
    if (!slot.isRented) return true
    if (!slot.rentBuyStart || !slot.rentEnd) return false
    const start = new Date(slot.rentBuyStart)
    const end = new Date(slot.rentEnd)
    return start <= today && today <= end
  })
})

const fetchUserSlots = async () => {
  const res = await axios.get('/park/parking-rentals/user/slots-and-rentals', {
    params: { usersId }
  })
  userSlots.value = res.data.data
}

onMounted(async () => {
  plateModalInstance = new Modal(plateModalRef.value)
  rentalModalInstance = new Modal(rentalModalRef.value)
  extendModalInstance = new Modal(extendModalRef.value)
  fetchUserSlots()
})

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

// 轉換日期格式 (年月日)
function formatDateOnly(dateStr) {
  const d = new Date(dateStr)
  return d.toISOString().slice(0, 10) // yyyy-MM-dd
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

const getApprovalText = (value) => value === null ? '待審核' : value ? '已審核' : '未通過'
const getStatusText = (value) => value ? '已繳費' : '未繳費'

const isFromRental = ref(false)

const openModal = (slot) => {
  selectedSlot.value = { ...slot }
  isFromRental.value = slot.isRented
  slot.isRented ? rentalModalInstance.show() : plateModalInstance.show()
}


const updatePlate = async () => {
  if (!plateOK(selectedSlot.value.licensePlate)) {
    await Swal.fire({
      icon: 'warning',
      title: '車牌格式錯誤',
      text: '車牌不可含中文，僅能輸入英數字與 -',
      confirmButtonText: '關閉'
    });
    return;
  }

  try {
    const payload = {
      ...selectedSlot.value,
      licensePlate: selectedSlot.value.licensePlate
    }

    if (isFromRental.value) {
      console.log("修改承租")
      // 修改承租紀錄
      payload.approved = false
      payload.approverId = null

      console.log(payload)
      await axios.put(
        `/park/parking-rentals/${selectedSlot.value.id}?communityId=${userStore.communityId}`,
        payload
      )
      await fetchUserSlots()
    } else {
      console.log(selectedSlot.value)
      // 修改車位資料
      await axios.put(`/park/parking-slots/${selectedSlot.value.id}`, payload)
      await fetchUserSlots()
    }

    await Swal.fire({
      icon: 'success',
      title: '已更新登記車牌',
      showConfirmButton: false,
      timer: 1000
    })

    plateModalInstance.hide()
    rentalModalInstance.hide()

  } catch (e) {
    console.error('修改失敗', e)
    await Swal.fire({
      icon: 'error',
      title: '修改失敗',
      text: e?.response?.data?.message || '請稍後再試'
    })
  }
}


const editRentalPlateFromModal = () => {
  isFromRental.value = true
  rentalModalInstance.hide()
  setTimeout(() => {
    plateModalInstance.show()
  }, 300)
}


const cancelRental = async () => {
  const result = await Swal.fire({
    icon: 'warning',
    title: '確認取消承租？',
    showCancelButton: true,
    confirmButtonText: '是',
    cancelButtonText: '否'
  })
  if (!result.isConfirmed) return
  await axios.delete(`/park/parking-rentals/${selectedSlot.value.id}`)
  await Swal.fire('已取消承租', '', 'success')
  await fetchUserSlots()
  rentalModalInstance.hide()
}

// 開啟續租 Modal
const openExtendModal = () => {
  initialSlotBackup = { ...selectedSlot.value }
  extendMonths.value = 1
  const start = new Date(selectedSlot.value.rentEnd)
  start.setDate(start.getDate() + 1)
  const end = new Date(start)
  end.setMonth(end.getMonth() + extendMonths.value - 1)
  extendStart.value = getFirstDayOfMonth(start)
  extendEnd.value = getLastDayOfMonth(end)
  extendModalInstance.show()
}

let initialSlotBackup = null
// 送出續租申請
const submitExtend = async () => {
  const start = new Date(selectedSlot.value.rentEnd)
  start.setDate(start.getDate() + 1)
  const end = new Date(start)
  end.setMonth(end.getMonth() + extendMonths.value)

  const payload = {
    ...selectedSlot.value,
    rentBuyStart: formatDateOnly(start),
    rentEnd: formatDateOnly(end),
    approved: false,
    status: false,
    approverName: null,
    id: null
  }

  const result = await Swal.fire({
    icon: 'question',
    title: '確認續租？',
    showCancelButton: true,
    confirmButtonText: '是',
    cancelButtonText: '否'
  })
  if (!result.isConfirmed) return
  console.log(payload)
  try {
    await axios.post(`/park/parking-rentals?communityId=${userStore.communityId}`, payload)
    await Swal.fire('續租成功', '', 'success')
    extendModalInstance.hide()
    if (initialSlotBackup) {
      initialSlotBackup = null
    }
    // rentalModalInstance.hide()
  } catch (e) {
    console.error('續租失敗', e)
    await Swal.fire({
      icon: 'error',
      title: '續租失敗',
      text: e?.response?.data?.message || '請稍後再試'
    })
    if (initialSlotBackup) {
      selectedSlot.value = { ...initialSlotBackup }
    }
    return;
  }
}

// 清除非法字元（例如貼上或有預設值）
function cleanInvalidChars(slot, field) {
    const cleaned = (slot[field] || '').replace(/[^A-Za-z0-9-]/g, '').slice(0, 10)
    slot[field] = cleaned
}

// 驗證車牌格式
function plateOK(plate) {
    return /^[A-Za-z0-9-]+$/.test(plate)
}
</script>

<style scoped>
.card-header {
  font-size: 1.1rem;
  font-weight: bold;
}
.card:hover {
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  transform: scale(1.01);
  transition: 0.2s;
}
.badge {
  font-size: 0.75rem;
  padding: 0.4em 0.6em;
  vertical-align: middle;
}

/* .container .input-box  */
.form-control {
  background-color: #fff !important;
  color: #000 !important;
}
.form-select {
  background-color: #fff !important;
  color: #000 !important;
}
.form-select {
  appearance: none; /* Chrome, Safari, Edge */
  -webkit-appearance: none; /* Safari */
  -moz-appearance: none; /* Firefox */
  background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 4 5'%3E%3Cpath fill='%23333' d='M0 0l2 2 2-2z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 8px 10px;
}

</style>
