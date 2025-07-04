<!-- 
 這是圖片Banner, 會有一張橫圖+中間白色文字動畫出現

 使用方法
 script:
 import BannerImage from '@/components/forAll/BannerImage.vue';
 import OO from '@/assets/images/你的圖片資料夾/檔名.jpg';

 template:
 
 <div class="w-60 position-relative" style="margin-left: calc(-50vw + 50%); width: 60vw;"> //這邊自己調整想要他多寬 或是選擇不加這個div
 <BannerImage :imageSrc="OO" heading="大標題" subtext="小內文" textAlign="left"/>
  </div>
 
 **圖片是必傳, 大標小標可寫可不寫
 **可以自己改style
 範例:
 .hero-image-wrapper {
    
 max-height: 100px; //最高100px
 max-width: 30vw; //最寬佔螢幕30%
 
 }
 **Hero是醒目橫標的常用詞, 好像是UI的慣例常用語
 -->



<template>
    <section class="hero-section py-5">

        <div class="hero-image-wrapper">
            <img :src="imageSrc" alt="Hero Image" class="img-fluid hero-image" />
            <div :class="['hero-text', `text-${textAlign}`]">
                <h2 class="text-white fw-bold">{{ heading }}</h2>
                <p class="text-white">{{ subtext }}</p>
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
    width: 100%;
    /* ✅ 佔滿整個父容器寬度 */
    max-width: 100vw;
    /* ✅ 最多等於螢幕寬度 */
    height: 300px;
    /* 或你原本的高度 */
    margin: 0 auto;
    border-radius: 0px;
    /* 或視需求保留圓角 */
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
    top: 70%;
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
