<!-- src/views/AdminDashboard.vue -->
<template>
  <div class="dashboard-wrap">
    <AdminRightInfoNav />
    <div class="banner-container">
      <BannerImage :imageSrc="adminBanner" heading="管理員後台" subtext="社區管理一站式儀表板" textAlign="left" />
    </div>

    <div class="dashboard-container">
      <div class="masonry-grid">
        <div class="masonry-card" v-for="group in groupedCards" :key="group.key">
          <div class="moc-card-header">
            <span class="moc-card-title">　{{ group.title }}</span>
          </div>
          <div class="moc-card-links">
            <div class="moc-card-link" v-for="item in group.children" :key="item.routeName"
              @click="goTo(item.routeName)">
              <span class="moc-link-icon">◆</span>{{ item.label }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>


  <router-link to="/CommunityList" class="btn btn-light">
    功能設定頁面
  </router-link>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
import BannerImage from '@/components/forAll/BannerImage.vue'
import adminBanner from '@/assets/images/main/adminBanner.jpg'
import AdminRightInfoNav from '@/components/forAll/main/admin/AdminRightInfoNav.vue'
import axios from '@/plugins/axios'

const router = useRouter()
const userStore = useUserStore()
const groupedCards = ref([])

// 原始功能清單
const menuList = [
  {
    title: '包裹管理',
    key: 'PACKAGE',
    children: [
      { label: '管理員包裹查詢', routeName: 'packages_security', key: 'PACKAGESEARCH' },
      { label: '新增包裹', routeName: 'addPackage', key: 'ADDPACKAGE' },
    ]
  },
  {
    title: '繳費資訊',
    key: 'INVOICE',
    children: [
      { label: '繳費功能主頁入口', routeName: 'FinAdminDashboard', key: 'FINADMIN' },
      { label: '費用項目管理', routeName: 'FeeTypeAdd', key: 'FEETYPEADD' },
      { label: '繳費期間設定', routeName: 'BillingPeriodAdd', key: 'BILLINGPERIODADD' },
      { label: '繳費通知製作', routeName: 'InvoiceAdd', key: 'INVOICEADD' },
      { label: '繳費通知審核', routeName: 'InvoiceValidate', key: 'INVOICEVALIDATE' },
      { label: '審核帳單回覆', routeName: 'InvoiceWithResponse', key: 'INVOICEWITHRESPONSE' },
      { label: '收據管理中心', routeName: 'ReceiptAdd', key: 'RECEIPTADD' },
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
  },
  {
    title: '配合廠商',
    key: 'VENDOR',
    children: [
      { label: '後臺 - 配合廠商', routeName: 'Vendor', key: 'VENDOR' },
    ]
  }
]

onMounted(async () => {
  try {
    const res = await axios.get(`/communitys/functions/${userStore.communityId}`)
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
.banner-container {
  width: 100%;
  margin: 0 auto 32px auto;
  max-width: 1200px;
}

.dashboard-title-bar {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
  margin-left: 8px;
}

.title-bar-line {
  display: inline-block;
  width: 6px;
  height: 38px;
  background: linear-gradient(180deg, #a3bffa 60%, #667eea 100%);
  border-radius: 3px;
  margin-right: 18px;
}

.dashboard-title {
  font-size: 2.4rem;
  font-weight: 900;
  color: #a3bffa;
  letter-spacing: 2.5px;
  font-family: 'Noto Serif TC', 'Noto Sans TC', 'Segoe UI', 'Arial', serif;
  text-shadow: 0 2px 8px #232a36;
}

.dashboard-container {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.masonry-grid {
  column-count: 3;
  column-gap: 32px;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.masonry-card {
  display: inline-block;
  width: 100%;
  margin-bottom: 32px;
  background: linear-gradient(135deg, #232a36 0%, #323a4d 100%);
  color: #f3f6fa;
  border-radius: 0;
  box-shadow: 0 4px 18px rgba(80, 120, 255, 0.10);
  border: 2px solid #4a5670;
  min-height: 140px;
  padding: 24px 20px 18px 20px;
  transition: box-shadow 0.18s, border 0.18s, transform 0.18s;
  cursor: pointer;
}

.masonry-card:hover {
  box-shadow: 0 8px 32px #a3bffa22;
  border: 2px solid #a3bffa;
  transform: translateY(-3px) scale(1.03);
}

.moc-card-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  background: linear-gradient(90deg, #3a4060 60%, #667eea 100%);
  padding: 12px 0 12px 0;
  border-radius: 0;
  border-left: 6px solid #a3bffa;
  box-shadow: 0 2px 8px #232a3640;
}

.moc-card-title {
  font-size: 1.25rem;
  font-weight: 900;
  letter-spacing: 2.5px;
  color: #fff;
  font-family: 'Noto Serif TC', 'Noto Sans TC', 'Segoe UI', 'Arial', serif;
  text-shadow: 0 2px 8px #232a36;
  padding-left: 2px;
}

.moc-card-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.moc-card-link {
  color: #f3f6fa;
  font-size: 1.08rem;
  font-weight: 600;
  padding: 7px 0 7px 0;
  border-radius: 0;
  background: none;
  margin-bottom: 0;
  cursor: pointer;
  transition: color 0.18s, border 0.18s;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}

.moc-card-link:hover {
  color: #a3bffa;
  text-decoration: underline;
}

.moc-link-icon {
  color: #a3bffa;
  font-size: 0.95em;
  margin-right: 10px;
  margin-left: 2px;
  display: inline-block;
  transform: scale(1.1) translateY(-1px) rotate(15deg);
}
</style>
