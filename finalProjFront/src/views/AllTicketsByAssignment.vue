<template>
  <div class="container py-4">
    <h2 class="mb-4">📋 全部報修單列表</h2>

    <!-- ✅ 已指派 -->
    <div class="mb-5">
      <h4 class="text-success">✅ 已指派報修單</h4>
      <div v-if="assignedTickets.length">
        <div v-for="ticket in assignedTickets" :key="ticket.id" class="card mb-3 p-3 
        shadow-sm" @click="openDetail(ticket)">
          <h5>{{ ticket.title }}</h5>
          <p>通報人：{{ ticket.name }}</p>
          <p>指派人：{{ ticket.assigneeName ?? '（未知）' }}</p>
          <p>廠商：{{ ticket.vendorName ?? '（尚未指派）' }}</p>
          <p>建立時間：{{ formatDate(ticket.startDate) }}</p>

          <div class="mt-3 border-top pt-2 text-secondary small">
            <p><strong>描述：</strong>{{ ticket.issueDescription || '（無）' }}</p>
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
      <div v-else class="text-muted">目前沒有已指派的報修單</div>
    </div>
    <AssignedTicketDetail
  v-if="showDetailModal"
  :ticket="selectedTicket"
  :vendor-list="vendors"
  @close="showDetailModal = false"
  @update-ticket="selectedTicket = $event"
/>

    <!-- ❌ 未指派 -->
<div>
  <h4 class="text-danger">❌ 未指派報修單</h4>
  <div v-if="unassignedTickets.length">
    <div
      v-for="(ticket, index) in unassignedTickets"
      :key="ticket.id"
      class="card mb-3 p-3 border border-warning position-relative"
      @click="toggleExpanded(index)"
      style="cursor: pointer"
    >
      <div class="d-flex justify-content-between align-items-start">
        <div>
          <h5 class="mb-1">{{ ticket.title }}</h5>
          <p class="mb-1">通報人：{{ ticket.name }}</p>
          <p class="text-muted mb-1">尚未指派</p>
          <p class="mb-1">建立時間：{{ formatDate(ticket.startDate) }}</p>
        </div>
      </div>

      <transition name="fade">
  <div v-show="expanded.includes(index)" class="mt-3 border-top pt-2 text-secondary small">
    <!-- 問題種類 -->
    <div class="mb-2">
      <p><strong>問題種類：</strong></p>
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

    <!-- 問題描述 -->
    <p><strong>描述：</strong>{{ ticket.issueDescription || '無' }}</p>

    <!-- 工程商選擇區 -->
    <div class="row mt-3">
      <!-- 左欄：下拉選單 -->
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

      <!-- 右欄：顯示已選與按鈕 -->
      <div class="col-md-6 d-flex flex-column justify-content-between">
        <!-- ✅ 顯示已選項目 -->
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
              >&times;</span>
            </span>
          </div>
        </div>

        <!-- ✅ 接收按鈕靠右 -->
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
  <div v-else class="text-muted">目前沒有未指派的報修單</div>
</div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import AssignedTicketDetail from './AssignedTicketDetail.vue'

const tickets = ref([])
const vendors = ref([])
const expanded = ref([])

const showDetailModal = ref(false)
const selectedTicket = ref(null)

function toggleExpanded(index) {
  if (expanded.value.includes(index)) {
    expanded.value = expanded.value.filter(i => i !== index)
  } else {
    expanded.value.push(index)
  }
}
function openDetail(ticket) {
  const plain = JSON.parse(JSON.stringify(ticket))
  selectedTicket.value = plain
  showDetailModal.value = true
}

onMounted(() => {
  fetchTickets()
})

async function fetchTickets() {
  try {
    const [ticketRes, assignRes, vendorRes] = await Promise.all([
      axios.get('http://localhost:8080/ticket'),
      axios.get('http://localhost:8080/TicketToAdministrator'),
      axios.get('http://localhost:8080/vendors')
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
      console.log('Ticket ID:', ticket.id, 'assignedVendorIds:', assignedVendorIds)
      return {
        ...ticket,
        assignedVendorIds,
        assigned: assignedVendorIds.length > 0,
        assigneeName: ticket.assigner?.name ?? null,
        vendorName: assignedVendorIds.map(id => vendors.value.find(v => v.vendorID === id)?.vendorName).join(', '),
        selectedVendorIds: []
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
    await axios.post('http://localhost:8080/TicketToAdministrator/assign', payload)

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
</style>
