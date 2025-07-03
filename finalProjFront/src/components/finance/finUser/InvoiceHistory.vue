<template>
  <div class="container mt-4">
    <h3>繳費紀錄/已繳發票</h3>
    <div v-if="receipts.length === 0" class="alert alert-info">目前沒有繳費紀錄</div>
    <table v-else class="table table-bordered">
      <thead>
        <tr>
          <th>收據號</th>
          <th>金額</th>
          <th>付款方式</th>
          <th>付款時間</th>
          <th>備註</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in receipts" :key="r.receiptId">
          <td>{{ r.receiptNum }}</td>
          <td>NT$ {{ r.amountPay }}</td>
          <td>{{ r.paymentMethod }}</td>
          <td>{{ r.paidAt ? r.paidAt.replace('T', ' ').slice(0, 16) : '' }}</td>
          <td>{{ r.note }}</td>
          <td>
            <button class="btn btn-outline-primary btn-sm me-1" @click="downloadPDF(r)">下載PDF</button>
            <button class="btn btn-outline-secondary btn-sm" @click="printReceipt(r)">列印</button>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 隱藏列印區域 -->
    <div id="printArea" style="display:none"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import jsPDF from 'jspdf'

const receipts = ref([])

const fetchReceipts = async () => {
  // 假設已登入用戶ID為1，實際應從登入狀態取得
  const userId = 1
  const res = await axios.get(`/finance/receipts/user/${userId}`)
  receipts.value = res.data
}

onMounted(fetchReceipts)

const downloadPDF = (r) => {
  const doc = new jsPDF()
  doc.setFontSize(16)
  doc.text('收據', 15, 20)
  doc.setFontSize(12)
  doc.text(`收據號：${r.receiptNum || ''}`, 15, 35)
  doc.text(`金額：NT$ ${r.amountPay || ''}`, 15, 45)
  doc.text(`付款方式：${r.paymentMethod || ''}`, 15, 55)
  doc.text(`付款時間：${r.paidAt ? r.paidAt.replace('T', ' ').slice(0, 16) : ''}`, 15, 65)
  doc.text(`備註：${r.note || ''}`, 15, 75)
  doc.save(`receipt_${r.receiptNum || r.receiptId}.pdf`)
}

const printReceipt = (r) => {
  const printArea = document.getElementById('printArea')
  printArea.innerHTML = `
    <div style='font-family:Arial;padding:24px;'>
      <h2>收據</h2>
      <div>收據號：${r.receiptNum || ''}</div>
      <div>金額：NT$ ${r.amountPay || ''}</div>
      <div>付款方式：${r.paymentMethod || ''}</div>
      <div>付款時間：${r.paidAt ? r.paidAt.replace('T', ' ').slice(0, 16) : ''}</div>
      <div>備註：${r.note || ''}</div>
    </div>
  `
  printArea.style.display = 'block'
  window.print()
  printArea.style.display = 'none'
}
</script>

<style scoped>
.container { max-width: 700px; }
</style> 