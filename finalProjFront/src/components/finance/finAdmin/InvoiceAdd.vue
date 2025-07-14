<template>

<div class="w-60 position-relative" style="margin-left: calc(-50vw + 50%); width: 60vw;">
    <BannerImage :imageSrc="OO" heading="繳款單設定" subtext="您可以在此新增繳款單, 透過簡易生成器一鍵生成下月繳款單, 或是自訂繳款單。" textAlign="left" />
  </div>

  <div class="container-xl mt-4">
    <div class="row g-4">
      <!-- 左側：簡易產生功能 -->
      <div class="col-lg-5">
        <div class="card card-unified shadow p-4 h-100 bg-dark text-light">
          <h3 class="mb-4 fw-bold card-title-unified text-info">新增繳款單</h3>
          <form @submit.prevent="submitForm" novalidate>
            <!-- 費用類型 -->
            <div class="mb-3">
              <label class="form-label">費用類型 <span class="text-danger">*</span></label>
              <select v-model="selectedFeeTypeDesc" class="form-select" required @change="onFeeTypeChange">
                <option disabled value="">請選擇費用類型</option>
                <option v-for="ft in feeTypes" :key="ft.feeTypeId" :value="ft.description">
                  {{ ft.description }}
                </option>
              </select>
              <div v-if="selectedFeeType">
                <div class="mt-2 small text-secondary">單位：{{ selectedFeeType.unit }}　每單位費用：{{ selectedFeeType.amountPerUnit }}
                </div>
              </div>
            </div>
            <!-- 期別 -->
            <div class="mb-3">
              <label class="form-label">期別 <span class="text-danger">*</span></label>
              <select v-model="selectedPeriodName" class="form-select" required @change="onPeriodChange">
                <option disabled value="">請選擇期別</option>
                <option v-for="bp in latestBillingPeriods" :key="bp.billingPeriodId" :value="bp.periodName">
                  {{ bp.periodName }}
                </option>
              </select>
              <div v-if="selectedPeriod">
                <div class="mt-2 small text-secondary">
                  起始日：{{ formatDate(selectedPeriod.startDate) }}　結束日：{{ formatDate(selectedPeriod.endDate) }}<br>
                  繳費截止日：{{ formatDateTime(selectedPeriod.dueDate) }}
                </div>
              </div>
            </div>
            <div v-if="successMsg" class="alert alert-success">{{ successMsg }}</div>
            <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>
            <button type="submit" class="btn btn-primary w-100">送出繳款單</button>
          </form>
          <hr>
          <!-- 一鍵產生下月管理費 -->
          <div class="mt-4">
            <button class="btn btn-success w-100" @click="prepareNextMonthManagementFee">一鍵產生下月份管理費</button>
          </div>
          <div v-if="showConfirm" class="alert alert-info mt-3">
            <div>
              將以每 <b>{{ confirmData.unit }}</b> 新台幣 <b>{{ confirmData.amountPerUnit }}</b> 元整產生 <b>{{ confirmData.periodName }}</b> 繳費通知<br>
              期別開始日: {{ formatDate(confirmData.startDate) }}<br>
              期別結束日: {{ formatDate(confirmData.endDate) }}<br>
              繳款期限: {{ formatDateTime(confirmData.dueDate) }}
            </div>
            <div class="mt-2">
              <button class="btn btn-primary me-2" @click="confirmGenerate">確認</button>
              <button class="btn btn-secondary" @click="showConfirm = false">取消</button>
            </div>
          </div>
        </div>
      </div>
      <!-- 右側：完整產生請款單 -->
      <div class="col-lg-7">
        <div class="card card-unified shadow p-4 bg-dark text-light h-100">
          <h3 class="mb-4 fw-bold card-title-unified text-info">完整產生請款單</h3>
          <form @submit.prevent="submitFullForm">
            <div class="row g-3 align-items-end">
              <div class="col-md-6 col-xl-5">
                <label class="form-label">期別 <span class="text-danger">*</span></label>
                <select v-model="fullForm.billingPeriodId" class="form-select bg-dark text-light border-info" required>
                  <option disabled value="">請選擇期別</option>
                  <option v-for="bp in billingPeriods" :key="bp.billingPeriodId" :value="bp.billingPeriodId">
                    {{ bp.periodName }}
                  </option>
                </select>
              </div>
              <div class="col-md-6 col-xl-5">
                <label class="form-label">費用類型 <span class="text-danger">*</span></label>
                <select v-model="fullForm.feeTypeId" class="form-select bg-dark text-light border-info" required>
                  <option disabled value="">請選擇費用類型</option>
                  <option v-for="ft in feeTypes" :key="ft.feeTypeId" :value="ft.feeTypeId">
                    {{ ft.description }}
                  </option>
                </select>
              </div>
              <div class="col-12">
                <label class="form-label">對象搜尋 <span class="text-danger">*</span></label>
                <small class="text-secondary d-block mb-1">可輸入姓名、ID、UnitId、Email、電話等關鍵字</small>
                <input v-model="userSearch" class="form-control bg-dark text-light border-info mb-2" placeholder="ex: 王小明、1001、A101、john@email.com、0912xxxxxx">
                <div class="user-list-scroll border rounded p-2 bg-secondary" style="max-height: 260px; overflow-y: auto;">
                  <table class="table table-dark table-hover table-sm mb-0">
                    <thead>
                      <tr>
                        <th scope="col"></th>
                        <th scope="col">ID</th>
                        <th scope="col">姓名</th>
                        <th scope="col">Email</th>
                        <th scope="col">UnitId</th>
                        <th scope="col">電話</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="user in filteredUsers" :key="user.usersId">
                        <td>
                          <input class="form-check-input" type="checkbox" :id="'user-'+user.usersId" :value="user.usersId" v-model="fullForm.userIds">
                        </td>
                        <td>{{ user.usersId }}</td>
                        <td>{{ user.name }}</td>
                        <td>{{ user.email }}</td>
                        <td>{{ user.unitId || (user.unit && user.unit[0]?.unit) }}</td>
                        <td>{{ user.contactInfo }}</td>
                      </tr>
                      <tr v-if="filteredUsers.length === 0">
                        <td colspan="6" class="text-center text-secondary">查無資料</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div class="mt-4 text-end">
              <button type="submit" class="btn btn-info px-4 text-dark fw-bold">批次產生請款單</button>
            </div>
            <div v-if="fullSuccessMsg" class="alert alert-success mt-3">{{ fullSuccessMsg }}</div>
            <div v-if="fullErrorMsg" class="alert alert-danger mt-3">{{ fullErrorMsg }}</div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axiosapi from '@/plugins/axios'
import { useRouter } from 'vue-router'
import BannerImage from '@/components/forAll/BannerImage.vue'
import OO from '@/assets/images/main/adminBanner.jpg'
const feeTypes = ref([])
const billingPeriods = ref([])
const selectedFeeTypeDesc = ref('')
const selectedFeeType = ref(null)
const selectedPeriodName = ref('')
const selectedPeriod = ref(null)
const form = ref({
  feeTypeId: '',
  billingPeriodId: ''
})
const successMsg = ref('')
const errorMsg = ref('')
const showConfirm = ref(false)
const confirmData = ref({})
const router = useRouter()

// 完整產生請款單區塊
const fullForm = ref({
  billingPeriodId: '',
  feeTypeId: '',
  userIds: []
})
const userSearch = ref('')
const fullSuccessMsg = ref('')
const fullErrorMsg = ref('')
// mock user 資料（請串接真實 API）
const allUsers = ref([])
const filteredUsers = computed(() => {
  if (!userSearch.value) return allUsers.value
  const keyword = userSearch.value.trim().toLowerCase()
  return allUsers.value.filter(u =>
    (u.name && u.name.toLowerCase().includes(keyword)) ||
    (u.usersId && String(u.usersId).includes(keyword)) ||
    (u.email && u.email.toLowerCase().includes(keyword)) ||
    (u.unitId && u.unitId.toLowerCase().includes(keyword)) ||
    (u.unit && u.unit[0]?.unit && u.unit[0].unit.toLowerCase().includes(keyword)) ||
    (u.contactInfo && u.contactInfo.toLowerCase().includes(keyword))
  )
})

onMounted(async () => {
  try {
    const res1 = await axiosapi.get('/finance/fee-types')
    feeTypes.value = res1.data
    const res2 = await axiosapi.get('/finance/billing-periods')
    billingPeriods.value = res2.data
    // 取得所有 user 資料
    const res3 = await axiosapi.get('/users/all')
    allUsers.value = res3.data
  } catch (e) {
    errorMsg.value = '載入資料失敗：' + (e.response?.data?.message || e.message)
  }
})

const latestBillingPeriods = computed(() => {
  // 依 endDate 倒序取最新10筆
  return [...billingPeriods.value]
    .sort((a, b) => new Date(b.endDate) - new Date(a.endDate))
    .slice(0, 10)
})

const onFeeTypeChange = () => {
  selectedFeeType.value = feeTypes.value.find(ft => ft.description === selectedFeeTypeDesc.value)
  form.value.feeTypeId = selectedFeeType.value ? selectedFeeType.value.feeTypeId : ''
}

const onPeriodChange = () => {
  selectedPeriod.value = billingPeriods.value.find(bp => bp.periodName === selectedPeriodName.value)
  form.value.billingPeriodId = selectedPeriod.value ? selectedPeriod.value.billingPeriodId : ''
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString()
}
function formatDateTime(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleString()
}

// 一鍵產生下月管理費
const prepareNextMonthManagementFee = () => {
  // 1. 找管理費的feeType
  const mgmt = feeTypes.value.find(ft => ft.description.includes('管理費'))
  if (!mgmt) {
    errorMsg.value = '找不到管理費費用類型'
    return
  }
  // 2. 找下個月的期別（管理費）
  const now = new Date()
  const nextMonth = new Date(now.getFullYear(), now.getMonth() + 1, 1)
  // 期別名稱/代碼可能格式：2025年8月/25M8/2025-08...，這裡用最接近下月的管理費
  const mgmtPeriods = billingPeriods.value.filter(bp => bp.feeType && bp.feeType.feeTypeId === mgmt.feeTypeId)
  let targetPeriod = null
  let minDiff = Infinity
  for (const bp of mgmtPeriods) {
    const start = new Date(bp.startDate)
    const diff = Math.abs(start.getFullYear() * 12 + start.getMonth() - (nextMonth.getFullYear() * 12 + nextMonth.getMonth()))
    if (diff < minDiff) {
      minDiff = diff
      targetPeriod = bp
    }
  }
  if (!targetPeriod) {
    errorMsg.value = '找不到下月管理費期別，請先建立期別'
    return
  }
  confirmData.value = {
    unit: mgmt.unit,
    amountPerUnit: mgmt.amountPerUnit,
    periodName: targetPeriod.periodName,
    startDate: targetPeriod.startDate,
    endDate: targetPeriod.endDate,
    dueDate: targetPeriod.dueDate,
    feeTypeId: mgmt.feeTypeId,
    billingPeriodId: targetPeriod.billingPeriodId
  }
  showConfirm.value = true
}

const confirmGenerate = async () => {
  try {
    await axiosapi.post('/finance/invoice-generator/generate', {
      feeTypeId: confirmData.value.feeTypeId,
      billingPeriodId: confirmData.value.billingPeriodId
    })
    showConfirm.value = false
    successMsg.value = '新增成功！將導向審核頁面...'
    setTimeout(() => {
      router.push('/finance/invoice-review')
    }, 1200)
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
    showConfirm.value = false
  }
}

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  if (!form.value.feeTypeId || !form.value.billingPeriodId) {
    errorMsg.value = '請選擇費用類型與期別'
    return
  }
  try {
    const res = await axiosapi.post('/finance/invoice-generator/generate', {

      feeType: {
        feeTypeId: form.value.feeTypeId
      },
      billingPeriod: {
        billingPeriodId: form.value.billingPeriodId,
      },
      createdBy: 2
    })
    console.log(res);
    successMsg.value = '新增成功！將導向審核頁面...'
    selectedFeeTypeDesc.value = ''
    selectedFeeType.value = null
    selectedPeriodName.value = ''
    selectedPeriod.value = null
    form.value.feeTypeId = ''
    form.value.billingPeriodId = ''
    setTimeout(() => {
      router.push('/finance/invoice-review')
    }, 1200)
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}

// 完整產生請款單區塊
const submitFullForm = async () => {
  fullSuccessMsg.value = ''
  fullErrorMsg.value = ''
  if (!fullForm.value.billingPeriodId || !fullForm.value.feeTypeId || fullForm.value.userIds.length === 0) {
    fullErrorMsg.value = '請選擇期別、費用類型與至少一位對象'
    return
  }
  try {
    // 請串接真實 API
    // await axiosapi.post('/finance/invoice-generator/batch-generate', {
    //   billingPeriodId: fullForm.value.billingPeriodId,
    //   feeTypeId: fullForm.value.feeTypeId,
    //   userIds: fullForm.value.userIds
    // })
    fullSuccessMsg.value = '批次產生成功！（此為 mock 成功訊息）'
    fullForm.value.billingPeriodId = ''
    fullForm.value.feeTypeId = ''
    fullForm.value.userIds = []
    userSearch.value = ''
  } catch (e) {
    fullErrorMsg.value = '批次產生失敗：' + (e.response?.data?.message || e.message)
  }
}
</script>

<style scoped>
.container-xl {
  max-width: 1300px;
}
.card-unified {
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(60, 60, 60, 0.10);
  padding: 2rem 2rem 2.5rem 2rem;
  margin-bottom: 0;
  margin-left: 10%;
}
.card-title-unified {
  font-size: 1.7rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  letter-spacing: 1px;
}
.user-list-scroll {
  min-height: 60px;
  background: #23272b;
}
.table-dark th, .table-dark td {
  vertical-align: middle;
}
</style>
