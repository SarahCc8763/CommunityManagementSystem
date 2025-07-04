<template>
    <ModalWrapper :visible="props.show" @update:visible="val => emit('update:show', val)">
        <div class="mb-3 d-flex">
            <input v-model="newCategory" type="text" class="form-control me-2" placeholder="新增分類名稱" />
            <button class="btn btn-primary" @click="addCategory">新增</button>
        </div>

        <ul class="list-group">
            <li v-for="cat in categoryList" :key="cat.categoryId"
                class="list-group-item d-flex justify-content-between align-items-center">
                <div class="flex-grow-1 me-2">
                    <input v-model="cat.categoryName" class="form-control" @blur="updateCategory(cat)" />
                </div>
                <button class="btn btn-sm btn-outline-danger" @click="deleteCategory(cat.categoryId)">刪除</button>
            </li>
        </ul>
    </ModalWrapper>
</template>

<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'

const props = defineProps({
    show: Boolean,
    communityId: Number
})
const emit = defineEmits(['update:show', 'updated', 'close'])

const newCategory = ref('')
const categoryList = ref([])

watch(() => props.show, (val) => {
    if (val) fetchCategories()
})

async function fetchCategories() {
    const res = await axios.get('http://localhost:8080/api/bulletin/categories')
    categoryList.value = res.data
}

async function addCategory() {
    if (!newCategory.value.trim()) return
    await axios.post('http://localhost:8080/api/bulletin/category', {
        name: newCategory.value.trim(),
        community: { communityId: props.communityId },
    })
    newCategory.value = ''
    fetchCategories()
    emit('updated')
}

async function updateCategory(cat) {
    await axios.put(`http://localhost:8080/api/bulletin/category/${cat.categoryId}`, {
        name: cat.categoryName,
        community: { communityId: props.communityId },
    })
    emit('updated')
}

async function deleteCategory(id) {
    await axios.delete(`http://localhost:8080/api/bulletin/category/${id}`)
    fetchCategories()
    emit('updated')
}
</script>
