<template>
    <div class="container py-4">
        <h1 class="h4 fw-bold mb-4">常見問題 (FAQ)</h1>

        <!-- 分類篩選 + 查詢 -->
        <div class="row my-3 align-items-center">
            <!-- 左側：分類按鈕（佔 8 欄） -->
            <div class="col-md-8 d-flex flex-wrap gap-2">
                <button @click="selectCategory('全部')" :class="buttonClass('全部')">全部</button>
                <button v-for="cat in categories" :key="cat" @click="selectCategory(cat)" :class="buttonClass(cat)">
                    {{ cat }}
                </button>
            </div>

            <!-- 右側：搜尋欄位（佔 4 欄） -->
            <div class="col-md-4 mt-2 mt-md-0">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="輸入關鍵字查詢" v-model="searchKeyword"
                        @keyup.enter="searchFaqs" />
                    <button class="btn  btn-primary" type="button" @click="searchFaqs">
                        查詢
                    </button>
                    <button class="btn  btn-primary" @click="clearSearch">清除搜尋</button>

                </div>
            </div>
        </div>





        <!-- FAQ Accordion -->
        <div v-if="loading">載入中...</div>
        <div v-else>
            <div v-if="filteredFaqs.length === 0" class="text-muted">無符合資料</div>

            <!-- FAQ Accordion 區塊 -->
            <div class="accordion" id="faqAccordion">
                <div class="accordion-item" v-for="faq in paginatedFaqs" :key="faq.id">
                    <h2 class="accordion-header" :id="`heading-${faq.id}`">
                        <button class="accordion-button  collapsed" type="button" data-bs-toggle="collapse"
                            :data-bs-target="`#collapse-${faq.id}`" aria-expanded="false"
                            :aria-controls="`collapse-${faq.id}`"><span class="badge rounded-pill me-2"
                                :class="getCategoryBadgeClass(faq.category)">
                                {{ faq.category }}
                            </span>
                            <span class="fs-6">{{ faq.question }}</span></button>
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
import axios from '@/plugins/axios'

const fullfaqList = ref([])
const faqList = ref([])
const categories = ref([])
const loading = ref(true)

const selectedCategory = ref('全部')
const page = ref(1)
const pageSize = 10
const searchKeyword = ref('')
const communityId = 1
const userId = 1

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
            axios.get('/api/faq'),
            axios.get(`/api/faq/${communityId}/category`)
        ])

        const categoryOrder = categoryRes.data || []
        categories.value = categoryOrder

        if (faqRes.data.success) {
            // 只保留 postStatus 為 true 的資料
            const filtered = faqRes.data.list.filter(faq => faq.postStatus === true)

            // 再根據分類順序排序
            fullfaqList.value = filtered.sort((a, b) => {
                const aIndex = categoryOrder.indexOf(a.category)
                const bIndex = categoryOrder.indexOf(b.category)
                return aIndex - bIndex
            })

            faqList.value = fullfaqList.value
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
        const res = await axios.post('/api/faq/searchbykeyword', requestBody)
        if (res.data.success) {
            // ✅ 重新依分類順序排序
            const categoryOrder = categories.value
            faqList.value = res.data.list.sort((a, b) => {
                const aIndex = categoryOrder.indexOf(a.category)
                const bIndex = categoryOrder.indexOf(b.category)
                return aIndex - bIndex
            })
            page.value = 1
        }
    } catch (error) {
        console.error('查詢 FAQ 失敗:', error)
    } finally {
        loading.value = false
    }
}
const clearSearch = () => {
    searchKeyword.value = ''
    selectedCategory.value = '全部'
    faqList.value = [...fullfaqList.value]
    page.value = 1
}

const selectCategory = (cat) => {
    selectedCategory.value = cat
    page.value = 1

    if (cat === '全部' && searchKeyword.value.trim() === '') {
        // ✅ 如果沒輸入關鍵字且選的是全部 → 還原初始 faqList
        faqList.value = [...fullfaqList.value]
    } else {
        // ✅ 其他情況就重新查詢
        searchFaqs()
    }
}


const formatDate = (dateStr) => new Date(dateStr).toLocaleString()

const prevPage = () => {
    if (page.value > 1) page.value--
}

const nextPage = () => {
    if (page.value < totalPages.value) page.value++
}

const buttonClass = (cat) =>
    `btn-class ${selectedCategory.value === cat ? 'btn-outline-class' : ''}`

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

<style scoped>
.btn {
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
    /* transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); */
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.btn-class {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 5px 10px !important;
    border: none;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 400;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background: rgb(135, 175, 212);

    color: white;
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.btn-outline-class {

    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
    /* 可有可無：藍色光暈感 */
    background: rgb(138, 193, 245);
    background: linear-gradient(135deg, #9eace9 0%, #764ba2 100%);
    font-weight: 600;


}





.btn:hover {
    transform: translateY(0px);
    /* box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4); */
}
</style>
