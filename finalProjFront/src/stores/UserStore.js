// src/stores/user.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
    const isAuthenticated = ref(false)
    const name = ref('')
    const username = ref('')
    const avatarUrl = ref('')

    function login({ name: n, username: u, avatarUrl: a }) {
        isAuthenticated.value = true
        name.value = n
        username.value = u
        avatarUrl.value = a || `https://i.pravatar.cc/100?u=${u}`
    }

    function logout() {
        isAuthenticated.value = false
        name.value = ''
        username.value = ''
        avatarUrl.value = ''
    }

    const userId = ref(3)
    function setUserId(data) {
        userId.value = data
    }

    const token = ref('')
    function setToken(data) {
        token.value = data
    }
    const community = ref(1) // 登入時設定
    function setCommunity(data) {
        community.value = data
    }
    const email = ref('sa') // 登入時設定
    function setEmail(data) {
        email.value = data
    }
    const id = ref(1) // 登入時設定
    function setId(data) {
        id.value = data
    }
    const displayName = computed(() => name.value || username.value)
    return {
        isAuthenticated,
        name,
        username,
        avatarUrl,
        login,
        logout,
        displayName,
        token,
        setToken,
        community,
        setCommunity,
        email,
        setEmail,
        id,
        setId
    }
})
