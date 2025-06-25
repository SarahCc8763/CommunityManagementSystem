<template>
        <div class="card">
                    <img class="card-img-top" :src="path+product.id" :alt="product.name">

                    <div class="card-body">
                        <h5 class="card-title text-start">{{product.name}}</h5>
                        <div class="card-text text-danger text-end">NT${{ product.price }}</div>
                        <div class="row">

                            <div class="col text-start">
                                <button class="btn btn-primary" @click="emit('openUpdate', 'update',product.id)">開啟修改</button>
                            </div>
                            <div class="col text-end">
                                <button class="btn btn-primary" @click="emit('delete',product.id)">刪除</button>
                            </div>                            
                        </div>
                    </div>
                    </div>
</template>
    
<script setup>
    const props = defineProps(['product'])
    const path = import.meta.env.VITE_DETAIL_URL
    const emit = defineEmits(['openUpdate','delete'])
</script>
    
<style>
    
</style>