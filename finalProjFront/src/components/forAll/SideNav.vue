<template>
    <aside class="bg-light border-end p-3" style="min-width: 240px;">
      <!-- 會員頭像區（登入後才顯示） -->
      <div v-if="UserStore.isAuthenticated" class="mb-4">
        <div
          class="card user-profile-card border-0 shadow-sm p-3 position-relative"
          data-bs-toggle="dropdown"
          aria-expanded="false"
          style="cursor: pointer;"
        >
          <!-- 頭像與名稱 -->
          <div class="d-flex align-items-center">
            <img
              :src="UserStore.avatarUrl"
              alt="頭像"
              class="rounded-circle me-3 border"
              width="64"
              height="64"
            />
            <div>
              <div class="fw-bold fs-5 mb-1">{{ UserStore.name }}</div>
              <div class="text-muted small">@{{ UserStore.username }}</div>
              <span class="badge bg-success mt-1">一般會員</span>
            </div>
          </div>
  
          <!-- 快捷功能 -->
          <div class="mt-3 d-flex justify-content-around border-top pt-2">
            <router-link to="/profile" class="text-center text-decoration-none text-dark small">
              <i class="bi bi-person fs-5 mb-1"></i><br />檔案
            </router-link>
            <router-link to="/notifications" class="text-center text-decoration-none text-dark small">
              <i class="bi bi-bell fs-5 mb-1"></i><br />通知
            </router-link>
            <router-link to="/account/settings" class="text-center text-decoration-none text-dark small">
              <i class="bi bi-gear fs-5 mb-1"></i><br />設定
            </router-link>
          </div>
  
          <!-- 登出按鈕 -->
          <button class="btn btn-sm btn-outline-danger w-100 mt-3" @click.prevent="logout">
            <i class="bi bi-box-arrow-right me-1"></i> 登出
          </button>
        </div>
      </div>
  
      <!-- 功能選單 -->
      <h5 class="mb-3 text-primary fw-bold">功能選單</h5>
      <nav>
        <ul class="list-unstyled">
          <li v-for="(item, idx) in menuItems" :key="idx" class="mb-2">
            <!-- 有子選單 -->
            <div v-if="item.children">
              <div
                class="d-flex justify-content-between align-items-center py-2 px-3 text-dark fw-bold border-start border-4 border-primary"
                style="cursor:pointer;"
                @click="toggle(idx)"
              >
                <router-link
                  :to="item.link || '#'"
                  class="flex-grow-1 text-dark text-decoration-none"
                  @click.stop
                >
                  {{ item.label }}
                </router-link>
                <span class="triangle" :class="{ rotated: openIdx === idx }">&#9654;</span>
              </div>
  
              <transition name="slide-fade">
                <ul v-if="openIdx === idx" class="list-unstyled ps-4 mt-2">
                  <li v-for="(child, c) in item.children" :key="c" class="mb-1">
                    <router-link :to="child.link" class="text-secondary text-decoration-none">
                      {{ child.label }}
                    </router-link>
                  </li>
                </ul>
              </transition>
            </div>
  
            <!-- 無子選單 -->
            <router-link
              v-else
              :to="item.link"
              class="d-block py-2 px-3 text-dark fw-bold border-start border-4 border-primary text-decoration-none"
            >
              {{ item.label }}
            </router-link>
          </li>
        </ul>
      </nav>
    </aside>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useUserStore } from '@/stores/UserStore'
  
  const UserStore = useUserStore()
  const router = useRouter()
  
  const menuItems = [
    {
      label: '服務介紹',
      link: '/service',
      children: [
        { label: '保單查詢', link: '/service/policy' },
        { label: '理賠申請', link: '/service/claim' }
      ]
    },
    { label: '最新消息', link: '/news' },
    { label: '聯絡我們', link: '/contact' }
  ]
  
  const openIdx = ref(null)
  
  function toggle(idx) {
    openIdx.value = openIdx.value === idx ? null : idx
  }
  
  function logout() {
    UserStore.logout()
    router.push('/login')
  }
  </script>
  
  <style scoped>
  .user-profile-card {
    transition: transform 0.2s;
    background: linear-gradient(to bottom right, #f9fafb, #ffffff);
    border-radius: 16px;
  }
  .user-profile-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  }
  
  .triangle {
    font-size: 0.8rem;
    color: gray;
    transition: transform 0.3s ease;
    user-select: none;
    text-decoration: none;
  }
  .rotated {
    transform: rotate(90deg);
  }
  
  .slide-fade-enter-active,
  .slide-fade-leave-active {
    transition: all 0.25s ease;
  }
  .slide-fade-enter-from,
  .slide-fade-leave-to {
    opacity: 0;
    max-height: 0;
  }
  .slide-fade-enter-to,
  .slide-fade-leave-from {
    opacity: 1;
    max-height: 500px;
  }
  </style>
  