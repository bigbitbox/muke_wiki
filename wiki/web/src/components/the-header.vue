<template>
    <a-layout-header class="header">
      <!-- <div class="logo" /> -->
      <a-menu
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1">
          <RouterLink to="/">首页</RouterLink>
        </a-menu-item>
        <a-menu-item key="2">
          <RouterLink to="/admin/ebook">电子书管理</RouterLink>
        </a-menu-item>
        <a-menu-item key="3">
          <RouterLink to="/about">关于我们</RouterLink>
        </a-menu-item>
      </a-menu>
    </a-layout-header>
</template>

<script setup lang="ts">

</script>

<style scoped>

</style>