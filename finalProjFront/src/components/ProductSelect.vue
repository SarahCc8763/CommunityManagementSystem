<template>
    <span>
        <span v-if="total<=0">查無資料</span>
        <span v-else>共 {{ total }} 項商品</span>
        ，每頁顯示
        <select :value="modelValue" @change="doChange($event)">
            <option v-for="option in options" :key="option" :value="option">{{ option }}</option>
        </select>

    </span>
</template>
    
<script setup>
    const props = defineProps(['total', 'modelValue', 'options'])
    const emit = defineEmits(['selectChange','update:modelValue'])

    const doChange = (event) => {
        console.log('ETV'+event.target.value)
        emit('update:modelValue', event.target.value)
        // 先傳值再callFind
        emit('selectChange')
    }
</script>
    
<style>
    
</style>