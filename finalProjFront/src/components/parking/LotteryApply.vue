<template>
  <div class="container mt-4">
    <!-- 麵包屑導航 -->
    <nav aria-label="breadcrumb" class="mb-3 ms-1">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('home')" class="text-decoration-none"><i class="bi bi-house-door-fill me-1"></i>首頁</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('parkingFront')" class="text-decoration-none">停車場</a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">抽籤活動申請</li>
      </ol>
    </nav>

    <div class="tag-style px-4 py-2 mb-4">
      <h2 class="mb-0 fw-bold text-primary section-title">抽籤活動申請</h2>
    </div>
    
    <div class="row">
      <div class="col-md-4 mb-4" v-for="event in lotteryEvents" :key="event.id">
        <div class="card h-100 shadow-sm">
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
          <div class="card-footer d-flex flex-column gap-2">
            <button  v-if="appliedEventMap[event.id]" class="btn btn-danger w-100" @click="cancelApply(appliedEventMap[event.id].id, event)">
              取消申請
            </button>
          <button  v-else class="btn btn-primary w-100" @click="applyForLottery(event.id)">
            申請抽籤
          </button>
          
          <button class="btn btn-outline-secondary w-100" @click="viewWinners(event)">
            查看中獎名單
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
const communityId = userStore.communityId
const userId = userStore.userId

// 從資料庫取得抽籤活動資料
const lotteryEvents = ref([])
async function fetchEvents() {
  const res = await axios.get(`/park/lottery-event?communityId=${communityId}`)
  lotteryEvents.value = res.data.data || []
  console.log(lotteryEvents.value);
}

// Card Icon
function getIcon(typeName) {
  switch (typeName) {
    case '汽車': return 'bi bi-car-front'
    case '機車': return 'bi bi-scooter'
    case '電動車': return 'bi bi-ev-front'
    case '殘障車位': return 'bi bi-person-wheelchair'
    default: return 'bi bi-question-circle'
  }
}

// 格式化時間
function formatDate(dateStr) {
  return new Date(dateStr).toLocaleString()
}

// 轉為年月格式
function formatYearMonth(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}/${month}`;
}

// 取得已申請的抽籤活動
const appliedEventIds = ref([])
// 查詢某申請者的所有申請
const appliedEventMap = ref({}) // 用 map 存申請資訊：eventId -> { id, lotteryEventsId, userId }

async function fetchAppliedEvents() {
  const res = await axios.get(`/park/lottery-apply/user/${userId}`)
  const data = res.data.data || []
  console.log(data);
  appliedEventIds.value = data.map(app => app.lotteryEventsId)
  appliedEventMap.value = Object.fromEntries(data.map(app => [app.lotteryEventsId, app]))
}

// 申請抽籤
async function applyForLottery(eventId) {
  console.log(eventId);
  try {
    const confirm = await Swal.fire({
      title: '確認申請此活動？',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: '確認申請',
      cancelButtonText: '取消'
    })
    if (!confirm.isConfirmed) return
    console.log(userId);
    const res = await axios.post(`/park/lottery-apply?eventId=${eventId}&userId=${userId}`)
    Swal.fire('申請成功', '', 'success')

    fetchAppliedEvents()
  } catch (err) {
    Swal.fire('申請失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}


// 取消申請
async function cancelApply(applyId,event) {
  const confirm = await Swal.fire({
    title: '確認取消申請？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '確認',
    cancelButtonText: '取消'
  })
  if (!confirm.isConfirmed) return
  
  console.log(event.status);
  if(event.status){
    Swal.fire('活動已結束，無法取消申請', '', 'error')
    return
  }
  try {
    console.log(applyId);
    await axios.delete(`/park/lottery-apply/${applyId}`)
    Swal.fire('已取消申請', '', 'success')
    await fetchAppliedEvents()
  } catch (err) {
    Swal.fire('取消失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}


// 查詢某活動的中獎名單
async function viewWinners(event) {
  // 判斷活動是否已抽籤並結束
  const now = new Date()
  const endedAt = new Date(event.endedAt)

  if (!event.status || isNaN(endedAt.getTime()) || endedAt > now) {
    await Swal.fire('活動尚未結束或尚未抽籤', '', 'info')
    return
  }

  try {
    const res = await axios.get(`/park/lottery-apply/winners?eventId=${event.id}`)
    const winners = res.data.data || []

    // 確認登入者是否中籤
    const userWinner = winners.find(w => w.userId === userId)
    const isWinner = !!userWinner
    
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

    await Swal.fire({
      icon: isWinner ? 'success' : 'info',
      title: isWinner ? '🎉 恭喜中籤！' : '未中籤',
      text: isWinner ? `中籤車位：${userWinner.slotNumber}` : '請再接再厲～'
    })

    // 顯示中獎名單（含標記目前使用者）
    showWinnerList(winners, isWinner)
  } catch (err) {
    console.error('查詢中獎名單失敗', err)
    await Swal.fire('查詢失敗', err.response?.data?.message || '請稍後再試', 'error')
  }
}


// 中獎名單 Modal 彈出
async function showWinnerList(winners, isUserWinner) {
  if (!Array.isArray(winners) || winners.length === 0) {
    await Swal.fire('無中獎者', '本次抽籤沒有任何中獎者。', 'info')
    return
  }

  const htmlList = winners.map(w => {
    const isCurrentUser = w.userId === userId
    const nameHtml = isCurrentUser
      ? `<span style="color:green; font-weight:bold;">${w.userName}</span>`
      : w.userName
    return `<li>${nameHtml} - ${w.slotNumber}（${w.parkingType}）</li>`
  }).join('')

  const header = !isUserWinner
    ? `<p style="color:red; font-weight:bold;">您未中籤</p>`
    : ''

  await Swal.fire({
    title: '中獎名單',
    html: `${header}<ul style="text-align:left;">${htmlList}</ul>`,
    confirmButtonText: '關閉'
  })
}

onMounted(() => {
  fetchEvents()
  fetchAppliedEvents()
})

// 麵包屑導航
import { useRouter } from 'vue-router'
const router = useRouter()
const goTo = (target) => {
    switch (target) {
        case 'home':
            router.push('/')
            break
        case 'parkingFront':
            router.push('/pages/park/parking-front')
            break
        }
    }
</script>

<style scoped>
.card-title {
  font-weight: bold;
}
.card-footer {
  padding: 0.75rem;
}
</style>