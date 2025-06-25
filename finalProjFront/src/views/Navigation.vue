<template>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <span class="navbar-brand">Vue專案</span>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarNavDropdown">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <RouterLink class="nav-link active" aria-current="page" to="/">首頁</RouterLink>
                    </li>
                    <li class="nav-item">
                        <RouterLink class="nav-link" to="/secure/login">登入</RouterLink>
                    </li>
                    <li class="nav-item dropdown" v-if="userStore.email || true">
                        <span class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">功能</span>
                        <ul class="dropdown-menu">
                            <li>
                                <RouterLink class="dropdown-item" to="/pages/product">產品</RouterLink>
                            </li>
                            <li>
                                <RouterLink class="dropdown-item" to="/pages/park">停車場</RouterLink>
                            </li>
                            <li>
                                <RouterLink class="dropdown-item" to="/pages/park/initialize">停車場建置 (管理員)</RouterLink>
                            </li>
                            <li>
                                <RouterLink class="dropdown-item" to="/pages/park/rental-front">承租車位 (一般住戶)</RouterLink>
                            </li>
                            <li>
                                <RouterLink class="dropdown-item" to="/pages/park/rental-back">承租車位 (管理員)</RouterLink>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>
    
<script setup>
    import { RouterLink } from 'vue-router';
    import  useUserStore  from "@/stores/user";
    const userStore = useUserStore();
</script>
    
<style>
    
</style>