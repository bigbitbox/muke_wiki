<template>
    <a-layout-footer style="text-align: center">
      wiki电子书网站<span v-show = "user.id">,欢迎：{{user.name}}</span>
    </a-layout-footer>
</template>

<script  lang="ts">
import store from '@/store';
import { defineComponent ,computed} from 'vue';

export default defineComponent({
  name: 'HomeView',
});

</script>

<script setup lang="ts">
  const user = computed(()=>store.state.user)
</script>

<style scoped>

</style>