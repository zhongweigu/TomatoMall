<script setup>
import {ElMenu, ElMenuItem} from "element-plus";
import Icon from "@/components/Ico.vue";
import {Search} from "@element-plus/icons-vue";

const identity = sessionStorage.getItem("role");

</script>

<template>
  <div class="nav-bar-container">
    <el-menu
        class="nav-bar"
        mode="horizontal"
        :router="true"
    >
      <el-menu-item index="/home">
        <Icon iconName="icon-shouyeA" />
        <span>番茄书城</span>
      </el-menu-item>
      <el-menu-item index="/cart">
        <Icon iconName="icon-gouwuche" />
        <span>购物车</span>
      </el-menu-item>
      <el-menu-item index="/orders">
        <Icon iconName="icon-cangku" />
        <span>订单管理</span>
      </el-menu-item>
      <el-menu-item index="/personal">
        <Icon iconName="icon-geren" />
        <span>个人中心</span>
      </el-menu-item>
      <el-menu-item v-if="identity === 'staff'" index="/product-management">
        <Icon iconName="icon-kufang" />
        <span>商品管理</span>
      </el-menu-item>
      <el-menu-item index="/forum">
        <Icon iconName="icon-luntan" />
        <span>番茄论坛</span>
      </el-menu-item>
      <el-menu-item index="/search">
        <Icon iconName="icon-sousuo" />
        <span>搜索</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<style scoped>
.nav-bar-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 60px;
}

.nav-bar {
  display: inline-block;
  width: 100%;
  margin: 0 auto;
  height: 100%;
  background-color: rgba(243, 200, 172, 0.8);
}

</style>
