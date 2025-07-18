<template>
  <div id="app" :class="{ 'dark-mode': route.meta?.dark }">
    <!--  <BeforeLogIn /> -->
    <HeaderAll :isDarkMode="isDarkMode" />
    <main class="main-content">
      <aside>
        <LeftSideNav :isDarkMode="isDarkMode" />
        <RightSideNav :show="showRightNav" @close="showRightNav = false" />
        <button class="right-nav-toggle" @click.stop="showRightNav = true">
          <i class="bi bi-layout-sidebar-inset"></i>
        </button>
        <div v-if="!showRightNav" class="drawer-tab" @click.stop="showRightNav = true">
          <i class="bi bi-chevron-left"></i>
          <span class="drawer-tab-text">更多</span>
        </div>
        <div v-if="showRightNav" class="drawer-mask" @click="showRightNav = false"></div>
      </aside>
      <div class="main-area" :class="[{ 'with-right-nav': showRightNav }, isDarkMode ? 'dark-mode' : '']"
        @click="showRightNav && (showRightNav = false)">
        <RouterView />
        <FeedbackModal />
      </div>
    </main>
    <FooterAll />
    <!-- 登入模態框 -->
    <LoginModal :isVisible="showLogin" @close="showLogin = false" @login-success="handleLoginSuccess" />
  </div>
</template>

<script setup>
import BeforeLogIn from '@/views/BeforeLogIn.vue'
//功能類import
import { RouterLink, RouterView } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
import { ref, computed, onMounted, onUnmounted } from 'vue'


//固定的頁首頁尾以及側邊欄位
import FooterAll from './components/forAll/main/FooterAll.vue';
import LoginModal from './components/forAll/main/LoginModal.vue';
import HeaderAll from './components/forAll/main/HeaderAll.vue';
import RightSideNav from './components/forAll/main/RightSideNav.vue';
import LeftSideNav from './components/forAll/main/LeftSideNav.vue';
// Yu
import FeedbackModal from '@/components/feedback/FeedbackModal.vue'
// Yu

import { useRoute } from 'vue-router'  // ✅ 加上這行
const route = useRoute()



import Swal from 'sweetalert2'
const user = useUserStore()
const showLogin = ref(false)
const showRightNav = ref(false)


// ✅ 只判斷 meta.dark
const isDarkMode = computed(() => route.meta?.dark === true)
const handleShowLoginModal = () => {
  showLogin.value = true
}


// 處理登入成功
window.dispatchEvent(new Event('refresh-community-functions'))
// }
const handleLoginSuccess = (loginData) => {
  user.login(loginData) // ✅ 傳整包 payload，不要自己手動塞東西
  showLogin.value = false

  // 讓其他元件知道登入完成
  window.dispatchEvent(new CustomEvent('login-success', { detail: loginData }))
  window.dispatchEvent(new Event('refresh-community-functions')) // ✅ 即時刷新 header 功能
  console.log(loginData)
}

// 處理登出
const handleLogout = () => {
  // 這裡可以添加登出時的清理邏輯
  console.log('用戶已登出')
}

onMounted(() => {
  window.addEventListener('show-login-modal', handleShowLoginModal)
  window.addEventListener('logout', handleLogout)
})

onUnmounted(() => {
  window.removeEventListener('show-login-modal', handleShowLoginModal)
  window.removeEventListener('logout', handleLogout)
  const route = useRoute()
})




onMounted(() => {
  window.addEventListener('show-login-modal', () => (showLogin.value = true))
  window.addEventListener('logout', () => console.log('用戶已登出'))
})
onUnmounted(() => {
  window.removeEventListener('show-login-modal', () => (showLogin.value = true))
  window.removeEventListener('logout', () => console.log('用戶已登出'))
})

//把sweetAlert放最上面用
Swal.mixin({
  customClass: {
    popup: 'swal-on-top'
  }
})
</script>




<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.main-content {
  flex: 1;
  padding: 0;
  animation: fadeIn 0.8s ease-out;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}



/* 主要內容區 */
.main-area {
  flex: 1;
  min-width: 0;
  margin-left: 80px;
  margin-right: 0;
  padding-top: 72px;
  padding-bottom: 72px;
  min-height: calc(100vh - 72px);
  display: flex;
  flex-direction: column;
  /* align-items: center; */
  justify-content: flex-start;
  background: #fff;
  max-width: 100%;
  margin-left: auto;
  margin-right: auto;
  position: relative;
  transition: margin-right 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.main-area.with-right-nav {
  margin-right: 0px;
}

.main-area.dark-mode {
  background: #23272f !important;
  color: #e0e6ed !important;
}

/* 左右 SideNav */
.left-side-nav {
  position: fixed;
  top: 72px;
  left: 0;
  bottom: 0;
  width: 80px;
  z-index: 2101;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
}

.right-side-nav {
  position: fixed;
  top: 72px;
  right: 0;
  bottom: 0;
  width: 320px;
  z-index: 1001;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
}

.right-nav-toggle {
  position: absolute;
  top: 24px;
  right: 24px;
  z-index: 10;
  background: #fff;
  border: 1.5px solid #e2e8f0;
  border-radius: 50%;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.08);
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.right-nav-toggle:hover {
  background: #f0f4f8;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.12);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 響應式設計 */
@media (max-width: 1200px) {
  .main-area {
    margin-left: 80px;
    margin-right: 220px;
  }
}

@media (max-width: 992px) {
  .main-area {
    margin-left: 80px;
    margin-right: 0;
  }
}

@media (max-width: 768px) {
  .main-area {
    margin-left: 0;
    margin-right: 0;
    padding: 60px 16px 72px 16px;
  }
}


.drawer-tab {
  position: fixed;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  z-index: 2100;
  background: #fff;
  color: #667eea;
  border-radius: 24px 0 0 24px;
  box-shadow: 0 2px 12px rgba(102, 126, 234, 0.10);
  padding: 8px 14px 8px 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s, color 0.2s;
  border: 2px solid #e2e8f0;
  border-right: none;
  min-width: 44px;
  height: 48px;
}

.drawer-tab:hover {
  background: #f0f4f8;
  color: #5a67d8;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.16);
}

.drawer-tab i {
  font-size: 24px;
  margin-right: 2px;
}

.drawer-tab-text {
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 4px;
  color: #667eea;
  user-select: none;
}

.drawer-mask {
  position: fixed;
  top: 72px;
  left: 0;
  right: 320px;
  bottom: 0;
  background: rgba(0, 0, 0, 0.08);
  z-index: 2000;
  cursor: pointer;
  border-radius: 0 0 16px 16px;
}

/* 深色模式 */
.dark-mode {
  background-color: #868fac;
  color: #f1f1f1;
}

.dark-mode .table {
  background-color: #2a2d3c;
  color: #f1f1f1;
  border-color: #444;
}

.dark-mode .table th,
.dark-mode .table td {
  border-color: #444;
}

.dark-mode .table thead {
  background-color: #3a3d4e;
  color: #f0f0f0;
}

.dark-mode .btn {
  background-color: #4a4a6a;
  color: #fff;
  border-color: #5a5f73;
}

.dark-mode .btn:hover {
  background-color: #5a5f73;
}

.dark-mode .section-title {
  color: #ffc107 !important;
}

.dark-mode .modal-content {
  background-color: #2e2f3e;
  color: #f1f1f1;
}

.dark-mode .form-control,
.dark-mode .form-select {
  background-color: #35394a;
  color: #f0f0f0;
  border: 1px solid #666;
}

/* 把swtteAlert放上面 */
.swal-on-top {
  z-index: 99999 !important;
}

.swal2-container {
  z-index: 100000 !important;
}
</style>
