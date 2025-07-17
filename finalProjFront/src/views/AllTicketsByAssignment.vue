<template>
  <div class="container py-4">
    <h2 class="mb-4">📋 全部報修單列表</h2>
    <!-- 🔍 搜尋列 -->
<div class="d-flex align-items-center mb-3">
  <input
        v-model="searchText"
        type="text"
        class="form-control"
        placeholder="輸入標題關鍵字"
      />
</div>


<!-- 篩選條件 -->
<div class="card p-3 mb-3 shadow-sm bg-dark text-light">
  <div class="row">
<!-- 問題種類 -->
<div class="col-md-6 mb-3 position-relative ">
  <label class="form-label">問題種類</label>
  <div class="selected-tags mb-1 ">
    <span
      v-for="(name, idx) in filter.issueTypeNames"
      :key="idx"
      class="tag"
    >
      {{ name }}
      <i class="bi bi-x-circle-fill ms-1" @click.stop="removeIssueType(name)"></i>
    </span>
  </div>

  <div class="custom-multiselect" @click.stop="toggleDropdown">
    <div class="select-box">
      <span class="text-light">
        {{ filter.issueTypeNames.length ? '已選擇 ' + filter.issueTypeNames.length + ' 項' : '請選擇問題種類（可複選）' }}
      </span>
      <i class="bi bi-chevron-down float-end"></i>
    </div>

    <!-- Dropdown 清單 -->
    <ul
      v-if="showDropdown"
      class="dropdown-list bg-dark text-light"
      @click.stop
    >
      <li
        v-for="type in issueTypes"
        :key="type.id"
        @click="toggleIssueType(type.issueTypeName)"
        :class="{ selected: filter.issueTypeNames.includes(type.issueTypeName) }"
      >
        {{ type.issueTypeName }}
        <i v-if="filter.issueTypeNames.includes(type.issueTypeName)" class="bi bi-check2 ms-2"></i>
      </li>
    </ul>
  </div>
</div>


    <!-- 狀態 -->
    <div class="col-md-6 mb-3">
      <label class="form-label">狀態</label>
      <select class="form-select bg-dark" v-model="filter.status">
        <option value="">ALL</option>
        <option value="to do">TO DO</option>
        <option value="In Progress">IN PROGRESS</option>
        <option value="Done">DONE</option>
      </select>
    </div>

    <!-- 通報人 -->
    <div class="col-md-6 mb-3">
      <label class="form-label">通報人</label>
      <select class="form-select bg-dark" v-model="filter.reporter">
        <option value="">全部</option>
        <option v-for="u in users" :key="u.id" :value="u.name">
          {{ u.name }}
        </option>
      </select>
    </div>

    <!-- 建立時間 -->
    <div class="col-md-6 mb-3">
      <label class="form-label ">建立時間</label>
      <input type="date" class="form-control bg-dark" v-model="filter.startDate" />
    </div>
  </div>
</div>



<ul class="nav nav-tabs mb-4" role="tablist">
  <li class="nav-item">
    <a class="nav-link"
       :class="{ active: selectedTab === 'unassigned' }"
       href="#"
       @click.prevent="selectedTab = 'unassigned'"
    >❌ 未指派報修單</a>
  </li>
  <li class="nav-item">
    <a class="nav-link"
       :class="{ active: selectedTab === 'assigned' }"
       href="#"
       @click.prevent="selectedTab = 'assigned'"
    >✅ 已指派報修單</a>
  </li>
</ul>


<div v-if="selectedTab === 'assigned'">
  <!-- ✅ 已指派 -->
  <div class="mb-5">
    <h4 class="text-success">✅ 已指派報修單</h4>
    <div v-if="assignedTickets.length">
      <div class="d-flex">
        <!-- 左側：報修單卡片列表 -->
        <div class="flex-grow-1">
          <div
            v-for="ticket in paginatedAssignedTickets"
            :key="ticket.id"
            class="card mb-3 p-3 shadow-sm bg-dark text-light"
            @click="openDetail(ticket)"
          >
            <span
              class="badge position-absolute top-0 end-0 m-2"
              :class="{
                'bg-secondary': ticket.status === 'to do',
                'bg-warning text-dark': ticket.status === 'In Progress',
                'bg-success': ticket.status === 'Done'
              }"
            >
              {{ formatStatus(ticket.status) }}
            </span>

            <h5>{{ ticket.title }}</h5>
            <p>通報人：{{ ticket.name }}</p>
            <p>指派人：{{ ticket.assigneeName ?? '（未知）' }}</p>
            <p>廠商：{{ ticket.vendorName ?? '（尚未指派）' }}</p>
            <p>建立時間：{{ formatDate(ticket.startDate) }}</p>

            <div class="mt-3 border-top pt-2 text-light small">
              <p><strong>描述：</strong> <span v-html="ticket.issueDescription || '（無）'"></span></p>
              <div v-if="ticket.attachments?.length">
                <p><strong>附件：</strong></p>
                <div class="d-flex flex-wrap gap-2">
                  <img
                    v-for="(img, idx) in ticket.attachments"
                    :key="idx"
                    :src="img.url"
                    class="rounded border"
                    style="width: 100px; height: auto;"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右側：垂直分頁 -->
        <ul class="pagination flex-column ms-3">
  <li
    class="page-item"
    v-for="page in assignedTotalPages"
    :key="page"
    :class="{ active: assignedPage === page }"
    @click="assignedPage = page"
    style="cursor: pointer"
  >
    <a class="page-link">{{ page }}</a>
  </li>
</ul>

      </div>
    </div>
    <div v-else class="text-muted">目前沒有已指派的報修單</div>
  </div>
</div>

    <AssignedTicketDetail
  v-if="showDetailModal"
  :key="selectedTicket.id"
  :ticket="selectedTicket"
  :vendor-list="vendors"
  @close="showDetailModal = false"
  @update-ticket="updateTicket"
/>


<div v-if="selectedTab === 'unassigned'">
  <h4 class="text-danger">❌ 未指派報修單</h4>

  <div v-if="paginatedUnassignedTickets.length">
    <div class="d-flex">
      <!-- 左側：未指派卡片列表 -->
      <div class="flex-grow-1">
        <div
          v-for="(ticket, index) in paginatedUnassignedTickets"
          :key="ticket.id"
          class="card mb-3 p-3  position-relative bg-dark text-light"
          @click="toggleExpanded(index)"
          style="cursor: pointer"
        >
          <!-- 狀態標籤 -->
          <span
            class="badge position-absolute top-0 end-0 m-2"
            :class="{
              'bg-secondary': ticket.status === 'to do',
              'bg-warning text-dark': ticket.status === 'In Progress',
              'bg-success': ticket.status === 'Done'
            }"
          >
            {{ formatStatus(ticket.status) }}
          </span>

          <div class="d-flex justify-content-between align-items-start">
            <div>
              <h5 class="mb-1">{{ ticket.title }}</h5>
              <p class="mb-1">通報人：{{ ticket.name }}</p>
              <p class="text-muted mb-1">尚未指派</p>
              <p class="mb-1">建立時間：{{ formatDate(ticket.startDate) }}</p>
            </div>
          </div>

          <!-- 展開詳細 -->
          <transition name="fade">
            <div
              v-show="expanded.includes(index)"
              class="mt-3 border-top pt-2 text-secondary small"
            >
              <!-- 問題種類 -->
              <div class="mb-2">
                <p class="text-light"><strong>問題種類：</strong></p>
                <div v-if="ticket.issueTypes?.length">
                  <span
                    v-for="(rel, i) in ticket.issueTypes"
                    :key="i"
                    class="badge bg-info me-2"
                  >
                    {{ rel.issueType?.issueTypeName }}
                  </span>
                </div>
                <p v-else class="text-muted">無</p>
              </div>

              <!-- 描述 -->
              <p class="text-light"><strong>描述：</strong><span v-html="ticket.issueDescription || '無'"></span></p>

              <!-- 附件圖片 -->
              <div class="mb-2" v-if="ticket.attachments?.length">
                <p><strong>附件圖片：</strong></p>
                <div class="d-flex flex-wrap gap-2">
                  <img
                    v-for="(img, i) in ticket.attachments"
                    :key="i"
                    :src="img.url"
                    class="rounded border"
                    style="width: 100px; height: 100px; object-fit: cover"
                    @click.stop="openPreview(img)"
                  />
                </div>
              </div>
              <div
                v-if="previewImageUrl"
                class="image-preview-overlay"
                @click.stop="closePreview"
              >
                <img :src="previewImageUrl" class="image-preview" @click.stop />
              </div>

              <!-- 工程商選擇 -->
              <div class="row mt-3">
                <!-- 左欄 -->
                <div class="col-md-6">
                  <label class="form-label">選擇工程商（可複選）</label>
                  <div class="border rounded p-2">
                    <select
                      class="form-select"
                      @change="handleVendorSelect($event, ticket)"
                      @mousedown.stop
                      @click.stop
                    >
                      <option disabled selected>請選擇工程商</option>
                      <option
                        v-for="vendor in vendors"
                        :key="vendor.vendorID"
                        :value="vendor.vendorID"
                        :disabled="ticket.selectedVendorIds.includes(vendor.vendorID)"
                      >
                        {{ vendor.vendorName }} - {{ vendor.contactPerson }}
                      </option>
                    </select>
                  </div>
                </div>

                <!-- 右欄 -->
                <div class="col-md-6 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <label class="form-label">已選擇的工程商</label>
                    <div class="d-flex flex-wrap gap-2">
                      <span
                        v-for="id in ticket.selectedVendorIds"
                        :key="id"
                        class="badge bg-success"
                      >
                        {{ vendors.find(v => v.vendorID === id)?.vendorName }}
                        <span
                          class="ms-1 text-white"
                          style="cursor: pointer"
                          @click.stop="removeVendor(ticket, id)"
                          >&times;</span
                        >
                      </span>
                    </div>
                  </div>
                  <div class="text-end mt-auto">
                    <button
                      class="btn btn-primary"
                      @click.stop="confirmAssign(ticket)"
                    >
                      ✅ 接收此報修單
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </transition>
        </div>
      </div>

      <!-- 右側分頁 -->
      <ul class="pagination flex-column ms-3">
  <li
    class="page-item"
    v-for="page in unassignedTotalPages"
    :key="'unassigned-' + page"
    :class="{ active: unassignedPage === page }"
    @click="unassignedPage = page"
    style="cursor: pointer"
  >
    <a class="page-link">{{ page }}</a>
  </li>
</ul>
    </div>
  </div>
  <div v-else class="text-muted">目前沒有未指派的報修單</div>
</div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed ,watch} from 'vue'
import axios from '@/plugins/axios'
import AssignedTicketDetail from './AssignedTicketDetail.vue'
import { useUserStore } from '@/stores/UserStore'


const userStore = useUserStore()

const selectedTab=ref('未指派')
const tickets = ref([])
const vendors = ref([])
const expanded = ref([])
const previewImageUrl = ref(null)
const showDetailModal = ref(false)
const selectedTicket = ref(null)

const searchText = ref('')
const filter = ref({
  status: '',
  reporter: '',
  issueTypeNames: []
})

const users = ref([])
const issueTypes = ref([])
const showDropdown = ref(false)

const assignedTotalPages = computed(() => Math.ceil(assignedTickets.value.length / pageSize))
const unassignedTotalPages = computed(() => Math.ceil(unassignedTickets.value.length / pageSize))

const assignedPage = ref(1)
const assignedItemsPerPage = 5

const unassignedPage = ref(1)
const unassignedItemsPerPage = 5


const pageSize = 5

const paginatedAssignedTickets = computed(() => {
  const start = (assignedPage.value - 1) * assignedItemsPerPage
  return assignedTickets.value.slice(start, start + assignedItemsPerPage)
})

const paginatedUnassignedTickets = computed(() => {
  const start = (unassignedPage.value - 1) * unassignedItemsPerPage
  return unassignedTickets.value.slice(start, start + unassignedItemsPerPage)
})


function toggleDropdown() {
  showDropdown.value = !showDropdown.value
}

function toggleIssueType(name) {
  const list = filter.value.issueTypeNames
  const index = list.indexOf(name)
  if (index === -1) list.push(name)
  else list.splice(index, 1)
}

function removeIssueType(name) {
  filter.value.issueTypeNames = filter.value.issueTypeNames.filter(n => n !== name)
}


watch([searchText, filter], async () => {
  await applySearch()
}, { deep: true })

function formatStatus(status) {
  switch (status) {
    case "to do": return 'TO DO'
    case "In Progress": return 'IN PROGRESS'
    case "Done": return 'Done'
    default: return '未知'
  }
}



onMounted(() => {
  selectedTab.value = 'unassigned'
  fetchTickets()
  fetchUsers()
  fetchIssueTypes()
})

async function fetchUsers() {
  const res = await axios.get('/users/ticket')
  users.value = res.data.map(u => ({ id: u.usersId, name: u.name }))
}

async function fetchIssueTypes() {
  const res = await axios.get('/IssueTypes')
  issueTypes.value = res.data
}

async function applySearch() {
  let reporterId = null
  if (filter.value.reporter) {
    const match = users.value.find(u => u.name === filter.value.reporter)
    reporterId = match?.id ?? null
  }

  const payload = {
    title: searchText.value || null,
    status: filter.value.status || null,
    startDate: filter.value.startDate || null,
    reporterId,
    issueTypeNames: filter.value.issueTypeNames || []
  }
  assignedPage.value = 1
  unassignedPage.value = 1
  await fetchTickets(payload)
}


function toggleExpanded(index) {
  if (expanded.value.includes(index)) {
    expanded.value = expanded.value.filter(i => i !== index)
  } else {
    expanded.value.push(index)
  }
}
function openDetail(ticket) {
  const plain = JSON.parse(JSON.stringify(ticket))

  console.log('🔍 原始 ticket.attachments:', ticket.attachments)
  plain.attachments = (plain.attachments || []).map(a => ({
    url: `data:image/png;base64,${a.file}`,
    file: a.file,
    fileName: a.fileName
  }))
  console.log('✅ 處理後 plain.attachments:', plain.attachments)
  selectedTicket.value = plain
  showDetailModal.value = true
}


function openPreview(img) {
  previewImageUrl.value = img.url  // ✅ 不要再包 base64
}
function closePreview() {
  previewImageUrl.value = null
}


onMounted(() => {
  fetchTickets()
})

async function fetchTickets(searchPayload = null) {
  try {

    const payload = searchPayload || {
      title: null,
      status: null,
      startDate: null,
      reporterId: null,
      issueTypeNames: []
    }

    const [ticketRes, assignRes, vendorRes] = await Promise.all([
    axios.post('/ticket/search', payload),
      // axios.get('http://localhost:8080/ticket'),
      axios.get('/TicketToAdministrator'),
      axios.get('/vendors')
    ])

    vendors.value = vendorRes.data
    console.log('assignRes.data', assignRes.data)

    const vendorMap = {}
    for (const v of vendorRes.data) {
      vendorMap[v.vendorID] = v.vendorName
    }

    const ticketMap = new Map()
    for (const rel of assignRes.data) {
      const existing = ticketMap.get(rel.ticketId) || []
      existing.push(rel.vendorId)
      ticketMap.set(rel.ticketId, existing)
    }
    console.log('ticketMap:', ticketMap)

    tickets.value = ticketRes.data.map(ticket => {
      const assignedVendorIds = ticketMap.get(ticket.id) || []

      console.log('ticket.assigner:', ticket.assigner)
      // console.log('Ticket ID:', ticket.id, 'assignedVendorIds:', assignedVendorIds)

      const attachments = (ticket.attachments || []).map(a => ({
    url: `data:image/png;base64,${a.file}`,
    file: a.file,
    fileName: a.fileName
  }))
      return {
        ...ticket,
        assignedVendorIds,
        assigned: assignedVendorIds.length > 0,
        assigneeName: ticket.assignerName ?? '（未知）',
        vendorName: assignedVendorIds.map(id => vendors.value.find(v => v.vendorID === id)?.vendorName).join(', '),
        selectedVendorIds: [],
        attachments
      }
      
    })
 
  } catch (err) {
    console.error('❌ 載入報修單失敗', err)
  }
}

const assignedTickets = computed(() => tickets.value.filter(t => t.assigned))
const unassignedTickets = computed(() => tickets.value.filter(t => !t.assigned))


async function confirmAssign(ticket) {
  if (!ticket.selectedVendorIds?.length) {
    alert('請至少選擇一個工程商')
    return
  }
  try {
    const payload = {
      ticketId: ticket.id,
      vendorIds: ticket.selectedVendorIds
    }
    console.log('🚀 準備送出指派資料：', payload)
    await axios.post('/TicketToAdministrator/assign', payload)
 // ✅ 再補送 PUT 更新 assignerId
 const putPayload = {
      reporterId: ticket.reporter?.usersId || ticket.reporterId || 1,  // 避免 undefined，預設 1
      title: ticket.title,
      assignerId: userStore.userId,
      status: ticket.status,
      issueDescription: ticket.issueDescription,
      notes: ticket.notes || '',
      communityId: userStore.communityId,
      actionBy: userStore.userId, 
    }
    console.log('🚀 準備送出 PUT 資料：', putPayload)

    await axios.put(`/ticket/${ticket.id}`, putPayload)

    await fetchTickets()

    // ✅ 前端同步更新，避免點 Detail 時資料為空
    ticket.assigned = true
    ticket.assignedVendorIds = [...ticket.selectedVendorIds]
    ticket.vendorName = ticket.assignedVendorIds
      .map(id => vendors.value.find(v => v.vendorID === id)?.vendorName)
      .join(', ')
  } catch (err) {
    console.error('❌ 指派失敗', err)
  }
}


function handleVendorSelect(event, ticket) {
  const selected = parseInt(event.target.value)
  if (!ticket.selectedVendorIds.includes(selected)) {
    ticket.selectedVendorIds.push(selected)
  }
  event.target.value = '' // reset select
}

function removeVendor(ticket, id) {
  ticket.selectedVendorIds = ticket.selectedVendorIds.filter(v => v !== id)
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleString()
}

function updateTicket(updatedTicket) {
  const index = tickets.value.findIndex(t => t.id === updatedTicket.id)
  if (index !== -1) {
    updatedTicket.attachments = (updatedTicket.attachments || []).map(a => ({
      ...a,
      url: a.url || `data:image/png;base64,${a.file}`
    }))
    // 保留原本的 assigned 狀態
    updatedTicket.assigned = tickets.value[index].assigned
    tickets.value[index] = { ...updatedTicket }
  }

  if (selectedTicket.value?.id === updatedTicket.id) {
    selectedTicket.value = { ...updatedTicket }
  }
}

</script>

<style scoped>
h2 {
  font-weight: bold;
}
.card {
  border-radius: 10px;
}
img {
  object-fit: cover;
}
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: scaleY(0.95);
}
/* 多選下拉外框 */
.custom-multiselect {
  position: relative;
  border: 1px solid #495057;
  border-radius: 0.375rem;
  padding: 0.375rem 0.75rem;
  background-color: #1e1e2f; /* 深色背景 */
  color: #f8f9fa; /* 白字 */
  cursor: pointer;
  min-height: 38px;
  user-select: none;
}

/* 文字區（例如「請選擇」） */
.select-box {
  user-select: none;
  color: inherit;
}

/* 下拉選單列表 */
.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: white;
  border: 1px solid #ced4da;
  border-radius: 0.375rem;
  max-height: 160px;
  overflow-y: auto;
  list-style: none;
  padding: 0.5rem;
  margin-top: 0.25rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 選項樣式 */
.dropdown-list li {
  padding: 0.375rem 0.5rem;
  cursor: pointer;
  border-radius: 0.25rem;
  transition: background-color 0.2s ease;
}
.dropdown-list li:hover {
  background-color: #f1f3f5;
}
.dropdown-list li.selected {
  background-color: #e9ecef;
  font-weight: 500;
}

/* Tag 樣式（已選項目） */
.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
}
.tag {
  background-color: #0d6efd;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.85rem;
  display: inline-flex;
  align-items: center;
}
.tag i {
  margin-left: 0.25rem;
  cursor: pointer;
}
.bg-dark {
  background-color: #1e1e2f !important;
}
/* ✅ 新增：垂直分頁樣式 */
.pagination.flex-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 0.5rem;
  gap: 0.5rem;
}

.pagination.flex-column .page-item {
  list-style: none;
}

.pagination.flex-column .page-item a {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #2c2f36;
  color: #ffffff;
  font-weight: 500;
  font-size: 16px;

  display: flex;
  justify-content: center;
  align-items: center;

  text-align: center;
  border: none;
  transition: all 0.2s;
}

.pagination.flex-column .page-item.active a {
  background-color: #4e65f9;
  color: #fff;
  font-weight: bold;
}

.pagination.flex-column .page-item a:hover {
  background-color: #4e65f9;
  color: #fff;
}


</style>
