<template>
  <a-layout>
    <div id="container">
      <the-header id="header" />
      <RouterView id="router" />
      <the-footer id="footer" />
    </div>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import TheHeader from "@/components/the-header.vue"; // @ is an alias to /src
import TheFooter from "@/components/the-footer.vue";
import TheWelcome from "./components/the-welcome.vue";

export default defineComponent({
  name: "app",
  components: {
    TheHeader,
    TheFooter,
  },
});
</script>

<style scoped>
.site-layout-background {
  background: #fff;
}

html,body{
height:100%;
width:100%;
}

/* #container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

#header {
  background: #999;
  height: 60px;
}
#router {
  height: 100%;
  width: 100%;
  background: orange;
  flex: 1;
}
#footer {
  background: #333;
  height: 60px;
} */
</style>
