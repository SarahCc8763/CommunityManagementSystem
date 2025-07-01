
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'


//Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import axios from 'axios'


import BootstrapVue3 from 'bootstrap-vue-3'


createApp(App)
    .use(router)
    .use(createPinia())
    .mount('#app')
