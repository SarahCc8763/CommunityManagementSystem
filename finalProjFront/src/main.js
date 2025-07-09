//Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'
import axios from 'axios'
import Swal from 'sweetalert2'


// 引入自定義全局樣式
import './assets/custom-bootstrap.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const newSwal = Swal.mixin({
    theme: 'dark',
})

window.Swal = newSwal
createApp(App)
    .use(router)
    .use(createPinia())
    .mount('#app')
