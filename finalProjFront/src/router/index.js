import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import TicketList from '@/views/TicketList.vue'
import TicketForm from '@/views/TicketForm.vue'
import TicketDetailView from '@/views/TicketDetailView.vue'
import Faq from '@/views/faq/Faq.vue'
import MyFeedback from '@/views/feedback/MyFeedback.vue'
import AnnouncementLatest from '@/views/bulletin/AnnouncementLatest.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/TicketList',
      name: 'TicketList',
      component: TicketList,
    },
    {
      path: '/TicketForm',
      name: 'TicketForm',
      component: TicketForm,
    },

    {
      path: '/ticket/detail',
      name: 'TicketDetail',
      component: TicketDetailView
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
  ],
})


export default router;
