<!-- 這個我還沒整理, 大家可以各自加上自己要的內容在 menuList中並改routeName -->


<template>
  <header class="header" @mouseleave="closeDropdown">
    <!-- LOGO -->
    <div class="logo" @click="goHome">
      <img src="https://img.freepik.com/premium-vector/building-logo-design-vector_67715-609.jpg" alt="Logo" />
    </div>

    <!-- 主選單列：直接展示所有大分類 -->
    <nav class="nav">
      <div v-for="(category, index) in menuList" :key="category.title" class="nav-item"
        :class="{ active: activeIndex === index }" @mouseenter="activeIndex = index">
        {{ category.title }}
      </div>
    </nav>

    <!-- 下拉大選單 -->
    <div class="mega-menu" v-if="activeIndex !== null" @mouseenter="keepDropdown" @mouseleave="closeDropdown">
      <div class="mega-grid">
        <div v-for="(category, index) in menuList" :key="category.title" class="mega-category"
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
      <div class="welcome-block" v-if="isLoggedIn">
        <span class="welcome">你好，{{ user.name }}</span>
        <span class="points">{{ user.points }} pt</span>
      </div>
      <div v-else class="avatar placeholder"></div>
      <div v-if="isLoggedIn" class="avatar" :style="{ backgroundImage: 'url(' + user.avatar + ')' }"></div>
      <button @click.stop="isLoggedIn ? logout() : login()" class="auth-button">
        {{ isLoggedIn ? '登出' : '登入' }}
      </button>
    </div>
  </header>
</template>



<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const isLoggedIn = ref(false)
const showDropdown = ref(false)

// 控制目前滑鼠停留的分類 index
const activeIndex = ref(null)

// 假資料!!!!!!!!!使用者登入狀態與資料
const user = ref({
  name: '小明',
  avatar: 'https://randomuser.me/api/portraits/women/72.jpg',
  points: 120
})
// 回首頁
const goHome = () => {
  router.push('/')
}

// 登入登出切換
const login = () => {
  isLoggedIn.value = true
}
const logout = () => {
  isLoggedIn.value = false
}
// 滑鼠移出 header，下拉收起
const closeDropdown = () => {
  activeIndex.value = null
}
// 滑鼠移入 mega menu，下拉維持
const keepDropdown = () => {
  // 不做事，只為阻止滑鼠移開事件讓 dropdown 收起
}

// // 點擊子功能導頁與 API 日誌（可改）
// const handleNavigate = async (item) => {
//   try {
//     await axios.post('/api/log', {
//       user: user.value.name,
//       action: 'navigate',
//       target: item.routeName
//     })
//   } catch (e) {
//     console.error('Log API 失敗', e)
//   }
//   router.push({ name: item.routeName }
// }




//分類功能清單
const menuList = ref([

  {
    title: '包裹管理',
    children: [
      { label: '待領包裹', routeName: 'parcel-pending' },
      { label: '領取紀錄', routeName: 'parcel-history' }
    ]
  },
  {
    title: '公告',
    children: [
      { label: '重要通知', routeName: 'announcement-important' },
      { label: '最新公告', routeName: 'announcement-latest' }
    ]
  },
  {
    title: '預約系統',
    children: [
      { label: '健身房預約', routeName: 'reservation-gym' },
      { label: '游泳池預約', routeName: 'reservation-pool' },
      { label: '停車預約', routeName: 'reservation-parking' }
    ]
  },
  {
    title: '繳費資訊',
    children: [
      { label: '待繳帳單', routeName: 'bill-unpaid' },
      { label: '繳費明細', routeName: 'bill-history' },
      { label: '財務報表', routeName: 'financial-report' }
    ]
  },
  {
    title: '會員服務',
    children: [
      { label: '會員資訊修改', routeName: 'member-profile-edit' },
      { label: '點數轉贈', routeName: 'points-transfer' }
    ]
  },
  {
    title: '報修服務',
    children: [
      { label: '提交報修', routeName: 'repair-request' },
      { label: '維修進度查詢', routeName: 'repair-status' }
    ]
  },
  {
    title: '常見問題',
    children: [
      { label: 'FAQ 問答集', routeName: 'faq' },
      { label: '聯絡客服', routeName: 'contact-us' }, //這是那個提出問題的頁面 上傳嫌警衛滑手機那個
      { label: '回饋與抱怨？', routeName: 'feedback' } //問題的進度跟進
    ]
  },
  {
    title: '車位管理',
    children: [
      { label: '車位資訊維護', routeName: 'parking-info-edit' },
      { label: '停車預約', routeName: 'reservation-parking' },  // 共用同個路徑去韋韋那頁
      { label: '承租車位管理', routeName: 'parking-rent' }
    ]
  }
]

)

</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 14px 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  font-family: 'Segoe UI', sans-serif;
  z-index: 2000;
  position: relative;
}

.logo img {
  height: 40px;
  cursor: pointer;
  transition: transform 0.3s;
}

.logo img:hover {
  transform: scale(1.05);
}

/* 主選單列 (大分類) */
.nav {
  display: flex;
  gap: 24px;
  align-items: center;
}

.nav-item {
  font-size: 16px;
  color: #333;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: color 0.3s, background-color 0.3s;
}

.nav-item:hover,
.nav-item.active {
  color: #007acc;
  background-color: #e6f0fa;
  font-weight: 600;
}



.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.welcome-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.2;
}

.welcome {
  font-size: 15px;
  color: #444;
}

.points {
  font-size: 13px;
  color: #888;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  border: 1px solid #ddd;
}

.placeholder {
  background-color: #ccc;
}

.auth-button {
  background-color: #007acc;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.auth-button:hover {
  background-color: #005f99;
}

/* Mega Menu 下拉容器 */
.mega-menu {
  position: absolute;
  top: 58px;
  left: 0;
  right: 0;
  background: white;
  box-shadow: 0 8px 30px rgb(0 0 0 / 0.1);
  border-radius: 8px;
  padding: 24px 48px;
  z-index: 9999;
  user-select: text;


  display: flex;
  justify-content: center;
}

@keyframes dropdownFade {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-menu.mega {
  top: 40px;
  left: 0;
  right: 0;
  background: #fff;
  padding: 24px 48px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 10;
}

/* Mega Menu 容器內的 flex 排版 */
.mega-grid {
  display: flex;
  gap: 45px;
  justify-content: center;
}

.dropdown-item {
  display: block;
  padding: 8px 16px;
  color: #333;
  text-decoration: none;
}

.dropdown-item:hover {
  background: #f0f0f0;
}

/* 當前滑鼠選中的分類：醒目 */
.mega-active {
  opacity: 1;
  pointer-events: auto;
}

/* 其他沒選中的分類：透明 */
.mega-inactive {
  opacity: 0.4;
  transition: opacity 0.3s;
  pointer-events: auto;
}

/* 每個分類區塊 */
.mega-category {
  flex: 1 1 250px;
  /* 每個分類最小寬度250px，平均撐開 */
  opacity: 0.5;
  transition: opacity 0.3s;
  cursor: default;
  user-select: none;

  display: flex;
  flex-direction: column;
  /* 大標題+子選項縱向排列 */
}

.mega-category:hover {
  opacity: 1 !important;
  /* 滑鼠滑到分類，自動變深 */
}

/* 大分類標題 */
.category-title {

  margin-bottom: 15px;
  color: #308421;
  font-size: 13px;
  border-bottom: 1px solid #eee;
  padding-bottom: 4px;
  user-select: text;
}

/* 子功能項目 */
.category-items .dropdown-item {
  padding: 8px 0;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  user-select: text;
  transition: color 0.3s;
}

.category-items .dropdown-item:hover {
  color: #59cc6e;
}
</style>