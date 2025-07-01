<template>
    <div class="container my-4">
        <div v-if="error" class="alert alert-danger" role="alert">
            {{ error }}
        </div>

        <div v-if="loading" class="text-center">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>

        <div v-else class="d-flex justify-content-center">
            <div class="row row-cols-1 g-4" style="width: 80%">
                <div class="col" v-for="feedback in feedbackList" :key="feedback.id">
                    <div class="card h-100 flex-row">
                        <img :src="getFirstImage(feedback)" class="img-fluid rounded-start" alt="Feedback Image"
                            style="width: 20%; height: auto; object-fit: contain" />
                        <div class="card-body d-flex flex-column">
                            <span class="badge mb-2" :class="'bg-' + getStatusVariant(feedback.status)"
                                style="width: 50%">
                                {{ feedback.status }}
                            </span>
                            <h5 class="card-title">{{ feedback.title }}</h5>
                            <p class="card-text">{{ feedback.description }}</p>

                            <div>
                                <strong>最新回覆：</strong>
                                <div v-if="feedback.replies && feedback.replies.length > 0">
                                    {{ feedback.replies[feedback.replies.length - 1].reply }}
                                </div>
                                <div v-else class="text-muted">尚無回覆</div>
                            </div>

                            <div class="d-flex gap-2 mt-2">
                                <button class="btn btn-outline-primary btn-sm" @click="toggleReplies(feedback)">
                                    {{ feedback.showReplies ? '隱藏回覆' : '顯示所有回覆' }}
                                </button>
                                <button class="btn btn-outline-secondary btn-sm" @click="openEditModal(feedback.id)">
                                    ✏️ 修改
                                </button>
                            </div>

                            <ul class="mt-3 list-unstyled" v-if="feedback.showReplies">
                                <li v-for="reply in feedback.replies" :key="reply.id" class="d-flex mb-3">
                                    <div class="me-2">
                                        <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center"
                                            style="width: 40px; height: 40px; font-size: 0.9rem">
                                            {{ reply.user?.name?.charAt(0) || '?' }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="fw-bold">{{ reply.user?.name || '未知使用者' }}</div>
                                        <div class="bg-light rounded p-2">{{ reply.reply }}</div>
                                        <div class="text-muted small">{{ formatDate(reply.repliedAt) }}</div>
                                    </div>
                                </li>
                            </ul>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 引入編輯 Modal 元件 -->
        <feedback-modal />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import noImage from '@/assets/images/feedback/noImage.jpg'
import FeedbackModal from '@/components/feedback/feedbackModal.vue'

const defaultImage = noImage
const feedbackList = ref([])
const loading = ref(false)
const error = ref(null)

const toggleReplies = (feedback) => {
    feedback.showReplies = !feedback.showReplies
}

const getFirstImage = (feedback) => {
    const image = feedback.attachments?.find((a) => a.mimeType?.startsWith('image/'))
    if (image && image.attachment) {
        return `data:${image.mimeType};base64,${image.attachment}`
    } else {
        return defaultImage
    }
}

const getStatusVariant = (status) => {
    switch (status) {
        case '待處理':
            return 'secondary'
        case '處理中':
            return 'warning'
        case '已完成':
            return 'success'
        default:
            return 'dark'
    }
}

const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleString()
}

const openEditModal = async (feedbackId) => {
    try {
        const res = await axios.get(`http://localhost:8080/api/feedback/${feedbackId}`)
        const feedback = res.data

        const modalEl = document.getElementById('feedbackModal')
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl)
        modalInstance.show()

        // 傳送完整 feedback 資料到 modal
        window.dispatchEvent(new CustomEvent('load-feedback-for-edit', { detail: feedback }))
    } catch (err) {
        console.error('載入意見資料失敗', err)
    }
}

const fetchData = () => {
    const userId = 1 // localStorage.getItem('userId')
    if (!userId) {
        error.value = '找不到使用者資訊，請重新登入。'
        return
    }

    loading.value = true
    axios
        .get(`http://localhost:8080/api/feedback/findbyuser/${userId}`)
        .then((res) => {
            feedbackList.value = res.data.map((f) => ({
                ...f,
                showReplies: false,
            }))
        })
        .catch((err) => {
            error.value = '無法載入資料，請稍後再試。'
            console.error(err)
        })
        .finally(() => {
            loading.value = false
        })
}

onMounted(() => {
    fetchData()
})
</script>

<style scoped>
ul {
    padding-left: 1.2rem;
}
</style>