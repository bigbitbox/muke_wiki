<template>
  <a-layout-header class="header">
    <div class="logo">wiki知识库</div>
    <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
      <a-menu-item key="/">
        <RouterLink to="/">首页</RouterLink>
      </a-menu-item>
      <a-menu-item key="/admin/user" v-if="user.id">
        <RouterLink to="/admin/user">用户管理</RouterLink>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" v-if="user.id">
        <RouterLink to="/admin/ebook">电子书管理</RouterLink>
      </a-menu-item>
      <a-menu-item key="/admin/category" v-if="user.id">
        <RouterLink to="/admin/category">分类管理</RouterLink>
      </a-menu-item>
      <a-menu-item key="/about">
        <RouterLink to="/about">关于我们</RouterLink>
      </a-menu-item>
      <a-menu-item key="/login" style="margin-left: auto" v-if="!user.id">
        <a @click="showLoginModal">登录</a>
      </a-menu-item>

      <a-menu-item key="/login" style="margin-left: auto" v-if="user.id">
        <span>你好：{{ user.name }}</span>
      </a-menu-item>
      <a-popconfirm
        title="确认退出登录?"
        ok-text="是"
        cancel-text="是"
        @confirm="logout()"
      >
        <a-menu-item key="/logout" style="margin-left: 0" v-if="user.id">
          <a>退出登录</a>
        </a-menu-item>
      </a-popconfirm>
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
import store from '@/store';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { ref, computed, defineComponent } from 'vue';
import { RouterLink } from 'vue-router';
declare let hexMd5: any;
declare let KEY: any;
export default defineComponent({
  name: "HomeView",
});
</script>

<script setup lang="ts">

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

const user = computed(() => store.state.user);

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
      // user.value = data.content;
      store.commit("setUser", data.content);
    } else {
      message.error(data.message);
    }
  });
};

// 退出登录
const logout = () => {
  console.log("退出登录开始");
  axios.get("/user/logout/" + user.value.token).then((response) => {
    const data = response.data;
    if (data.success) {
      message.success("退出登录成功！");
      store.commit("setUser", {});
    } else {
      message.error(data.message);
    }
  });
};
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>
