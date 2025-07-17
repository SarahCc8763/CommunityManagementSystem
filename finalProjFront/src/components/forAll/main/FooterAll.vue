<template>
  <footer class="bg-dark text-white pt-5 pb-3">
    <div class="container">
      <!-- 上排：Logo + 簡介 + 快速連結 -->
      <div class="row">
        
        <!-- Logo & 公司介紹 -->
        <div class="col-md-4 mb-4">
          <div class="d-flex align-items-center mb-2">
            <img
            src="https://img.freepik.com/premium-vector/building-logo-design-vector_67715-609.jpg"
            alt="Logo"
            style="width: 50px; height: 50px; border-radius: 8px;"
            class="me-3"
            />
            <h5 class="mb-0">智匯建設</h5>
            <p>打造智慧生活的每一磚一瓦</p>
            <p>Email: <a href="mailto:OO123@gmail.com" class="text-white text-decoration-underline">OO123@gmail.com</a></p>
            <p>電話：02-1234-5678</p>
            <p>地址：台北市信義區松高路66、68號</p>
          </div>
        </div>
        <!-- 快速導覽 -->
        <div class="col-md-2 mb-4">
          <h6 class="text-uppercase">快速導覽</h6>
          <ul class="list-unstyled">
            <li><a href="#" class="text-white text-decoration-none">首頁</a></li>
            <li><a href="#" class="text-white text-decoration-none">最新消息</a></li>
            <li><a href="#" class="text-white text-decoration-none">工程案例</a></li>
            <li><a href="#" class="text-white text-decoration-none">聯絡我們</a></li>
          </ul>
        </div>
        
        <!-- 法律資訊 -->
        <div class="col-md-3 mb-4">
          <h6 class="text-uppercase">法務資訊</h6>
          <ul class="list-unstyled">
            <li><a href="#" class="text-white text-decoration-none">隱私權政策</a></li>
            <li><a href="#" class="text-white text-decoration-none">使用者條款</a></li>
            <li><a href="#" class="text-white text-decoration-none">網站安全聲明</a></li>
            <li><a href="#" class="text-white text-decoration-none">Cookie 政策</a></li>
          </ul>
        </div>
        
        <!-- 電子報訂閱 -->
        <div class="col-md-3 mb-4">
          <h6 class="text-uppercase">訂閱電子報</h6>
          <p class="small">不定期寄送最新優惠與消息</p>
          <form>
            <div class="mb-2">
              <input
              type="email"
              class="form-control form-control-sm"
              placeholder="輸入您的Email"
              />
              <button type="submit" class="btn btn-sm btn-outline-light">訂閱</button>
            </div>
          </form>
        </div>
        
        
        <hr class="border-secondary my-4" />
        
        <!-- 底部：社群圖示與版權 -->
        <div class="row justify-content-between align-items-center">
          <div class="col-md-6 mb-2 mb-md-0">
            <a href="#" class="text-white me-3"><i class="bi bi-facebook"></i></a>
            <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
            <a href="#" class="text-white"><i class="bi bi-line"></i></a>
          </div>
          <div class="col-md-6 text-md-end">
            <small class="text-secondary">&copy; 2025 智匯建設. 保留所有權利。</small>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>
  
<script setup>
</script>
  
<style scoped>
@media (max-width: 576px) {
  footer .row.justify-content-between {
    text-align: center;
  }

  footer .row.justify-content-between .col-md-6 {
    justify-content: center;
    margin-bottom: 1rem;
  }
}

</style>