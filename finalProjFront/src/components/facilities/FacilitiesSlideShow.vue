<template>
    <div :id="carouselId" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3500">
        <!-- 指示器 -->
        <div class="carousel-indicators">
            <button v-for="(item, index) in items" :key="index" type="button" :data-bs-target="'#' + carouselId"
                :data-bs-slide-to="index" :class="{ active: index === 0 }" :aria-label="item.title" />
        </div>

        <!-- 輪播內容 -->
        <div class="carousel-inner">
            <router-link v-for="(item, index) in items" :key="index" :to="item.link"
                :class="['carousel-item', { active: index === 0 }]" class="carousel-link-wrapper">
                <img class="d-block w-100 carousel-image" :src="item.image" :alt="item.title" />
                <div class="carousel-caption">
                    <h1>{{ item.title }}</h1>
                    <p>{{ item.subtitle }}</p>
                </div>
            </router-link>
        </div>

        <!-- 控制按鈕-->
        <button class="carousel-control-prev" type="button" :data-bs-target="'#' + carouselId" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" :data-bs-target="'#' + carouselId" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</template>


<script setup>
defineProps({
    items: {
        type: Array,
        required: true
    },
    carouselId: {
        type: String,
        default: 'carousel1'
    }
})

</script>

<style scoped>
.carousel-inner {
    width: 100%;
    margin: 0;
    background-color: transparent;
}

.carousel-item {
    height: 400px;
    overflow: hidden;
    position: relative;
}


.carousel-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
    border-radius: 16px;
}

.carousel {
    background: transparent;
}


/* 文字動畫 */
.carousel-caption {
    position: absolute;
    top: 60%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 10;
    color: white;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
    font-size: 2rem;
    font-weight: bold;
    opacity: 0;
    transition: opacity 0.8s ease, transform 0.8s ease;
    text-align: center;
    padding: 0 1rem;
    width: 100%;
    max-width: 90vw;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.carousel-item.active .carousel-caption {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1.05);
    background-color: transparent;
}

/* 箭頭 */
.carousel-indicators {
    list-style: none;
    margin-bottom: 0;

}

.carousel-indicators button {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    margin: 0 10px;
    background-color: #8b8a87;
    border: none;
    opacity: 0.7;
}

.carousel-control-prev {
    width: auto;
    left: 3%;
}


.carousel-control-next {
    width: auto;
    right: 3%;
}

.carousel-indicators .active {
    background-color: #46433e;
    opacity: 1;
}

.carousel-control-next i,
.carousel-control-prev i {
    color: #575351;
    font-size: 2.8em;
}

/* 修改左右箭頭點擊區域 */
.carousel-control-prev,
.carousel-control-next {
    /* 原本是 auto，這裡擴大感應區域 */
    width: 10%;
    height: 100%;
    top: 0;
    bottom: 0;
    opacity: 1;
    z-index: 10;
}

.carousel-control-prev-icon {
    margin-left: -70%;
    /* 負值讓它靠更左邊 */
    /* background-color: rgba(0, 0, 0, 0.5); */
    border-radius: 50%;
    padding: 20px;
    filter: brightness(0.7);
    /* 初始暗一點 */
    background-size: 100% 100%;
}

.carousel-control-next-icon {
    margin-right: -70%;
    /* 負值讓它靠更右邊 */
    /* background-color: rgba(0, 0, 0, 0.5); */
    border-radius: 50%;
    padding: 20px;
    filter: brightness(0.7);
    /* 初始暗一點 */
    background-size: 100% 100%;
}

/* hover 時讓箭頭更明顯 */
.carousel-control-prev:hover .carousel-control-prev-icon,
.carousel-control-next:hover .carousel-control-next-icon {
    filter: brightness(1.8);
    /* 滑鼠移入變亮 */
}

.carousel-link-wrapper:hover .carousel-image {
    filter: brightness(0.9);
}

.carousel-link-wrapper {
    transition: transform 0.3s ease;
}

.carousel-link-wrapper:hover {
    transform: scale(1.01);
}
</style>