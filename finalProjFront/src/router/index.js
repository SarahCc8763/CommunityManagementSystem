import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import FeeTypeAdd from '../components/finance/finAdmin/FeeTypeAdd.vue'
import BillingPeriodAdd from '../components/finance/finAdmin/BillingPeriodAdd.vue'
import InvoiceAdd from '../components/finance/finAdmin/InvoiceAdd.vue'
import InvoiceResponseAdd from '../components/finance/finUser/InvoiceResponseAdd.vue'
import ReceiptAdd from '../components/finance/finAdmin/ReceiptAdd.vue'
import InvoiceHistory from '../components/finance/finUser/InvoiceHistory.vue'
import Park from "@/views/pages/Park.vue"
import ParkInitialize from "@/views/pages/ParkInitialize.vue"
import ParkSlot from "@/views/pages/ParkSlot.vue";
import ParkRentalFront from "@/views/pages/ParkRentalFront.vue"
import ParkRentalBack from "@/views/pages/ParkRentalBack.vue"
import LotteryEvent from '@/views/pages/LotteryEvent.vue'
import LotteryApply from '@/views/pages/LotteryApply.vue'

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
  ],
})

export default router
