<template>
    <div class="modal fade" id="feedbackManageModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content modal-content-x">
                <div class="modal-header position-relative justify-content-center bg-title">
                    <h4 class="modal-title fw-bold">回 饋 詳 情 與 管 理</h4>
                    <button type="button" class="btn-close position-absolute end-0 me-3"
                        data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body fs-5">
                    <div class="container">
                        <!-- 詳情欄位：主旨、內容、分類、時間等 -->
                        <div class="row mb-2" v-for="(value, label) in detailFields" :key="label">
                            <div class="col-sm-3 fw-bold ">{{ label }}：</div>
                            <div class="col-sm-9">{{ value }}</div>
                        </div>

                        <!-- 附件 -->
                        <div class="row mb-3" v-if="selectedFeedback?.attachments?.length">
                            <div class="col-sm-3 fw-bold">附件：</div>
                            <div class="col-sm-9">
                                <ul class="list-unstyled">
                                    <li v-for="file in selectedFeedback.attachments" :key="file.id">
                                        {{ file.fileName }}
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <!-- 處理狀態 -->
                        <div class="row mb-3 fs-6">
                            <div class="col-sm-3 fw-bold">處理狀態：</div>
                            <div class="col-sm-3">
                                <select v-model="status" class="form-select bg-status ">
                                    <option value="待處理">待處理</option>
                                    <option value="確認中">確認中</option>
                                    <option value="處理中">處理中</option>
                                    <option value="已結案">已結案</option>
                                </select>
                            </div>
                        </div>

                        <!-- 回覆列表 -->
                        <div class="row fs-6">
                            <div class="col-sm-3 fw-bold">留言紀錄：</div>
                            <div class="col-sm-9">
                                <ul class="list-group">
                                    <li class="list-group-item bg-reply" v-for="reply in selectedFeedback?.replies"
                                        :key="reply.id">
                                        <div>
                                            <strong>{{ reply.userName || '未知使用者' }}</strong>：{{ reply.reply }}
                                            <br />
                                            <small class="text-muted">{{ formatDateTime(reply.repliedAt) }}</small>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <!-- 按鈕 -->
                        <div class="text-end mt-4">
                            <button type="button" @click="submitReply" class="btn btn-primary">送出修改</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'
const userStore = useUserStore()
const userId = userStore.userId || 0 // 假設當前使用者 id
const communityId = userStore.communityId || 0 // 假設當前社區 ID

const status = ref('')

const emit = defineEmits(['updated'])

const props = defineProps({
    selectedFeedback: {
        type: Object,
        // required: true
    }
})
const detailFields = computed(() => ({
    主旨: props.selectedFeedback?.title || '',
    內容: props.selectedFeedback?.description || '',
    分類: props.selectedFeedback?.category?.name || '',
    提交時間: formatDate(props.selectedFeedback?.submittedAt),
    最後更新時間: formatDate(props.selectedFeedback?.lastUpdated)
}))


const formatDate = (dt) => new Date(dt).toLocaleString('zh-TW')
const formatDateTime = (dt) => new Date(dt).toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
})

// 每次 props.selectedFeedback 改變，就同步狀態
watch(
    () => props.selectedFeedback,
    (val) => {
        if (val) {
            status.value = val.status
            //console.log(status.value);
        }
    },
    { immediate: true } // ✅ 加這行讓它在初始化時也會跑一次
)



// ✅ 修改狀態 更新 feedback.status
const submitReply = async () => {
    try {
        console.log(userId);
        const res = await axios.put(`/api/feedback/status/${props.selectedFeedback.id}`, {
            id: props.selectedFeedback.id, // ✅ 後端使用 findById(id)
            status: status.value, // ✅ 後端會比對原始與新狀態
            handlerId: userId || 0 // ✅ 後端會透過此 ID 查找承辦人

        });

        console.log(res.data);
        if (res.data?.list[0]?.id) {
            // 可以在這裡加入成功提示或更新本地狀態
            emit('updated'); // 通知父元件重新 fetch 資料
            await Swal.fire({
                icon: 'success',
                title: '成功',
                text: '狀態更新成功',
                showConfirmButton: false,
                timer: 1500
            })
        }
    } catch (err) {
        //console.error('狀態更新失敗', err);
        alert('更新失敗，請稍後再試');
    }
}



</script>


<style scoped>
.border-dashed {
    border-style: dashed !important;
}

.modal-content-x {
    background: linear-gradient(to bottom right, #2c3b4f, #3e3560) !important;
    color: rgb(255, 255, 255) !important;
    border: 1px solid rgb(80, 71, 111) !important;
    border-radius: 20px !important;
}

.bg-status {
    background-color: #fff !important;
    color: black !important;
    appearance: auto !important;
}

.text-d {
    color: rgb(22, 30, 42) !important;

}

.bg-title {
    background: #445484 !important;
}
</style>
