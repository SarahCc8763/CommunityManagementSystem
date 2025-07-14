<template>
  <div class="admin-dashboard-bg py-5">
    <div class="container-xl">
      <h2 class="fw-bold text-info mb-4">財務管理員專區</h2>
      <!-- 小卡功能區 -->
      <div class="row g-4 mb-5">
        <div class="col-md-4 col-lg-3" v-for="card in cards" :key="card.title">
          <div class="admin-card bg-dark text-light shadow h-100 d-flex flex-column justify-content-between" @click="go(card.route)" style="cursor:pointer;">
            <div>
              <div class="admin-card-icon mb-3">
                <i :class="card.icon"></i>
              </div>
              <h5 class="fw-bold mb-2 text-info">{{ card.title }}</h5>
              <p class="small mb-0 text-secondary">{{ card.desc }}</p>
            </div>
            <div class="text-end mt-3">
              <button class="btn btn-outline-info btn-sm px-3">前往</button>
            </div>
          </div>
        </div>
      </div>
      <!-- 教學/說明區塊 -->
      <div class="card bg-dark text-light shadow p-4 mb-5">
        <h4 class="fw-bold text-info mb-3">管理員操作教學</h4>
        <ol class="mb-3">
          <li><b>期別設定：</b>系統會自動於每半年產生新期別（如1/1、7/1），如遇特殊需求（如臨時加收、特殊費用），可手動新增期別。</li>
          <li><b>費用類型設定：</b>請先設定各類費用（如管理費、水電費、停車費等），可自訂名稱、單位、金額。</li>
          <li><b>產生請款單：</b>可使用「簡易產生」一鍵產生下月管理費，或用「完整產生」自訂期別、費用類型、對象（可多選住戶）批次產生請款單。</li>
          <li><b>查看請款單：</b>可於「請款單管理」頁面查詢所有已產生的請款單，並可依期別、費用類型、住戶等條件篩選。</li>
          <li><b>繳款回覆與審核：</b>住戶繳款後，管理員可於「繳款回覆」頁面審核繳款狀態，並可手動標記已繳/未繳。</li>
        </ol>
        <ul class="mb-0 text-secondary small">
          <li>期別、費用類型、請款單等皆可於左側功能卡進入對應管理頁面。</li>
          <li>如需臨時加收費用，請先新增費用類型，再新增期別，最後批次產生請款單。</li>
          <li>所有操作皆有權限控管，僅管理員可見。</li>
        </ul>
      </div>
      <div class="text-center text-secondary small">如有操作疑問，請聯絡系統管理員或參閱後台說明文件。</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const cards = [
  {
    title: '期別管理',
    desc: '設定、檢視所有繳費期別，可自動或手動新增。',
    icon: 'bi bi-calendar2-week',
    route: '/finance/billing-period/add'
  },
  {
    title: '費用類型管理',
    desc: '設定各類費用（如管理費、水電費等），可自訂金額與單位。',
    icon: 'bi bi-cash-coin',
    route: '/finance/fee-type/add'
  },
  {
    title: '請款單管理',
    desc: '查詢、管理所有已產生的請款單，支援多條件篩選。',
    icon: 'bi bi-file-earmark-text',
    route: '/finance/invoice/InvoiceWithResponse'
  },
  {
    title: '繳款回覆/審核',
    desc: '審核住戶繳款狀態，手動標記已繳/未繳。',
    icon: 'bi bi-clipboard-check',
    route: '/finance/invoice/validate'
  },
  {
    title: '批次產生請款單',
    desc: '自訂條件批次產生請款單，支援多選住戶。',
    icon: 'bi bi-collection',
    route: '/finance/invoice/add'
  }
]
function go(route) {
  router.push(route)
}
</script>

<style scoped>
.admin-dashboard-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #23272b 0%, #1a1d20 100%);
}
.admin-card {
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(60, 60, 60, 0.13);
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  transition: transform 0.15s, box-shadow 0.15s;
  border: 1.5px solid #23272b;
}
.admin-card:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.18);
  border-color: #0dcaf0;
}
.admin-card-icon {
  font-size: 2.5rem;
  color: #0dcaf0;
  margin-bottom: 0.5rem;
}
.card {
  border-radius: 18px;
}
</style>