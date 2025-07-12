// src/stores/user.js
import { defineStore } from 'pinia'
import { ref, reactive, computed } from 'vue'
import Swal from 'sweetalert2'

export const useUserStore = defineStore('user', () => {
    const isAuthenticated = ref(false)
    const userId = ref('')
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
    const assignerId = ref('')
    const actionBy = ref('')
    const unit = ref('')                     //0704 javert新增增加unit門牌號
    const floor = ref('')                    //0704 javert新增增加unit門牌號
    const rawData = reactive({}) // 保存完整後端回傳


    async function login(payload) {
        isAuthenticated.value = true
        // 整包儲存
        Object.assign(rawData, payload)
        // 拆出需要立即顯示的欄位
        userId.value = payload.id
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
        assignerId.value = payload.id
        actionBy.value = payload.id
        unit.value = payload.unit || ''         //0704 javert新增增加unit門牌號
        floor.value = payload.floor || ''         //0704 javert新增增加unit門牌號
        console.log('登入 payload', payload)

        //0704 javert新增查詢帳戶資訊
        if (payload.unitId) {
            try {
                const res = await axios.get(`/api/pointAccount/unit/${payload.unitId}`)
                const facilitiesStore = useFacilitiesStore()
                facilitiesStore.setAccountInfo({
                    accountId: res.data.accountId,
                    unitId: payload.unitId,
                    unit: payload.unit, 
                    floor: payload.floor,
                    username: payload.name,
                    userEmail: payload.email,
                    totalBalance: res.data.totalBalance,                    
                    expiredAt: res.data.expiredAt
                })
                points.value = res.data.totalBalance //把點數改成account裡的真實餘額
            } catch (e) {
                console.error('查詢帳戶失敗', e)
            }
        }
        Object.assign(rawData, payload)
    }

    function logout() {
        isAuthenticated.value = false
        userId.value = ''
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
        assignerId.value = ''
        actionBy.value=''
        unit.value = ''  //0704 javert新增增加unit門牌號
        floor.value = ''  //0704 javert新增增加unit門牌號
        Object.keys(rawData).forEach(k => delete rawData[k])
        useFacilitiesStore().clearAccountInfo()  // 同時清空帳戶資訊 0704 javert新增
        localStorage.removeItem('user')

    }

    // const displayName = computed(() => name.value || username.value)

    return {
        isAuthenticated,
        userId,
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
        assignerId,
        actionBy,
        unit,                //0704 javert新增增加unit門牌號
        floor,               //0704 javert新增增加unit門牌號
        // displayName,
    }
}, {
    persist: true // 啟用 Pinia persistedstate
})
