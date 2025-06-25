<template>
  <div>
    <Navigation></Navigation>
  </div>
  <RouterView />
</template>

<script setup>
import { RouterView } from "vue-router";
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import Navigation from "./views/Navigation.vue";
</script>

<style></style>