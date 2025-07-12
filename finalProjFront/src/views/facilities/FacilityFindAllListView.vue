<template>
    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-4 fw-bold">可預約公設</h2>
            <div class="row g-4">
                <div class="col-md-4" v-for="(facility, index) in facilities" :key="facility.facilityId">
                    <div class="card h-100 shadow-sm border-0 feature-card animate-card"
                        :style="{ animationDelay: (index * 0.2) + 's' }">
                        <img :src="`${path}${facility.imageUrl}`" @error="e => e.target.src = noImage"
                            class="card-img-top" alt="公設圖片" style="object-fit: cover; height: 200px;" />
                        <div class="card-body">
                            <h5 class="card-title">{{ facility.facilityName }}</h5>
                            <p class="card-text">{{ facility.facilityDescription }}</p>
                            <p class="card-text text-muted">{{ facility.facilityLocation }}</p>
                            <p class="card-text text-primary">每小時收費：{{ facility.fee }} 點</p>
                        </div>
                        <div class="card-footer bg-transparent border-0 text-end">
                            <router-link
                                :to="{ name: 'ReservationFormView', params: { facilityId: facility.facilityId } }"
                                class="btn btn-outline-success btn-sm">
                                我要預約
                            </router-link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from '@/plugins/axios';
import noImage from '@/assets/images/facilities/no-image.jpg';



const path = import.meta.env.VITE_API_URL;


const facilities = ref([]);

onMounted(async () => {
    try {
        const response = await axios.get('/api/facilities/simple');
        facilities.value = response.data;
    } catch (error) {
        console.error('載入公設失敗', error);
    }
});
</script>

<style scoped>
/* 卡片外觀與初始樣式 */
.feature-card {
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
    /* 設定初始透明度為 0 */
    opacity: 0;
}

/* 滑鼠移入卡片效果 */
.feature-card:hover {
    background-color: #f8f9fa;
    transform: scale(1.02);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 卡片內容 */
.card-body {
    background-color: transparent;
    transition: background-color 1s ease;
}

/* 掉落式動畫效果 */
@keyframes dropDown {
    0% {
        transform: translateY(-30px);
        opacity: 0;
    }

    100% {
        transform: translateY(0);
        opacity: 1;
    }
}

/* 動畫 class，獨立處理進場 */
.animate-card {
    animation: dropDown 1.2s ease both;
}
</style>