<template>
  <div class="w-60 position-relative" style="margin-left: calc(-50vw + 50%); width: 60vw;">
    <BannerImage :imageSrc="bannerImg" heading="繳費期別管理" subtext="您可以在此檢視、管理所有繳費期別，或新增新期別。" textAlign="left" />
  </div>

  <!-- 新增繳費期別 Modal -->
  <div class="modal fade" id="billingPeriodModal" tabindex="-1" aria-labelledby="billingPeriodModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <form @submit.prevent="submitForm">
          <div class="modal-header">
            <h5 class="modal-title" id="billingPeriodModalLabel">新增繳費期別</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" />
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label">期別名稱</label>
                  <div class="d-flex gap-2 align-items-center">
                    <select v-model="formYear" class="form-select" style="max-width: 100px;">
                      <option v-for="y in yearOptions" :key="y" :value="y">{{ y }}年</option>
                    </select>

                    <select v-model="formMonthOrQuarter" class="form-select" style="max-width: 80px;">
                      <option v-for="n in (formType === 'M' ? 12 : 4)" :key="n" :value="n">{{ formType === 'M' ? n + '月'
                        :
                        '第'+n+'季' }}</option>
                    </select>

                    <select v-model="formType" class="form-select" style="max-width: 80px;">
                      <option value="M">月</option>
                      <option value="Q">季</option>
                    </select>


                  </div>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">期別代碼</label>
                  <input v-model="form.periodCode" class="form-control" required readonly />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">開始日期</label>
                  <input v-model="form.startDate" type="date" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">結束日期</label>
                  <input v-model="form.endDate" type="date" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">繳費截止日</label>
                  <input v-model="form.dueDate" type="datetime-local" class="form-control" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">備註</label>
                  <input v-model="form.note" class="form-control" />
                </div>


                <div class="col-md-6 mb-3">
                  <label class="form-label">狀態</label>
                  <select v-model="form.status" class="form-select">
                    <option :value="true">啟用</option>
                    <option :value="false">停用</option>
                  </select>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">建立者</label>
                  <input v-model="form.createdBy" class="form-control" />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">更新者ID</label>
                  <input v-model.number="form.updatedBy" type="number" class="form-control" />
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

  <!-- 顯示最新10筆繳費期別 -->
  <div class="container mt-4" :class="{ 'dark-mode': isDarkMode }">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <div class="tag-style px-4 py-2" :class="{ 'dark-mode': isDarkMode }">
        <h4 class="mb-0 fw-bold text-primary section-title">繳費期別列表</h4>
      </div>
      <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#billingPeriodModal">
        新增繳費期別
      </button>
    </div>
    <div class="row mb-3 g-2 align-items-end flex-wrap">
      <div class="col-lg-3 col-md-6 col-12">
        <label class="form-label mb-1">期別ID</label>
        <input v-model="searchId" type="number" class="form-control" placeholder="請輸入期別ID" />
      </div>
      <div class="col-lg-4 col-md-6 col-12">
        <label class="form-label mb-1">期別名稱</label>
        <input v-model="searchName" class="form-control" placeholder="請輸入期別名稱（如2024年6月）" />
      </div>
      <div class="col-lg-5 col-12">
        <label class="form-label mb-1">期別代碼</label>
        <div class="input-group flex-nowrap period-code-group">
          <span class="input-group-text">年份</span>
          <input v-model="searchYear" class="form-control year-input" style="max-width: 110px; min-width: 80px;"
            disabled />
          <span class="input-group-text">月份/季</span>
          <select v-model="searchMonth" class="form-select month-select" style="max-width: 110px; min-width: 80px;">
            <option value="">--</option>
            <option v-for="n in 12" :key="n" :value="n">{{ n }}</option>
          </select>
          <select v-model="searchType" class="form-select type-select" style="max-width: 110px; min-width: 80px;">
            <option value="M">月</option>
            <option value="Q">季</option>
          </select>
          <button class="btn btn-primary" @click="searchBillingPeriod" type="button">搜尋</button>
        </div>
      </div>
    </div>
    <div v-if="searchResult">
      <div class="alert alert-info mb-2">搜尋結果</div>
      <table class="table align-middle table-hover table-borderless shadow-sm rounded"
        :class="{ 'dark-mode': isDarkMode }">
        <thead class="text-secondary border-bottom">
          <tr>
            <th scope="col">期別名稱</th>
            <th scope="col">期別代碼</th>
            <th scope="col">開始日</th>
            <th scope="col">結束日</th>
            <th scope="col">截止日</th>

            <th scope="col">狀態</th>
            <th scope="col">備註</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ searchResult.periodName }}</td>
            <td>{{ searchResult.periodCode }}</td>
            <td>{{ searchResult.startDate }}</td>
            <td>{{ searchResult.endDate }}</td>
            <td>{{ searchResult.dueDate }}</td>

            <td>
              <span class="badge" :class="searchResult.status ? 'badge-success' : 'badge-secondary'">
                {{ searchResult.status ? '啟用' : '停用' }}
              </span>
            </td>
            <td>{{ searchResult.note }}</td>
          </tr>
        </tbody>
      </table>
      <button class="btn btn-link" @click="clearSearch">回到最新10筆</button>
    </div>
    <div v-else>
      <div class="table-responsive">
        <table class="table align-middle table-hover table-borderless shadow-sm rounded"
          :class="{ 'dark-mode': isDarkMode }">
          <thead class="text-secondary border-bottom">
            <tr>
              <th scope="col">期別名稱</th>
              <th scope="col">期別代碼</th>
              <th scope="col">開始日</th>
              <th scope="col">結束日</th>
              <th scope="col">截止日</th>

              <th scope="col">狀態</th>
              <th scope="col">備註</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in latestBillingPeriods" :key="item.billingPeriodId" class="border-bottom">
              <td>{{ item.periodName }}</td>
              <td>{{ item.periodCode }}</td>
              <td>{{ item.startDate }}</td>
              <td>{{ item.endDate }}</td>
              <td>{{ item.dueDate }}</td>

              <td>
                <span class="badge" :class="item.status ? 'badge-success' : 'badge-secondary'">
                  {{ item.status ? '啟用' : '停用' }}
                </span>
              </td>
              <td>{{ item.note }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import axiosapi from '@/plugins/axios'
import BannerImage from '@/components/forAll/BannerImage.vue'
import bannerImg from '@/assets/images/main/adminBanner.jpg'
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { useUserStore } from '@/stores/UserStore'
const userStore = useUserStore()

const successMsg = ref('')
const errorMsg = ref('')
const getDefaultForm = () => ({
  periodName: '',
  periodCode: '',
  startDate: '',
  endDate: '',
  dueDate: '',
  note: '',
  communityId: userStore.communityId,
  status: true,
  createdAt: new Date().toISOString(),
  createdBy: userStore.userId,
  updatedBy: userStore.userId,
});
const form = ref(getDefaultForm());
const billingPeriods = ref([])
const route = useRoute()
const isDarkMode = computed(() => route.meta?.dark === true)
const searchId = ref('')
const searchName = ref('')
const searchYear = ref(new Date().getFullYear())
const searchMonth = ref('')
const searchType = ref('M')
const searchResult = ref(null)

const now = new Date();
const yearOptions = Array.from({ length: 5 }, (_, i) => now.getFullYear() - 2 + i);
const formYear = ref(now.getFullYear());
const formType = ref('M');
const formMonthOrQuarter = ref(1);

watch([formYear, formType, formMonthOrQuarter], ([y, t, m]) => {
  if (t === 'M') {
    form.value.periodName = `${y}年${m}月`;
    const yearShort = String(y).slice(-2);
    form.value.periodCode = `${yearShort}M${m}`;
  } else {
    form.value.periodName = `${y}年第${m}季`;
    const yearShort = String(y).slice(-2);
    form.value.periodCode = `${yearShort}Q${m}`;
  }
});

const fetchBillingPeriods = async () => {

  try {
    const res = await axiosapi.get('/finance/billing-periods')
    billingPeriods.value = res.data || []
  } catch (e) {
    errorMsg.value = '載入失敗：' + (e.response?.data?.message || e.message)
  }
}

const latestBillingPeriods = computed(() => {
  if (Array.isArray(billingPeriods.value)) {
    return billingPeriods.value.slice(-10).reverse()
  } else {
    return []
  }
})
const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  form.value.communityId = userStore.communityId
  form.value.createdBy = userStore.userId
  form.value.updatedBy = userStore.userId
  form.value.createdAt = new Date().toISOString()
  try {
    await axiosapi.post('/finance/billing-periods/create', form.value)
    successMsg.value = '新增成功！'
    const modalEl = document.getElementById('billingPeriodModal')
    const modal = bootstrap.Modal.getInstance(modalEl)
    modal?.hide()
    fetchBillingPeriods()
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}

const searchBillingPeriod = async () => {
  errorMsg.value = ''
  searchResult.value = null
  if (!searchId.value && !searchName.value && !searchMonth.value) {
    errorMsg.value = '請輸入查詢條件'
    return
  }
  try {
    const params = {}
    if (searchId.value) params.billingPeriodId = searchId.value
    if (searchName.value) params.periodName = searchName.value
    if (searchMonth.value) {
      let yearShort = String(searchYear.value).slice(-2)
      let code = ''
      if (searchType.value === 'M') {
        code = `${yearShort}M${searchMonth.value}`
      } else if (searchType.value === 'Q') {
        code = `${yearShort}Q${searchMonth.value}`
      }
      params.periodCode = code
    }
    const res = await axiosapi.get('/finance/billing-periods/query', { params })
    if (res.data) {
      searchResult.value = res.data
    } else {
      errorMsg.value = '查無資料'
    }
  } catch (e) {
    errorMsg.value = '查詢失敗：' + (e.response?.data?.message || e.message)
  }
}

const clearSearch = () => {
  searchId.value = ''
  searchName.value = ''
  searchMonth.value = ''
  searchType.value = 'M'
  searchResult.value = null
  errorMsg.value = ''
}

onMounted(() => {
  fetchBillingPeriods()
  // 初始化 periodName/periodCode
  if (formType.value === 'M') {
    form.value.periodName = `${formYear.value}年${formMonthOrQuarter.value}月`;
    const yearShort = String(formYear.value).slice(-2);
    form.value.periodCode = `${yearShort}M${formMonthOrQuarter.value}`;
  } else {
    form.value.periodName = `${formYear.value}年第${formMonthOrQuarter.value}季`;
    const yearShort = String(formYear.value).slice(-2);
    form.value.periodCode = `${yearShort}Q${formMonthOrQuarter.value}`;
  }
})
</script>

<style scoped>
.container {
  max-width: 900px;
}

.input-group>.form-label {
  margin-bottom: 0;
}

.period-code-group>.input-group-text,
.period-code-group>.form-control,
.period-code-group>.form-select {
  margin-right: 8px;
}

.period-code-group>.form-control,
.period-code-group>.form-select {
  min-width: 80px;
  max-width: 110px;
}

.period-code-group>.year-input {
  min-width: 100px;
  max-width: 120px;
}

.period-code-group>.month-select,
.period-code-group>.type-select {
  min-width: 100px;
  max-width: 120px;
}

@media (max-width: 991px) {

  .period-code-group>.input-group-text,
  .period-code-group>.form-control,
  .period-code-group>.form-select {
    margin-right: 4px;
  }

  .period-code-group>.form-control,
  .period-code-group>.form-select {
    min-width: 70px;
    max-width: 100px;
  }
}
</style>