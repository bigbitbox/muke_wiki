<template>
  <a-layout>
    <a-layout-content
      :style="{
        background: '#fff',
        padding: '24px',
        margin: 0,
        minHeight: '280px',
      }"
    >
      <p>
        <a-button type="primary" @click="add()" size="large">新增</a-button>
      </p>
      <a-table
        :columns="columns"
        :row-key="(record:any) => record.id"
        :data-source="level1"
        :pagination="false"
        :loading="loading"
      >
        <template #cover="{ text: cover }">
          <img
            v-if="cover"
            :src="cover"
            alt="avatar"
            style="width: 20%; height: 20%"
          />
        </template>


        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)"> 编辑 </a-button>
            <a-popconfirm
              title="删除后不可恢复，确定删除？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)"
            >
              <a-button type="danger"> 删除 </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
    title="分类表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form
      :model="category"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <!-- <a-input v-model:value="category.parent" /> -->
        <a-select v-model:value="category.parent" ref="select">
          <a-select-option value="0"> 新加分类 </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id===c.id">
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { Tool } from "@/utils/tool";
import { message } from "ant-design-vue";
import { any } from "_vue-types@3.0.2@vue-types";

export default defineComponent({
  name: "AdminCategory",
  // components: {
  //
  // },
  setup() {
    let categorys:any;
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0,
    });
    const loading = ref(false);
    const columns = [
      {
        title: "名称",
        dataIndex: "name",
      },

      {
        title: "分类",
        slots: {customRender: 'category'}
        // dataIndex: "parent",
      },

      {
        title: "顺序",
        dataIndex: "sort",
      },

      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" },
      },
    ];

    /*
     * 数据查询
     * */
    const level1 = ref();
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/category/allList").then((resp) => {
        loading.value = false;
        const data = resp.data;
        if (data.success) {
          console.log(data.content);
          console.log("origin array", data.content);

          level1.value = [];
          level1.value = Tool.array2Tree(data.content, 0);
          console.log("树形结构", level1);
        } else {
          message.error(data.message);
        }

        categorys.value = data.content;
      });
    };

    // -------- 表单 ---------
    const category = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    // 删除方法
    const handleDelete = (id: number) => {
      axios.get("/category/remove", { params: { id: id } }).then((resp) => {
        if (resp.data.success) {
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };

    const handleModalOk = () => {
      modalLoading.value = true;
      console.log(category.value);
      axios.post("/category/save", category.value).then((resp) => {
        const data = resp.data;
        if (data.success) {
          modalVisible.value = false;
          modalLoading.value = false;
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };
    //编辑
    const edit = (record: any) => {
      category.value = record;
      modalVisible.value = true;
    };
    //新增
    const add = () => {
      category.value = {};
      modalVisible.value = true;
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
      // handleQueryCategory;
    });
    return {
      categorys,
      pagination,
      columns,
      loading,

      add,
      edit,
      modalLoading,
      modalVisible,
      handleModalOk,
      handleDelete,
      category,

      level1,
    };
  },
});
</script>
