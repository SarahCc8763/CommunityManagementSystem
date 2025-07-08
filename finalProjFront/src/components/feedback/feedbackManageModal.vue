<template>
    <div class="modal fade" id="feedbackModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content modal-content-x">
                <div class="modal-header">
                    <h5 class="modal-title">回饋詳情與管理</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <p><strong>主旨：</strong>{{ selectedFeedback?.title }}</p>
                    <p><strong>內容：</strong>{{ selectedFeedback?.description }}</p>
                    <p><strong>分類：</strong>{{ selectedFeedback?.category?.name }}</p>
                    <p><strong>提交時間：</strong>{{ formatDate(selectedFeedback?.submittedAt) }}</p>
                    <p><strong>最後更新時間：</strong>{{ formatDate(selectedFeedback?.lastUpdated) }}</p>

                    <!-- 附件 -->
                    <div v-if="selectedFeedback?.attachments?.length">
                        <strong>附件：</strong>
                        <ul>
                            <li v-for="file in selectedFeedback?.attachments" :key="file.id">
                                {{ file.fileName }}
                            </li>
                        </ul>
                    </div>

                    <!-- 狀態下拉選單 -->
                    <div class="mb-3">
                        <label class="form-label fw-bold text-muted text-d">處理狀態</label>
                        <select v-model="status" class="form-select bg-status">
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
                            <li class="list-group-item bg-reply" v-for="reply in selectedFeedback?.replies"
                                :key="reply.id">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <strong>{{ reply.user?.name || '未知使用者' }}</strong>：{{ reply.reply }}
                                        <br />
                                        <small class="text-muted">{{ formatDateTime(reply.repliedAt) }}</small>
                                    </div>

                                </div>
                            </li>
                        </ul>
                    </div>

                    <button type="button" @click="submitReply" class="btn btn-primary mt-3">送出修改</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'

const status = ref('')

const emit = defineEmits(['updated'])

const props = defineProps({
    selectedFeedback: {
        type: Object,
        required: true
    }
})

const formatDate = (dt) => new Date(dt).toLocaleString('zh-TW')
const formatDateTime = (dt) => new Date(dt).toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
})

// 每次 props.selectedFeedback 改變，就同步狀態
watch(() => props.selectedFeedback, (val) => {
    if (val) {
        status.value = val.status
    }

})


// ✅ 修改狀態 or 加回覆都會更新 feedback.status
const submitReply = async () => {

    const res = await axios.post(`http://localhost:8080/api/feedback/${props.selectedFeedback.id}/reply`, {
        user: { usersId: Number(localStorage.getItem('userId')) },
        reply: newReply.value.trim(),
        preFeedBackStatus: props.selectedFeedback.status,
        newFeedBackStatus: status.value

    })
    if (res.data?.id) {
        newReply.value = ''
        props.selectedFeedback.replies.push({
            ...res.data,
            user: { name: localStorage.getItem('userName') || '我' }
        })
    }
}


</script>


<style scoped>
.border-dashed {
    border-style: dashed !important;
}

.modal-content-x {
    background: rgb(229, 235, 253) !important;
    color: rgb(22, 30, 42) !important;
    border: 1px solid rgb(80, 71, 111) !important;
    border-radius: 3% !important;
}

.bg-status {
    background-color: #fff !important;
    color: black !important;
}

.text-d {
    color: rgb(22, 30, 42) !important;

}
</style>