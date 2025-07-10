<template>
  <div class="container mt-4 card p-4">
    <h2 class="card-title mb-3">Create Issue</h2>

    <form @submit.prevent="handleSubmit">
      <!-- 標題 -->
      <div class="mb-3">
        <label class="label">標題</label>
        <input type="text" v-model="form.title" class="form-control input" />
      </div>

      <!-- 問題種類 -->
      <div class="mb-3">
        <label class="label">問題種類</label>
        <Multiselect v-model="formIssue.issueType" :options="issueOptions" :multiple="true" :taggable="true"
          :close-on-select="false" :hide-selected="true" placeholder="請選擇或輸入問題種類" tag-placeholder="新增項目" track-by="name"
          label="name" @tag="addNewTag" />
      </div>

      <!-- 描述 + 附件 -->
      <div class="mb-3">
        <label class="label">問題描述</label>
        <QuillEditor style="min-height:300px" v-model:content="form.description" contentType="html"
          placeholder="Describe the issue..." class="input" />

        <div class="upload-area mt-3 p-3 border rounded" @dragover.prevent @drop.prevent="handleDrop">
          <p>📎 拖曳圖片到這裡，或 <span @click="fileInput.click()" class="text-primary">點選上傳</span></p>
          <input type="file" multiple ref="fileInput" class="d-none" @change="handleFileChange" />

          <div class="preview-list d-flex flex-wrap gap-2 mt-2">
            <div class="position-relative" v-for="(file, index) in previews" :key="index">
              <img :src="file.url" alt="preview" class="rounded border"
                style="width: 100px; height: 100px; object-fit: cover;" />
              <button type="button" @click="removeFile(index)"
                class="btn btn-danger btn-sm position-absolute top-0 end-0 translate-middle p-0 rounded-circle"
                style="width: 24px; height: 24px;">
                &times;
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Buttons -->
      <div class="mt-4 d-flex gap-2">
        <button type="submit" class="btn">Create</button>
        <!-- <button type="button" class="btn btn-secondary" @click="$emit('close')">Cancel</button> -->
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import Multiselect from 'vue-multiselect'
import 'vue-multiselect/dist/vue-multiselect.min.css'

// 表單資料
const form = ref({
  project: '',
  title: '',
  description: '',
  priority: 'Medium'
})

const formIssue = ref({ issueType: [] })
const issueOptions = ref([])

// 取得 issueType 選項
onMounted(() => {
  axios.get('http://localhost:8080/IssueTypes')
    .then(res => {
      issueOptions.value = res.data.map(item => ({ name: item.issueTypeName }))
    })
    .catch(err => {
      console.error('載入 issueTypes 失敗', err)
    })
})

// 新增自定問題類型
async function addNewTag(newTag) {
  const newOption = { name: newTag }
  try {
    await axios.post('http://localhost:8080/IssueTypes', {
      issueTypeName: newTag
    })
    issueOptions.value.push(newOption)
    formIssue.value.issueType.push(newOption)
  } catch (err) {
    console.error('新增 issueType 失敗', err)
  }
}

// 上傳圖片處理
const fileInput = ref(null)
const files = ref([])
const previews = ref([])

function handleFileChange(event) {
  const selected = Array.from(event.target.files)
  processFiles(selected)
}

function handleDrop(event) {
  const dropped = Array.from(event.dataTransfer.files)
  processFiles(dropped)
}

function processFiles(selected) {
  selected.forEach(file => {
    files.value.push(file)
    const reader = new FileReader()
    reader.onload = () => {
      previews.value.push({ file, url: reader.result })
    }
    reader.readAsDataURL(file)
  })
}

function removeFile(index) {
  files.value.splice(index, 1)
  previews.value.splice(index, 1)
}

function toBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      const result = reader.result
      const base64 = result.split(',')[1]
      resolve(base64)
    }
    reader.onerror = reject
  })
}

// 建立 ticket + 上傳附件
async function handleSubmit() {
  if (!form.value.title || form.value.title.trim() === '') {
    alert('❗請填寫標題')
    return
  }

  const payload = {
    reporterId: 2, // TODO: 改為登入使用者 ID
    communityId: 1, // TODO: 改為登入者社區 ID
    title: form.value.title,
    issueDescription: form.value.description,
    status: 'to do',
    cost: 3000,
    notes: '住戶反映已多次發生',
    issueTypeNames: formIssue.value.issueType.map(i => i.name)
  }

  try {
    const ticketRes = await axios.post('http://localhost:8080/ticket', payload)
    const ticketResponse = ticketRes.data

    if (!ticketResponse.success) {
      alert('❌ 建立 ticket 失敗：' + ticketResponse.message)
      return
    }

    const ticketId = ticketResponse.data.id

    const base64Files = await Promise.all(files.value.map(async (file) => {
      const base64Data = await toBase64(file)
      return {
        fileName: file.name,
        base64Data,
        uploadedBy: 2,
        ticketId
      }
    }))

    if (base64Files.length > 0) {
      const uploadRes = await axios.post(
        'http://localhost:8080/ticket-attachment/upload/base64/multiple',
        base64Files
      )
      const uploadResult = uploadRes.data

      if (uploadResult.success) {
        alert('✅ 報修單與附件上傳成功！')
      } else {
        alert('📎 報修單建立成功，但附件上傳失敗：' + uploadResult.message)
      }
    } else {
      alert('✅ 報修單建立成功（無附件）')
    }

    form.value.summary = ''
    form.value.description = ''
    formIssue.value.issueType = []
    files.value = []
    previews.value = []

  } catch (err) {
    console.error('❌ 建立失敗', err)
    alert('建立失敗，請稍後再試')
  }
}
</script>
<style scoped>
.container {
  max-width: 1200px !important;
  margin: 0 auto !important;
  padding: 0 24px !important;
}
</style>
