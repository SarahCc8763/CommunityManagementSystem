<template>
    <div class="container py-4 ">
        <BannerImage :imageSrc="OO" heading="回饋管理" subtext="在此回應或處理用戶的回饋意見" textAlign="left" />
        <div class="d-flex justify-content-center align-items-center gap-3 flex-wrap">
            <div class="d-flex align-items-center">
                <span class="me-2">分類篩選：</span>
                <select v-model="selectedCategory" class="form-select w-auto">
                    <option value="">全部類別</option>
                    <option v-for="(cat, key) in categoryList" :key="key" :value="cat">{{ cat }}</option>
                </select>
            </div>

            <div class="d-flex align-items-center">
                <span class="me-2">處理狀態：</span>
                <select v-model="selectedStatus" class="form-select w-auto">
                    <option value="">全部</option>
                    <option value="待處理">待處理</option>
                    <option value="處理中">處理中</option>
                    <option value="確認中">確認中</option>
                    <option value="已結案">已結案</option>
                </select>
            </div>
        </div>

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
                <div class="accordion bg-darkx" id="feedbackAccordion" style="width: 90%;">
                    <div class="accordion-item bg-darkx" v-for="(feedback, index) in filteredFeedbackList"
                        :key="feedback.id">
                        <h2 class="accordion-header" :id="'heading-' + index">
                            <button class="accordion-button collapsed w-100 bg-darkx text-light" type="button"
                                data-bs-toggle="collapse" :data-bs-target="'#collapse-' + index" aria-expanded="false"
                                :aria-controls="'collapse-' + index">

                                <div class="d-flex justify-content-between align-items-center w-100">

                                    <!-- 左側：分類 + 標題 -->
                                    <div class="d-flex align-items-center">
                                        <div class="badge fw-normal"
                                            :class="getCategoryBadgeClass(feedback.category?.id)"
                                            style="font-size: 95%;">
                                            {{ feedback.category?.name }}
                                        </div>

                                        <div class=" mx-2">
                                            <span class="fs-5"> {{ feedback.title }}</span>
                                        </div>
                                    </div>

                                    <!-- 右側：狀態 + 使用者/時間（左右排列） -->
                                    <div class="d-flex align-items-center ms-auto gap-3">
                                        <!-- 狀態 Badge -->
                                        <span class="badge" :class="'bg-' + getStatusVariant(feedback.status)"
                                            style="font-size: 90%;">
                                            {{ feedback.status }}
                                        </span>

                                        <!-- 使用者與時間（上下排列） -->
                                        <div class="text-start small " style="color: lightgray;">
                                            <div>反映人：{{ feedback.frontEndData.userName }}</div>
                                            <div>提交時間：{{ formatDate(feedback.submittedAt) }}</div>
                                        </div>
                                    </div>

                                </div>
                            </button>




                        </h2>
                        <div :id="'collapse-' + index" class="accordion-collapse collapse bg-darkx"
                            :aria-labelledby="'heading-' + index" data-bs-parent="#feedbackAccordion">
                            <div class="accordion-body bg-darky">
                                <div class="card h-100 flex-row bg-darkx">
                                    <img :src="getFirstImage(feedback)" class="img-fluid rounded-start"
                                        alt="Feedback Image" style="width: 20%; height: auto; object-fit: contain" />
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="fw-normal">主旨：span{{ feedback.title }}</h5>
                                        <p class="card-text mx-2">內容：{{ feedback.description }}</p>

                                        <div>
                                            <h6>最新進度：</h6>
                                            <div v-if="feedback.replies && feedback.replies.length > 0" class="mx-2">
                                                {{ feedback.replies[feedback.replies.length - 1].reply }}
                                                <div class="lh-lg" style="font-size: 70%;">{{
                                                    formatDateTime(feedback.replies[feedback.replies.length -
                                                        1].repliedAt) }}</div>
                                            </div>
                                            <div v-else>尚無回覆</div>
                                        </div>
                                        <!-- 附件 -->
                                        <div v-if="feedback.attachments?.length" class="mt-4">
                                            <strong>附件：</strong>
                                            <ul>
                                                <li v-for="file in feedback.attachments" :key="file.id">
                                                    <a :href="'data:' + file.mimeType + ';base64,' + file.attachment"
                                                        :download="file.fileName"
                                                        class="text-info text-decoration-underline" target="_blank">
                                                        {{ file.fileName }}
                                                    </a>
                                                </li>
                                            </ul>

                                        </div>
                                        <div class="d-flex gap-2 mt-2">
                                            <button class="btn btn-outline-primary btn-sm"
                                                @click="toggleReplies(feedback)">
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
                                                    <div class="bg-darky rounded p-2">{{ reply.reply }}</div>
                                                    <div class="text-light small">{{ formatDateTime(reply.repliedAt) }}
                                                    </div>
                                                </div>
                                                <button type="button" class="btn btn-outline-danger btn-sm ms-auto"
                                                    @click="deleteReply(reply.id)">刪除</button>
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
            <FeedbackManageModal @updated="fetchData" :selectedFeedback="selectedFeedback" />

        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import noImage from '@/assets/images/feedback/noImage.jpg'
import FeedbackManageModal from '@/components/feedback/FeedbackManageModal.vue'
import Swal from 'sweetalert2'


const defaultImage = noImage
const feedbackList = ref([])
const categoryList = ref([])
const loading = ref(false)
const error = ref(null)
const userId = Number(localStorage.getItem('userId')) || 1
const currentUserName = localStorage.getItem('userName') || '我'
const currentUserInitial = currentUserName.charAt(0)
const communityId = localStorage.getItem('communityId') || 1
const selectedStatus = ref('')
const selectedCategory = ref('')
const selectedFeedback = ref(null)


import BannerImage from '@/components/forAll/BannerImage.vue';
import OO from '@/assets/images/main/adminBanner.jpg';


const filteredFeedbackList = computed(() => {
    return feedbackList.value.filter(fb => {
        const matchStatus = !selectedStatus.value || fb.status == selectedStatus.value
        const matchCategory = !selectedCategory.value || fb.category?.name == selectedCategory.value
        return matchStatus && matchCategory
    })
})

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

const badgeColors = [
    'bg-primary',
    'bg-success',
    'bg-danger',
    'bg-secondary',
]

const getCategoryBadgeClass = (categoryId) => {
    if (!categoryId) return 'bg-secondary'
    const index = categoryId % badgeColors.length
    return badgeColors[index]
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

        selectedFeedback.value = feedback.list[0]
        console.log(selectedFeedback.value);

    } catch (err) {
        console.error('載入意見資料失敗', err)
    }
}

// ✅ 刪除留言
const deleteReply = async (replyId) => {
    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        text: '刪除後將無法恢復',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定刪除',
        cancelButtonText: '取消'
    });

    if (!result.isConfirmed) return;

    try {
        const res = await axios.delete(`http://localhost:8080/api/feedback/reply/${replyId}`);

        if (res.data?.result !== '刪除成功') {
            throw new Error('伺服器回應非預期');
        }

        // ✅ 找到有這筆回覆的 feedback 並移除 reply
        const parentFeedback = feedbackList.value.find(fb =>
            fb.replies?.some(reply => reply.id === replyId)
        );
        if (parentFeedback) {
            parentFeedback.replies = parentFeedback.replies.filter(reply => reply.id !== replyId);
        }

        Swal.fire({
            icon: 'success',
            title: '刪除成功',
            showConfirmButton: false,
            timer: 1500
        });

    } catch (e) {
        console.error('刪除發生錯誤', e);
        Swal.fire({
            icon: 'error',
            title: '刪除失敗',
            text: '請稍後再試'
        });
    }
};




const fetchData = async () => {
    const userId = 1 // localStorage.getItem('userId')
    if (!userId) {
        error.value = '找不到使用者資訊，請重新登入。'
        return
    }

    loading.value = true
    await axios
        .get(`http://localhost:8080/api/feedback/community/${communityId}`)
        .then((res) => {
            feedbackList.value = res.data.list.map((f) => ({
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

    axios.get(`http://localhost:8080/api/feedback/${communityId}/category`)
        .then((res) => {
            categoryList.value = res.data.data
            console.log(res);
            console.log(categoryList.value);
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

.bg-darkx {
    background-color: #2A3A56;
    color: #ffffff;
}

.bg-darky {
    background-color: #4a638d;
    color: #ffffff;
}
</style>