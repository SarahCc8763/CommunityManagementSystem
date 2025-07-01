import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'
import InvoiceResponseAdd from '../components/finance/finUser/InvoiceResponseAdd.vue'
import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import Invoice from '../components/finance/finUser/Invoice.vue'

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
