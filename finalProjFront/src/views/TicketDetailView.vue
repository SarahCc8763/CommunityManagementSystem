<template>
    <div class="ticket-detail-layout">
      <!-- 左側主內容 -->
      <div class="ticket-main">
        <h2 contenteditable class="ticket-title">淹水啦</h2>
  
        <div class="section">
          <h3>Description</h3>
          <QuillEditor v-model:content="ticket.description" contentType="html" />
        </div>
  
        <div class="section">
          <h3>Comments</h3>
          <!-- 留言輸入區 -->
          <div class="comment-input">
            <textarea v-model="newCommentText" placeholder="Add a comment..." rows="3" />
            <div class="suggestions">
              <span @click="quickComment('Looks good!')">🎉 Looks good!</span>
              <span @click="quickComment('Need help?')">🧠 Need help?</span>
              <span @click="quickComment('This is blocked...')">⛔ This is blocked...</span>
              <span @click="quickComment('Can you clarify...?')">💬 Can you clarify...?</span>
            </div>
            <button class="btn-submit" @click="submitComment">Comment</button>
          </div>
  
          <!-- 留言清單 -->
          <div class="comment-list">
            <div class="comment-item" v-for="(comment, i) in [...ticket.comments].reverse()" :key="i">
              <div class="comment-header">
                <div class="avatar">{{ getInitials(comment.user) }}</div>
                <div class="meta">
                  <strong>{{ comment.user }}</strong>
                  <span class="time">{{ comment.time }}</span>
                </div>
              </div>
              <div class="comment-body">{{ comment.text }}</div>
              <div class="comment-actions">
                <span>Reply</span>
                <span>Edit</span>
                <span>Delete</span>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 右側欄位 -->
      <div class="ticket-side">
        <div class="side-group">
          <label>Assignee</label>
          <input type="text" v-model="ticket.assignee" />
        </div>
        <div class="side-group">
          <label>Labels</label>
          <Multiselect
            v-model="ticket.labels"
            :options="allLabels"
            :multiple="true"
            :taggable="true"
            placeholder="選擇或新增標籤"
            track-by="name"
            label="name"
            @tag="addLabel"
          />
        </div>
        <div class="side-group">
          <label>Status</label>
          <select v-model="ticket.status">
            <option value="To Do">To Do</option>
            <option value="In Progress">In Progress</option>
            <option value="Done">Done</option>
          </select>
        </div>
        <div class="side-group">
          <label>Fix Version</label>
          <input type="text" v-model="ticket.fixVersion" />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import Multiselect from 'vue-multiselect'
  import 'vue-multiselect/dist/vue-multiselect.min.css'
  import { QuillEditor } from '@vueup/vue-quill'
  import '@vueup/vue-quill/dist/vue-quill.snow.css'
  
  const ticket = ref({
    title: '',
    description: '<p>浴室天花板長期滲水，懷疑樓上水管破裂，水漬擴散至牆角，已暫時關閉主水閥但未見改善，請盡快安排檢修，謝謝！</p>',
    assignee: 'Raphael',
    labels: [ { name: '水塔' }, { name: '水管' } ],
    status: 'In Progress',
    fixVersion: 'v1.2.3',
    comments: [
      { user: 'Jay', text: '請聯繫樓上住戶，確認水管狀況', time: '2 hours ago' },
      { user: 'PM', text: '這張單請趕快解決', time: '1 hour ago' }
    ]
  })
  
  const newCommentText = ref('')
  
  function quickComment(text) {
    newCommentText.value = text
  }
  
  function submitComment() {
    if (!newCommentText.value.trim()) return
    ticket.value.comments.push({
      user: '你現在登入者',
      time: new Date().toLocaleString(),
      text: newCommentText.value
    })
    newCommentText.value = ''
  }
  
  function getInitials(name) {
    return name.split(' ').map(n => n[0]).join('').toUpperCase()
  }
  
  const allLabels = ref([
    { name: 'Frontend' },
    { name: 'Backend' },
    { name: 'iOS' },
    { name: 'Bug' }
  ])
  
  function addLabel(newTag) {
    const tag = { name: newTag }
    allLabels.value.push(tag)
    ticket.value.labels.push(tag)
  }
  </script>
  
  <style scoped>
  .ticket-detail-layout {
    display: flex;
    height: 100vh;
    overflow: hidden;
    background-color: #f3f4f6;
    color: #333;
    font-family: 'Segoe UI', sans-serif;
  }
  
  .ticket-main {
    flex: 1;
    padding: 24px;
    overflow-y: auto;
    background: white;
  }
  
  .ticket-side {
    width: 320px;
    padding: 24px;
    overflow-y: auto;
    background-color: #f9f9fb;
    border-left: 1px solid #ddd;
  }
  
  .ticket-title {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
    color: #222;
  }
  
  .section {
    margin-bottom: 32px;
  }
  
  .section h3 {
    font-size: 16px;
    margin-bottom: 8px;
    color: #555;
  }
  
  .side-group {
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .side-group label {
    font-size: 13px;
    color: #666;
    margin-bottom: 4px;
  }
  
  input,
  select,
  textarea {
    padding: 6px 10px;
    border-radius: 4px;
    border: 1px solid #ccc;
    background: #fff;
    color: #333;
  }
  
  .multiselect {
    background: #fff;
    border-radius: 4px;
    border: 1px solid #ccc;
    font-size: 14px;
    color: #333;
  }
  
  .multiselect__input,
  .multiselect__single {
    background: transparent;
    color: #333;
  }
  
  .comment-input textarea {
    width: 100%;
    border-radius: 6px;
    padding: 8px;
    background: #fff;
    border: 1px solid #ccc;
    color: #333;
    resize: vertical;
    margin-bottom: 8px;
  }
  
  .suggestions span {
    display: inline-block;
    background: #eee;
    padding: 4px 8px;
    margin: 4px 4px 0 0;
    border-radius: 12px;
    cursor: pointer;
    font-size: 13px;
    color: #555;
  }
  
  .btn-submit {
    padding: 8px 16px;
    background-color: #0052cc;
    color: white;
    border: none;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
    margin-top: 8px;
  }
  
  .btn-submit:hover {
    background-color: #003d99;
  }
  
  .comment-list {
    margin-top: 16px;
  }
  
  .comment-item {
    background: #f5f5f5;
    border-radius: 6px;
    padding: 12px;
    margin-top: 16px;
  }
  
  .comment-header {
    display: flex;
    align-items: center;
    margin-bottom: 6px;
  }
  
  .avatar {
    width: 32px;
    height: 32px;
    background: #bbb;
    color: #fff;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 10px;
    font-size: 14px;
  }
  
  .comment-actions span {
    font-size: 12px;
    color: #777;
    margin-right: 12px;
    cursor: pointer;
  }
  </style>
  