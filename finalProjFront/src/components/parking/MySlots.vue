<template>
  <div class="container mt-4">
    <h2 class="fw-bold text-primary mb-4">我的車位與登記車牌</h2>
    <div class="row g-4">
      <div
        class="col-md-4"
        v-for="slot in userSlotsFiltered"
        :key="slot.slotNumber"
      >
        <div
          class="card h-100 shadow-sm border-0"
          @click="openModal(slot)"
          style="cursor: pointer;"
        >
          <div
            class="card-header d-flex justify-content-between align-items-center bg-light"
          >
            <span class="fw-bold">{{ slot.slotNumber }}</span>
            <span
              class="badge"
              :class="slot.isRented ? 'bg-warning text-dark' : 'bg-success'"
            >
              <i
                class="bi"
                :class="slot.isRented ? 'bi-person-lines-fill' : 'bi-house-door-fill'"
                style="margin-right: 4px;"
              ></i>
              {{ slot.isRented ? '承租' : '擁有' }}
            </span>
          </div>
          <div class="card-body">
            <div class="mb-2">
              <span class="badge bg-primary me-2 fs-6">{{ slot.parkingType }}</span>
              <span class="badge bg-info text-dark fs-6">{{ slot.location }}</span>
            </div>
            <p class="mb-1">
              <strong>登記車牌：</strong>{{ slot.licensePlate || '未登記' }}
            </p>
            <template v-if="slot.isRented">
              <p class="mb-0" v-if="slot.rentBuyStart">
                <strong>承租期間：</strong>{{ formatDate(slot.rentBuyStart) }} ~
                {{ formatDate(slot.rentEnd) }}
              </p>
              <p class="mb-0">
                <strong>審核狀態：</strong>{{ getApprovalText(slot.approved) }}
              </p>
              <p class="mb-0">
                <strong>繳費狀態：</strong>{{ getStatusText(slot.status) }}
              </p>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 車牌登記 Modal -->
    <div class="modal fade" id="plateModal" tabindex="-1" ref="plateModalRef">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">我的車位</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <label class="form-label">車牌號碼：</label>
            <input
              type="text"
              class="form-control"
              v-model="selectedSlot.licensePlate"
              placeholder="請輸入新車牌"
              :readonly="!plateEditMode"
            />
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            <button v-if="!plateEditMode" class="btn btn-outline-primary" @click="plateEditMode = true">編輯</button>
            <button v-else class="btn btn-primary">儲存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 承租資料 Modal -->
    <div class="modal fade" id="rentalModal" tabindex="-1" ref="rentalModalRef">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">承租資料</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label">車牌號碼</label>
              <input
                type="text"
                class="form-control"
                v-model="selectedSlot.licensePlate"
                placeholder="ABC-123"
                :readonly="!rentalEditMode"
              />
            </div>
            <div class="mb-3">
              <label class="form-label">承租起始日</label>
              <input type="date" class="form-control" v-model="rentalStart" :readonly="!rentalEditMode" />
            </div>
            <div class="mb-3">
              <label class="form-label">承租截止日</label>
              <input type="date" class="form-control" v-model="rentalEnd" :readonly="!rentalEditMode" />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            <button v-if="!rentalEditMode" class="btn btn-outline-primary" @click="rentalEditMode = true">編輯</button>
            <button v-else class="btn btn-primary">儲存</button>
            <button v-if="rentalEditMode" class="btn btn-danger">取消承租</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '@/plugins/axios'
import { useUserStore } from '@/stores/userStore'
import { Modal } from 'bootstrap'

const userStore = useUserStore()
const userSlots = ref([])
const selectedSlot = ref({})
const rentalStart = ref('')
const rentalEnd = ref('')
const plateEditMode = ref(false)
const rentalEditMode = ref(false)

const plateModalRef = ref(null)
const rentalModalRef = ref(null)
let plateModalInstance, rentalModalInstance

const usersId = userStore.id || 2

const today = new Date()

const userSlotsFiltered = computed(() => {
  return userSlots.value.filter((slot) => {
    if (!slot.isRented) return true
    if (!slot.rentBuyStart || !slot.rentEnd) return false
    const start = new Date(slot.rentBuyStart)
    const end = new Date(slot.rentEnd)
    return start <= today && today <= end
  })
})

onMounted(async () => {
  try {
    plateModalInstance = new Modal(plateModalRef.value)
    rentalModalInstance = new Modal(rentalModalRef.value)

    const res = await axios.get('/park/parking-rentals/user/slots-and-rentals', {
      params: { usersId }
    })
    userSlots.value = res.data.data
  } catch (err) {
    console.error('取得車位資料失敗', err)
  }
})

const formatDate = (str) => {
  if (!str) return '-'
  return new Date(str).toLocaleDateString('zh-TW')
}

const getApprovalText = (value) => {
  return value === null ? '待審核' : value ? '已審核' : '未通過'
}

const getStatusText = (value) => {
  return value === null ? '未提供' : value ? '已繳費' : '未繳費'
}

const openModal = (slot) => {
  selectedSlot.value = { ...slot }
  rentalStart.value = slot.rentBuyStart?.slice(0, 10) || ''
  rentalEnd.value = slot.rentEnd?.slice(0, 10) || ''
  plateEditMode.value = false
  rentalEditMode.value = false
  slot.isRented ? rentalModalInstance.show() : plateModalInstance.show()
}
</script>

<style scoped>
.card-header {
  font-size: 1.1rem;
  font-weight: bold;
}
.card:hover {
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  transform: scale(1.01);
  transition: 0.2s;
}
.badge {
  font-size: 0.75rem;
  padding: 0.4em 0.6em;
  vertical-align: middle;
}
</style>
