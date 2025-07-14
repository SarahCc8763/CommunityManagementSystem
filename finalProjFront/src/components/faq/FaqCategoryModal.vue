<template>
    <ModalWrapper :visible="visible" @update:visible="val => emit('update:visible', val)">
        <div class="mb-3 d-flex justify-content-center align-items-center">
            <input v-model="newCategory" type="text" class="form-cat me-2 fs-5" placeholder="新增分類名稱" />
            <button class="btn btn-primary" @click="addCategory">新增</button>
        </div>

        <!-- 類別 badge 列表 -->
        <div class="d-flex flex-wrap gap-2 fs-6">
            <div v-for="cat in categoryList" :key="cat.id" class="badge-wrapper d-flex align-items-center">
                <input v-model="cat.name" class="badge-input fs-6" @focus="handleFocus(cat.id)" @blur="handleBlur" />
                <transition name="fade">
                    <button v-if="focusedId === cat.id" type="button" class="btn-checkmark ms-1 px-2 py-0"
                        @click="updateCategory(cat)">
                        ✔
                    </button>
                </transition>
                <button type="button" class="btn-close ms-1" aria-label="Close"
                    @click="deleteCategory(cat.id)"></button>
            </div>
        </div>
    </ModalWrapper>
</template>
<script setup>
import { ref, watch } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'

const props = defineProps({
    visible: Boolean,
    communityId: Number
})
const emit = defineEmits(['update:visible', 'updated'])

const newCategory = ref('')
const categoryList = ref([])
const focusedId = ref(null)

watch(() => props.visible, (val) => {
    if (val) fetchCategories()
})

const fetchCategories = async () => {
    const res = await axios.get(`/api/faq/${props.communityId}/category/list`)
    categoryList.value = res.data
}

const addCategory = async () => {
    if (!newCategory.value.trim()) return
    await axios.post('/api/faq/category', {
        name: newCategory.value.trim(),
        community: { communityId: props.communityId },
    })
    newCategory.value = ''
    fetchCategories()
    emit('updated')
}

const updateCategory = async (cat) => {
    await axios.put(`/api/faq/category/${cat.id}`, {
        name: cat.name,
        community: { communityId: props.communityId },
    })
    emit('updated')
}

const deleteCategory = async (id) => {
    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        text: '刪除會將該分類所屬所有FAQ一起刪除，且將無法恢復！',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    })

    if (result.isConfirmed) {
        try {
            await axios.delete(`/api/faq/category/${id}`)
            await fetchCategories()
            emit('updated')
            Swal.fire({ title: '刪除成功', icon: 'success', timer: 1500, showConfirmButton: false })
        } catch (err) {
            Swal.fire({
                title: '刪除失敗',
                text: err.response?.data?.result || '伺服器錯誤',
                icon: 'error',
                timer: 1500,
                showConfirmButton: false
            })
        }
    }
}

const handleFocus = (id) => { focusedId.value = id }
const handleBlur = () => { setTimeout(() => (focusedId.value = null), 200) }
</script>


<style scoped>
.badge-wrapper {
    background-color: #d5eeff;
    padding: 4px 8px;
    border-radius: 999px;
    display: inline-flex;
    align-items: center;

}

.badge-input {
    border: none;
    background-color: transparent;
    width: auto;
    max-width: 120px;
    font-size: 0.875rem;
    padding: 0;
    margin: 0;
    outline: none;
    color: #000000;
}

.badge-input:focus {
    background-color: white;
    border: 1px solid #ced4da;
    border-radius: 5px;
    padding: 2px 4px;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

.btn-checkmark {
    color: rgb(228, 101, 101);
    border: none;
    font-weight: bold;
    border-radius: 50%;
    width: 26px;
    height: 26px;
    font-size: 120%;
    text-align: center;
    line-height: 22px;
    padding: 0;
    transition: background-color 0.2s;
}

.form-cat {
    background: #000000;
    color: #ffffff;
    border: 1px solid #2c3440;
    border-radius: 5px;
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