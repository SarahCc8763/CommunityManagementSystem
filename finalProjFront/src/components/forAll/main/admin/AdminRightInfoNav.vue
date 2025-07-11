<template>
  <aside class="admin-right-info-nav">
    <h3 class="info-title">社區資訊總覽</h3>
    <div class="info-list">
      <div class="info-item">
        <span class="info-label">社區名稱</span>
        <span class="info-value">River Bank-水岸晴川</span>
      </div>
      <div class="info-item">
        <span class="info-label">電話</span>
        <span class="info-value">02-1234-5678</span>
      </div>
      <div class="info-item">
        <span class="info-label">總人數</span>
        <span class="info-value">128</span>
      </div>
      <div class="info-item">
        <span class="info-label">總車位數</span>
        <span class="info-value">80</span>
      </div>
      <div class="info-item">
        <span class="info-label">下個繳款截止日</span>
        <span class="info-value">2024-07-31</span>
      </div>
      <div class="info-item">
        <span class="info-label">未繳帳單數</span>
        <span class="info-value">7</span>
      </div>
      <div class="info-item">
        <span class="info-label">車位出租人數</span>
        <span class="info-value">18</span>
      </div>
      <div class="info-row-cards">
        <div class="mini-card">
          <div class="mini-label">總房數</div>
          <div class="mini-value">72</div>
        </div>
        <div class="mini-card">
          <div class="mini-label">入住戶數</div>
          <div class="mini-value">60</div>
        </div>
      </div>
      <div class="mini-card" style="margin-top: 8px;">
        <div class="mini-label">本月報修單據 / 解決數量</div>
        <div class="mini-value">23 / 19</div>
      </div>
    </div>

  </aside>
</template>

<script setup>
//我可不可以先寫死資料就好 QwQ 
</script>

<style scoped>
.admin-right-info-nav {
  position: fixed;
  top: 80px;
  right: 0;
  width: 320px;
  height: calc(100vh - 80px);
  background: linear-gradient(135deg, #232a36 0%, #232a36 100%);
  color: #e0e6ed;
  box-shadow: -4px 0 24px rgba(80, 120, 255, 0.10);
  z-index: 2000;
  padding: 24px 18px 18px 18px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-left: 2.5px solid #4a5670;
}

.info-title {
  font-size: 1.32rem;
  font-weight: 900;
  color: #fff;
  letter-spacing: 1.5px;
  margin-bottom: 18px;
  font-family: 'Noto Serif TC', 'Noto Sans TC', 'Segoe UI', 'Arial', serif;
  background: linear-gradient(90deg, #4a5a7a 60%, #667eea 100%);
  padding: 12px 18px 10px 14px;
  border-radius: 8px;
  box-shadow: 0 2px 8px #232a3640;
  border-left: 5px solid #a3bffa;
}

.info-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-item {
  display: flex;
  align-items: flex-end;
  background: none;
  border-radius: 0;
  box-shadow: none;
  padding: 0 0 6px 0;
  font-size: 0.98rem;
  font-weight: 600;
  letter-spacing: 0.5px;
  margin-bottom: 0;
  border-bottom: 1px solid #3a4060;
  transition: border 0.18s;
}

.info-label {
  flex: 1 1 60%;
  color: #a3bffa;
  font-weight: 700;
  font-size: 0.98rem;
  padding-right: 8px;
}

.info-value {
  flex: 0 0 auto;
  color: #fff;
  font-size: 1.05rem;
  font-weight: 900;
}

.info-row-cards {
  display: flex;
  gap: 10px;
  margin: 10px 0 8px 0;
  width: 100%;
  justify-content: space-between;
}

.mini-card {
  background: #2a3140;
  border-left: 3px solid #a3bffa;
  padding: 10px 12px 8px 12px;
  font-size: 0.98rem;
  color: #e0e6ed;
  font-weight: 600;
  min-width: 0;
  flex: 1 1 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 0;
  box-shadow: none;
}

.mini-label {
  color: #a3bffa;
  font-size: 0.93rem;
  font-weight: 700;
  margin-bottom: 2px;
}

.mini-value {
  color: #fff;
  font-size: 1.08rem;
  font-weight: 900;
}
</style>