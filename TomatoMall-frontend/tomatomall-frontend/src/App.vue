<template>
  <el-container>
    <router-view/>
  </el-container>
</template>

<style>
body {
  background: linear-gradient(
      to bottom,
      #FFFFFF 0%,
      #FFFFFF 15%,
      #FFF0EB 100%
  );
  overflow-x: hidden;
  max-width: 100vw;
  min-height: 100vh; /* 确保渐变覆盖全屏 */
}
</style>
<script setup lang="js">
</script>
