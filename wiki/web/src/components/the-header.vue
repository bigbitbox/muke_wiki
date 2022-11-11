<template>
    <a-layout-header class="header">
      <!-- <div class="logo" /> -->
      <a-menu
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="/">
          <RouterLink to="/">首页</RouterLink>
        </a-menu-item>
        <a-menu-item key="/admin/ebook">
          <RouterLink to="/admin/ebook">电子书管理</RouterLink>
        </a-menu-item>
        <a-menu-item key="/admin/category">
          <RouterLink to="/admin/category">分类管理</RouterLink>
        </a-menu-item>
        <a-menu-item key="/about">
          <RouterLink to="/about">关于我们</RouterLink>
        </a-menu-item>
      </a-menu>
    </a-layout-header>
</template>

<script setup lang="ts">

</script>

<style scoped>

</style>