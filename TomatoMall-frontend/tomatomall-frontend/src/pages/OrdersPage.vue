<script setup >
import NavigationBar from "@/components/NavigationBar.vue";
import {onMounted, ref} from "vue";
import {getAllCartsOrdersRelation, getCartsOrdersRelation, deleteCartsOrdersRelation} from "@/api/order.js";

const fetchOrders = () => {
  // TODO:获取已完成的订单列表(getAllCartsOrdersRelation)
}

onMounted(() => {
  fetchOrders()
})

// TODO: 查看订单详细信息模态框，显示订单信息（getCartsOrdersRelation），订单对应的商品信息（getProductById）,操作就一个删除(deleteCartsOrdersRelation)，同样参考ProductManagement
const orderDialogVisible = ref(false)


</script>

<template>
  <NavigationBar ></NavigationBar>
</template>

<style scoped>

</style>
