<template>
    <canvas ref="canvas" class="chart-wrapper "></canvas>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import {
    Chart,
    BarElement,
    BarController,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend
} from 'chart.js'

const props = defineProps({
    labels: Array,
    data: Array,
})


Chart.register(BarController, BarElement, CategoryScale, LinearScale, Tooltip, Legend)



const canvas = ref(null)
let chartInstance = null
function renderChart() {
    if (chartInstance) chartInstance.destroy()

    // ✅ 根據資料數設定 canvas 高度
    const barHeight = 40
    const padding = 40
    const canvasHeight = props.labels.length * barHeight + padding
    canvas.value.height = canvasHeight

    const maxDataValue = Math.max(...props.data) || 0
    const suggestedMax = maxDataValue < 5 ? 5 : Math.ceil((maxDataValue + 3) / 10) * 10

    chartInstance = new Chart(canvas.value, {
        type: 'bar',
        data: {
            labels: props.labels,
            datasets: [
                {
                    label: '票數',
                    data: props.data,
                    backgroundColor: '#6699cc',
                    maxBarThickness: 40,
                },
            ],
        },
        options: {
            responsive: false, // ✅ 一定要關掉，才會用 DOM 高度
            maintainAspectRatio: false,
            indexAxis: 'y',
            plugins: {
                legend: {
                    display: false,
                },
            },
            scales: {
                x: {
                    beginAtZero: true,
                    max: suggestedMax,
                    ticks: {
                        stepSize: 1,
                    },
                },
            },
        },
    })
}


onMounted(renderChart)
watch(() => [props.labels, props.data], renderChart, { deep: true })
</script>
<style scoped>
.chart-wrapper {
    width: 100%;
    max-width: 800px;
}

canvas {
    display: block;
    width: 100% !important;
    height: auto !important;
}
</style>