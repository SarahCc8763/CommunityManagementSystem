<template>
  <div id="app">
    <HeaderAll />
    <!-- <BeforeLogIn /> -->
    <!-- <Home /> -->
    <!-- <RouterView /> -->
    <main class="main-content"> 
     
        <LeftSideNav />
        <div class="main-area">
          <!-- <Example /> -->
          <Home />
        </div>
        <RightSideNav />
     
    </main>

    <FooterAll /> 

    <!-- 登入模態框 -->
    <LoginModal 
      :isVisible="showLogin" 
      @close="showLogin = false"
      @login-success="handleLoginSuccess"
    />
  </div>
</template>






<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HeaderAll from './components/forAll/HeaderAll.vue';
import Home from './views/Home.vue';
import BeforeLogIn from './views/BeforeLogIn.vue';
import BannerImage from './components/forAll/BannerImage.vue';
import happyFaces from '@/assets/images/finance/happyFaces.jpg';
import Finance from './components/finance/Finance.vue';
import FooterAll from './components/forAll/FooterAll.vue';
import LoginModal from './components/forAll/LoginModal.vue';
import Example from './components/forAll/Example.vue';
import RightSideNav from './components/forAll/RightSideNav.vue';

import { useUserStore } from '@/stores/UserStore'
import LeftSideNav from './components/forAll/LeftSideNav.vue';
import { ref, onMounted, onUnmounted } from 'vue'

const user = useUserStore()
const showLogin = ref(false)

//以下放的是測試登入登出的假資料
// 登入
user.login({
  name: '王小明',
  username: 'ming123',
  avatarUrl: 'https://i.pravatar.cc/100?img=13', // 可省略
})

// 登出
// user.logout()

// 監聽SideNav的登入按鈕事件
const handleShowLoginModal = () => {
  showLogin.value = true
}

// 處理登入成功
const handleLoginSuccess = (loginData) => {
  // 更新用戶狀態
  user.login({
    name: loginData.username,
    username: loginData.username,
    avatarUrl: 'https://i.pravatar.cc/100?img=13'
  })
  showLogin.value = false
  
  // 觸發全局登入成功事件，讓其他組件也能收到通知
  window.dispatchEvent(new CustomEvent('login-success', {
    detail: loginData
  }))
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
})
</script>




<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

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

.main-content {
  flex: 1;
  padding: 0;
  animation: fadeIn 0.8s ease-out;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

/* 固定左右 SideNav */
.left-side-nav {
  position: fixed;
  top: 72px;
  left: 0;
  bottom: 0;
  width: 80px;
  z-index: 1001;
  background: rgba(255,255,255,0.95);
  box-shadow: 2px 0 8px rgba(0,0,0,0.04);
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
  background: rgba(255,255,255,0.95);
  box-shadow: -2px 0 8px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
}

.main-area {
  flex: 1;
  min-width: 0;
  margin-left: 80px;
  margin-right: 320px;
  padding-top: 72px;
  min-height: calc(100vh - 72px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  background: #fff;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 2px 16px rgba(0,0,0,0.04);
  border-radius: 16px;
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
  .right-side-nav {
    width: 220px;
  }
  .main-area {
    margin-right: 220px;
  }
}

@media (max-width: 900px) {
  .main-area {
    margin-left: 0;
    margin-right: 0;
  }
  .left-side-nav,
  .right-side-nav {
    display: none;
  }
}
</style>
