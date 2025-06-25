<template>
    <h1>停車場功能</h1>
    <div>
        <h2>承租社區車位</h2>
        <div>
            
        </div>
    </div>
<!-- 
    承租社區車位 -> 以時間區段、車種查詢可承租車位 -> 點選後進入新增承租頁面 (一般用戶) -> 管理員審核
    -> 搜尋自己的承租資訊 (一般用戶) -> 修改承租資訊 (一般用戶) -> 管理員審核
    -> 模糊搜尋所有承租資訊 (管理員) -> 修改承租資訊 (管理員) -> 2次確認
    臨時停車申請 -> 以時間區段、車種申請臨時停車 (一般住戶) -> 即時顯示是否overlapping -> 管理員審核
    -> 搜尋自己的臨時停車 (一般用戶) -> 修改臨時停車 (一般用戶) -> 管理員審核
    -> 模糊搜尋所有臨時停車 (管理員) -> 修改臨時停車 (管理員) -> 2次確認
    抽籤活動     -> 新增抽籤活動及抽籤車位 (管理員)
    -> 模糊查詢所有抽籤活動 -> 修改抽籤活動 (管理員) -> 2次確認
    -> 模糊查詢所有抽籤活動 -> 申請參與 (一般用戶) / 取消參與
    -> 搜尋自己參與的抽籤 (一般用戶) -> 修改臨時停車 (一般用戶) -> 管理員審核
    
    <button @click="testRead">查詢</button>
    <button @click="testCreate">新增</button>
    <button @click="testUpdate">修改</button>
    <button @click="testDelete">刪除</button>
    -->
</template>

<script setup>
import { ref } from 'vue'

import axios from '@/plugins/axios.js';


</script>

<style>
    
    </style>