<template>
    <div class="ticket-form">
        <h2>Create Issue</h2>

        <form class="form-box" @submit.prevent="handleSubmit">
            <!-- Project -->
            <div class="form-group">
                <label>標題</label>
                <input type="text" />
            </div>

            <!-- Issue Type -->
            <div class="form-group">
                <label>問題種類</label>
                <Multiselect
  v-model="formIssue.issueType"
  :options="issueOptions"
  :multiple="true"
  :taggable="true"
  :close-on-select="false"
  :hide-selected="true"
  placeholder="請選擇或輸入問題種類"
  tag-placeholder="新增項目"
  track-by="name"
  label="name"
  @tag="addNewTag"
/>
            </div>
            <!-- Summary -->
            <div class="form-group">
                <label>概述</label>
                <input type="text" v-model="form.summary" />
            </div>

            <!-- Epic Link -->
            <!-- <div class="form-group">
                <label>Epic Link</label>
                <select v-model="form.epic">
                    <option>Select Epic</option>
                </select>
                <small>Choose an epic to assign this issue to.</small>
            </div> -->

            <!-- Description -->
            <div class="form-group full-width">
                <label>問題描述</label>
                <QuillEditor v-model:content="form.description" contentType="html"
                    placeholder="Describe the issue..." />

                <!-- 📎 拖曳上傳 -->
                <div class="upload-area" @dragover.prevent @drop.prevent="handleDrop">
                    <p>📎 拖曳檔案到這裡，或 <span @click="fileInput.click()">點選上傳</span></p>
                    <input type="file" multiple ref="fileInput" style="display: none" @change="handleFileChange" />
                </div>

                <!-- 預覽圖片 -->
                <div class="preview-list">
                    <div class="preview-item" v-for="(file, index) in previews" :key="index">
                        <img :src="file.url" />
                        <button type="button" @click="removeFile(index)">❌</button>
                    </div>
                </div>
            </div>

            <!-- Assignee -->
            <!-- <div class="form-group">
                <label>Assignee</label>
                <input type="text" placeholder="Automatic" disabled />
                <a href="#">Assign to me</a>
            </div> -->

            <!-- Priority -->
            <div class="form-group">
                <label>優先級</label>
                <select v-model="form.priority">
                    <option>Medium</option>
                    <option>High</option>
                    <option>Low</option>
                </select>
            </div>

            <!-- Buttons -->
            <div class="form-actions">
                <button type="submit">Create</button>
                <button type="button">Cancel</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import Multiselect from 'vue-multiselect'
import 'vue-multiselect/dist/vue-multiselect.min.css'

// 表單欄位資料
const form = ref({
    project: '',
    issueType: '',
    summary: '',
    epic: '',
    description: '',
    priority: 'Medium'
})


const issueOptions = ref([
  { name: '水電問題' },
  { name: '公共設施' },
  { name: '電梯異常' },
  { name: '其他' }
])

const formIssue = ref({
  issueType: []
})

function addNewTag(newTag) {
  const newOption = { name: newTag }

  // 新增到選項中
  issueOptions.value.push(newOption)

  // 新增到選取中
  if (!Array.isArray(formIssue.value.issueType)) {
    formIssue.value.issueType = []
  }

  formIssue.value.issueType.push(newOption)
}

// 附件處理
const fileInput = ref(null)
const files = ref([]) // 真正要送的 File 陣列
const previews = ref([]) // 預覽圖 URL 陣列

function handleFileChange(event) {
    const selected = Array.from(event.target.files)
    processFiles(selected)
}

function handleDrop(event) {
    const dropped = Array.from(event.dataTransfer.files)
    processFiles(dropped)
}

function processFiles(selected) {
    selected.forEach((file) => {
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

// 處理提交
function handleSubmit() {
    // 模擬提交
    console.log('📝 表單內容：', form.value)
    console.log('📎 附件檔案：', files.value)

    alert('資料準備送出（這裡尚未串後端）')
}
</script>

<style scoped>
.ticket-form {
    max-width: 1000px;
    /* 👉 拉寬至接近全螢幕 */
    width: 95vw;
    /* 👉 寬度以螢幕寬度為主，留 5% 邊界 */
    font-family: Arial, sans-serif;
    padding: 32px;
    box-sizing: border-box;
    max-height: 90vh;
    overflow-y: auto;
    background: white;
    border-radius: 8px;
}

.form-box {
    background: #fff;
    border: 1px solid #ddd;
    padding: 24px;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.form-group {
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
}

label {
    font-weight: 600;
    margin-bottom: 4px;
}

input,
select,
textarea {
    padding: 8px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

textarea {
    resize: vertical;
}

small {
    font-size: 12px;
    color: #777;
}

a {
    font-size: 12px;
    color: #0052cc;
    margin-top: 4px;
    cursor: pointer;
}

.form-actions {
    display: flex;
    gap: 10px;
    margin-top: 16px;
}

button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
}

button[type="submit"] {
    background-color: #0052cc;
    color: white;
}

button[type="button"] {
    background-color: #ddd;
}

.full-width {
    width: 100%;
}

.ql-editor {
    min-height: 150px;
}

/* 附件樣式 */
.upload-area {
    margin-top: 10px;
    padding: 16px;
    border: 2px dashed #ccc;
    text-align: center;
    border-radius: 6px;
    background-color: #f9f9f9;
    cursor: pointer;
}

.upload-area span {
    color: #0052cc;
    text-decoration: underline;
}

.preview-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 10px;
}

.preview-item {
    position: relative;
}

.preview-item img {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.preview-item button {
    position: absolute;
    top: -8px;
    right: -8px;
    background-color: red;
    color: white;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    font-size: 12px;
    padding: 2px 6px;
}
</style>