<!-- src/components/base/ModalWrapper.vue -->
<template>
    <div class="modal-backdrop" v-if="visible">
        <div class="modal-content-wrapper">
            <div class="modal-header d-flex justify-content-between align-items-center">
                <h5 class="modal-title text-dark">{{ title }}</h5>
                <button type="button" class="btn-close" @click="$emit('update:visible', false)"></button>
            </div>

            <!-- 加入 max-height 與 overflow-auto -->
            <div class="modal-body overflow-auto" style="max-height: 70vh;">
                <slot />
            </div>
        </div>
    </div>

</template>

<script setup>
defineProps({
    visible: Boolean,
    title: String
})

defineEmits(['update:visible'])
</script>

<style scoped>
.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 10%;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1050;
}

.modal-content-wrapper {
    background: white;
    padding: 20px;
    width: 80%;
    max-width: 800px;
    border-radius: 12px;
    height: 90%;
    top: 0;

}
</style>
