<template>
    <ModalWrapper :visible="visible" @update:visible="v => emit('update:visible', v)">
        <h4 class="fw-bold mb-3 text-center">{{ mode === 'edit' ? '編輯 FAQ' : '新增 FAQ' }}</h4>

        <div class="fs-5">
            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" v-model="form.postStatus" id="postSwitch" />
                <label class="form-check-label" for="postSwitch">是否公開</label>
            </div>
            <div class="mb-3">
                <label class="form-label">問題 <span class="text-danger">*</span></label>
                <input v-model="form.question" type="text" class="form-control" placeholder="輸入問題" />
            </div>

            <div class="mb-3">
                <label class="form-label">解答 <span class="text-danger">*</span></label>
                <textarea v-model="form.answer" class="form-control" rows="4" placeholder="輸入解答"></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">分類 <span class="text-danger">*</span></label>
                <select v-model="form.category.name" class="form-select">
                    <option disabled value="">請選擇分類</option>
                    <option v-for="cat in categoryOptions" :key="cat" :value="cat">{{ cat }}</option>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">關鍵字（以半形逗號 , 分隔）</label>
                <input v-model="form.keywords" type="text" class="form-control" placeholder="ex: 電梯,水管,住戶" />
            </div>


            <div class="d-flex justify-content-end mt-4">
                <button class="btn btn-secondary me-2" @click="emit('update:visible', false)">取消</button>
                <button class="btn btn-primary" @click="submitForm">送出</button>
            </div>
        </div>
    </ModalWrapper>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'

const props = defineProps({
    visible: Boolean,
    faqData: Object, // 若為編輯時傳入
    communityId: Number,
    categoryOptions: Array
})
const emit = defineEmits(['update:visible', 'submitted'])

const mode = computed(() => props.faqData ? 'edit' : 'create')

const form = ref({
    question: '',
    answer: '',
    postStatus: false,
    category: { name: '' },
    keywords: '',
    community: { communityId: props.communityId }
})

watch(() => props.visible, (val) => {
    if (val && props.faqData) {
        // 編輯模式：複製原資料
        const { id, question, answer, postStatus, category, keywords } = props.faqData
        form.value = {
            question,
            answer,
            postStatus,
            category: { name: category },
            keywords: keywords.join(','),
            community: { communityId: props.communityId || 1 }
        }
        //console.log(form.value);
    } else if (val) {
        // 清空表單（新增）
        form.value = {
            question: '',
            answer: '',
            postStatus: false,
            category: { name: '' },
            keywords: '',
            community: { communityId: props.communityId || 1 }
        }
        //console.log(form.value);
    }
})

const validate = () => {
    if (!form.value.question.trim() || !form.value.answer.trim() || !form.value.category.name) {
        Swal.fire({
            icon: 'warning',
            title: '請填寫所有必填欄位'
        })
        return false
    }
    return true
}

const submitForm = async () => {
    if (!validate()) return

    try {
        const payload = { ...form.value }
        const url = mode.value === 'edit'
            ? `/api/faq/${props.faqData.id}`
            : '/api/faq'

        const method = mode.value === 'edit' ? 'put' : 'post'
        const res = await axios[method](url, payload)

        if (res.data?.success) {
            Swal.fire({
                icon: 'success',
                title: '儲存成功',
                timer: 1500
            })
            emit('submitted')
            emit('update:visible', false)
        } else {
            throw new Error('儲存失敗')
        }
    } catch (err) {
        //console.error('送出失敗', err)
        Swal.fire({
            icon: 'error',
            title: '儲存失敗',
            text: err.message || '伺服器錯誤'
        })
    }
}
</script>

<style scoped>
.form-label {
    font-weight: 600;
}

:deep(.modal-content-wrapper) {
    background: linear-gradient(to bottom right, #2c3b4f, #3e3560) !important;
    color: rgb(255, 255, 255) !important;
    border: 1px solid rgb(80, 71, 111) !important;
    border-radius: 20px !important;
    width: 80%;
    max-width: 800px;
    border-radius: 12px;
    height: 70%;
    top: 0;

}
</style>
