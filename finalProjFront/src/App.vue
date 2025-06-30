<template>
  <div id="app">
    <HeaderAll />
    <!-- <BeforeLogIn /> -->
    <!-- <Home /> -->
    <main>
      <div class="d-flex">
        <SideNav />
        <div class="main-content flex-grow-1">
          <RouterView />
        </div>
        <!-- <Finance /> -->
      </div>
    </main>

    <FooterAll />
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
import { useUserStore } from '@/stores/UserStore'
import SideNav from './components/forAll/SideNav.vue';
const user = useUserStore();

//以下放的是測試登入登出的假資料
// 登入
user.login({
  name: '王小明',
  username: 'ming123',
  avatarUrl: 'https://i.pravatar.cc/100?img=13', // 可省略
})
// 登出
// user.logout()
</script>
<style scoped>
.main-content {
  padding: 24px;
  overflow-x: hidden;
}
</style>