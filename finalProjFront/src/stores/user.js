import { ref } from 'vue'
import { defineStore } from 'pinia'
export default defineStore('user', () => {
    const email = ref('')
    function setEmail(data) {
        email.value = data
    }
    const token = ref('')
    function setToken(data) {
        token.value = data
    }
    return {
        email,
        setEmail,
        token,
        setToken
    }
}, {
    persist: true,
})