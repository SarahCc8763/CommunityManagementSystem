
<template>
  <div class="container mt-4">
    <!-- Hero Banner 圖片與文字 -->
    <div class="position-relative banner-container">
      <img src="@/assets/images/parking/parking-banner.jpg" class="img-fluid w-100 banner-image" alt="社區停車場管理" />
      <div class="banner-overlay-layer"></div>
      <div class="banner-overlay">
        <h2 class="fw-bold text-white">社區停車服務</h2>
      </div>
    </div>
    <div class="row g-4">
      <!-- 1. 查看自己車位與車牌 -->
      <div class="col-md-6 col-lg-4">
        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex align-items-center">
            <i class="bi bi-person-badge fs-1 me-3 text-secondary"></i>
            <div>
              <h5 class="card-title mb-2">我的車位與車牌</h5>
              <p class="card-text text-secondary">查看自己擁有與承租的車位，管理車牌資料。</p>
              <button class="btn btn-outline-primary btn-sm" @click="goTo('mySlots')">前往</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 2. 承租車位與歷史 -->
      <div class="col-md-6 col-lg-4">
        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex align-items-center">
            <i class="bi bi-calendar-check fs-1 me-3 text-secondary"></i>
            <div>
              <h5 class="card-title mb-2">車位承租與紀錄</h5>
              <p class="card-text text-secondary">申請承租社區車位與查看歷史紀錄。</p>
              <button class="btn btn-outline-primary btn-sm" @click="goTo('rental')">前往</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 3. 車位抽籤 -->
      <div class="col-md-6 col-lg-4">
        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex align-items-center">
            <i class="bi bi-box2-heart fs-1 me-3 text-secondary"></i>
            <div>
              <h5 class="card-title mb-2">參加抽籤活動</h5>
              <p class="card-text text-secondary">報名參與車位抽籤，查看抽籤結果。</p>
              <button class="btn btn-outline-primary btn-sm" @click="goTo('lottery')">前往</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goTo = (target) => {
  switch (target) {
    case 'mySlots':
      router.push('/pages/park/my-slots')
      break
    case 'rental':
      router.push('/pages/park/rental-front')
      break
    case 'lottery':
      router.push('/pages/park/lottery-apply')
      break
  }
}
</script>

<style scoped>
.card-text {
  font-size: 0.9rem;
}

.banner-container {
  position: relative;
  height: 240px;
  overflow: hidden;
  border-radius: 12px;
  margin-bottom: 2rem;
}

/* 圖片本身柔化處理 */
.banner-image {
  object-fit: cover;
  width: 100%;
  height: 100%;
  filter: brightness(85%) blur(1px);
}

/* 暗色半透明遮罩（避免圖片太硬） */
.banner-overlay-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(rgba(0, 0, 0, 0.35), rgba(0, 0, 0, 0.35));
  z-index: 1;
}

/* 文字區塊：玻璃感背景＋置中＋z-index 蓋過遮罩 */
.banner-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  transition: transform 0.4s ease, opacity 0.4s ease;
}


/* 文字本身 hover 動畫效果 */
.banner-overlay h2 {
  color: white;
  transition: all 0.4s ease;
}

.banner-container:hover .banner-overlay h2 {
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.9), 0 0 20px rgba(255, 255, 255, 0.7);
  transform: scale(1.1);
}
</style>
