import finUser from '@/components/finance/finUser/finUser.vue'
import { createRouter, createWebHistory } from 'vue-router'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'
import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import Invoice from '../components/finance/finUser/Invoice.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import Home from '../views/Home.vue'
import LotteryApply from "@/components/parking/LotteryApply.vue"
import LotteryEvent from "@/components/parking/LotteryEvent.vue"
import Park from "@/components/parking/Park.vue"
import ParkInitialize from "@/components/parking/ParkInitialize.vue"
import ParkRentalBack from "@/components/parking/ParkRentalBack.vue"
import ParkRentalFront from "@/components/parking/ParkRentalFront.vue"
import ParkSlot from "@/components/parking/ParkSlot.vue"
import TemporaryParking from "@/components/parking/TemporaryParking.vue"

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
      path: '/finUser',
      name: 'FinUser',
      component: finUser
    },
    {
      path: "/pages/park",
      name: "park",
      component: Park,
    },
    {
      path: "/pages/park/initialize",
      name: "parkInitialize",
      component: ParkInitialize,
    },
    {
      path: "/pages/park/slot",
      name: "parkSlot",
      component: ParkSlot,
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
    },
    {
      path: "/pages/park/lottery-event",
      name: "lotteryEvent",
      component: LotteryEvent,
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
    },
  ],
})

export default router
