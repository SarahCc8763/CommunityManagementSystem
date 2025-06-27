import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import NotFound from "@/views/NotFound.vue";
import Forbidden from "@/views/Forbidden.vue";
import Login from "@/views/secure/Login.vue";
import Product from "@/views/pages/Product.vue"
import Park from "@/views/pages/Park.vue"
import ParkInitialize from "@/views/pages/ParkInitialize.vue"
import ParkSlot from "@/views/pages/ParkSlot.vue";
import ParkRentalFront from "@/views/pages/ParkRentalFront.vue"
import ParkRentalBack from "@/views/pages/ParkRentalBack.vue"

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
    },
    {
        path: "/:pathMatch(.*)*",
        name: "404",
        component: NotFound,
    },
    {
        path: "/403",
        name: "403",
        component: Forbidden,
    },
    {
        path: "/secure/login",
        name: "login",
        component: Login,
    },
    {
        path: "/pages/product",
        name: "product",
        component: Product,
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
]


export default createRouter({
    history: createWebHistory(),
    routes,
})