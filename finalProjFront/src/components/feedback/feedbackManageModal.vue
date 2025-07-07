<template>
    <div class="modal fade" id="feedbackModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">回饋詳情與管理</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <p><strong>主旨：</strong>{{ feedback?.title }}</p>
                    <p><strong>內容：</strong>{{ feedback?.description }}</p>
                    <p><strong>分類：</strong>{{ feedback?.category?.name }}</p>
                    <p><strong>提交時間：</strong>{{ formatDate(feedback?.submittedAt) }}</p>
                    <p><strong>最後更新時間：</strong>{{ formatDate(feedback?.lastUpdated) }}</p>

                    <!-- 附件 -->
                    <div v-if="feedback.attachments?.length">
                        <strong>附件：</strong>
                        <ul>
                            <li v-for="file in feedback.attachments" :key="file.id">
                                {{ file.fileName }}
                            </li>
                        </ul>
                    </div>

                    <!-- 狀態下拉選單 -->
                    <div class="mb-3">
                        <label class="form-label fw-bold">處理狀態</label>
                        <select v-model="status" class="form-select">
                            <option value="待處理">待處理</option>
                            <option value="處理中">處理中</option>
                            <option value="確認中">確認中</option>
                            <option value="已結案">已結案</option>
                        </select>
                    </div>

                    <!-- 回覆列表 -->
                    <div>
                        <h6>留言紀錄</h6>
                        <ul class="list-group">
                            <li class="list-group-item" v-for="reply in feedback.replies" :key="reply.id">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <strong>{{ reply.user?.name || '未知使用者' }}</strong>：{{ reply.reply }}
                                        <br />
                                        <small class="text-muted">{{ formatDateTime(reply.repliedAt) }}</small>
                                    </div>
                                    <button class="btn btn-sm btn-danger" @click="deleteReply(reply.id)">刪除</button>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <!-- 新增留言 -->
                    <div class="mt-3">
                        <label class="form-label fw-bold">新增回覆</label>
                        <input v-model="newReply" type="text" class="form-control mb-2" placeholder="輸入留言..." />
                        <button class="btn btn-primary" @click="submitReply">送出</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const feedback = ref({})
const status = ref('')
const newReply = ref('')

const emit = defineEmits(['updated'])

const formatDate = (dt) => new Date(dt).toLocaleString('zh-TW')
const formatDateTime = (dt) => new Date(dt).toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
})

onMounted(() => {
    window.addEventListener('load-feedback-for-edit', (e) => {
        feedback.value = e.detail
        status.value = e.detail.status
    })
})

// ✅ 修改狀態 or 加回覆都會更新 feedback.status
const submitReply = async () => {
    if (!newReply.value.trim()) return
    const res = await axios.post(`http://localhost:8080/api/feedback/${feedback.value.id}/reply`, {
        user: { usersId: Number(localStorage.getItem('userId')) },
        reply: newReply.value.trim(),
        preFeedBackStatus: feedback.value.status,
        newFeedBackStatus: status.value
    })
    if (res.data?.id) {
        newReply.value = ''
        emit('updated') // 通知主頁刷新資料
    }
}

// ✅ 刪除留言
const deleteReply = async (replyId) => {
    const confirmed = confirm('確定要刪除這則留言嗎？')
    if (!confirmed) return
    await axios.delete(`http://localhost:8080/api/feedback/reply/${replyId}`)
    emit('updated')
}
</script>


<style scoped>
.border-dashed {
    border-style: dashed !important;
}
</style>