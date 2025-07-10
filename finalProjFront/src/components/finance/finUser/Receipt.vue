<template>
  <div class="container mt-4 receipt-page">
    <h3 class="receipt-title">
      <i class="bi bi-receipt-cutoff me-2"></i>繳費紀錄
    </h3>
    <div v-if="receipts.length === 0" class="alert alert-info text-center py-5">
      <img src="https://cdn-icons-png.flaticon.com/512/4076/4076549.png" alt="no data" style="width:80px;opacity:0.5;">
      <div class="mt-2">目前沒有繳費紀錄</div>
    </div>
    <div v-else class="table-responsive">
      <table class="table receipt-table align-middle shadow-sm rounded">
        <thead class="table-light">
          <tr>
            <th>收據號</th>
            <th>金額</th>
            <th>付款方式</th>
            <th>付款時間</th>
            <th>備註</th>
            <th class="text-center">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in receipts" :key="r.receiptId" class="receipt-row">
            <td>{{ r.receiptNum }}</td>
            <td><b class="text-danger">NT$ {{ r.amountPay?.toLocaleString() }}</b></td>
            <td>{{ r.paymentMethod }}</td>
            <td>{{ formatDate(r.paidAt) }}</td>
            <td>{{ r.note }}</td>
            <td class="text-center">
              <button class="btn btn-outline-primary btn-sm me-1" @click="downloadPDF(r)">
                <i class="bi bi-file-earmark-arrow-down"></i> 下載PDF
              </button>
              <button class="btn btn-outline-secondary btn-sm" @click="printReceipt(r)">
                <i class="bi bi-printer"></i> 列印
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 隱藏列印區域 -->
    <div id="printArea" style="display:none"></div>
    <!-- 專業收據列印區塊 -->
    <div id="receiptPrint" ref="receiptPrintRef" style="display:none">
      <div class="receipt-print-layout">
        <div class="receipt-print-header">收據</div>
        <div class="receipt-print-body">
          <div class="row">
            <div class="col-6">收據號：{{ printData.receiptNum ?? '' }}</div>
            <div class="col-6 text-end">日期：{{ formatDate(printData.paidAt) }}</div>
          </div>
          <div class="row mt-2">
            <div class="col-12">金額：<b class="text-danger">NT$ {{ printData.amountPay?.toLocaleString() }}</b></div>
          </div>
          <div class="row mt-2">
            <div class="col-6">付款方式：{{ printData.paymentMethod ?? '' }}</div>
            <div class="col-6">分期資訊：{{ printData.installments ?? '' }}</div>
          </div>
          <div class="row mt-2">
            <div class="col-12">備註：{{ printData.note ?? '' }}</div>
          </div>
        </div>
        <div class="receipt-print-footer mt-4">
          <div class="row">
            <div class="col-6">收款人：________________</div>
            <div class="col-6 text-end">收款日期：{{ formatDate(printData.paidAt) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosapi from '@/plugins/axios'
import { jsPDF } from 'jspdf'

//這段來抓User跟Community ID
import { useUserStore } from '@/stores/UserStore'
const userStore = useUserStore()
const userId = userStore.userId

const receipts = ref([])
const printData = ref({})
const receiptPrintRef = ref(null)

const fetchReceipts = async () => {
  try {
    const res = await axiosapi.get(`/finance/receipts/user/${userId}`)
    receipts.value = res.data
  } catch (err) {
    console.error('取得收據失敗:', err)
  }
}

onMounted(fetchReceipts)

const downloadPDF = async (r) => {
  const doc = new jsPDF()
  await import('@/assets/fonts/msjh-normal.js')

  doc.setFont('msjh')
  doc.setFontSize(16)
  doc.text('收據', 15, 20)

  doc.setFontSize(12)
  doc.text(`收據號：${r.receiptNum ?? ''}`, 15, 35)
  doc.text(`金額：NT$ ${r.amountPay ?? ''}`, 15, 45)
  doc.text(`付款方式：${r.paymentMethod ?? ''}`, 15, 55)
  doc.text(`付款時間：${formatDate(r.paidAt)}`, 15, 65)
  doc.text(`備註：${r.note ?? ''}`, 15, 75)

  doc.save(`receipt_${r.receiptNum ?? r.receiptId}.pdf`)
}

const printReceipt = (r) => {
  printData.value = { ...r }
  // 顯示 receiptPrint 區塊
  const printDiv = document.getElementById('receiptPrint')
  printDiv.style.display = 'block'
  // 隱藏主頁內容
  const mainContent = document.querySelector('.receipt-page')
  if (mainContent) mainContent.style.display = 'none'
  // 列印
  window.print()
  // 列印後還原
  printDiv.style.display = 'none'
  if (mainContent) mainContent.style.display = ''
}

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return d.getFullYear() + '-' + String(d.getMonth()+1).padStart(2,'0') + '-' + String(d.getDate()).padStart(2,'0') + ' ' + String(d.getHours()).padStart(2,'0') + ':' + String(d.getMinutes()).padStart(2,'0')
}
</script>

<style scoped>
.container.receipt-page {
  max-width: 900px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px 0 #0001;
  padding: 32px 24px 24px 24px;
}
.receipt-title {
  font-weight: bold;
  border-bottom: 2px solid #eee;
  padding-bottom: 8px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  font-size: 1.6rem;
}
.table.receipt-table {
  border-radius: 12px;
  overflow: hidden;
  background: #fafbfc;
  margin-bottom: 0;
}
.table.receipt-table th, .table.receipt-table td {
  vertical-align: middle;
  text-align: center;
}
.table.receipt-table th {
  background: #f4f6fa;
  font-weight: 600;
  font-size: 1rem;
}
.table.receipt-table tbody tr {
  transition: background 0.2s;
}
.table.receipt-table tbody tr:hover {
  background: #f0f7ff;
}
.text-danger {
  color: #d32f2f !important;
  font-weight: bold;
}
@media (max-width: 768px) {
  .container.receipt-page {
    padding: 12px 2px;
  }
  .receipt-title {
    font-size: 1.2rem;
    padding-bottom: 4px;
    margin-bottom: 12px;
  }
  .table.receipt-table th, .table.receipt-table td {
    font-size: 0.95rem;
    padding: 6px 2px;
  }
}
/* 專業收據列印區塊樣式 */
#receiptPrint {
  font-family: 'Arial', 'Microsoft JhengHei', sans-serif;
  background: #fff;
  color: #222;
  width: 420px;
  margin: 0 auto;
  padding: 32px 32px 24px 32px;
  border-radius: 12px;
  box-shadow: 0 2px 16px 0 #0002;
}
.receipt-print-layout {
  width: 100%;
}
.receipt-print-header {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 24px;
  letter-spacing: 4px;
}
.receipt-print-body {
  font-size: 1.1rem;
  margin-bottom: 24px;
}
.receipt-print-footer {
  font-size: 1rem;
  margin-top: 24px;
}
@media print {
  body * {
    visibility: hidden !important;
  }
  #receiptPrint, #receiptPrint * {
    visibility: visible !important;
  }
  #receiptPrint {
    position: absolute;
    left: 0; right: 0; top: 0;
    margin: auto;
    box-shadow: none;
    width: 420px;
    background: #fff;
    color: #222;
    z-index: 9999;
  }
}
</style>