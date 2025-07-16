<!-- PointTopupResultView.vue -->
<template>
    <section class="container py-5">
        <div class="card shadow-sm rounded-4 p-4 w-100 text-center">
            <h2 class="fw-bold mb-4">點數儲值結果</h2>

            <div v-if="isSuccess" class="alert alert-success">
                ✅ 儲值成功！
            </div>
            <div v-else class="alert alert-danger">
                ❌ 儲值失敗：{{ rtnMsg || '未知錯誤' }}
            </div>

            <div class="my-4">
                <p><strong>交易編號：</strong>{{ merchantTradeNo }}</p>
                <p><strong>金額：</strong>{{ tradeAmt }} 元</p>
                <p><strong>付款方式：</strong>{{ paymentType || '未提供' }}</p>
                <p><strong>訊息：</strong>{{ rtnMsg }}</p>
            </div>

            <div class="mt-4">
                <router-link to="/" class="btn btn-outline-primary me-3">回首頁</router-link>
                <router-link to="/points/history" class="btn btn-outline-success">查看點數紀錄</router-link>
            </div>
        </div>
    </section>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()

const rtnCode = route.query.RtnCode
const rtnMsg = route.query.RtnMsg
const merchantTradeNo = route.query.MerchantTradeNo
const tradeAmt = route.query.TradeAmt
const paymentType = route.query.PaymentType

const isSuccess = computed(() => rtnCode === '1')
</script>

<style scoped>
.alert {
    font-size: 1.25rem;
}
</style>
