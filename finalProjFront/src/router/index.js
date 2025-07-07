import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'

import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import Invoice from '../components/finance/finUser/Invoice.vue'
import Faq from '@/views/faq/Faq.vue'
import MyFeedback from '@/views/feedback/MyFeedback.vue'

import BulletinAdmin from '@/views/bulletin/BulletinAdmin.vue'

import finUser from '@/components/finance/finUser/finUser.vue'
import AnnouncementLatest from '@/views/bulletin/AnnouncementLatest.vue'
import FaqAdmin from '../views/faq/FaqAdmin.vue'
import FeedbackAdmin from '../views/feedback/FeedbackAdmin.vue'




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
  ],
})

export default router
