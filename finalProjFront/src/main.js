//Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'
import axios from 'axios'

// 引入自定義全局樣式
import './assets/custom-bootstrap.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'




createApp(App)
    .use(router)
    .use(createPinia())
    .mount('#app')
