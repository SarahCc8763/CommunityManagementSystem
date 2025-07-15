<template>
  <aside class="left-side-nav">
    <!-- 會員頭像區（登入後才顯示） -->
    <div v-if="UserStore.isAuthenticated" class="user-section">
      <div class="user-profile-card new-member-card no-border-card">
        <div class="member-avatar-block">
          <div class="avatar-container">
            <img :src="UserStore.avatarUrl" alt="頭像" class="user-avatar" />
            <div class="status-indicator"></div>
          </div>
          <div class="member-info-texts">
            <div class="user-name">{{ UserStore.name }}</div>
            <div class="user-row">
              <span class="user-username">{{ UserStore.username }}</span>

              <span class="user-badge">一般會員</span>
            </div>
          </div>
        </div>

        <!-- 快捷功能橫向排列 -->
        <div class="quick-actions-row no-border-actions">
          <router-link to="/profile" class="quick-action-card no-border-action">
            <i class="bi bi-person-circle"></i>
            <span>檔案</span>
          </router-link>
          <router-link to="/notifications" class="quick-action-card no-border-action">
            <i class="bi bi-bell"></i>
            <span>通知</span>
          </router-link>
          <router-link to="/account/settings" class="quick-action-card no-border-action">
            <i class="bi bi-gear"></i>
            <span>設定</span>
          </router-link>
        </div>
        <div class="logout-btn-wrapper">
          <button class="logout-button no-border-logout" @click.prevent="logout">
            <i class="bi bi-box-arrow-right"></i>
            登出
          </button>
        </div>
      </div>
    </div>

    <!-- 登入前的placeholder -->
    <div v-else class="login-placeholder">
      <div class="placeholder-card">
        <div class="placeholder-avatar">
          <i class="bi bi-person-circle"></i>
        </div>
        <div class="placeholder-text">
          <div class="placeholder-title">請先登入</div>
          <div class="placeholder-subtitle">登入後即可使用完整功能</div>
        </div>
        <button class="placeholder-login-btn" @click="showLoginModal">
          <i class="bi bi-box-arrow-in-right"></i>
          立即登入
        </button>
      </div>
    </div>

    <!-- 功能選單 -->
    <div class="menu-section">
      <h5 class="menu-title">功能選單</h5>

      <div class="map-section">
        <div class="map-title">RiverBank社區</div>
        <iframe class="google-map" src="https://www.google.com/maps?q=103台北市大同區環河北路一段113號&output=embed"
          allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
      </div>

      <nav class="menu-nav">
        <ul class="menu-list">
          <li v-for="(item, idx) in menuItems" :key="idx" class="menu-item">
            <!-- 有子選單 -->
            <div v-if="item.children" class="menu-group">
              <div class="menu-header" :class="{ 'menu-header-active': openIdx === idx }" @click="toggle(idx)">
                <router-link :to="item.link || '#'" class="menu-link" @click.stop>
                  {{ item.label }}
                </router-link>
                <i class="bi bi-chevron-down menu-arrow" :class="{ 'menu-arrow-rotated': openIdx === idx }"></i>
              </div>

              <transition name="slide-fade">
                <ul v-if="openIdx === idx" class="submenu-list">
                  <li v-for="(child, c) in item.children" :key="c" class="submenu-item">
                    <router-link :to="child.link" class="submenu-link">
                      {{ child.label }}
                    </router-link>
                  </li>
                </ul>
              </transition>
            </div>

            <!-- 無子選單 -->
            <router-link v-else :to="item.link" class="menu-link menu-link-single">
              {{ item.label }}
            </router-link>
          </li>
        </ul>
      </nav>
    </div>

  </aside>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'

const UserStore = useUserStore()
const router = useRouter()

const menuItems = [
  {
    label: '服務介紹',
    link: '/service',
    children: [
      { label: '改別的', link: '/service/policy' },
      { label: '改別的', link: '/service/claim' }
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
  // 觸發全局登出事件
  window.dispatchEvent(new CustomEvent('logout'))
  router.push('/login')
}

function showLoginModal() {
  // 觸發父組件的登入模態框
  // 這裡需要通過事件向上傳遞
  window.dispatchEvent(new CustomEvent('show-login-modal'))
}

// 監聽全局登出事件
const handleGlobalLogout = () => {
  // SideNav 會自動響應 UserStore 的狀態變化
  // 這裡不需要額外處理，因為模板已經綁定到 UserStore.isAuthenticated
}

onMounted(() => {
  // 監聽全局登出事件
  window.addEventListener('logout', handleGlobalLogout)
})

onUnmounted(() => {
  // 移除事件監聽器
  window.removeEventListener('logout', handleGlobalLogout)
})
</script>

<style scoped>
.left-side-nav {
  min-width: 320px;
  max-width: 320px;
  width: 320px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.2);
  padding: 24px;
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.05);
  animation: slideInLeft 0.6s ease-out;
}

.user-section {
  margin-bottom: 32px;
}

.user-profile-card {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  padding: 36px 24px 28px 24px;
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.10);
  background: linear-gradient(135deg, #fff 60%, #f8fafc 100%);
  border: 1.5px solid #e2e8f0;
  margin-bottom: 12px;
  position: relative;
  min-height: 320px;
}

.member-avatar-block {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 14px;
  margin-bottom: 10px;
}

.avatar-container {
  position: relative;
  margin-right: 16px;
}

.user-avatar {
  width: 54px;
  height: 54px;
  border-radius: 50%;
  border: 3px solid #fff;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.10);
  margin-bottom: 0;
}

.status-indicator {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 16px;
  height: 16px;
  background: #48bb78;
  border: 2px solid #fff;
  border-radius: 50%;
}

.member-info-texts {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  gap: 2px;
}

.user-name {
  font-size: 19px;
  font-weight: 800;
  color: #2d3748;
  margin-bottom: 2px;
  letter-spacing: 0.5px;
  font-family: var(--serif-title), serif;
}

.user-row {
  display: flex;



  flex-direction: column;
  align-items: flex-start;


  gap: 8px;
}

.user-username {
  font-size: 13px;
  color: #718096;
  font-weight: 500;
}

.user-badge {
  font-size: 11px;
  padding: 2px 8px;
  background: linear-gradient(135deg, #e6fffa 0%, #c6f6d5 100%);
  color: #38b2ac;
  border-radius: 20px;
  font-weight: 600;
  margin-bottom: 0;
  letter-spacing: 0.5px;
}

.quick-actions-row {
  display: flex;
  width: 100%;
  justify-content: space-between;
  gap: 12px;
  margin: 12px 0 0 20px;
}

.quick-action-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f7fafc;
  border-radius: 14px;
  padding: 14px 0 8px 0;
  color: #4a5568;
  text-decoration: none;
  font-weight: 500;
  font-size: 13px;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.04);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1.5px solid #e2e8f0;
}

.quick-action-card i {
  font-size: 22px;
  margin-bottom: 4px;
}

.quick-action-card:hover {
  background: linear-gradient(135deg, #e9d8fd 0%, #bee3f8 100%);
  color: #5a67d8;
  transform: translateY(-2px) scale(1.04);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.10);
  border-color: #b794f4;
}

.logout-btn-wrapper {
  margin-top: 12px;

  padding-top: 0;
  display: flex;
  justify-content: flex-end;
}

.logout-button.no-border-logout {
  width: 100%;
  margin-top: 0;

  background: #fff;
  color: #5a67d8;
  border: 2px solid #5a67d8;
  box-shadow: none;
  border-radius: 14px;
  font-size: 15px;
  font-weight: 600;
  padding: 10px 0;
  transition: background 0.2s, color 0.2s, border 0.2s;
}

.logout-button.no-border-logout:hover {
  background: #5a67d8;
  color: #fff;
  border-color: #5a67d8;
}

/* 登入前placeholder樣式 */
.login-placeholder {
  margin-bottom: 32px;
}

.placeholder-card {
  background: linear-gradient(135deg, rgba(226, 232, 240, 0.8) 0%, rgba(203, 213, 224, 0.8) 100%);
  border-radius: 20px;
  padding: 24px;
  border: 2px dashed rgba(160, 174, 192, 0.3);
  text-align: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.placeholder-card:hover {
  border-color: rgba(102, 126, 234, 0.5);
  background: linear-gradient(135deg, rgba(226, 232, 240, 0.9) 0%, rgba(203, 213, 224, 0.9) 100%);
  transform: translateY(-2px);
}

.placeholder-avatar {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e0 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  color: #718096;
  font-size: 32px;
}

.placeholder-text {
  margin-bottom: 20px;
}

.placeholder-title {
  font-size: 18px;
  font-weight: 700;
  color: #4a5568;
  margin-bottom: 8px;
}

.placeholder-subtitle {
  font-size: 14px;
  color: #718096;
  line-height: 1.4;
}

.placeholder-login-btn {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 16px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 16px;
}

.placeholder-login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.placeholder-login-btn i {
  font-size: 16px;
}

.menu-section {
  margin-top: 32px;
}

.menu-title {
  font-size: 18px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(102, 126, 234, 0.1);
}

.menu-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin-bottom: 8px;
}

.menu-group {
  border-radius: 12px;
  overflow: hidden;
}

.menu-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: rgba(102, 126, 234, 0.05);
  border-left: 4px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 0;
}

.menu-header:hover,
.menu-header-active {
  background: rgba(102, 126, 234, 0.1);
  border-left-color: #667eea;
}

.menu-link {
  color: #2d3748;
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  transition: color 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.menu-link:hover {
  color: #667eea;
}

.menu-link-single {
  display: block;
  padding: 12px 16px;
  background: rgba(102, 126, 234, 0.05);
  border-left: 4px solid transparent;
  border-radius: 12px;
  transition: all 0.3s ease;
  min-width: 0;
}

.menu-link-single:hover {
  background: rgba(102, 126, 234, 0.1);
  border-left-color: #667eea;
  color: #667eea;
}

.menu-arrow {
  font-size: 14px;
  color: #a0aec0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.menu-arrow-rotated {
  transform: rotate(180deg);
  color: #667eea;
}

.submenu-list {
  list-style: none;
  padding: 0;
  margin: 0;
  background: rgba(248, 250, 252, 0.8);
}

.submenu-item {
  border-bottom: 1px solid rgba(102, 126, 234, 0.05);
}

.submenu-item:last-child {
  border-bottom: none;
}

.submenu-link {
  display: block;
  padding: 10px 20px;
  color: #4a5568;
  text-decoration: none;
  font-size: 13px;
  transition: all 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.submenu-link:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.05);
  padding-left: 24px;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  max-height: 0;
  transform: translateY(-10px);
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  opacity: 1;
  max-height: 200px;
  transform: translateY(0);
}

.map-section {
  margin-top: 18px;
  margin-bottom: 18px;

  width: 100%;
}

.map-title {
  font-size: 0.98rem;
  font-weight: 700;
  color: #a3bffa;
  margin-bottom: 4px;
}

.google-map {
  width: 100%;
  height: 140px;
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px #232a3640;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 響應式設計 */
@media (max-width: 768px) {
  .left-side-nav {
    min-width: 100%;
    padding: 16px;
  }

  .user-profile-card,
  .placeholder-card {
    padding: 16px;
  }

  .user-avatar,
  .placeholder-avatar {
    width: 48px;
    height: 48px;
  }

  .user-name,
  .placeholder-title {
    font-size: 16px;
  }

  .quick-actions-row {
    padding: 12px 0;
  }
}

@media (max-width: 1200px) {

  .user-name,
  .user-username,
  .user-badge {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .left-side-nav {
    min-width: 100vw;
    padding: 4px;
  }

  .user-profile-card,
  .placeholder-card {
    padding: 8px;
    border-radius: 10px;
  }

  .user-avatar,
  .placeholder-avatar {
    width: 36px;
    height: 36px;
  }

  .user-name,
  .placeholder-title {
    font-size: 10px;
  }

  .user-username,
  .user-badge,
  .placeholder-subtitle {
    font-size: 3px;
  }

  .quick-actions-row {
    gap: 4px;
    padding: 4px 0;
  }

  .logout-button,
  .placeholder-login-btn {
    font-size: 11px;
    padding: 7px 8px;
    border-radius: 8px;
    margin-top: 6px;
  }

  .menu-section {
    margin-top: 12px;
  }

  .menu-title {
    font-size: 13px;
    margin-bottom: 8px;
    padding-bottom: 4px;
  }

  .menu-link,
  .menu-link-single {
    font-size: 11px;
    padding: 7px 8px;
    border-radius: 8px;
  }

  .submenu-link {
    font-size: 10px;
    padding: 6px 10px;
  }
}

.no-border-card {
  background: #fff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: none;
  border-radius: 18px;
  padding: 22px 18px 16px 18px;
  min-height: 150px;
  max-height: 200px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  height: 100%;
  position: relative;
}

.no-border-actions {
  background: none;
  box-shadow: none;
  border: none;
}

.no-border-action {
  background: none;
  border: none;
  box-shadow: none;
  transition: background 0.2s, color 0.2s;
}

.no-border-action:hover {
  background: #f0f4f8;
  color: #5a67d8;
  transform: translateY(-2px) scale(1.04);
}

/* 確保快捷選項文字顯示 */
.quick-action-card span {
  display: inline;
}

.quick-action-card.no-border-action {
  transition: all 0.2s cubic-bezier(0.4,0,0.2,1);
  color: #444;
  background: transparent;
  border-radius: 12px;
  padding: 12px 0 8px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-weight: 500;
}
.quick-action-card.no-border-action:hover {
  background: #f0f4ff;
  color: #667eea;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.10);
  font-weight: 700;
}
.quick-action-card.no-border-action:hover i {
  color: #667eea;
  transform: scale(1.12);
  transition: all 0.2s cubic-bezier(0.4,0,0.2,1);
}
.quick-action-card.no-border-action i {
  font-size: 1.7rem;
  margin-bottom: 4px;
  transition: all 0.2s cubic-bezier(0.4,0,0.2,1);
}
</style>
