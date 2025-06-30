import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finUser/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finUser/BillingPeriodAdd.vue'
import InvoiceAdd from '../components/finance/finUser/InvoiceAdd.vue'
import InvoiceResponseAdd from '../components/finance/finUser/InvoiceResponseAdd.vue'
import ReceiptAdd from '../components/finance/finUser/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'

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
    },
    {
      path: '/finance/billing-period/add',
      name: 'BillingPeriodAdd',
      component: BillingPeriodAdd,
    },
    {
      path: '/finance/invoice/add',
      name: 'InvoiceAdd',
      component: InvoiceAdd,
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
    },
    {
      path: '/finance/invoice/history',
      name: 'InvoiceHistory',
      component: InvoiceHistory,
    },
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
})

export default router
