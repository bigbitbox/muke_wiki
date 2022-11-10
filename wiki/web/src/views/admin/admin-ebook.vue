<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 20%;height: 20%"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">
                编辑
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>
</template>
<script lang="ts">
import { defineComponent ,onMounted,ref} from 'vue';
import axios from 'axios'



export default defineComponent({
  name: 'AdminEbook',
  // components: {
  //
  // },
  setup(){
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: { customRender: 'category' }
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /*
    * 数据查询
    * */
    const  handleQuery = (params:any)=>{
      loading.value = true;
      axios.get(process.env.VUE_APP_SERVER+"/getEbookByEbookReq",params).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        ebooks.value = data.content;

        //重置分页按钮
        pagination.value.current = params.page;
      });
    };
    /*
    * 表格点击页码时触发
    * */
    const handleTableChange =(pagination:any)=>{
      console.log("看看自带分页的参数都有些啥："+pagination);
      handleQuery({
        page:pagination.current,
        size:pagination.pageSize
      });
    };

    onMounted(()=>{
      handleQuery({});
    })
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    }
  }
});
</script>