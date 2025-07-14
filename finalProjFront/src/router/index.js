
import { useUserStore } from '@/stores/UserStore'

import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import BeforeLogIn from '../views/BeforeLogIn.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'
import InvoiceResponseAdd from '../components/finance/finUser/InvoiceResponseAdd.vue'
import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import TicketDetailView from '../views/TicketDetailView.vue'
import TicketForm from '../views/TicketForm.vue'
import TicketList from '../views/TicketList.vue'
import TicketPage from '../views/TicketPage.vue'
import AllTicketsByAssignment from '../views/AllTicketsByAssignment.vue'
import CommunityList from '../views/CommunityList.vue'
import resetPassword from '@/components/profile/resetPassword.vue';
import AdminDashboard from '@/views/AdminDashboard.vue'
import packages from '@/components/package/packages.vue'
import TicketDashboard from '../views/TicketDashboard.vue'
import Vendor from '../views/Vendor.vue'


import finUser from '@/components/finance/finUser/finUser.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import Invoice from '../components/finance/finUser/Invoice.vue'
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


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/BeforeLogIn',                // 你要把 BeforeLogin 當首頁的話
      name: 'BeforeLogIn',
      component: BeforeLogIn
    },
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
      path: '/finance/invoice-response/add',
      name: 'InvoiceResponseAdd',
      component: InvoiceResponseAdd,

    },
    {
      path: '/finance/receipt/add',
      name: 'ReceiptAdd',
      component: ReceiptAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/history',
      name: 'InvoiceHistory',
      component: InvoiceHistory,
    },
    {
      path: '/ticket/:id',
      name: 'TicketDetail',
      component: TicketDetailView,
    },
    {
      path: '/TicketForm',
      name: 'TicketForm',
      component: TicketForm,
    },
    {
      path: '/TicketList',
      name: 'TicketList',
      component: TicketList,
    },
    {
      path: '/TicketPage',
      name: 'TicketPage',
      component: TicketPage,
    }, {
      path: '/AllTicketsByAssignment',
      name: 'AllTicketsByAssignment',
      component: AllTicketsByAssignment,
    },
    {
      path: '/CommunityList',
      name: 'CommunityList',
      component: CommunityList,
      meta: { dark: true },
    },
    {
      path: '/resetPassword',
      name: 'resetPassword',
      component: resetPassword,
    },
    {
      path: '/AdminDashboard',
      name: 'AdminDashboard',
      component: AdminDashboard,
    },
    {
      path: '/packages',
      name: 'packages',
      component: packages,
      meta: { requiresAuth: true }
    },
    {
      path: '/TicketDashboard',
      name: 'TicketDashboard',
      component: TicketDashboard,
    },
    {
      path: '/Vendor',
      name: 'Vendor',
      component: Vendor,
    },{
      
    path: '/finance/invoice',
      name: 'Invoice',
      component: Invoice
    },
    {
      path: '/finUser',
      name: 'FinUser',
      component: finUser
    },
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
    }
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
