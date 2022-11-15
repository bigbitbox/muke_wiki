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
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>

      <a-form-item label="父文档">
        <a-tree-select
          v-model:value="doc.parent"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="treeSelectData"
          placeholder="请选择父文档"
          tree-default-expand-all
          :field-names="{ children: 'children', label: 'name', value: 'id' }"
        >
        </a-tree-select>
      </a-form-item>

      <a-form-item label="父分类">
        <!-- <a-input v-model:value="doc.parent" /> -->
        <a-select v-model:value="doc.parent" ref="select">
          <a-select-option value="0"> 新加分类 </a-select-option>
          <a-select-option
            v-for="c in level1"
            :key="c.id"
            :value="c.id"
            :disabled="doc.id === c.id"
          >
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
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
  name: "AdminDoc",
  // components: {
  //
  // },
  setup() {
    let docs: any;
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
      // {
      //   title: '分类',
      //   slots: {customRender: 'doc'}
      // },
      {
        title: "父分类",
        dataIndex: "parent",
      },
      {
        title: "顺序",
        dataIndex: "sort",
      },
      // {
      //   title: '文档数',
      //   dataIndex: 'docCount'
      // },
      // {
      //   title: '阅读数',
      //   dataIndex: 'viewCount'
      // },
      // {
      //   title: '点赞数',
      //   dataIndex: 'voteCount'
      // },
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
      level1.value = [];
      axios.get("/doc/allList").then((resp) => {
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

        // docs.value = data.content;
      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    // -------- 表单 ---------
    const doc = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const treeSelectData = ref();
    treeSelectData.value = [];
    // 删除方法
    const handleDelete = (id: number) => {
      axios.get("/doc/remove", { params: { id: id } }).then((resp) => {
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
      console.log(doc.value);
      axios.post("/doc/save", doc.value).then((resp) => {
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
      doc.value = record;
      modalVisible.value = true;

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id:0,name:'无'});
    };

    // 不能选择当前节点以及子孙所有节点

    //新增
    const add = () => {
      doc.value = {};
      modalVisible.value = true;

      treeSelectData.value = Tool.copy(level1.value) || [];

      treeSelectData.value.unshift({id:0,name:'无'});
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
      // handleQuerydoc;
    });
    return {
      docs,
      pagination,
      columns,
      loading,

      add,
      edit,
      modalLoading,
      modalVisible,
      handleModalOk,
      handleDelete,
      doc,

      level1,
      treeSelectData,
    };
  },
});
</script>
