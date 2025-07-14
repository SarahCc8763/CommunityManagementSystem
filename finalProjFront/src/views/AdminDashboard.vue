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
      { label: 'AllTicketsByAssignment', routeName: 'AllTicketsByAssignment', key: 'TICKETASSIGN' },
      { label: 'CommunityList', routeName: 'CommunityList', key: 'TICKETCOMMUNITY' }
    ]
  },
  {
    title: '常見問題',
    key: 'FQA',
    children: [
    { label: '後臺 - FAQ 管理', routeName: 'faqAdmin', key: 'FAQADMIN' }, //FAQ後台
    { label: '後臺 - 回饋管理', routeName: 'feedbackAdmin', key: 'FEEDBACKADMIN' }, //回饋後台
    ]
  },
  {
    title: '車位管理',
    key: 'PARK',
    children: [
      { label: '後台停車主頁', key: 'PARKBACK', routeName: 'parkingBack' }, // 這個看你設計可以不用
      { label: '社區停車場建置', key: 'PARKINIT', routeName: 'parkInitialize' },
      { label: '所有車位查詢', key: 'PARKSLOT', routeName: 'parkSlot' },
      { label: '承租記錄查詢', key: 'PARKREC', routeName: 'parkRentalBack' },
      { label: '抽籤活動', key: 'PARKEVE', routeName: 'lotteryEvent' },
    ]
  },
  {
    title: '公告',
    key: 'NOTICE',
    children: [
    { label: '後臺 - 公告管理', routeName: 'bulletin-admin', key: 'BULLETINADMIN' },
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
