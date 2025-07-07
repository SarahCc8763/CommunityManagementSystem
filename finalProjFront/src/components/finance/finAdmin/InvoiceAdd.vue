<template>
  <div class="container mt-4" style="max-width: 600px;">
    <h3 class="mb-4 fw-bold text-primary">新增繳款單</h3>
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
        將以每 <b>{{ confirmData.unit }}</b> 新台幣 <b>{{ confirmData.amountPerUnit }}</b> 元整產生 <b>{{ confirmData.periodName
        }}</b> 繳費通知<br>
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
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axiosapi from '@/plugins/axios'
import { useRouter } from 'vue-router'

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

onMounted(async () => {
  try {
    const res1 = await axiosapi.get('/api/finance/fee-types')
    feeTypes.value = res1.data
    const res2 = await axiosapi.get('/api/finance/billing-periods')
    billingPeriods.value = res2.data
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
    await axiosapi.post('/api/finance/invoice-generator/generate', {
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
    await axiosapi.post('/api/finance/invoice-generator/generate', {
      feeTypeId: form.value.feeTypeId,
      billingPeriodId: form.value.billingPeriodId
    })
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
</script>

<style scoped>
.container {
  max-width: 500px;
}
</style>