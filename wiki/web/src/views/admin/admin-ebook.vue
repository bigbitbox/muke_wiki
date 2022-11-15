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
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input
              v-model:value="param.name"
              placeholder="名称"
              :style="{
                borderRadius: '15px',
              }"
            >
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button
              type="primary"
              @click="handleQuery({ page: 1, size: pagination.pageSize })"
              :style="{
                background: 'green',
                borderColor: 'green',
                borderRadius: '15px',
              }"
            >
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button
              type="primary"
              @click="add()"
              :style="{
                borderRadius: '15px',
              }"
            >
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table
        :columns="columns"
        :row-key="(record:any) => record.id"
        :data-source="ebooks"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img
            v-if="cover"
            :src="cover"
            alt="avatar"
            style="width: 20%; height: 20%"
          />
        </template>

        <template v-slot:category="{ text, record }">
          <span>
            {{ getCategoryName(record.category1_id) }}
            /
            {{ getCategoryName(record.category2_id) }}
          </span>
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link to="/admin/doc">
              <a-button type = "primary">
                文档管理
              </a-button>
            </router-link>
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
    title="电子书表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
          v-model:value="categoryIds"
          :field-names="{ label: 'name', value: 'id', children: 'children' }"
          :options="level1"
        />
      </a-form-item>
      <!-- <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1_id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2_id" />
      </a-form-item> -->
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
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
import router from "@/router";

export default defineComponent({
    name: "AdminEbook",
    // components: {
    //
    // },
    setup() {
        const ebooks = ref();
        const pagination = ref({
            current: 1,
            pageSize: 4,
            total: 0,
        });
        const loading = ref(false);
        const columns = [
            {
                title: "封面",
                dataIndex: "cover",
                slots: { customRender: "cover" },
            },
            {
                title: "名称",
                dataIndex: "name",
            },
            {
                title: "分类",
                slots: { customRender: "category" },
            },
            {
                title: "文档数",
                dataIndex: "doc_count",
            },
            {
                title: "阅读数",
                dataIndex: "view_count",
            },
            {
                title: "点赞数",
                dataIndex: "vote_count",
            },
            {
                title: "Action",
                key: "action",
                slots: { customRender: "action" },
            },
        ];
        /**
         * 搜索相关
         * @param params
         */
        const param = ref();
        param.value = {};
        /*
         * 数据查询
         * */
        const handleQuery = (params: any) => {
            loading.value = true;
            axios
                .get("/ebook/getEbookListByPage", {
                params: {
                    page: params.page,
                    size: params.size,
                    // size: 10000,
                    name: param.value.name,
                },
            })
                .then((resp) => {
                loading.value = false;
                const data = resp.data;
                if (data.success) {
                    ebooks.value = data.content.list;
                    //重置分页按钮
                    pagination.value.current = params.page;
                    pagination.value.total = data.content.total;
                }
                else {
                    message.error(data.message);
                }
            });
        };
        /*
         * 表格点击页码时触发
         * */
        const handleTableChange = (pagination: any) => {
            console.log("看看自带分页的参数都有些啥：" + pagination);
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize,
            });
        };
        // -------- 表单 ---------
        const ebook = ref();
        const categoryIds = ref();
        const modalVisible = ref(false);
        const modalLoading = ref(false);
        // 删除方法
        const handleDelete = (id: number) => {
            axios.get("/ebook/remove", { params: { id: id } }).then((resp) => {
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
            ebook.value.category1_id = categoryIds.value[0];
            ebook.value.category2_id = categoryIds.value[1];
            console.log(ebook.value);
            axios.post("/ebook/save", ebook.value).then((resp) => {
                const data = resp.data;
                if (data.success) {
                    modalVisible.value = false;
                    // modalLoading.value = false;
                    //重新加载列表
                    handleQuery({
                        page: pagination.value.current,
                        size: pagination.value.pageSize,
                    });
                }
                else {
                    message.error(data.message);
                }
            });
        };
        //  分类相关 ————————————————
        const level1 = ref();
        let categorys: any;
        // 查询所有分类
        const handleQueryCategory = () => {
            loading.value = true;
            ebooks.value = [];
            axios.get("/category/allList").then((resp) => {
                loading.value = false;
                const data = resp.data;
                categorys = data.content;
                if (data.success) {
                    console.log(data.content);
                    console.log("origin array", data.content);
                    level1.value = [];
                    level1.value = Tool.array2Tree(data.content, 0);
                    console.log("树形结构", level1);
                    handleQuery({
                        page: 1,
                        size: pagination.value.pageSize,
                    });
                }
                else {
                    message.error(data.message);
                }
                // categorys.value = data.content;
            });
        };
        // ————————获取分类名字————————
        const getCategoryName = (cid: number) => {
            let result = "";
            categorys.forEach((item: any) => {
                if (item.id === cid) {
                    result = item.name;
                }
            });
            return result;
        };
        //编辑
        const edit = (record: any) => {
            ebook.value = Tool.copy(record);
            modalVisible.value = true;
            categoryIds.value = [ebook.value.category1_id, ebook.value.category2_id];
        };
        //新增
        const add = () => {
            ebook.value = {};
            modalVisible.value = true;
        };
        onMounted(() => {
            handleQueryCategory();
            console.log("this is level1", level1);
        });
        return {
            ebooks,
            pagination,
            columns,
            loading,
            handleTableChange,
            add,
            edit,
            modalLoading,
            modalVisible,
            handleModalOk,
            handleDelete,
            ebook,
            categoryIds,
            level1,
            getCategoryName,
            categorys,
            handleQuery,
            param,
        };
    },
    components: { router }
});
</script>
