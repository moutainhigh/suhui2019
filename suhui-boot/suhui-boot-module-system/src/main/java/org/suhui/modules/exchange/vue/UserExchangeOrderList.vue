<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="换汇数量">
              <a-input placeholder="请输入换汇数量" v-model="queryParam.amount"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="源货币">
              <a-input placeholder="请输入源货币" v-model="queryParam.sourceCurrency"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="目标货币">
              <a-input placeholder="请输入目标货币" v-model="queryParam.targetCurrency"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="预约处理时间">
              <a-input placeholder="请输入预约处理时间" v-model="queryParam.orderTime"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成
">
              <a-input placeholder="请输入预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成
" v-model="queryParam.orderState"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('用户换汇预约记录表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <userExchangeOrder-modal ref="modalForm" @ok="modalFormOk"></userExchangeOrder-modal>
  </a-card>
</template>

<script>
  import UserExchangeOrderModal from './modules/UserExchangeOrderModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "UserExchangeOrderList",
    mixins:[JeecgListMixin],
    components: {
      UserExchangeOrderModal
    },
    data () {
      return {
        description: '用户换汇预约记录表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '换汇数量',
            align:"center",
            dataIndex: 'amount'
           },
		   {
            title: '源货币',
            align:"center",
            dataIndex: 'sourceCurrency'
           },
		   {
            title: '目标货币',
            align:"center",
            dataIndex: 'targetCurrency'
           },
		   {
            title: '预约处理时间',
            align:"center",
            dataIndex: 'orderTime'
           },
		   {
            title: '预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成
',
            align:"center",
            dataIndex: 'orderState'
           },
		   {
            title: '处理人id',
            align:"center",
            dataIndex: 'handleId'
           },
		   {
            title: '换汇地址',
            align:"center",
            dataIndex: 'address'
           },
		   {
            title: '第二联系电话',
            align:"center",
            dataIndex: 'secondPhone'
           },
		   {
            title: '处理预约换汇管理员id',
            align:"center",
            dataIndex: 'supervisorId'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/org.suhui.modules.exchange/userExchangeOrder/list",
          delete: "/org.suhui.modules.exchange/userExchangeOrder/delete",
          deleteBatch: "/org.suhui.modules.exchange/userExchangeOrder/deleteBatch",
          exportXlsUrl: "org.suhui.modules.exchange/userExchangeOrder/exportXls",
          importExcelUrl: "org.suhui.modules.exchange/userExchangeOrder/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>