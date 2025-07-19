<template>
  <div class="modal-mask" @click.self="$emit('close')">
    <div class="modal-container p-4 bg-dark text-light">
      <h4 class="mb-3">📝 報修單詳細資訊</h4>
      <p class="d-flex align-items-center">
        <strong class="me-2">標題：</strong>
        <span class="me-auto">{{ ticket.title }}</span>
        <span class="badge" :class="{
          'bg-secondary': ticket.status === 'to do',
          'bg-warning text-dark': ticket.status === 'In Progress',
          'bg-success': ticket.status === 'Done'
        }">
          {{ formatStatus(ticket.status) }}
        </span>
      </p>



      <p><strong>通報人：</strong>{{ ticket.name }}</p>
      <p><strong>指派人：</strong>{{ ticket.assigneeName ?? '（未知）' }}</p>
      <p><strong>建立時間：</strong>{{ formatDate(ticket.startDate) }}</p>
      <p><strong>描述：</strong> <span v-html="ticket.issueDescription || '無'"></span></p>

      <!-- ✅ 問題種類 -->
      <!-- 問題種類 -->
      <div class="mb-3">
        <p><strong>問題種類：</strong></p>
        <div v-if="editMode">
          <div class="dropdown">
            <div class="mt-2">
              <span v-for="id in editedIssueTypeIds" :key="'issue-selected-' + id"
                class="badge rounded-pill bg-info me-2">
                {{ getIssueTypeNameById(id) }}
                <span class="ms-1" style="cursor: pointer;" @click.stop="removeIssueType(id)">×</span>
              </span>
            </div>
            <button class="btn custom-select-btn" type="button" @click="showIssueTypeDropdown = !showIssueTypeDropdown">
  選擇問題種類
</button>
            <div class="dropdown-menu show" v-if="showIssueTypeDropdown">
              <div v-for="type in issueTypeOptions" :key="type.id" class="dropdown-item"
                @click="toggleIssueType(type.id)">
                <span :class="['me-2', editedIssueTypeIds.includes(type.id) ? 'text-primary fw-bold' : 'text-muted']">
                  {{ type.issueTypeName }}
                </span>
              </div>
            </div>

            <!-- 選取顯示 tag -->

          </div>
        </div>

        <div v-else>
          <div v-if="ticket.issueTypes?.length">
            <span v-for="(rel, i) in ticket.issueTypes" :key="i" class="badge bg-info me-2">
              {{ rel.issueType?.issueTypeName }}
            </span>
          </div>
          <p v-else class="text-muted">無</p>
        </div>
      </div>


      <!-- ✅ 已指派廠商 -->
      <div class="mb-3">
        <p><strong>已指派廠商：</strong></p>
        <div v-if="editMode">
          <div class="dropdown">
            <div class="mt-2">
              <span v-for="id in editedVendorIds" :key="'vendor-selected-' + id" class="badge bg-primary me-1">
                {{ vendorMap[id] || '未知廠商' }}
              </span>
            </div>
            <button class="btn custom-select-btn" type="button" @click="showVendorDropdown = !showVendorDropdown">
  選擇廠商
</button>
            <div class="dropdown-menu show" v-if="showVendorDropdown">
              <div v-for="vendor in vendorList" :key="vendor.vendorID" class="dropdown-item">
                <input type="checkbox" :id="'vendor-' + vendor.vendorID" :value="vendor.vendorID"
                  v-model="editedVendorIds" />
                <label :for="'vendor-' + vendor.vendorID">{{ vendor.vendorName }} - {{ vendor.contactPerson }}</label>
              </div>
            </div>

          </div>
        </div>
        <div v-else>
          <ul class="list-unstyled">
            <li v-for="id in ticket.assignedVendorIds ?? []" :key="id">
              {{ vendorMap[id] || '未知廠商' }}
            </li>
          </ul>
        </div>
      </div>
      <!-- ✅ 附件圖片（縮圖 + 點擊可預覽） -->
      <div class="mb-3" v-if="ticket.attachments?.length">
        <p><strong>附件圖片：</strong></p>
        <div class="d-flex flex-wrap gap-2">
          <img v-for="(img, i) in ticket.attachments" :key="i" :src="`data:image/png;base64,${img.file}`"
            :alt="img.fileName" class="rounded border"
            style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" @click="openPreview(img)" />
        </div>
      </div>

      <!-- ✅ 圖片預覽彈窗 -->
      <div v-if="previewImage" class="image-preview-overlay" @click="closePreview">
        <img :src="previewImage" class="image-preview" @click.stop />
      </div>

      <!-- ✅ 控制按鈕 -->
      <div class="text-end">
        <button
  v-if="!editMode" class="animated-btn custom-edit-btn" @click="editMode = true">✏️ 編輯</button>
        <button v-else class="btn custom-save-btn" @click="submitUpdate">💾 儲存</button>
        <button class="btn custom-close-btn" @click="$emit('close')">關閉</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '@/plugins/axios'
import Swal from 'sweetalert2'

const props = defineProps({
  ticket: Object,
  vendorList: Array
})
const emit = defineEmits(['close', 'update-ticket'])
const editMode = ref(false)
const editedVendorIds = ref([])
const editedIssueTypeIds = ref([])
const issueTypeOptions = ref([])

const showVendorDropdown = ref(false)
const showIssueTypeDropdown = ref(false)
const previewImage = ref(null)


function formatStatus(status) {
  switch (status) {
    case "to do": return '待處理'
    case "In Progress": return '處理中'
    case "Done": return '已完成'
    default: return '未知'
  }
}


onMounted(() => {
  editedVendorIds.value = props.ticket.assignedVendorIds ? [...props.ticket.assignedVendorIds] : []
  editedIssueTypeIds.value = props.ticket.issueTypes?.map(rel => rel.issueType?.id).filter(Boolean) || []
  console.log('初始 issueTypes：', props.ticket.issueTypes)
  fetchIssueTypes()
})

async function fetchIssueTypes() {
  const res = await axios.get('/IssueTypes')
  issueTypeOptions.value = res.data
}

const vendorMap = computed(() => {
  const map = {}
  props.vendorList.forEach(v => {
    map[v.vendorID] = v.vendorName
  })
  return map
})

function getIssueTypeNameById(id) {
  const found = issueTypeOptions.value.find(opt => opt.id === id)
  return found?.issueTypeName || '未知'
}

function toggleIssueType(id) {
  const index = editedIssueTypeIds.value.indexOf(id)
  if (index === -1) {
    editedIssueTypeIds.value.push(id)
  } else {
    editedIssueTypeIds.value.splice(index, 1)
  }
}



function openPreview(img) {
  previewImage.value = `data:image/png;base64,${img.file}`
}

function closePreview() {
  previewImage.value = null
}



function removeIssueType(id) {
  editedIssueTypeIds.value = editedIssueTypeIds.value.filter(i => i !== id)
}

async function submitUpdate() {
  try {
    await axios.put(`/ticket-issue/update/${props.ticket.id}`, editedIssueTypeIds.value)
    await axios.put(`/TicketToAdministrator/ticket-vendors/update/${props.ticket.id}`, editedVendorIds.value)


    // 🔁 重新拉資料，與你其他頁面邏輯一致
    const res = await axios.get(`/ticket/${props.ticket.id}`)
    const fixedData = {
      ...res.data,
      assignedVendorIds: [...editedVendorIds.value] // 🟣手動補充資料，畫面會顯示
    }
    emit('update-ticket', fixedData) // 替換內容，畫面會 reactive 更新
    console.log(fixedData)

    await Swal.fire({
      icon: 'success',
      title: '✅ 更新成功',
      text: '報修單內容已更新',
      confirmButtonText: '好的'
    })
    editMode.value = false
    showVendorDropdown.value = false
    showIssueTypeDropdown.value = false
  } catch (err) {
    console.error('❌ 更新失敗', err)
    Swal.fire({
      icon: 'error',
      title: '❌ 更新失敗',
      text: '請檢查網路連線或資料格式',
      confirmButtonText: '確定'
    })
  }
}


function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 1050;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-container {
  background: white;
  max-width: 600px;
  width: 90%;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.dropdown-menu.show {
  max-height: 200px;
  overflow-y: auto;
  padding: 0.5rem;
  border: 1px solid #ccc;
  background: white;
  z-index: 10;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.25rem 0;
}

.preview-img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
  cursor: pointer;
  transition: transform 0.2s;
}

.preview-img:hover {
  transform: scale(1.05);
}

.image-wrapper {
  width: 110px;
  text-align: center;
}

.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.image-preview {
  max-width: 90vw;
  max-height: 90vh;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.6);
}
.custom-select-btn {
  background: linear-gradient(to right, #6fb1fc, #4364f7);
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: background 0.3s ease;
  margin-top: 0.5rem;
}

.custom-select-btn:hover {
  background: linear-gradient(to right, #5aa0f2, #3659e3);
  color: white;
}
.custom-close-btn {
  background-color: #2c2f36; /* 深灰藍色 */
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  transition: background 0.3s ease;
}

.custom-close-btn:hover {
  background-color: #1f2127;
  color: white;
}
.custom-save-btn {
  background: linear-gradient(to right, #00c9a7, #007d77);
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: background 0.3s ease;
}

.custom-save-btn:hover {
  background: linear-gradient(to right, #00b297, #006c67);
  color: white;
}
.custom-edit-btn{
  background-color: #2c2f36; /* 深灰藍色 */
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  transition: background 0.3s ease;
}
.custom-edit-btn:hover {
  background-color: #1f2127;
  color: white;
}
</style>
