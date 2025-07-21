<!-- TicketDetailView.vue -->
<template>
  <div class="ticket-detail-layout container mt-4">
    <!-- 左側主內容 -->
    <div class="ticket-main">
      <h2 class="ticket-title" contenteditable @input="isEditing.title = true" @blur="checkTitleChange">
        {{ ticket.title }}
      </h2>

      <div class="section">
        <h3>報修單描述</h3>
        <QuillEditor  style="min-height:300px" v-model:content="edited.issueDescription" contentType="html"
          @focus="isEditing.issueDescription = true" class="custom-quill" />
        <div v-if="isEditing.issueDescription" class="edit-controls">
          <button @click="cancelEdit('issueDescription')" class="btn-cancel">取消</button>
          <button @click="saveEdit('issueDescription')" class="btn-save">儲存</button>
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
        <h3>留言</h3>
        <CommentInput :ticket-id="ticketId" :onSuccess="handleCommentAdded" />

        <div class="comment-list mt-3">
          <div class="comment-item card p-3 mb-3" v-for="(comment, i) in reversedComments" :key="i">
            <div class="comment-header d-flex align-items-center mb-2">
              <div class="avatar me-2">{{ getInitials(comment.displayName) }}</div>
              <div class="meta">
                <strong>{{ comment.displayName }}</strong>
                <span class="text-secondary small" :title="comment.time">🕒 {{ timeAgo(comment.time) }}</span>
              </div>
            </div>
            <div class="comment-body">
    <div v-if="isEditingComment === i">
 <div
  class="editable-input mb-2"
  contenteditable="true"
  @input="editedCommentText = $event.target.innerText"
  :textContent="editedCommentText"
></div>
      <div>
        <button class="btn-cancel me-2" @click="cancelCommentEdit">取消</button>
        <button class="btn-save" @click="saveCommentEdit(comment.id)">儲存</button>
      </div>
    </div>
    <div v-else>
      <p>{{ comment.text }}</p>
    </div>

              <div class="d-flex flex-wrap gap-2 mt-2" v-if="comment.attachments.length">
                <img v-for="(img, j) in comment.attachments" :key="j" :src="`data:image/png;base64,${img.file}`"
                  :alt="img.fileName" class="rounded border"
                  style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" @click="openPreview(img)" />
              </div>
                <div class="mt-2" v-if="userStore.roleId === 3">
                  <button
                  class="btn-edit"
                  v-if="userStore.roleId === 2 && isEditingComment !== i"
                  @click="editComment(comment, i)"
                >
                  編輯
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右側詳情 -->
    <div class="ticket-side p-4 bg-light border-start" style="width: 320px; overflow-y: auto;">
      <div class="mb-3">
  <label class="label">狀態</label>

  <!-- 管理員才可以編輯 -->
      <select v-if="isAdmin" v-model="ticket.status" @change="saveStatus" class="form-control">
        <option value="to do">TO DO</option>
        <option value="In Progress">IN PROGRESS</option>
        <option value="Done">DONE</option>
      </select>
      <!-- 一般使用者只看文字 -->
      <div v-else class="form-control">
      {{ ticket.status }}
    </div>
    </div>
      <div class="mb-3">
        <label class="label">指派人</label>
        <div class="form-control bg-white">
          {{ ticket.assigner?.name || '未指派' }}
        </div>
      </div>

      <div class="mb-3">
  <label class="label">問題種類</label>

  <!-- ✅ 管理員才能編輯 -->
  <Multiselect
    v-if="isAdmin"
    v-model="selectedIssueTypes"
    :options="allIssueTypes"
    :multiple="true"
    :taggable="true"
    tag-placeholder="新增..."
    placeholder="請選擇或輸入問題種類"
    track-by="id"
    label="issueTypeName"
    @tag="addNewIssueType"
    @update:modelValue="saveIssueTypes"
  />

  <!-- ❌ 非管理員只顯示已選項目 -->
  <div v-else class="form-control">
    <span
      v-for="(type, i) in selectedIssueTypes"
      :key="i"
      class="badge bg-secondary me-1"
    >
      {{ type.issueTypeName }}
    </span>
  </div>
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
import axios from '@/plugins/axios'
import CommentInput from '@/views/CommentInput.vue'
import 'vue-multiselect/dist/vue-multiselect.min.css'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { useUserStore } from '@/stores/UserStore.js'
const isAdmin = computed(() => userStore.roleId === 2)
const userStore = useUserStore()

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
function convertStatusFromBackend(s) {
  if (s === 'to do') return 'to do'
  if (s === 'In Progress') return 'In Progress'
  if (s === 'Done') return 'Done'
  return s
}

const isEditingComment = ref(null) // null 表示沒在編輯，否則記錄 comment index
const editedCommentText = ref('')  // 暫存編輯中文本

onMounted(loadTicket)
async function loadTicket() {
  try {
    const res = await axios.get(`/ticket/${ticketId}`)
    const data = res.data
       // 👉 加這段，補 assigner
    if (!data.assigner && data.assignerName) {
      data.assigner = { name: data.assignerName }
    }

    // 設定 ticket 主要資料
    ticket.value = data
    
    edited.value.issueDescription = data.issueDescription

    // if (!data.assigner) {
    //   ticket.value.assigner = { name: userStore.name } // 補上目前登入者名稱
    // }

    // 設定留言（其實 data.comments 就有了）
    ticket.value.comments = data.comments

    // 所有問題種類選項（你若需要載入全部類型供選擇）
    ticket.value.status = convertStatusFromBackend(data.status)
    const allTypesRes = await axios.get('/IssueTypes')
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
    await axios.put(`/ticket/status/${ticket.value.id}`, {
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
    await axios.put(`/ticket-issue/update/${ticketId}`, ids)
  } catch (err) {
    console.error('❌ 儲存失敗', err)
  }
}

async function addNewIssueType(newName) {
  const res = await axios.post('/IssueTypes', { issueTypeName: newName })
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
        // assignerId: 31,
        status: ticket.value.status,
        issueDescription: edited.value.issueDescription,
        notes: ticket.value.notes,
        communityId: 1,
        actionBy: 1
      }
       if (ticket.value.assignerId) {
        payload.assignerId = ticket.value.assignerId
      }
      await axios.put(`/ticket/${ticketId}`, payload)
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
    id: c.id,
    displayName: c.name || c.commenter?.name || '匿名',
    text: c.text || c.comment,
    time: c.commentTime || '',
    attachments: c.attachments || []
  }))
)

function editComment(comment, index) {
  isEditingComment.value = index
  editedCommentText.value = comment.text
}

function cancelCommentEdit() {
  isEditingComment.value = null
  editedCommentText.value = ''
}

async function saveCommentEdit(commentId) {
  try {
    const payload={
      ticketId: ticket.value.id,
      comment: editedCommentText.value,
      commenter: userStore.userId 
    }
    await axios.put(`/TicketComment/${commentId}`, payload)

    isEditingComment.value = null
    editedCommentText.value = ''
    loadTicket() // 重新載入留言
  } catch (err) {
    alert('儲存失敗：' + (err.response?.data?.message || err.message))
  }
}

function timeAgo(dateTimeString) {
  if (!dateTimeString) return ''

  const safeDate = dateTimeString.replace(' ', 'T') // 👉 修正格式
  const commentTime = new Date(safeDate)

  if (isNaN(commentTime.getTime())) return ''

  const now = new Date()
  const diff = Math.floor((now - commentTime) / 1000)

  if (diff < 60) return `${diff} 秒前`
  if (diff < 3600) return `${Math.floor(diff / 60)} 分鐘前`
  if (diff < 86400) return `${Math.floor(diff / 3600)} 小時前`
  return `${Math.floor(diff / 86400)} 天前`
}

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
.editable-input {
  border: 1px solid #ced4da;
  border-radius: 0.375rem;
  padding: 6px 12px;
  min-height: 38px;
  background-color: #fff;
  outline: none;
}
.editable-input:focus {
  border-color: #86b7fe;
  box-shadow: 0 0 0 0.1rem rgba(13, 110, 253, 0.25);
}
.btn-cancel,
.btn-save {
  padding: 4px 12px;
  font-size: 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.btn-cancel {
  background-color: #f4f5f7;
  color: #172b4d;
}

.btn-cancel:hover {
  background-color: #ebecf0;
}

.btn-save {
  background-color: #0052cc;
  color: white;
}

.btn-save:hover {
  background-color: #0065ff;
}
.btn-edit {
  font-size: 13px;
  padding: 4px 10px;
  border: 1px solid #0052cc;
  background-color: white;
  color: #0052cc;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
}

.btn-edit:hover {
  background-color: #e6f0ff;
  box-shadow: 0 2px 6px rgba(0, 82, 204, 0.2);
  color: #0747a6;
}
.btn-cancel {
  font-size: 13px;
  padding: 4px 10px;
  border: 1px solid #ccc;
  background-color: #f4f5f7;
  color: #42526e;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-cancel:hover {
  background-color: #ebecf0;
  color: #172b4d;
}

.btn-save {
  font-size: 13px;
  padding: 4px 12px;
  border: 1px solid #0052cc;
  background-color: #0052cc;
  color: #fff;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-save:hover {
  background-color: #0065ff;
}

</style>




