import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
// 📌 首頁
import Home from '../views/Home.vue'
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
import Dashboard from '@/components/finance/finAdmin/Dashboard.vue'
// Ticket相關
import TicketDetailView from '../views/TicketDetailView.vue'
import TicketForm from '../views/TicketForm.vue'
import TicketList from '../views/TicketList.vue'
import TicketPage from '../views/TicketPage.vue'
import AllTicketsByAssignment from '../views/AllTicketsByAssignment.vue'
import CommunityList from '../views/CommunityList.vue'
import AdminDashboard from '@/views/AdminDashboard.vue'
import TicketDashboard from '../views/TicketDashboard.vue'
import Vendor from '../views/Vendor.vue'
// Parking相關
import LotteryApply from "@/components/parking/LotteryApply.vue"
import LotteryEvent from "@/components/parking/LotteryEvent.vue"
import ParkingBack from "@/components/parking/ParkingBack.vue"
import ParkingFront from "@/components/parking/ParkingFront.vue"
import ParkInitialize from "@/components/parking/ParkInitialize.vue"
import ParkRentalBack from "@/components/parking/ParkRentalBack.vue"
import ParkRentalFront from "@/components/parking/ParkRentalFront.vue"
import ParkSlot from "@/components/parking/ParkSlot.vue"
import MySlots from '@/components/parking/MySlots.vue'
//Julie end
// Yu Start
import AnnouncementLatest from '@/views/bulletin/AnnouncementLatest.vue'
import FaqAdmin from '../views/faq/FaqAdmin.vue'
import FeedbackAdmin from '../views/feedback/FeedbackAdmin.vue'
import Faq from '@/views/faq/Faq.vue'
import MyFeedback from '@/views/feedback/MyFeedback.vue'
import BulletinAdmin from '@/views/bulletin/BulletinAdmin.vue'
//Yu End
// 包裹
import packages from '@/components/package/packages.vue'
import profile from '@/components/profile/profile.vue'
import notification from '@/components/notification/notification.vue'
import resetPassword from '@/components/profile/resetPassword.vue'
import packages_picked from '@/components/package/packages_picked.vue';
import packages_security from '@/components/package/packages_security.vue';
import addPackage from '@/components/package/addPackage.vue';
// 📌 公設預約頁面
import FacilityReservationView from '../views/facilities/FacilityHomepageView.vue'
import FacilityFindAllListView from '../views/facilities/FacilityFindAllListView.vue'
import ReservationFormView from '../views/facilities/ReservationFormView.vue'
import ReservationHistoryView from '../views/facilities/ReservationHistoryView.vue'
import PointHistoryView from '../views/facilities/PointHistoryView.vue'
import PointTransferView from '../views/facilities/PointTransferView.vue'
import PointTopupView from '../views/facilities/PointTopupView.vue'
import PointTopupResultView from '../views/facilities/PointTopupResultView.vue'
// 新增 BeforeLogIn 頁面路由
import BeforeLogIn from '@/views/BeforeLogIn.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/beforeLogIn',
      name: 'beforeLogIn',
      component: BeforeLogIn,
    },
    {
      path: '/',
      name: 'home',
      component: Home,

    },
    // Finance相關
    //// Fin-Admin
    {
      path: '/finance/billing-period/add',
      name: 'BillingPeriodAdd',
      component: BillingPeriodAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/admin-dashboard',
      name: 'FinAdminDashboard',
      component: Dashboard,
      meta: { dark: true },
    },
    {
      path: '/finance/fee-type/add',
      name: 'FeeTypeAdd',
      component: FeeTypeAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/add',
      name: 'InvoiceAdd',
      component: InvoiceAdd,
      meta: { dark: true },
    },
    {
      path: '/finance/invoice/validate',
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
      path: '/finance/receipt/add',
      name: 'ReceiptAdd',
      component: ReceiptAdd,
      meta: { dark: true },
    },

    ////fin-user
    {
      path: '/finUser',
      name: 'FinUser',
      component: finUser,
    },
    {

      path: '/finance/invoice',
      name: 'Invoice',
      component: Invoice,
    },
    {
      path: '/finance/receipt/my',
      name: 'Receipt',
      component: Receipt,
    },
    //Finance相關 end





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
    // Finance相關結束
    // package相關
    {
      path: '/profile',
      name: 'profile',
      component: profile,
      meta: { requiresAuth: true }
    },
    {
      path: '/notification',
      name: 'notification',
      component: notification
    },
    {
      path: '/resetPassword',
      name: 'resetPassword',
      component: resetPassword,
    },
    {
      path: '/packages',
      name: 'packages',
      component: packages,
      meta: { requiresAuth: true }
    },
    {
      path: '/packages_picked',
      name: 'packages_picked',
      component: packages_picked,
      meta: { requiresAuth: true }
    },
    {
      path: '/packages_security',
      name: 'packages_security',
      component: packages_security,
      meta: { requiresAuth: true, dark: true }
    },
    {
      path: '/addPackage',
      name: 'addPackage',
      component: addPackage,
      meta: { requiresAuth: true, dark: true }
    },
    {
      path: '/resetPassword',
      name: 'resetPassword',
      component: resetPassword
    },
    // package相關結束
    // ticket相關
    {
      path: '/adminDashboard',
      name: 'AdminDashboard',
      component: AdminDashboard,
      meta: { dark: true },

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
      meta: { dark: true },
    },
    {
      path: '/CommunityList',
      name: 'CommunityList',
      component: CommunityList
    },
    // ticket相關結束
    // Parking相關
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
      path: '/announcement-latest',
      name: 'announcement-latest',
      component: AnnouncementLatest

    },
    // Parking相關結束
    // faq相關
    {
      path: '/faqAdmin',
      name: 'faqAdmin',
      component: FaqAdmin,
      meta: { dark: true }
    },
    {
      path: '/feedbackAdmin',
      name: 'feedbackAdmin',
      component: FeedbackAdmin,
      meta: { dark: true }
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
      path: '/bulletin-admin',
      name: 'bulletin-admin',
      component: BulletinAdmin,
      meta: { dark: true }

    },
    // faq相關結束
    // facility相關
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
    // facility相關結束    
  ],

  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  }
})



export default router
