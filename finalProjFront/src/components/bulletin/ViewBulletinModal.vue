<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)"
        :title="bulletin?.title || '公告詳情'">
        <div>
            <div class="mb-3 text-muted small">
                分類：{{ bulletin.categoryName }} ｜ 發布人：{{ bulletin.userName }} ｜ 發布時間：{{ formatDate(bulletin.postTime) }}
            </div>

            <p class="lh-lg" style="white-space: pre-wrap">
                {{ bulletin.description }}
            </p>

            <div v-if="attachments.length" class="my-3">
                <h6>附件：</h6>
                <ul class="list-group">
                    <li v-for="att in attachments" :key="att.id" class="list-group-item">
                        <a :href="`http://localhost:8080/api/bulletin/attachments/${att.id}`" target="_blank">{{
                            att.fileName }}</a>
                    </li>
                </ul>
            </div>

            <div v-if="bulletin.poll" class="my-4">
                <h6>投票結果：{{ bulletin.poll.title }}</h6>
                <BarChart :labels="pollLabels" :data="pollVotes" />
            </div>

            <div v-if="Array.isArray(bulletin.comments)" class="mt-4">
                <h6>留言：</h6>
                <div v-for="comment in rootComments" :key="comment.id" class="border rounded p-2 mb-2">
                    <div><strong>{{ comment.userData[0] || '匿名用戶' }}</strong></div>
                    <p>{{ comment.comment }}</p>
                    <small class="text-muted">{{ formatDate(comment.time) }}</small>
                    <div class="mt-2">
                        <button class="btn btn-sm btn-outline-primary me-1" @click="likeComment(comment.id)">🧡 {{
                            comment.likeCount }}</button>
                        <button class="btn btn-sm btn-outline-secondary me-1" @click="replyTo(comment.id)">回覆</button>
                        <button v-if="comment.userData[2] == userId" class="btn btn-sm btn-outline-danger"
                            @click="deleteComment(comment.id)">刪除</button>
                    </div>

                    <!-- 回覆列表 -->
                    <div v-for="reply in repliesOf(comment.id)" :key="reply.id" class="ms-4 mt-2 border-start ps-2">
                        <strong>{{ reply.userData[0] || '匿名用戶' }}</strong>
                        <p>{{ reply.comment }}</p>
                        <small class="text-muted">{{ formatDate(reply.time) }}</small>
                    </div>

                    <!-- 回覆輸入框 -->
                    <div v-if="replyingToId === comment.id" class="mt-2">
                        <input v-model="replyContent" class="form-control" placeholder="輸入回覆內容..." />
                        <button class="btn btn-sm btn-primary mt-1" @click="submitReply(comment.id)">送出回覆</button>
                    </div>
                </div>

                <!-- 新增留言 -->
                <input v-model="newComment" class="form-control" placeholder="新增留言..." />
                <button class="btn btn-primary mt-2" @click="submitComment">送出留言</button>
            </div>
        </div>
    </ModalWrapper>
</template>

<script setup>
import { computed, ref } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import BarChart from '@/components/bulletin/BarChart.vue'
import axios from 'axios'

const props = defineProps({
    visible: Boolean,
    bulletin: Object,
    userId: Number,
})
const emit = defineEmits(['update:visible', 'refresh'])

const newComment = ref('')
const replyContent = ref('')
const replyingToId = ref(null)

const attachments = computed(() => props.bulletin?.attachments || [])
const pollLabels = computed(() => props.bulletin?.poll?.options?.map(opt => opt.text) || [])
const pollVotes = computed(() => props.bulletin?.poll?.options?.map(opt => opt.voteCount || 0) || [])
const rootComments = computed(() => props.bulletin?.comments?.filter(c => !c.parentCommentId) || [])
const repliesOf = (parentId) => props.bulletin?.comments?.filter(c => c.parentCommentId === parentId) || []

const formatDate = (dt) => new Date(dt).toLocaleString()

function replyTo(commentId) {
    replyingToId.value = replyingToId.value === commentId ? null : commentId
    replyContent.value = ''
}

function submitReply(parentId) {
    axios.post(`http://localhost:8080/api/bulletin/${props.bulletin.id}/comment`, {
        user: { usersId: props.userId },
        comment: replyContent.value,
        parentComment: { id: parentId },
    }).then(() => {
        emit('refresh')
        replyContent.value = ''
        replyingToId.value = null
    })
}

function submitComment() {
    axios.post(`http://localhost:8080/api/bulletin/${props.bulletin.id}/comment`, {
        user: { usersId: props.userId },
        comment: newComment.value,
    }).then(() => {
        emit('refresh')
        newComment.value = ''
    })
}

function deleteComment(commentId) {
    axios.post(`http://localhost:8080/api/bulletin/comment/${commentId}`).then(() => {
        emit('refresh')
    })
}

function likeComment(commentId) {
    axios.post(`http://localhost:8080/api/bulletin/comment/${commentId}/like/${props.userId}`).then(() => {
        emit('refresh')
    })
}
</script>
