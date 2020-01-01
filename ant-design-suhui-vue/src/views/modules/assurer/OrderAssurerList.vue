<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="用户编号">
              <a-input placeholder="请输入用户编号" v-model="queryParam.userNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="承兑商名称">
              <a-input placeholder="请输入承兑商名称" v-model="queryParam.assurerName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('去')">导出</a-button>
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">-->
        <!--<a-menu slot="overlay">-->
          <!--<a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
        <!--</a-menu>-->
        <!--<a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--</a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        <span>已选择</span>
        <a style="font-weight: 600">
          {{ selectedRowKeys.length }}
        </a>
        <span>项</span>
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
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <orderAssurer-modal ref="modalForm" @ok="modalFormOk"/>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OrderAssurerModal from './modules/OrderAssurerModal'

  export default {
    name: "OrderAssurerList",
    mixins: [JeecgListMixin],
    components: {
      OrderAssurerModal
    },
    data () {
      return {
        description: '去管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender:function (t, r, index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '在线状态',
            align:"center",
            dataIndex: 'onlineState'
          },
          {
            title: '业务状态',
            align:"center",
            dataIndex: 'assurerState'
          },
          {
            title: '承兑商名称',
            align:"center",
            dataIndex: 'assurerName'
          },
          {
            title: '国家码',
            align:"center",
            dataIndex: 'countryCode'
          },
          {
            title: '策略',
            align:"center",
            dataIndex: 'assurerStrategy'
          },
          {
            title: '费率',
            align:"center",
            dataIndex: 'assurerRate'
          },
          {
            title: '可用额度',
            align:"center",
            dataIndex: 'canUseLimit'
          },
          {
            title: '已用额度',
            align:"center",
            dataIndex: 'usedLimit'
          },
          {
            title: '总额度',
            align:"center",
            dataIndex: 'totalLimit'
          },
          {
            title: '支付锁定金额',
            align:"center",
            dataIndex: 'payLockMoney'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        // 请求参数
    	url: {
              list: "/order/orderAssurer/list",
              delete: "/order/orderAssurer/delete",
              deleteBatch: "/order/orderAssurer/deleteBatch",
              exportXlsUrl: "order/orderAssurer/exportXls",
              importExcelUrl: "order/orderAssurer/importExcel",
           },
        }
      },
      computed: {
        importExcelUrl: function(){
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
      },


    methods: {

      initDictConfig() {
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>