<!-- <template>
    <Dialog :visible="visible" @update:visible="closeModal" modal header="處理歷程" :style="{ width: '50vw' }"
        :breakpoints="{ '1199px': '75vw', '575px': '90vw' }" class="custom-primevue-modal">
        <div class="p-fluid">
            <template v-if="historyData && historyData.length > 0">
                <Timeline :value="historyData" align="alternate" class="custom-timeline">
                    <template #marker="slotProps">
                        <span class="custom-marker shadow-2"
                            :style="{ backgroundColor: getStatusColor(slotProps.item.newStatus) }">
                            <i :class="getStatusIcon(slotProps.item.newStatus)"></i>
                        </span>
                    </template>
                    <template #content="slotProps">
                        <Card class="mt-3 custom-card">
                            <template #title>
                                <span :class="getStatusTextColor(slotProps.item.newStatus)">
                                    {{ slotProps.item.oldStatus }} &rarr; {{ slotProps.item.newStatus }}
                                </span>
                            </template>
                            <template #subtitle>
                                {{ formatDateTime(slotProps.item.changedAt) }}
                            </template>
                        </Card>
                    </template>
                </Timeline>
            </template>
            <template v-else>
                <p class="text-center">尚無處理歷程。</p>
            </template>
        </div>
    </Dialog>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import Dialog from 'primevue/dialog';
import Timeline from 'primevue/timeline';
import Card from 'primevue/card';

const props = defineProps({
    visible: {
        type: Boolean,
        default: false
    },
    historyData: { // 將 statusHistory 改名為 historyData，避免與 FeedbackProgress 的 props 混淆
        type: Array,
        default: () => []
    }
});

const emit = defineEmits(['update:visible']);

const closeModal = () => {
    emit('update:visible', false);
};

const formatDateTime = (datetime) => {
    if (!datetime) return '';
    const date = new Date(datetime);
    return date.toLocaleString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};

const getStatusColor = (status) => {
    switch (status) {
        case '待處理':
            return '#ef4444'; // Red for danger
        case '處理中':
            return '#f59e0b'; // Orange for warning
        case '確認中':
            return '#3b82f6'; // Blue for info
        case '已結案':
            return '#6b7280'; // Gray for secondary
        default:
            return '#a1a1aa'; // Default gray
    }
};

const getStatusTextColor = (status) => {
    switch (status) {
        case '待處理':
            return 'text-red-500';
        case '處理中':
            return 'text-orange-500';
        case '確認中':
            return 'text-blue-500';
        case '已結案':
            return 'text-gray-500';
        default:
            return 'text-gray-400';
    }
};

const getStatusIcon = (status) => {
    switch (status) {
        case '待處理':
            return 'pi pi-exclamation-circle';
        case '處理中':
            return 'pi pi-hourglass';
        case '確認中':
            return 'pi pi-question-circle';
        case '已結案':
            return 'pi pi-check-circle';
        default:
            return 'pi pi-info-circle';
    }
};
</script>

<style>
/* PrimeVue Dialog 的背景和文字顏色調整 */
.custom-primevue-modal .p-dialog-content {
    background-color: #2A3A56;
    /* 與您的 bg-darkx 一致 */
    color: #ffffff;
}

.custom-primevue-modal .p-dialog-header {
    background-color: #2A3A56;
    /* 與您的 bg-darkx 一致 */
    color: #ffffff;
    border-bottom: 1px solid #4a638d;
    /* 添加分隔線 */
}

.custom-primevue-modal .p-dialog-title {
    color: #ffffff;
}

.custom-primevue-modal .p-dialog-header-icons .p-dialog-header-icon {
    color: #ffffff;
}

/* Custom styles for PrimeVue Timeline and Card */
.custom-timeline .custom-marker {
    display: flex;
    width: 2rem;
    height: 2rem;
    align-items: center;
    justify-content: center;
    color: #ffffff;
    border-radius: 50%;
    z-index: 1;
}

.custom-timeline .p-timeline-event-content,
.custom-timeline .p-timeline-event-opposite {
    line-height: 1;
}

.custom-card.p-card {
    background-color: #4a638d;
    /* 與您的 bg-darky 一致 */
    color: #ffffff;
    border: none;
    box-shadow: none;
}

.custom-card .p-card-body {
    padding: 1rem;
}

.custom-card .p-card-title {
    color: #e0e0e0;
}

.custom-card .p-card-subtitle {
    color: #b0bec5;
}


.text-red-500 {
    color: #ef4444;
}

.text-orange-500 {
    color: #f59e0b;
}

.text-blue-500 {
    color: #3b82f6;
}

.text-gray-500 {
    color: #6b7280;
}

.text-gray-400 {
    color: #a1a1aa;
}
</style> -->