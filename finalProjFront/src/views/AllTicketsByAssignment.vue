<template>
  <div class="container py-4">

    <h2 class="mb-4">📋 全部報修單列表</h2>

    <!-- ✅ 已指派 -->
    <div class="mb-5">
      <h4 class="text-success">✅ 已指派報修單</h4>
      <div v-if="assignedTickets.length">
        <div v-for="ticket in assignedTickets" :key="ticket.id" class="card mb-2 p-3 shadow-sm">
          <h5>{{ ticket.title }}</h5>
          <p>通報人：{{ ticket.name }}</p>
          <p>指派人：{{ ticket.assignee }}</p>
          <p>建立時間：{{ formatDate(ticket.startDate) }}</p>
        </div>
      </div>
      <div v-else class="text-muted">目前沒有已指派的報修單</div>
    </div>

    <!-- 🚫 未指派 -->
    <div>
      <h4 class="text-danger">🚫 未指派報修單</h4>
      <div v-if="unassignedTickets.length">
        <div v-for="ticket in unassignedTickets" :key="ticket.id" class="card mb-2 p-3 border border-warning">
          <h5>{{ ticket.title }}</h5>
          <p>通報人：{{ ticket.name }}</p>
          <p class="text-muted">尚未指派</p>
          <p>建立時間：{{ formatDate(ticket.startDate) }}</p>
        </div>
      </div>
      <div v-else class="text-muted">目前沒有未指派的報修單</div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const tickets = ref([])

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/ticket')
    tickets.value = res.data
  } catch (err) {
    console.error('❌ 載入報修單失敗', err)
  }
})

const assignedTickets = computed(() =>
  tickets.value.filter(t => t.assignee && t.assignee.trim() !== '')
)

const unassignedTickets = computed(() =>
  tickets.value.filter(t => !t.assignee || t.assignee.trim() === '')
)

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
</style>
