import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'
import finUser from '@/components/finance/finUser/finUser.vue'
import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import Invoice from '../components/finance/finUser/Invoice.vue'
import InvoiceValidate from '../components/finance/finAdmin/InvoiceValidate.vue'
import InvoiceWithResponse from '@/components/finance/finAdmin/InvoiceWithResponses.vue'
import ParkRentalBack from '../components/parking/ParkRentalBack.vue'

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
      path: '/parking/rental/back',
      name: 'ParkRentalBack',
      component: ParkRentalBack,
      meta: { dark: true },
    },
  ],
})

export default router
