<template>
    <canvas ref="canvas"></canvas>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Chart, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from 'chart.js'

Chart.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend)

const props = defineProps({
    labels: Array,
    data: Array,
})

const canvas = ref(null)
let chartInstance = null

function renderChart() {
    if (chartInstance) chartInstance.destroy()

    chartInstance = new Chart(canvas.value, {
        type: 'bar',
        data: {
            labels: props.labels,
            datasets: [
                {
                    label: '票數',
                    data: props.data,
                    backgroundColor: '#6699cc',
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: false,
                },
            },
            scales: {
                y: {
                    beginAtZero: true,
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
