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
import ZhLogo from '@/assets/images/main/ZhLogo.jpg'


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

  // ✅ 顯示 SweetAlert 等待視窗
  Swal.fire({
    title: '下載準備中...',
    text: '請稍候，正在產生收據 PDF',
    allowOutsideClick: false,
    didOpen: () => {
      Swal.showLoading()
    }
  })

  try {
    // ✅ 載入字體（SweetAlert 保持開啟中）   
    await import('@/assets/fonts/msjh_regular-normal.js')
    const doc = new jsPDF()
    doc.setFont('msjh_regular')

    // 載入 logo
    const loadLogo = () => {
      return new Promise((resolve, reject) => {
        const img = new Image()
        img.onload = () => resolve(img)
        img.onerror = reject
        // img.src = '/src/assets/images/main/ZhLogo.jpg'
        img.src = ZhLogo
      })
    }
    console.log('1')
    const logoImg = await loadLogo()
    console.log('2')
    // 白底全頁
    doc.setFillColor(255, 255, 255)
    doc.rect(0, 0, 210, 297, 'F')

    // 灰色 header（高度 35）
    doc.setFillColor(240, 240, 240)
    doc.rect(0, 0, 210, 35, 'F')
    console.log('3')
    // 插入 logo
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d')
    canvas.width = logoImg.width
    canvas.height = logoImg.height
    ctx.drawImage(logoImg, 0, 0)
    const logoDataUrl = canvas.toDataURL('image/jpeg')
    doc.addImage(logoDataUrl, 'JPEG', 15, 11, 30, 15)
    console.log('4')
    // 標題
    doc.setFontSize(18)
    doc.setTextColor(0, 0, 0)
    doc.text('智匯建設', 55, 18)
    doc.setFontSize(12)
    doc.text('繳費收據', 55, 25)

    // 收據號與日期
    doc.setFontSize(9)
    doc.text(`收據號碼: ${r.receiptNum || ''}`, 140, 20)
    doc.text(`開立日期: ${formatDate(r.paidAt)}`, 140, 25)

    // 社區區塊
    doc.setFontSize(14)
    doc.text('River Bank社區', 105, 55, { align: 'center' })
    doc.setFontSize(10)
    doc.text('103台北市大同區環河北路一段113號', 105, 60, { align: 'center' })

    // 分隔線
    doc.setDrawColor(0, 0, 0)
    doc.setLineWidth(0.5)
    doc.line(15, 75, 195, 75)
    console.log('5')
    // 資訊區塊
    let y = 90
    const leftX = 25
    const rightX = 110
    const lineHeight = 15

    doc.setFontSize(12)
    doc.text('付款方式：', leftX, y)
    doc.text(r.paymentMethod || '', leftX + 30, y)

    doc.text('收款時間：', rightX, y)
    doc.text(formatDate(r.paidAt), rightX + 30, y)

    y += lineHeight
    doc.text('分期資訊：', leftX, y)
    doc.text(r.installments || '一次付清', leftX + 30, y)

    doc.text('繳費金額：', rightX, y)
    doc.text(`NT$ ${r.amountPay?.toLocaleString() || '0'}`, rightX + 30, y)

    y += lineHeight
    doc.text('備註說明：', leftX, y)
    doc.text(r.note || '無', leftX + 30, y)

    // 簽名區
    y += 30
    doc.line(leftX, y, leftX + 60, y)
    doc.line(rightX, y, rightX + 60, y)
    doc.setFontSize(10)
    doc.text('收款人簽名', leftX, y + 8)
    doc.text('客戶簽名', rightX, y + 8)
    console.log('6')
    // 條碼區
    const barcodeCanvas = document.createElement('canvas')
    JsBarcode(barcodeCanvas, r.receiptNum || 'RB000000001', {
      format: 'CODE128',
      width: 2,
      height: 40,
      displayValue: false
    })
    console.log('7')
    const barcodeDataUrl = barcodeCanvas.toDataURL('image/png')
    doc.addImage(barcodeDataUrl, 'PNG', 25, 170, 40, 10)

    // Footer
    y = 250
    doc.setFillColor(245, 245, 245)
    doc.rect(15, y, 180, 22, 'F')
    doc.setFontSize(11)
    doc.setTextColor(0, 0, 0)
    doc.text('智匯建設', 105, y + 8, { align: 'center' })
    doc.setFontSize(9)
    doc.text('打造台灣最值得信賴的建築品牌', 105, y + 15, { align: 'center' })
    console.log('8')
    // ✅ 儲存 PDF 並結束 loading
    doc.save(`receipt_${r.receiptNum || r.receiptId}.pdf`)
    Swal.close()
    Swal.fire('下載完成', '收據已成功產生並下載', 'success')
  } catch (e) {
    console.error('產生 PDF 失敗:', e)
    Swal.close()
    Swal.fire('錯誤', '下載失敗，請稍後再試', 'error')
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
