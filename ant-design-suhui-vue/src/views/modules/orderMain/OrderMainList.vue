<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="订单编号">
              <a-input placeholder="请输入订单编号" v-model="queryParam.orderCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="订单状态">
              <a-input placeholder="请输入订单状态" v-model="queryParam.orderState"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="用户姓名">
                <a-input placeholder="请输入用户姓名" v-model="queryParam.userName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="用户联系方式">
                <a-input placeholder="请输入用户联系方式" v-model="queryParam.userContact"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <a-button type="primary" icon="download" @click="handleExportXls('订单管理')">导出</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
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
          <a @click="handleDetail(record)">查看详情</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <orderMain-modal ref="modalForm" @ok="modalFormOk"></orderMain-modal>
  </a-card>
</template>

<script>
  import OrderMainModal from './modules/OrderMainModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'OrderMainList',
    mixins: [JeecgListMixin],
    components: {
      OrderMainModal
    },
    data() {
      return {
        description: '订单管理管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '订单编号',
            align: 'center',
            dataIndex: 'orderCode'
          },
          {
            title: '订单状态',
            align: 'center',
            dataIndex: 'orderState'
          },
          {
            title: '用户姓名',
            align: 'center',
            dataIndex: 'userName'
          },
          {
            title: '联系方式',
            align: 'center',
            dataIndex: 'userContact'
          },
          {
            title: '源币种',
            align: 'center',
            dataIndex: 'sourceCurrency'
          },
          {
            title: '源币种金额',
            align: 'center',
            dataIndex: 'sourceCurrencyMoney'
          },
          {
            title: '目标币种',
            align: 'center',
            dataIndex: 'targetCurrency'
          },
          {
            title: '汇率',
            align: 'center',
            dataIndex: 'exchangeRate'
          },
          {
            title: '承兑商名称',
            align: 'center',
            dataIndex: 'assurerName'
          },
          {
            title: '自动分配状态',
            align: 'center',
            dataIndex: 'autoDispatchState'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/order/orderMain/list',
          delete: '/order/orderMain/delete',
          deleteBatch: '/order/orderMain/deleteBatch',
          exportXlsUrl: 'order/orderMain/exportXls',
          importExcelUrl: 'order/orderMain/importExcel'
        }
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {}
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>