<template>
  <div class="container mt-4">
    <h3>新增收據</h3>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label class="form-label">發票ID</label>
        <input v-model.number="form.invoiceId" type="number" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">付款方式</label>
        <input v-model="form.paymentMethod" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">付款時間</label>
        <input v-model="form.paidAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">扣款時間</label>
        <input v-model="form.debitAt" type="datetime-local" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">實付金額</label>
        <input v-model.number="form.amountPay" type="number" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">分期資訊</label>
        <input v-model="form.installments" class="form-control" />
      </div>
      <div class="mb-3">
        <label class="form-label">備註</label>
        <input v-model="form.note" class="form-control" />
      </div>
      <button type="submit" class="btn btn-primary">送出</button>
      <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
      <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
    </form>

    <!-- 收據預覽卡片 -->
    <div v-if="receiptDetail" class="receipt-preview my-5" ref="receiptRef">
      <div class="receipt-title">{{ communityName }} 收據</div>
      <div class="receipt-row"><span>收據號碼：</span>{{ receiptDetail.receiptNum || receiptDetail.receiptId }}</div>
      <div class="receipt-row"><span>住戶姓名：</span>{{ receiptDetail.userName }}</div>
      <div class="receipt-row"><span>地址：</span>{{ receiptDetail.address }}</div>
      <div class="receipt-row"><span>發票ID：</span>{{ receiptDetail.invoiceId }}</div>
      <div class="receipt-row"><span>費用類型：</span>{{ receiptDetail.feeType }}</div>
      <div class="receipt-row"><span>期別：</span>{{ receiptDetail.periodName }}</div>
      <div class="receipt-row"><span>實付金額：</span><b class="text-danger">NT$ {{ receiptDetail.amountPay?.toLocaleString() }}</b></div>
      <div class="receipt-row"><span>付款方式：</span>{{ receiptDetail.paymentMethod }}</div>
      <div class="receipt-row"><span>付款時間：</span>{{ formatDate(receiptDetail.paidAt) }}</div>
      <div class="receipt-row"><span>扣款時間：</span>{{ formatDate(receiptDetail.debitAt) }}</div>
      <div class="receipt-row"><span>分期資訊：</span>{{ receiptDetail.installments }}</div>
      <div class="receipt-row"><span>備註：</span>{{ receiptDetail.note }}</div>
      <div class="receipt-row"><span>經手人：</span>{{ receiptDetail.createdBy || '管理員' }}</div>
      <div class="receipt-footer">收款日期：{{ formatDate(receiptDetail.createdAt) }}　　收款人簽章：</div>
    </div>
    <div v-if="receiptDetail" class="mb-4">
      <button class="btn btn-outline-primary me-2" @click="printReceipt">列印收據</button>
      <button class="btn btn-outline-success" @click="downloadPDF">下載PDF</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axiosapi from '@/plugins/axios'
import html2pdf from 'html2pdf.js'

const route = useRoute()
const form = ref({
  invoiceId: null,
  paymentMethod: '',
  paidAt: '',
  debitAt: '',
  amountPay: null,
  installments: '',
  note: '',
})

const successMsg = ref('')
const errorMsg = ref('')
const receiptDetail = ref(null)
const communityName = ref('')
const receiptRef = ref(null)

onMounted(async () => {
  // 若有query參數自動帶入
  if (route.query.invoiceId) form.value.invoiceId = Number(route.query.invoiceId)
  // 可根據userId查詢用戶資訊自動顯示於收據
  // 若需自動查詢發票資訊，可在此加axios查詢
})

const submitForm = async () => {
  successMsg.value = ''
  errorMsg.value = ''
  try {
    const res = await axiosapi.post('/api/finance/receipts', form.value)
    successMsg.value = '新增成功！'
    // 取得收據詳細資料
    const receiptId = res.data.receiptId || res.data.id
    const detailRes = await axiosapi.get(`/api/finance/receipts/${receiptId}`)
    receiptDetail.value = detailRes.data
    // 查社區名稱
    if (detailRes.data.communityId) {
      const commRes = await axiosapi.get(`/communitys/${detailRes.data.communityId}`)
      communityName.value = commRes.data.name
    } else {
      communityName.value = ''
    }
  } catch (e) {
    errorMsg.value = '新增失敗：' + (e.response?.data?.message || e.message)
  }
}

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleString()
}

function printReceipt() {
  const printContent = receiptRef.value
  const win = window.open('', '', 'width=600,height=800')
  win.document.write('<html><head><title>列印收據</title>')
  win.document.write('<style>body{font-family:sans-serif;} .receipt-preview{max-width:420px;margin:0 auto;padding:24px;border:1.5px solid #aaa;border-radius:12px;} .receipt-title{text-align:center;font-size:1.5rem;font-weight:700;margin-bottom:18px;} .receipt-row{margin-bottom:8px;font-size:1.1rem;} .receipt-footer{margin-top:24px;text-align:right;font-size:1.1rem;} @media print{body{background:#fff;}}</style>')
  win.document.write('</head><body>')
  win.document.write(printContent.outerHTML)
  win.document.write('</body></html>')
  win.document.close()
  win.focus()
  setTimeout(()=>{win.print();win.close()}, 500)
}

function downloadPDF() {
  html2pdf(receiptRef.value, {
    margin: 10,
    filename: '收據.pdf',
    image: { type: 'jpeg', quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: 'mm', format: 'a5', orientation: 'portrait' }
  })
}
</script>

<style scoped>
.container { max-width: 500px; }
.receipt-preview {
  max-width: 420px;
  margin: 0 auto;
  padding: 24px 28px 18px 28px;
  border: 1.5px solid #aaa;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 4px 16px rgba(102,126,234,0.08);
  font-family: 'Noto Sans TC', 'Microsoft JhengHei', sans-serif;
}
.receipt-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 18px;
  letter-spacing: 2px;
}
.receipt-row {
  margin-bottom: 8px;
  font-size: 1.1rem;
  letter-spacing: 1px;
}
.receipt-footer {
  margin-top: 24px;
  text-align: right;
  font-size: 1.1rem;
}
@media print {
  body * { visibility: hidden; }
  .receipt-preview, .receipt-preview * { visibility: visible !important; }
  .receipt-preview { position: absolute; left: 0; top: 0; width: 100vw; background: #fff; box-shadow: none; }
}
</style> 