<template>

    <!-- Hero 圖片區塊 -->
    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img src="@/assets/images/forSlideShow/livingRoom.jpg" alt="Hero Image" class="img-fluid hero-image" />
                <div class="hero-text">
                    <h1 class="text-white fw-bold">歡迎回到 <span style="color: rgb(220, 245, 179);">RiVER BANK</span> </h1>
                    <h2 class="text-white fw-bold">智慧社區管理，從此開始</h2>
                    <br><br>
                    <p class="text-white" style=""> <span style="color: rgb(228, 245, 220);">
                            整合住戶服務，提升生活品質與社區效率。完美社區的最佳首選 OO建設</span></p>

                </div>
            </div>
        </div>
    </section>


    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-4 fw-bold">社區功能導覽</h2>
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
        link: '/repair'
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
.hero-image-wrapper {
    position: relative;
    text-align: center;
    max-width: 80vw;
    margin: 0 auto;
}

.hero-image {
    width: 100%;
    height: auto;
    max-height: 500px;
    object-fit: cover;
    border-radius: 12px;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.hero-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
    animation: fadeInUp 0.8s ease;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translate(-50%, -40%);
    }

    to {
        opacity: 1;
        transform: translate(-50%, -50%);
    }
}
</style>