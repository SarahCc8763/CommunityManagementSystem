<!--
 這是功能卡片們, 可以展示小功能如 車位相關可以做"車位管理"的heading + 1.車位資訊查詢 2.預約停車位 3.月租車位抽籤 這三張小卡

 使用方法：

 template:
    <FeatureCards :features="OO"  heading="可寫可不寫"/>


 script:
    import FeatureCards from '@/components/forAll/FeatureCards.vue';
    const OO = [
    {
        icon: 'bi-box-seam', //這個是bootstrap的內建icon,可以問GPT哪個icon適合然後放這裡
        title: '包裹管理',
        description: '即時查詢與領取住戶包裹狀態，確保重要物品不遺漏。',
        link: '/package'
    },
    {
        icon: 'bi-question-circle-fill', 
        title: '常見問題與支援',
        description: '找不到想要的資訊嗎? 點擊這裡為您服務', //感覺每個人都可以加一個這個, 裡面頁面就放相應的FAQ+陳同學的 回報問題 按鈕連結
        link: '/package'
    },]



 **features 陣列必傳，每個項目需包含title,其餘可寫可不寫
 **可自己改寫style如 .feature-card 或 .card-body改字體位置跟卡片大小



-->

<template>
    <section class="py-5 bg-light">
        <div class="container">
            <h2 v-if="heading" class="text-center mb-4 fw-bold">{{ heading }}</h2>
            <div class="row g-4">
                <div class="col-md-4" v-for="feature in features" :key="feature.title">
                    <div class="card h-100 shadow-sm border-0 feature-card" @click="navigate(feature.link)">
                        <div class="card-body text-center">
                            <span class="diamond-frame mb-3 d-inline-block">
                              <i v-if="feature.icon" :class="['bi', feature.icon, 'animated-icon']"></i>
                            </span>
                            <h5 v-if="feature.title" class="card-title fw-bold">{{ feature.title }}</h5>
                            <p v-if="feature.description" class="card-text text-muted">{{ feature.description }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
    heading: {
        type: String,
        default: ''
    },
    features: {
        type: Array,
        required: true
    }
})

const router = useRouter()
const navigate = (link) => {
    if (link) router.push(link)
}
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

.feature-card:hover {
    background-color: #f8f9fa;
    transform: scale(1.02);
}

.feature-card:hover .animated-icon {
    animation: diamond-rotate 0.7s cubic-bezier(0.4,0,0.2,1);
    color: #28a745;
}

.feature-card:hover .diamond-frame {
  animation: diamond-rotate 0.7s cubic-bezier(0.4,0,0.2,1);
}
.diamond-frame {
  display: inline-block;
  padding: 0;
  border: none;
  background: none;
}
.diamond-frame, .diamond-frame * {
  text-decoration: none !important;
  border-bottom: none !important;
  box-shadow: none !important;
}
.animated-icon {
  font-size: 3rem;
  color: #198754;
  display: inline-block;
  transition: color 0.3s, transform 0.3s;
  border: none !important;
  box-shadow: none !important;
  background: none !important;
}

@keyframes diamond-rotate {
    0% {
        transform: rotate(0deg);
    }
    20% {
        transform: rotate(-12deg);
    }
    40% {
        transform: rotate(12deg);
    }
    60% {
        transform: rotate(-8deg);
    }
    80% {
        transform: rotate(8deg);
    }
    100% {
        transform: rotate(0deg);
    }
}

.card-body {
    background-color: transparent;
    transition: background-color 1s ease;
}
</style>