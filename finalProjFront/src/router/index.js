
import { useUserStore } from '@/stores/UserStore'

import { createRouter, createWebHistory } from 'vue-router'



// 財務相關-使用者
import finUser from '@/components/finance/finUser/finUser.vue'
import Invoice from '@/components/finance/finUser/Invoice.vue'
import Receipt from '@/components/finance/finUser/Receipt.vue'
//財務相關-管理員

import FeeTypeAdd from '@/components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '@/components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '@/components/finance/finAdmin/InvoiceAdd.vue'
import InvoiceValidate from '@/components/finance/finAdmin/InvoiceValidate.vue'
import InvoiceWithResponse from '@/components/finance/finAdmin/InvoiceWithResponse.vue'
import ReceiptAdd from '@/components/finance/finAdmin/ReceiptAdd.vue'


// Ticket相關
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'

// 📌 首頁
import Home from '../views/Home.vue'

import TicketDetailView from '../views/TicketDetailView.vue'
import TicketForm from '../views/TicketForm.vue'
import TicketList from '../views/TicketList.vue'
import TicketPage from '../views/TicketPage.vue'
import AllTicketsByAssignment from '../views/AllTicketsByAssignment.vue'


import CommunityList from '../views/CommunityList.vue'
import AdminDashboard from '@/views/AdminDashboard.vue'
import TicketDashboard from '../views/TicketDashboard.vue'
import Vendor from '../views/Vendor.vue'



import Home from '../views/Home.vue'
import LotteryApply from "@/components/parking/LotteryApply.vue"
import LotteryEvent from "@/components/parking/LotteryEvent.vue"
import ParkingBack from "@/components/parking/ParkingBack.vue"
import ParkingFront from "@/components/parking/ParkingFront.vue"
import ParkInitialize from "@/components/parking/ParkInitialize.vue"
import ParkRentalBack from "@/components/parking/ParkRentalBack.vue"
import ParkRentalFront from "@/components/parking/ParkRentalFront.vue"
import ParkSlot from "@/components/parking/ParkSlot.vue"
import TemporaryParking from "@/components/parking/TemporaryParking.vue"
import MySlots from '@/components/parking/MySlots.vue'

// 📌 使用者功能頁面
import packages from '@/components/package/packages.vue'
import profile from '@/components/profile/profile.vue'
import notification from '@/components/notification/notification.vue'
import resetPassword from '@/components/profile/resetPassword.vue'

// 📌 公設預約頁面
import FacilityReservationView from '../views/facilities/FacilityHomepageView.vue'
import FacilityFindAllListView from '../views/facilities/FacilityFindAllListView.vue'
import ReservationFormView from '../views/facilities/ReservationFormView.vue'
import ReservationHistoryView from '../views/facilities/ReservationHistoryView.vue'
import PointHistoryView from '../views/facilities/PointHistoryView.vue'
import PointTransferView from '../views/facilities/PointTransferView.vue'
import PointTopupView from '../views/facilities/PointTopupView.vue'
import PointTopupResultView from '../views/facilities/PointTopupResultView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },

    // Finance相關
    //// Fin-Admin
    {
      path: '/finance/fee-type/add',
      name: 'FeeTypeAdd',
      component: FeeTypeAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/billing-period/add',
      name: 'BillingPeriodAdd',
      component: BillingPeriodAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/add',
      name: 'InvoiceAdd',
      component: InvoiceAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/receipt/add',
      name: 'ReceiptAdd',
      component: ReceiptAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/validate',
      name: 'InvoiceValidate',
      component: InvoiceValidate,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice-review',
      name: 'InvoiceValidate',
      component: InvoiceValidate,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/InvoiceWithResponse',
      name: 'InvoiceWithResponse',
      component: InvoiceWithResponse,
      meta: { dark: true },
    },
    ////fin-user
    {
      path: '/finance/receipt/my',
      name: 'Receipt',
      component: Receipt,
    },
    {
      path: '/finance/invoice',
      name: 'Invoice',
      component: Invoice,
    },
    {
      path: '/finUser',
      name: 'FinUser',
      component: finUser,
    },
    //Finance相關 end
    {
      path: "/pages/park/initialize",
      name: "parkInitialize",
      component: ParkInitialize,
      meta: { dark: true },
    },
    {
      path: "/pages/park/slot",
      name: "parkSlot",
      component: ParkSlot,
      meta: { dark: true },
    },
    {
      path: "/pages/park/rental-front",
      name: "parkRentalFront",
      component: ParkRentalFront,
    },
    {
      path: "/pages/park/rental-back",
      name: "parkRentalBack",
      component: ParkRentalBack,
      meta: { dark: true },
    },
    {
      path: "/pages/park/lottery-event",
      name: "lotteryEvent",
      component: LotteryEvent,
      meta: { dark: true },
    },
    {
      path: "/pages/park/lottery-apply",
      name: "lotteryApply",
      component: LotteryApply,
    },
    {
      path: "/pages/park/temporary-parking",
      name: "temporaryParking",
      component: TemporaryParking,
      meta: { dark: true },
    },
    {
      path: "/pages/park/parking-front",
      name: "parkingFront",
      component: ParkingFront,
    },
    {
      path: "/pages/park/parking-back",
      name: "parkingBack",
      component: ParkingBack,
      meta: { dark: true },
    },
    {
      path: '/pages/park/my-slots',
      name: 'mySlots',
      component: MySlots,
    },
    {
      path: '/facilities',
      name: 'FacilityHomepageView',
      component: FacilityReservationView
    },
    {
      path: '/facilities/findAll',
      name: 'FacilityFindAllListView',
      component: FacilityFindAllListView
    },
    {
      path: '/reservations/book/:facilityId',
      name: 'ReservationFormView',
      component: ReservationFormView,
      props: true
    },
    {
      path: '/reservations/history',
      name: 'ReservationHistoryView',
      component: ReservationHistoryView,
      props: true
    },
    {
      path: '/points/history',
      name: 'PointHistoryView',
      component: PointHistoryView,
      props: true
    },
    {
      path: '/points/transfer',
      name: 'PointTransferView',
      component: PointTransferView,
      props: true
    },
    {
      path: '/points/topup',
      name: 'PointTopupView',
      component: PointTopupView,
      props: true
    },
    {
      path: '/points/topup/result',
      name: 'PointTopupResultView',
      component: PointTopupResultView,
      props: true
    },
  ],

  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  }
})
router.beforeEach((to) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    console.log('尚未登入，導去首頁並觸發登入 modal')
    window.dispatchEvent(new CustomEvent('show-login-modal'))
    return { name: 'home' }
  }

})


export default router
