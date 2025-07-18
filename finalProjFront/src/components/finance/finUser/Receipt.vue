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
            <td><b>NT$ {{ r.amountPay?.toLocaleString() }}</b></td>
            <td>{{ r.paymentMethod }}</td>
            <td>{{ formatDate(r.paidAt) }}</td>
            <td>{{ r.note }}</td>
            <td class="text-center">
              <button class="btn btn-outline-primary btn-sm me-1" @click="downloadPDF(r)">
                <i class="bi bi-file-earmark-arrow-down"></i> 下載PDF
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import axiosapi from '@/plugins/axios'
import { jsPDF } from 'jspdf'
// import '@/assets/fonts/msjh_regular-normal.js'
import { useUserStore } from '@/stores/UserStore'
import JsBarcode from 'jsbarcode'
import Swal from 'sweetalert2'

const userStore = useUserStore()
const receipts = ref([])

const fetchReceipts = async () => {
  try {
    const res = await axiosapi.get(`/finance/receipts/user/${userStore.userId}`)
    receipts.value = res.data
  } catch (err) {
    console.error('取得收據失敗:', err)
  }
}

watch(() => userStore.userId, (newVal) => {
  if (newVal) fetchReceipts()
}, { immediate: true })





const downloadPDF = async (r) => {
  await import('@/assets/fonts/msjh_regular-normal.js')

  // ✅ 顯示 loading SweetAlert2
  Swal.fire({
    title: '下載中...',
    text: '請稍候，正在產生收據 PDF',
    allowOutsideClick: false,
    didOpen: () => {
      Swal.showLoading()
    }
  })

  const doc = new jsPDF()
  doc.setFont('msjh_regular')

  // 載入 logo
  const loadLogo = () => {
    return new Promise((resolve, reject) => {
      const img = new Image()
      img.onload = () => resolve(img)
      img.onerror = reject
      img.src = '/src/assets/images/main/ZhLogo.jpg'
    })
  }

  try {
    const logoImg = await loadLogo()
    // ...（中略，保留原本繪製 PDF 的所有內容）

    // ✅ 儲存 PDF
    doc.save(`receipt_${r.receiptNum || r.receiptId}.pdf`)

    // ✅ 關閉 loading 視窗，並可選擇顯示成功提示
    Swal.close()
    Swal.fire('下載完成', '收據已成功產生並下載', 'success')
  } catch (e) {
    console.error('產生 PDF 失敗:', e)

    // ✅ 發生錯誤時顯示錯誤提示
    Swal.close()
    Swal.fire('錯誤', '下載失敗，請稍後再試', 'error')
  }
}
function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return d.getFullYear() + '-' +
    String(d.getMonth() + 1).padStart(2, '0') + '-' +
    String(d.getDate()).padStart(2, '0') + ' ' +
    String(d.getHours()).padStart(2, '0') + ':' +
    String(d.getMinutes()).padStart(2, '0')
}

</script>
