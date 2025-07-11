<!-- TicketDetailView.vue -->
<template>
  <div class="ticket-detail-layout">
    <!-- 左側主內容 -->
    <div class="ticket-main">
      <h2 class="ticket-title" contenteditable @input="isEditing.title = true" @blur="checkTitleChange">
        {{ ticket.title }}
      </h2>

      <div class="section">
        <h3>Description</h3>
        <QuillEditor style="min-height:300px" v-model:content="edited.issueDescription" contentType="html"
          @focus="isEditing.issueDescription = true" class="custom-quill" />
        <div v-if="isEditing.issueDescription" class="edit-controls">
          <button @click="cancelEdit('issueDescription')">取消</button>
          <button @click="saveEdit('issueDescription')">儲存</button>
        </div>
      </div>

      <div class="section" v-if="ticket.attachments?.length">
        <h3>Attachments</h3>
        <div class="d-flex flex-wrap gap-2">
          <img v-for="(attachment, index) in ticket.attachments" :key="index"
            :src="`data:image/png;base64,${attachment.file}`" :alt="attachment.fileName" class="rounded border"
            style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" @click="openPreview(attachment)" />
        </div>
      </div>

      <div v-if="previewImageUrl" class="image-preview-overlay" @click="closePreview">
        <img :src="previewImageUrl" class="image-preview" @click.stop />
      </div>

      <div class="section">
        <h3>Comments</h3>
        <CommentInput :ticket-id="ticketId" :onSuccess="handleCommentAdded" />

        <div class="comment-list mt-3">
          <div class="comment-item card p-3 mb-3" v-for="(comment, i) in reversedComments" :key="i">
            <div class="comment-header d-flex align-items-center mb-2">
              <div class="avatar me-2">{{ getInitials(comment.displayName) }}</div>
              <div class="meta">
                <strong>{{ comment.displayName }}</strong>
                <span class="text-secondary ms-2">{{ comment.time }}</span>
              </div>
            </div>
            <div class="comment-body">
              <p>{{ comment.text }}</p>

              <div class="d-flex flex-wrap gap-2 mt-2" v-if="comment.attachments.length">
                <img v-for="(img, j) in comment.attachments" :key="j" :src="`data:image/png;base64,${img.file}`"
                  :alt="img.fileName" class="rounded border"
                  style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" @click="openPreview(img)" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右側詳情 -->
    <div class="ticket-side p-4 bg-light border-start" style="width: 320px; overflow-y: auto;">
      <div class="mb-3">
        <label class="label">Status</label>
        <select v-model="ticket.status" @change="saveStatus" class="form-control">
          <option value="To Do">To Do</option>
          <option value="In Progress">In Progress</option>
          <option value="Done">Done</option>
        </select>
      </div>

      <div class="mb-3">
        <label class="label">指派人</label>
        <div class="form-control bg-white">
          {{ ticket.assignee && ticket.assignee.trim() !== '' ? ticket.assignee : '未指派' }}
        </div>
      </div>

      <div class="mb-3">
        <label class="label">問題種類</label>
        <Multiselect v-model="selectedIssueTypes" :options="allIssueTypes" :multiple="true" :taggable="true"
          tag-placeholder="新增..." placeholder="請選擇或輸入問題種類" track-by="id" label="issueTypeName" @tag="addNewIssueType"
          @update:modelValue="saveIssueTypes" />
      </div>

      <div class="mb-3">
        <label class="label">發布時間</label>
        <div class="form-control">{{ formatDate(ticket.startDate) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import Multiselect from 'vue-multiselect'
import { QuillEditor } from '@vueup/vue-quill'
import { useRoute } from 'vue-router'
import axios from 'axios'
import CommentInput from '@/views/CommentInput.vue'
import 'vue-multiselect/dist/vue-multiselect.min.css'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const route = useRoute()
const ticketId = route.params.id
const ticket = ref({
  title: '', issueDescription: '', assignee: '', status: '', startDate: '',
  comments: [], attachments: []
})
const previewImageUrl = ref(null)
const edited = ref({ issueDescription: '' })
const isEditing = ref({ issueDescription: false, title: false })
const selectedIssueTypes = ref([])
const allIssueTypes = ref([])


onMounted(loadTicket)
async function loadTicket() {
  try {
    const res = await axios.get(`http://localhost:8080/ticket/${ticketId}`)
    const data = res.data

    // 設定 ticket 主要資料
    ticket.value = data
    edited.value.issueDescription = data.issueDescription

    // 設定留言（其實 data.comments 就有了）
    ticket.value.comments = data.comments

    // 所有問題種類選項（你若需要載入全部類型供選擇）
    const allTypesRes = await axios.get('http://localhost:8080/IssueTypes')
    allIssueTypes.value = allTypesRes.data

    // 處理多對多的 issueTypes => 提取 issueType 欄位
    selectedIssueTypes.value = data.issueTypes.map(i => i.issueType)

  } catch (err) {
    console.error('❌ 載入資料失敗', err)
  }
}


function handleCommentAdded() {
  loadTicket() // 確保留言重新載入
}

function openPreview(a) { previewImageUrl.value = `data:image/png;base64,${a.file}` }
function closePreview() { previewImageUrl.value = null }

function getInitials(name) {
  return name?.split(' ').map(n => n[0]).join('').toUpperCase()
}

function formatDate(datetime) {
  if (!datetime) return ''
  return new Date(datetime).toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit'
  })
}

async function saveStatus() {
  try {
    await axios.put(`http://localhost:8080/ticket/status/${ticket.value.id}`, {
      status: ticket.value.status // 只送出 status 欄位即可
    })
    console.log('✅ 狀態更新成功')
  } catch (err) {
    console.error('❌ 狀態更新失敗', err)
  }
}


async function saveIssueTypes() {
  try {
    const ids = selectedIssueTypes.value.map(t => t.id)
    await axios.put(`http://localhost:8080/ticket-issue/update/${ticketId}`, ids)
  } catch (err) {
    console.error('❌ 儲存失敗', err)
  }
}

async function addNewIssueType(newName) {
  const res = await axios.post('http://localhost:8080/IssueTypes', { issueTypeName: newName })
  allIssueTypes.value.push(res.data)
  selectedIssueTypes.value.push(res.data)
  saveIssueTypes()
}

async function saveEdit(field) {
  try {
    if (field === 'issueDescription') {
      const payload = {
        reporterId: 1,
        title: ticket.value.title,
        assignerId: 2,
        status: ticket.value.status,
        issueDescription: edited.value.issueDescription,
        notes: ticket.value.notes,
        communityId: 1,
        actionBy: 1
      }
      await axios.put(`http://localhost:8080/ticket/${ticketId}`, payload)
      ticket.value.issueDescription = edited.value.issueDescription
    }
    isEditing.value[field] = false
  } catch (err) {
    alert('儲存失敗：' + (err.response?.data?.message || err.message))
  }
}

function cancelEdit(field) {
  if (field === 'issueDescription') {
    edited.value.issueDescription = ticket.value.issueDescription
  }
  isEditing.value[field] = false
}

function checkTitleChange() {
  isEditing.value.title = false
}
const reversedComments = computed(() =>
  [...ticket.value.comments].reverse().map(c => ({
    displayName: c.user || c.commenter?.name || '匿名',
    text: c.text || c.comment,
    time: c.time || c.createdAt || '',
    attachments: c.attachments || []
  }))
)
</script>

<style scoped>
.ticket-detail-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background-color: #f8f9fa;
}

.ticket-main {
  flex: 1;
  padding: 32px;
  overflow-y: auto;
  background: white;
}

.ticket-title {
  font-size: 24px;
  font-weight: 700;
}

.section h3 {
  font-size: 16px;
  margin-top: 24px;
  margin-bottom: 8px;
}

.label {
  font-weight: 600;
  margin-bottom: 4px;
  display: inline-block;
}

.avatar {
  width: 32px;
  height: 32px;
  background-color: #667eea;
  color: white;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.ql-editor {
  min-height: 600px;
}

.custom-quill {
  height: 1000px;
  border-radius: 10px;
  border: 1px solid #ccc;
  overflow: auto;
}

.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-preview {
  max-width: 90vw;
  max-height: 90vh;
  border-radius: 8px;
  box-shadow: 0 0 20px #000;
}
</style>
