<template>
    <div class="container">
      <h2 class="mb-3">社區臨時停車申請紀錄</h2>
  
      <div class="d-flex justify-content-between align-items-center mb-3">
        <button class="btn btn-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#advancedSearch">
          進階搜尋
        </button>
        <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#recordModal" @click="openAddModal">
          新增申請紀錄
        </button>
      </div>
  
      <div class="collapse mb-3" id="advancedSearch">
        <div class="card card-body">
          <div class="row">
            <div class="col-md-4 mb-2">
              <label>訪客姓名</label>
              <input class="form-control" v-model="filter.visitorName" />
            </div>
            <div class="col-md-4 mb-2">
              <label>車牌</label>
              <input class="form-control" v-model="filter.licensePlate" />
            </div>
            <div class="col-md-4 mb-2">
              <label>車位編號</label>
              <input class="form-control" v-model="filter.slotNumber" />
            </div>
          </div>
        </div>
      </div>
  
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>流水號</th>
            <th>訪客</th>
            <th>車牌</th>
            <th>用途</th>
            <th>開始時間</th>
            <th>結束時間</th>
            <th>申請時間</th>
            <th>車位</th>
            <th>車位種類</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in filteredRecords" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.visitorName }}</td>
            <td>{{ item.licensePlate }}</td>
            <td>{{ item.purpose }}</td>
            <td>{{ item.startTime }}</td>
            <td>{{ item.endTime }}</td>
            <td>{{ item.requestTime }}</td>
            <td>{{ item.slotNumber }}</td>
            <td>{{ item.parkingType }}</td>
            <td>
              <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#recordModal" @click="viewDetails(item)">詳情</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <!-- Modal -->
      <div class="modal fade" id="recordModal" tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content p-3">
            <h5>{{ isAddMode ? '新增申請紀錄' : '申請紀錄詳情' }}</h5>
  
            <div v-if="selected">
              <div class="mb-2">
                <label>訪客姓名：<input class="form-control" v-model="selected.visitorName" :readonly="!isEditMode" /></label>
              </div>
              <div class="mb-2">
                <label>車牌號碼：<input class="form-control" v-model="selected.licensePlate" :readonly="!isEditMode" /></label>
              </div>
              <div class="mb-2">
                <label>用途：<input class="form-control" v-model="selected.purpose" :readonly="!isEditMode" /></label>
              </div>
              <div class="mb-2">
                <label>起始時間：<input type="datetime-local" class="form-control" v-model="selected.startTime" :readonly="!isEditMode" /></label>
              </div>
              <div class="mb-2">
                <label>結束時間：<input type="datetime-local" class="form-control" v-model="selected.endTime" :readonly="!isEditMode" /></label>
              </div>
              <div class="mb-2">
            </div>
            <div class="mb-2">
                <label>車位種類：
  <select class="form-select" v-model="selected.parkingTypeId" :disabled="!isEditMode">
    <option value="">請選擇</option>
    <option v-for="type in parkingTypes" :key="type.id" :value="type.id">
      {{ type.label }}
    </option>
  </select>
</label>

<small v-if="!selected.parkingType || !selected.startTime || !selected.endTime" class="text-muted">
  請先選擇車位種類與起訖時間才能載入車位
</small>

<label>車位編號：
  <select class="form-select" v-model="selected.slotNumber" :disabled="!isEditMode">
    <option value="">請選擇</option>
    <option v-for="slot in slots" :key="slot.slotNumber" :value="slot.slotNumber">{{ slot.slotNumber }}</option>
  </select>
</label>

              </div>
              <div class="mb-2">
                <label>戶號 ID：<input class="form-control" v-model="selected.unitsId" :readonly="!isEditMode" /></label>
              </div>
  
              <div class="d-flex gap-2 mt-3">
                <button class="btn btn-success" v-if="isAddMode" @click="addRecord">送出新增</button>
                <template v-else>
                  <button class="btn btn-warning" @click="toggleEdit">{{ isEditMode ? '取消修改' : '修改' }}</button>
                  <button class="btn btn-primary" v-if="isEditMode" @click="saveEdit">儲存修改</button>
                  <button class="btn btn-danger" @click="deleteRecord">刪除</button>
                </template>
                <button class="btn btn-secondary" data-bs-dismiss="modal">退出</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed, watch } from 'vue'
  import axios from '@/plugins/axios'
  import { useUserStore } from '@/stores/UserStore'
  
  const userStore = useUserStore()
  const communityId = userStore.community
  
  const records = ref([])
  const slots = ref([])
  const parkingTypes = ref([])
  const selected = ref(null)
  const isEditMode = ref(false)
  const isAddMode = ref(false)
  
  function formatDateTime(datetimeStr) {
  if (!datetimeStr) return ''
  const date = new Date(datetimeStr)
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const hh = String(date.getHours()).padStart(2, '0')
  const mi = String(date.getMinutes()).padStart(2, '0')
  const ss = String(date.getSeconds()).padStart(2, '0') // ✅ 加上秒
  return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`
}



  const filter = ref({
    visitorName: '',
    licensePlate: '',
    slotNumber: ''
  })
  
  const filteredRecords = computed(() => {
    return records.value.filter(r => {
      return (
        (!filter.value.visitorName || r.visitorName.includes(filter.value.visitorName)) &&
        (!filter.value.licensePlate || r.licensePlate.includes(filter.value.licensePlate)) &&
        (!filter.value.slotNumber || r.slotNumber.includes(filter.value.slotNumber))
      )
    })
  })
  
  const fetchAll = async () => {
    const res = await axios.get(`/park/temporary-parking?communityId=${communityId}`)
    records.value = res.data.data
  }
  
  const fetchOptions = async () => {
    const [slotRes, typeRes] = await Promise.all([
      axios.get(`/park/parking-slots?communityId=${communityId}`),
      axios.get(`/park/parking-types?communityId=${communityId}`)
    ])
    slots.value = slotRes.data.data
    parkingTypes.value = typeRes.data.data.map(t => ({ id: t.id, label: t.type }))
  }
  
  function viewDetails(record) {
    isAddMode.value = false
    isEditMode.value = false
    selected.value = { ...record }
  }
  
  function openAddModal() {
    isAddMode.value = true
    isEditMode.value = true
    selected.value = {
  visitorName: '',
  licensePlate: '',
  purpose: '',
  startTime: '',
  endTime: '',
  slotNumber: '',
  parkingTypeId: '',  // ← 改這裡
  unitsId: null
}

  }
  
  function toggleEdit() {
    isEditMode.value = !isEditMode.value
  }
  
  async function addRecord() {
  try {
    const payload = {
      ...selected.value,
      startTime: formatDateTime(selected.value.startTime),
      endTime: formatDateTime(selected.value.endTime)
    }
    console.log(payload)
    const res = await axios.post(`/park/temporary-parking?communityId=${communityId}`, payload)
    alert('新增成功')
    fetchAll()
  } catch (e) {
    console.error(e)
    alert('新增失敗')
  }
}

  
async function saveEdit() {
  try {
    const payload = {
      ...selected.value,
      startTime: formatDateTime(selected.value.startTime),
      endTime: formatDateTime(selected.value.endTime)
    }
    await axios.put(`/park/temporary-parking/${selected.value.id}?communityId=${communityId}`, payload)
    alert('更新成功')
    fetchAll()
  } catch (e) {
    console.error(e)
    alert('更新失敗')
  }
}

  
  async function deleteRecord() {
    if (!confirm('確定要刪除嗎？')) return
    try {
      await axios.delete(`/park/temporary-parking/${selected.value.id}`)
      alert('刪除成功')
      fetchAll()
    } catch (e) {
      console.error(e)
      alert('刪除失敗')
    }
  }
  
  onMounted(() => {
    fetchAll()
    fetchOptions()
  })

  watch(
  () => [selected.value?.parkingTypeId, selected.value?.startTime, selected.value?.endTime],
  async ([typeId, start, end]) => {
    if (!selected.value || !typeId || !start || !end) {
      slots.value = []
      return
    }

    const formattedStart = formatDateTime(start)
    const formattedEnd = formatDateTime(end)

    try {
      const res = await axios.get(`/park/temporary-parking/available-slots`, {
        params: {
          communityId,
          typeId, // 注意是 typeId，不是 type
          start: formattedStart,
          end: formattedEnd
        }
      })
      slots.value = res.data.data
    } catch (e) {
      console.error('查詢可用車位失敗', e)
    }
  }
)



  </script>
  