<!-- PointTopupView.vue -->
<template>
    <section class="container py-5">
        <div class="card shadow-sm rounded-4 p-4 w-100">
            <h2 class="mb-4 fw-bold text-center">點數儲值</h2>

            <!-- 帳戶資訊區塊 -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <p><strong>使用者：</strong>{{ userStore.name }}</p>
                </div>
                <div class="col-md-6">
                    <p><strong>儲值單位：</strong>{{ `${facilitiesStore.unit}-${facilitiesStore.floor}` }}</p>
                </div>
                <div class="col-md-6">
                    <p>
                        <strong>剩餘點數：</strong>
                        <span class="fw-bold text-primary">{{ facilitiesStore.totalBalance }}</span> 點
                    </p>
                </div>
                <div class="col-md-6">
                    <p>
                        <strong>點數到期日：</strong>
                        <span class="fw-bold text-danger">{{ formatDate(facilitiesStore.expiredAt) }}</span>
                    </p>
                </div>
            </div>

            <!-- 儲值選單 -->
            <div class="mb-4">
                <label for="amount" class="form-label fw-bold">選擇儲值金額：</label>
                <select v-model="amount" class="form-select">
                    <option v-for="option in amountOptions" :key="option" :value="option">
                        {{ option }} 元（可得 {{ option }} 點）
                    </option>
                </select>
            </div>

            <!-- 提交按鈕 -->
            <div class="text-center">
                <button class="btn btn-success px-4" @click="submitTopup">
                    立即付款
                </button>
            </div>
            <div id="ecpayFormContainer" style="display: none;"></div>
        </div>
    </section>
</template>

<script setup>
import { ref } from 'vue'
import { format } from 'date-fns'
import { useFacilitiesStore } from '@/stores/FacilitiesStore'
import { useUserStore } from '@/stores/UserStore'
import axios from '@/plugins/axios'

const facilitiesStore = useFacilitiesStore()
const userStore = useUserStore()

const amount = ref(100)
const amountOptions = [50, 100, 200]

const formatDate = (dateStr) => {
    return format(new Date(dateStr), 'yyyy-MM-dd')
}

const submitTopup = async () => {
    try {
        const payload = {
            amount: amount.value,
            accountId: facilitiesStore.accountId,
            unitId: facilitiesStore.unitId,
            description: `住戶${userStore.name} 儲值 ${amount.value} 點`
        }

        const res = await axios.post('/api/point-topup/create-order', payload)

        const formContainer = document.getElementById('ecpayFormContainer')
        formContainer.innerHTML = res.data.form

        // 手動觸發 form.submit()
        const form = document.getElementById('ecpay-form')
        if (form) {
            form.submit()
        } else {
            console.error('找不到 ecpay-form')
        }

    } catch (err) {
        console.error('儲值失敗', err)
        alert('儲值失敗，請稍後再試')
    }
}
</script>

<style scoped>
.card p {
    margin-bottom: 0.5rem;
}
</style>
