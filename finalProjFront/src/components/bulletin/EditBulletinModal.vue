<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)" title="編輯公告">
        <form @submit.prevent="submitEdit">
            <div class="mb-3">
                <label class="form-label">標題</label>
                <input v-model="form.title" class="form-control " required />
            </div>

            <div class="mb-3">
                <label class="form-label">內容</label>
                <textarea v-model="form.description" class="form-control" rows="10" required></textarea>
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
                <label class="form-label">下架時間</label>
                <input type="datetime-local" v-model="form.removeTime" class="form-control" required />
            </div>

            <!-- 附件編輯 -->
            <div class="mb-3">
                <label class="form-label">附件</label>
                <ul class="list-group mb-2">
                    <li v-for="(att, i) in form.existingAttachments" :key="i"
                        class="list-group-item d-flex justify-content-between align-items-center">
                        {{ att.fileName }}
                        <button type="button" class="btn btn-sm btn-outline-danger"
                            @click="removeExistingAttachment(i)">刪除</button>
                    </li>
                </ul>
                <input type="file" multiple @change="handleNewFiles" class="form-control" ref="fileInput" />
            </div>

            <!-- 投票區塊 -->

            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <!-- 還要新增修改投票區塊的功能 -->
            <div v-if="bulletin.poll" class="my-4 text-dark ">
                <div class="poll-card">

                    <h6 class="text-center">投票結果：{{ bulletin.poll.title }}</h6>
                    <BarChart :labels="pollLabels" :data="pollVotes" />
                </div>
            </div>

            <div class="text-end">
                <button type="submit" class="btn btn-primary">送出修改</button>
            </div>
        </form>
    </ModalWrapper>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import axios from 'axios'
import Swal from 'sweetalert2'
import BarChart from '@/components/bulletin/BarChart.vue'

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
    removeTime: '',
    existingAttachments: [],
    newAttachments: [],
    isNew: false,
    poll: null
})

watch(() => props.bulletin, (val) => {
    if (val) {
        form.value.title = val.title
        form.value.description = val.description
        form.value.categoryName = props.categoryList.find(c => c.name === val.categoryName)?.name || null
        form.value.removeTime = val.removeTime?.slice(0, 16) || ''
        form.value.existingAttachments = val.attachments || []
        form.value.newAttachments = []
        form.value.poll = val.poll
        pollBackup.value = val.poll ? { ...val.poll } : null  // ⬅️ 備份原始 poll
        addPoll.value = !!val.poll
    }
}, { immediate: true })

watch(addPoll, (val) => {
    if (val) {
        // 使用者打開 checkbox，若有備份就還原，沒有就初始化
        form.value.poll = pollBackup.value ?? {
            title: '',
            isMultiple: false,
            start: '',
            end: '',
            options: [
                { text: '' },
                { text: '' }
            ]
        }
    } else {
        // 關閉時備份 poll，然後清除
        pollBackup.value = form.value.poll
        form.value.poll = null
    }
})



function handleNewFiles(e) {
    const files = Array.from(e.target.files)
    console.log(files);
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
            removeTime: form.value.removeTime,
            attachments: form.value.existingAttachments.map(att => ({
                fileName: att.fileName,
                mimeType: att.mimeType,
                fileDataBase64: att.fileDataBase64 || null,
                isNew: att.isNew || false,
                poll: att.poll || null
            }))
        }

        console.log('送出資料', data)

        axios.put(`http://localhost:8080/api/bulletin/${props.bulletin.id}`, data)
            .then(() => {
                emit('updated')
                emit('update:visible', false)
                Swal.fire({
                    title: '成功',
                    text: '修改成功',
                    icon: 'success',
                    confirmButtonText: '確定'
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
</style>