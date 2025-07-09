<!-- src/views/AdminDashboard.vue -->
<template>
  <div class="p-4">
    <h2 class="mb-4">🛠 管理員後台</h2>
    <div class="group" v-for="group in groupedCards" :key="group.key">
      <h4 class="group-title">{{ group.title }}</h4>
      <div class="card-grid">
        <div
          class="card"
          v-for="item in group.children"
          :key="item.routeName"
          @click="goTo(item.routeName)"
        >
          {{ item.label }}
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()
const groupedCards = ref([])

// 原始功能清單（僅保留你希望放進後台卡片的主功能）
const menuList = [
 {
    title: '包裹管理',
    key: 'PACKAGE',
    children: [
      { label: '待領包裹', routeName: 'parcel-pending', key: 'PACKAGEPENDING' },
      { label: '領取紀錄', routeName: 'parcel-history', key: 'PACKAGEHISTORY' }
    ]
  },
  {
    title: '預約系統',
    key: 'BOOKING',
    children: [
      { label: '健身房預約', routeName: 'reservation-gym', key: 'BOOKINGGYM' },
      { label: '游泳池預約', routeName: 'reservation-pool', key: 'BOOKINGPOOL' },
      { label: '停車預約', routeName: 'reservation-parking', key: 'BOOKINGPARKING' }
    ]
  },
  {
    title: '繳費資訊',
    key: 'INVOICE',
    children: [
      { label: '待繳帳單', routeName: 'Invoice', key: 'INVOICEBILL' },
      { label: '繳費紀錄', routeName: 'InvoiceHistory', key: 'INVOICEHISTORY' },
      { label: '新增費用類型', routeName: 'FeeTypeAdd', key: 'INVOICETYPEADD' },
      { label: '新增繳費期別', routeName: 'BillingPeriodAdd', key: 'INVOICEPERIODADD' },
      { label: '新增發票', routeName: 'InvoiceAdd', key: 'INVOICEINVOICEADD' },
      { label: '新增收據', routeName: 'ReceiptAdd', key: 'INVOICERECEIPTADD' },
      { label: '發票回覆', routeName: 'InvoiceResponseAdd', key: 'INVOICEREPLY' }
    ]
  },
  {
    title: '會員服務',
    key: 'MANBERSERVICE',
    children: [
      { label: '會員資訊修改', routeName: 'member-profile-edit', key: 'MANBERSERVICEEDIT' },
      { label: '點數轉贈', routeName: 'points-transfer', key: 'MANBERSERVICETRANSFER' }
    ]
  },
  {
    title: '報修服務',
    key: 'TICKET',
    children: [
      { label: '提交報修', routeName: 'TicketForm', key: 'TICKETFORM' },
      { label: '維修進度查詢', routeName: 'TicketList', key: 'TICKETLIST' },
      { label: '報修內容', routeName: 'TicketDetailView', key: 'TICKETDETAIL' },
      { label: 'AllTicketsByAssignment', routeName: 'AllTicketsByAssignment', key: 'TICKETASSIGN' },
      { label: 'CommunityList', routeName: 'CommunityList', key: 'TICKETCOMMUNITY' }
    ]
  },
  {
    title: '常見問題',
    key: 'FQA',
    children: [
      { label: 'FAQ 問答集', routeName: 'faq', key: 'FAQQANDA' },
      { label: '聯絡客服', routeName: 'contact-us', key: 'FQACONTACT' },
      { label: '回饋與抱怨？', routeName: 'feedback', key: 'FQAFEEDBACK' }
    ]
  },
  {
    title: '車位管理',
    key: 'PARKING',
    children: [
      { label: '車位資訊維護', routeName: 'parking-info-edit', key: 'PARKINGINFO' },
      { label: '停車預約', routeName: 'reservation-parking', key: 'PARKINGRESERVE' },
      { label: '承租車位管理', routeName: 'parking-rent', key: 'PARKINGRENT' }
    ]
  },
  {
    title: '公告',
    key: 'NOTICE',
    children: [
      { label: '重要通知', routeName: 'announcement-important', key: 'NOTICEIMPORTANT' },
      { label: '最新公告', routeName: 'announcement-latest', key: 'NOTICELATEST' }
    ]
  }
]

onMounted(async () => {
  try {
    const res = await axios.get(`http://localhost:8080/communitys/functions/${userStore.communityId}`)
    const allowed = res.data

    // 根據社區權限過濾主功能與子功能
    groupedCards.value = menuList
      .filter(m => allowed.includes(m.key)) // 主功能有啟用
      .map(m => ({
        title: m.title,
        key: m.key,
        children: m.children.filter(child => allowed.includes(child.key)) // 子功能也需有啟用
      }))
      .filter(group => group.children.length > 0) // 避免空的群組顯示
  } catch (err) {
    console.error('❌ 載入社區功能失敗', err)
  }
})
function goTo(name) {
  router.push({ name })
}
</script>

<style scoped>
.card-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.card {
  padding: 20px;
  background: #f0f4f8;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  font-weight: bold;
  transition: 0.2s ease;
}
.card:hover {
  background: #e2e8f0;
  transform: translateY(-3px);
}
</style>
