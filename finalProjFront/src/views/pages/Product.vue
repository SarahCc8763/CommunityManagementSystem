<template>
    <h3>產品功能</h3>
    <div class="container text-center">
        <div class="row">
            <div class="col">
                <button class="btn btn-primary" @click="openModal('insert')">開啟新增</button>
            </div>
            <div class="col">
                <input type="text" placeholder="請輸入產品名稱" v-model="findName" @input="callFind(1)">
            </div>
            <div class="col">
                <ProductSelect :total="total" v-model="rows" @select-change="callFind" :options="[2,3,4,5]"></ProductSelect>
            </div>
        </div>
        <br>

        <div class="row">
            <div class="col-6" v-show="total > 0">
            <Paginate
            :first-last-button="true" 
            first-button-text="&lt;&lt;" 
            last-button-text="&gt;&gt;" 
            prev-text="&lt;"
            next-text="&gt;"
            :initial-page="current"
            v-model="current"
            :page-count="pages"
            :click-handler="callFind"
            >
            </Paginate>
            </div>
        </div>
        <br>

        <div class="row">
            <div class="col-12 col-sm-12 col-md-6 col-lg-3 col-xl-3 col-xxl-2" v-for="item in products" :key="item.id">
                <ProductCard :product="item" @open-update="openModal" @delete="callRemove"></ProductCard>
            </div>
        </div>
    </div>
    <ProductModal ref="productModelRef" v-model:product="product" :is-show-button-insert="isShowButtonInsert" @update="callModify" @insert="callCreate"></ProductModal>
</template>
    
<script setup>
import { ref, onMounted } from 'vue';

import axios from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import Paginate from 'vuejs-paginate-next';

import ProductCard from '@/components/ProductCard.vue';
import ProductSelect from '@/components/ProductSelect.vue';
import ProductModal from '@/components/ProductModal.vue';

const product = ref({});
const isShowButtonInsert = ref(true);
const productModelRef = ref(null);

const findName = ref("");
const total = ref(0);
const pages = ref(100);
const current = ref(50);
const start = ref(0);
const rows = ref(3);

const lastPageRows = ref(0);

const products = ref([]);
async function openModal(action, id) {
    if(action === "insert") {
        isShowButtonInsert.value = true;
        product.value = {};
    }else{
        isShowButtonInsert.value = false;
        callFindById(id);
    }
    productModelRef.value.showModal();
}

onMounted(function() {
    callFind();
})

async function callFind(page) {
    console.log(rows.value)
    if (page) {
        start.value = (page - 1) * rows.value;
        current.value = page;
    }else{
        start.value = 0;
        current.value = 1;
    }

    if(findName.value === ""){
        findName.value = null;
    }
    console.log('方法內'+ rows.value)
    const body = {
        "start": start.value,
        "rows": rows.value,
        "name": findName.value,
        "order": "id",
        "dir": false
    };

    try {
        const response = await axios.post("/ajax/pages/products/find", body);
        console.log("response", response);
        products.value = response.data.list;
        total.value = response.data.count;
        pages.value = Math.ceil(total.value / rows.value);
        lastPageRows.value = total.value % rows.value;

        

        setTimeout(function () {
            Swal.close();
        }, 200);
    } catch (error) {
        console.log("error", error);
        Swal.fire({
            title: '出現了奇怪的錯誤 !',
            text: error.message,
            icon: 'error',
            showConfirmButton: false,
            allowOutsideClick: false,
        })
        setTimeout(function () {
            Swal.close();
        }, 2000);

    }

}

    function callFindById(id) {
            axios.get(`/ajax/pages/bean/products/${id}`)
                .then(response => {
                    console.log("response", response);
                    if(response.data){
                        product.value = response.data.list[0];
                    }
                })
                .catch(error => {
                    console.log("error", error);
                    Swal.fire({
                        title: '出現了奇怪的錯誤 !',
                        text: error.message,
                        icon: 'error',
                        showConfirmButton: false,
                        allowOutsideClick: false,
                    })
                    setTimeout(function () {
                        Swal.close();
                    }, 200);
                });

        }

        async function callRemove(id) {
            const result = await Swal.fire({
                title: '刪除資料',
                text: '確定要刪除資料嗎????',
                icon: 'question',
                showCancelButton: true,
                allowOutsideClick: false,
                confirmButtonText: '確定',
                cancelButtonText: '我再想想',
            });
            if (result.isConfirmed) {
                Swal.fire({
                    title: '刪除資料',
                    text: 'Loading....',
                    icon: 'question',
                    showConfirmButton: false,
                    allowOutsideClick: false,
                });
                try {
                    const response = await axios.delete("/ajax/pages/products/" + id);
                    if (response.data.success) {
                        Swal.fire({
                            title: '刪除成功',
                            text: "已刪除id為 " + id + "的資料",
                            confirmButtonText: '掰掰QQ',
                        })
                        if(lastPageRows.value === 1 && current.value >1){
                            current.value = current.value - 1;
                            }
                        callFind(current.value);
                    } else {
                        Swal.fire({
                            title: '刪除失敗',
                            text: response.data.message,
                            icon: 'warning',
                            confirmButtonText: '太難過了QQ',
                        })
                    }
                } catch (error) {
                    console.log("error", error);
                    Swal.fire({
                        title: '出現了奇怪的錯誤 !',
                        text: error.message,
                        icon: 'error',
                        showConfirmButton: false,
                        allowOutsideClick: false,
                    })
                    setTimeout(function () {
                        Swal.close();
                    }, 200);

                }
            }
        }

        async function callModify() {
            if(product.value.id===""){
                product.value.id = null;
            }
            if(product.value.name===""){
                product.value.name = null;
            }
            if(product.value.price===""){
                product.value.price = null;
            }
            if(product.value.make===""){
                product.value.make = null;
            }
            if(product.value.expire===""){
                product.value.expire = null;
            }

            const body = product.value;

            try {
                const response = await axios.put(`/ajax/pages/products/${product.value.id}`, body);
                console.log("response", response);

                if (response.data.success) {
                    
                    Swal.fire({
                        title: '修改成功',
                        confirmButtonText: '太棒了 !',
                    }).then(() => {
                        callFind(current.value);
                        productModelRef.value.hideModal();
                    })
                    ;
                } else {
                    Swal.fire({
                        title: '修改失敗',
                        text: response.data.message,
                        icon: 'warning',
                        confirmButtonText: '太難過了QQ',
                    });
                }
            } catch (error) {
                console.log("error", error);
                Swal.fire({
                    title: '出現了奇怪的錯誤 !',
                    text: error.message,
                    icon: 'error',
                    showConfirmButton: false,
                    allowOutsideClick: false,
                })
                setTimeout(function () {
                    Swal.close();
                }, 200);

            }
        }

        async function callCreate() {
            if(product.value.id===""){
                product.value.id = null;
            }
            if(product.value.name===""){
                product.value.name = null;
            }
            if(product.value.price===""){
                product.value.price = null;
            }
            if(product.value.make===""){
                product.value.make = null;
            }
            if(product.value.expire===""){
                product.value.expire = null;
            }

            try {
                const response = await axios.post("/ajax/pages/products", product.value);
                console.log("response", response);
                
                if (response.data.success) {
                    Swal.fire({
                        title: '新增成功',
                        confirmButtonText: '太棒了 !',
                    }).then(() => {
                        callFind(current.value);
                        product.value = {};
                    });
                } else {
                    Swal.fire({
                        title: '查詢失敗',
                        text: response.data.message,
                        icon: 'warning',
                        confirmButtonText: '太難過了QQ',
                    });
                }
            } catch (error) {
                console.log("error", error);
                Swal.fire({
                    title: '出現了奇怪的錯誤 !',
                    text: error.message,
                    icon: 'error',
                    showConfirmButton: false,
                    allowOutsideClick: false,
                })
                setTimeout(function () {
                    Swal.close();
                }, 2000);
            }
        }

</script>
    
<style>

</style>