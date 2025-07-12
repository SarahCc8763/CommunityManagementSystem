// src/stores/facilities.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from '@/plugins/axios'


export const useFacilitiesStore = defineStore('facilities', () => {
    const accountId = ref('')
    const unitId = ref('')
    const username = ref('')
    const userEmail = ref('')
    const totalBalance = ref(0)
    const expiredAt = ref('')
    const unit = ref('') //0704 javert新增增加unit門牌號
    const floor = ref('') //0704 javert新增增加unit門牌號

    function setAccountInfo(payload) {
        accountId.value = payload.accountId || ''
        unitId.value = payload.unitId || ''
        username.value = payload.name || ''
        userEmail.value = payload.userEmail || '' 
        totalBalance.value = payload.totalBalance || 0        
        expiredAt.value = payload.expiredAt || ''
        unit.value = payload.unit || '' //0704 javert新增增加unit門牌號
        floor.value = payload.floor || '' //0704 javert新增增加unit門牌號
    }

    function clearAccountInfo() {
        accountId.value = ''
        unitId.value = ''
        username.value = ''
        totalBalance.value = 0
        expiredAt.value = ''
        unit.value = '' //0704 javert新增增加unit門牌號
        floor.value = ''
    }

    async function refreshAccountInfo() {
        if (!unitId.value) return
        try {
        const res = await axios.get(`/api/pointAccount/unit/${unitId.value}`)
        setAccountInfo({
            accountId: res.data.accountId,
            unitId: unitId.value,
            unit: unit.value,
            floor: floor.value,
            username: username.value, // 沿用原本
            totalBalance: res.data.totalBalance,
            expiredAt: res.data.expiredAt                        
        })
        console.log('點數帳戶已更新')
        } catch (e) {
        console.error('更新點數帳戶失敗', e)
        }
    }

    return {
        accountId,
        unitId,
        username,
        totalBalance,
        expiredAt,
        unit,
        floor,
        setAccountInfo,
        clearAccountInfo,
        refreshAccountInfo
    }
}, {
    persist: true // 若你希望頁面切換仍保留帳戶資料
})
