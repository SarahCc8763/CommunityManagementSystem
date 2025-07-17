<template>

    <div class="modal fade" id="feedbackModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">

                    <h5 class="modal-title fw-bolder text-center w-100">💬 聯絡客服</h5>

                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <img src="@/assets/images/feedback/postfeedback.jpg" alt="" width="100%">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label fw-bold">標題*</label>
                        <input type="text" v-model="form.title" class="form-control" />
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">內容*</label>
                        <textarea v-model="form.description" class="form-control" rows="4"></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">分類*</label>
                        <select v-model="form.category.id" class="form-select">
                            <option disabled value="">請選擇分類</option>
                            <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                                {{ cat.name }}
                            </option>
                        </select>
                    </div>

                    <label class="form-label fw-bold mt-3">上傳附件</label>
                    <div class="mb-3 border border-2 border-dashed rounded p-3 text-center" @dragover.prevent
                        @drop.prevent="handleDrop">
                        <p class="mb-2">拖拉檔案至此處以上傳</p>
                        <input type="file" multiple class="form-control" @change="handleFileChange" />
                        <div v-if="attachments.length > 0" class="mt-2 text-start">
                            <strong>已上傳檔案：</strong>
                            <ul class="list-unstyled">
                                <li v-for="(file, index) in attachments" :key="index"
                                    class="d-flex justify-content-between align-items-center">
                                    {{ file.fileName }}
                                    <button class="btn btn-sm btn-outline-danger ms-3"
                                        @click="removeAttachment(index)">✕</button>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button class="btn btn-primary" @click="submitFeedback">送出</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from '@/plugins/axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const form = reactive({
    id: null,
    title: '',
    description: '',
    category: { id: '' },
    user: { usersId: userStore.userId },
    community: { communityId: userStore.communityId },
})

const categories = ref([])
const attachments = ref([])

const fetchCategories = async () => {
    try {
        const res = await axios.get('/api/feedback/1/category')
        if (res.data && res.data.data) {
            categories.value = Object.entries(res.data.data).map(([id, name]) => ({ id: Number(id), name }))
        }
    } catch (error) {
        //console.error('取得分類失敗', error)
    }
}

const handleFileChange = (event) => {
    convertFiles(event.target.files)
    event.target.value = ''
}

const handleDrop = (event) => {
    const files = event.dataTransfer.files
    convertFiles(files)
}

const convertFiles = (fileList) => {
    Array.from(fileList).forEach((file) => {
        const reader = new FileReader()
        reader.onload = () => {
            const base64 = reader.result.split(',')[1]
            attachments.value.push({
                fileName: file.name,
                mimeType: file.type || 'file/' + file.name.split('.').pop(),
                fileDataBase64: base64,
            })
        }

        reader.readAsDataURL(file)
    })
}

const removeAttachment = (index) => {
    attachments.value.splice(index, 1)
}

const submitFeedback = async () => {
    if (!form.title.trim() || !form.description.trim() || !form.category.id) {
        swal.fire({
            icon: 'warning',
            title: '請填寫完整欄位',
            text: '標題、內容與分類為必填',
            confirmButtonText: '好的'
        })
        return
    }

    const payload = {
        ...form,
        attachments: attachments.value.length > 0 ? attachments.value : undefined,
    }

    try {
        const url = form.id ? `/api/feedback/${form.id}` : '/api/feedback'
        const method = form.id ? 'put' : 'post'

        const res = await axios[method](url, payload)
        //console.log(payload);
        // //console.log(res.data);
        if (res.data.success === true) {
            swal.fire({ icon: 'success', title: '成功', text: form.id ? '意見更新成功' : '意見已成功送出！', showConfirmButton: false, timer: 1500 }).then(() => {
                closeModal()
                resetForm()
                // location.reload();

            })
        } else {
            swal.fire({ icon: 'error', title: '失敗', text: res.data.message, showConfirmButton: false, timer: 1500 })
        }
    } catch (error) {
        swal.fire({ icon: 'error', title: '失敗', text: error.data?.message || '送出失敗', showConfirmButton: false, timer: 1500 })
        //console.error('送出失敗', error)
    }
}

const closeModal = () => {
    const modalEl = document.getElementById('feedbackModal')
    const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl)
    modalInstance.hide()

    setTimeout(() => {
        document.body.classList.remove('modal-open')
        const backdrop = document.querySelector('.modal-backdrop')
        if (backdrop) backdrop.remove()
    }, 300)
}

const resetForm = () => {
    form.id = null
    form.title = ''
    form.description = ''
    form.category.id = ''
    attachments.value = []
}

onMounted(fetchCategories)
onMounted(() => {

    // 當 modal 關閉時自動清空資料
    const modalEl = document.getElementById('feedbackModal')
    modalEl.addEventListener('hidden.bs.modal', () => {
        resetForm()
    })
    window.addEventListener('load-feedback-for-edit', (e) => {
        const feedback = e.detail
        //console.log(feedback);
        form.id = feedback.list[0].id
        form.title = feedback.list[0].title
        form.description = feedback.list[0].description
        form.category = feedback.list[0].category || ''
        attachments.value = feedback.list[0].attachments || []
    })
})
</script>

<style scoped>
.border-dashed {
    border-style: dashed !important;
}
</style>
