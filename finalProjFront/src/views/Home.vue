<template>
    <h3>首頁</h3>
    <p v-if="userStore.email">歡迎 {{ userStore.email }} !</p>
</template>

<script setup>
// let user = sessionStorage.getItem("user");
import  useUserStore  from "@/stores/user";
const userStore = useUserStore();
</script>

<style></style>