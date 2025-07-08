<template>

  <div class="w-60 position-relative" style="margin-left: calc(-50vw + 50%); width: 60vw;">
    <BannerImage :imageSrc="OO" heading="費用類別設定" subtext="您可以在此管理所有費用類別，包括檢視現有項目、修改資料，或新增新費用類別。" textAlign="left" />
  </div>




  <!-- 新增費用類別 Modal -->
  <div class="modal fade" id="feeTypeModal" tabindex="-1" aria-labelledby="feeTypeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <form @submit.prevent="submitForm">
          <div class="modal-header">
            <h5 class="modal-title" id="feeTypeModalLabel">新增費用類別</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" />
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label">費用代碼</label>
                  <input v-model="form.feeCode" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">名稱/描述</label>
                  <input v-model="form.description" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單價</label>
                  <input v-model.number="form.amountPerUnit" type="number" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">單位</label>
                  <select v-model="form.unit" class="form-select">
                    <option value="每戶">每戶</option>
                    <option value="每坪">每坪</option>
                    <option value="每住戶">每住戶</option>
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
                    <option value="其他">其他</option>
                  </select>
                  <input v-if="form.frequency === '其他'" v-model="customFrequency"
                    @input="form.frequency = customFrequency" class="form-control mt-2" placeholder="請輸入自訂頻率" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">備註</label>
                  <input v-model="form.note" class="form-control" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">社區ID</label>
                  <input v-model.number="form.communityId" type="number" class="form-control" />
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
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" />
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
      <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#feeTypeModal">
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
              <span class="badge" :class="item.status ? 'badge-success' : 'badge-secondary'">
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
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/plugins/axios.js'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js';
import BannerImage from '@/components/forAll/BannerImage.vue'
import OO from '@/assets/images/main/adminBanner.jpg'

const successMsg = ref('')
const errorMsg = ref('')
const customUnit = ref('')
const customFrequency = ref('')
const editItem = ref({})
const deleteTarget = ref(null)
const feeTypes = ref([])

const route = useRoute()
const isDarkMode = computed(() => route.meta?.dark === true)

const form = ref({
  feeCode: '',
  description: '',
  amountPerUnit: null,
  unit: '',
  frequency: '',
  note: '',
  communityId: null,
  status: true,
})

const fetchFeeTypes = async () => {
  try {
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
    await axios.post('/finance/fee-types', form.value)
    successMsg.value = '新增成功！'
    form.value = {
      feeCode: '',
      description: '',
      amountPerUnit: null,
      unit: '',
      frequency: '',
      note: '',
      communityId: null,
      status: true
    }
    const modalEl = document.getElementById('feeTypeModal')
    const modal = bootstrap.Modal.getInstance(modalEl)
    modal?.hide()
    await fetchFeeTypes()
  } catch (e) {
    if (e.response?.status === 409) {
      errorMsg.value = '新增失敗：費用代碼已存在，請改用其他代碼'
    } else {
      errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
    }
  }
}

const openEditModal = (item) => {
  editItem.value = { ...item }
  const modal = new bootstrap.Modal(document.getElementById('editFeeTypeModal'))
  modal.show()
}

const submitEditForm = async () => {
  try {
    await axios.put(`/finance/fee-types/${editItem.value.feeTypeId}`, editItem.value)
    successMsg.value = '修改成功！'
    setTimeout(() => {
      const modalEl = document.getElementById('editFeeTypeModal')
      const modal = bootstrap.Modal.getInstance(modalEl)
      modal?.hide()
      successMsg.value = ''
    }, 2000)
    await fetchFeeTypes()
  } catch (e) {
    errorMsg.value = '修改失敗：' + (e.response?.data?.message || e.message)
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

onMounted(fetchFeeTypes)
</script>
