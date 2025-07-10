
import { useUserStore } from '@/stores/UserStore'

import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
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
// Yu Start
import AnnouncementLatest from '@/views/bulletin/AnnouncementLatest.vue'
import FaqAdmin from '../views/faq/FaqAdmin.vue'
import FeedbackAdmin from '../views/feedback/FeedbackAdmin.vue'
import Faq from '@/views/faq/Faq.vue'
import MyFeedback from '@/views/feedback/MyFeedback.vue'
import BulletinAdmin from '@/views/bulletin/BulletinAdmin.vue'
//Yu End








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
      path: '/finance/invoice',
      name: 'Invoice',
      component: Invoice
    },

    {
      path: '/faq',
      name: 'faq',
      component: Faq

    },

    {
      path: '/feedback',
      name: 'feedback',
      component: MyFeedback

    },

    {
      path: '/announcement-latest',
      name: 'announcement-latest',
      component: AnnouncementLatest

    },
    {
      path: '/bulletin-admin',
      name: 'bulletin-admin',
      component: BulletinAdmin,
      meta: { dark: true }

    },

    {
      path: '/finUser',
      name: 'FinUser',
      component: finUser
    },


    {
      path: '/feebackAdmin',
      name: 'feedbackAdmin',
      component: FeedbackAdmin,
      meta: { dark: true }
    },
    {
      path: '/faqAdmin',
      name: 'faqAdmin',
      component: FaqAdmin,
      meta: { dark: true }
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
    }
    // {
    //   path: '/',
    //   name: 'home',
    //   component: Home.vue,
    // },
    // {
    //   path: '/',
    //   name: 'home',
    //   component: Home.vue,
    // },
    // {
    //   path: '/',
    //   name: 'home',
    //   component: Home.vue,
    // },
    // {
    //   path: '/',
    //   name: 'home',
    //   component: Home.vue,
    // },
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
