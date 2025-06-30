<template>
    <div class="p-6">
        <h1 class="text-2xl font-bold mb-4">常見問題 (FAQ)</h1>

        <!-- 分類標籤 -->
        <div class="mb-4 flex flex-wrap gap-2">
            <button @click="selectCategory('全部')" :class="buttonClass('全部')">全部</button>
            <button v-for="cat in categories" :key="cat" @click="selectCategory(cat)" :class="buttonClass(cat)">{{ cat
                }}</button>
        </div>

        <div v-if="loading">載入中...</div>
        <div v-else>
            <div v-if="filteredFaqs.length === 0" class="text-gray-500">無符合資料</div>

            <div v-for="faq in paginatedFaqs" :key="faq.id" class="mb-6 p-4 border rounded shadow">
                <h2 class="text-lg font-semibold">{{ faq.question }}</h2>
                <p class="text-gray-700 mt-2">{{ faq.answer }}</p>
                <p class="text-sm text-gray-500 mt-1">分類：{{ faq.category }} | 更新時間：{{ formatDate(faq.lastModified) }}
                </p>
                <div class="text-sm text-gray-500 mt-1">關鍵字：{{ faq.keywords.join(', ') }}</div>
            </div>

            <!-- 分頁控制 -->
            <div class="flex justify-center items-center space-x-2 mt-4" v-if="filteredFaqs.length > 0">
                <button @click="prevPage" :disabled="page === 1" class="px-3 py-1 border rounded">上一頁</button>
                <span>第 {{ page }} 頁 / 共 {{ totalPages }} 頁</span>
                <button @click="nextPage" :disabled="page === totalPages" class="px-3 py-1 border rounded">下一頁</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const faqList = ref([])
const categories = ref([])
const loading = ref(true)

const selectedCategory = ref('全部')
const page = ref(1)
const pageSize = 10

// 計算符合分類的 FAQ
const filteredFaqs = computed(() => {
    return selectedCategory.value === '全部'
        ? faqList.value
        : faqList.value.filter(faq => faq.category === selectedCategory.value)
})

const totalPages = computed(() =>
    Math.ceil(filteredFaqs.value.length / pageSize)
)

const paginatedFaqs = computed(() => {
    const start = (page.value - 1) * pageSize
    return filteredFaqs.value.slice(start, start + pageSize)
})

const fetchFaqs = async () => {
    try {
        const [faqRes, categoryRes] = await Promise.all([
            axios.get('http://localhost:8080/api/faq'),
            axios.get('http://localhost:8080/api/faq/category')
        ])
        if (faqRes.data.success) faqList.value = faqRes.data.list
        categories.value = categoryRes.data || []
    } catch (error) {
        console.error('取得FAQ或分類失敗:', error)
    } finally {
        loading.value = false
    }
}

const selectCategory = (cat) => {
    selectedCategory.value = cat
    page.value = 1 // 切換分類時回到第一頁
}

const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleString()
}

const prevPage = () => {
    if (page.value > 1) page.value--
}

const nextPage = () => {
    if (page.value < totalPages.value) page.value++
}

const buttonClass = (cat) =>
    `px-3 py-1 rounded border ${selectedCategory.value === cat
        ? 'bg-blue-600 text-white'
        : 'bg-white text-gray-700 hover:bg-gray-100'
    }`

onMounted(fetchFaqs)
</script>
