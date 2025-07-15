
<template>

  <div class="ticket-list-view container mt-4">
<!-- 上方：標題 + 放大鏡 + 我要報修 -->
<div class="d-flex align-items-center mb-3">
<!-- 左側：標題 + 放大鏡 -->
<div class="d-flex align-items-center me-auto">
  <h2 class="serif-title text-primary mb-0 me-2">報修單列表</h2>

  <!-- 放大鏡圖示 -->
  <img
    src="@/assets/images/ticket/2989907.png"
    alt="搜尋"
    style="width: 20px; height: 20px; cursor: pointer;"
    @click="toggleSearch"
  />
</div>

<!-- 我要報修 -->
<button class="btn btn-primary ms-2" @click="ticketModal.showModal()">我要報修</button>
</div>

<!-- 搜尋列 -->
<div v-if="showSearch" class="mb-3 d-flex align-items-center">
<input
  v-model="searchText"
  type="text"
  class="form-control me-2"
  placeholder="輸入關鍵字搜尋..."
  @keyup.enter="applySearch"
/>

<!-- 篩選按鈕 -->
<img
src="@/assets/images/ticket/setting.png"
alt="篩選"
style="width: 24px; height: 24px; cursor: pointer;"
@click="toggleFilter"
/>
</div>

<!-- 篩選條件區塊 -->
<div v-if="showFilter" class="mb-3 card p-3 shadow-sm" style="margin-bottom: 5rem;">
<div class="row">

<!-- 左側：問題種類 + 狀態 -->
<div class="col-md-6">
  <!-- 問題種類 -->
  <div class="mb-3 position-relative">
    <label class="form-label">問題種類</label>
    <div class="selected-tags mb-1">
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
        <span class="text-muted">請選擇問題種類（可複選）</span>
        <i class="bi bi-chevron-down float-end"></i>
      </div>

      <ul
        v-if="showDropdown"
        class="dropdown-list"
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
  <div class="mb-3">
    <label class="form-label">狀態</label>
    <select class="form-select" v-model="filter.status">
      <option value="">全部</option>
      <option value="to do">待處理</option>
      <option value="In Progress">處理中</option>
      <option value="Done">已完成</option>
    </select>
  </div>
</div>

<!-- 右側：通報人 + 建立時間 -->
<div class="col-md-6">
  <!-- 通報人 -->
  <div class="mb-3">
    <label class="form-label">通報人</label>
    <select class="form-select" v-model="filter.reporter">
      <option value="">全部</option>
      <option v-for="user in users" :key="user.id" :value="user.name">
        {{ user.name }}
      </option>
    </select>
  </div>

  <!-- 建立日期 -->
  <div class="mb-3">
    <label class="form-label">建立時間</label>
    <input type="date" class="form-control" v-model="filter.date" />
  </div>
</div>


</div>

</div>


    <!-- Modal -->
    <TicketPage ref="ticketModal" @created="fetchTickets" />

    <div class="card p-3 mt-5">
      <table class="table table-hover align-middle">
        <thead class="bg-light text-secondary">
          <tr>
            <th></th>
            <!-- <th><input type="checkbox" /></th> -->
            <th>ID</th>
            <th>標題</th>
            <th>狀態</th>
            <th>指派人</th>
            <th>通報人</th>
            <th>描述摘要</th>
            <th>建立時間</th>
          </tr>
        </thead>
        <tbody>
          <!-- <template v-for="(ticket, index) in tickets" :key="ticket.id"> -->
            <template v-for="(ticket, index) in pagedTickets" :key="ticket.id">
            <tr @click="goToDetail(ticket.id)" class="clickable-row">
              <td @click.stop="toggleExpanded(ticket.id)" class="text-center text-primary">▶</td>
              <!-- <td><input type="checkbox" /></td> -->
              <td>T-{{ ticket.id }}</td>
              <td class="text-primary font-semibold">{{ ticket.title }}</td>
              <td>
                <span class="badge" :class="statusClass(ticket.status)">
                  {{ ticket.status }}
                </span>
              </td>
              <td>
                <div class="avatar">{{ getInitials(ticket.assigner) }}</div> {{ ticket.assigner ??'未指派'}}
              </td>
                <td>
                <div class="avatar">{{ getInitials(ticket.name) }}</div>
                {{ ticket.name ?? '無資料' }}
              </td>
              <td class="text-secondary small" v-html="getShortHtmlSummary(ticket.issueDescription, 25)"></td>

              <td class="text-secondary small">{{ formatDate(ticket.startDate) }}</td>
            </tr>

            <!-- 展開區塊 -->
              <tr v-if="expandedIndexes.includes(ticket.id)">
                <td colspan="9" class="p-0 border-0">
                  <transition name="slide-fade">
                    <div class="expand-wrapper" v-show="expandedIndexes.includes(ticket.id)">
                      <div class="p-3">
                        <strong>問題種類：</strong>
                        <span
                          v-for="(type, i) in ticket.issueTypes ?? []"
                          :key="i"
                          class="badge bg-info me-2"
                        >
                          {{ type.issueType?.issueTypeName ?? '未知' }}
                        </span>
                      </div>
                    </div>
                  </transition>
                </td>
              </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="d-flex justify-content-center mt-3">
<nav>
  <ul class="pagination mb-0">
    <li class="page-item" :class="{ disabled: currentPage === 1 }">
      <button class="page-link" @click="prevPage">上一頁</button>
    </li>

    <li
      class="page-item"
      v-for="page in totalPages"
      :key="page"
      :class="{ active: page === currentPage }"
    >
      <button class="page-link" @click="goToPage(page)">
        {{ page }}
      </button>
    </li>

    <li class="page-item" :class="{ disabled: currentPage === totalPages }">
      <button class="page-link" @click="nextPage">下一頁</button>
    </li>
  </ul>
  <div class="d-flex justify-content-end mb-2">
<label class="me-2">每頁顯示</label>
<select class="form-select w-auto" v-model.number="pageSize">
  <option :value="5">5 筆</option>
  <option :value="10">10 筆</option>
  <option :value="20">20 筆</option>
</select>
</div>
</nav>
</div>
  </div>
</template>

<script setup>
import { ref, onMounted ,computed,onUnmounted} from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/plugins/axios'
import TicketPage from './TicketPage.vue'
import { watch } from 'vue'
import DOMPurify from 'dompurify'


function getShortHtmlSummary(html, length = 25) {
// 清掉 HTML 標籤後取前幾字（避免<p>出現）
const plain = DOMPurify.sanitize(html || '', { ALLOWED_TAGS: [] })
return plain.length > length ? plain.slice(0, length) + '...' : plain
}


const currentPage = ref(1)
const pageSize = ref(5) // 每頁幾筆資料
const pagedTickets = computed(() => {
const start = (currentPage.value - 1) * pageSize.value
const end = start + pageSize.value
return tickets.value.slice(start, end)
})

const totalPages = computed(() => {
return Math.ceil(tickets.value.length / pageSize.value)
})

const showSearch = ref(false)
const showFilter = ref(false)
const searchText = ref('')
const filter = ref({
        issueTypeNames: [],
        reporter: '',
        date: '',
      })


      const users = ref([])


const router = useRouter()
const ticketModal = ref(null)
const tickets = ref([])
const expandedIndexes = ref([])
const issueTypes = ref([])
const showDropdown = ref(false)

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

// 點外面自動收起
onMounted(() => {
document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
document.removeEventListener('click', handleClickOutside)
})

function handleClickOutside(e) {
if (!e.target.closest('.custom-multiselect')) {
  showDropdown.value = false
}
}

watch(pageSize, () => {
currentPage.value = 1
})

watch(searchText, () => {
applySearch()
})

watch(filter, () => {
applySearch()
}, { deep: true })

onMounted(() => {
fetchIssueTypes()
callTicketSearch()
fetchUsers()
})


const fetchUsers = async () => {
try {
  const res = await axios.get('/users/ticket') // 或用你剛剛設的 /users/simple
  console.log(res.data)
  users.value = res.data.map(user => ({
    id: user.usersId,
    name: user.name
  }))
} catch (err) {
  console.error('❌ 載入使用者失敗', err)
}
}


const fetchIssueTypes = async () => {
try {
  const res = await axios.get('/IssueTypes')
  issueTypes.value = res.data
} catch (err) {
  console.error('❌ 載入問題種類失敗', err)
}
}

const toggleSearch = () => {
showSearch.value = !showSearch.value
// 如果關閉搜尋，也順便關掉篩選
if (!showSearch.value) {
  showFilter.value = false
}
}


const toggleFilter = () => {
showFilter.value = !showFilter.value
}

const applySearch = async () => {
let reporterId = null
if (filter.value.reporter) {
  const match = users.value.find(u => u.name === filter.value.reporter)
  if (match) reporterId = match.id
}


const payload = {
  title: searchText.value || null,
  status: filter.value.status || null,
  startDate: filter.value.date || null,
  reporterId,
  issueTypeNames: filter.value.issueTypeNames || []
}

const isEmpty =
  !payload.title &&
  !payload.status &&
  !payload.startDate &&
  !payload.reporterId &&
  (payload.issueTypeNames.length === 0)

if (isEmpty) {
  callTicketSearch()
  currentPage.value = 1
  return
}

try {
  const ticketRes = await axios.post('/ticket/search', payload)
  tickets.value = ticketRes.data
} catch (err) {
  console.error('搜尋失敗 ❌', err)
}
}
const handleIssueTypeChange = () => {
if (filter.value.issueTypeNames.includes("")) {
  filter.value.issueTypeNames = []
}
}



function goToDetail(id) {
  router.push({ name: 'TicketDetail', params: { id } })
}

function toggleExpanded(ticketId) {
  if (expandedIndexes.value.includes(ticketId)) {
    expandedIndexes.value = expandedIndexes.value.filter(i => i !== ticketId)
  } else {
    expandedIndexes.value.push(ticketId)
  }
}

function formatDate(datetime) {
  if (!datetime) return ''
  const date = new Date(datetime)
  return date.toLocaleString('zh-TW', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}

function getInitials(name) {
return name?.[0]?.toUpperCase() ?? '?'
}


function statusClass(status) {
return {
  'bg-warning text-dark': status.toLowerCase() === 'to do',
  'bg-danger': status.toLowerCase() === 'in progress',
  'bg-secondary': status.toLowerCase() === 'done',
}
}

function fetchTickets() {
  callTicketSearch()
  currentPage.value = 1
}
async function callTicketSearch() {
  try {
    const [ticketRes, relationRes] = await Promise.all([
      axios.get('/ticket'),              // ✅ 改這裡
      axios.get('/ticket-issue'),
    ])
    // console.log(ticketRes.data)
    const issueMap = {}
    for (const item of relationRes.data) {
      const ticketId = item.ticket?.id
      const typeName = item.issueType?.issueTypeName
      console.log(typeName)//拿的到東西


      if (!ticketId || !typeName) continue // 跳過資料不完整的情況
      
      if (!issueMap[ticketId]) issueMap[ticketId] = []
      issueMap[ticketId].push(typeName)
    }
   tickets.value = ticketRes.data
} catch (err) {
  console.error('❌ 資料載入失敗', err)
}
}
function goToPage(page) {
if (page >= 1 && page <= totalPages.value) {
  currentPage.value = page
}
}
function prevPage() {
if (currentPage.value > 1) {
  currentPage.value--
}
}
function nextPage() {
if (currentPage.value < totalPages.value) {
  currentPage.value++
}
}


</script>

<style scoped>
.ticket-list-view {
  padding-bottom: 60px;
}

.avatar {
  display: inline-block;
  width: 24px;
  height: 24px;
  background: #6c757d;
  border-radius: 50%;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  color: #fff;
  margin-right: 4px;
}

.clickable-row {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.clickable-row:hover {
  background-color: #f1f5f9;
}

/* 展開動畫：上下滑動效果 */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: max-height 0.3s ease, opacity 0.3s ease;
  overflow: hidden;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  max-height: 300px; /* 預估最大高度 */
  opacity: 1;
}

.expand-wrapper {
  overflow: hidden;
  background-color: #f9fcff;
}
.custom-multiselect {
position: relative;
border: 1px solid #ccc;
border-radius: 8px;
background: #fff;
cursor: pointer;
user-select: none;
}

.select-box {
padding: 10px 12px;
font-size: 14px;
}

.dropdown-list {
position: absolute;
top: 100%;
left: 0;
right: 0;
max-height: 160px;
overflow-y: auto;
background: white;
border: 1px solid #ccc;
border-radius: 8px;
z-index: 1000;
margin-top: 2px;
padding: 0;
list-style: none;
}

.dropdown-list li {
padding: 8px 12px;
font-size: 14px;
cursor: pointer;
}

.dropdown-list li:hover {
background-color: #f1f3f5;
}

.dropdown-list li.selected {
background-color: #e9f5ff;
color: #1c7ed6;
}

.selected-tags {
display: flex;
flex-wrap: wrap;
gap: 6px;
}

.tag {
background-color: #edf2ff;
color: #3b5bdb;
border-radius: 12px;
padding: 4px 10px;
font-size: 13px;
display: flex;
align-items: center;
}

</style>

