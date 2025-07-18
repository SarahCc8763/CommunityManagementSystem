<template>

    <div class="d-flex justify-content-center">

        <main>
            <BannerImage :imageSrc="OO" heading="財務管理區" subtext="快速查帳、繳費與明細查詢，提醒您 : 別忘了定期查看帳單與費用明細" textAlign="left" />
            <FeatureCards :features="cards" />

        </main>
    </div>
</template>

<script setup>
import BannerImage from '@/components/forAll/BannerImage.vue'
import OO from '@/assets/images/finance/happyFaces.jpg';
import FeatureCards from '@/components/forAll/FeatureCards.vue'

const cards = [
    {
        icon: 'bi-receipt',
        title: '待繳帳單',
        description: '檢視尚未繳交的費用並選擇繳費方式',
        link: '/finance/invoice'
    },
    {
        icon: 'bi-journal-text',
        title: '繳費紀錄',
        description: '查看過往繳費明細與下載收據',
        link: '/finance/receipt/my'
    },
    {
        icon: 'bi-bar-chart-line',
        title: '財務報表',
        description: '查看大樓每月的收支總覽與統計',
        link: '/finance/report'
    },
    {
        icon: 'bi-question-circle-fill',
        title: '常見問題與支援',
        description: '找不到想要的資訊嗎? 點擊這裡為您服務',
        link: '/package'
    },]



</script>

<style scoped></style>
