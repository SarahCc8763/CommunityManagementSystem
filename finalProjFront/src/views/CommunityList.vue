<template>

  <div class="container py-4">
    <!-- 🔽 社區選擇 -->


    <h2 class="mb-4">🏘 {{ selectedCommunity?.name || '社區' }} - 功能設定</h2>

    <div v-if="selectedCommunity" class="card p-4 shadow-sm bg-light">
      <div class="mb-3">
        <p><strong>社區名稱：</strong>{{ selectedCommunity.name }}</p>
        <p><strong>地址：</strong>{{ selectedCommunity.address }}</p>
        <p><strong>創建時間：</strong>{{ formatDate(selectedCommunity.createTime) }}</p>
      </div>

      <div class="function-section">
        <div
          v-for="module in allFunctionOptions"
          :key="module.value"
          class="mb-4 border-bottom pb-3"
        >
          <!-- 主功能 Checkbox -->
          <div class="form-check mb-2">
            <input
              class="form-check-input"
              type="checkbox"
              :id="module.value"
              :checked="isModuleChecked(module)"
              @change="toggleMainFunction(module.value, module.children)"
            />
            <label class="form-check-label fw-bold" :for="module.value">
              {{ module.value }} (主項)
            </label>
          </div>

          <!-- 子功能列 -->
          <div class="ms-4">
            <div
              v-for="child in module.children"
              :key="child.key"
              class="form-check form-check-inline"
            >
              <input
                class="form-check-input"
                type="checkbox"
                :id="child.key"
                :checked="selectedFunctionNames.includes(child.key)"
                @change="toggleChildFunction(child.key)"
              />
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
import axios from 'axios'
import { useUserStore } from '@/stores/UserStore'


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
      { label: 'CommunityList', key: 'TICKETCOMMUNITY' }
    ]
  },
  {
    label: '公告系統',
    value: 'NOTICE',
    children: [
      { label: '重要通知', key: 'NOTICEIMPORTANT' },
      { label: '最新公告', key: 'NOTICELATEST' }
    ]
  },
  {
    label: '包裹管理',
    value: 'PACKAGE',
    children: [
      { label: '待領包裹', key: 'PACKAGEPENDING' },
      { label: '領取紀錄', key: 'PACKAGEHISTORY' }
    ]
  },
  {
    label: '公設預約',
    value: 'BOOKING',
    children: [
      { label: '健身房預約', key: 'BOOKINGGYM' },
      { label: '游泳池預約', key: 'BOOKINGPOOL' },
      { label: '停車預約', key: 'BOOKINGPARKING' }
    ]
  },
  {
    label: 'FAQ',
    value: 'FQA',
    children: [
      { label: 'FAQ 問答集', key: 'FAQQANDA' },
      { label: '聯絡客服', key: 'FQACONTACT' },
      { label: '回饋與抱怨？', key: 'FQAFEEDBACK' }
    ]
  },
  {
    label: '社區活動',
    value: 'MANBERSERVICE',
    children: [
      { label: '會員資訊修改', key: 'MANBERSERVICEEDIT' },
      { label: '點數轉贈', key: 'MANBERSERVICETRANSFER' }
    ]
  },
  {
    label: '財務報表',
    value: 'INVOICE',
    children: [
      { label: '待繳帳單', key: 'INVOICEBILL' },
      { label: '繳費紀錄', key: 'INVOICEHISTORY' },
      { label: '新增費用類型', key: 'INVOICETYPEADD' },
      { label: '新增繳費期別', key: 'INVOICEPERIODADD' },
      { label: '新增發票', key: 'INVOICEINVOICEADD' },
      { label: '新增收據', key: 'INVOICERECEIPTADD' },
      { label: '發票回覆', key: 'INVOICEREPLY' }
    ]
  },
  {
    label: '停車管理',
    value: 'PARK',
    children: [
    { label: '社區停車場建置', key: 'PARKINIT'},
      { label: '所有車位查詢', key: 'PARKSLOT'},  // 共用同個路徑去韋韋那頁
      { label: '使用者承租車位', key: 'PARKRENT'},
      { label: '承租記錄查詢', key: 'PARKREC'},
      { label: '抽籤活動', key: 'PARKEVE'},
      { label: '抽籤申請', key: 'PARKAPP'},
      { label: '我的車位', key: 'MYPARK'},
      { label: '前端停車主頁', key: 'PARKFRONT'},
      { label: '後端停車主頁', key: 'PARKBACK'},
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
    const res = await axios.get(`http://localhost:8080/communitys/${communityId}`)
    selectedCommunity.value = res.data

    // 載入該社區的功能設定
    const functionRes = await axios.get(
      `http://localhost:8080/communitys/functions/${communityId}`
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
        `http://localhost:8080/communitys/functions/${newVal.communityId}`
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

    const res = await axios.put(
      `http://localhost:8080/communitys/${selectedCommunity.value.communityId}`,
      payload
    )
    selectedCommunity.value.function = res.data.function

    window.dispatchEvent(new CustomEvent('refresh-community-functions'))


    alert('✅ 功能設定已儲存')
  } catch (err) {
    console.error('❌ 儲存失敗', err)
    alert('❌ 儲存失敗')
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
