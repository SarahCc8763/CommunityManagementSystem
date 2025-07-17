<template>
  <div class="reset-password-container">
    <h2>重設密碼</h2>

    <form @submit.prevent="handleResetPassword">
      <!-- 信箱 -->
      <div class="mb-3" >
        <label for="email" class="form-label">請輸入信箱</label>
        <input type="email" id="email" v-model="form.email" class="form-control" placeholder="輸入您的註冊信箱" />
        <div v-if="errors.email" class="text-danger">{{ errors.email }}</div>
      </div>

      <!-- 新密碼 -->
      <div class="mb-3">
        <label for="password" class="form-label">請輸入新密碼</label>
        <input type="password" id="password" v-model="form.password" class="form-control" placeholder="輸入新密碼" />
        <div v-if="errors.password" class="text-danger">{{ errors.password }}</div>
      </div>

      <!-- 再次輸入新密碼 -->
      <div class="mb-3">
        <label for="confirmPassword" class="form-label">再次輸入新密碼</label>
        <input type="password" id="confirmPassword" v-model="form.confirmPassword" class="form-control"
          placeholder="再次輸入新密碼" />
        <div v-if="errors.confirmPassword" class="text-danger">{{ errors.confirmPassword }}</div>
      </div>

      <button type="submit" class="btn btn-primary w-100">
        確認更新
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Swal from 'sweetalert2'
import axios from '@/plugins/axios'
import { useRouter } from 'vue-router'
const path = import.meta.VITE_API_URL
const router = useRouter();

const form = ref({
  email: '',
  password: '',
  confirmPassword: ''
})

const errors = ref({})

const handleResetPassword = async () => {
  errors.value = {}

  if (!form.value.email.trim()) {
    errors.value.email = '請輸入信箱'
  }
  if (!form.value.password.trim()) {
    errors.value.password = '請輸入新密碼'
  }
  if (!form.value.confirmPassword.trim()) {
    errors.value.confirmPassword = '請再次輸入新密碼'
  }
  if (
    form.value.password.trim() &&
    form.value.confirmPassword.trim() &&
    form.value.password !== form.value.confirmPassword
  ) {
    errors.value.confirmPassword = '兩次輸入的密碼不一致'
  }

  if (Object.keys(errors.value).length > 0) {
    return
  }

  try {
    const response = await axios.post(`/users/resetPassword`, {
      "email": form.value.email,
      "newPassword": form.value.password
    })
console.log(response);
    // 後端成功回傳
    if (response.data.success) {
      console.log(response.data);
      await Swal.fire({
        text: response.data.message,
        icon: "success",
      });
      router.push({ path: '/BeforeLogIn' })
      window.dispatchEvent(new CustomEvent('show-login-modal'))
    } else {
      // 後端回傳失敗訊息
      Swal.fire({
        text: response.data.message,
        icon: "warning",
      });
      errors.value.general = response.data.message || '更改失敗'
    }
  } catch (error) {
    Swal.fire({
      text: error.response?.data?.message || '伺服器錯誤，請稍後再試',
      icon: "error",
    });
    console.error('更改失敗:', error)
    errors.value.general = '伺服器錯誤，請稍後再試'
  }
  // finally {
  //   isLoading.value = false
  // }
}
</script>

<style scoped>
/* .reset-password-container {
  width: 100%;
  max-width: 800px;  
  margin: 100px auto;
  padding: 3rem 3rem;
  border: 1px solid #ddd;
  border-radius: 16px;
  background: #fefefe; 
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
} */
.reset-password-container {
  display: block;
  width: 100%;
  max-width: 800px;
  min-width: 600px;
  /* 最小寬度撐住 */
  margin: 80px auto;
  padding: 3rem;
  background: #fefefe;
  border: 1px solid #ddd;
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
}


.reset-password-container h2 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
  color: #222;
}


.mb-3 label {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.form-control {
  width: 100%;
  border-radius: 6px;
  padding: 10px 12px;
  border: 1px solid #ccc;
  transition: border-color 0.2s, box-shadow 0.2s;
  background-color: #fff !important;
  color: #000 !important;
}

.form-control:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.text-danger {
  font-size: 0.9rem;
  margin-top: 0.25rem;
}

.btn-primary {
  width: 100%;
  padding: 12px 0;
  font-weight: 600;
  font-size: 1.5rem;
  border-radius: 6px;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>