<template>

    <section class="hero-section py-5">
        <div class="container">
            <div class="hero-image-wrapper">
                <img :src="FeedbackBg" alt="Hero Image" class="img-fluid hero-image" />
                <div :class="['hero-text', 'text-left']">
                    <h2 class="fw-bold" style="color: #383838">回饋提交紀錄</h2>
                    <p style="color: #5e5e5e">您可以在此查看您已提交的回饋歷史紀錄</p>
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
        <div v-if="feedbackList.length === 0 && !loading" class="d-flex justify-content-center">
            <p>您尚未提交任何回饋。</p>
        </div>

        <div v-else class="d-flex justify-content-center">
            <div class="accordion" id="feedbackAccordion" style="width: 90%;">
                <div class="accordion-item" v-for="(feedback, index) in feedbackList" :key="feedback.id">
                    <h2 class="accordion-header" :id="'heading-' + index">
                        <button class="accordion-button w-100" type="button"
                            :class="{ 'collapsed': !feedback.isExpanded }" @click="toggleAccordion(feedback)">

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
                        :class="{ 'show': feedback.isExpanded }" :aria-labelledby="'heading-' + index">

                        <div class="accordion-body">
                            <div class="card h-100 flex-row">
                                <img :src="getFirstImage(feedback)" class="img-fluid rounded-start" alt="Feedback Image"
                                    style="width: 20%; height: auto; object-fit: contain" />
                                <div class="card-body d-flex flex-row justify-content-between gap-3">
                                    <div class="flex-grow-1">
                                        <h5 class="fw-bold">主旨：{{ feedback.title }}</h5>
                                        <p class="card-text">內容：{{ feedback.description }}</p>
                                        <!-- 附件 -->
                                        <div v-if="feedback.attachments?.length" class="mt-4">
                                            <strong>附件：</strong>
                                            <ul>
                                                <li v-for="file in feedback.attachments" :key="file.id"
                                                    class="d-flex align-items-center gap-2 mb-2">．
                                                    <!-- 檔案下載連結 -->
                                                    <a :href="'data:' + file.mimeType + ';base64,' + file.attachment"
                                                        :download="file.fileName"
                                                        class="text-dark text-decoration-underline" target="_blank">
                                                        {{ file.fileName }}
                                                    </a>

                                                    <!-- 如果是圖片，加一個「放大」按鈕 -->
                                                    <button v-if="file.mimeType?.startsWith('image/')"
                                                        class="btn btn-outline-light btn-sm btn-expand"
                                                        @click="openImageModal('data:' + file.mimeType + ';base64,' + file.attachment)">
                                                        <i class="bi bi-arrows-angle-expand"></i>
                                                    </button>
                                                </li>
                                            </ul>
                                        </div>

                                        <br>
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
                                            <button class="btn btn-outline-primary btn-sm"
                                                @click="toggleReplies(feedback)">
                                                {{ feedback.showReplies ? '隱藏回覆' : '顯示所有回覆' }}
                                            </button>
                                            <button v-if="feedback.status != '已結案'"
                                                class="btn btn-outline-secondary btn-sm"
                                                @click="openEditModal(feedback.id)">
                                                修改
                                            </button>

                                        </div>
                                        <!-- 評分區塊 -->
                                        <div class="mt-3">
                                            <div v-if="feedback.status == '已結案'"
                                                class="d-flex align-items-center gap-2">
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
                                                        {{ reply.userName?.charAt(0) || '?' }}
                                                    </div>
                                                </div>
                                                <div>
                                                    <div class="fw-bold">{{ reply.userName || '未知使用者' }}</div>
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
                                    <div class="d-flex flex-column align-items-center" style="min-width: 180px;">
                                        <button v-if="feedback.status != '已結案'" class=" btn btn-delete btn-sm"
                                            @click="deleteFeedback(feedback.id)">
                                            我要撤銷此案
                                        </button>
                                        <FeedbackProgress :feedback="feedback"
                                            @show-history-detail="showFeedbackHistoryDetail" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 點擊圖片後的放大 Modal -->
        <!-- 改寫為純 Vue Modal，不依賴 Bootstrap JS -->
        <div v-if="showImageModal" class="custom-image-modal">
            <!-- 背景可點擊關閉 -->
            <div class="modal-backdrop" @click="showImageModal = false"></div>

            <!-- 圖片容器 -->
            <div class="modal-content-box">
                <button class="btn-close btn-close-white position-absolute top-0 end-0 m-3"
                    @click="showImageModal = false"></button>
                <img :src="enlargedImageSrc" class="img-fluid" style="max-height: 80vh;" />
            </div>
        </div>

        <!-- 引入編輯 Modal 元件 -->
        <feedback-modal />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import noImage from '@/assets/images/feedback/noImage.jpg'
import FeedbackModal from '@/components/feedback/FeedbackModal.vue'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'
import FeedbackProgress from '@/components/feedback/FeedbackProgress.vue'
const showImageModal = ref(false)
const enlargedImageSrc = ref('')

function openImageModal(imageBase64) {
    enlargedImageSrc.value = imageBase64
    console.log(enlargedImageSrc.value);
    showImageModal.value = true
}

const userStore = useUserStore()
const userId = userStore.userId || 0 // 假設當前使用者 id
const communityId = userStore.communityId || 0 // 假設當前社區 ID

const defaultImage = noImage
const feedbackList = ref([])
const loading = ref(false)
const error = ref(null)
const currentUserName = localStorage.getItem('userName') || '我'
const currentUserInitial = currentUserName.charAt(0)
const historyData = ref([])






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
    console.log(payload);
    try {
        const res = await axios.post(
            `/api/feedback/${feedback.id}/reply`,
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
        //console.error('送出回覆失敗', err)
        alert('無法送出，請稍後再試')
    }
}
const showFeedbackHistoryDetail = async (feedback, stepKey) => {
    try {
        if (stepKey === '待處理') {
            Swal.fire({
                icon: 'info',
                title: `「建立回饋」➜「待處理」`,
                html: `
                <div class="text-start " style="margin:0 15%">
                建立時間：${formatDateTime(feedback.submittedAt)}
                <br>建立人員：${feedback.frontEndData.userName}</div>`
            });
            return;
        }
        // 重新發送 API 請求獲取完整的歷史記錄
        const res = await axios.get(`/api/feedback/history/${feedback.id}`);
        // console.log(res.data);
        const historyArray = res.data.filter((item) => item.newStatus === stepKey);
        historyData.value = historyArray[historyArray.length - 1];
        // console.log(historyData.value);
        Swal.fire({
            icon: 'info',
            title: `「${historyData.value.oldStatus}」➜「${historyData.value.newStatus}」`,
            html: `
            <div class="text-start " style="margin:0 15%">
            狀態變更時間：${formatDateTime(historyData.value.changedAt)}<br>變更人員：${historyData.value.changedByUserName}
            </div>`,
            showConfirmButton: true,
            confirmButtonText: '確定'
        });


    } catch (err) {
        // //console.error('載入意見回饋歷史失敗', err);
        Swal.fire({
            icon: 'error',
            title: '載入失敗',
            text: '處理歷程目前無法載入。'
        });
    }
};
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
        const res = await axios.get(`/api/feedback/${feedbackId}`)
        const feedback = res.data
        //console.log(feedback);
        const modalEl = document.getElementById('feedbackModal')
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl)
        modalInstance.show()

        // 傳送完整 feedback 資料到 modal
        window.dispatchEvent(new CustomEvent('load-feedback-for-edit', { detail: feedback }))
    } catch (err) {
        //console.error('載入意見資料失敗', err)
    }
}


// delete
const deleteFeedback = async (feedbackId) => {
    const confirm = await Swal.fire({
        title: '確定要撤銷嗎？',
        text: '撤銷後將無法恢復。',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    })
    if (!confirm.isConfirmed) {
        return
    }
    try {
        console.log(111);
        const res = await axios.delete(`/api/feedback/${feedbackId}`)
        console.log(res);
        if (res.data?.success) {
            Swal.fire({
                icon: 'success',
                title: '刪除成功',
                text: '意見回饋已刪除',
                timer: 1500
            })
            // 刪除成功後，重新取得資料
            await fetchData()
        } else {
            Swal.fire({
                icon: 'error',
                title: res.data?.result || '刪除失敗',
                text: '請稍後再試',
                timer: 1500
            })
        }
    } catch (err) {
        // console.error('刪除意見回饋失敗', err)
        Swal.fire({
            icon: 'error',
            title: err.data?.result || '刪除失敗',
        })
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
            confirmButtonText: '確定',
            cancelButtonText: '取消'

        })
        if (result.isConfirmed) {
            const res = await axios.put(`/api/feedback/rating/${feedback.id}`, payload)

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
        //console.error('送出評分失敗', err)
        Swal.fire({
            icon: 'error',
            title: '送出失敗',
            text: '請再試一次',
            timer: 1500
        })
    }
}


const fetchData = () => {
    if (!userId) {
        error.value = '找不到使用者資訊，請重新登入。'
        return
    }

    loading.value = true
    axios
        .get(`/api/feedback/findbyuser/${userId}`)
        .then((res) => {
            feedbackList.value = res.data.map((f) => ({
                ...f,
                isExpanded: false,
                showReplies: false,
                newReplyText: '',
                tempRating: null
            })).sort((a, b) => {
                if (a.status === b.status) {
                    return new Date(b.submittedAt) - new Date(a.submittedAt) // 狀態相同，按建立時間新到舊排序
                }
                return a.status === '已結案' ? 1 : -1 // 已結案排在後面
            })

        })
        .catch((err) => {
            error.value = '無法載入資料，請稍後再試。'
            //console.error(err)
        })
        .finally(() => {
            loading.value = false
        })
}
const toggleAccordion = (clickedFeedback) => {
    const wasExpanded = clickedFeedback.isExpanded;

    // Close all other items
    feedbackList.value.forEach(item => {
        item.isExpanded = false;
    });

    // Toggle the clicked item
    if (!wasExpanded) {
        clickedFeedback.isExpanded = true;
    }
};
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

:deep(.feedback-progress-container) {
    padding: 15px;
    background-color: #f2f4f8;
    border-radius: 8px;
    color: #333333;
}

:deep(.progress-title) {
    margin-bottom: 15px;
    color: #444444;
}

:deep(.progress-steps) {
    position: relative;
    padding: 10px 10px;
}

:deep(.progress-step) {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    position: relative;
    cursor: pointer;
}

:deep(.step-connector) {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 12px;
}

:deep(.circle) {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: 1.5px solid #ccc;
    background-color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2rem;
    color: #333333;
    transition: all 0.3s ease;
}

:deep(.circle.reached) {
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
    border-color: #888888;
}

:deep(.vertical-line) {
    width: 4px;
    height: 30px;
    background-color: #d0d7e2;
    margin: 0;
    flex-shrink: 0;
}

:deep(.step-content) {
    display: flex;
    flex-direction: column;
}

:deep(.label) {
    font-size: 0.95em;
    font-weight: 400;
    color: #444;
    margin-top: 4px;
}

:deep(.step-date) {
    font-size: 0.8em;
    color: #888888;
    margin-top: 4px;
}

:deep(.current-status) {
    color: #3429ff;
    font-weight: 600;
}

.btn-delete {

    font-size: medium;
    font-weight: 500;
    letter-spacing: 0.1em;
    background: linear-gradient(0deg, #834444 0%, #c09595 100%);
    color: rgb(255, 255, 255);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.custom-image-modal {
    position: fixed;
    inset: 0;
    z-index: 1055;
    display: flex;
    align-items: center;
    justify-content: center;
}

.modal-backdrop {
    position: absolute;
    inset: 0;
    background-color: rgba(0, 0, 0, 0.7);
}

.modal-content-box {
    position: relative;
    z-index: 1060;
    background: #2e2e2e;
    padding: 0.6rem;
    border-radius: 8px;
    max-width: 90%;
    max-height: 90%;
}

.btn-expand {
    background: #8e8e8e4b !important;
    padding: 4px 7px;
    border-radius: 5px;
    color: gray;
}
</style>