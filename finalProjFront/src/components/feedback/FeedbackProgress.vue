<template>
    <div class="feedback-progress-container mt-4">
        <h6>處理進度：</h6>
        <div class="progress-steps d-flex flex-column align-items-start">
            <div v-for="(step, index) in steps" :key="index" class="progress-step"
                @click="emitShowHistory(step.status)">
                <!-- 線與圓 -->
                <div class="step-connector">
                    <div class="circle" :class="{ reached: isReached(step.status) }"
                        :style="isReached(step.status) ? { backgroundColor: getStepColor(step.status) } : {}">
                        <i v-if="isReached(step.status)" :class="step.icon"></i>
                    </div>
                    <div v-if="index < steps.length - 1" class="vertical-line"></div>
                </div>

                <!-- 文字 -->
                <div class="step-content">
                    <div class="label" :class="{ 'current-status': isCurrentStatus(step.status) }">
                        {{ step.label }}
                    </div>

                    <div class="step-date" v-if="isReached(step.status) && getStepDate(step.status)">
                        {{ formatDate(getStepDate(step.status)) }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue'

const props = defineProps({
    feedback: {
        type: Object,
        default: () => ({})
    }
})

const emit = defineEmits(['show-history-detail'])

const steps = [
    { status: '待處理', label: '待處理', icon: 'bi bi-hourglass' },
    { status: '確認中', label: '確認中', icon: 'bi bi-eye' },
    { status: '處理中', label: '處理中', icon: 'bi bi-arrow-repeat' },
    { status: '已結案', label: '已結案', icon: 'bi bi-check-circle-fill' }
]
const isCurrentStatus = (status) => {
    return steps[currentReachedIndex.value]?.status === status
}

const currentReachedIndex = computed(() => {
    const latest = props.feedback.statusHistories?.[props.feedback.statusHistories.length - 1]?.newStatus || '待處理'
    return steps.findIndex(s => s.status === latest)
})

const isReached = (status) => {
    const index = steps.findIndex(s => s.status === status)
    return index <= currentReachedIndex.value
}

const getStepColor = (status) => {
    switch (status) {
        case '待處理': return '#FFA08F'   // 柔紅（Google Keep style）
        case '確認中': return '#8FE9FF'   // 柔藍
        case '處理中': return '#E8FFBD'   // 柔黃
        case '已結案': return '#C2FF68'   // 
        default: return '#a0a0a0'         // 柔灰（未達成）
    }
}

const getStepDate = (status) => {
    if (status === '待處理') {
        return props.feedback.submittedAt
    }
    const matched = props.feedback.statusHistories?.filter(h => h.newStatus === status)
    return matched?.length > 0 ? matched[matched.length - 1].changedAt : null
}

const formatDate = (datetime) => {
    const date = new Date(datetime)
    return date.toLocaleDateString('zh-TW', { month: '2-digit', day: '2-digit' })
}

const emitShowHistory = (status) => {
    emit('show-history-detail', props.feedback, status)
}
</script>

<style scoped>
.feedback-progress-container {
    padding: 15px;
    background-color: #3b5077;
    border-radius: 8px;
    color: #ffffff;
}

h6 {
    margin-bottom: 15px;
    color: #e0e0e0;
}

.progress-steps {
    position: relative;
    padding: 10px 10px;
}

.progress-step {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    position: relative;
    /* margin-bottom: 25px; */
    cursor: pointer;
}

.step-connector {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 12px;
}

.circle {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: 1.5px solid #b0b0b0;
    background-color: transparent;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2rem;
    color: rgb(0, 0, 0);
    transition: all 0.3s ease;
}

.circle.reached {
    box-shadow: 0 0 0px rgba(255, 255, 255, 0.4);
    border-color: transparent;
}

.vertical-line {
    width: 4px;
    height: 30px;
    background-color: #5a74a1;
    margin: 0;
    flex-shrink: 0;
}

.step-content {
    display: flex;
    flex-direction: column;
}

.label {
    font-size: 0.95em;
    font-weight: 200;
    color: #e3e3e3;
    margin-top: 4px;
}

.step-date {
    font-size: 0.8em;
    color: #b0b0b0;
    margin-top: 4px;
}

.current-status {
    color: #c4ff4e;
    font-weight: bold;
    /* 柔和亮黃色 */
}
</style>
