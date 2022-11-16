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
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-button type="primary" @click="add()" size="lage">
              新增
            </a-button>
          </p>
          <a-table
            v-if="level1.length > 0"
            :columns="columns"
            :row-key="(record) => record.id"
            :data-source="level1"
            :pagination="false"
            :loading="loading"
            size="small"
            :default-expand-all-rows="true"
          >
            <template #cover="{ text: record }">
              {{ record.sort }} {{ text }}
              <!--                            <img v-if="cover" :src="cover" alt="avatar" style="width: 20%;height: 20%"/>-->
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                  title="删除后无法恢复，确定删除？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small"> 删除 </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()"> 保存 </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <!--      <a-form-item label="封面">-->
            <!--        <a-input v-model:value="doc.cover"/>-->
            <!--      </a-form-item>-->
            <a-form-item label="名称">
              <a-input v-model:value="doc.name" placeholder="名称" />
            </a-form-item>
            <a-form-item label="父文档">
              <a-tree-select
                v-model:value="doc.parent"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeSelectData"
                placeholder="请选择父文档"
                tree-default-expand-all
                :field-names="{
                  children: 'children',
                  label: 'name',
                  value: 'id',
                }"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item label="父文档编号">
              <a-input v-model:value="doc.parent" />
              <a-select v-model:value="doc.parent" ref="select">
                <a-select-option value="0"> 创建父文档 </a-select-option>
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
              <a-input v-model:value="doc.sort" placeholder="顺序" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined />
              </a-button>
            </a-form-item>
            <a-form-item label="内容">
              <div id="content"></div>
            </a-form-item>
            <!--      <a-form-item label="描述">-->
            <!--        <a-input v-model:value="doc.description" type="textarea"/>-->
            <!--      </a-form-item>-->
          </a-form>
        </a-col>
      </a-row>

      <a-drawer
        width="900"
        placement="right"
        :closable="false"
        :visible="drawerVisible"
        @close="onDrawerClose"
      >
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
  <!--    <a-modal-->
  <!--            title="分类表单"-->
  <!--            v-model:visible="modalVisible"-->
  <!--            :confirm-loading="modalLoading"-->
  <!--            @ok="handleSave"-->
  <!--    >-->
  <!--        -->
  <!--    </a-modal>-->
</template>
<script lang="ts">
import { createVNode, defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { Tool } from "@/utils/tool";
import { message, Modal } from "ant-design-vue";
import { useRoute } from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from "wangeditor";

export default defineComponent({
  name: "AdminDoc",
  // components: {
  //
  // },
  setup() {
    const docs = ref();

    const route = useRoute();
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0,
    });
    const loading = ref(false);
    const columns = [
      {
        title: "名称",
        dataIndex: "name",
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
    const level1 = ref(); //一级文档树，children属性就是二级文档
    level1.value = [];

    const handleQuery = (params: any) => {
      loading.value = true;
      //如果不清空现有数据，则编辑保存数据后，再编辑数据会是之前的数据
      level1.value = [];
      axios.get("/doc/allList/" + route.query.ebookId).then((resp) => {
        loading.value = false;
        const data = resp.data;
        if (data.success) {
          console.log(data.content);
          console.log("原始数组:", data.content);

          level1.value = [];
          level1.value = Tool.array2Tree(data.content, 0);
          console.log("树形结构:", level1);


          treeSelectData.value = Tool.copy(level1.value) || [];
          treeSelectData.value.unshift({ id: 0, name: "无" });
        } else {
          message.error(data.message);
        }
        // docs.value = data.content;

        // //重置分页按钮
        // pagination.value.current = params.page;
        // pagination.value.total = data.content.total;
      });
    };
    /*
     * 表格点击页码时触发
     * */
    // const handleTableChange = (pagination: any) => {
    //   console.log("看看自带分页的参数都有些啥：" + pagination);
    //   handleQuery({
    //     page: pagination.current,
    //     size: pagination.pageSize
    //   });
    // };

    //----------------表单------------------
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref();
    doc.value = {
      ebook_id: route.query.ebookId
    }
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const editor = new E("#content");
    editor.config.zIndex = 0;

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/findContentById/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((resp) => {
        modalLoading.value = false;
        const data = resp.data;
        if (data.success) {
          message.success("保存成功！");
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };
    //编辑
    const edit = (record: any) => {
      //   doc.value = record;
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();

      //不能选择当前节点及其所以子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      //为选择树添加一个‘无’
      treeSelectData.value.unshift({ id: 0, name: "无" });

      // //由于富本文加载非常快捷，往往表单还没有加载完毕，富文本就加载好了，所以需要延时加载一会
      // setTimeout(() => {
      //     editor.create();
      // }, 100)
    };

    // ---富文本预览
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    //新增
    const add = () => {
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId,
      };

      treeSelectData.value = Tool.copy(level1.value) || [];

      //为选择树添加一个“无”
      treeSelectData.value.unshift({ id: 0, name: "无" });
      // //由于富本文加载非常快捷，往往表单还没有加载完毕，富文本就加载好了，所以需要延时加载一会
      // setTimeout(() => {
      //     editor.create();
      // }, 100)
    };

    /**
     * 查找整根树枝
     */
    const ids: Array<string> = [];
    const deleteNames: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标ID放入结果集ids
          // node.disabled = true;
          ids.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    //删除
    const handleDelete = (id: number) => {
      // 清空数组，否则多次删除时，数组会一直增加
      ids.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: "重要提醒",
        icon: createVNode(ExclamationCircleOutlined),
        content:
          "将删除：【" +
          deleteNames.join("，") +
          "】删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.get("/doc/remove?ids=" + ids.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize,
              });
            } else {
              message.error(data.message);
            }
          });
        },
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

    onMounted(() => {
      editor.create();
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });
    return {
      docs,
      pagination,
      columns,
      loading,
      treeSelectData,
      // handleTableChange,

      edit,
      add,

      level1,
      doc,
      modalVisible,
      modalLoading,
      handleSave,

      handleDelete,
      setDisable,

      ids,
      deleteNames,
      getDeleteIds,

      editor,
      handlePreviewContent,
      drawerVisible,
      onDrawerClose,
      previewHtml,
    };
  },
});
</script>
