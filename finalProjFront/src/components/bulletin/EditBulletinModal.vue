<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)" title="編輯公告">
        <form @submit.prevent="submitEdit">
            <div class="form-check form-switch my-3">
                <input type="checkbox" id="postStatus" class="form-check-input " v-model="form.postStatus">
                <label for="postStatus" class="form-check-label text-dark ">對外發布</label>
            </div>
            <div class="form-check form-switch my-3">
                <input type="checkbox" id="isPinned" class="form-check-input " v-model="form.isPinned">
                <label for="isPinned" class="form-check-label text-dark ">是否置頂</label>
            </div>
            <div class="mb-3">
                <label class="form-label ">標題</label>
                <input v-model="form.title" class="form-control " required />
            </div>

            <div class="mb-3">
                <label class="form-label">內容</label>
                <textarea v-model="form.description" class="form-control" rows="10"></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">分類</label>
                <select v-model="form.categoryName" class="form-select" required>
                    <option v-for="cat in categoryList" :key="cat.id" :value="cat.name">
                        {{ cat.name }}
                    </option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">發布時間</label>
                <input type="datetime-local" v-model="form.postTime" class="form-control" />
            </div>
            <div class="mb-3">
                <label class="form-label">下架時間</label>
                <input type="datetime-local" v-model="form.removeTime" class="form-control" required />
            </div>

            <!-- 附件編輯 -->
            <div class="mb-3">
                <label class="form-label">附件</label>
                <ul class="list-group mb-2">
                    <li v-for="(att, i) in form.existingAttachments" :key="i"
                        class="list-group-item d-flex justify-content-between align-items-center fs-6">
                        {{ att.fileName }}
                        <button type="button" class="btn-remove" @click="removeExistingAttachment(i)"
                            style="font-size: 80%;">移除</button>
                    </li>
                </ul>
                <input type="file" multiple @change="handleNewFiles" class="form-control" ref="fileInput" />
            </div>

            <!-- 投票區塊 -->

            <div v-if="bulletin.poll" class="my-4 text-dark ">
                <div class="poll-card">

                    <h6 class="text-center">目前投票情形：{{ bulletin.poll.title }}</h6>
                    <p class="text-center " style="font-size: 80%;color: #828282;">( 僅供查看，需修改請於公告列表點選「修改投票」)</p>
                    <div class="d-flex justify-content-end mx-3">

                        <!-- <button type="button" class="btn btn-secondary" @click="openPollModal">修改投票內容</button> -->
                    </div>
                    <!-- <EditPollModal v-model:visible="visiblePollEdit" :poll="bulletin.poll" :pollBackup="pollBackup" /> -->

                    <BarChart :labels="pollLabels" :data="pollVotes" />
                </div>
            </div>

            <div class="text-end mb-2 pb-4">
                <button type="btn" class="btn btn-primary">送出修改</button>
                <button type="reset" class="btn-poll  mx-2" @click="$emit('update:visible', false)">取消</button>
            </div>
        </form>
    </ModalWrapper>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import BarChart from '@/components/bulletin/BarChart.vue'
// import EditPollModal from '@/components/bulletin/EditPollModal.vue'

// const visiblePollEdit = ref(false)

const fileInput = ref(null) // 綁定 ref
const addPoll = ref(false)
const pollBackup = ref(null)

const pollLabels = computed(() => props.bulletin?.poll?.options?.map(opt => opt.text) || [])
const pollVotes = computed(() => props.bulletin?.poll?.options?.map(opt => opt.votesCount || 0) || [])


const props = defineProps({
    visible: Boolean,
    bulletin: Object,
    categoryList: Array,
    communityId: Number,
    usersId: Number
})
const emit = defineEmits(['update:visible', 'updated'])

const form = ref({
    title: '',
    description: '',
    categoryName: '',
    postTime: '',
    removeTime: '',
    existingAttachments: [],
    newAttachments: [],
    postStatus: false,
    poll: null,
    isPinned: false
})

watch(() => props.bulletin, (val) => {
    if (val) {
        form.value.title = val.title
        form.value.description = val.description
        form.value.categoryName = props.categoryList.find(c => c.name === val.categoryName)?.name || null
        form.value.postTime = val.postTime?.slice(0, 16) || ''
        form.value.removeTime = val.removeTime?.slice(0, 16) || ''
        form.value.existingAttachments = val.attachments || []
        form.value.newAttachments = []
        form.value.postStatus = val.postStatus
        form.value.poll = val.poll
        form.value.isPinned = val.isPinned
    }
}, { immediate: true })




function handleNewFiles(e) {
    const files = Array.from(e.target.files)
    //console.log(files);
    files.forEach(file => {
        form.value.existingAttachments.push({
            fileName: file.name,
            mimeType: file.type,
            file: file, // 原始檔案，後續轉 base64 用
            fileDataBase64: null,
            isNew: true
        })

    })

    // 清空 input，避免無法重選同樣檔案
    if (fileInput.value) {
        fileInput.value.value = ''
    }
}



function removeExistingAttachment(index) {
    form.value.existingAttachments.splice(index, 1)
}



function submitEdit() {
    // 對所有有 file 的附件轉 base64
    const fileReads = form.value.existingAttachments
        .filter(att => att.file)
        .map(att => {
            return new Promise(resolve => {
                const reader = new FileReader()
                reader.onload = () => {
                    // 寫入原 att 的 base64
                    att.fileDataBase64 = reader.result.split(',')[1]
                    resolve()
                }
                reader.readAsDataURL(att.file)
            })
        })

    Promise.all(fileReads).then(() => {
        const data = {
            title: form.value.title,
            description: form.value.description,
            category: {
                name: form.value.categoryName
            },
            community: {
                communityId: props.communityId
            },
            user: {
                usersId: props.usersId
            },
            postTime: form.value.postTime,
            removeTime: form.value.removeTime,
            attachments: form.value.existingAttachments.map(att => ({
                fileName: att.fileName,
                mimeType: att.mimeType,
                fileDataBase64: att.fileDataBase64 || null,
                isNew: att.isNew || false

            })),
            poll: form.value.poll || null,
            postStatus: form.value.postStatus,
            isPinned: form.value.isPinned
        }

        //console.log('送出資料', data)

        axios.put(`/api/bulletin/${props.bulletin.id}`, data)
            .then(() => {
                emit('updated')
                emit('update:visible', false)
                Swal.fire({
                    title: '成功',
                    text: '修改成功',
                    icon: 'success',
                    confirmButtonText: '確定',
                    timer: 1500
                })
            })
            .catch(error => {
                console.error(error)
                Swal.fire({
                    title: '失敗',
                    text: '修改失敗',
                    icon: 'error',
                    confirmButtonText: '確定'
                })
            })
    })
}







</script>

<style scoped>
:deep(.form-control),
:deep(.form-select) {
    background-color: #fff !important;
    color: #161616 !important;
}

.list-group-item {
    font-size: 0.95rem;
}

.btn-poll,
.btn-remove {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 12px 24px;
    border: none;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #cfd4ec 0%, #d7d7d7 100%);
    color: rgb(127, 127, 127);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.btn-remove {
    padding: 8px 16px;
    background: #9cc8f3;
    color: rgb(60, 60, 60);
}

.btn-poll::before,
.btn-remove::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.5s;
}

.btn-poll:hover::before,
.btn-remove:hover::before {
    left: 100%;
}

.btn-poll:hover,
.btn-remove:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.btn-poll:active,
.btn-remove:active {
    transform: translateY(0);
}
</style>