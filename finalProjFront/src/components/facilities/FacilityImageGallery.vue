<template>
  <div class="gallery-wrapper">
    <h5 class="fw-bold mb-3">設施照片預覽</h5>

    <div v-if="!images || images.length === 0" class="text-muted">尚無照片</div>

    <div v-else class="image-grid">
      <div v-for="img in images" :key="img.imageId" class="image-card">
        <img :src="path + img.imageUrl" :alt="img.imageDescription || 'facility image'" @click="openModal(img)" />
      </div>
    </div>

    <!-- Modal 放大圖 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <img :src="path + selectedImage.imageUrl" alt="Zoom Image" />
        <button class="close-btn" @click="closeModal">×</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  images: Array
})

const path = import.meta.env.VITE_API_URL

import { ref } from 'vue'
const showModal = ref(false)
const selectedImage = ref({})

const openModal = (img) => {
  selectedImage.value = img
  showModal.value = true
}
const closeModal = () => {
  showModal.value = false
}
</script>

<style scoped>
.gallery-wrapper {
  padding: 12px;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.image-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.image-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
  border: 1px solid #ddd;
  transition: transform 0.3s ease;
  z-index: 1;
  position: relative;
}

.image-card img:hover {
  transform: scale(1.03);
  cursor: pointer;
}

/* Modal 樣式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  position: relative;
  max-width: 90%;
  max-height: 90%;
}

/* .modal-content img {
  width: 100%;   
  height: auto;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
} */

.modal-content img {
  max-width: 100%;
  max-height: 80vh;
  width: auto;
  height: auto;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
  display: block;
  margin: 0 auto;
}

.close-btn {
  position: absolute;
  top: -16px;
  right: -16px;
  background: #fff;
  color: #000;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}
</style>
