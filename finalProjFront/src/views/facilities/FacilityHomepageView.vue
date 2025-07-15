<template>
    <section class="hero-section pt-3 pb-2">
        <div class="container">
            <div class="hero-image-wrapper position-relative fade-in-slide">
                <FacilitySlideShow :items="facilitySlides" carousel-id='facility-carousel' />
                <div class="hero-text position-absolute top-50 start-50 translate-middle text-center">
                    <!-- nothing -->
                </div>
            </div>
        </div>
    </section>

    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-4 fw-bold">公設預約及點數</h2>
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
</template>

<script setup>
import { useRouter } from 'vue-router';
import FacilitySlideShow from '@/components/facilities/FacilitiesSlideShow.vue';
import 'bootstrap-icons/font/bootstrap-icons.css';

const router = useRouter();

//設定輪播器ID:'facility-carousel'載入圖片
// const imagesContext = import.meta.glob('@/assets/images/facilities/*.png', { eager: true, import: 'default' });
// const slideshowImages = Object.values(imagesContext);
const facilitySlides = [
    {
        image: new URL('@/assets/images/facilities/pool.png', import.meta.url).href,
        title: '預約游泳池',
        subtitle: '清涼一夏的最佳選擇，開放式空間安心暢泳',
        link: '/reservations/book/3'
    },
    {
        image: new URL('@/assets/images/facilities/gym.png', import.meta.url).href,
        title: '預約健身房',
        subtitle: '配備完整設備，讓你隨時動起來',
        link: '/reservations/book/2'
    },
    {
        image: new URL('@/assets/images/facilities/tabletennis.png', import.meta.url).href,
        title: '預約桌球室',
        subtitle: '室內舒適空間，揮拍對戰隨時開打',
        link: '/reservations/book/4'
    },
    {
        image: new URL('@/assets/images/facilities/snooker.png', import.meta.url).href,
        title: '預約撞球室',
        subtitle: '沈穩風格場地，休閒與技術兼備',
        link: '/reservations/book/6'
    },
    {
        image: new URL('@/assets/images/facilities/lounge.png', import.meta.url).href,
        title: '預約交誼廳',
        subtitle: '聚會、會議皆宜，社區共享交流空間',
        link: '/reservations/book/1'
    },
    {
        image: new URL('@/assets/images/facilities/parking_grid.png', import.meta.url).href,
        title: '預約停車位',
        subtitle: '即時預約車位，訪客與住戶皆適用',
        link: '/reservations/book/7'
    }
];


const navigate = (link) => {
    if (link) router.push(link);
};

const features = [
    {
        icon: 'bi-table',
        title: '查詢可預約公設',
        description: '查看各公設詳細介紹，並快速建立預約。',
        link: '/facilities/findAll'
    },
    {
        icon: 'bi-water',
        title: '預約游泳池',
        description: '線上預約游泳時段，享受清涼舒適的泳池設施。',
        link: '/reservations/book/3'
    },
    {
        icon: 'bi-heart-pulse',
        title: '預約健身房',
        description: '健身好時光不怕排隊，立即預約健身空間。',
        link: '/reservations/book/2'
    },
    {
        icon: 'bi-award',
        title: '預約桌球室',
        description: '與鄰居揮拍對戰，輕鬆線上預約桌球場地。',
        link: '/reservations/book/4'
    },
    {
        icon: 'bi-disc',
        title: '預約撞球室',
        description: '享受撞球樂趣，線上管理使用時段。',
        link: '/reservations/book/6'
    },
    {
        icon: 'bi-people',
        title: '預約交誼廳',
        description: '聚會、會議、活動，立即預約社區交誼廳。',
        link: '/reservations/book/1'
    },
    {
        icon: 'bi-car-front-fill',
        title: '預約停車位',
        description: '掌握停車位使用，提供訪客或短時車位預約。',
        link: '/reservations/book/7'
    },
    {
        icon: 'bi-cash-stack',
        title: '我的預約紀錄',
        description: '查詢點數異動與使用紀錄，帳務透明清晰。',
        link: '/reservations/history'
    },
    {
        icon: 'bi-arrow-left-right',
        title: '點數轉移',
        description: '將點數轉給其他住戶或家人，社區更方便。',
        link: '/points/transfer'
    },
    {
        icon: 'bi-wallet2',
        title: '點數儲值',
        description: '線上儲值點數，輕鬆支付各項社區服務。',
        link: '/points/topup'
    },
    {
        icon: 'bi-wallet2',
        title: '點數交易紀錄',
        description: '線上儲值點數，輕鬆支付各項社區服務。',
        link: '/points/history'
    }
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
    /* max-width: 80vw; */
    width: 100%;
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

:deep(.carousel-image) {
    width: 100%;
    height: auto;
    max-height: 400px;
    object-fit: cover;
    border-radius: 16px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
    display: block;
}


.fade-in-slide {
    opacity: 0;
    transform: translateY(-30px);
    animation: fadeSlideIn 1.5s ease-out forwards;
}

@keyframes fadeSlideIn {
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.carousel-control-prev,
.carousel-control-next {
    width: 10%;
    /* 點擊寬度拉寬一點 */
    height: 100%;
    top: 0;
    bottom: 0;
    opacity: 1;
    z-index: 10;
}

.carousel-control-prev-icon,
.carousel-control-next-icon {
    background-color: rgba(0, 0, 0, 0.4);
    /* 有透明黑背景才看得清楚 */
    border-radius: 50%;
    padding: 20px;
    background-size: 100% 100%;
}

.carousel-control-prev:hover .carousel-control-prev-icon,
.carousel-control-next:hover .carousel-control-next-icon {
    background-color: rgba(0, 0, 0, 0.7);
}
</style>