// src/stores/user.js
import { defineStore } from 'pinia'
import { ref, reactive, computed } from 'vue'
import Swal from 'sweetalert2'

export const useUserStore = defineStore('user', () => {
    const isAuthenticated = ref(false)
    const name = ref('')
    const username = ref('')
    const avatarUrl = ref('')
    const points = ref('')
    const token = ref('')
    const unitId = ref('')
    const contactInfo = ref('')
    const roleId = ref('')
    const emergencyContactName = ref('')
    const state = ref('')
    const emergencyContactRelation = ref('')
    const emergencyContactPhone = ref('')
    const communityId = ref('')
    const rawData = reactive({}) // 保存完整後端回傳


    function login(payload) {
        isAuthenticated.value = true
        // 整包儲存
        Object.assign(rawData, payload)
        // 拆出需要立即顯示的欄位
        name.value = payload.name || ''
        username.value = payload.email || ''
        avatarUrl.value = payload.photo || ''
        token.value = payload.token || ''
        points.value = payload.points || ''
        unitId.value = payload.unitId
        roleId.value = payload.roleId
        contactInfo.value = payload.contactInfo
        emergencyContactName.value = payload.emergencyContactName
        state.value = payload.state
        emergencyContactRelation.value = payload.emergencyContactRelation
        emergencyContactPhone.value = payload.emergencyContactPhone
        communityId.value = payload.communityId

        Object.assign(rawData, payload)
    }

    function logout() {
        isAuthenticated.value = false
        name.value = ''
        username.value = ''
        avatarUrl.value = ''
        token.value = ''
        points.value = ''
        unitId.value = ''
        contactInfo.value = ''
        roleId.value = ''
        emergencyContactName.value = ''
        state.value = ''
        emergencyContactRelation.value = ''
        emergencyContactPhone.value = ''
        communityId.value = ''
        Object.keys(rawData).forEach(k => delete rawData[k])
        localStorage.removeItem('user')

    }

    // const displayName = computed(() => name.value || username.value)

    return {
        isAuthenticated,
        name,
        username,
        avatarUrl,
        token,
        rawData,     // 要用完整欄位可從 rawData 拿
        login,
        logout,
        points,
        unitId,
        contactInfo,
        roleId,
        emergencyContactName,
        state,
        emergencyContactRelation,
        emergencyContactPhone,
        communityId,
        // displayName,
    }
}, {
    persist: true // 啟用 Pinia persistedstate
})
