<template>
    <div class="container py-4">
        <h1 class="h4 fw-bold mb-4">常見問題 (FAQ)</h1>

        <!-- 分類篩選 + 查詢 -->
        <div class="mb-3 d-flex justify-content-between align-items-center flex-wrap">
            <!-- 左側：分類按鈕 -->
            <div class="d-flex flex-wrap gap-2">
                <button @click="selectCategory('全部')" :class="buttonClass('全部')">全部</button>
                <button v-for="cat in categories" :key="cat" @click="selectCategory(cat)" :class="buttonClass(cat)">
                    {{ cat }}
                </button>
            </div>

            <!-- 右側：查詢欄位 -->
            <div class="input-group mt-2 mt-md-0 w-50">
                <input type="text" class="form-control" placeholder="輸入關鍵字查詢" v-model="searchKeyword"
                    @keyup.enter="searchFaqs" />
                <button class="btn btn-outline-secondary" type="button" @click="searchFaqs">
                    查詢
                </button>
            </div>
        </div>




        <!-- FAQ Accordion -->
        <div v-if="loading">載入中...</div>
        <div v-else>
            <div v-if="filteredFaqs.length === 0" class="text-muted">無符合資料</div>

            <div class="accordion" id="faqAccordion">
                <div class="accordion-item" v-for="faq in paginatedFaqs" :key="faq.id">
                    <h2 class="accordion-header" :id="`heading-${faq.id}`">
                        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            :data-bs-target="`#collapse-${faq.id}`" aria-expanded="false"
                            :aria-controls="`collapse-${faq.id}`">
                            <h5>
                                <span :class="['badge', 'rounded-pill', getCategoryBadgeClass(faq.category)]">
                                    {{ faq.category }}
                                </span>
                            </h5>
                            &nbsp;{{ faq.question }}
                        </button>
                    </h2>
                    <div :id="`collapse-${faq.id}`" class="accordion-collapse collapse"
                        :aria-labelledby="`heading-${faq.id}`" data-bs-parent="#faqAccordion">
                        <div class="accordion-body">
                            <p class="mb-2">{{ faq.answer }}</p>
                            <p class="small text-muted">
                                分類：{{ faq.category }} | 更新時間：{{ formatDate(faq.lastModified) }}
                            </p>
                            <p class="small text-muted">關鍵字：{{ faq.keywords.join(', ') }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 分頁控制 -->
            <nav class="mt-4" v-if="totalPages > 1">
                <ul class="pagination justify-content-center">
                    <li class="page-item" :class="{ disabled: page === 1 }">
                        <a class="page-link" href="#" @click.prevent="prevPage">上一頁</a>
                    </li>
                    <li class="page-item disabled">
                        <span class="page-link">第 {{ page }} 頁 / 共 {{ totalPages }} 頁</span>
                    </li>
                    <li class="page-item" :class="{ disabled: page === totalPages }">
                        <a class="page-link" href="#" @click.prevent="nextPage">下一頁</a>
                    </li>
                </ul>
            </nav>
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
const pageSize = 5
const searchKeyword = ref('')

// 計算顯示用 FAQ
const filteredFaqs = computed(() =>
    selectedCategory.value === '全部'
        ? faqList.value
        : faqList.value.filter(faq => faq.category === selectedCategory.value)
)

const totalPages = computed(() =>
    Math.ceil(filteredFaqs.value.length / pageSize)
)

const paginatedFaqs = computed(() => {
    const start = (page.value - 1) * pageSize
    return filteredFaqs.value.slice(start, start + pageSize)
})

// 🔹取得 FAQ 資料（初始用）
const fetchFaqs = async () => {
    loading.value = true
    try {
        const [faqRes, categoryRes] = await Promise.all([
            axios.get('http://localhost:8080/api/faq'),
            axios.get(`http://localhost:8080/api/faq/1/category`)
        ])

        const categoryOrder = categoryRes.data || []
        categories.value = categoryOrder

        if (faqRes.data.success) {
            faqList.value = faqRes.data.list.sort((a, b) => {
                const aIndex = categoryOrder.indexOf(a.category)
                const bIndex = categoryOrder.indexOf(b.category)
                return aIndex - bIndex
            })
        }
    } catch (error) {
        console.error('取得FAQ或分類失敗:', error)
    } finally {
        loading.value = false
    }
}

// 🔹查詢 FAQ（分類/關鍵字）
const searchFaqs = async () => {
    loading.value = true

    const requestBody = {}
    if (selectedCategory.value !== '全部') {
        requestBody.category = { name: selectedCategory.value }
    }
    if (searchKeyword.value.trim() !== '') {
        requestBody.keywords = searchKeyword.value.trim()
    }

    try {
        const res = await axios.post('http://localhost:8080/api/faq/searchby', requestBody)
        if (res.data.success) {
            faqList.value = res.data.list
            page.value = 1
        }
    } catch (error) {
        console.error('查詢 FAQ 失敗:', error)
    } finally {
        loading.value = false
    }
}

const selectCategory = (cat) => {
    selectedCategory.value = cat
    page.value = 1
    searchFaqs()
}

const formatDate = (dateStr) => new Date(dateStr).toLocaleString()

const prevPage = () => {
    if (page.value > 1) page.value--
}

const nextPage = () => {
    if (page.value < totalPages.value) page.value++
}

const buttonClass = (cat) =>
    `btn ${selectedCategory.value === cat ? 'btn-primary' : 'btn-outline-primary'}`

const getCategoryBadgeClass = (category) => {
    const index = categories.value.indexOf(category)
    const colorIndex = index % 3
    switch (colorIndex) {
        case 0: return 'bg-danger'
        case 1: return 'bg-success'
        case 2: return 'bg-warning'
        default: return 'bg-secondary'
    }
}

onMounted(fetchFaqs)
</script>
