<!-- 
 這是圖片Banner, 會有一張橫圖+中間白色文字動畫出現

 使用方法
 script:
 import BannerImage from '@/components/forAll/BannerImage.vue';
 import OO from '@/assets/images/你的圖片資料夾/檔名.jpg';

 template:
 
 <BannerImage :imageSrc="OO" heading="大標題" subtext="小內文" textAlign="left"/>
 
 
 **圖片是必傳, 大標小標可寫可不寫
 **可以自己改style
 範例:
 .hero-image-wrapper {
    
 max-height: 100px; //最高100px
 max-width: 30vw; //最寬佔螢幕30%
 
 }
 -->



<template>
    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img :src="imageSrc" alt="Hero Image" class="img-fluid hero-image" />
                <div :class="['hero-text', `text-${textAlign}`]">
                    <h2 class="text-white fw-bold">{{ heading }}</h2>
                    <p class="text-white">{{ subtext }}</p>
                </div>
            </div>
        </div>
    </section>
</template>

<!-- BannerImage.vue / HeroImageSection.vue -->
<script setup>
defineProps({
    imageSrc: String,
    heading: String,
    subtext: String,
    textAlign: {
        type: String,
        default: 'center' // 可選值: 'center', 'left', 'right'
    }
})
</script>



<style scoped>
.hero-image-wrapper {
    position: relative;
    overflow: hidden;
    max-height: 300px;
    max-width: 80vw;
    margin: 0 auto;
    border-radius: 12px;
}

.hero-image {
    width: 100%;
    height: auto;
    max-height: 500px;
    object-fit: cover;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.hero-text {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
    animation: fadeInUp 0.8s ease;
    width: 100%;
    padding: 0 2rem;
}

/* 各種對齊 */
.text-center {
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
}

.text-left {
    left: 5%;
    text-align: left;
}

.text-right {
    right: 5%;
    left: auto;
    text-align: right;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(-50%);
    }
}
</style>
