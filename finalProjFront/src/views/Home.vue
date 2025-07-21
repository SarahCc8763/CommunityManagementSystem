<template>

    <!-- Hero 圖片區塊 -->
    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img src="@/assets/images/forSlideShow/livingRoom.jpg" alt="Hero Image" class="img-fluid hero-image" />
                <div class="hero-text">
                    <h1 class="serif-title text-white fw-bold">歡迎回到 <span style="color: rgb(220, 245, 179);">RiVER
                            BANK</span> </h1>
                    <h2 class="serif-title text-white fw-bold">好家宅社區管理，從此開始</h2>
                    <br><br>
                    <p class="text-white" style=""> <span style="color: rgb(228, 245, 220);">
                            整合住戶服務，提升生活品質與社區效率。完美社區的最佳首選 智匯建設</span></p>

                </div>
            </div>
        </div>
    </section>




    <!-- 最新公告區域 -->
    <div class="announcements-section">
        <div class="section-header">
            <h2 class="serif-title section-title">
                <i class="bi bi-megaphone"></i>
                最新公告
            </h2>
            <a :href="baseUrl + '/announcement-latest'">
                <button class="view-all-btn">

                    <i class="bi bi-arrow-right"></i>
                    查看全部
                </button>
            </a>
        </div>

        <div class="announcements-grid">
            <!-- 公告 -->
            <div v-for="bulletin in bulletins" :key="bulletin.id"
                :class="['announcement-card', getGridColor(bulletin.categoryName)]">
                <div class="announcement-header">
                    <div class="announcement-badge fs-6">
                        <i :class="['bi', getIcon(bulletin.categoryName)]"></i>
                        {{ bulletin.categoryName }}
                    </div>

                    <div class="announcement-date">{{ formatDate(bulletin.postTime) }}</div>
                </div>

                <h3 class="announcement-title">{{ bulletin.title }}</h3>
                <p class="announcement-content">
                    {{ normalizeNewline(truncateText(bulletin.description, 50)) }}
                    <span v-if="bulletin.description.length > 50">
                        ...
                    </span>
                </p>
                <div class="announcement-footer">
                    <span class="announcement-author">發布人：{{ bulletin.userName }}</span>

                </div>
            </div>
        </div>

    </div>

    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="serif-title text-center mb-4 fw-bold">社區功能導覽</h2>
            <div class="row g-4">
                <div class="col-md-4" v-for="feature in filteredFeatures" :key="feature.title">
                    <div class="card h-100 shadow-sm border-0 feature-card" @click="navigate(feature)">
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


</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router';
import SlideShow from '@/components/forAll/SlideShow.vue';
import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'


const userStore = useUserStore()
const groupedCards = ref([])
const filteredFeatures = ref([])

//Yu start
const baseUrl = window.location.origin
const communityId = userStore.communityId
const bulletins = ref([])
const categoryList = ref([])
const gridClass = ['important', 'event', 'service', '']
const iconClass = ['bi-exclamation-triangle', 'bi-calendar-check', 'bi-info-circle', 'bi-megaphone']

const formatDate = (dt) => new Date(dt).toLocaleString()
const truncateText = (text, maxLength) => text?.length > maxLength ? text.slice(0, maxLength) : text

function normalizeNewline(text) {
    return text?.replace(/\\n/g, '\n') || ''
}

function getGridColor(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 4

    return gridClass[index]
}
function getIcon(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 4
    return iconClass[index]
}


function fetchBulletins() {
    axios.get('/api/bulletin/community/' + communityId)
        .then(res => {
            const now = new Date()

            // 篩選：已發佈 && 現在時間在 postTime 和 removeTime 之間
            const postedList = res.data.list.filter(val =>
                val.postStatus === true &&
                new Date(val.postTime) <= now &&
                new Date(val.removeTime) > now
            )

            // 依 isPinned 再依 postTime 排序
            const sortedList = postedList.sort((a, b) => {
                if (a.isPinned === b.isPinned) {
                    // 同樣都是置頂或都不是 → 用 postTime 新到舊
                    return new Date(b.postTime) - new Date(a.postTime)
                }
                // isPinned 為 true 的排前面
                return a.isPinned ? -1 : 1
            })

            bulletins.value = sortedList.slice(0, 2)
            console.log(bulletins.value);
            // 取分類名稱（不重複）
            const cats = new Set(postedList.map(b => b.categoryName))
            categoryList.value = [...cats]
        })
        .catch(err => {
            // //console.error('載入公告失敗', err)
        })
}
//Yu End
onMounted(async () => {
    try {
        const res = await axios.get(`/communitys/functions/${userStore.communityId}`)
        const allowed = res.data // 後端回傳的功能 key 陣列，例如：["PACKAGE", "TICKET", ...]

        // 只保留被允許的功能卡片
        filteredFeatures.value = features.filter(f => allowed.includes(f.key))
        fetchBulletins()

    } catch (err) {
        console.error('❌ 載入社區功能失敗', err)
    }
})


const router = useRouter();

const navigate = (feature) => {
    // 有改---------------------------------
    // if (link) router.push(link);
    if (userStore.roleId == 2 && feature.linkSecurity) {
        router.push(feature.linkSecurity)
    } else if (feature.link) router.push(feature.link);
    // 有改---------------------------------
};

const features = [
    {
        icon: 'bi-box-seam',
        title: '包裹管理',
        description: '即時查詢與領取住戶包裹狀態，確保重要物品不遺漏。',
        link: '/packages',
        key: 'PACKAGE',
        linkSecurity: '/packages_security'
    },
    {
        icon: 'bi-car-front-fill',
        title: '停車場管理',
        description: '掌握社區停車位使用狀況，並支援訪客車輛申請與排程。',
        link: '/pages/park/parking-front',
        key: 'PARK'
    },
    {
        icon: 'bi-person-circle',
        title: '帳戶資訊',
        description: '檢視與更新個人資料、聯絡方式，方便又安全。',
        link: '/Users'
    },
    {
        icon: 'bi bi-cash',
        title: '帳務資訊',
        description: '快速查帳，繳費與明細查詢。',
        link: '/finUser'
    },
    {
        icon: 'bi-megaphone',
        title: '最新公告',
        description: '快速接收社區重要通知與活動資訊，不再錯過任何消息。',
        link: '/announcements',
        key: 'NOTICE'
    },
    {
        icon: 'bi-question-circle',
        title: '常見問題',
        description: '整理住戶最常遇到的問題與解答，操作流程一目瞭然。',
        link: '/faq',
        key: 'FQA'
    },
    {
        icon: 'bi-calendar-check',
        title: '公設預約',
        description: '線上預約健身房、交誼廳等公設，點數管理。',
        link: '/facilities',
        key: 'BOOKING'
    },
    {
        icon: 'bi-tools',
        title: '報修單',
        description: '設備損壞通報即時送達，快速安排維修處理。',
        link: '/TicketDashboard',
        key: 'TICKET'
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
    max-width: 1200px;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 32px;
    padding-left: 16px;
    padding-right: 16px;
}

.section-header {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 48px;
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
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
    justify-content: center;
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

@media (max-width: 900px) {
    .announcements-grid {
        grid-template-columns: 1fr;
        gap: 24px;
    }

    .announcements-section {
        padding-left: 8px;
        padding-right: 8px;
    }

    .section-header {
        flex-direction: column;
        gap: 16px;
    }
}
</style>