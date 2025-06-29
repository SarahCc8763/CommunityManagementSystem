<template>
    <BannerImage :imageSrc="myImage" heading="XX管理區" subtext="為您提供ＯＯ服務, 以及ＸＸ服務" textAlign="left" />

    <FeatureCards :features="cards" />
</template>

<script setup>
import BannerImage from '@/components/forAll/BannerImage.vue'
import myImage from '@/assets/images/finance/happyFaces.jpg';
import FeatureCards from '@/components/forAll/FeatureCards.vue'

const cards = [
    {
        icon: '問ＧＰＴ適合的bootstrap icon',
        title: '第一個功能',
        description: '功能細節解說',
        link: '/你的路徑'
    },
    {
        icon: 'bi-？？',
        title: '第一個功能',
        description: '功能細節解說',
        link: '/你的路徑'
    },
    {
        icon: 'bi-？？',
        title: '第三個功能',
        description: '功能細節解說',
        link: '/你的路徑'
    },
    {
        icon: 'bi-question-circle-fill',
        title: '常見問題與支援',
        description: '找不到想要的資訊嗎? 點擊這裡為您服務',
        link: '/相應的ＦＡＱ頁面'
    },]
</script>

<style scoped></style>