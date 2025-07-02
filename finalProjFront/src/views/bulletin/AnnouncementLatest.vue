<template>
    <div class="container py-4">
        <h2 class="mb-4">最新公告</h2>

        搜尋區塊
        <div class="d-flex gap-2 mb-3">
            <input v-model="searchTitle" class="form-control" placeholder="輸入標題關鍵字" />
            <select v-model="searchCategory" class="form-select">
                <option value="">全部分類</option>
                <option v-for="cat in categoryList" :key="cat" :value="cat">{{ cat }}</option>
            </select>
            <button class="btn btn-primary" @click="searchBulletins">搜尋</button>
        </div>

        <!-- 公告列表 -->
        <div v-for="bulletin in bulletins" :key="bulletin.id" class="card mb-3" style="background-color: white;">
            <h5 class="card-header fw-bold" :style="{ backgroundColor: getCategoryColor(bulletin.categoryName) }">
                <span class="badge rounded-pill me-2"
                    :style="{ backgroundColor: getBadgeColor(bulletin.categoryName), color: '#fff', textShadow: '1px 1px 1px grey', fontSize: '80%' }">
                    {{ bulletin.categoryName }}
                </span>

                <a href="#" class="text-dark hover-underline" @click.prevent=" openBulletin(bulletin.id)"
                    style="font-size: 90%;">
                    {{ bulletin.title }}
                </a>
            </h5>

            <div class="card-body mx-4">
                <p class="card-text " style="white-space: pre-wrap;">
                    {{ normalizeNewline(truncateText(bulletin.description, 50)) }}
                    <span v-if="bulletin.description.length > 50">
                        ...
                        <a href="#" class="text-primary text-decoration-underline"
                            @click.prevent="openBulletin(bulletin.id)" style="font-size: 80%;">(查看更多)</a>
                    </span>
                </p>
                <a href="#" class="text-primary text-decoration-underline" @click.prevent="openBulletin(bulletin.id)"
                    style="font-size: 80%;">查看詳細內容</a>
                <h6 style="font-size: 80%;">發布時間：{{ formatDate(bulletin.postTime) }}</h6>
            </div>
        </div>

        <!-- 公告 Modal -->
        <div class="modal fade" id="bulletinModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header"
                        :style="{ backgroundColor: getCategoryColor(selectedBulletin?.categoryName) }">
                        <h4 class="modal-title fw-bold"> <span class="badge rounded-pill me-2"
                                :style="{ backgroundColor: getBadgeColor(selectedBulletin?.categoryName), color: '#fff', textShadow: '1px 1px 1px grey', fontSize: '80%' }">
                                {{ selectedBulletin?.categoryName }}
                            </span>{{ selectedBulletin?.title }}</h4>


                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body lh-lg mx-4">

                        <p style="white-space: pre-wrap;"><br>{{
                            normalizeNewline(selectedBulletin?.description) }}</p>

                        <!-- 附件 -->
                        <div v-if="selectedBulletin?.attachments?.length" style="font-size: 80%;">
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
                                    <label class="form-check-label my-2">{{ opt.text }}</label>
                                </div>
                                <button class="btn btn-primary mt-2" @click="submitVote">提交投票</button>
                            </div>
                        </div>

                        <!-- 留言區塊 -->
                        <div class="mt-4">
                            <h6>留言：</h6>

                            <!-- 若有留言 -->
                            <div v-if="Array.isArray(selectedBulletin?.comments)">
                                <div v-for="comment in selectedBulletin.comments.filter(c => !c.parentCommentId)"
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
                                    <div>
                                        <button class="btn btn-sm me-1"
                                            :class="comment.likedByCurrentUser ? 'btn-primary' : 'btn-outline-primary'"
                                            @click="likeComment(comment.id)">
                                            🧡 {{ comment.likeCount }}
                                        </button>
                                        <button class="btn btn-sm btn-outline-secondary me-1"
                                            @click="toggleReply(comment.id)">回覆</button>
                                        <button class="btn btn-sm btn-outline-danger"
                                            @click="deleteComment(comment.id)">刪除</button>
                                    </div>

                                    <!-- 第二層留言 -->
                                    <div v-for="reply in selectedBulletin.comments.filter(r => r.parentCommentId === comment.id)"
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
                                        <div>
                                            <button class="btn btn-sm me-1"
                                                :class="reply.likedByCurrentUser ? 'btn-primary' : 'btn-outline-primary'"
                                                @click="likeComment(reply.id)">
                                                🧡 {{ reply.likeCount }}
                                            </button>
                                            <button class="btn btn-sm btn-outline-secondary me-1"
                                                @click="toggleReply(reply.id)">回覆</button>
                                            <button class="btn btn-sm btn-outline-danger"
                                                @click="deleteComment(reply.id)">刪除</button>
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

const bulletins = ref([])
const selectedBulletin = ref(null)
const selectedOptions = ref([])
const newComment = ref('')
const replyContent = ref('')
const replyingToId = ref(null)
const searchTitle = ref('')
const searchCategory = ref('')
const categoryList = ref([])
const userId = 5 // 假設當前使用者 id
import maleIcon from '@/assets/images/bulletin/male.png'
import femaleIcon from '@/assets/images/bulletin/female.png'
import defaultIcon from '@/assets/images/bulletin/default.png'

const formatDate = (dt) => new Date(dt).toLocaleString()
const truncateText = (text, maxLength) => text?.length > maxLength ? text.slice(0, maxLength) : text

const bgColors = ['#b0cefa', '#fff7e6', '#f3fdf3', '#f8e8ff', '#e6ffe6']
const badgeColors = ['#0d6efd', '#ffc107', '#28a745', '#d63384', '#20c997']

function getCategoryColor(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 5
    return bgColors[index % bgColors.length]
}
function getAvatarByGender(gender) {
    if (gender === '男') return maleIcon;
    if (gender === '女') return femaleIcon;
    return defaultIcon;
}
function getBadgeColor(categoryName) {
    const index = categoryList.value.findIndex(c => c === categoryName) % 5
    return badgeColors[index % badgeColors.length]
}


function normalizeNewline(text) {
    return text?.replace(/\\n/g, '\n') || ''
}

onMounted(() => {
    fetchAll()
})

function fetchAll() {
    axios.get('http://localhost:8080/api/bulletin')
        .then(res => {
            console.log(res.data.list);
            bulletins.value = res.data.list
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
        bulletins.value = res.data.list
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
        openBulletin(selectedBulletin.value.id)
        newComment.value = ''
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
        openBulletin(selectedBulletin.value.id)
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
        })
}





function deleteComment(commentId) {
    axios.post(`http://localhost:8080/api/bulletin/comment/${commentId}`)
        .then(() => openBulletin(selectedBulletin.value.id))
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
</style>
