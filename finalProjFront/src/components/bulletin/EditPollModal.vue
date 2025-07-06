<template>

    <ModalWrapper :visible="visible" :height="'80%'" :width="'70%'"
        @update:visible="val => { emit('update:visible', val); recoverPoll(); }" :title="'修改投票'">


        <form @submit.prevent="submitEdit">
            <div class="my-3 card-poll">
                <label>投票標題</label>
                <input type="text" name="" id="" v-model="pollForm.title" class="form-control" placeholder="投票標題">
                <div class="form-check form-switch my-3">
                    <input type="checkbox" id="isMultiple" class="form-check-input " v-model="pollForm.isMultiple">
                    <label for="isMultiple" class="form-check-label text-dark ">是否為複選</label>
                </div>
                <!-- 動態選項區 -->
                <div class="mb-3">
                    <label class="fw-bold">修改/刪除選項</label>
                    <div v-for="(option, index) in pollForm.options" :key="index"
                        class="d-flex align-items-center mb-2">
                        <input type="text" class="form-control me-2" v-model="pollForm.options[index].text"
                            placeholder="請輸入選項內容" style="width: 80%;" />
                        <button type="button" class="btn btn-poll" @click="removeOption(index)"
                            style="font-size: 80%;">刪除</button>
                    </div>
                    <div>
                        <button type="button" class="btn btn-poll mt-2" @click="addOption">＋
                            新增選項</button>
                    </div>
                </div>
                <div class="mb-3">
                    <label>投票開始時間</label>
                    <input type="datetime-local" name="" id="" v-model="pollForm.start" class="form-control"
                        placeholder="投票開始時間">
                </div>
                <div class="mb-3">
                    <label>投票結束時間</label>
                    <input type="datetime-local" name="" id="" v-model="pollForm.end" class="form-control"
                        placeholder="投票截止時間">
                </div>
                <div class="text-end m-2 pb-4">
                    <button type="button" class="btn btn-primary">送出修改</button>
                </div>

            </div>
        </form>

    </ModalWrapper>

</template>

<script setup>
import { ref, watch, computed } from 'vue'
import ModalWrapper from '@/components/bulletin/ModalWrapper.vue'
import axios from 'axios'
import Swal from 'sweetalert2'

const fileInput = ref(null) // 綁定 ref


const pollLabels = computed(() => props.bulletin?.poll?.options?.map(opt => opt.text) || [])
const pollVotes = computed(() => props.bulletin?.poll?.options?.map(opt => opt.votesCount || 0) || [])


const props = defineProps({
    visible: Boolean,
    poll: Object,
    pollBackup: Object,
    communityId: Number,
    usersId: Number
})
const emit = defineEmits(['update:visible', 'updated'])

const pollForm = ref({
    id: "",
    title: "",
    isMultiple: false,
    start: "",
    end: "",
    options: [
        { text: "" },
        { text: "" }
    ]
})

watch(() => props.poll, (val) => {
    if (val) {
        pollForm.value.id = val.id
        pollForm.value.title = val.title
        pollForm.value.isMultiple = val.isMultiple
        pollForm.value.start = val.start
        pollForm.value.end = val.end
        pollForm.value.options = val.options

    }
}, { immediate: true })


function addOption() {
    pollForm.value.options.push({ text: '' })
}

function removeOption(index) {
    pollForm.value.options.splice(index, 1)
}

function recoverPoll() {
    console.log(props.pollBackup);
    pollForm.value = props.pollBackup
}


function submitEdit() {

    const data = {
        id: pollForm.value.id,
        title: pollForm.value.title,
        isMultiple: pollForm.value.isMultiple,
        start: pollForm.value.start,
        end: pollForm.value.end,
        options: pollForm.value.options
    }

    axios.put(`http://localhost:8080/api/poll/${props.poll.id}`, data)
        .then(() => {
            emit('updated')
            emit('update:visible', false)
            Swal.fire({
                title: '成功',
                text: '修改成功',
                icon: 'success',
                confirmButtonText: '確定'
            })
        })
        .catch(error => {
            console.error(error)
            Swal.fire({
                title: '失敗',
                text: '修改失敗',
                icon: 'error',
                confirmButtonText: '確定'
            })
        })
}








</script>

<style scoped>
:deep(.form-control),
:deep(.form-select) {
    background-color: #fff !important;
    color: #161616 !important;
}

.list-group-item {
    font-size: 0.95rem;
}

.btn-poll,
.btn-remove {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 12px 24px;
    border: none;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #cfd4ec 0%, #d7d7d7 100%);
    color: rgb(127, 127, 127);
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}

.btn-remove {
    padding: 8px 16px;
    background: #9cc8f3;
    color: rgb(60, 60, 60);
}

.btn-poll::before,
.btn-remove::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.5s;
}

.btn-poll:hover::before,
.btn-remove:hover::before {
    left: 100%;
}

.btn-poll:hover,
.btn-remove:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.btn-poll:active,
.btn-remove:active {
    transform: translateY(0);
}

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

.modal-content-edit {
    background: rgb(255, 255, 255);
    padding: 1%;
    width: 50%;
    max-width: 500px;
    border-radius: 12px;
    height: 65%;
    margin: 10% auto;
    border: 1px solid #ccc;

}
</style>