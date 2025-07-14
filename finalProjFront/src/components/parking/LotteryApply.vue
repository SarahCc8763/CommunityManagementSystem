<template>
  <div class="container mt-4">
    <h2 class="mb-4">抽籤活動申請</h2>

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
          <div class="card-footer d-flex justify-content-between">
            <button class="btn btn-primary w-100" :disabled="appliedEventIds.includes(event.id)"
              @click="applyForLottery(event.id)">
              {{ appliedEventIds.includes(event.id) ? '已申請' : '申請抽籤' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const communityId = userStore.community
const userId = userStore.userId

const lotteryEvents = ref([])
const appliedEventIds = ref([])

function getIcon(typeName) {
  switch (typeName) {
    case '汽車': return 'bi bi-car-front'
    case '機車': return 'bi bi-scooter'
    case '電動車': return 'bi bi-ev-front'
    case '殘障車位': return 'bi bi-person-wheelchair'
    default: return 'bi bi-question-circle'
  }
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleString()
}

async function fetchEvents() {
  const res = await axios.get(`/park/lottery-event?communityId=${communityId}`)
  lotteryEvents.value = res.data.data || []
}

async function fetchAppliedEvents() {
  const res = await axios.get(`/park/lottery-apply/user/${userId}`)
  console.log(res.data.data)
  appliedEventIds.value = res.data.data.map(app => app.lotteryEventsId)
  console.log(appliedEventIds.value)
}

async function applyForLottery(eventId) {
  try {
    const confirm = await Swal.fire({
      title: '確認申請此活動？',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: '確認申請'
    })
    if (!confirm.isConfirmed) return


    const res = await axios.post(`/park/lottery-apply?eventId=${eventId}&userId=${userId}`)
    Swal.fire('申請成功', '', 'success')
    appliedEventIds.value.push(eventId)
  } catch (err) {
    Swal.fire('申請失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}

onMounted(() => {
  fetchEvents()
  fetchAppliedEvents()
})
</script>

<style scoped>
.card-title {
  font-size: 1.25rem;
}

.card-footer {
  padding: 0.75rem;
}
</style>