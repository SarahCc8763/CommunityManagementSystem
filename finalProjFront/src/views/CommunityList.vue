<template>

  <div class="container py-4">
    <!-- 🔽 社區選擇 -->


    <h2 class="mb-4">{{ selectedCommunity?.name || '社區' }} - 功能設定</h2>

    <div v-if="selectedCommunity" class="card p-4 shadow-sm bg-light bg-dark text-light">
      <div class="mb-3">
        <p><strong>社區名稱：</strong>{{ selectedCommunity.name }}</p>
        <p><strong>地址：</strong>{{ selectedCommunity.address }}</p>
        <p><strong>創建時間：</strong>{{ formatDate(selectedCommunity.createTime) }}</p>
      </div>

      <div class="function-section">
        <div v-for="module in allFunctionOptions" :key="module.value" class="mb-4 border-bottom pb-3">
          <!-- 主功能 Checkbox -->
          <div class="form-check mb-2">
            <input class="form-check-input" type="checkbox" :id="module.value" :checked="isModuleChecked(module)"
              @change="toggleMainFunction(module.value, module.children)" />
            <label class="form-check-label fw-bold" :for="module.value">
              {{ module.label }}
            </label>
          </div>

          <!-- 子功能列 -->
          <div class="ms-4">
            <div v-for="child in module.children" :key="child.key" class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" :id="child.key"
                :checked="selectedFunctionNames.includes(child.key)" @change="toggleChildFunction(child.key)" />
              <label class="form-check-label" :for="child.key">{{ child.label }}</label>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-4 text-end">
        <button class="btn btn-primary" @click="saveFunction">💾 儲存功能設定</button>
      </div>
    </div>

    <div v-else>載入中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import axios from '@/plugins/axios'
import { useUserStore } from '@/stores/UserStore'
import Swal from 'sweetalert2'


const userStore = useUserStore()

console.log(userStore.rawData.communityId)

// 所有功能定義（主功能與子功能）
const allFunctionOptions = [
  {
    label: '報修系統',
    value: 'TICKET',
    children: [
      { label: '提交報修', key: 'TICKETFORM' },
      { label: '維修進度查詢', key: 'TICKETLIST' },
      { label: '報修內容', key: 'TICKETDETAIL' },
      { label: 'AllTicketsByAssignment', key: 'TICKETASSIGN' },
    ]
  },
  {
    label: '公告系統',
    value: 'NOTICE',
    children: [
      { label: '重要通知', key: 'NOTICEIMPORTANT' },
      { label: '最新公告', key: 'NOTICELATEST' },
      { label: '後臺 - 公告管理', key: 'BULLETINADMIN' },
    ]
  },
  {
    label: '包裹管理',
    value: 'PACKAGE',
    children: [
      { label: '待領包裹', key: 'PACKAGEPENDING' },
      { label: '領取紀錄', key: 'PACKAGEHISTORY' },
      { label: '管理員包裹查詢', key: 'PACKAGESEARCH' },
      { label: '新增包裹', key: 'ADDPACKAGE' },
    ]
  },
  {
    label: '公設預約',
    value: 'BOOKING',
    children: [
      { label: '公設與點數系統', key: 'FHV' },
      { label: '查詢公設', key: 'FFAV' },
      { label: '我的預約紀錄', key: 'RHV' },
      { label: '點數轉移', key: 'PTV' },
      { label: '點數儲值', key: 'PTUV' },
      { label: '點數交易紀錄', key: 'PHV' },
    ]
  },
  {
    label: 'FAQ',
    value: 'FQA',
    children: [
      { label: 'FAQ 問答集', key: 'FAQQANDA' },
      { label: '聯絡客服', key: 'FQACONTACT' },
      { label: '回饋與抱怨？', key: 'FQAFEEDBACK' }, //問題的進度跟進
      { label: '後臺 - FAQ 管理', key: 'FAQADMIN' }, //FAQ後台
      { label: '後臺 - 回饋管理', key: 'FEEDBACKADMIN' }, //回饋後台
    ]
  },
  {
    label: '財務報表',
    value: 'INVOICE',
    children: [
      { label: '繳費總覽', key: 'FINUSER' },
      { label: '待繳帳單', key: 'INVOICEBILL' },
      { label: '繳費紀錄', key: 'RECEIPT' },
      { label: '新增費用類型', key: 'FEETYPEADD' },
      { label: '新增繳費期別', key: 'BILLINGPERIODADD' },
      { label: '新增繳款單', key: 'INVOICEADD' },
      { label: '新增收據', key: 'RECEIPTADD' },
      { label: '請款單審核', key: 'INVOICEVALIDATE' },
      { label: '審核帳單回覆', key: 'INVOICEWITHRESPONSE' },
    ]
  },
  {
    label: '停車管理',
    value: 'PARK',
    children: [
      { label: '社區停車場建置', key: 'PARKINIT' },
      { label: '所有車位查詢', key: 'PARKSLOT' },  // 共用同個路徑去韋韋那頁
      { label: '使用者承租車位', key: 'PARKRENT' },
      { label: '承租記錄查詢', key: 'PARKREC' },
      { label: '抽籤活動', key: 'PARKEVE' },
      { label: '抽籤申請', key: 'PARKAPP' },
      { label: '我的車位', key: 'MYPARK' },
      { label: '前端停車主頁', key: 'PARKFRONT' },
      { label: '後端停車主頁', key: 'PARKBACK' },
    ]
  },
  {
    label: '配合廠商',
    value: 'VENDOR',
    children: [
      { label: '配合廠商', key: 'VENDOR' },
    ]
  }
]

// 狀態資料
// const communities = ref([])
const selectedCommunity = ref(null)
const selectedFunctionNames = ref([])

// 🧠 轉換 bitmask -> 主功能值清單
// function convertFunctionBitToNames(funcBit) {
//   const result = []
//   allFunctionOptions.forEach((module, i) => {
//     const mask = BigInt(1) << BigInt(i)
//     if ((BigInt(funcBit) & mask) !== 0n) {
//       result.push(module.value)
//     }
//   })
//   return result
// }

// 載入社區

onMounted(async () => {
  try {
    const communityId = userStore.rawData?.communityId
    if (!communityId) {
      console.error('❌ 無法取得登入者社區 ID')
      return
    }

    // 從後端取得社區詳細資料
    const res = await axios.get(`/communitys/${communityId}`)
    selectedCommunity.value = res.data

    // 載入該社區的功能設定
    const functionRes = await axios.get(
      `/communitys/functions/${communityId}`
    )
    selectedFunctionNames.value = functionRes.data
  } catch (err) {
    console.error('❌ 載入失敗', err)
  }
})
// 切換社區時更新選項
watch(selectedCommunity, async (newVal) => {
  if (newVal) {
    try {
      const res = await axios.get(
        `/communitys/functions/${newVal.communityId}`
      )
      selectedFunctionNames.value = res.data
    } catch (err) {
      console.error('❌ 載入功能失敗', err)
    }
  }
})

function isModuleChecked(module) {
  return selectedFunctionNames.value.includes(module.value)
}

function toggleMainFunction(value, children = []) {
  const hasMain = selectedFunctionNames.value.includes(value)
  if (!hasMain) {
    selectedFunctionNames.value.push(value)
    children.forEach(child => {
      if (!selectedFunctionNames.value.includes(child.key)) {
        selectedFunctionNames.value.push(child.key)
      }
    })
  } else {
    selectedFunctionNames.value = selectedFunctionNames.value.filter(f =>
      f !== value && !children.map(c => c.key).includes(f)
    )
  }
}

function toggleChildFunction(key) {
  const idx = selectedFunctionNames.value.indexOf(key)
  if (idx === -1) {
    selectedFunctionNames.value.push(key)
  } else {
    selectedFunctionNames.value.splice(idx, 1)
  }
}

async function saveFunction() {
  try {
    const finalFunctions = [...selectedFunctionNames.value]

    // 補上主功能（如子功能有被選，但主功能沒被選）
    allFunctionOptions.forEach(module => {
      const hasChild = module.children.some(child => finalFunctions.includes(child.key))
      if (hasChild && !finalFunctions.includes(module.value)) {
        finalFunctions.push(module.value)
      }
    })

    const payload = {
      name: selectedCommunity.value.name,
      address: selectedCommunity.value.address,
      functions: finalFunctions
    }
    console.log('🧪 最終送出的 functions:', finalFunctions)
    const res = await axios.put(
      `/communitys/${selectedCommunity.value.communityId}`,
      payload
    )
    selectedCommunity.value.function = res.data.function

    window.dispatchEvent(new CustomEvent('refresh-community-functions'))


    Swal.fire({
    icon: 'success',
    title: '儲存成功',
    text: '✅ 功能設定已儲存',
    confirmButtonText: 'OK'
  })
  } catch (err) {
    console.error('❌ 儲存失敗', err)
    Swal.fire({
  icon: 'error',
  title: '儲存失敗',
  text: '❌ 請稍後再試一次',
  confirmButtonText: '知道了'
})
  }
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleString()
}
</script>


<style scoped>
.badge {
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.badge:hover {
  background-color: #444 !important;
}
</style>
