<template>

    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img :src="FeedbackBg" alt="Hero Image" class="img-fluid hero-image" />
                <div :class="['hero-text', 'text-left']">
                    <h2 class="fw-bold" style="color: #383838">回饋提交紀錄</h2>
                    <p cstyle="color: #5e5e5e">您可以在此查看您已提交的回饋歷史紀錄</p>
                </div>
            </div>
        </div>
    </section>



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
            <div class="accordion" id="feedbackAccordion" style="width: 90%;">
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
                                        <button v-if="feedback.status != '已結案'" class="btn btn-outline-secondary btn-sm"
                                            @click="openEditModal(feedback.id)">
                                            修改
                                        </button>
                                    </div>
                                    <!-- 評分區塊 -->
                                    <div class="mt-3">
                                        <div v-if="feedback.status == '已結案'" class="d-flex align-items-center gap-2">
                                            <span
                                                v-if="feedback.status == '已結案' && feedback.userRating == null">給予評分：</span>
                                            <span v-else>您已評分：</span>
                                            <span v-for="star in 5" :key="star" @click="setRating(feedback, star)"
                                                :style="{ cursor: feedback.status === '已結案' && feedback.userRating == null ? 'pointer' : 'default', fontSize: '24px', color: 'gold' }">
                                                <i
                                                    :class="star <= (feedback.userRating ?? feedback.tempRating) ? 'bi bi-star-fill' : 'bi bi-star'"></i>

                                            </span>
                                            <button v-if="feedback.status == '已結案' && feedback.userRating == null"
                                                class="btn btn-sm btn-primary ms-3"
                                                @click="submitRating(feedback)">送出評分</button>
                                        </div>
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
                                                <button class=" mx-2 rounded-circle send-btn"
                                                    @click="submitReply(feedback)"
                                                    style="border: 0 none;background-color: #fff;">
                                                    <img src="@/assets/images/feedback/sendIcon.png" alt="➤"
                                                        style="height: 35px;width: 35px;">
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
import FeedbackModal from '@/components/feedback/FeedbackModal.vue'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'


const userStore = useUserStore()
const userId = userStore.id || 0 // 假設當前使用者 id
const communityId = userStore.communityId || 0 // 假設當前社區 ID

const defaultImage = noImage
const feedbackList = ref([])
const loading = ref(false)
const error = ref(null)
const currentUserName = localStorage.getItem('userName') || '我'
const currentUserInitial = currentUserName.charAt(0)






import BannerImage from '@/components/forAll/BannerImage.vue';
import FeedbackBg from '@/assets/images/feedback/feedbackbg.jpg';

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
        //console.log(feedback);
        const modalEl = document.getElementById('feedbackModal')
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl)
        modalInstance.show()

        // 傳送完整 feedback 資料到 modal
        window.dispatchEvent(new CustomEvent('load-feedback-for-edit', { detail: feedback }))
    } catch (err) {
        console.error('載入意見資料失敗', err)
    }
}


// 設定暫存評分
const setRating = (feedback, star) => {
    if (!feedback || feedback.userRating != null || feedback.status !== '已結案') return
    feedback.tempRating = star
}

// 送出評分
const submitRating = async (feedback) => {
    if (!feedback.tempRating) {
        alert('請選擇星數再送出')
        return
    }

    try {
        const payload = {
            userRating: feedback.tempRating
        }
        const result = await Swal.fire({
            title: `確定送出${feedback.tempRating}顆星嗎？`,
            icon: 'info',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '確定'
        })
        if (result.isConfirmed) {
            const res = await axios.put(`http://localhost:8080/api/feedback/rating/${feedback.id}`, payload)

            if (res.data?.success) {
                // 更新本地畫面資料
                feedback.userRating = feedback.tempRating
                Swal.fire({
                    icon: 'success',
                    title: '送出成功',
                    text: '感謝您的評分！',
                    timer: 1500
                })

            } else {
                Swal.fire({
                    icon: 'error',
                    title: '送出失敗',
                    text: '請再試一次',
                    timer: 1500
                })
            }
        }
    } catch (err) {
        console.error('送出評分失敗', err)
        Swal.fire({
            icon: 'error',
            title: '送出失敗',
            text: '請再試一次',
            timer: 1500
        })
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
                newReplyText: '',
                tempRating: null
            }))
            //console.log(feedbackList.value);
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

.hero-image-wrapper {
    position: relative;
    overflow: hidden;
    max-height: 150px;
    max-width: 100vw;
    margin: 0 auto;
    border-radius: 12px;
}

.hero-image {
    width: 100%;
    height: auto;
    max-height: 500px;
    object-fit: cover;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.hero-text {
    position: absolute;
    top: 55%;
    transform: translateY(-50%);
    text-shadow: 0 2px 4px rgba(126, 126, 126, 0.6);
    animation: fadeInUp 0.8s ease;
    width: 100%;
    padding: 0 2rem;
}

.text-center {
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
}

.text-left {
    left: 5%;
    text-align: left;
}

.text-right {
    right: 5%;
    left: auto;
    text-align: right;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(-50%);
    }
}

.send-btn:hover {
    background-color: #e0f0ff;
    /* 背景變淡藍 */
    box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    /* 加陰影 */
    transition: all 0.3s ease;
}
</style>