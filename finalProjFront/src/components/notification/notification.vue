<template>
    <div class="notification-center">
        <h1>通知中心</h1>

        <div v-for="notice in notifications" :key="notice.id" class="notification-item"
            :class="{ unread: !notice.read }" @click="toggleRead(notice)">
            <h3>{{ notice.title }}</h3>
            <p>{{ notice.message }}</p>
            <span class="time">{{ notice.time }}</span>
        </div>

        <div v-if="!notifications.length" class="no-data">
            尚無通知
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const notifications = ref([
    { id: 1, title: '包裹已到達', message: '您的包裹已送達管理室，請儘快領取。', time: '2025-07-01 09:00', read: false },
    { id: 2, title: '社區公告', message: '7/5 社區將進行大樓外牆清潔，請住戶留意。', time: '2025-06-30 17:00', read: true },
    { id: 3, title: '車位預約提醒', message: '您已成功預約 7/2 車位使用，請準時停車。', time: '2025-06-29 12:30', read: true },
])

function toggleRead(notice) {
    notice.read = !notice.read
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
