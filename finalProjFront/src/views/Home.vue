<template>

    <!-- Hero 圖片區塊 -->
    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img src="@/assets/images/forSlideShow/livingRoom.jpg" alt="Hero Image" class="img-fluid hero-image" />
                <div class="hero-text">
                    <h1 class="serif-title text-white fw-bold">歡迎回到 <span style="color: rgb(220, 245, 179);">RiVER
                            BANK</span> </h1>
                    <h2 class="serif-title text-white fw-bold">智慧社區管理，從此開始</h2>
                    <br><br>
                    <p class="text-white" style=""> <span style="color: rgb(228, 245, 220);">
                            整合住戶服務，提升生活品質與社區效率。完美社區的最佳首選 OO建設</span></p>

                </div>
            </div>
        </div>
    </section>


    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="serif-title text-center mb-4 fw-bold">社區功能導覽</h2>
            <div class="row g-4">
                <div class="col-md-4" v-for="feature in features" :key="feature.title">
                    <div class="card h-100 shadow-sm border-0 feature-card" @click="navigate(feature.link)">
                        <div class="card-body text-center">
                            <i :class="['bi', feature.icon, 'animated-icon']" class="display-4 text-success mb-3"></i>
                            <h5 class="card-title fw-bold">{{ feature.title }}</h5>
                            <p class="card-text text-muted">{{ feature.description }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <SlideShow :images="slideshowImages" carousel-id="home-carousel" />

    <div class="home-container">
        <!-- 歡迎橫幅 -->
        <div class="welcome-banner">
            <div class="banner-content">
                <h1 class="banner-title">歡迎來到智慧社區管理系統</h1>
                <p class="banner-subtitle">讓您的居住生活更加便利與安全</p>
                <div class="banner-stats">
                    <div class="stat-item">
                        <i class="bi bi-people"></i>
                        <span>1,234 住戶</span>
                    </div>
                    <div class="stat-item">
                        <i class="bi bi-shield-check"></i>
                        <span>24/7 保全</span>
                    </div>
                    <div class="stat-item">
                        <i class="bi bi-star"></i>
                        <span>98% 滿意度</span>
                    </div>
                </div>
            </div>
            <div class="banner-image">
                <i class="bi bi-building"></i>
            </div>
        </div>

        <!-- 最新公告區域 -->
        <div class="announcements-section">
            <div class="section-header">
                <h2 class="serif-title section-title">
                    <i class="bi bi-megaphone"></i>
                    最新公告
                </h2>
                <button class="view-all-btn">
                    <i class="bi bi-arrow-right"></i>
                    查看全部
                </button>
            </div>

            <div class="announcements-grid">
                <!-- 重要公告 -->
                <div class="announcement-card important">
                    <div class="announcement-header">
                        <div class="announcement-badge">
                            <i class="bi bi-exclamation-triangle"></i>
                            重要
                        </div>
                        <div class="announcement-date">2024/12/10</div>
                    </div>
                    <h3 class="announcement-title">電梯維護通知</h3>
                    <p class="announcement-content">
                        為確保住戶安全,A棟電梯將於12月15日進行年度維護,預計維護時間為上午9:00至下午5:00,
                        請住戶提前安排行程,造成不便敬請見諒。
                    </p>
                    <div class="announcement-footer">
                        <span class="announcement-author">管理委員會</span>
                        <button class="read-more-btn">
                            <i class="bi bi-arrow-right"></i>
                            閱讀更多
                        </button>
                    </div>
                </div>

                <!-- 一般公告 -->
                <div class="announcement-card">
                    <div class="announcement-header">
                        <div class="announcement-badge">
                            <i class="bi bi-info-circle"></i>
                            一般
                        </div>
                        <div class="announcement-date">2024/12/08</div>
                    </div>
                    <h3 class="announcement-title">聖誕晚會活動通知</h3>
                    <p class="announcement-content">
                        一年一度的聖誕晚會即將來臨！誠摯邀請所有住戶參加12月15日晚間7點在社區大廳舉辦的聖誕晚會，
                        現場將有精彩表演、美食饗宴及抽獎活動，歡迎闔家參與。
                    </p>
                    <div class="announcement-footer">
                        <span class="announcement-author">活動委員會</span>
                        <button class="read-more-btn">
                            <i class="bi bi-arrow-right"></i>
                            閱讀更多
                        </button>
                    </div>
                </div>

                <!-- 活動公告 -->
                <div class="announcement-card event">
                    <div class="announcement-header">
                        <div class="announcement-badge">
                            <i class="bi bi-calendar-event"></i>
                            活動
                        </div>
                        <div class="announcement-date">2024/12/05</div>
                    </div>
                    <h3 class="announcement-title">社區健康講座</h3>
                    <p class="announcement-content">
                        本週六下午2點將舉辦「冬季養生保健」講座，邀請專業醫師為住戶講解冬季健康管理要點，
                        地點：社區會議室，名額有限，請提前報名。
                    </p>
                    <div class="announcement-footer">
                        <span class="announcement-author">健康促進組</span>
                        <button class="read-more-btn">
                            <i class="bi bi-arrow-right"></i>
                            閱讀更多
                        </button>
                    </div>
                </div>

                <!-- 服務公告 -->
                <div class="announcement-card service">
                    <div class="announcement-header">
                        <div class="announcement-badge">
                            <i class="bi bi-tools"></i>
                            服務
                        </div>
                        <div class="announcement-date">2024/12/03</div>
                    </div>
                    <h3 class="announcement-title">新增智慧門禁系統</h3>
                    <p class="announcement-content">
                        為提升社區安全性，本社區已全面升級智慧門禁系統，支援手機APP開門、訪客預約等功能，
                        詳細使用說明請至管理室索取或查看APP教學。
                    </p>
                    <div class="announcement-footer">
                        <span class="announcement-author">技術支援組</span>
                        <button class="read-more-btn">
                            <i class="bi bi-arrow-right"></i>
                            閱讀更多
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>




</template>

<script setup>
import { useRouter } from 'vue-router';
import SlideShow from '@/components/forAll/SlideShow.vue';
import 'bootstrap-icons/font/bootstrap-icons.css';

const router = useRouter();

const navigate = (link) => {
    if (link) router.push(link);
};

const features = [
    {
        icon: 'bi-box-seam',
        title: '包裹管理',
        description: '即時查詢與領取住戶包裹狀態，確保重要物品不遺漏。',
        link: '/package'
    },
    {
        icon: 'bi-car-front-fill',
        title: '停車場管理',
        description: '掌握社區停車位使用狀況，並支援訪客車輛申請與排程。',
        link: '/parking'
    },
    {
        icon: 'bi-person-circle',
        title: '帳戶資訊',
        description: '檢視與更新個人資料、聯絡方式與繳費紀錄，方便又安全。',
        link: '/account'
    },
    {
        icon: 'bi-megaphone',
        title: '最新公告',
        description: '快速接收社區重要通知與活動資訊，不再錯過任何消息。',
        link: '/announcements'
    },
    {
        icon: 'bi-question-circle',
        title: '常見問題',
        description: '整理住戶最常遇到的問題與解答，操作流程一目瞭然。',
        link: '/faq'
    },
    {
        icon: 'bi-calendar-check',
        title: '公設預約',
        description: '線上預約健身房、交誼廳等公設，避免重複與衝突。',
        link: '/reservation'
    },
    {
        icon: 'bi-tools',
        title: '報修單',
        description: '設備損壞通報即時送達，快速安排維修處理。',
        link: '/TicketList'
    }
];

const slideshowImages = [
    new URL('@/assets/images/forSlideShow/indoorDeco.webp', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/livingRoom.jpg', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/nightView.jpg', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/sunHouse.jpg', import.meta.url).href
];
</script>

<style scoped>
.container {
    max-width: 1200px;
    margin-left: auto;
    margin-right: auto;
}

.home-container {
    max-width: 1200px;
    margin-left: auto;
    margin-right: auto;
}

.welcome-banner,
.announcements-section {
    max-width: 900px;
    margin-left: auto;
    margin-right: auto;
}

.card-body i {
    font-size: 3rem;
    display: inline-block;
    transition: transform 0.3s ease;
}

.feature-card {
    transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
    cursor: pointer;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 12px 30px rgba(0, 0, 0, 0.12);
}

.feature-card {
    background-color: #ffffff;
    /* 初始背景色 */
}

.feature-card:hover {
    background-color: #f8f9fa;
    /* 柔和變化 */
}

.feature-card:hover .animated-icon {
    animation: drop-in 1s ease;
    color: #28a745;
}

@keyframes drop-in {
    from {
        transform: translateY(-12px);
        opacity: 0;
    }

    to {
        transform: translateY(0);
        opacity: 1;
    }
}

.card-body {
    background-color: transparent;
    transition: background-color 1s ease;
}

/* Hero 圖片區塊樣式 */
.hero-section {
    position: relative;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto 32px auto;
    background: transparent;
    overflow: hidden;
    min-height: 340px;
    display: flex;
    align-items: center;
    justify-content: center;

    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.hero-image-wrapper {
    position: relative;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    overflow: hidden;
}

.hero-image {
    width: 100%;
    max-width: 1200px;
    height: 380px;
    object-fit: cover;
    display: block;
    filter: brightness(0.7);

}

.hero-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 2;
    width: 100%;
    max-width: 900px;
    text-align: center;
    color: #fff;
    pointer-events: none;
}

.hero-text h1,
.hero-text h2,
.hero-text p {
    color: #fff;
    text-shadow: 0 2px 16px rgba(0, 0, 0, 0.25);
}

@media (max-width: 900px) {
    .hero-image-wrapper {
        max-width: 100%;
        padding: 16px 0;
    }

    .hero-image {
        border-radius: 16px;
    }
}

.home-container {
    padding: 24px;
    max-width: 1200px;
    margin: 0 auto;
}

/* 歡迎橫幅 */
.welcome-banner {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 32px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: white;
    position: relative;
    overflow: hidden;
}

.welcome-banner::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 100%;
    height: 200%;
    background: rgba(255, 255, 255, 0.1);
    transform: rotate(45deg);
    animation: shimmer 4s infinite;
}

@keyframes shimmer {
    0% {
        transform: translateX(-100%) rotate(45deg);
    }

    100% {
        transform: translateX(100%) rotate(45deg);
    }
}

.banner-content {
    flex: 1;
    z-index: 1;
}

.banner-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 12px;
}

.banner-subtitle {
    font-size: 18px;
    opacity: 0.9;
    margin-bottom: 24px;
}

.banner-stats {
    display: flex;
    gap: 24px;
}

.stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    opacity: 0.9;
}

.banner-image {
    font-size: 80px;
    opacity: 0.3;
    z-index: 1;
}

/* 公告區域 */
.announcements-section {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.section-title {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 24px;
    font-weight: 600;
    color: #2d3748;
    margin: 0;
}

.section-title i {
    color: #667eea;
}

.view-all-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 25px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 8px;
}

.view-all-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.announcements-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 24px;
}

.announcement-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.announcement-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.announcement-card.important::before {
    background: linear-gradient(135deg, #f56565 0%, #e53e3e 100%);
}

.announcement-card.event::before {
    background: linear-gradient(135deg, #ffc107 0%, #f6ad55 100%);
}

.announcement-card.service::before {
    background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
}

.announcement-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.announcement-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.announcement-badge {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    background: rgba(102, 126, 234, 0.1);
    color: #667eea;
}

.announcement-card.important .announcement-badge {
    background: rgba(245, 101, 101, 0.1);
    color: #f56565;
}

.announcement-card.event .announcement-badge {
    background: rgba(255, 193, 7, 0.1);
    color: #ffc107;
}

.announcement-card.service .announcement-badge {
    background: rgba(72, 187, 120, 0.1);
    color: #48bb78;
}

.announcement-date {
    font-size: 12px;
    color: #a0aec0;
}

.announcement-title {
    font-size: 18px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 12px;
    line-height: 1.4;
}

.announcement-content {
    color: #718096;
    line-height: 1.6;
    margin-bottom: 20px;
    display: -webkit-box;

    -webkit-box-orient: vertical;
    overflow: hidden;
}

.announcement-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.announcement-author {
    font-size: 12px;
    color: #a0aec0;
}

.read-more-btn {
    background: none;
    border: none;
    color: #667eea;
    font-size: 12px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
    transition: all 0.3s ease;
}

.read-more-btn:hover {
    color: #5a6fd8;
    transform: translateX(4px);
}

/* 快速功能區 */
.quick-functions {
    margin-bottom: 32px;
}

.functions-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.function-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    text-align: center;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
}

.function-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.function-icon {
    width: 60px;
    height: 60px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
    color: white;
    font-size: 24px;
}

.function-card h3 {
    font-size: 16px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 8px;
}

.function-card p {
    font-size: 14px;
    color: #718096;
    margin: 0;
}

/* 響應式設計 */
@media (max-width: 768px) {
    .home-container {
        padding: 16px;
    }

    .welcome-banner {
        flex-direction: column;
        text-align: center;
        padding: 30px 20px;
    }

    .banner-title {
        font-size: 24px;
    }

    .banner-subtitle {
        font-size: 16px;
    }

    .banner-stats {
        justify-content: center;
        flex-wrap: wrap;
    }

    .banner-image {
        font-size: 60px;
        margin-top: 20px;
    }

    .section-header {
        flex-direction: column;
        gap: 16px;
        align-items: flex-start;
    }

    .announcements-grid {
        grid-template-columns: 1fr;
    }

    .functions-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 480px) {
    .functions-grid {
        grid-template-columns: 1fr;
    }
}
</style>