<!-- 這個我還沒改好 先當個靜態網頁到時候截圖用 -->
<!-- 登入前看到的頁面 -->








<template>

<div class="before-login-layout">
        <div class="login-mask">

            

 

        <!-- 左側 Nav（fixed） -->
        <aside class="before-login-left-nav">
            <div class="nav-card">
                <div class="logo-block">
                    <img src="@/assets/images/main/ZhLogo.jpg" alt="建商LOGO" class="logo-img" />
                    <div >智匯建設 </div>
                </div>
                <nav class="nav-list">
                    <div class="nav-item">關於我們</div>
                    <div class="nav-item">建案特色</div>
                    <div class="nav-item">聯絡資訊</div>
                    <div class="nav-item">常見問題</div>
                </nav>
                <button class="login-btn" @click="showLoginModal">
                    <i class="bi bi-box-arrow-in-right"></i> 立即登入
                </button>
            </div>
        </aside>

        <!-- 主內容區塊 -->


        <!-- 取代原本 BannerImage -->
<div style="width: 100vw; height: 100vh; margin: 0; padding: 0; overflow: hidden; position: relative;">
  <img :src="bigBuilding" alt="智匯建設" style="width: 100vw; height: 100vh; object-fit: cover; display: block;" />
  <div style="position: absolute; left: 5%; top: 60%; transform: translateY(-50%); color: white; text-shadow: 0 2px 4px rgba(0,0,0,0.6); max-width: 600px;">
    <h2 style="font-size: 2.5rem; font-weight: bold; margin-bottom: 1rem;">智匯建設 你的新生活選擇</h2>
    <p style="font-size: 1.25rem;">智匯建設以「誠信、服務、創新、永續」為經營理念，數十年來從建築精緻家園，到提升服務品質；從提供優質生活空間，到創新生活方式。</p>
  </div>
</div>


        <main class="before-login-main">
            <div class="main-content-wrapper">
                <div class="hero-section">
                    <div class="hero-carousel wide-carousel">
                        <SlideShow :images="slideshowImages" :captions="slideshowCaptions" carousel-id="main-hero" />
                    </div>
                    <div class="hero-text">
                        <h1>用心築夢・共創永續</h1>
                        <div class="hero-divider"></div>
                        <p class="hero-desc">智匯建設秉持信念，推動人本與自然融合的建築價值，打造台灣最值得信賴的建築品牌。</p>
                        <ul class="feature-list">
                            <li><i class="bi bi-house-door"></i> 智慧門禁與安全管理</li>
                            <li><i class="bi bi-lightning"></i> 綠能節能設計</li>
                            <li><i class="bi bi-people"></i> 社區活動豐富多元</li>
                            <li><i class="bi bi-phone"></i> 一站式住戶服務 App</li>
                        </ul>
                        <button class="login-btn main-login-btn" @click="showLoginModal">
                            <i class="bi bi-box-arrow-in-right"></i> 立即登入體驗
                        </button>
                    </div>
                </div>
                <!-- 建案介紹區塊 -->
                <section class="projects-section">
                    <h2 class="text-center mb-4">熱門建案介紹</h2>
                    <div class="row g-4">
                        <div class="col-md-4" v-for="project in projects" :key="project.name">
                            <div class="card h-100 shadow-sm">
                                <img :src="project.image" class="card-img-top" :alt="project.name">
                                <div class="card-body">
                                    <h5 class="card-title">{{ project.name }}</h5>
                                    <p class="card-text">{{ project.description }}</p>
                                    <a :href="project.link" target="_blank"
                                        class="btn btn-outline-primary btn-sm">查看更多</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </main>

        <!-- 右側廣告/介紹卡片（fixed） -->
        <aside class="before-login-right-ad">
            <div class="nav-card">
                <div class="ad-card">
                    <div class="ad-title">新住戶專屬優惠</div>
                    <div class="ad-desc">立即預約賞屋，享受專屬好禮！</div>
                    <button class="ad-btn">
                        <i class="bi bi-arrow-right"></i> 了解更多
                    </button>
                </div>
                <div class="ad-card">
                    <div class="ad-title">建案熱銷中</div>
                    <div class="ad-desc">智慧宅、綠建築、全齡社區，歡迎參觀！</div>
                </div>
                <div class="ad-card">
                    <div class="ad-title">聯絡我們</div>
                    <div class="ad-desc">客服專線：02-1234-5678<br />Email: info@riverbank.com</div>
                </div>
            </div>
        </aside>
    </div>
</div>
<div class="bg-dark text-white pt-5 pb-3">
    <div class="container">
      <!-- 上排：Logo + 簡介 + 快速連結 -->
      <div class="row">

        <!-- Logo & 公司介紹 -->
        <div class="col-md-4 mb-4">
          <div class="d-flex align-items-center gap-3 mb-3">
            <img src="https://img.freepik.com/premium-vector/building-logo-design-vector_67715-609.jpg" alt="Logo"
              style="width: 50px; height: 50px; border-radius: 8px;" />
            <h5 class="mb-0 mt-1">智匯建設</h5>
          </div>
          <p class="mb-2 small">打造智慧生活的每一磚一瓦</p>
          <div class="small lh-sm">
            <div>Email：<a href="mailto:OO123@gmail.com" class="text-white text-decoration-underline">OO123@gmail.com</a>
            </div>
            <div>電話：02-1234-5678</div>
            <div>地址：12345 假地址市 建設路100號</div>
          </div>
        </div>

        <!-- 快速導覽 -->
        <div class="col-md-2 mb-4">
          <h6 class="text-uppercase fw-bold border-bottom pb-1 mb-3">快速導覽</h6>
          <ul class="list-unstyled small">
            <li><a href="#" class="text-white text-decoration-none">首頁</a></li>
            <li><a href="#" class="text-white text-decoration-none">最新消息</a></li>
            <li><a href="#" class="text-white text-decoration-none">工程案例</a></li>
            <li><a href="#" class="text-white text-decoration-none">聯絡我們</a></li>
          </ul>
        </div>

        <!-- 法律資訊 -->
        <div class="col-md-3 mb-4">
          <h6 class="text-uppercase fw-bold border-bottom pb-1 mb-3">法務資訊</h6>
          <ul class="list-unstyled small">
            <li><a href="#" class="text-white text-decoration-none">隱私權政策</a></li>
            <li><a href="#" class="text-white text-decoration-none">使用者條款</a></li>
            <li><a href="#" class="text-white text-decoration-none">網站安全聲明</a></li>
            <li><a href="#" class="text-white text-decoration-none">Cookie 政策</a></li>
          </ul>
        </div>

        <!-- 電子報訂閱 -->
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

      <!-- 底部：社群圖示與版權 -->
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
 </div>
</template>

<script setup>
import SlideShow from '@/components/forAll/SlideShow.vue'
import bigBuilding from '@/assets/images/main/bigBuilding.jpg'


import { useUserStore } from '@/stores/UserStore'
import { onMounted } from 'vue'
const userStore = useUserStore()
onMounted(() => {
    console.log('BeforeLogIn mounted, isAuthenticated:', userStore.isAuthenticated)
  userStore.logout()
})
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
  position: fixed;
  z-index: 9999;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgb(240, 243, 244); 
  flex-direction: column;
  overflow-y: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}
.before-login-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 64px;
    background: #fff;
    box-shadow: 0 2px 12px rgba(102, 126, 234, 0.08);
    z-index: 3000;
    display: flex;
    align-items: center;
    justify-content: center;
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

.before-login-layout {
    display: flex;
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.nav-card {
    background: #fff;
    border-radius: 24px;
    box-shadow: 0 8px 32px rgba(102, 126, 234, 0.10);
    padding: 40px 24px 32px 24px;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 24px;
    box-sizing: border-box;
}

.before-login-left-nav {
    width: 260px;
    flex-shrink: 0;
    position: fixed;
    top: 55%;
    left: 0;
    transform: translateY(-50%);
    z-index: 2000;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
}

.before-login-right-ad {
    width: 260px;
    flex-shrink: 0;
    position: fixed;
    top: 55%;
    right: 0;
    transform: translateY(-50%);
    z-index: 2000;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
    gap: 24px;
    padding-top: 0;
    padding-bottom: 0;
}

.before-login-main {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding: 64px 0 48px 0;
    min-height: 100vh;
}

.main-content-wrapper {
    width: 100%;
    max-width: 1000px;
    margin: 0 auto;
    background: #fff;

    box-shadow: 0 8px 32px rgba(102, 126, 234, 0.10);
    padding: 10px 40px 40px 40px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    margin-left: 280px;
    margin-right: 280px;
}

.hero-section {
    display: flex;
    gap: 48px;
    align-items: center;
    justify-content: center;
}

.hero-carousel {
    width: 300px;
    border-radius: 18px;
    box-shadow: 0 4px 24px rgba(102, 126, 234, 0.10);
    flex-shrink: 0;
    align-self: flex-start;
}

.wide-carousel {
    width: 600px;
    height: 320px;
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

.logo-img {
    width: 72px;
    height: 72px;
    border-radius: 16px;
    margin-bottom: 10px;
}

.brand-title {
    font-size: 22px;
    font-weight: 800;
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

.nav-item:hover {
    background: #f0f4f8;
    color: #667eea;
}
/* 加大 SlideShow 區塊高度 */
.hero-carousel.wide-carousel {
  height: 350px;
  max-width: 600px;
  margin-bottom: 24px;
}
.hero-carousel.wide-carousel :deep(img),
.hero-carousel.wide-carousel :deep(.slide-image) {
  height: 100%;
  object-fit: cover;
}

</style>