<template>
  <div class="comment-input">
    <textarea v-model="commentText" placeholder="新增你的留言..." rows="3" class="input" />
    <div class="suggestions">
      <span @click="quickComment('🎉看起來太棒了!')">🎉 看起來太棒了!</span>
      <span @click="quickComment('🧠需要幫忙嗎?')">🧠 需要幫忙嗎?</span>
      <span @click="quickComment('⛔ 這裡已被阻止...')">⛔ 這裡已被阻止...</span>
      <span @click="quickComment('💬 能澄清一下嗎...?')">💬 能澄清一下嗎...?</span>
    </div>

    <!-- 圖片留言上傳區 -->
    <div class="comment-attachments mt-2 p-2 border rounded" @dragover.prevent @drop.prevent="handleDrop">
      <p>📎 拖曳圖片到這裡，或 <span @click="fileInput.click()" class="text-primary">點選上傳</span></p>
      <input type="file" multiple ref="fileInput" class="d-none" @change="handleFileChange" />
      <div class="d-flex flex-wrap gap-2 mt-2">
        <div v-for="(f, idx) in files" :key="idx">
          <img :src="f.preview" style="width: 80px; height: 80px; object-fit: cover; border: 1px solid #ccc;"
            class="rounded" />
        </div>
      </div>
    </div>

    <button class="btn btn-primary mt-2" @click="submit">送出</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore.js'
const userStore = useUserStore()

const props = defineProps({
  ticketId: Number,
  onSuccess: Function,
})

const commentText = ref('')
const fileInput = ref(null)
const files = ref([])

function quickComment(text) {
  commentText.value = text
}

function handleFileChange(e) {
  const selected = Array.from(e.target.files)
  processFiles(selected)
}

function handleDrop(e) {
  const dropped = Array.from(e.dataTransfer.files)
  processFiles(dropped)
}

function processFiles(fileList) {
  fileList.forEach(file => {
    const reader = new FileReader()
    reader.onload = () => {
      files.value.push({ file, preview: reader.result })
    }
    reader.readAsDataURL(file)
  })
}

function toBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      const base64 = reader.result.split(',')[1]
      resolve(base64)
    }
    reader.onerror = err => reject(err)
  })
}

async function submit() {
  if (!commentText.value.trim()) return

  try {
    const commentPayload = {
      ticketId: props.ticketId,
      comment: commentText.value,
      commenter: userStore.userId 
    }

    const res = await axios.post(`/TicketComment`, commentPayload)
    const newComment = res.data.data

    if (files.value.length > 0) {
      const base64Files = await Promise.all(
        files.value.map(async (f) => {
          return {
            fileName: f.file.name,
            base64Data: await toBase64(f.file),
            uploadedBy: userStore.userId,
            commentId: newComment.id
          }
        })
      )
      await axios.post(`/ticket-attachment/upload/base64/multiple`, base64Files)
    }

    // 通知父元件
    props.onSuccess?.({
      user: userStore.name,
      time: new Date().toLocaleString(),
      text: newComment.comment
    })

    commentText.value = ''
    files.value = []

  } catch (err) {
    console.error('❌ 新增評論失敗', err)
    alert('新增評論失敗：' + (err.response?.data?.message || err.message))
  }
}
</script>