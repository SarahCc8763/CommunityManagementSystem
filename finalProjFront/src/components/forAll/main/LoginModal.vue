<template>
  <div class="modal-overlay" v-if="isVisible" @click="closeModal">
    <div class="modal-container" @click.stop>
      <!-- 關閉按鈕 -->
      <button class="close-button" @click="closeModal">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>

      <!-- 登入表單 -->
      <div class="login-form">
        <div class="form-header">
          <div class="logo-container">
            <div class="logo-circle">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
          <h2 class="title">歡迎回來</h2>
          <p class="subtitle">請登入您的帳戶</p>
        </div>

        <form @submit.prevent="handleLogin" class="form">
          <!-- 帳號輸入框 -->
          <div class="input-group">
            <div class="input-wrapper">
              <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              <input 
                type="text" 
                v-model="form.username" 
                placeholder="請輸入帳號"
                class="form-input"
                :class="{ 'error': errors.username }"
                @focus="clearError('username')"
              />
            </div>
            <span class="error-message" v-if="errors.username">{{ errors.username }}</span>
          </div>

          <!-- 密碼輸入框 -->
          <div class="input-group">
            <div class="input-wrapper">
              <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="12" cy="16" r="1" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              <input 
                :type="showPassword ? 'text' : 'password'" 
                v-model="form.password" 
                placeholder="請輸入密碼"
                class="form-input"
                :class="{ 'error': errors.password }"
                @focus="clearError('password')"
              />
              <button 
                type="button" 
                class="password-toggle"
                @click="showPassword = !showPassword"
              >
                <svg v-if="showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M1 12S5 4 12 4S23 12 23 12S19 20 12 20S1 12 1 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20C7 20 2 16 2 12C2 7 7 2 12 2C16 2 20 7 20 12C20 16 17.94 17.94 17.94 17.94Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4C7 4 2 8 2 12C2 16 7 20 12 20C16 20 20 16 20 12C20 9.9 19.76 9.9 19.76 9.9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <line x1="1" y1="1" x2="23" y2="23" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
            </div>
            <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
          </div>

          <!-- 驗證碼 -->
          <!-- <div class="input-group">
            <div class="captcha-container">
              <div class="input-wrapper captcha-input">
                <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M9 12L11 14L15 10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <path d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <input 
                  type="text" 
                  v-model="form.captcha" 
                  placeholder="驗證碼"
                  class="form-input"
                  :class="{ 'error': errors.captcha }"
                  @focus="clearError('captcha')"
                  maxlength="4"
                />
              </div>
              <div class="captcha-image" @click="refreshCaptcha">
                <canvas ref="captchaCanvas" width="120" height="40"></canvas>
                <div class="refresh-icon">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M23 4V10H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M1 20V14H7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10M23 14L18.36 18.36A9 9 0 0 1 3.51 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </div>
            </div>
            <span class="error-message" v-if="errors.captcha">{{ errors.captcha }}</span>
          </div> -->

          <!-- 記住我選項 -->
          <div class="form-options">
            <label class="checkbox-container">
              <input type="checkbox" v-model="form.rememberMe" class="checkbox-input">
              <span class="checkmark"></span>
              <span class="checkbox-label">記住我</span>
            </label>
            <a @click="goResetPassword" class="forgot-password">忘記密碼？</a>
          </div>

          <!-- 登入按鈕 -->
          <button type="submit" class="login-button" :disabled="isLoading">
            <span v-if="!isLoading">登入</span>
            <div v-else class="loading-spinner"></div>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import Swal from 'sweetalert2'
import { useUserStore } from '@/stores/UserStore'
import { useRouter } from 'vue-router'
import resetPassword from '@/components/profile/resetPassword.vue';
const userStore = useUserStore();
const router = useRouter()
const props = defineProps({
  isVisible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'login-success'])

// 忘記密碼
const goResetPassword = () => {
  router.push({ name: 'resetPassword' })
  closeModal()
}

// 表單數據
const form = ref({
  username: '',
  password: '',
  captcha: '',
  rememberMe: false
})

// 錯誤訊息
const errors = ref({})

// 狀態控制
const isLoading = ref(false)
const showPassword = ref(false)
const captchaCanvas = ref(null)
const captchaText = ref('')

// 關閉模態框
const closeModal = () => {
  emit('close')
  // resetForm()
}

// 重置表單
const resetForm = () => {
  form.value = {
    username: '',
    password: '',
    captcha: '',
    // rememberMe: false
  }
  errors.value = {}
}

// 清除錯誤
const clearError = (field) => {
  if (errors.value[field]) {
    delete errors.value[field]
  }
}

// 生成驗證碼
const generateCaptcha = () => {
  const canvas = captchaCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
  let text = ''
  
  // 清空畫布
  ctx.fillStyle = '#f8f9fa'
  ctx.fillRect(0, 0, 120, 40)
  
  // 生成文字
  for (let i = 0; i < 4; i++) {
    text += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  captchaText.value = text
  
  // 繪製文字
  ctx.font = 'bold 20px Arial'
  ctx.fillStyle = '#2d3748'
  ctx.textAlign = 'center'
  
  for (let i = 0; i < text.length; i++) {
    ctx.save()
    ctx.translate(30 + i * 20, 20)
    ctx.rotate((Math.random() - 0.5) * 0.4)
    ctx.fillText(text[i], 0, 0)
    ctx.restore()
  }
  
  // 添加干擾線
  for (let i = 0; i < 3; i++) {
    ctx.strokeStyle = `rgba(102, 126, 234, ${Math.random() * 0.3})`
    ctx.lineWidth = 1
    ctx.beginPath()
    ctx.moveTo(Math.random() * 120, Math.random() * 40)
    ctx.lineTo(Math.random() * 120, Math.random() * 40)
    ctx.stroke()
  }
}

// 刷新驗證碼
const refreshCaptcha = () => {
  generateCaptcha()
  form.value.captcha = ''
}

// 處理登入


const handleLogin = async () => {
  // 驗證表單
  // errors.value = {}

  if (!form.value.username.trim()) {
    errors.value.username = '請輸入帳號'
  }

  if (!form.value.password.trim()) {
    errors.value.password = '請輸入密碼'
  }
  
  if (form.value.password.trim() === 'P@ssw0rd') {
  router.push({name:'resetPassword'})
  closeModal()
  Swal.fire({
                    text: '首次登入請變更密碼',
                    icon: "warning",
                });
  return
  }

  // if (!form.value.captcha.trim()) {
  //   errors.value.captcha = '請輸入驗證碼'
  // } else if (form.value.captcha.toUpperCase() !== captchaText.value) {
  //   errors.value.captcha = '驗證碼錯誤'
  //   refreshCaptcha()
  //   return
  // }

  // if (Object.keys(errors.value).length > 0) {
  //   return
  // }

  isLoading.value = true

  try {
    const response = await axios.post('http://localhost:8080/users/login', {
      "email": form.value.username,
      "password": form.value.password
    })
  
    // 後端成功回傳
    if (response.data.success) {
      // remember me
      if (form.value.rememberMe) {
        localStorage.setItem('rememberedUsername', form.value.username)
      } else {
        localStorage.removeItem('rememberedUsername')
      }  
      
      userStore.login(response.data)
      console.log(response.data);
      // emit('login-success', {
      //   username: form.value.username,
      //   rememberMe: form.value.rememberMe
      // })

      closeModal()
      router.push({ name: 'home' })
      await Swal.fire({
                    text: response.data.message,
                    icon: "success",
                });
    } else {
      // 後端回傳失敗訊息
      Swal.fire({
                    text: response.data.message,
                    icon: "warning",
                });
      errors.value.general = response.data.message || '登入失敗'
    }
  } catch (error) {
    closeModal()
    Swal.fire({
    text: error.response?.data?.message || '伺服器錯誤，請稍後再試',
    icon: "error",
  });
  console.error('登入失敗:', error)
  errors.value.general = '伺服器錯誤，請稍後再試' 
  } finally {
    isLoading.value = false
  }
}


onMounted(() => {
  nextTick(() => {
    generateCaptcha()
  })
  // remember me
  const remembered = localStorage.getItem('rememberedUsername')
  if (remembered) {
    form.value.username = remembered
    form.value.rememberMe = true
  }
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
  animation: fadeIn 0.3s ease;
  padding: 20px;
}

.modal-container {
  background: white;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  max-width: 380px;
  position: relative;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
  max-height: calc(100vh - 40px);
  overflow-y: auto;
}

.close-button {
  position: absolute;
  top: 12px;
  right: 12px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.close-button:hover {
  background: rgba(0, 0, 0, 0.1);
  color: #333;
}

.form-header {
  text-align: center;
  margin-bottom: 24px;
}

.logo-container {
  margin-bottom: 12px;
}

.logo-circle {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  color: white;
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: #2d3748;
  margin: 12px 0 6px 0;
}

.subtitle {
  color: #718096;
  font-size: 14px;
  margin: 0;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  color: #a0aec0;
  z-index: 1;
}

.form-input {
  width: 100%;
  padding: 14px 14px 14px 44px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 14px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: #f8f9fa;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.form-input.error {
  border-color: #f56565;
  box-shadow: 0 0 0 4px rgba(245, 101, 101, 0.1);
}

.form-input::placeholder {
  color: #a0aec0;
}

.password-toggle {
  position: absolute;
  right: 16px;
  background: none;
  border: none;
  color: #a0aec0;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: color 0.3s ease;
}

.password-toggle:hover {
  color: #667eea;
}

.error-message {
  font-size: 12px;
  color: #f56565;
  margin-left: 4px;
}

.captcha-container {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  position: relative;
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e2e8f0;
  transition: border-color 0.3s ease;
  flex-shrink: 0;
}

.captcha-image:hover {
  border-color: #667eea;
}

.captcha-image canvas {
  display: block;
}

.refresh-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  color: #667eea;
}

.captcha-image:hover .refresh-icon {
  opacity: 1;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 4px;
}

.checkbox-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.checkbox-input {
  display: none;
}

.checkmark {
  width: 16px;
  height: 16px;
  border: 2px solid #e2e8f0;
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-input:checked + .checkmark {
  background: #667eea;
  border-color: #667eea;
}

.checkbox-input:checked + .checkmark::after {
  content: '';
  position: absolute;
  left: 4px;
  top: 1px;
  width: 5px;
  height: 8px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  font-size: 13px;
  color: #4a5568;
}

.forgot-password {
  font-size: 13px;
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  cursor: pointer;
  color: #5a6fd8;
}

.login-button {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 響應式設計 */
@media (max-width: 480px) {
  .modal-container {
    padding: 8px;
    margin: 0;
    max-width: 100vw;
    width: 100vw;
    border-radius: 10px;
  }
  .form-header {
    margin-bottom: 12px;
  }
  .title {
    font-size: 17px;
  }
  .subtitle {
    font-size: 12px;
  }
  .form {
    gap: 8px;
  }
  .input-group {
    gap: 2px;
  }
  .form-input {
    font-size: 12px;
    padding: 10px 10px 10px 36px;
  }
  .captcha-container {
    flex-direction: column;
    gap: 4px;
  }
  .captcha-image {
    max-width: 100%;
    height: auto;
  }
  .form-options {
    flex-direction: column;
    gap: 4px;
    align-items: flex-start;
  }
  .checkbox-label, .forgot-password {
    font-size: 11px;
  }
  .login-button {
    font-size: 13px;
    padding: 10px;
    margin-top: 4px;
  }
}
</style> 