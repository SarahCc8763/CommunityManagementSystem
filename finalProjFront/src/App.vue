<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HeaderAll from './components/forAll/HeaderAll.vue';
import Home from './views/Home.vue';
import BeforeLogIn from './views/BeforeLogIn.vue';
import BannerImage from './components/forAll/BannerImage.vue';
import happyFaces from '@/assets/images/finance/happyFaces.jpg';
import Finance from './components/finance/Finance.vue';
</script>

<template>
  <div id="app">
    <HeaderAll />
    <!-- <BeforeLogIn /> -->



    <!-- <Home /> -->
    <!-- <RouterView /> -->


    <Finance />


  </div>
</template>

<style scoped></style>
