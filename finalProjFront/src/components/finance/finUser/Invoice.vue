<template>
  <div class="invoice-container">
    <h2 class="serif-title">未繳帳單</h2>
    <div v-if="invoices.length === 0" class="no-invoice">
      <i class="bi bi-emoji-smile"></i> 目前沒有未繳帳單！
    </div>
    <div v-else class="invoice-list">
      <div v-for="invoice in invoices" :key="invoice.invoiceId" class="invoice-card">
        <div class="invoice-header">
          <span class="invoice-id">帳單編號：{{ invoice.invoiceId }}</span>
          <span class="amount-due">NT$ {{ invoice.amountDue.toLocaleString() }}</span>
        </div>
        <div class="invoice-details">
          <div class="detail-row">
            <span>單位數：</span>
            <span>{{ invoice.unit_count }}</span>
          </div>
          <div class="detail-row">
            <span>單價：</span>
            <span>NT$ {{ invoice.unitPrice.toLocaleString() }}</span>
          </div>
        </div>
        <button class="pay-btn">去繳費</button>
      </div>
      <div class="total-row">
        <span>總金額：</span>
        <span class="total-amount">NT$ {{ totalAmount.toLocaleString() }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 靜態假資料，未來可用 API 替換
const invoices = ref([
  {
    invoiceId: 1001,
    amountDue: 3200,
    unit_count: 8,
    unitPrice: 400
  },
  {
    invoiceId: 1002,
    amountDue: 1500,
    unit_count: 3,
    unitPrice: 500
  },
  {
    invoiceId: 1003,
    amountDue: 2100,
    unit_count: 7,
    unitPrice: 300
  }
])

const totalAmount = computed(() =>
  invoices.value.reduce((sum, inv) => sum + inv.amountDue, 0)
)
</script>

<style scoped>
.invoice-container {
  max-width: 520px;
  margin: 48px auto 0 auto;
  padding: 32px 20px 24px 20px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(102,126,234,0.10);
  min-height: 400px;
}
.serif-title {
  font-family: 'Merriweather', serif;
  font-size: 2rem;
  font-weight: 800;
  color: #2d3748;
  margin-bottom: 32px;
  text-align: center;
}
.no-invoice {
  text-align: center;
  color: #718096;
  font-size: 1.2rem;
  margin-top: 60px;
}
.no-invoice i {
  font-size: 2.2rem;
  color: #a0aec0;
  margin-bottom: 12px;
}
.invoice-list {
  display: flex;
  flex-direction: column;
  gap: 28px;
}
.invoice-card {
  background: linear-gradient(135deg, #f8fafc 60%, #e9eafc 100%);
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(102,126,234,0.08);
  padding: 24px 20px 18px 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  border: 1.5px solid #e2e8f0;
  position: relative;
}
.invoice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.1rem;
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 8px;
}
.amount-due {
  color: #667eea;
  font-size: 1.2rem;
  font-weight: 800;
}
.invoice-details {
  display: flex;
  gap: 32px;
  font-size: 1rem;
  color: #4a5568;
  margin-bottom: 8px;
}
.detail-row {
  display: flex;
  gap: 8px;
}
.pay-btn {
  align-self: flex-end;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 10px 28px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(102,126,234,0.10);
  transition: background 0.2s, transform 0.2s;
}
.pay-btn:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
  transform: translateY(-2px) scale(1.04);
}
.total-row {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 12px;
  font-size: 1.2rem;
  font-weight: 800;
  color: #2d3748;
  border-top: 2px solid #e2e8f0;
  margin-top: 24px;
  padding-top: 16px;
}
.total-amount {
  color: #d53f8c;
  font-size: 1.4rem;
  font-weight: 900;
}
@media (max-width: 600px) {
  .invoice-container {
    max-width: 100vw;
    padding: 12px 2vw 16px 2vw;
    border-radius: 10px;
  }
  .serif-title {
    font-size: 1.3rem;
    margin-bottom: 18px;
  }
  .invoice-card {
    padding: 14px 8px 12px 8px;
    border-radius: 8px;
  }
  .invoice-details {
    gap: 12px;
    font-size: 0.95rem;
  }
  .pay-btn {
    padding: 8px 16px;
    font-size: 0.95rem;
    border-radius: 8px;
  }
  .total-row {
    font-size: 1rem;
    padding-top: 8px;
  }
  .total-amount {
    font-size: 1.1rem;
  }
}
</style> 