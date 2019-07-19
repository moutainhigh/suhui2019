<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="rateId">
              <a-input placeholder="请输入rateId" v-model="queryParam.rateId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="换汇的产品名字">
              <a-input placeholder="请输入换汇的产品名字" v-model="queryParam.rateName"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="源货币">
              <a-input placeholder="请输入源货币" v-model="queryParam.sourceCurrency"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="目标货币">
              <a-input placeholder="请输入目标货币" v-model="queryParam.targetCurrency"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="当前汇率">
              <a-input placeholder="请输入当前汇率" v-model="queryParam.liveRate"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('换汇产品')">导出</a-button>
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
    <basicExchangeRate-modal ref="modalForm" @ok="modalFormOk"></basicExchangeRate-modal>
  </a-card>
</template>

<script>
  import BasicExchangeRateModal from './modules/BasicExchangeRateModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "BasicExchangeRateList",
    mixins:[JeecgListMixin],
    components: {
      BasicExchangeRateModal
    },
    data () {
      return {
        description: '换汇产品管理页面',
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
            title: 'rateId',
            align:"center",
            dataIndex: 'rateId'
           },
		   {
            title: '换汇的产品名字',
            align:"center",
            dataIndex: 'rateName'
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
            title: '当前汇率',
            align:"center",
            dataIndex: 'liveRate'
           },
		   {
            title: '换汇备注',
            align:"center",
            dataIndex: 'rateNote'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/org.suhui.modules.exchange/basicExchangeRate/list",
          delete: "/org.suhui.modules.exchange/basicExchangeRate/delete",
          deleteBatch: "/org.suhui.modules.exchange/basicExchangeRate/deleteBatch",
          exportXlsUrl: "org.suhui.modules.exchange/basicExchangeRate/exportXls",
          importExcelUrl: "org.suhui.modules.exchange/basicExchangeRate/importExcel",
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