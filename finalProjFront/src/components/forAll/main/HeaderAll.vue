<!-- 這個我還沒整理, 大家可以各自加上自己要的內容在 menuList中並改routeName -->


<template>
  <header class="header" :class="{ 'dark-mode': isDarkMode }" @mouseleave="closeDropdown">
    <!-- LOGO -->
    <router-link to="/home" class="logo" style="cursor:pointer;">
      <img :src="Logo" alt="Logo" />
    </router-link>

    <nav class="nav">
      <div v-for="(category, index) in finalMenuList" :key="category.title" class="nav-item"
        :class="{ active: activeIndex === index }" @mouseenter="activeIndex = index">
        {{ category.title }}
      </div>
    </nav>

    <!-- 下拉大選單 -->
    <div class="mega-menu" v-if="activeIndex !== null" @mouseenter="keepDropdown" @mouseleave="closeDropdown">
      <div class="mega-grid">
        <div v-for="(category, index) in finalMenuList" :key="category.title" class="mega-category"
          :class="{ 'mega-active': activeIndex === index, 'mega-inactive': activeIndex !== index }">
          <!-- 大分類標題（下拉內） -->
          <div class="category-title">{{ category.title }}</div>
          <div class="category-items">
            <!-- 子功能清單 -->
            <div class="dropdown-item" v-for="item in category.children" :key="item.label"
              @click="handleNavigate(item)">
              {{ item.label }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 使用者區塊 -->
    <div class="user-info">
      <div class="welcome-block" v-if="userStore.isAuthenticated">
        <span class="welcome">你好，{{ userStore.name }}</span>
        <span class="points">{{ facilitiesStore.totalBalance }} pt</span>
      </div>
      <div v-else class="avatar placeholder">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
            stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
          <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
            stroke-linejoin="round" />
        </svg>
      </div>
      <div v-if="userStore.isAuthenticated" class="avatar" :style="{ backgroundImage: 'url(' + userStore.avatarUrl + ')' }" @click="toggleNotificationCenter"></div>
      <div v-if="isAdmin">
        <button class="admin-button" @click="router.push('/AdminDashboard')">
          管理後台
        </button>
      </div>
      
      <!-- 通知中心彈出 -->
      <div v-if="isNotificationCenterOpen" class="notification-center" ref="notificationCenterRef" >
        <!-- 你可以放列表、已讀未讀、捲軸等 -->
        <div class="notification-header">
          <h3>通知中心</h3>
        </div>  
        <ul v-if="notifications.length > 0" class="notification-list">
          <li v-for="notice in notifications" :key="notice.unitsNotificationsId" class="notification-item">
            <p class="title">{{ notice.title }}</p>
            <!-- <small>{{ notice.description }}</small> -->
          </li>
        </ul>
      <!-- 沒有通知時 -->
        <div v-else class="notification-empty">
          尚無新通知
        </div>
      </div>

      <button @click.stop="userStore.isAuthenticated ? logout() : triggerLogin()" class="auth-button">
        {{ userStore.isAuthenticated ? '登出' : '登入' }}
      </button>
    </div>
  </header>
</template>



<script setup>
import { ref, onMounted, onUnmounted, onBeforeUnmount, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
import Logo from '@/assets/images/main/Logo.jpg'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'

const path = import.meta.env.VITE_API_URL
const isAdmin = computed(() => userStore.roleId === 2)
const router = useRouter()
const userStore = useUserStore()
const facilitiesStore = useFacilitiesStore()
const isLoggedIn = ref(false)
const showDropdown = ref(false)

//存放社區功能
const communityFunctions = ref([])
const finalMenuList = ref([])

// 控制目前滑鼠停留的分類 index
const activeIndex = ref(null)

watch(
  () => userStore.communityId,
  (newVal) => {
    if (newVal) {
      loadCommunityFunctions()
    } else {
      console.warn('❗️ 尚未取得社區 ID，跳過功能載入')
    }
  },
  { immediate: true }
)
// 通知中心--------------------------------------------------------------------------
const isNotificationCenterOpen = ref(false)
const notificationCenterRef = ref(null)
// 模擬通知資料
const notifications = ref([])   // ⬅️ 全局通知陣列

// 輪詢邏輯
const unitId = userStore.unitId

async function pollNotifications() {
  try {
    const res = await axios.get(`/notifications/unit/${unitId}`)
    console.log('📬 收到通知', res.data.data)
    notifications.value = res.data.data.filter(i => i.isRead === 0 || i.isRead === '0') // 只顯示未讀通知
      .slice(0, 10)
  } catch (error) {
    console.error('❌ 輪詢失敗', error)
  }
}
// 點擊頭像時執行
async function toggleNotificationCenter() {
  isNotificationCenterOpen.value = !isNotificationCenterOpen.value

  if (isNotificationCenterOpen.value) {
    console.log('🔔 開啟通知中心，開始取得最新通知')
    await pollNotifications()
  }
}
// 通知中心--------------------------------------------------------------------------

function handleClickOutside(event) {
  if (
    notificationCenterRef.value &&
    !notificationCenterRef.value.contains(event.target) &&
    !event.target.closest('.avatar')
  ) {
    isNotificationCenterOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})

// 回首頁
const goHome = () => {
  router.push('/')
}

// 觸發全局登入事件
const triggerLogin = () => {
  // 觸發自定義事件，讓 App.vue 顯示登入模態框
  window.dispatchEvent(new CustomEvent('show-login-modal'))
  // console.log(userStore.communityId)
}

// 處理登入成功
const handleLoginSuccess = (loginData) => {
  isLoggedIn.value = true
  user.value.name = loginData.username
}

// 登入登出切換
const login = () => {
  isLoggedIn.value = true
}

const logout = () => {
  isLoggedIn.value = false
  userStore.logout()

  // 清空功能選單
  communityFunctions.value = []
  finalMenuList.value = []


  router.push('/')
  // 觸發全局登出事件（可有可無）
  window.dispatchEvent(new CustomEvent('logout'))

  // 可選：導回首頁
  // router.push('/')
}

// 滑鼠移出 header，下拉收起
const closeDropdown = () => {
  activeIndex.value = null
}
// 滑鼠移入 mega menu，下拉維持
const keepDropdown = () => {
  // 不做事，只為阻止滑鼠移開事件讓 dropdown 收起
}

// 點擊子功能導頁
const handleNavigate = (item) => {
  // 特殊處理：若是要開啟 Bootstrap Modal
  // 有改--------------------------------------------------------
  // if(userStore.roleId == 2 && item.label ==='待領包裹'){
  //   router.push({ name:'packages_security' })
  // }else if(userStore.roleId == 2 && item.label === '領取紀錄'){
  //   router.push({ name:'addPackage' })
  // }
  // 有改--------------------------------------------------------
  if (item.routeName === 'contact-us') {
    const modalEl = document.getElementById('feedbackModal')
    if (modalEl) {
      const modal = bootstrap.Modal.getOrCreateInstance(modalEl)
      modal.show()
    } else {
      console.warn('找不到 #feedbackModal 元素')
    }
  } else {
    // 正常跳轉路由（有參數 or 無參數）
    if (item.params) {
      router.push({ name: item.routeName, params: item.params })
    } else {
      router.push({ name: item.routeName })
    }
  }

  // ✅ 不論是否跳轉或開 Modal，最後都收起 mega menu
  activeIndex.value = null
}






// 監聽登入成功事件
const handleGlobalLoginSuccess = (event) => {
  handleLoginSuccess(event.detail)
}

// 監聽登出事件
const handleGlobalLogout = () => {
  isLoggedIn.value = false
}

onMounted(() => {
  // 監聽全局登入成功事件
  window.addEventListener('login-success', handleGlobalLoginSuccess)
  // 監聽全局登出事件
  window.addEventListener('logout', handleGlobalLogout)

  // 初始化登入狀態
  isLoggedIn.value = userStore.isAuthenticated
  // if (userStore.isAuthenticated) {
  //   user.value.name = userStore.name
  // }
  //不要解開!!!!!

})

onUnmounted(() => {
  // 移除事件監聽器
  window.removeEventListener('login-success', handleGlobalLoginSuccess)
  window.removeEventListener('logout', handleGlobalLogout)
})


//const communityFunctions = ['PACKAGE','BOOKING','INVOICE','MANBERSERVICE','FQA','PARKING','NOTICE','TICKET']//這邊之後會加上API
//分類功能清單
const menuList = ref([
  {
    title: '包裹管理',
    key: 'PACKAGE',
    children: [
      { label: '待領包裹', routeName: 'packages', key: 'PACKAGEPENDING' },
      { label: '領取紀錄', routeName: 'packages_picked', key: 'PACKAGEHISTORY' },
    ]
  },
  {
    title: '預約系統',
    key: 'BOOKING',
    children: [
      { label: '公設與點數系統', routeName: 'FacilityHomepageView', key: 'FHV' },
      { label: '查詢公設', routeName: 'FacilityFindAllListView', key: 'FFAV' },
      { label: '我的預約紀錄', routeName: 'ReservationHistoryView', key: 'RHV' },
      { label: '點數轉移', routeName: 'PointTransferView', key: 'PTV' },
      { label: '點數儲值', routeName: 'PointTopupView', key: 'PTUV' },
      { label: '點數交易紀錄', routeName: 'PointHistoryView', key: 'PHV' },
    ]
  },
  {
    title: '繳費資訊',
    key: 'INVOICE',
    children: [
      { label: '繳費總覽', routeName: 'FinUser', key: 'FINUSER' },
      { label: '待繳帳單', routeName: 'Invoice', key: 'INVOICEBILL' },
      { label: '繳費紀錄', routeName: 'Receipt', key: 'RECEIPT' },
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
    ]
  },
  {
    title: '常見問題',
    key: 'FQA',
    children: [
      { label: 'FAQ 問答集', routeName: 'faq', key: 'FAQQANDA' },
      { label: '聯絡客服', routeName: 'feedback', key: 'FQACONTACT' },
      { label: '我的回饋紀錄', routeName: 'feedback', key: 'FQAFEEDBACK' }, //問題的進度跟進
    ]
  },
  {
    title: '車位管理',
    key: 'PARK',
    children: [
      // 這邊是前台
      { label: '前台停車主頁', key: 'PARKFRONT', routeName: 'parkingFront' }, // 希望可以當成title的<a>
      { label: '我的車位', key: 'MYPARK', routeName: 'mySlots' },
      { label: '使用者承租車位', key: 'PARKRENT', routeName: 'parkRentalFront' },
      { label: '抽籤申請', key: 'PARKAPP', routeName: 'lotteryApply' },

    ]
  },
  {
    title: '公告',
    key: 'NOTICE',
    children: [
      { label: '重要通知', routeName: 'announcement-important', key: 'NOTICEIMPORTANT' },
      { label: '最新公告', routeName: 'announcement-latest', key: 'NOTICELATEST' },
    ]
  }
])

onMounted(() => {
  // loadCommunityFunctions()

  window.addEventListener('refresh-community-functions', loadCommunityFunctions)
})

onUnmounted(() => {
  window.removeEventListener('refresh-community-functions', loadCommunityFunctions)
})

async function loadCommunityFunctions() {
  try {
    console.log(userStore.rawData.communityId)
    const res = await axios.get(`/communitys/functions/${userStore.rawData.communityId}`)
    console.log('✅ API 回傳內容：', res.data)

    if (Array.isArray(res.data)) {
      communityFunctions.value = res.data
      finalMenuList.value = menuList.value
        .filter(module => communityFunctions.value.includes(module.key))
        .map(module => ({
          ...module,
          children: module.children.filter(child =>
            communityFunctions.value.includes(child.key)
          )
        }))
    }
  } catch (err) {
    console.error('載入社區功能失敗', err)
  }
}

const props = defineProps({
  isDarkMode: { type: Boolean, default: false }
})

</script>

<style scoped>
/* 僅保留 layout/spacing/animation，移除背景、字色、border，這些交由 custom-bootstrap.scss 控制 */
/* 通知中心---------------------------------------------------------------- */
.notification-center {
  position: absolute;
  top: 60px; /* 根據頭像位置調整 */
  right: 20px;
  width: 300px;
  max-height: 400px;
  overflow-y: auto;
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  padding: 16px;
  z-index: 1000;
}


/* 標題區塊 */
.notification-header {
  padding: 12px 20px;
  border-bottom: 1px solid #eee;
}

.notification-header h3 {
  margin: 0;
  font-size: 25px;
  color: #333;
}

/* 列表 */
.notification-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

/* 每一項 */
.notification-item {
  padding: 12px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-item:hover {
  background: #f9f9f9;
}

.notification-item .title {
  margin: 0;
  font-size: 20px;
  color: black;
}

/* 如果有未讀 */
.notification-item.unread {
  background: #f5faff;
}

.notification-item:not(.unread) {
  opacity: 0.7;
}

/* 捲軸美化（可選） */
.notification-center::-webkit-scrollbar {
  width: 6px;
}

.notification-center::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

/* 通知中心---------------------------------------------------------------- */

.header {
  width: 100vw;
  height: 72px;
  min-height: 72px;
  max-height: 72px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  padding: 0 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif;
  z-index: 2000;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
}

body {
  margin: 0;
  padding: 0;
}

.logo img {
  height: 44px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
}

.logo img:hover {
  transform: scale(1.05) rotate(2deg);
  filter: drop-shadow(0 4px 16px rgba(0, 0, 0, 0.15));
}

/* 主選單列 (大分類) */
.nav {
  display: flex;
  gap: 28px;
  align-items: center;
}

.nav-item {
  font-size: 16px;
  color: #2d3748;
  cursor: pointer;
  padding: 12px 16px;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  font-weight: 500;
}

.nav-item::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 3px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform: translateX(-50%);
  border-radius: 2px;
}

.nav-item:hover,
.nav-item.active {
  color: #667eea;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.15);
}

.nav-item:hover::before,
.nav-item.active::before {
  width: 80%;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.welcome-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.3;
  animation: slideInRight 0.5s ease-out;
}

.welcome {
  font-size: 15px;
  color: #2d3748;
  font-weight: 500;
}

.points {
  font-size: 13px;
  color: #667eea;
  font-weight: 600;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  padding: 2px 8px;
  border-radius: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  border: 3px solid #fff;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.placeholder {
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #718096;
  border: 2px dashed #cbd5e0;
}

.placeholder svg {
  opacity: 0.6;
}

.auth-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.auth-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.auth-button:hover::before {
  left: 100%;
}

.auth-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

/* Mega Menu 下拉容器 */
.mega-menu {
  position: absolute;
  top: 72px;
  left: 0;
  right: 0;
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
  padding: 0 32px 32px 32px;
  z-index: 9999;
  user-select: text;
  display: flex;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
  /* animation: megaMenuSlide 0.3s cubic-bezier(0.4, 0, 0.2, 1); */
  padding-top: 0;
}

.mega-grid {
  display: flex;
  gap: 32px;
  justify-content: flex-start;
  max-width: 100%;
  margin: 0 auto;
  flex-wrap: wrap;
  align-items: flex-start;
  overflow-x: visible;
}

.category-title {
  margin: 0;
  padding: 12px 16px;
  color: #667eea;
  font-size: 16px;
  font-weight: 500;
  border-bottom: 2px solid rgba(102, 126, 234, 0.2);
  padding-bottom: 8px;
  user-select: text;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 當前滑鼠選中的分類：醒目 */
.mega-active {
  opacity: 1;
  pointer-events: auto;
}

/* 其他沒選中的分類：透明 */
.mega-inactive {
  opacity: 0.5;
  transition: opacity 0.3s;
  pointer-events: auto;
}

/* 每個分類區塊 */
.mega-category {
  min-width: 140px;
  max-width: 180px;
  flex: 0 0 160px;
  opacity: 0.5;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: default;
  user-select: none;
  display: flex;
  flex-direction: column;
}

.mega-category:hover {
  opacity: 1 !important;
  transform: translateY(-4px);
}

/* 子功能項目 */
.category-items .dropdown-item {
  padding: 10px 0;
  font-size: 14px;
  color: #2a3342;
  cursor: pointer;
  user-select: text;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 6px;
  margin: 2px 0;
}

.category-items .dropdown-item:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.08);
  padding-left: 8px;
}

/* 響應式設計 */
@media (max-width: 1200px) {
  .nav {
    gap: 20px;
  }

  .nav-item {
    padding: 10px 12px;
    font-size: 14px;
  }

  .mega-grid {
    gap: 32px;
  }
}

@media (max-width: 768px) {
  .header {
    padding: 12px 20px;
  }

  .nav {
    display: none;
  }

  .user-info {
    gap: 12px;
  }

  .auth-button {
    padding: 10px 20px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .header {
    flex-direction: column;
    height: auto;
    min-height: unset;
    max-height: unset;
    padding: 6px 4px;
    gap: 4px;
  }

  .logo img {
    height: 32px;
  }

  .user-info {
    gap: 6px;
  }

  .auth-button {
    padding: 7px 12px;
    font-size: 11px;
    border-radius: 16px;
  }

  .avatar {
    width: 28px;
    height: 28px;
  }

  .welcome {
    font-size: 12px;
  }

  .points {
    font-size: 10px;
    padding: 1px 4px;
  }
}
.admin-button {
  background-color: #343a40; /* 深灰色背景 */
  color: #fff; /* 白字 */
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.admin-button:hover {
  background-color: #495057; /* hover 淺一點 */
  transform: translateY(-2px);
}

.admin-button:active {
  background-color: #212529;
  transform: scale(0.98);
}

</style>