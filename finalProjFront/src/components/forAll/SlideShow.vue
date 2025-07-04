<!-- 
 這是圖片連播器,依照需要可以在上面顯示文字

 使用方法


 script:
    import SlideShow from '@/components/forAll/SlideShow.vue';
    const O圖片陣列O = [
        new URL('@/assets/images/你的圖片資料夾/圖片1.jpg', import.meta.url).href,
        new URL('@/assets/images/你的圖片資料夾/圖片2.jpg', import.meta.url).href,
        ....
    ]
    const X文字陣列X = ['圖片1介紹', '圖片2介紹' ...]



 template:
       <SlideShow :images="O圖片陣列O" :captions="X文字陣列X" carousel-id="自取名字A"/>
       <SlideShow :images="O圖片陣列O" :captions="X文字陣列X" carousel-id="自取名字B"/>

 **carousel-id 可寫可不寫,他有default名字 若是一個頁面要出現兩個或以上則一定要寫
 **圖片是必傳, 圖片介紹可寫可不寫
 **可以自己改style

 範例:
 .hero-image-wrapper {
   
    max-height: 100px; //最高100px
    max-width: 30vw; //最寬佔螢幕30%
  
}
-->






<!-- Carousel.vue -->
<template>
    <div class="mt-5 position-relative z-0">
        <div :id="carouselId" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
            <!-- 指示器 -->
            <div class="carousel-indicators">
                <button v-for="(image, index) in images" :key="index" type="button" :data-bs-target="'#' + carouselId"
                    :data-bs-slide-to="index" :class="{ active: index === 0 }" :aria-label="'Slide ' + (index + 1)" />
            </div>

            <!-- 圖片區塊 -->
            <div class="carousel-inner">
                <div v-for="(image, index) in images" :key="index" :class="['carousel-item', { active: index === 0 }]">
                    <img class="d-block w-100 carousel-image" :src="image" alt="Slideshow image" />

                    <!-- 對應的文字 -->
                    <div v-if="captions && captions[index]" class="carousel-caption">
                        {{ captions[index] }}
                    </div>
                </div>
            </div>

            <!-- 左右控制箭頭 -->
            <button class="carousel-control-prev" type="button" :data-bs-target="'#' + carouselId" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>

            <button class="carousel-control-next" type="button" :data-bs-target="'#' + carouselId" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
    </div>
</template>


<script setup>
defineProps({
    images: {
        type: Array,
        required: true
    },
    captions: {
        type: Array,
        default: () => []
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
}

.carousel-image {
    width: 100%;
    height: 80vh;
    object-fit: cover;
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
</style>