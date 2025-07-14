<template>
    <div class="container mt-5">
      <h2 class="mb-4">🛠️ 報修管理功能</h2>
      <div class="row row-cols-1 row-cols-md-2 g-4">
        <div class="col" v-for="feature in features" :key="feature.title">
          <div class="card h-100 shadow-sm">
            <div class="card-body d-flex flex-column justify-content-between">
              <div>
                <h5 class="card-title">{{ feature.title }}</h5>
                <p class="card-text text-muted">{{ feature.description }}</p>
              </div>
              <button class="btn btn-primary mt-3" @click="goTo(feature.route)">
                前往
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const features = [
    {
      title: '提交報修',
      description: '填寫報修表單並上傳圖片說明問題。',
      route: 'TicketForm',
    },
    {
      title: '查詢報修進度',
      description: '查看目前報修單的處理狀態與紀錄。',
      route: 'TicketList',
    },
    {
      title: '維修派工與指派',
      description: '由管理員指派報修案件給負責人員。',
      route: 'AllTicketsByAssignment',
    },
    {
      title: '統計與報表',
      description: '查看各類報修統計、分類與處理效率。',
      route: 'TicketReport',
    },
  ]
  
  const goTo = (routeName) => {
    router.push({ name: routeName })
  }
  </script>
  
  <style scoped>
  .card {
    transition: transform 0.2s ease, box-shadow 0.2s ease;
  }
  .card:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  }
  </style>
  