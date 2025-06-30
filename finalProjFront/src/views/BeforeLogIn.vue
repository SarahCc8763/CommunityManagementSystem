
<!-- 這個我還沒改好 先當個靜態網頁到時候截圖用 -->
<!-- 登入前看到的頁面 -->








<template>
    <div>
        <!-- 半透明遮罩 -->
        <div v-if="isLoggingIn" class="modal-backdrop"></div>


        <header class="header">
            <!-- LOGO -->
            <div class="logo" @click="goHome">
                <img src="https://dummyimage.com/100x40/ccc/000&text=Logo" alt="Logo" />
            </div>
            <nav>OO建設-智慧社區管理系統</nav>
            <!-- 主選單列（灰色、不可點） -->
            <nav class="nav" @mouseleave="showTooltip = null">
                <div v-for="item in fakeMenu" :key="item" class="nav-item text-gray-400 cursor-default"
                    @mouseenter="handleTooltip(item)">
                    {{ item }}
                    <transition name="fade-slide">
                        <span v-if="showTooltip === item" class="tooltip">請先登入</span>
                    </transition>
                </div>
            </nav>

            <!-- 使用者登入區塊 -->
            <div class="user-info">
                <div>
                    <button @click="isLoggingIn = true" class="auth-button">
                        登入
                    </button>
                </div>
            </div>
        </header>
        <!-- Bootstrap Modal 登入框 -->
        <div v-if="isLoggingIn" class="modal d-block" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title d-flex align-items-center">
                                <i class="bi bi-person-circle me-2"></i> 登入系統
                            </h5>
                            <button type="button" class="btn-close btn-close-white"
                                @click="isLoggingIn = false"></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">帳號</label>
                            <input v-model="loginForm.username" type="text" class="form-control" placeholder="請輸入帳號" />
                        </div>
                        <div class="mb-3">
                            <label class="form-label">密碼</label>
                            <input v-model="loginForm.password" type="password" class="form-control"
                                placeholder="請輸入密碼" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-outline-secondary" @click="isLoggingIn = false">取消</button>
                        <button class="btn btn-success" @click="submitLogin">登入</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Bootstrap Modal 登入框結束 -->

        <SlideShow :images="slideshowImages" :captions="slideshowCaptions" carousel-id="main-hero" />

        <!-- 關於區塊 -->


        <section class="section about" style="margin-top: 50px">
            <h2>用心築夢・共創永續</h2>
            <p>建設秉持信念，推動人本與自然融合的建築價值，打造台灣最值得信賴的建築品牌。</p>
        </section>

        <!-- 綠色區塊 -->
        <div class="intro-section">
            <div class="intro-card">
                <a href="https://www.farglory-land.com.tw/" target="_blank" class="intro-image-link">
                    <img src="https://i0.wp.com/www.b-studio.com.tw/wp-content/uploads/2023/01/20230104.jpg?w=1896&ssl=1"
                        alt="綠建築" class="intro-image" />
                </a>
                <div class="intro-text">
                    <h2>綠意共生・築未來</h2>
                    <p>
                        我們秉持永續發展理念，融合綠建築設計、智慧節能系統與低碳建材，打造兼具環保與舒適的現代空間。從社區能源管理到雨水回收與自然採光，我們用科技實現對土地的承諾，為每一位住戶創造健康、宜居、共好的生活環境。我們融合綠建築設計、智慧節能系統與低碳建材，打造兼具環保與舒適的現代空間。從社區能源管理到雨水回收與自然採光，用科技實現對土地的承諾。
                    </p>
                </div>
            </div>
        </div>
        <!-- 建案介紹區塊 -->
        <section class="projects-section container py-5">
            <h2 class="text-center mb-4">熱門建案介紹</h2>
            <div class="row g-4">
                <div class="col-md-4" v-for="project in projects" :key="project.name">
                    <div class="card h-100 shadow-sm">
                        <img :src="project.image" class="card-img-top" :alt="project.name">
                        <div class="card-body">
                            <h5 class="card-title">{{ project.name }}</h5>
                            <p class="card-text">{{ project.description }}</p>
                            <a :href="project.link" target="_blank" class="btn btn-outline-primary btn-sm">查看更多</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>


    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import SlideShow from '@/components/forAll/SlideShow.vue'
const router = useRouter()
const isLoggingIn = ref(false)
const loginForm = ref({ username: '', password: '' })
const showTooltip = ref(null)
const fakeMenu = ['公告與包裹', '預約系統', '繳費資訊', '會員服務']
const slideshowImages = [
    new URL('@/assets/images/forSlideShow/indoorDeco.webp', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/livingRoom.jpg', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/nightView.jpg', import.meta.url).href,
    new URL('@/assets/images/forSlideShow/sunHouse.jpg', import.meta.url).href
]

const slideshowCaptions = ['美麗住宅，質感生活', '夜色中點亮城市', '這裡是圖片3介紹', '這裡是圖片4介紹']
const projects = [
    {
        name: '大里左岸公園',
        description: '坐擁綠意與水岸第一排，結合休閒與交通便利。',
        image: 'https://dummyimage.com/600x400/ccc/000&text=Project+1',
        link: 'https://www.farglory-land.com.tw/project/1'
    },
    {
        name: '晶和晴空匯',
        description: '景觀視野絕佳，規劃完善的智慧社區建築。',
        image: 'https://dummyimage.com/600x400/bbb/000&text=Project+2',
        link: 'https://www.farglory-land.com.tw/project/2'
    },
    {
        name: 'VM智慧公寓',
        description: '智慧複合園區，兼具住宅與商辦機能。',
        image: 'https://dummyimage.com/600x400/aaa/000&text=Project+3',
        link: 'https://www.farglory-land.com.tw/project/3'
    }, {
        name: '大里左岸公園',
        description: '坐擁綠意與水岸第一排，結合休閒與交通便利。',
        image: 'https://dummyimage.com/600x400/ccc/000&text=Project+1',
        link: 'https://www.farglory-land.com.tw/project/1'
    },
    {
        name: '晶和晴空匯',
        description: '景觀視野絕佳，規劃完善的智慧社區建築。',
        image: 'https://dummyimage.com/600x400/bbb/000&text=Project+2',
        link: 'https://www.farglory-land.com.tw/project/2'
    },
    {
        name: 'VM智慧公寓',
        description: '智慧複合園區，兼具住宅與商辦機能。',
        image: 'https://dummyimage.com/600x400/aaa/000&text=Project+3',
        link: 'https://www.farglory-land.com.tw/project/3'
    }
]
// 回到首頁
const goHome = () => {
    router.push('/')
}

// 登入後頁面 要記得改!!!!!!!!
const submitLogin = () => {
    if (loginForm.value.username && loginForm.value.password) {
        isLoggingIn.value = false
        router.push('/dashboard')
    }
}

//請先登入的提醒
const handleTooltip = (item) => {
    if (!showTooltip.value) {
        showTooltip.value = item
    }
}
</script>

<style scoped>
/* 一些標題跟內文 */
.about {
    text-align: center;
    font-family: "Noto Serif TC", "PMingLiU", "Times New Roman", serif;
    padding: 2rem 1rem;
}

.about h2 {
    font-weight: bold;
    font-size: 2rem;
    margin-bottom: 0.5rem;
}

.about p {
    font-size: 1.1rem;
    color: #555;
}

/* intro的style的style 綠色那塊啦 */
.intro-section {
    background: linear-gradient(to right, #e4f1df, #d0e2c4);
    padding: 60px 20px;
    display: flex;
    justify-content: center;
}

.intro-card {
    display: flex;
    gap: 40px;
    background-color: white;
    border-radius: 16px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    padding: 32px;
    max-width: 1100px;
    width: 100%;
    align-items: center;
}

.intro-image-link {
    display: block;
    flex-shrink: 0;
    margin-left: 20px;
    transition: transform 0.4s ease;
    cursor: pointer;
}

.intro-image-link:hover {
    transform: scale(1.06);
}

.intro-image {
    width: 480px;
    max-width: 100%;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.intro-text {
    flex: 1;
    color: #2f4f2f;
}

.intro-text h2 {
    font-size: 2rem;
    font-weight: 700;
    margin-bottom: 1rem;
}

.intro-text p {
    font-size: 1.1rem;
    line-height: 1.8;
    white-space: pre-line;
}


/* 介紹建案 */
.projects-section {
    background: #f8f9fa;
}

.card-img-top {
    height: 200px;
    object-fit: cover;
}

/* 這裡都是header的style */
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    padding: 14px 30px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo img {
    height: 40px;
    cursor: pointer;
}

.nav {
    display: flex;
    gap: 24px;
}

.nav-item {
    position: relative;
    font-size: 16px;
    color: #999;
}

.tooltip {
    position: absolute;
    top: 120%;
    left: 50%;
    transform: translateX(-50%);
    background: #f0f0f0;
    color: #333;
    padding: 6px 10px;
    border-radius: 6px;
    font-size: 13px;
    white-space: nowrap;
    z-index: 10;
    opacity: 0;
    animation: fadeSlideIn 0.3s ease forwards;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

@keyframes fadeSlideIn {
    from {
        opacity: 0;
        transform: translate(-50%, -4px);
    }

    to {
        opacity: 1;
        transform: translate(-50%, 0);
    }
}

.fade-slide-enter-active,
.fade-slide-leave-active {
    transition: opacity 0.3s, transform 0.3s;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
    opacity: 0;
    transform: translateY(-4px);
}

.auth-button {
    background-color: #007acc;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.auth-button:hover {
    background-color: #005f99;
}

.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.4);
    z-index: 1040;
}

/* header的style結束 */
</style>