import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import TicketList from '@/views/TicketList.vue'
import TicketForm from '@/views/TicketForm.vue'
import TicketDetailView from '@/views/TicketDetailView.vue'
import Faq from '@/components/faq/Faq.vue'



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
