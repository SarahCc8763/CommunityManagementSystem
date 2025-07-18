<template>
    <div class="notification-center">
        <h1>通知中心</h1>

        <div v-for="notice in notifications" :key="notice.unitsNotificationsId" class="notification-item"
            :class="{ unread: notice.isRead == 0 || notice.isRead === '0' }" @click="toggleRead(notice)">
            <h3>{{ notice.title }}</h3>
            <!-- <p>{{ notice.message }}</p> -->
        </div>

        <div v-if="!notifications.length" class="no-data">
            尚無通知
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, onBeforeUnmount } from 'vue'
import { useUserStore } from '@/stores/UserStore';
import axios from '@/plugins/axios';
import Swal from 'sweetalert2'

const userStore = useUserStore();
const path = import.meta.env.VITE_API_URL
const unitId = userStore.unitId
const notifications = ref([])
console.log('unitId = ' + unitId);
async function pollNotifications() {
    try {
        const res = await axios.get(`/notifications/unit/${unitId}`)
        console.log('📬 收到通知', res.data.data)
        // 寫入陣列並按未讀、已讀排序、顯示10筆
        notifications.value = res.data.data.sort((a,b)=>Number(a.isRead) - Number(b.isRead)).slice(0,10) 
    } catch (error) {
        console.error('❌ 輪詢失敗', error)
    }
}

let intervalId = null

onMounted(() => {
    pollNotifications()
    intervalId = setInterval(pollNotifications, 2000) // 每 2 秒輪詢一次
})

onBeforeUnmount(() => {
    clearInterval(intervalId)
})

// 點擊後：顯示 Swal，並呼叫後端更新 isRead
async function toggleRead(notice) {
    console.log('🔍 點擊通知 ID:', notice.unitsNotificationsId)

    await Swal.fire({
        icon: 'info',
        title: notice.title,
        text: notice.description,
        confirmButtonText: '知道了'
    })

    try {
        // 呼叫後端更新已讀
        await axios.put(`${path}/notifications/isRead/${notice.unitsNotificationsId}`)

        // 成功後，前端也標記已讀（或重新撈）
        notice.isRead = 1  // 或 true，看你的欄位怎麼回來
    } catch (error) {
        console.error('❌ 更新已讀失敗', error)
    }
}

</script>

<style scoped>
.notification-center {
  display: block;
  width: 100%;
  max-width: 800px;         /* 容器最大寬度 */
  min-width: 500px;         /* 避免太窄 */
  margin: 80px auto 40px auto; /* 離頁面頂端 100px 並置中 */
  padding: 3rem;
  background: #fefefe;
  border: 1px solid #ddd;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
}

.notification-center h1 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
  color: #222;
}

.notification-item {
  display: block;
  width: 75%;               /* 單卡寬度小一點 */
  margin: 0 auto 20px auto; /* 左右置中並有下間距 */
  background: #e0e0e0;      /* 已讀背景 */
  padding: 1rem 1.5rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  text-align: center;       /* 文字置中 */
}

.notification-item.unread {
  background: #f5f5f5;      /* 未讀背景 */
  opacity: 1;
}

.notification-item:not(.unread) {
  opacity: 0.7;
}

.notification-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

.notification-item h3 {
  margin: 0 0 8px 0;
  font-size: 1.8rem;
  color: #333;
}

.notification-item p {
  margin: 0;
  font-size: 1rem;
  color: #555;
}

.notification-item.unread h3,
.notification-item.unread p {
  color: #222;
}

.no-data {
  text-align: center;
  color: #888;
  margin-top: 2rem;
  font-size: 1.2rem;
}

</style>
