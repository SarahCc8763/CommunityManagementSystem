
import { useUserStore } from '@/stores/UserStore'

import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'


// 財務相關-使用者
import finUser from '@/components/finance/finUser/finUser.vue'
import Invoice from '@/components/finance/finUser/Invoice.vue'
import Receipt from '@/components/finance/finUser/Receipt.vue'
//財務相關-管理員
import Dashboard from '@/components/finance/finAdmin/Dashboard.vue'
import FeeTypeAdd from '@/components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '@/components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '@/components/finance/finAdmin/InvoiceAdd.vue'
import InvoiceValidate from '@/components/finance/finAdmin/InvoiceValidate.vue'
import InvoiceWithResponse from '@/components/finance/finAdmin/InvoiceWithResponse.vue'
import ReceiptAdd from '@/components/finance/finAdmin/ReceiptAdd.vue'


// Ticket相關
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







const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
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
      path: '/finance/receipt/add',
      name: 'ReceiptAdd',
      component: ReceiptAdd,
      meta: { dark: true },
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
      meta: { dark: true },
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
