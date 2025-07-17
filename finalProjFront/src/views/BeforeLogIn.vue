<template>
  <div class="login-mask">
    <div>


      <!-- 超大 hero 大圖（全寬） -->
      <section class="hero-full-img-section">
        <img :src="bigBuilding" alt="智匯建設" class="hero-full-img" />
        <div class="hero-full-img-text">
          <h1>智匯建設 你的新生活選擇</h1>
          <p>智匯建設以「誠信、服務、創新、永續」為經營理念，數十年來從建築精緻家園，到提升服務品質；從提供優質生活空間，到創新生活方式。</p>
        </div>
      </section>

      <!-- 智能管理系統大區塊（全寬，置中） -->
      <section class="system-banner-section">
        <!-- 新增滿寬白色背景 container -->
        <div class="system-banner-tabs">
          <div class="system-banner-tab active"><i class="bi bi-stars me-1"></i>系統特色</div>
          <div class="system-banner-tab"><i class="bi bi-person me-1"></i>住戶服務</div>
          <div class="system-banner-tab"><i class="bi bi-shield-check me-1"></i>安全保障</div>
          <div class="system-banner-tab"><i class="bi bi-gear me-1"></i>管理員功能</div>
        </div>
        <div class="system-banner-full-bg">
          <div class="system-banner-container">
            <!-- 左側：智能管理系統 LOGO 與介紹 -->
            <div class="system-banner-logo-block">
              <img src="@/assets/images/main/Logo.jpg" alt="智能管理系統LOGO" class="system-banner-logo" />
            </div>
            <div class="system-banner-content">
              <div class="system-banner-label"><i class="bi bi-award me-2"></i>獨家專屬智能管理系統</div>
              <div class="system-banner-title">好家宅社區管理系統</div>
              <ul class="system-banner-feature-list">
                <li><i class="bi bi-app-indicator text-primary me-1"></i> 一站式住戶服務 App，報修、繳費、公告、投票、活動一手掌握</li>
                <li><i class="bi bi-person-badge text-success me-1"></i> 管理員後台高效管理，數據分析、即時通知</li>
                <li><i class="bi bi-people text-info me-1"></i> 支援多社區、多角色權限，安全又便利</li>
                <li><i class="bi bi-emoji-smile text-warning me-1"></i> 住戶滿意度提升，社區凝聚力更強</li>
              </ul>
              <div class="system-banner-desc">
                <strong>好家宅</strong> 是智匯建設旗下專為現代社區打造的智慧管理平台，讓住戶與管理員都能享受高效、便利、安全的社區生活體驗。
              </div>
              <button class="login-btn system-banner-login-btn mt-3" @click="showLoginModal">
                <i class="bi bi-box-arrow-in-right"></i> 立即登入體驗
              </button>
            </div>
          </div>
          <!-- 右側：用心築夢・共創永續區域 -->
          <div class="system-banner-hero-text">
            <h1>用心築夢・共創永續</h1>
            <div class="hero-divider"></div>
            <p class="hero-desc">智匯建設秉持信念，推動人本與自然融合的建築價值，打造台灣最值得信賴的建築品牌。</p>
            <ul class="feature-list">
              <li><i class="bi bi-house-door"></i> 智慧門禁與安全管理</li>
              <li><i class="bi bi-lightning"></i> 綠能節能設計</li>
              <li><i class="bi bi-people"></i> 社區活動豐富多元</li>
              <li><i class="bi bi-phone"></i> 一站式住戶服務 App</li>
            </ul>
            <button class="btn btn-outline-primary">
              <i> 開啟你的下個旅程</i>
            </button>
          </div>
        </div>
        <!-- SlideShow 區塊拉寬展示 -->
        <div class="system-banner-slideshow">
          <SlideShow :images="slideshowImages" :captions="slideshowCaptions" carousel-id="main-hero" :autoplay="true"
            :interval="3000" />
        </div>
      </section>

      <!-- 下方三欄（左右介紹小卡+中間主要內容） -->
      <div class="main-3col-layout">
        <!-- 左側介紹小卡 -->
        <aside class="side-float-card left">
          <div class="side-float-block">
            <i class="bi bi-info-circle side-float-icon"></i>
            <div class="side-float-title">關於我們</div>
            <div class="side-float-content">專業團隊，品質保證<br>智慧建築，綠能永續<br>完善售後，服務至上</div>
          </div>
          <div class="side-float-block">
            <i class="bi bi-telephone side-float-icon"></i>
            <div class="side-float-title">聯絡資訊</div>
            <div class="side-float-content">
              客服：02-1234-5678<br>
              Email: info@riverbank.com
            </div>
          </div>
        </aside>

        <!-- 中間主要內容（建案卡片） -->
        <main class="main-content">
          <section class="projects-section bg-light py-5">
            <div class="container">
              <h2 class="text-center fw-bold mb-5">熱門建案介紹</h2>
              <div class="row g-4">
                <div class="col-md-4" v-for="project in projects" :key="project.name">
                  <div class="card h-100 shadow-sm project-card">
                    <img :src="project.image" class="card-img-top" :alt="project.name">
                    <div class="card-body">
                      <h5 class="card-title">{{ project.name }}</h5>
                      <p class="card-text">{{ project.description }}</p>
                      <a :href="project.link" target="_blank" class="btn btn-outline-primary btn-sm">查看更多</a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </main>

        <!-- 右側介紹小卡 -->
        <aside class="side-float-card right">
          <div class="side-float-block">
            <i class="bi bi-megaphone side-float-icon"></i>
            <div class="side-float-title">最新消息</div>
            <div class="side-float-content">新案開賣，敬請期待！</div>
          </div>
          <div class="side-float-block">
            <i class="bi bi-gift side-float-icon"></i>
            <div class="side-float-title">專屬優惠</div>
            <div class="side-float-content">預約賞屋享好禮</div>
          </div>
          <div class="side-float-block">
            <i class="bi bi-share side-float-icon"></i>
            <div class="side-float-title">社群連結</div>
            <div class="side-float-content">
              <a href="#" class="text-primary me-2"><i class="bi bi-facebook"></i></a>
              <a href="#" class="text-danger me-2"><i class="bi bi-instagram"></i></a>
              <a href="#" class="text-success"><i class="bi bi-line"></i></a>
            </div>
          </div>
        </aside>
      </div>

      <!-- Footer -->
      <footer class="bg-dark text-white pt-5 pb-3 mt-0">
        <div class="container">
          <div class="row">
            <div class="col-md-4 mb-4">
              <div class="d-flex align-items-center gap-3 mb-3">
                <img src="https://img.freepik.com/premium-vector/building-logo-design-vector_67715-609.jpg" alt="Logo"
                  style="width: 50px; height: 50px; border-radius: 8px;" />
                <h5 class="mb-0 mt-1">智匯建設</h5>
              </div>
              <p class="mb-2 small">打造智慧生活的每一磚一瓦</p>
              <div class="small lh-sm">
                <div>Email：<a href="mailto:OO123@gmail.com"
                    class="text-white text-decoration-underline">OO123@gmail.com</a></div>
                <div>電話：02-1234-5678</div>
                <div>地址：12345 假地址市 建設路100號</div>
              </div>
            </div>
            <div class="col-md-2 mb-4">
              <h6 class="text-uppercase fw-bold border-bottom pb-1 mb-3">快速導覽</h6>
              <ul class="list-unstyled small">
                <li><a href="#" class="text-white text-decoration-none">首頁</a></li>
                <li><a href="#" class="text-white text-decoration-none">最新消息</a></li>
                <li><a href="#" class="text-white text-decoration-none">工程案例</a></li>
                <li><a href="#" class="text-white text-decoration-none">聯絡我們</a></li>
              </ul>
            </div>
            <div class="col-md-3 mb-4">
              <h6 class="text-uppercase fw-bold border-bottom pb-1 mb-3">法務資訊</h6>
              <ul class="list-unstyled small">
                <li><a href="#" class="text-white text-decoration-none">隱私權政策</a></li>
                <li><a href="#" class="text-white text-decoration-none">使用者條款</a></li>
                <li><a href="#" class="text-white text-decoration-none">網站安全聲明</a></li>
                <li><a href="#" class="text-white text-decoration-none">Cookie 政策</a></li>
              </ul>
            </div>
            <div class="col-md-3 mb-4">
              <h6 class="text-uppercase fw-bold text-light mb-3">訂閱電子報</h6>
              <p class="small mb-2">不定期寄送最新優惠與消息</p>
              <form>
                <div class="mb-2">
                  <input type="email" class="form-control form-control-sm" placeholder="輸入您的 Email" />
                </div>
                <button type="submit" class="btn btn-sm btn-outline-light">訂閱</button>
              </form>
            </div>
          </div>
          <hr class="border-secondary my-4" />
          <div class="row justify-content-between align-items-center">
            <div class="col-md-6 mb-2 mb-md-0">
              <a href="#" class="text-white me-3"><i class="bi bi-facebook"></i></a>
              <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
              <a href="#" class="text-white"><i class="bi bi-line"></i></a>
            </div>
            <div class="col-md-6 text-md-end">
              <small class="text-secondary">&copy; 2025 智匯建設. 保留所有權利。</small>
            </div>
          </div>
        </div>
      </footer>
    </div>
  </div>
</template>

<script setup>
import SlideShow from '@/components/forAll/SlideShow.vue'
import bigBuilding from '@/assets/images/main/bigBuilding.jpg'

const slideshowImages = [
  new URL('@/assets/images/forSlideShow/indoorDeco.webp', import.meta.url).href,
  new URL('@/assets/images/forSlideShow/livingRoom.jpg', import.meta.url).href,
  new URL('@/assets/images/forSlideShow/nightView.jpg', import.meta.url).href,
  new URL('@/assets/images/forSlideShow/sunHouse.jpg', import.meta.url).href
]
const slideshowCaptions = [
  '現代美學空間，品味生活新高度',
  '溫馨居家，讓幸福每一天都在發生',
  '夜色璀璨，守護您的每一刻安全',
  '陽光綠意，打造永續健康社區'
]
const projects = [
  {
    name: '大里左岸公園',
    description: '坐擁綠意與水岸第一排，結合休閒與交通便利。',
    image: 'https://www.kunyue.com.tw/upload/image/hot_mod/hot_a08_data12.jpg',
    link: 'https://www.farglory-land.com.tw/project/1'
  },
  {
    name: '晶和晴空匯',
    description: '景觀視野絕佳，規劃完善的智慧社區建築。',
    image: 'https://www.abic.com.tw/upload/greenbeauty/x12.jpg.pagespeed.ic.u47Dl3HMGh.jpg',
    link: 'https://www.farglory-land.com.tw/project/2'
  },
  {
    name: 'VM智慧公寓',
    description: '智慧複合園區，兼具住宅與商辦機能。',
    image: 'https://tpl.housetube.tw/img/products/popup/3/9/6/5/5/39655_01.jpg?1624952756',
    link: 'https://www.farglory-land.com.tw/project/3'
  },
  {
    name: '碧海園洲',
    description: '綠意盎然，海景視野，喜愛生活的最佳選擇。',
    image: 'https://cdn2.ettoday.net/images/buildcase/0/164_buildcase.jpg',
    link: 'https://www.farglory-land.com.tw/project/1'
  },
  {
    name: '帝璽玉寶',
    description: '王者之姿，尊榮建設 成就你的不凡',
    image: 'https://housedomo.com.tw/wp-content/uploads/2024/11/v123-tw-a3cc8a53274c3480ac8e84bca9bc0a42-1536x1024-1.jpg',
    link: 'https://www.farglory-land.com.tw/project/2'
  },
  {
    name: '茉荷苑',
    description: '荷姿百態 生機盎然 讓您與家人享受悠閒時光。',
    image: 'https://blog.housetube.tw/wp-content/uploads/2019/12/bc04a6ce97c6632ba94e799f6a866141.jpg',
    link: 'https://www.farglory-land.com.tw/project/3'
  }
]

function showLoginModal() {
  window.dispatchEvent(new CustomEvent('show-login-modal'))
}
</script>

<style scoped>
.login-mask {
  position: relative;
  z-index: 9999;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgb(240, 243, 244);
  flex-direction: column;
  overflow-y: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}

.main-header {
  width: 100%;
  background: #d3ebff;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 4px 24px rgba(102, 126, 234, 0.4), 0 2px 8px rgba(102, 126, 234, 0.7);
  position: relative;
  z-index: 20;
}

.header-container {
  max-width: 1600px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 72px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-logo,
.logo-img {
  max-width: 100px;
  max-height: 60px;
  width: auto;
  height: auto;
  object-fit: contain;
  border-radius: 8px;
  display: block;
}

.header-title {
  font-size: 1.6rem;
  font-weight: 800;
  color: #667eea;
  letter-spacing: 2px;
}

.header-login-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 32px;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.header-login-btn:hover {
  background: #5a67d8;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.12);
}

.hero-flex-section {
  width: 100vw;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 48px 0 48px 0;
  display: flex;
  justify-content: center;
}

.hero-flex-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 56px;
  max-width: 1200px;
  width: 100%;
}

.hero-carousel.wide-carousel {
  width: 700px;
  height: 400px;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.13);
  display: flex;
  align-items: center;
  justify-content: center;
}

.wide-carousel :deep(img) {
  width: 100% !important;
  height: 320px !important;
  object-fit: cover !important;
  border-radius: 24px !important;
}

.hero-text {
  max-width: 420px;
  flex: 1;
  padding-top: 18px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-text h1 {
  font-size: 2.2rem;
  font-weight: 800;
  color: #2d3748;
  margin-bottom: 8px;
}

.hero-divider {
  width: 48px;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
  margin-bottom: 18px;
}

.hero-desc {
  font-size: 1.1rem;
  color: #4a5568;
  margin-bottom: 18px;
  line-height: 1.7;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0 0 18px 0;
}

.feature-list li {
  font-size: 1rem;
  color: #5a67d8;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.login-btn {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 14px 0;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  margin-top: 32px;
  transition: background 0.2s, box-shadow 0.2s;
}

.login-btn:hover {
  background: #5a67d8;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.12);
}

.main-login-btn {
  margin-top: 24px;
  font-size: 18px;
  padding: 16px 0;
}

.projects-section {
  margin-top: 0;
}

.ad-card {
  width: 100%;
  background: linear-gradient(135deg, #f8fafc 60%, #e9eafc 100%);
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.08);
  padding: 24px 20px 18px 20px;
  margin-bottom: 12px;
  text-align: center;
}

.ad-title {
  font-size: 18px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 8px;
}

.ad-desc {
  font-size: 14px;
  color: #4a5568;
  margin-bottom: 12px;
}

.ad-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.ad-btn:hover {
  background: #5a67d8;
}

.logo-block {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32px;
}

.system-banner-container {
  max-width: 1200px;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 48px;
  background: #fff;

  padding: 20px 48px 40px 48px;
  margin-top: 60px;
  margin-bottom: 10px;
}

.system-banner-logo-block {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.system-banner-logo {
  height: 90px;
  width: auto;
  border-radius: 16px;
  margin-bottom: 10px;
}

.brand-title {
  font-size: 22px;
  font-weight: 800;
  color: #2d3748;
  margin-bottom: 14px;
}

.system-banner-feature-list {
  list-style: disc inside;
  margin: 0 0 8px 0;
  padding-left: 0;
}

.system-banner-feature-list li {
  margin-bottom: 6px;
  display: flex;
  align-items: center;
}

.system-banner-desc {
  font-size: 1.05rem;
  color: #2d3748;
  margin-top: 10px;
}

.login-btn.system-banner-login-btn {
  margin-top: 22px;
  font-size: 18px;
  padding: 16px 0;
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.login-btn.system-banner-login-btn:hover {
  background: #5a67d8;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.12);
}

.system-banner-tabs {
  display: flex;
  gap: 18px;
  justify-content: flex-start;
  align-items: flex-end;
  margin: 0 auto 4px auto;
  /* 原本 18px，改為 4px */
  max-width: 1200px;
  padding-left: 12px;
}

.system-banner-tab {
  background: #f7fafe;
  color: #667eea;
  letter-spacing: 2px;
}

.nav-list {
  width: 100%;
  margin-bottom: 40px;
}

.nav-item {
  padding: 14px 0;
  font-size: 16px;
  color: #4a5568;
  border-bottom: 1px solid #e2e8f0;
  cursor: pointer;
  text-align: center;
  transition: background 0.2s, color 0.2s;
}

.system-banner-tab.active {
  background: #fff;
  color: #2d3748;
  border-bottom: 3px solid #667eea;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.13);
  z-index: 2;
}

.system-banner-hero-text {
  flex: 1;
  max-width: 380px;
  background: #e8f1e6;
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(102, 126, 234, 0.10);
  padding: 36px 32px 32px 32px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 20px;
  margin-bottom: 20px;
  margin-left: 24px;
}

.system-banner-slideshow {
  width: 100vw;
  max-width: 1600px;
  margin: 32px auto 0 auto;
  display: flex;
  justify-content: center;
}

.system-banner-slideshow :deep(.carousel),
.system-banner-slideshow :deep(img) {
  width: 90vw !important;
  max-width: 1400px !important;
  height: 420px !important;
  object-fit: cover !important;
  border-radius: 24px !important;
}

/* 新增滿寬白色背景 container CSS */
.system-banner-full-bg {
  width: 100vw;
  background: #fff;
  display: flex;
  justify-content: center;
  box-sizing: border-box;
  padding: 0;
  margin: 0 auto 0 auto;
}
</style>