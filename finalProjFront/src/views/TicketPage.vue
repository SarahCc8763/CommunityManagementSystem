<template>
    <div ref="modal" class="modal fade" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content p-4">
                <div class="modal-header">
                    <h5 class="modal-title serif-title">建立報修單</h5>
                    <button type="button" class="btn-close" @click="hideModal"></button>
                </div>

                <div class="modal-body">
                    <form @submit.prevent="handleSubmit">
                        <!-- 標題 -->
                        <div class="mb-3">
                            <label class="form-label">標題</label>
                            <input type="text" v-model="form.title" class="form-control" placeholder="請填寫標題"/>
                        </div>

                        <!-- 問題種類 -->
                        <div class="mb-3">
                            <label class="form-label">問題種類</label>
                            <Multiselect v-model="formIssue.issueType" :options="issueOptions" :multiple="true"
                                :taggable="true" :close-on-select="false" :hide-selected="true" placeholder="請選擇或輸入問題種類"
                                tag-placeholder="新增項目" track-by="name" label="name" @tag="addNewTag" />
                        </div>

                        <!-- 描述 + 附件 -->
                        <div class="mb-3">
                            <label class="form-label">問題描述</label>
                            <QuillEditor style="min-height:150px" v-model:content="form.description" contentType="html"
                                class="form-control" placeholder=" 請描述報修區域..." />

                            <div class="upload-area mt-3 p-3 border rounded" @dragover.prevent
                                @drop.prevent="handleDrop">
                                <p>📎 拖曳圖片到這裡，或 <span @click="fileInput.click()" class="text-primary">點選上傳</span></p>
                                <input type="file" multiple ref="fileInput" class="d-none" @change="handleFileChange" />

                                <div class="preview-list d-flex flex-wrap gap-2 mt-2">
                                    <div class="position-relative" v-for="(file, index) in previews" :key="index">
                                        <img :src="file.url" alt="preview" class="rounded border"
                                            style="width: 100px; height: 100px; object-fit: cover;" />
                                        <button type="button" @click="removeFile(index)"
                                            class="btn btn-danger btn-sm position-absolute top-0 end-0 translate-middle p-0 rounded-circle"
                                            style="width: 24px; height: 24px;">&times;</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 按鈕區 -->
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Create</button>
                            <button type="button" class="btn btn-secondary" @click="hideModal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, defineExpose, defineEmits } from 'vue'
import axios from '@/plugins/axios'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import Multiselect from 'vue-multiselect'
import 'vue-multiselect/dist/vue-multiselect.min.css'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import { useUserStore } from '@/stores/UserStore'
import Swal from 'sweetalert2'



const userStore = useUserStore()

const emit = defineEmits(['close', 'created'])
const modal = ref(null)
let bsModal = null


const form = ref({
    project: '',
    title: '',
    description: '',
    priority: 'Medium'
})

const formIssue = ref({ issueType: [] })
const issueOptions = ref([])

onMounted(() => {
    axios.get('/IssueTypes')
        .then(res => {
            issueOptions.value = res.data.map(item => ({ name: item.issueTypeName }))
        })
        .catch(err => {
            console.error('載入 issueTypes 失敗', err)
        })

    bsModal = new bootstrap.Modal(modal.value)
})

function showModal() {
    bsModal?.show()
}
function hideModal() {
    bsModal?.hide()
    emit('close')
}

defineExpose({ showModal, hideModal })

async function addNewTag(newTag) {
    const newOption = { name: newTag }
    try {
        await axios.post('/IssueTypes', {
            issueTypeName: newTag
        })
        issueOptions.value.push(newOption)
        formIssue.value.issueType.push(newOption)
    } catch (err) {
        console.error('新增 issueType 失敗', err)
    }
}

const fileInput = ref(null)
const files = ref([])
const previews = ref([])

function handleFileChange(event) {
    const selected = Array.from(event.target.files)
    processFiles(selected)
}
function handleDrop(event) {
    const dropped = Array.from(event.dataTransfer.files)
    processFiles(dropped)
}
function processFiles(selected) {
    selected.forEach(file => {
        files.value.push(file)
        const reader = new FileReader()
        reader.onload = () => {
            previews.value.push({ file, url: reader.result })
        }
        reader.readAsDataURL(file)
    })
}
function removeFile(index) {
    files.value.splice(index, 1)
    previews.value.splice(index, 1)
}
function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => {
            const base64 = reader.result.split(',')[1]
            resolve(base64)
        }
        reader.onerror = reject
    })
}

async function handleSubmit() {
    if (!form.value.title || form.value.title.trim() === '') {
        Swal.fire({
        icon: 'warning',
        title: '欄位未填寫',
        text: '❗請填寫標題',
        confirmButtonText: '了解'
    })
        return
    }
    const payload = {
        reporterId: userStore.userId,
        communityId: userStore.communityId,
        title: form.value.title,
        issueDescription: form.value.description,
        status: 'to do',
        cost: 3000,
        notes: '住戶反映已多次發生',
        issueTypeNames: formIssue.value.issueType.map(i => i.name)
    }
    try {
        const ticketRes = await axios.post('/ticket', payload)
        const ticketResponse = ticketRes.data
        if (!ticketResponse.success) {
            alert('❌ 建立 ticket 失敗：' + ticketResponse.message)
            return
        }
        const ticketId = ticketResponse.data.id
        const base64Files = await Promise.all(files.value.map(async file => {
            const base64Data = await toBase64(file)
            return {
                fileName: file.name,
                base64Data,
                uploadedBy: 2,
                ticketId
            }
        }))
        if (base64Files.length > 0) {
            const uploadRes = await axios.post('/ticket-attachment/upload/base64/multiple', base64Files)
            const uploadResult = uploadRes.data
            if (uploadResult.success) {
                          Swal.fire({
                            icon: 'success',
                            title: '報修成功',
                            text: '✅ 報修單與附件上傳成功！',
                            confirmButtonText: 'OK'
                            })
                emit('created')
            } else {
                Swal.fire({
                icon: 'warning',
                title: '附件上傳失敗',
                text: '📎 報修單建立成功，但附件上傳失敗',
                confirmButtonText: '了解'
                })
                emit('created')
            }
        } else {
            Swal.fire({
            icon: 'success',
            title: '報修成功',
            text: '✅ 報修單建立成功（無附件）',
            confirmButtonText: 'OK'
            })
            emit('created')
        }
        form.value.title = ''
        form.value.description = ''
        formIssue.value.issueType = []
        files.value = []
        previews.value = []
        hideModal()
    } catch (err) {
        console.error('❌ 建立失敗', err)
        alert('建立失敗，請稍後再試')
    }
}
</script>