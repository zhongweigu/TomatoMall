<script setup >
import NavigationBar from "@/components/NavigationBar.vue";
import {onMounted, ref} from "vue";
import {getAllCarts, addCart, deleteCart, updateCart, checkout} from "@/api/carts.js";
import {pay} from "@/api/order.js";

const fetchCarts = () => {
  // TODO:获取购物车列表
}

onMounted(() => {
  fetchCarts()
})

// TODO: 订单模态框显示，参考ProductManagement，显示当前订单信息和checkout按钮
const orderDialogVisible = ref(false)

// TODO: 订单确认模态框显示，在checkout后，后端会返回订单信息（见文档2.3.1），显示这些信息和“确认支付”按钮，点击确认支付调用/api/orders/{orderId}/pay
const orderConfirmDialogVisible = ref(false)


// TODO: 支付调用，支付完成后后端会自动跳转到购物车页面，无需额外操作
const pay = (row) =>{
  window.open("http://localhost:8080/api/orders/" + row.orderNo + "/pay")
  // TODO: ...
}
</script>

<template>
  <NavigationBar ></NavigationBar>

</template>

<style scoped>

</style>
