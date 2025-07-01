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
            <div class="accordion w-75" id="feedbackAccordion">
                <div class="accordion-item" v-for="(feedback, index) in feedbackList" :key="feedback.id">
                    <h2 class="accordion-header" :id="'heading-' + index">
                        <button class="accordion-button collapsed w-100" type="button" data-bs-toggle="collapse"
                            :data-bs-target="'#collapse-' + index" aria-expanded="false"
                            :aria-controls="'collapse-' + index">

                            <div class="d-flex justify-content-between align-items-center w-100">
                                <!-- 左側：狀態 + 標題 -->
                                <div class="d-flex align-items-center">
                                    <span class="me-2 badge" :class="'bg-' + getStatusVariant(feedback.status)">
                                        {{ feedback.status }}
                                    </span>
                                    <strong class="fs-5">{{ feedback.title }}</strong>
                                </div>

                                <!-- 右側：日期靠右 -->
                                <small class="text-muted text-end">用戶提交時間：{{ formatDate(feedback.submittedAt) }}</small>
                            </div>
                        </button>


                    </h2>
                    <div :id="'collapse-' + index" class="accordion-collapse collapse"
                        :aria-labelledby="'heading-' + index" data-bs-parent="#feedbackAccordion">
                        <div class="accordion-body">
                            <div class="card h-100 flex-row">
                                <img :src="getFirstImage(feedback)" class="img-fluid rounded-start" alt="Feedback Image"
                                    style="width: 20%; height: auto; object-fit: contain" />
                                <div class="card-body d-flex flex-column">
                                    <h5 class="fw-bold">主旨：{{ feedback.title }}</h5>
                                    <p class="card-text">內容：{{ feedback.description }}</p>

                                    <div>
                                        <strong>最新進度：</strong>
                                        <div v-if="feedback.replies && feedback.replies.length > 0">
                                            {{ feedback.replies[feedback.replies.length - 1].reply }}
                                            <div class="lh-lg" style="font-size: 70%;">{{
                                                formatDateTime(feedback.replies[feedback.replies.length -
                                                    1].repliedAt) }}</div>
                                        </div>
                                        <div v-else class="text-muted">尚無回覆</div>
                                    </div>

                                    <div class="d-flex gap-2 mt-2">
                                        <button class="btn btn-outline-primary btn-sm" @click="toggleReplies(feedback)">
                                            {{ feedback.showReplies ? '隱藏回覆' : '顯示所有回覆' }}
                                        </button>
                                        <button class="btn btn-outline-secondary btn-sm"
                                            @click="openEditModal(feedback.id)">
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
                                                <div class="text-muted small">{{ formatDateTime(reply.repliedAt) }}
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                    <!-- 回覆輸入區塊 -->
                                    <div v-if="feedback.showReplies" class="mt-3 d-flex align-items-start">
                                        <!-- 左側：頭像圓圈（顯示登入者名稱縮寫） -->
                                        <div class="me-2">
                                            <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center"
                                                style="width: 40px; height: 40px; font-size: 0.9rem;">
                                                {{ currentUserInitial }}
                                            </div>
                                        </div>

                                        <!-- 右側：輸入框 + 送出按鈕 -->
                                        <div class="flex-grow-1">
                                            <div class="input-group">
                                                <input type="text" class="form-control rounded-pill px-3"
                                                    v-model="feedback.newReplyText" placeholder="留言……"
                                                    @keyup.enter="submitReply(feedback)">
                                                <button class="btn btn-sm btn-outline-light ms-2 rounded-circle"
                                                    @click="submitReply(feedback)">
                                                    <img src="@/assets/images/feedback/sendIcon.png" alt="➤"
                                                        style="height: 25px;width: 25px;">
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
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
const userId = Number(localStorage.getItem('userId')) || 1
const currentUserName = localStorage.getItem('userName') || '我'
const currentUserInitial = currentUserName.charAt(0)


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

const submitReply = async (feedback) => {
    const text = feedback.newReplyText?.trim()
    if (!text) return

    const payload = {
        user: { usersId: userId },
        reply: text,
        preFeedBackStatus: feedback.status, // 從 feedback 中取原本狀態
        newFeedBackStatus: null              // 使用者自行留言不變更狀態
    }

    try {
        const res = await axios.post(
            `http://localhost:8080/api/feedback/${feedback.id}/reply`,
            payload
        )

        if (res.data?.id) {
            const newReply = {
                ...res.data,
                user: { name: currentUserName }
            }
            feedback.replies = [...(feedback.replies || []), newReply]
            feedback.newReplyText = ''
        } else {
            alert('送出失敗')
        }
    } catch (err) {
        console.error('送出回覆失敗', err)
        alert('無法送出，請稍後再試')
    }
}

const getImageAttachments = (feedback) => {
    return feedback.attachments?.filter(a => a.mimeType?.startsWith('image/')) || []
}


const getStatusVariant = (status) => {
    switch (status) {
        case '待處理':
            return 'danger'
        case '處理中':
            return 'warning'
        case '已結案':
            return 'secondary'
        default:
            return 'dark'
    }
}

const formatDate = (datetime) => {
    if (!datetime) return ''
    const date = new Date(datetime)
    return date.toLocaleDateString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    })
}
const formatDateTime = (datetime) => {
    if (!datetime) return ''
    const date = new Date(datetime)
    return date.toLocaleDateString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    })
}


const openEditModal = async (feedbackId) => {
    try {
        const res = await axios.get(`http://localhost:8080/api/feedback/${feedbackId}`)
        const feedback = res.data
        console.log(feedback);
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
                newReplyText: ''
            }))
            console.log(feedbackList.value);
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