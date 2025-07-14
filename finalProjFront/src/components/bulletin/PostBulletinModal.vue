<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)" title="新增公告">
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
            <div class="text-dark mb-3">
                <div class="form-check form-switch mt-5 fs-5">
                    <input type="checkbox" id="addPoll" class="form-check-input " @click="pollInit">
                    <label for="addPoll" class="form-check-label text-dark ">需要建立投票</label>
                </div>

                <div v-if="addPoll" class="my-3 card-poll">
                    <label>投票標題</label>
                    <input type="text" name="" id="" v-model="form.poll.title" class="form-control" placeholder="投票標題">
                    <div class="form-check form-switch my-3">
                        <input type="checkbox" id="isMultiple" class="form-check-input " v-model="form.poll.isMultiple">
                        <label for="isMultiple" class="form-check-label text-dark ">是否為複選</label>
                    </div>
                    <!-- 動態選項區 -->
                    <div class="mb-3">
                        <label class="fw-bold">選項</label>
                        <div v-for="(option, index) in form.poll.options" :key="index"
                            class="d-flex align-items-center mb-2">
                            <input type="text" class="form-control me-2" v-model="form.poll.options[index].text"
                                placeholder="請輸入選項內容" style="width: 80%;" />
                            <button class="btn btn-poll" @click="removeOption(index)"
                                style="font-size: 80%;">刪除</button>
                        </div>
                        <div>
                            <button type="button" class="btn btn-poll mt-2" @click="addOption">＋
                                新增選項</button>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label>投票開始時間</label>
                        <input type="datetime-local" name="" id="" v-model="form.poll.start" class="form-control"
                            placeholder="投票開始時間">
                    </div>
                    <div class="mb-3">
                        <label>投票結束時間</label>
                        <input type="datetime-local" name="" id="" v-model="form.poll.end" class="form-control"
                            placeholder="投票截止時間">
                    </div>
                </div>
            </div>



            <div class="text-end">
                <button type="button" class="btn btn-primary" @click="submitEdit">送出修改</button>
                <button type="button" class="btn-poll-cancel  mx-2" @click="initPost">取消</button>

            </div>
        </form>
    </ModalWrapper>
</template>

<script setup>
import { ref, watch } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'

const fileInput = ref(null) // 綁定 ref
const addPoll = ref(false)

const props = defineProps({
    visible: Boolean,
    categoryList: Array,
    communityId: Number,
    usersId: Number
})
const emit = defineEmits(['update:visible', 'post'])

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

function initPost() {
    emit('update:visible', false)
    form.value = {
        title: '',
        description: '',
        categoryName: '',
        removeTime: '',
        existingAttachments: [],
        newAttachments: [],
        isNew: false,
        poll: null
    }
}

function pollInit() {
    if (!addPoll.value) {

        form.value.poll = {
            title: '',
            isMultiple: false,
            start: '',
            end: '',
            options: [
                { text: '' },
                { text: '' },
            ]
        }
        addPoll.value = true
    } else {
        addPoll.value = false
        form.value.poll = null
    }
}

// watch(() => from.value.poll?.options, val => {


// })
function addOption() {
    form.value.poll.options.push({ text: '' })
}

function removeOption(index) {
    form.value.poll.options.splice(index, 1)
}


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
async function submitEdit() {
    const fileReads = form.value.existingAttachments
        .filter(att => att.file)
        .map(att => {
            return new Promise(resolve => {
                const reader = new FileReader()
                reader.onload = () => {
                    att.fileDataBase64 = reader.result.split(',')[1]
                    resolve()
                }
                reader.readAsDataURL(att.file)
            })
        })

    await Promise.all(fileReads) // 🔁 等待全部 base64 完成

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
            isNew: att.isNew || false
        })),
        poll: form.value.poll || null
    }

    //console.log('送出資料', data)

    try {
        await axios.post(`/api/bulletin`, data)
        emit('post')
        emit('update:visible', false)
        await Swal.fire({
            icon: 'success',
            title: '成功',
            text: '發佈成功',
            timer: 1500
        })

    } catch (err) {
        console.error('送出失敗', err)
        Swal.fire({
            icon: 'error',
            title: '失敗',
            text: '發佈失敗'
        })
    }
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

.card-poll {
    width: 97%;
    background: rgba(255, 255, 255, 0.95);
    border: 1px solid rgba(0, 0, 0, 0.2);
    backdrop-filter: blur(20px);
    border-radius: 20px;
    padding: 24px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    /* border: 1px solid rgba(255, 255, 255, 0.2); */
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    /* animation: fadeIn 0.6s ease-out; */
}

.btn-poll {
    /* background-color: #fff; */
    color: #ffffff;
    border: 1px solid rgba(0, 0, 0, 0.2);
    border-radius: 20px;
    padding: 8px 16px;
}

.btn-poll-cancel {
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

.btn-poll-cancel::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.5s;
}

.btn-poll-cancel:hover::before {
    left: 100%;
}

.btn-poll-cancel:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.btn-poll-cancel:active {
    transform: translateY(0);
}
</style>