<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="账户类型">
              <a-input placeholder="请输入账户类型" v-model="queryParam.platformAccountType"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">


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
      <a-button type="primary" icon="download" @click="handleExportXls('缴费单')">导出</a-button>
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
          <a @click="showDetail(record)">查看</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <OrderAssurerPaySingleDetail ref="detailForm"></OrderAssurerPaySingleDetail>
  </a-card>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  import OrderAssurerPaySingleDetail from "./modules/OrderAssurerPaySingleDetail";

  export default {
    name: "OrderAssurerPaySingleList",
    mixins: [JeecgListMixin],
    components: {
      OrderAssurerPaySingleDetail
    },
    data() {
      return {
        description: '缴费单管理页面',
        accountTypeDictOptions:[],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '账户类型',
            align: "center",
            dataIndex: 'platformAccountType',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.accountTypeDictOptions, text + '')
            }
          },
          {
            title: '账户',
            align: "center",
            dataIndex: 'platformAccountNo'
          },
          {
            title: '缴费金额',
            align: "center",
            dataIndex: 'payMoney'
          },
          {
            title: '款项说明',
            align: "center",
            dataIndex: 'payText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }
        ],
        url: {
          list: "/order/PaySingle/list",
          delete: "/order/PaySingle/delete",
          deleteBatch: "/order/PaySingle/deleteBatch",
          exportXlsUrl: "order/PaySingle/exportXls",
          importExcelUrl: "order/PaySingle/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    created() {
      this.initDictConfig()
    },
    methods: {
      showDetail(record){
        this.$refs.detailForm.edit(record)
      },
      initDictConfig() {
        initDictOptions('channel_type').then((res) => {
          if (res.success) {
            this.accountTypeDictOptions = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>