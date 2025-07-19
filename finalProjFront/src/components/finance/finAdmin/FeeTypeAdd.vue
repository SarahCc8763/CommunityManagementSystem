<template>




  <div style="width: 60vw; max-width: 1200px; margin: 2rem auto 0;">
    <!-- 麵包屑導航 -->
    <nav aria-label="breadcrumb" class="mb-3 ms-1">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('home')" class="text-decoration-none text-light"><i
              class="bi bi-house-door-fill me-1"></i>首頁</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('adminDashboard')" class="text-decoration-none text-light">後台管理</a>
        </li>
        <li class="breadcrumb-item">
          <a href="#" @click="goTo('finBack')" class="text-decoration-none text-light">財務後台</a>
        </li>
        <li class="breadcrumb-item active text-white" aria-current="page">費用項目管理</li>
      </ol>
    </nav>
    <BannerImage :imageSrc="OO" heading="費用項目管理" subtext="管理所有收費項目，可以查看現有費用類別、編輯項目資訊，或新增新的收費項目。" textAlign="left" />
  </div>




  <!-- 新增費用類別 Modal -->
  <div class="modal fade" id="feeTypeModal" tabindex="-1" aria-labelledby="feeTypeModalLabel" aria-hidden="true"
    ref="addModalRef">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <form @submit.prevent="submitForm">
          <div class="modal-header">
            <h5 class="modal-title" id="feeTypeModalLabel">新增費用類別</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" @click="closeAddModal"></button>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label">費用代碼</label>
                  <input v-model="form.feeCode" class="form-control" placeholder="ex: 25WP" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">名稱/描述</label>
                  <input v-model="form.description" class="form-control" placeholder="ex: 第四屆年度歡迎派對" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單價</label>
                  <input v-model.number="form.amountPerUnit" type="number" class="form-control" required
                    placeholder="每單位價格" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單位</label>
                  <select v-model="form.unit" class="form-select">
                    <option value="每戶">每戶</option>
                    <option value="每坪">每坪</option>
                    <option value="每住戶">每住戶</option>
                    <option value="指定參與者">指定參與者</option>
                    <option value="其他">其他</option>
                  </select>
                  <input v-if="form.unit === '其他'" v-model="customUnit" @input="form.unit = customUnit"
                    class="form-control mt-2" placeholder="請輸入其他單位" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">頻率</label>
                  <select v-model="form.frequency" class="form-select">
                    <option value="每月">每月</option>
                    <option value="每季">每季</option>
                    <option value="每年">每年</option>
                    <option value="單次">單次收費</option>
                    <option value="其他">其他</option>
                  </select>
                  <input v-if="form.frequency === '其他'" v-model="customFrequency"
                    @input="form.frequency = customFrequency" class="form-control mt-2" placeholder="請輸入自訂頻率" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">備註</label>
                  <input v-model="form.note" class="form-control" placeholder="ex: 限定現金交易" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">建立者</label>
                  <input v-model="form.createdBy" class="form-control" disabled />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">狀態</label>
                  <select v-model="form.status" class="form-select">
                    <option :value="true">啟用</option>
                    <option :value="false">停用</option>
                  </select>
                </div>
              </div>

              <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
              <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="submit" class="btn btn-primary">送出</button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- 修改用 -->





  <!-- 修改費用類別 Modal -->
  <div class="modal fade" id="editFeeTypeModal" tabindex="-1" aria-labelledby="editFeeTypeModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <form @submit.prevent="submitEditForm">
          <div class="modal-header">
            <h5 class="modal-title" id="editFeeTypeModalLabel">修改費用類別</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label">費用代碼</label>
                  <input v-model="editItem.feeCode" class="form-control" required disabled />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">名稱/描述</label>
                  <input v-model="editItem.description" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單價</label>
                  <input v-model.number="editItem.amountPerUnit" type="number" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單位</label>
                  <input v-model="editItem.unit" class="form-control" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">頻率</label>
                  <input v-model="editItem.frequency" class="form-control" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">備註</label>
                  <input v-model="editItem.note" class="form-control" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">狀態</label>
                  <select v-model="editItem.status" class="form-select">
                    <option :value="true">啟用</option>
                    <option :value="false">停用</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="submit" class="btn btn-primary">儲存修改</button>
          </div>
        </form>
      </div>
    </div>
  </div>



  <!-- 顯示每筆資料 -->
  <div class="container mt-4" :class="{ 'dark-mode': isDarkMode }">

    <div class="d-flex justify-content-between align-items-center mb-3">
      <div class="tag-style px-4 py-2" :class="{ 'dark-mode': isDarkMode }">
        <h4 class="mb-0 fw-bold text-primary section-title">費用類別列表</h4>
      </div>
      <button class="btn btn-success" @click="openAddModal">
        新增費用類別
      </button>
    </div>

    <div class="table-responsive">
      <table class="table align-middle table-hover table-borderless shadow-sm rounded"
        :class="{ 'dark-mode': isDarkMode }">
        <thead class=" text-secondary border-bottom">
          <tr>
            <th scope="col">費用代碼</th>
            <th scope="col">名稱/描述</th>
            <th scope="col">單價</th>
            <th scope="col">單位</th>
            <th scope="col">頻率</th>
            <th scope="col">備註</th>
            <th scope="col" class="text-center">狀態</th>
            <th scope="col" class="text-center">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in feeTypes" :key="item.feeTypeId" class="border-bottom">
            <td>{{ item.feeCode }}</td>
            <td>{{ item.description }}</td>
            <td>{{ item.amountPerUnit }}</td>
            <td>{{ item.unit }}</td>
            <td>{{ item.frequency }}</td>
            <td>{{ item.note }}</td>
            <td class="text-center">
              <span class="badge" :class="item.status ? 'badge-success' : 'badge-danger'">
                {{ item.status ? '啟用' : '停用' }}
              </span>
            </td>
            <td class="text-center">
              <button class="btn btn-sm btn-outline-primary me-1" @click="openEditModal(item)">修改</button>
              <button class="btn btn-sm btn-outline-gray" @click="confirmDelete(item)">刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
  </div>

</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from '@/plugins/axios.js'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js';
import BannerImage from '@/components/forAll/BannerImage.vue'
import OO from '@/assets/images/main/adminBanner.jpg'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const successMsg = ref('')
const errorMsg = ref('')
const customUnit = ref('')
const customFrequency = ref('')
const editItem = ref({})
const deleteTarget = ref(null)
const feeTypes = ref([])
const isDarkMode = computed(() => route.meta?.dark === true)

const getDefaultForm = () => ({
  feeCode: '',
  description: '',
  amountPerUnit: null,
  unit: '',
  frequency: '',
  note: '',
  communityId: userStore.communityId || null,
  status: true,
  createdAt: new Date().toISOString(),
  createdBy: userStore.userId,
  updatedBy: userStore.userId,
})

const form = ref(getDefaultForm())

const fetchFeeTypes = async () => {
  try {
    successMsg.value = ''
    errorMsg.value = ''
    const response = await axios.get('/finance/fee-types')
    feeTypes.value = response.data
  } catch (e) {
    errorMsg.value = '載入費用類別失敗：' + (e.response?.data?.message || e.message)
  }
}


const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    // 顯示 loading 狀態
    Swal.fire({
      title: '新增中...',
      text: '系統正在儲存費用類型，請稍候',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading()
      }
    })

    // 發送請求
    await axios.post('/finance/fee-types', form.value)

    // 關閉 loading
    Swal.close()

    // 顯示成功提示
    await Swal.fire({
      icon: 'success',
      title: '新增成功',
      text: '費用類型已成功建立！',
      timer: 1000,
      showConfirmButton: false
    })

    // 重置表單
    form.value = getDefaultForm()

    // 關閉 modal
    addModalInstance.hide()

    // 重新載入資料
    await fetchFeeTypes()

  } catch (e) {
    // 關閉 loading
    Swal.close()

    // 錯誤處理
    let msg = '新增失敗：' + (e.response?.data?.message || e.message)
    if (e.response?.status === 409) {
      msg = '新增失敗：費用代碼已存在，請改用其他代碼'
    }

    // 顯示錯誤提示
    Swal.fire({
      icon: 'error',
      title: '錯誤',
      text: msg
    })

    errorMsg.value = msg
  }
}


import Swal from 'sweetalert2'

const submitEditForm = async () => {
  try {
    // 關閉 modal
    const modalEl = document.getElementById('editFeeTypeModal')
    const modal = bootstrap.Modal.getInstance(modalEl)
    modal?.hide()

    // 顯示 loading 中
    Swal.fire({
      title: '更新中...',
      text: '正在儲存費用類型的變更，請稍候',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading()
      }
    })

    // 發送修改請求
    await axios.put(`/finance/fee-types/${editItem.value.feeTypeId}`, editItem.value)

    // 關閉 loading
    Swal.close()

    // 顯示成功訊息
    await Swal.fire({
      icon: 'success',
      title: '修改成功',
      text: '費用類型已成功更新',
      timer: 2000,
      showConfirmButton: false
    })


    // 重新取得資料
    await fetchFeeTypes()

  } catch (e) {
    // 關閉 loading
    Swal.close()

    // 顯示錯誤訊息
    Swal.fire({
      icon: 'error',
      title: '修改失敗',
      text: e.response?.data?.message || e.message || '發生未知錯誤'
    })
  }
}


const confirmDelete = (item) => {
  if (window.confirm(`確定要刪除費用代碼「${item.feeCode}」嗎？`)) {
    deleteFeeType(item.feeTypeId)
  }
}

const deleteFeeType = async (id) => {
  try {
    await axios.delete(`/finance/fee-types/${id}`)
    successMsg.value = '刪除成功'
    await fetchFeeTypes()
  } catch (e) {
    errorMsg.value = '刪除失敗：' + (e.response?.data?.message || e.message)
  }
}

const goTo = (target) => {
  switch (target) {
    case 'home':
      router.push('/')
      break
    case 'adminDashboard':
      router.push('/AdminDashboard')
      break
    case 'finBack':
      router.push('/finance/admin-dashboard')
      break
  }
}

const addModalRef = ref(null)
let addModalInstance = null

onMounted(() => {
  fetchFeeTypes()
  addModalInstance = new bootstrap.Modal(addModalRef.value)
  form.value.communityId = userStore.communityId
})

const openAddModal = () => {
  form.value = getDefaultForm()
  addModalInstance.show()
}

const closeAddModal = () => {
  addModalInstance.hide()
}

const openEditModal = (item) => {
  editItem.value = { ...item }
  const modal = new bootstrap.Modal(document.getElementById('editFeeTypeModal'))
  modal.show()
}
</script>

<style scoped>
.container {
  max-width: 1150px;
  padding-top: 30px;
  border-radius: 15px;
}

/* 麵包屑 */
.breadcrumb-item+.breadcrumb-item::before {
  content: ">";
  color: #ccc;
  /* 或 text-light 用於深色背景 */
  margin: 0 0.5rem;
}

input::placeholder,
textarea::placeholder,
select::placeholder {
  color: #999;
  /* ← 請替換你想要的顏色 */
  opacity: 1;
  /* 修正某些瀏覽器預設半透明 */
}
</style>
