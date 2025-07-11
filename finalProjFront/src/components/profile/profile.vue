<template>
    <div class="profile-wrapper">
        <div class="profile-card">
            <div class="profile-left">
                <img :src="userStore.avatarUrl" alt="Avatar" class="avatar" />
                <!-- <img :src="profilePhoto" alt="Avatar" class="avatar" /> -->
                <h2>{{ userStore.name }}</h2>
                <p class="unit">門牌號：{{ }} </p>
                <p class="email">{{ userStore.username }}</p>
                <button @click="uploadPhoto">上傳大頭貼</button>
            </div>

            <div class="profile-right">
                <div class="info-card">
                    <h3>聯絡方式</h3>
                    <p>電話：{{ userStore.contactInfo }}</p>
                    <p>E-mail：{{ userStore.username }}</p>
                    <!-- <p>Line ID：{{ user.lineId }}</p> -->
                </div>

                <div class="info-card">
                    <h3>住戶資訊</h3>
                    <p>狀態：{{ userStore.state }}</p>
                    <p>緊急聯絡人：{{ userStore.emergencyContactName }}</p>
                    <p>關係：{{ userStore.emergencyContactRelation }}</p>
                    <p>緊急聯絡人電話：{{ userStore.emergencyContactPhone }}</p>
                </div>

                <div class="info-card">
                    <h3>帳號安全</h3>
                    <button @click="changePassword">修改密碼</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/UserStore'
import { useRouter } from 'vue-router'; 

const userStore = useUserStore();
const router = useRouter();
// const user = ref({
//     name: '王小明',
//     unit: 'A101',
//     email: 'xiaoming@example.com',
//     phone: '0912345678',
//     lineId: 'xiaomingline',
//     state: '啟用',
//     emergencyContact: '王媽媽',
//     emergencyContactNum: '0923456789',
//     emergencyRelation: '母親',
// })

// const profilePhoto = ref('https://png.pngtree.com/png-clipart/20201224/ourmid/pngtree-childrens-illustration-cute-boy-cute-hand-painted-q-version-character-original-png-image_2605661.jpg')

function uploadPhoto() {
    alert('上傳大頭貼功能開發中...')
}

function changePassword() {
    router.push({ name: 'resetPassword' })
}
</script>

<style scoped>
.profile-wrapper {
    display: flex;
    justify-content: center;
    padding: 40px;
    background: #f5f7fa;
    min-height: 100vh;
}

.profile-card {
    display: flex;
    flex-wrap: wrap;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    max-width: 1000px;
    width: 100%;
    overflow: hidden;
}

.profile-left {
    flex: 1 1 250px;
    background: #fafafa;
    text-align: center;
    padding: 40px 20px;
    border-right: 1px solid #eee;
}

.avatar {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 20px;
}

.profile-left h2 {
    margin: 10px 0;
}

.unit,
.email {
    color: #666;
    margin: 4px 0;
}

.profile-left button {
    margin-top: 20px;
    padding: 8px 16px;
    background: #4a90e2;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.profile-left button:hover {
    background: #357ab7;
}

.profile-right {
    flex: 2 1 500px;
    padding: 40px 30px;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.info-card {
    background: #fff;
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 20px;
}

.info-card h3 {
    margin-bottom: 10px;
}

.info-card button {
    padding: 8px 16px;
    background: #f36f45;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.info-card button:hover {
    background: #c9522b;
}

@media (max-width: 768px) {
    .profile-card {
        flex-direction: column;
    }

    .profile-left {
        border-right: none;
        border-bottom: 1px solid #eee;
    }
}
</style>
