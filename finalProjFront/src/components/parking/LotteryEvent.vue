<template>
  <div class="container mt-4">
    <h2 class="mb-4">抽籤活動管理</h2>

    <button class="btn btn-primary mb-3" @click="openCreateModal">新增活動</button>

    <div class="row">
      <div class="col-md-4 mb-4" v-for="event in lotteryEvents" :key="event.id">
        <div class="card h-100 shadow-sm">
          <div class="text-center pt-3">
            <i :class="getIcon(event.typeName)" style="font-size: 3rem;"></i>
          </div>
          <div class="card-body">
            <h5 class="card-title">{{ event.title }}</h5>
            <p class="card-text">
              <strong>起始：</strong> {{ formatDate(event.startedAt) }}<br />
              <strong>結束：</strong> {{ formatDate(event.endedAt) }}
            </p>
          </div>
          <div class="card-footer d-flex justify-content-between flex-wrap gap-1">
            <button
  class="btn btn-sm btn-success"
  @click="drawLots(event.id)"
  :disabled="event.status"
>
  抽籤
</button>

  <button class="btn btn-sm btn-info" @click="viewParticipants(event.id)">查看名單</button>
  <button class="btn btn-sm btn-warning" @click="openEditModal(event)">編輯</button>
</div>

        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="modal-mask">
      <div class="modal-container">
        <h5 class="mb-3 border-bottom pb-2">{{ isEditing ? '編輯活動' : '新增活動' }}</h5>

        <div class="mb-2">
          <label>活動名稱：</label>
          <input v-model="form.title" class="form-control" />
        </div>

        <div class="mb-2">
          <label>車位種類：</label>
          <select v-model="form.typeName" class="form-select">
            <option v-for="type in parkingTypes" :key="type.id" :value="type.id">{{ type.label }}</option>
          </select>
        </div>

        <div class="mb-2">
          <label>起始時間：</label>
          <input type="datetime-local" v-model="form.startedAt" class="form-control" />
        </div>
        <div class="mb-3">
          <label>結束時間：</label>
          <input type="datetime-local" v-model="form.endedAt" class="form-control" />
        </div>

        <div class="mb-3">
          <label>可選車位：</label>
          <div class="form-check" v-for="slot in parkingSlots" :key="slot.id">
  <input
    class="form-check-input"
    type="checkbox"
    :id="'slot-' + slot.id"
    :value="slot.id"
    v-model="rawSlotIds"
  />
  <label class="form-check-label" :for="'slot-' + slot.id">
    {{ slot.slotNumber }} - {{ slot.location }}
  </label>
</div>
<div v-if="parkingSlots.length === 0" class="text-muted">（無可選車位）</div>
        </div>

        <div class="text-end">
  <button class="btn btn-secondary me-2" @click="closeModal">取消</button>
  <button v-if="isEditing" class="btn btn-danger me-2" @click="deleteEvent(form.id)">刪除</button>
  <button class="btn btn-primary" @click="submitForm">確認</button>
</div>

      </div>
    </div>
  </div>

  <!-- 參與名單 Modal -->
<div v-if="showParticipantModal" class="modal-mask">
  <div class="modal-container" style="max-width: 800px; width: 90%;">
    <h5 class="mb-3 border-bottom pb-2">參與名單</h5>
    <table class="table table-bordered table-sm">
      <thead class="table-light">
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
            <button class="btn btn-sm btn-danger" @click="removeParticipant(p)">移除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="text-end">
      <button class="btn btn-secondary" @click="showParticipantModal = false">關閉</button>
    </div>
  </div>
</div>

</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const communityId = userStore.community

function getIcon(typeName) {
  switch (typeName) {
    case '汽車': return 'bi bi-car-front'
    case '機車': return 'bi bi-scooter'
    case '電動車': return 'bi bi-ev-front'
    case '殘障車位': return 'bi bi-person-wheelchair'
    default: return 'bi bi-question-circle'
  }
}

const lotteryEvents = ref([])
const parkingSlots = ref([])
const showModal = ref(false)
const isEditing = ref(false)
const rawSlotIds = ref([]) // 綁定 checkbox 的原始 ID 陣列
const form = ref({
  id: null,
  title: '',
  typeName: '',
  startedAt: '',
  endedAt: '',
  usersName: userStore.email,
  createdAt: '',
  parkingSlotIds: [],
  status: false
})

const showParticipantModal = ref(false)
const participants = ref([])
const lotteryDrawn = ref(false) // 是否已抽籤（後端也可補欄位提供）

async function viewParticipants(eventId) {
  try {
    const res = await axios.get(`/park/lottery-apply/${eventId}/participants`)
    participants.value = res.data.data || []

    // 判斷是否抽籤完成（只要有人有 awardedSlot 就算）
    lotteryDrawn.value = participants.value.some(p => p.awardedSlot !== null)

    showParticipantModal.value = true
  } catch (err) {
    Swal.fire('取得失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}

async function removeParticipant(p) {
  const confirm = await Swal.fire({
    title: `確認移除 ${p.userName} 嗎？`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '移除'
  })

  if (!confirm.isConfirmed) return

  try {
    await axios.delete(`/park/lottery-apply/${p.id}`)
    Swal.fire('已移除', '', 'success')
    viewParticipants(p.lotteryEventId) // 重新刷新列表
  } catch (err) {
    Swal.fire('移除失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}



const parkingTypes = ref([
  { id: '汽車', label: '汽車' },
  { id: '機車', label: '機車' },
  { id: '電動車', label: '電動車' },
  { id: '殘障車位', label: '殘障車位' }
])

const fetchEvents = async () => {
  const res = await axios.get(`/park/lottery-event?communityId=${communityId}`)
  lotteryEvents.value = res.data.data
}

const fetchParkingSlots = async () => {
  if (!form.value.typeName || !form.value.startedAt || !form.value.endedAt) return
  console.log('觸發車位查詢', {
    communityId,
    typeName: form.value.typeName,
    eventStart: formatDateTime(form.value.startedAt),
    eventEnd: formatDateTime(form.value.endedAt)
  })
  const res = await axios.post('/park/parking-slots/available', {
      communityId: communityId,
      typeName: form.value.typeName,
      eventStart: formatDateTime(form.value.startedAt),
      eventEnd: formatDateTime(form.value.endedAt),
      limit: 10
  })
  parkingSlots.value = res.data.data
}

function openCreateModal() {
  isEditing.value = false
  form.value = {
    id: null,
    title: '',
    typeName: '',
    startedAt: '',
    endedAt: '',
    usersName: userStore.email,
    createdAt: new Date().toISOString().slice(0, 19).replace('T', ' '),
    parkingSlotIds: [],
    status: false
  }
  rawSlotIds.value = []
  showModal.value = true
  setTimeout(() => fetchParkingSlots(), 100) // 或 watch 會觸發也可以不用

}

function openEditModal(event) {
  isEditing.value = true
  form.value = {
    id: event.id,
    title: event.title,
    typeName: event.typeName,
    startedAt: event.startedAt.slice(0, 16),
    endedAt: event.endedAt.slice(0, 16),
    createdAt: event.createdAt,
    usersName: userStore.email,
    parkingSlotIds: event.parkingSlotIds,
    status: event.status
  }
  rawSlotIds.value = parkingSlots.value
  .filter(slot => event.parkingSlotIds.some(p => p.parkingSlotId === slot.id))
  .map(slot => slot.id)

  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function formatDate(date) {
  return new Date(date).toLocaleString()
}

function formatDateTime(datetimeStr) {
  return datetimeStr.replace('T', ' ') + ':00'
}

async function submitForm() {
  if (!form.value.title || !form.value.typeName || !form.value.startedAt || !form.value.endedAt) {
    Swal.fire('請填寫所有欄位', '', 'warning')
    return
  }

  if (new Date(form.value.startedAt) >= new Date(form.value.endedAt)) {
    Swal.fire('起始時間需早於結束時間', '', 'warning')
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
    parkingSlotIds: rawSlotIds.value.map(id => ({ parkingSlotId: id }))
  }

  try {
    if (isEditing.value) {
      console.log('送出 payload：', payload)
      await axios.put(`/park/lottery-event/${payload.id}`, payload)
      Swal.fire('更新成功', '', 'success')
    } else {
      console.log('送出 payload：', payload)
      await axios.post(`/park/lottery-event?communityId=${communityId}`, payload)
      Swal.fire('新增成功', '', 'success')
    }
    closeModal()
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
    closeModal()
    fetchEvents()
  } catch (err) {
    Swal.fire('刪除失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}


async function drawLots(eventId) {
  try {
    const res = await axios.post(`/park/lottery-event/draw/${eventId}`)
    if(res.data.data.totalApplicants == 0) {
      Swal.fire('抽籤失敗', '無人申請', 'info')
    }else if(res.data.data.totalSpaces == 0){
      Swal.fire('抽籤失敗', '未選擇車位', 'info')
    }else{
      fetchEvents()
      Swal.fire('抽籤完成', res.data.data.winners, 'success')
    }
  } catch (e) {
    Swal.fire('抽籤失敗', e.response?.data?.message || '請稍後再試', 'error')
  }
}

watch(() => [form.value.typeName, form.value.startedAt, form.value.endedAt], () => {
  fetchParkingSlots()
})

watch(parkingSlots, () => {
  // 若正在編輯，對勾原本勾選的 slotIds
  if (isEditing.value && form.value.parkingSlotIds.length > 0) {
    rawSlotIds.value = parkingSlots.value
      .filter(slot => form.value.parkingSlotIds.some(p => p.parkingSlotId === slot.id))
      .map(slot => slot.id)
  }
})


onMounted(() => {
  fetchEvents()
})
</script>

<style scoped>

.modal-mask {
  position: fixed;
  z-index: 1050;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex; align-items: center; justify-content: center;
  max-height: 90vh;
  overflow-y: auto;
}
.modal-container {
  background: white;
  padding: 20px;
  width: 400px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
}
.swal-z-top {
  z-index: 10000 !important;
}

</style>
