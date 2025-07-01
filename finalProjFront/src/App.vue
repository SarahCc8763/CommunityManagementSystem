<template>
  <div id="app">
    <!-- <BeforeLogIn /> -->
    <HeaderAll />

    <main class="main-content">


      <!-- 這裡是左右的Nav -->
      <LeftSideNav />

      <RightSideNav :show="showRightNav" @close="showRightNav = false" />
      <div class="main-area" :class="{ 'with-right-nav': showRightNav }"
        @click="showRightNav && (showRightNav = false)">
        <button class="right-nav-toggle" @click.stop="showRightNav = true">
          <i class="bi bi-layout-sidebar-inset"></i>
        </button>

        <div v-if="!showRightNav" class="drawer-tab" @click.stop="showRightNav = true">
          <i class="bi bi-chevron-left"></i>
          <span class="drawer-tab-text">更多</span>
        </div>
        <div v-if="showRightNav" class="drawer-mask" @click="showRightNav = false"></div>
        <!-- 左右Nav結束 -->

        <RouterView />

        <!-- 大家想測試的頁面可以放這裡喔～ -->
        <!-- <Example /> -->
        <!-- <Home /> -->
        <!-- <Invoice /> -->
        <!-- <FeeTypeAdd /> -->

      </div>

    </main>

  </div>
  <FooterAll />

  <!-- 登入模態框 -->
  <LoginModal :isVisible="showLogin" @close="showLogin = false" @login-success="handleLoginSuccess" />
</template>






<script setup>

// ...原本的 code
//功能類import
import { RouterLink, RouterView } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
import { ref, onMounted, onUnmounted } from 'vue'
import { watch } from 'vue'


//固定的頁首頁尾以及側邊欄位
import FooterAll from './components/forAll/main/FooterAll.vue';
import LoginModal from './components/forAll/main/LoginModal.vue';
import HeaderAll from './components/forAll/main/HeaderAll.vue';
import RightSideNav from './components/forAll/main/RightSideNav.vue';
import LeftSideNav from './components/forAll/main/LeftSideNav.vue';



//還沒改好router但先放著的頁面 
import Home from './views/Home.vue';
import BeforeLogIn from './views/BeforeLogIn.vue';
import finUser from '@/components/finance/finUser/finUser.vue';
import Invoice from '@/components/finance/finUser/Invoice.vue';
import FeeTypeAdd from './components/finance/finAdmin/FeeTypeAdd.vue';



const user = useUserStore()
const showLogin = ref(false)
const showRightNav = ref(false)

watch(showRightNav, (val) => {
  console.log('測試點擊showRightNav:', val)
})
//以下放的是測試登入登出的假資料
// 登入
user.login({
  name: '王小明',
  username: 'ming123',
  avatarUrl: 'https://i.pravatar.cc/100?img=13',
})



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
  align-items: center;
  justify-content: flex-start;
  background: #fff;
  max-width: 100%;
  margin-left: auto;
  margin-right: auto;


  position: relative;
  transition: margin-right 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.main-area.with-right-nav {
  margin-right: 320px;
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
  .right-side-nav {
    width: 220px;
  }

  .main-area {
    margin-right: 220px;
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
</style>
