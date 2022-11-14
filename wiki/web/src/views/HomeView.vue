<template>
  <div class="home">
    <a-layout>
      <a-layout-sider width="200" style="background: #fff">
        <a-menu mode="inline" :style="{ height: '100%', borderRight: 0 }" @click="handleClick">
          <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span><user-outlined />{{ item.name }}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <MailOutlined /><span>{{ child.name }}</span>
            </a-menu-item>
          </a-sub-menu>
        </a-menu >
      </a-layout-sider>
      <a-layout-content
        :style="{
          background: '#fff',
          padding: '24px',
          margin: 0,
          minHeight: '280px',
        }"
      >
        <div class="welcome" v-show="isShowWelcom">
          <h1>欢迎使用wiki知识库</h1>
        </div>

        <a-list
          v-show="!isShowWelcom"
          item-layout="vertical"
          size="large"
          :grid="{ gutter: 20, column: 3 }"
          :pagination="pagination"
          :data-source="ebooks"
        >
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span v-for="{ type, text } in actions" :key="type">
                  <component :is="type" style="margin-right: 8px" />
                  {{ text }}
                </span>
              </template>

              <a-list-item-meta :description="item.description">
                <template #title>
                  <a :href="item.href">{{ item.name }}</a>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/utils/tool";

export default defineComponent({
  name: "HomeView",
  setup() {
    console.log("setup");

    const ebooks = ref();
    const ebooks2 = reactive({ books: [] });

    /**
     * 查询所有分类
     */
    const level1 = ref();
    let categorys: any;



    const handleQueryCategory = () => {
      axios.get("/category/allList").then((resp) => {
        const data = resp.data;
        categorys = data.content;

        if (data.success) {
          console.log(data.content);
          console.log("原始数组：", data.content);

          level1.value = [];
          level1.value = Tool.array2Tree(data.content, 0);
          console.log("树形结构：", level1);
        } else {
          message.error(data.message);
        }
      });
    };




    
    /**
     * 获取ebook
     */

     const handleQueryEbook = ()=>{
      axios
        // .get("http://localhost:8080/getEbooks")
        .get("/ebook/getEbookListByPage", {
          params: {
            page: 1,
            size: 1000,
            category2_id:category2id
          },
        })
        .then((resp) => {
          console.log(resp);
          const data = resp.data.content.list;
          ebooks.value = data;
          ebooks2.books = data;
        });
     }


    /**
     * 修改tab的展示
     */
    const isShowWelcom = ref(true);
    let category2id = 0;
    const handleClick = (value:any) => {
      console.log("menu click",value);
      if (value.key === 'welcome'){
        isShowWelcom.value = true;
      } else {
        category2id = value.key;
        isShowWelcom.value = false;
        handleQueryEbook();
      }

      /**
       * 代码简化
       * isShowWelcom.value = value.key === 'welcome';
       */

    };

    onMounted(() => {
      handleQueryCategory();

    });
    return {
      level1,
      handleClick,
      isShowWelcom,

      ebooks,
      ebooks2: toRef(ebooks2, "books"),
      // listData,
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        { type: "StarOutlined", text: "156" },
        { type: "LikeOutlined", text: "156" },
        { type: "MessageOutlined", text: "2" },
      ],
    };
  },
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
