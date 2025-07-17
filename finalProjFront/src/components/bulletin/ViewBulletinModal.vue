<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)"
        :title="bulletin?.title || '公告詳情'">
        <div>
            <div class="mb-3 text-muted small">
                分類：{{ bulletin.categoryName }} ｜ 發布人：{{ bulletin.userName }} ｜ 發布時間：{{ formatDate(bulletin.postTime) }}
            </div>

            <p class="lh-lg text-dark" style="white-space: pre-wrap">
                {{ normalizeFn(bulletin.description) }}
            </p>

            <div v-if="attachments.length" class="my-3 text-muted">
                <h6>附件：</h6>
                <ul class="list-group">
                    <li v-for="att in attachments" :key="att.id" class="list-group-item ">
                        <a :href="`${url}/api/bulletin/attachments/${att.id}`" target="_blank"
                            style="font-size: 90%;">{{
                                att.fileName }}</a>
                    </li>
                </ul>
            </div>

            <div v-if="bulletin.poll" class="my-4 text-dark ">
                <div class="poll-card">

                    <h6 class="text-center">投票結果：{{ bulletin.poll.title }}</h6>
                    <BarChart :labels="pollLabels" :data="pollVotes" />
                </div>
            </div>


            <div v-if="Array.isArray(bulletin.comments)" class="text-dark">
                <div v-for="comment in bulletin.comments.filter(c => !c.parentCommentId)" :key="comment.id"
                    class="border rounded p-2 mb-2">
                    <div class="d-flex align-items-start mb-2">
                        <img :src="getAvatarByGender(comment.userData[1])" class="rounded-circle me-2" width="40"
                            height="40" />
                        <div style="color: darkslategray;">
                            <strong>{{ comment.userData[0] || '匿名用戶' }}</strong>
                            <p class="mb-1">{{ comment.comment }}</p>
                            <span class="text-muted" style="font-size: 0.85rem">{{
                                formatDate(comment.time) }}</span>
                        </div>
                    </div>
                    <div style="margin-left: 5%;">
                        <button class="btn-comment me-1">
                            🧡 {{ comment.likeCount }}
                        </button>

                        <button class="btn-comment btn-cursor-pointer me-1"
                            @click="deleteComment(comment.id)">刪除</button>
                    </div>

                    <!-- 第二層留言 -->
                    <div v-for="reply in bulletin.comments.filter(r => r.parentCommentId === comment.id)"
                        :key="reply.id" class="ms-4 mt-2 border-start ps-2">
                        <div class="d-flex align-items-start mb-2">
                            <img :src="getAvatarByGender(reply.userData[1])" class="rounded-circle me-2" width="35"
                                height="35" />
                            <div style="color: darkslategray;">
                                <strong>{{ reply.userData[0] || '匿名用戶' }}</strong>
                                <p class="mb-1">{{ reply.comment }}</p>
                                <span class="text-muted" style="font-size: 0.8rem">{{
                                    formatDate(reply.time) }}</span>
                            </div>
                        </div>
                        <div style="margin-left: 5%;">
                            <button class=" btn-comment me-1">
                                🧡 {{ reply.likeCount }}
                            </button>
                            <button class="btn-comment btn-cursor-pointer me-1"
                                @click="deleteComment(reply.id)">刪除</button>
                        </div>
                    </div>


                </div>
            </div>


        </div>
    </ModalWrapper>
</template>

<script setup>
import { computed, ref } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import BarChart from '@/components/bulletin/BarChart.vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'

import maleIcon from '@/assets/images/bulletin/male.png'
import femaleIcon from '@/assets/images/bulletin/female.png'
import defaultIcon from '@/assets/images/bulletin/default.png'

const props = defineProps({
    visible: Boolean,
    bulletin: Object,
    userId: Number,
    normalizeFn: {
        type: Function,
        required: true
    }

})
const emit = defineEmits(['update:visible', 'refresh'])

const newComment = ref('')
const replyContent = ref('')
const replyingToId = ref(null)

const attachments = computed(() => props.bulletin?.attachments || [])
const pollLabels = computed(() => props.bulletin?.poll?.options?.map(opt => opt.text) || [])
const pollVotes = computed(() => props.bulletin?.poll?.options?.map(opt => opt.votesCount || 0) || [])
const rootComments = computed(() => props.bulletin?.comments?.filter(c => !c.parentCommentId) || [])
const repliesOf = (parentId) => props.bulletin?.comments?.filter(c => c.parentCommentId === parentId) || []

const formatDate = (dt) => new Date(dt).toLocaleString()
function getAvatarByGender(gender) {
    if (gender === '男') return maleIcon;
    if (gender === '女') return femaleIcon;
    return defaultIcon;
}

function deleteComment(commentId) {
    Swal.fire({
        title: '確定要刪除嗎？',
        text: '刪除後將無法復原，請確認是否確定刪除',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    }).then((result) => {
        axios.post(`/api/bulletin/comment/${commentId}`)
            .then(() => emit('refresh'))

    })
}
</script>

<style scoped>
.poll-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(0, 0, 0, 0.091);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
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
    cursor: default;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background-color: white;
    color: rgb(128, 159, 243);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
    height: 35px;
    margin-bottom: 3%;

}

.btn-cursor-pointer {
    cursor: pointer;
}
</style>