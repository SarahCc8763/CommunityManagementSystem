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
console.log('111' + unitId);
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
    max-width: 800px;
    margin: 40px auto;
    padding: 20px;
}

.notification-center h1 {
    margin-bottom: 20px;
}

.notification-item {
    background: #e0e0e0;
    /* 已讀背景 */
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 15px;
    cursor: pointer;
    transition: background 0.3s, opacity 0.3s;
}

.notification-item h3,
.notification-item p {
    margin: 0 0 8px 0;
    color: #555;
    /* 已讀字體深灰 */
}

.notification-item .time {
    font-size: 12px;
    color: #888;
}

.notification-item.unread {
    background: #f5f5f5;
    /* 未讀背景 */
    opacity: 1;
    /* 未讀完全不透明 */
}

.notification-item.unread h3,
.notification-item.unread p {
    color: #222;
    /* 未讀文字較黑 */
}

.notification-item:not(.unread) {
    opacity: 0.7;
    /* 已讀降低透明度 */
}

.notification-item:hover {
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}
</style>
