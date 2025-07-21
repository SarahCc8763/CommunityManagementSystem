<template>


    <div class="container py-4 ">
        <BannerImage :imageSrc="OO" heading="公告管理" subtext="" textAlign="left" />

        <div class="d-flex flex-row gap-2 mb-2">
        </div>

        <div class="d-flex gap-2 mb-3">
            <button class=" btn btn-primary w-10 " @click="postBulletins">新增公告</button>
            <button class="btn btn-primary w-10" @click="categoriesManagement" style="margin-right: 2%;">分類管理</button>

            <select v-model="searchCategory" class="form-select w-20" @change="searchBulletins">
                <option value="">全部分類</option>
                <option v-for="cat in categoryList" :key="cat.id" :value="cat.name">{{ cat.name }}</option>
            </select>
            <input type="text" v-model="searchTitle" class="form-control w-38" placeholder="輸入標題關鍵字" />
            <button class="btn btn-primary w-10 " @click="searchBulletins">搜尋</button>
            <button class="btn btn-secondary w-10" @click="clearSearch">清除搜尋</button>
        </div>

        <PostBulletinModal v-model:visible="showPost" :categoryList="categoryList" :communityId="communityId"
            :usersId="userId" @post="fetchAll" />


        <!-- 公告管理 card 列表 -->
        <div>
            <div>
                <div v-for="bulletin, index in bulletins" :key="bulletin.id"
                    class=" border p-3 rounded bg-light shadow-sm my-1">
                    <span class="fs-6 text-secondary fw-normal">{{ bulletin.postStatus ? '（已發佈）' :
                        "（草稿）"
                    }}</span>
                    <div class="d-flex justify-content-between align-items-center mb-2">

                        <div class="fw-bold text-dark fs-5"><span v-if="bulletin.isPinned">[置頂]</span> <span
                                class="badge  me-2 fw-normal" style="font-size: 80%;background-color: #BEBEBE;">{{
                                    bulletin.categoryName
                                }}</span>
                            {{ bulletin.title }} <span v-if="bulletin.poll"
                                class="fs-6 text-secondary fw-normal">(投票活動)</span>
                        </div>

                        <div>

                            <small class="text-muted">發布時間：{{ formatDate(bulletin.postTime) }}</small><br>
                            <small class="text-muted">下架時間：{{ formatDate(bulletin.removeTime) }}</small>
                        </div>
                    </div>
                    <div class="mb-2">

                        <span class="text-muted small">發布人：{{ bulletin.userName }}</span>
                    </div>
                    <p class="text-truncate text-muted small mb-3 fs-6">
                        {{ normalizeNewline(truncateText(bulletin.description, 80)) }}
                        <span v-if="bulletin.description.length > 80">...</span>
                    </p>
                    <div class="d-flex justify-content-end gap-2">
                        <button class="btn btn-sm btn-outline-primary" @click="editBulletin(bulletin)">
                            <i class="bi bi-pencil-square"></i> &nbsp;編輯
                        </button>
                        <button class="btn btn-sm btn-outline-danger" @click="deleteBulletin(bulletin.id)">
                            <i class="bi bi-trash"></i> &nbsp;刪除
                        </button>
                        <button class="btn btn-sm btn-outline-secondary" @click="viewBulletin(bulletin)">
                            <i class="bi bi-eye"></i> &nbsp;查看
                        </button>
                        <button v-if="bulletin.poll" type="button" class="btn btn-sm btn-secondary"
                            @click="openPollModal(index, bulletin)">
                            <i class="bi bi-pencil-square"></i> &nbsp;修改投票</button>

                        <button v-if="!bulletin.poll" type="button" class="btn btn-sm btn-outline-secondary"
                            @click="openPollModal(index, bulletin)">
                            <i class="bi bi-plus-circle"></i> &nbsp;新增投票</button>
                    </div>
                </div>
            </div>
        </div>


        <CategoryModal v-model:visible="showCategoryModal" @updated="fetchAll" :communityId="communityId" />

        <EditBulletinModal v-model:visible="showEdit" :bulletin="selectedBulletin" :categoryList="categoryList"
            :usersId="userId" @close="showEdit = false" @updated="fetchAll" :communityId="communityId" />

        <ViewBulletinModal v-model:visible="showView" :bulletin="selectedBulletin" :categoryList="categoryList"
            :usersId="userId" :normalizeFn="normalizeNewline" />

        <EditPollModal v-model:visible="showPollEdit" :poll="selectedBulletin?.poll" :pollBackup="pollToSend"
            @updated="fetchAll" :bulletinId="selectedBulletin?.id" />

        <!-- 公告 Modal -->

    </div>
</template>




<script setup>
// 函式庫
import { ref, onMounted } from 'vue'
import axios from '@/plugins/axios'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'
import Swal from 'sweetalert2'

// 元件
import BannerImage from '@/components/forAll/BannerImage.vue';
import OO from '@/assets/images/main/adminBanner.jpg';
import EditBulletinModal from '@/components/bulletin/EditBulletinModal.vue';
import ViewBulletinModal from '@/components/bulletin/ViewBulletinModal.vue';
import PostBulletinModal from '@/components/bulletin/PostBulletinModal.vue';
import EditPollModal from '@/components/bulletin/EditPollModal.vue'
import CategoryModal from '@/components/bulletin/CategoryModal.vue';
import { useUserStore } from '@/stores/UserStore'

// assets
const userStore = useUserStore()
const userId = userStore.userId || 0 // 假設當前使用者 id
const communityId = userStore.communityId || 0 // 假設當前社區 ID
const bulletins = ref([])
const selectedBulletin = ref(null)

const searchTitle = ref('')
const searchCategory = ref('')
const categoryList = ref([])

const showCategoryModal = ref(false)
const showEdit = ref(false)
const showView = ref(false)
const showPost = ref(false)
const showPollEdit = ref(false)

let pollBackup = null
const pollToSend = ref(null)


const formatDate = (dt) => new Date(dt).toLocaleString()
const truncateText = (text, maxLength) => text?.length > maxLength ? text.slice(0, maxLength) : text

function postBulletins() {
    showPost.value = true
}

function categoriesManagement() {
    showCategoryModal.value = true

}

function openPollModal(index, bulletin) {
    selectedBulletin.value = bulletin
    pollToSend.value = pollBackup[bulletin.id] || null
    //console.log('選到的公告的投票' + searchBulletins?.poll);
    //console.log('要傳送的備份' + pollToSend.value);
    showPollEdit.value = true
}



function editBulletin(bulletin) {
    selectedBulletin.value = bulletin
    showEdit.value = true
}


function deleteBulletin(id) {
    Swal.fire({
        title: '確定刪除？',
        text: '刪除後將無法恢復',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '確定',
        cancelButtonText: '取消'
    }).then((result) => {
        if (result.isConfirmed) {
            axios.delete(`/api/bulletin/${id}`).then(res => {
                if (res.data.success) {
                    Swal.fire({
                        title: '刪除成功',
                        text: '公告已刪除',
                        icon: 'success',
                        timer: 1000
                    })
                    fetchAll()
                } else {
                    Swal.fire({
                        title: '刪除失敗',
                        text: '請稍後再試',
                        icon: 'error'
                    })
                }
            })
        }
    })
}

function viewBulletin(bulletin) {
    selectedBulletin.value = bulletin
    showView.value = true
}

function normalizeNewline(text) {
    return text?.replace(/\\n/g, '\n') || ''
}


function searchBulletins() {
    axios.post('/api/bulletin/searchby', {
        title: searchTitle.value || undefined,
        category: searchCategory.value ? { name: searchCategory.value } : undefined,
        community: {
            communityId: communityId
        }
    }).then(res => {
        bulletins.value = res.data.list
    })
}

function clearSearch() {
    searchTitle.value = ''
    searchCategory.value = ''
    fetchAll()
}


onMounted(() => {
    fetchAll()
})

function fetchAll() {
    axios.get('/api/bulletin/community/' + communityId)
        .then(res => {
            // 根據 postTime 由新到舊排序
            const sortedList = res.data.list.sort((a, b) => new Date(b.postTime) - new Date(a.postTime))
            // 儲存排序後的 bulletins
            bulletins.value = sortedList
            // 據排序後的 bulletin 建立 poll 的深拷貝備份
            pollBackup = {}
            sortedList.forEach(bulletin => {
                pollBackup[bulletin.id] = JSON.parse(JSON.stringify(bulletin.poll))
            })

            //console.log('觸發了!!!');
        })

    axios.get('/api/bulletin/category/community/' + communityId)
        .then(res => {
            const cats = res.data.map(cat => ({ id: cat.id, name: cat.name }))
            categoryList.value = cats
        })
}




</script>

<style scoped>
/* h1 {
    color: #b0cefa;
} */

input::placeholder {
    color: #a0aec0;
    opacity: 1;
}


.card-title {
    font-size: 1.2rem;
    font-weight: bold;
}

.hover-underline:hover {
    text-decoration: underline;
}

.hover-underline {
    text-decoration: none;
}

.modal-dialog {
    margin-top: 10vh;
    /* 或你想要的距離，例如 10vh */
    max-height: 90vh;
}

.modal-content {
    max-height: 90vh;
    overflow: hidden;
}

.modal-body {
    overflow-y: auto;
    max-height: 75vh;
}


:deep(.hero-image-wrapper) {
    max-height: 150px;
    max-width: 100%;
}


/* 公告區域 */
.announcements-section {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.section-title {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 24px;
    font-weight: 600;
    color: #2d3748;
    margin: 0;
}

.section-title i {
    color: #667eea;
}

.view-all-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 25px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 8px;
}

.view-all-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.announcements-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 0.3333fr));
    gap: 1rem;
}

.announcement-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.announcement-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.announcement-card.important::before {
    background: linear-gradient(135deg, #f56565 0%, #e53e3e 100%);
}

.announcement-card.event::before {
    background: linear-gradient(135deg, #ffc107 0%, #f6ad55 100%);
}

.announcement-card.service::before {
    background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
}

.announcement-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.announcement-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.announcement-badge {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    background: rgba(102, 126, 234, 0.1);
    color: #667eea;
}

.announcement-card.important .announcement-badge {
    background: rgba(245, 101, 101, 0.1);
    color: #f56565;
}

.announcement-card.event .announcement-badge {
    background: rgba(255, 193, 7, 0.1);
    color: #ffc107;
}

.announcement-card.service .announcement-badge {
    background: rgba(72, 187, 120, 0.1);
    color: #48bb78;
}

.announcement-date {
    font-size: 12px;
    color: #a0aec0;
}

.announcement-title {
    font-size: 18px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 12px;
    line-height: 1.4;
}

.announcement-content {
    color: #718096;
    line-height: 1.6;
    margin-bottom: 20px;
    display: -webkit-box;

    -webkit-box-orient: vertical;
    overflow: hidden;
}

.announcement-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.announcement-author {
    font-size: 12px;
    color: #a0aec0;
}

.read-more-btn {
    background: none;
    border: none;
    color: #667eea;
    font-size: 12px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
    transition: all 0.3s ease;
}

.read-more-btn:hover {
    color: #5a6fd8;
    transform: translateX(4px);
}

.w-20 {
    width: 20% !important;
    /* margin-right: 1rem; */
}

.w-38 {
    width: 37% !important;
}

.w-10 {
    width: 10% !important;
}

.btn-comment {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 8px 16px;
    border: none;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background-color: white;
    color: rgb(128, 159, 243);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
    height: 35px;
    margin-bottom: 3%;

}

.btn-comment:hover {
    transform: translateY(-2px);
    background-color: #e5efff;
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);

}
</style>