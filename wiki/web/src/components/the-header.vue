<template>
  <a-layout-header class="header">
    <!-- <div class="logo" /> -->
    <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
      <a-menu-item key="/">
        <RouterLink to="/">首页</RouterLink>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <RouterLink to="/admin/user">用户管理</RouterLink>
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
      <a-menu-item key="/login" style="margin-left: auto" v-if = "!user.id">
        <a @click="showLoginModal">登录</a>
      </a-menu-item>
      <a-menu-item key="/login" style="margin-left: auto" v-if = "user.id">
        <span>你好：{{user.name}}</span>
      </a-menu-item>
    </a-menu>
    <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
    >
      <a-form
        :model="loginUser"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.login_name" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
  declare let hexMd5: any;
  declare let KEY: any;
</script>

<script setup lang="ts">
  import { ref, defineComponent } from "vue";
  import axios from "axios";
  import { message, Modal } from "ant-design-vue";

  const loginUser = ref({
    loginName: "test",
    password: "test",
  });
  const loginModalVisible = ref(false);
  const loginModalLoading = ref(false);
  const showLoginModal = () => {
    loginModalVisible.value = true;
  };

  // 登录

  const user = ref();
  user.value = {};

  const login = () => {
    console.log("开始登录");
    loginModalLoading.value = true;
    loginUser.value.password = hexMd5(loginUser.value.password + KEY);
    axios.post("/user/userLogin", loginUser.value).then((response) => {
      loginModalLoading.value = false;
      const data = response.data;
      if (data.success) {
        loginModalVisible.value = false;
        message.success("登录成功！");
        user.value = data.content;
      } else {
        message.error(data.message);
      }
    });
  };
</script>


<style scoped></style>
