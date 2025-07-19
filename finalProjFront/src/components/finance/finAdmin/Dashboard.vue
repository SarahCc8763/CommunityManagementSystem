<template>
  <div class="admin-dashboard-bg py-5">
    <div class="container-xl" style="margin-left: 20%;">
      <!-- 麵包屑導航 -->
      <nav aria-label="breadcrumb" class="mb-3 ms-1">
        <ol class="breadcrumb mb-0">
          <li class="breadcrumb-item">
            <a href="#" @click="goTo('home')" class="text-decoration-none text-light"><i
                class="bi bi-house-door-fill me-1"></i>首頁</a>
          </li>
          <li class="breadcrumb-item">
            <a href="#" @click="goTo('adminDashboard')" class="text-decoration-none text-light">後台管理</a>
          </li>
          <li class="breadcrumb-item active text-white" aria-current="page">財務後台</li>
        </ol>
      </nav>
      <h2 class="fw-bold text-info mb-4">財務管理員專區</h2>
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

      <!-- 小卡功能區 -->
      <div class="row g-4 mb-5">
        <div class="col-md-4 col-lg-3" v-for="card in cards" :key="card.title">
          <div class="admin-card bg-dark text-light shadow h-100 d-flex flex-column justify-content-between"
            @click="go(card.route)" style="cursor:pointer;">
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

      <!-- 重要提示 -->
      <div class="tips-section">
        <h4 class="fw-bold text-info mb-4">
          <i class="bi bi-lightbulb me-2"></i>
          重要提示與注意事項
        </h4>
        <div class="row g-4">
          <div class="col-md-4">
            <div class="tip-card">
              <div class="tip-icon">
                <i class="bi bi-diagram-3"></i>
              </div>
              <h6 class="fw-bold text-info mb-2">操作流程順序</h6>
              <p class="small text-light mb-0">期別、費用類型、請款單等皆可於上方功能卡進入對應管理頁面，按順序操作更有效率。</p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="tip-card">
              <div class="tip-icon">
                <i class="bi bi-plus-circle"></i>
              </div>
              <h6 class="fw-bold text-info mb-2">臨時加收處理</h6>
              <p class="small text-light mb-0">如需臨時加收費用，請先新增費用類型，再新增期別，最後批次產生請款單。</p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="tip-card">
              <div class="tip-icon">
                <i class="bi bi-shield-lock"></i>
              </div>
              <h6 class="fw-bold text-info mb-2">權限控管</h6>
              <p class="small text-light mb-0">所有操作皆有權限控管，僅管理員可見相關功能，確保系統安全。</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 詳細操作教學 -->
      <div class="mb-5">
        <h3 class="fw-bold text-info mb-4">
          <i class="bi bi-book-half me-2"></i>
          詳細操作教學
        </h3>

        <!-- 期別設定 -->
        <div class="tutorial-card fade-in">
          <div class="tutorial-header" @click="toggleTutorial('period')">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="tutorial-icon">
                  <i class="bi bi-calendar2-week"></i>
                </div>
                <div>
                  <h5 class="mb-1 fw-bold text-info">期別設定管理</h5>
                  <p class="text-secondary mb-0 small">管理各期繳費時間與期別設定</p>
                </div>
              </div>
              <i class="bi bi-chevron-down expand-icon" :class="{ 'rotated': expandedTutorial === 'period' }"></i>
            </div>
          </div>
          <div class="tutorial-content" :class="{ 'expanded': expandedTutorial === 'period' }">
            <div class="tutorial-content-inner">
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-robot text-info me-2"></i>自動生成期別</h6>
                <p class="mb-0 text-light">系統會自動於每半年產生新期別（如1/1、7/1），無需手動干預，確保費用收取時程順暢。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-plus-circle text-info me-2"></i>手動新增期別</h6>
                <p class="mb-0 text-light">如遇特殊需求（如臨時加收、特殊費用），可手動新增期別，靈活應對各種收費情況。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-gear text-info me-2"></i>管理建議</h6>
                <p class="mb-0 text-light">建議提前檢視期別設定，確保收費時程與社區需求一致，避免臨時調整。</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 費用類型設定 -->
        <div class="tutorial-card fade-in">
          <div class="tutorial-header" @click="toggleTutorial('fee')">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="tutorial-icon">
                  <i class="bi bi-cash-coin"></i>
                </div>
                <div>
                  <h5 class="mb-1 fw-bold text-info">費用類型設定</h5>
                  <p class="text-secondary mb-0 small">設定各類收費項目與金額標準</p>
                </div>
              </div>
              <i class="bi bi-chevron-down expand-icon" :class="{ 'rotated': expandedTutorial === 'fee' }"></i>
            </div>
          </div>
          <div class="tutorial-content" :class="{ 'expanded': expandedTutorial === 'fee' }">
            <div class="tutorial-content-inner">
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-list-ul text-info me-2"></i>費用類型管理</h6>
                <p class="mb-0 text-light">請先設定各類費用（如管理費、水電費、停車費等），這是後續操作的基礎。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-pencil text-info me-2"></i>自訂設定項目</h6>
                <p class="mb-0 text-light">可自訂名稱、計費單位、金額等詳細資訊，滿足不同費用類型的需求。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-lightbulb text-info me-2"></i>操作建議</h6>
                <p class="mb-0 text-light">建議先完成費用類型設定，再進行後續的請款單生成，避免操作過程中的困擾。</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 產生請款單 -->
        <div class="tutorial-card fade-in">
          <div class="tutorial-header" @click="toggleTutorial('invoice')">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="tutorial-icon">
                  <i class="bi bi-collection"></i>
                </div>
                <div>
                  <h5 class="mb-1 fw-bold text-info">產生請款單</h5>
                  <p class="text-secondary mb-0 small">快速生成各類繳費通知單</p>
                </div>
              </div>
              <i class="bi bi-chevron-down expand-icon" :class="{ 'rotated': expandedTutorial === 'invoice' }"></i>
            </div>
          </div>
          <div class="tutorial-content" :class="{ 'expanded': expandedTutorial === 'invoice' }">
            <div class="tutorial-content-inner">
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-magic text-info me-2"></i>簡易產生功能</h6>
                <p class="mb-0 text-light">可使用「簡易產生」一鍵產生下月管理費，快速便捷處理常規費用。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-sliders text-info me-2"></i>完整產生模式</h6>
                <p class="mb-0 text-light">用「完整產生」自訂期別、費用類型、對象（可多選住戶）批次產生請款單。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-people text-info me-2"></i>批次處理</h6>
                <p class="mb-0 text-light">支援多選住戶，大量生成請款單，大幅提升工作效率。</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 查看請款單 -->
        <div class="tutorial-card fade-in">
          <div class="tutorial-header" @click="toggleTutorial('view')">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="tutorial-icon">
                  <i class="bi bi-file-earmark-text"></i>
                </div>
                <div>
                  <h5 class="mb-1 fw-bold text-info">查看請款單</h5>
                  <p class="text-secondary mb-0 small">管理與查詢所有請款單資訊</p>
                </div>
              </div>
              <i class="bi bi-chevron-down expand-icon" :class="{ 'rotated': expandedTutorial === 'view' }"></i>
            </div>
          </div>
          <div class="tutorial-content" :class="{ 'expanded': expandedTutorial === 'view' }">
            <div class="tutorial-content-inner">
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-search text-info me-2"></i>請款單查詢</h6>
                <p class="mb-0 text-light">可於「請款單管理」頁面查詢所有已產生的請款單，掌握整體狀況。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-funnel text-info me-2"></i>多條件篩選</h6>
                <p class="mb-0 text-light">並可依期別、費用類型、住戶等條件篩選，快速找到特定資訊。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-eye text-info me-2"></i>狀態追蹤</h6>
                <p class="mb-0 text-light">即時查看請款單狀態，包括已發送、已繳費、待審核等各階段。</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 繳款回覆與審核 -->
        <div class="tutorial-card fade-in">
          <div class="tutorial-header" @click="toggleTutorial('audit')">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <div class="tutorial-icon">
                  <i class="bi bi-clipboard-check"></i>
                </div>
                <div>
                  <h5 class="mb-1 fw-bold text-info">繳款回覆與審核</h5>
                  <p class="text-secondary mb-0 small">審核住戶繳款狀況與處理</p>
                </div>
              </div>
              <i class="bi bi-chevron-down expand-icon" :class="{ 'rotated': expandedTutorial === 'audit' }"></i>
            </div>
          </div>
          <div class="tutorial-content" :class="{ 'expanded': expandedTutorial === 'audit' }">
            <div class="tutorial-content-inner">
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-check-circle text-info me-2"></i>繳款狀態審核</h6>
                <p class="mb-0 text-light">住戶繳款後，管理員可於「繳款回覆」頁面審核繳款狀態。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-hand-index text-info me-2"></i>手動標記功能</h6>
                <p class="mb-0 text-light">並可手動標記已繳/未繳，確保記錄的準確性。</p>
              </div>
              <div class="step-item">
                <h6 class="mb-2"><i class="bi bi-receipt text-info me-2"></i>開立收據</h6>
                <p class="mb-0 text-light">為已繳費住戶開立收款證明，完成整個收費流程。</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center text-secondary small">如有操作疑問，請聯絡系統管理員或參閱後台說明文件。</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const expandedTutorial = ref(null)

const cards = [
  {
    title: '繳費期間設定',
    desc: '設定、檢視所有繳費期別，可自動或手動新增。',
    icon: 'bi bi-calendar2-week',
    route: '/finance/billing-period/add'
  },
  {
    title: '費用項目管理',
    desc: '設定各類費用（如管理費、水電費等），可自訂金額與單位。',
    icon: 'bi bi-cash-coin',
    route: '/finance/fee-type/add'
  },
  {
    title: '繳費通知製作',
    desc: '自訂條件批次產生請款單，支援多選住戶。',
    icon: 'bi bi-collection',
    route: '/finance/invoice/add'
  },
  {
    title: '繳費通知審核',
    desc: '查詢、管理所有已產生的請款單，並核准發送，支援多條件篩選。',
    icon: 'bi bi-file-earmark-text',
    route: '/finance/invoice/validate'
  },
  {
    title: '審核帳單回覆',
    desc: '確認用戶留言，查詢、管理所有已產生的請款單支援多條件篩選。',
    icon: 'bi bi-file-earmark-text',
    route: '/finance/invoice/InvoiceWithResponse'
  },
  {
    title: '收據管理中心',
    desc: '審核住戶繳款狀態，手動標記已繳/未繳。',
    icon: 'bi bi-clipboard-check',
    route: '/finance/receipt/add'
  }

]

function go(route) {
  router.push(route)
}

function toggleTutorial(id) {
  if (expandedTutorial.value === id) {
    expandedTutorial.value = null
  } else {
    expandedTutorial.value = id
  }
}

onMounted(() => {
  // 載入動畫
  const fadeInElements = document.querySelectorAll('.fade-in')
  fadeInElements.forEach((element, index) => {
    element.style.animationDelay = `${index * 0.1}s`
  })
})

const goTo = (target) => {
  switch (target) {
    case 'home':
      router.push('/')
      break
    case 'adminDashboard':
      router.push('/AdminDashboard')
      break
    case 'finBack':
      router.push('/finance/admin-dashboard')
      break
  }
}
</script>

<style scoped>
.step-item {
  background: linear-gradient(135deg, #495057 0%, #343a40 100%);
  border-radius: 10px;
  padding: 1rem;
  margin-bottom: 0.8rem;
  border-left: 4px solid #366aca;
}

.expand-icon {
  transition: transform 0.3s ease;
  color: #2e61cf;
}

.expand-icon.rotated {
  transform: rotate(180deg);
}

.tips-section {
  background: linear-gradient(135deg, #212529 0%, #1a1d20 100%);
  border: 1px solid #495057;
  border-radius: 18px;
  padding: 2rem;
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.tip-card {
  background: #343a40;
  border-radius: 12px;
  padding: 1.5rem;
  height: 100%;
  border: 1px solid #495057;
  transition: all 0.3s ease;
}

.tip-card:hover {
  border-color: #2b62da;
  transform: translateY(-2px);
}

.tip-icon {
  font-size: 2rem;
  color: #68abf7;
  margin-bottom: 1rem;
}

.contact-section {
  background: #212529;
  border: 1px solid #495057;
  border-radius: 18px;
  padding: 2rem;
  margin-top: 2rem;
  text-align: center;
}

.fade-in {
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.6s ease forwards;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

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
  margin-bottom: 1.5rem;
}

.admin-card:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.18);
  border-color: #403a8f;
}

.admin-card-icon {
  font-size: 2.5rem;
  color: #667ca5;
  margin-bottom: 0.5rem;
}

.card {
  border-radius: 18px;
}

.tutorial-card {
  background: #212529;
  border: 1px solid #495057;
  border-radius: 18px;
  transition: all 0.3s ease;
  margin-bottom: 1.5rem;
}

.tutorial-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(13, 202, 240, 0.1);
  border-color: #463bd8;
}

.tutorial-header {
  padding: 1.5rem 1.5rem 1rem 1.5rem;
  cursor: pointer;
  border-bottom: 1px solid #495057;
}

.tutorial-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1764c9 0%, #286ed6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  color: white;
  margin-right: 1rem;
}

.tutorial-content {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.4s ease;
  background: #1a1d20;

  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;

}

.tutorial-content.expanded {
  max-height: 500px;
}

.tutorial-content-inner {
  padding: 1.5rem 1.5rem 1.5rem 2.5rem;
}

h2,
.main-title {
  color: #90caf9 !important;
  text-align: center;
  font-weight: 800;
  letter-spacing: 2px;
  margin-bottom: 2.5rem;
}

h4,
.section-title {
  color: #b39ddb !important;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 1.5rem;
}

h5,
.admin-card .fw-bold {
  color: #e3e3fa !important;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 0.8rem;
  text-align: left;
}

h6,
.step-item h6 {
  color: #b39ddb !important;
  font-weight: 600;
  letter-spacing: 1px;
}

.text-info {
  color: #90caf9 !important;
}

.text-secondary,
.admin-card p,
.tip-card p,
.tutorial-card p,
.step-item p {
  color: #b0b0b0 !important;
}

.text-light {
  color: #e0e0e0 !important;
}

.admin-card .admin-card-icon {
  font-size: 2.5rem;
  color: #b39ddb;
  margin-bottom: 0.5rem;
}

.admin-card .btn-outline-info {
  border-color: #90caf9;
  color: #90caf9;
}

.admin-card .btn-outline-info:hover {
  background: #90caf9;
  color: #23272b;
}

.admin-card p {
  margin-top: 0.5rem;
  font-size: 1rem;
  color: #b0b0b0;
}

.tip-card .tip-icon {
  font-size: 2rem;
  color: #90caf9;
  margin-bottom: 1rem;
}

/* 麵包屑 */
.breadcrumb-item+.breadcrumb-item::before {
  content: ">";
  color: #ccc;
  /* 或 text-light 用於深色背景 */
  margin: 0 0.5rem;
}
</style>