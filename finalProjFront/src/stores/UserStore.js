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
        communityId.value = data
    }


    const communityId = ref(1)
    function setCommunity(data) {
        communityId.value = data
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
        userId,
        communityId
    }
})
