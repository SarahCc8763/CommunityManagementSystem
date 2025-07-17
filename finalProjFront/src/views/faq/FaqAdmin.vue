<template>
    <div class="container py-4">
        <h1 class="h4 fw-bold mb-4">常見問題 (FAQ)</h1>

        <!-- 分類篩選 + 查詢 -->
        <div class="row my-3 align-items-center">
            <!-- 上方新增與分類維護按鈕 -->
            <div class="text-end my-2">
                <button class="btn  me-2" @click="openAddModal">新增 FAQ</button>
                <button class="btn " @click="openCategoryModal">分類維護</button>
            </div>
            <!-- 左側分類按鈕 -->
            <div class="col-md-8 d-flex flex-wrap gap-2">
                <button @click="selectCategory('全部')" :class="buttonClass('全部')">全部</button>
                <button v-for="cat in categories" :key="cat" @click="selectCategory(cat)" :class="buttonClass(cat)">
                    {{ cat }}
                </button>
            </div>

            <!-- 右側搜尋欄位 -->
            <div class="col-md-4 mt-2 mt-md-0">
                <div class="input-group mt-2">
                    <input type="text" class="form-control" placeholder="輸入關鍵字查詢" v-model="searchKeyword"
                        @keyup.enter="searchFaqs" />
                    <button class="btn btn-primary" type="button" @click="searchFaqs">查詢</button>
                    <button class="btn btn-primary" @click="clearSearch">清除搜尋</button>
                </div>
            </div>
        </div>

        <!-- FAQ Accordion -->
        <div v-if="loading">載入中...</div>
        <div v-else>
            <div v-if="filteredFaqs.length === 0" class="text-muted">無符合資料</div>

            <div class="accordion " id="faqAccordion">
                <div class="accordion-item " v-for="faq in paginatedFaqs" :key="faq.id">
                    <h2 class="accordion-header" :id="`heading-${faq.id}`">
                        <button class="accordion-button fw-normal" :class="{ 'collapsed': faq.isExpanded }"
                            @click="toggle(faq)" type="button">
                            <!-- 內容 -->

                            <span class="badge rounded-pill me-2 fs-6 py-2 "
                                :class="getCategoryBadgeClass(faq.category)">
                                {{ faq.category }}
                            </span>
                            <span style="font-size: 120%;">{{ faq.question }} <small v-if="!faq.postStatus"
                                    class="text-secondary">( 未公開
                                    )</small></span>
                        </button>
                    </h2>
                    <div v-show="faq.isExpanded" :id="`collapse-${faq.id}`" class="accordion-collapse"
                        :aria-labelledby="`heading-${faq.id}`" style="font-size: 110%;">
                        <div class="accordion-body d-flex flex-column justify-content-between"
                            style="min-height: 180px;">
                            <!-- 主要內容區 -->
                            <div>
                                <p class="mb-2">{{ faq.answer }}</p>
                            </div>

                            <!-- 底部列：分類與按鈕在同一行 -->
                            <div class="mt-auto d-flex justify-content-between align-items-end flex-wrap gap-2">
                                <!-- 分類與關鍵字 -->
                                <div class="small" style="color: #BEBEBE;">
                                    分類：{{ faq.category }}　
                                    關鍵字：{{ faq.keywords?.join(', ') || '—' }}
                                </div>

                                <!-- 編輯與刪除按鈕 -->
                                <div class="d-flex gap-2">
                                    <button class="btn btn-sm btn-outline-primary"
                                        @click="openEditModal(faq)">編輯</button>
                                    <button class="btn btn-sm btn-outline-danger"
                                        @click="confirmDelete(faq.id)">刪除</button>
                                </div>
                            </div>
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

        <!-- FAQ 表單 Modal -->
        <FaqFormModal :visible="showFaqModal" @update:visible="showFaqModal = $event" :faqData="editingFaq"
            :communityId="1" :categoryOptions="categories" @submitted="fetchFaqs" />
        <FaqCategoryModal :visible="showCategoryModal" @update:visible="showCategoryModal = $event" :communityId="1"
            @updated="fetchFaqs" />

    </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick, watch } from 'vue'

import FaqFormModal from '@/components/faq/FaqFormModal.vue'
import FaqCategoryModal from '@/components/faq/FaqCategoryModal.vue'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'



const userStore = useUserStore()
const userId = userStore.userId || 0 // 假設當前使用者 id
const communityId = userStore.communityId || 0 // 假設當前社區 ID
import axios from '@/plugins/axios'


const fullfaqList = ref([])
const faqList = ref([])
const categories = ref([])
const loading = ref(true)

const selectedCategory = ref('全部')
const page = ref(1)
const pageSize = 10
const searchKeyword = ref('')

const showFaqModal = ref(false)
const editingFaq = ref(null)
const showCategoryModal = ref(false)


const openEditModal = (faq) => {
    editingFaq.value = { ...faq }
    showFaqModal.value = true
}

const openAddModal = () => {
    editingFaq.value = null
    showFaqModal.value = true
}

const openCategoryModal = () => {
    showCategoryModal.value = true
}

const confirmDelete = async (faqId) => {
    const result = await Swal.fire({
        title: '確定要刪除嗎？',
        text: '刪除後將無法恢復',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    })

    if (result.isConfirmed) {
        try {
            await axios.delete(`/api/faq/${faqId}`)
            await fetchFaqs()
            Swal.fire('刪除成功', '', 'success')
        } catch (err) {
            //console.error(err)
            Swal.fire('刪除失敗', '', 'error')
        }
    }
}


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

const toggle = (targetFaq) => {
    faqList.value.forEach(faq => {
        if (faq === targetFaq) {
            faq.isExpanded = !faq.isExpanded // 切換自己
        } else {
            faq.isExpanded = false // 其他關掉
        }
    })
}


// 🔹取得 FAQ 資料（初始用）
const fetchFaqs = async () => {
    loading.value = true
    try {
        const [faqRes, categoryRes] = await Promise.all([
            axios.get(`/api/faq/community/${communityId}`),
            axios.get(`/api/faq/${communityId}/category`)
        ])

        const categoryOrder = categoryRes.data || []
        categories.value = categoryOrder

        if (faqRes.data.success) {
            fullfaqList.value = faqRes.data.list.sort((a, b) => {
                const aIndex = categoryOrder.indexOf(a.category)
                const bIndex = categoryOrder.indexOf(b.category)
                return aIndex - bIndex
            })
            faqList.value = fullfaqList.value.map(faq => ({ ...faq, isExpanded: false }))
        }

    } catch (error) {
        //console.error('取得FAQ或分類失敗:', error)
    } finally {
        loading.value = false
    }
}


watch(paginatedFaqs, async (faqs) => {
    await nextTick()
    faqs.forEach(faq => {
        const el = document.getElementById(`collapse-${faq.id}`)
        if (el) new bootstrap.Collapse(el, { toggle: false })
    })
})




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
        //console.error('查詢 FAQ 失敗:', error)
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



onMounted(() => {
    fetchFaqs()

})
</script>

<style scoped>
.btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 8px 16px;
    border: none;
    border-radius: 12px;
    font-size: 18px;
    font-weight: 500;
    text-decoration: none;
    cursor: pointer;
    /* transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); */
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #5864a1 0%, #5f3d81 100%);
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
    font-size: 18px;
    font-weight: 400;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background: rgb(61, 88, 114);

    color: white;
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.btn-outline-class {

    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
    /* 可有可無：藍色光暈感 */
    background: rgb(108, 136, 162);
    background: linear-gradient(135deg, #9eace9 0%, #764ba2 100%);
    font-weight: 600;


}





.btn:hover {
    transform: translateY(0px);
    /* box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4); */
}

/* 修改整個 Accordion 每個 item 的背景 */
.accordion-item {
    background-color: #3A2A5D !important;
    border: 3px solid #384464;

}

/* 修改展開與未展開的標題按鈕背景 */
.accordion-button {
    background-color: #202d44;
    /* 自訂標題背景 */
    color: #ffffff;
    font-weight: 600;

}

/* 當折疊時的樣式（加上 collapsed） */
.accordion-button.collapsed {
    background-color: #182234;
    /* 未展開的按鈕顏色 */
}

/* accordion 內容區塊背景 */
.accordion-body {
    background-color: #34445f;
    color: #f5f5f5;
    font-weight: 400;
    font-size: 100%;
    /* 內文白色 */
}
</style>
