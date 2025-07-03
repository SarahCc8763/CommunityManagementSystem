import { ref } from 'vue'
import { defineStore } from 'pinia'
export default defineStore('user', () => {

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



    return {
        token,
        setToken,
        community,
        setCommunity,
        email,
        setEmail,
    }
}, {
    persist: true,
})