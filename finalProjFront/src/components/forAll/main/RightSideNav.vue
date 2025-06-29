<template>
  <aside class="right-side-nav" :class="{ open: show }">
    <button class="right-nav-close" @click="emit('close')">
      <i class="bi bi-x-circle-fill"></i>
    </button>
   

<!-- 社區活動 -->
<div class="events-card">
  <div class="card-header">
    <i class="bi bi-calendar-event"></i>
    <span>近期活動</span>
  </div>
  <div class="events-content">
    <div class="event-item">
      <div class="event-date">
        <div class="event-day">15</div>
        <div class="event-month">12月</div>
      </div>
      <div class="event-info">
        <div class="event-title">聖誕晚會</div>
        <div class="event-time">19:00 - 22:00</div>
        <div class="event-location">社區大廳</div>
      </div>
    </div>
    <div class="event-item">
      <div class="event-date">
        <div class="event-day">25</div>
        <div class="event-month">12月</div>
      </div>
      <div class="event-info">
        <div class="event-title">新年倒數</div>
        <div class="event-time">23:30 - 00:30</div>
        <div class="event-location">頂樓花園</div>
      </div>
    </div>
  </div>
</div>
    <!-- 廣告橫幅 -->
    <div class="ad-banner">
      <div class="ad-content">
        <div class="ad-title">新住戶優惠</div>
        <div class="ad-desc">訂閱社區檢修服務</div>
        <button class="ad-btn">
          <i class="bi bi-arrow-right"></i>
          立即了解
        </button>
      </div>
      <div class="ad-image">
        <i class="bi bi-house-heart"></i>
      </div>
    </div>

    <!-- 緊急聯絡 -->
    <div class="emergency-card">
      <div class="card-header">
        <i class="bi bi-telephone"></i>
        <span>緊急聯絡</span>
      </div>
      <div class="emergency-content">
        <div class="emergency-item">
          <i class="bi bi-shield-exclamation"></i>
          <span>大廳保全</span>
          <div class="phone">02-1234-5678</div>
        </div>
        <div class="emergency-item">
          <i class="bi bi-tools"></i>
          <span>維修專線</span>
          <div class="phone">02-1234-5679</div>
        </div>
        <div class="emergency-item">
          <i class="bi bi-plus-lg"></i>
          <span>緊急救援</span>
          <div class="phone">119</div>
        </div>
      </div>
    </div>
    <!-- 推薦服務 -->
    <div class="services-card">
      <div class="card-header">
        <i class="bi bi-star"></i>
        <span>推薦服務</span>
      </div>
      <div class="services-content">
        <div class="service-item">
          <div class="service-icon">
            <i class="bi bi-house-check"></i>
          </div>
          <div class="service-info">
            <div class="service-title">居家清潔</div>
            <div class="service-desc">專業清潔服務</div>
            <div class="service-price">NT$ 800/次</div>
          </div>
        </div>
        <div class="service-item">
          <div class="service-icon">
            <i class="bi bi-tools"></i>
          </div>
          <div class="service-info">
            <div class="service-title">維修服務</div>
            <div class="service-desc">24小時快速維修</div>
            <div class="service-price">NT$ 500/次</div>
          </div>
        </div>
        <div class="service-item">
          <div class="service-icon">
            <i class="bi bi-shield-check"></i>
          </div>
          <div class="service-info">
            <div class="service-title">保全服務</div>
            <div class="service-desc">全天候安全守護</div>
            <div class="service-price">NT$ 1,200/月</div>
          </div>
        </div>
      </div>
    </div>



  </aside>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
const props = defineProps({
  show: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['close'])
</script>

<style scoped>
.right-side-nav {
  min-width: 320px;
  max-width: 320px;
  width: 320px;
  background: rgba(255,255,255,0.95);
  box-shadow: -2px 0 8px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 72px;
  right: 0;
  bottom: 0;
  z-index: 2001;
  transform: translateX(100%);
  transition: transform 0.35s cubic-bezier(0.4,0,0.2,1);
  overflow-y: auto;
}
.right-side-nav.open {
  transform: translateX(0);
}
.right-nav-close {
  position: absolute;
  top: 14px;
  right: 14px;
  background: #fff;
  border: 2px solid #667eea;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #4c51bf;
  box-shadow: 0 2px 8px rgba(102,126,234,0.10);
  cursor: pointer;
  z-index: 10;
  transition: background 0.2s, box-shadow 0.2s, border 0.2s, color 0.2s;
}
.right-nav-close:hover {
  background: #667eea;
  color: #fff;
  border-color: #4c51bf;
  box-shadow: 0 4px 16px rgba(102,126,234,0.16);
}

.right-side-nav > * {
  margin: 18px 18px 0 18px;
}
.right-side-nav > *:first-child {
  margin-top: 32px;
}
.right-side-nav > *:last-child {
  margin-bottom: 32px;
}

/* 卡片通用樣式 */
.weather-card,
.stats-card,
.services-card,
.events-card,
.emergency-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(248, 250, 252, 0.9) 100%);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.weather-card:hover,
.stats-card:hover,
.services-card:hover,
.events-card:hover,
.emergency-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-weight: 600;
  color: #2d3748;
  font-size: 14px;
}

.card-header i {
  color: #667eea;
  font-size: 16px;
}

/* 天氣卡片 */
.weather-content {
  text-align: center;
}

.temperature {
  font-size: 32px;
  font-weight: 700;
  color: #2d3748;
  margin-bottom: 4px;
}

.weather-desc {
  color: #718096;
  margin-bottom: 12px;
}

.weather-details {
  display: flex;
  justify-content: space-around;
  font-size: 12px;
  color: #a0aec0;
}

.weather-details span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 統計卡片 */
.stats-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 20px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #718096;
}

/* 服務卡片 */
.services-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.service-item:hover {
  background: rgba(102, 126, 234, 0.1);
  transform: translateX(4px);
}

.service-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.service-info {
  flex: 1;
}

.service-title {
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 2px;
}

.service-desc {
  font-size: 12px;
  color: #718096;
  margin-bottom: 4px;
}

.service-price {
  font-size: 12px;
  color: #667eea;
  font-weight: 600;
}

/* 活動卡片 */
.events-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.event-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 193, 7, 0.1);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.event-item:hover {
  background: rgba(255, 193, 7, 0.15);
  transform: translateX(4px);
}

.event-date {
  text-align: center;
  min-width: 50px;
}

.event-day {
  font-size: 20px;
  font-weight: 700;
  color: #ffc107;
}

.event-month {
  font-size: 12px;
  color: #718096;
}

.event-info {
  flex: 1;
}

.event-title {
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 2px;
}

.event-time,
.event-location {
  font-size: 12px;
  color: #718096;
}

/* 廣告橫幅 */
.ad-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  color: white;
  position: relative;
  overflow: hidden;
  min-height: 120px;
}

.ad-banner::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 200%;
  background: rgba(255, 255, 255, 0.1);
  transform: rotate(45deg);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%) rotate(45deg); }
  100% { transform: translateX(100%) rotate(45deg); }
}

.ad-content {
  flex: 1;
  z-index: 1;
}

.ad-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 4px;
}

.ad-desc {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 12px;
}

.ad-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.ad-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.ad-image {
  font-size: 48px;
  opacity: 0.8;
  z-index: 1;
}

/* 緊急聯絡 */
.emergency-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.emergency-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(220, 53, 69, 0.1);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.emergency-item:hover {
  background: rgba(220, 53, 69, 0.15);
  transform: translateX(4px);
}

.emergency-item i {
  color: #dc3545;
  font-size: 16px;
  width: 20px;
}

.emergency-item span {
  flex: 1;
  font-weight: 500;
  color: #2d3748;
}

.phone {
  font-weight: 600;
  color: #dc3545;
  font-size: 14px;
}

/* 動畫 */
@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 響應式設計 */
@media (max-width: 1200px) {
  .right-side-nav {
    min-width: 280px;
    padding: 20px;
  }
}

@media (max-width: 768px) {
  .right-side-nav {
    display: none;
  }
}
</style> 