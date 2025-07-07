<template>


    <div class="container py-4 ">
        <BannerImage :imageSrc="OO" heading="最新公告" subtext="" textAlign="left" />


        <div class="d-flex gap-2 mb-3">
            <select v-model="searchCategory" class="form-select w-30" @change="searchBulletins">
                <option value="">全部分類</option>
                <option v-for="cat in categoryList" :key="cat" :value="cat">{{ cat }}</option>
            </select>
            <input v-model="searchTitle" class="form-control w-48" placeholder="輸入標題關鍵字" />
            <button class="btn btn-primary w-10" @click="searchBulletins">搜尋</button>
            <button class="btn btn-secondary w-10" @click="clearSearch">清除搜尋</button>
        </div>



        <!-- 公告 card 列表 -->
        <div class="announcements-section">
            <div class="announcements-grid">
                <div v-for="bulletin in bulletins" :key="bulletin.id"
                    :class="['announcement-card', getGridColor(bulletin.categoryName)]">
                    <div class="announcement-header">
                        <div class="announcement-badge fs-6">
                            <i :class="['bi', getIcon(bulletin.categoryName)]"></i>
                            {{ bulletin.categoryName }}
                        </div>
                        <div class="announcement-date">{{ formatDate(bulletin.postTime) }}</div>
                    </div>
                    <h3 class="announcement-title">{{ bulletin.title }}</h3>
                    <p class="announcement-content">
                        {{ normalizeNewline(truncateText(bulletin.description, 50)) }}
                        <span v-if="bulletin.description.length > 50">
                            ...
                        </span>
                    </p>
                    <div class="announcement-footer">
                        <span class="announcement-author">發布人：{{ bulletin.userName }}</span>
                        <button class="read-more-btn" @click.prevent="openBulletin(bulletin.id)">
                            <i class="bi bi-arrow-right"></i>
                            閱讀更多
                        </button>
                    </div>
                </div>
            </div>
        </div>


        <!-- 公告 Modal -->
        <div class="modal fade " id="bulletinModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-scrollable ">
                <div class="modal-content">
                    <div class="modal-header"
                        :style="{ backgroundColor: getCategoryColor(selectedBulletin?.categoryName) }">


                        <div class="announcement-badge fs-5 mt-1">
                            <i :class="['bi', getIcon(selectedBulletin?.categoryName)]"></i>
                            {{ selectedBulletin?.categoryName }}
                        </div>
                        <h3 class="announcement-title mt-3 mx-2 fs-4">{{ selectedBulletin?.title }}</h3>


                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body lh-lg mx-4">

                        <p class="fs-5" style="white-space: pre-wrap;"><br>{{
                            normalizeNewline(selectedBulletin?.description) }}</p>

                        <!-- 附件 -->
                        <div v-if="selectedBulletin?.attachments?.length" style="font-size: 100%;">
                            <hr class="mt-5">
                            <h6>附件：</h6>
                            <ul class="list-group">
                                <li v-for="att in selectedBulletin.attachments" :key="att.id"
                                    class="list-group-item list-group-item-action m-2">
                                    <a :href="`http://localhost:8080/api/bulletin/attachments/${att.id}`"
                                        target="_blank">{{ att.fileName }}</a>
                                </li>
                            </ul>
                        </div>

                        <!-- 投票區塊 -->
                        <div v-if="selectedBulletin?.poll" class="card my-3 w-75 mx-auto d-block ">
                            <div class="card-header  text-grey fw-bold"
                                :style="{ backgroundColor: getCategoryColor(selectedBulletin?.categoryName) }">
                                投票：{{ selectedBulletin.poll.title }}
                            </div>
                            <div class="card-body">
                                <div v-for="opt in selectedBulletin.poll.options" :key="opt.id" class="form-check">
                                    <input :type="selectedBulletin.poll.isMultiple ? 'checkbox' : 'radio'"
                                        class="form-check-input my-2" name="voteOption" :value="opt.id"
                                        v-model="selectedOptions" />
                                    <label class="form-check-label ">{{ opt.text }}</label>
                                </div>
                                <button class="btn btn-primary mt-2" @click="submitVote">提交投票</button>
                            </div>
                        </div>

                        <!-- 留言區塊 -->
                        <div class="mt-4">
                            <h6>留言：</h6>

                            <!-- 若有留言 -->
                            <div v-if="Array.isArray(selectedBulletin?.comments)">
                                <div v-for="comment in selectedBulletin.comments.filter(c => !c.parentCommentId && c.isAlive === true)"
                                    :key="comment.id" class="border rounded p-2 mb-2">
                                    <div class="d-flex align-items-start mb-2">
                                        <img :src="getAvatarByGender(comment.userData[1])" class="rounded-circle me-2"
                                            width="40" height="40" />
                                        <div>
                                            <strong>{{ comment.userData[0] || '匿名用戶' }}</strong>
                                            <p class="mb-1">{{ comment.comment }}</p>
                                            <span class="text-muted" style="font-size: 0.85rem">{{
                                                formatDate(comment.time) }}</span>
                                        </div>
                                    </div>
                                    <div style="margin-left: 5%;">
                                        <button class="btn-comment me-1" @click="likeComment(comment.id)">
                                            🧡 {{ comment.likeCount }}
                                        </button>
                                        <button class="btn-comment me-1" @click="toggleReply(comment.id)">回覆</button>
                                        <button v-if="comment.userData[2] === userId" class="btn-comment me-1"
                                            @click="deleteComment(selectedBulletin.id, comment)">刪除</button>
                                    </div>

                                    <!-- 第二層留言 -->
                                    <div v-for="reply in selectedBulletin.comments.filter(r => r.parentCommentId === comment.id && r.isAlive === true)"
                                        :key="reply.id" class="ms-4 mt-2 border-start ps-2">
                                        <div class="d-flex align-items-start mb-2">
                                            <img :src="getAvatarByGender(reply.userData[1])" class="rounded-circle me-2"
                                                width="35" height="35" />
                                            <div>
                                                <strong>{{ reply.userData[0] || '匿名用戶' }}</strong>
                                                <p class="mb-1">{{ reply.comment }}</p>
                                                <span class="text-muted" style="font-size: 0.8rem">{{
                                                    formatDate(reply.time) }}</span>
                                            </div>
                                        </div>
                                        <div style="margin-left: 5%;">
                                            <button class=" btn-comment me-1" @click="likeComment(reply.id)">
                                                🧡 {{ reply.likeCount }}
                                            </button>
                                            <button class="btn-comment me-1"
                                                @click="toggleReply(reply.parentCommentId)">回覆</button>
                                            <button v-if="reply.userData[2] === userId" class="btn-comment me-1"
                                                @click="deleteComment(selectedBulletin.id, reply)">刪除</button>
                                        </div>
                                    </div>

                                    <!-- 回覆輸入框 -->
                                    <div v-if="replyingToId === comment.id" class="mt-2">
                                        <input v-model="replyContent" class="form-control" placeholder="輸入回覆內容..." />
                                        <button class="btn btn-sm btn-primary mt-1"
                                            @click="submitReply(comment.id)">送出回覆</button>
                                    </div>
                                </div>
                            </div>

                            <!-- 若沒有留言資料（null） -->
                            <div v-else class="text-muted">尚無留言</div>

                            <!-- 新增留言 -->
                            <div class="mt-3">
                                <input v-model="newComment" class="form-control" placeholder="新增留言..." />
                                <button class="btn btn-primary mt-2" @click="submitComment">送出留言</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import Swal from 'sweetalert2'


import BannerImage from '@/components/forAll/BannerImage.vue';
import OO from '@/assets/images/bulletin/banner.png';

const bulletins = ref([])
const selectedBulletin = ref(null)
const selectedOptions = ref([])
const newComment = ref('')
const replyContent = ref('')
const replyingToId = ref(null)
const searchTitle = ref('')
const searchCategory = ref('')
const categoryList = ref([])
const userId = 3 // 假設當前使用者 id
const communityId = 1 // 假設當前社區 ID
import maleIcon from '@/assets/images/bulletin/male.png'
import femaleIcon from '@/assets/images/bulletin/female.png'
import defaultIcon from '@/assets/images/bulletin/default.png'

const formatDate = (dt) => new Date(dt).toLocaleString()
const truncateText = (text, maxLength) => text?.length > maxLength ? text.slice(0, maxLength) : text

const bgColors = ['#b0cefa', '#fff7e6', '#f3fdf3', '#f8e8ff', '#e6ffe6']
const badgeColors = ['#0d6efd', '#ffc107', '#28a745', '#d63384', '#20c997']
const gridClass = ['important', 'event', 'service', '']
const iconClass = ['bi-exclamation-triangle', 'bi-calendar-check', 'bi-info-circle', 'bi-megaphone']

function getCategoryColor(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 5
    return gridClass[index % gridClass.length]
}
function getAvatarByGender(gender) {
    if (gender === '男') return maleIcon;
    if (gender === '女') return femaleIcon;
    return defaultIcon;
}
function getGridColor(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 4

    return gridClass[index]
}
function getIcon(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 4
    return iconClass[index]
}


function normalizeNewline(text) {
    return text?.replace(/\\n/g, '\n') || ''
}

onMounted(() => {

    fetchAll()
})

function fetchAll() {
    console.log(communityId);
    axios.get('http://localhost:8080/api/bulletin/community/' + communityId)
        .then(res => {
            // console.log(res.data.list);
            const postedList = res.data.list.filter(val => val.postStatus === true)
            bulletins.value = postedList.sort((a, b) => new Date(b.postTime) - new Date(a.postTime))
            const cats = new Set(res.data.list.map(b => b.categoryName))
            categoryList.value = [...cats]
        })
}

function openBulletin(id) {
    axios.get(`http://localhost:8080/api/bulletin/${id}`).then(res => {
        selectedBulletin.value = res.data.list[0]
        selectedOptions.value = []
        new bootstrap.Modal(document.getElementById('bulletinModal')).show()
    })
}

function searchBulletins() {
    axios.post('http://localhost:8080/api/bulletin/searchby', {
        title: searchTitle.value || undefined,
        category: searchCategory.value ? { name: searchCategory.value } : undefined
    }).then(res => {
        const sortedList = res.data.list.sort((a, b) => new Date(b.postTime) - new Date(a.postTime))
        bulletins.value = sortedList
        console.log(sortedList);
    })
}

function submitVote() {
    const poll = selectedBulletin.value.poll
    const options = poll.isMultiple ? selectedOptions.value : [selectedOptions.value]
    Promise.all(options.map(id => {
        return axios.post(`http://localhost:8080/api/poll/${id}/vote`, {
            user: { usersId: userId },
            option: { id }
        })
    })).then(() => alert('投票成功'))
}

function submitComment() {
    axios.post(`http://localhost:8080/api/bulletin/${selectedBulletin.value.id}/comment`, {
        user: { usersId: userId },
        comment: newComment.value
    }).then(() => {
        // ✅ 只更新留言部分
        axios.get(`http://localhost:8080/api/bulletin/${selectedBulletin.value.id}`)
            .then(res => {
                selectedBulletin.value.comments = res.data.list[0].comments
                newComment.value = ''
            })
    })
}


function toggleReply(commentId) {
    replyingToId.value = replyingToId.value === commentId ? null : commentId
    replyContent.value = ''
}

function submitReply(parentId) {
    axios.post(`http://localhost:8080/api/bulletin/${selectedBulletin.value.id}/comment`, {
        user: { usersId: userId },
        comment: replyContent.value,
        parentComment: { id: parentId }
    }).then(() => {
        axios.get(`http://localhost:8080/api/bulletin/${selectedBulletin.value.id}`).then(res => {
            selectedBulletin.value.comments = res.data.list[0].comments
        })

        // ✅ Step 3：清除輸入欄位
        replyContent.value = ''
        replyingToId.value = null
    })
}

function likeComment(commentId) {
    axios.post(`http://localhost:8080/api/bulletin/comment/${commentId}/like/${userId}`)
        .then(res => {
            const updated = res.data
            const comment = selectedBulletin.value.comments.find(c => c.id === commentId)
            if (comment) {
                comment.likeCount = updated.likeCount
                comment.likedByCurrentUser = updated.likedByCurrentUser
            }
            console.log(comment);
        })
}




async function deleteComment(bulletinId, comment) {
    const data = {
        bulletin: { id: bulletinId },
        comment: comment.comment,
        user: { usersId: userId },
        isAlive: false
    }

    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    })

    if (result.isConfirmed) {
        try {
            await axios.put(`http://localhost:8080/api/bulletin/comment/${comment.id}`, data)

            // ✅ 關閉 Modal
            const modalEl = document.getElementById('bulletinModal')
            const modalInstance = bootstrap.Modal.getInstance(modalEl)
            if (modalInstance) {
                modalInstance.hide()
            }

            await Swal.fire({
                title: '已刪除',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            })

            // ✅ 等動畫結束後重新開啟
            setTimeout(() => {
                openBulletin(bulletinId)
            }, 200) // 建議等 200ms 讓 backdrop 正常清除

        } catch (err) {
            console.error(err)
            Swal.fire('錯誤', '刪除失敗，請稍後再試', 'error')
        }
    }
}



function getFileUrl(att) {
    // 你可以根據檔案服務 API 自行補上 URL
    return `data:${att.mimeType};base64,${att.fileData}`
}
</script>

<style scoped>
/* h1 {
    color: #b0cefa;
} */

.card-title {
    font-size: 1.2rem;
    font-weight: bold;
}

.hover-underline:hover {
    text-decoration: underline;
}

.hover-underline {
    text-decoration: none;
}

.modal-dialog {
    margin-top: 10vh;
    /* 或你想要的距離，例如 10vh */
    max-height: 90vh;
}

.modal-content {
    max-height: 90vh;
    overflow: hidden;
}

.modal-body {
    overflow-y: auto;
    max-height: 75vh;
}


:deep(.hero-image-wrapper) {
    max-height: 150px;
    max-width: 100%;
}


/* 公告區域 */
.announcements-section {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.section-title {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 24px;
    font-weight: 600;
    color: #2d3748;
    margin: 0;
}

.section-title i {
    color: #667eea;
}

.view-all-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 25px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 8px;
}

.view-all-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.announcements-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 0.3333fr));
    gap: 1rem;
}

.announcement-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.announcement-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.announcement-card.important::before {
    background: linear-gradient(135deg, #f56565 0%, #e53e3e 100%);
}

.announcement-card.event::before {
    background: linear-gradient(135deg, #ffc107 0%, #f6ad55 100%);
}

.announcement-card.service::before {
    background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
}

.announcement-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.announcement-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.announcement-badge {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    background: rgba(102, 126, 234, 0.1);
    color: #667eea;
}

.announcement-card.important .announcement-badge {
    background: rgba(245, 101, 101, 0.1);
    color: #f56565;
}

.announcement-card.event .announcement-badge {
    background: rgba(255, 193, 7, 0.1);
    color: #ffc107;
}

.announcement-card.service .announcement-badge {
    background: rgba(72, 187, 120, 0.1);
    color: #48bb78;
}

.announcement-date {
    font-size: 12px;
    color: #a0aec0;
}

.announcement-title {
    font-size: 18px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 12px;
    line-height: 1.4;
}

.announcement-content {
    color: #718096;
    line-height: 1.6;
    margin-bottom: 20px;
    display: -webkit-box;

    -webkit-box-orient: vertical;
    overflow: hidden;
}

.announcement-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.announcement-author {
    font-size: 12px;
    color: #a0aec0;
}

.read-more-btn {
    background: none;
    border: none;
    color: #667eea;
    font-size: 12px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
    transition: all 0.3s ease;
}

.read-more-btn:hover {
    color: #5a6fd8;
    transform: translateX(4px);
}

.w-30 {
    width: 33.2% !important;
    margin-right: 1rem;
}

.w-48 {
    width: 47% !important;
}

.w-10 {
    width: 10% !important;
}

.btn-comment {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 8px 16px;
    border: none;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background-color: white;
    color: rgb(128, 159, 243);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
    height: 35px;
    margin-bottom: 3%;

}

.btn-comment:hover {
    transform: translateY(-2px);
    background-color: #e5efff;
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);

}
</style>