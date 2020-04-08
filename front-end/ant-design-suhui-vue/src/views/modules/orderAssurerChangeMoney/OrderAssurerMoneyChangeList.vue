<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="承兑商名称">
              <a-input placeholder="请输入承兑商名称" v-model="queryParam.assurerName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="变动种类">
              <j-dict-select-tag v-model="queryParam.changeClass" placeholder="请选择变动种类"
                                 dictCode="moneyType"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="执行状态">
              <j-dict-select-tag  v-model="queryParam.flag" placeholder="请选择执行状态"
                                 dictCode="flagState"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">

            <a-col :md="6" :sm="8">
              <a-form-item label="类型">
                <j-dict-select-tag  v-model="queryParam.changeType" placeholder="请选择类型"
                                    dictCode="changeType"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('承兑商指令列表')">导出</a-button>
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
          <a @click="handleEdit(record)">查看</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <OrderAssurerMoneyChangeModal ref="modalForm" @ok="modalFormOk"></OrderAssurerMoneyChangeModal>
  </a-card>
</template>

<script>
  import OrderAssurerMoneyChangeModal from './modules/OrderAssurerMoneyChangeModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  export default {
    name: 'OrderAssurerMoneyChangeList',
    mixins: [JeecgListMixin],
    components: {
      OrderAssurerMoneyChangeModal
    },
    data() {
      return {
        description: '承兑商指令列表',
        changeTypeOptions:[
          {
            value: "sub",
            title: "减少",
            text: "减少"
          },
          {
            value: "add",
            title: "增加",
            text: "增加"
          }
        ],
        changeClassOptions:[],
        flagStateOptions:[],
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
            title: '承兑商名称',
            align: 'center',
            dataIndex: 'assurerName'
          },
          {
            title: '变动金额',
            align: 'center',
            dataIndex: 'changeMoney'
          },
          {
            title: '类型',
            align: 'center',
            dataIndex: 'changeType',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.changeTypeOptions, text + '')
            }
          },
          {
            title: '变动种类',
            align: 'center',
            dataIndex: 'changeClass'
          },
          {
            title: '处理状态',
            align: 'center',
            dataIndex: 'flag',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.flagStateOptions, text + '')
            }
          },
          {
            title: '订单编号',
            align: 'center',
            dataIndex: 'orderNo'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/order/assurerMoneyChange/list',
          delete: '/order/assurerMoneyChange/delete',
          deleteBatch: '/order/assurerMoneyChange/deleteBatch',
          exportXlsUrl: 'order/assurerMoneyChange/exportXls',
          importExcelUrl: 'order/assurerMoneyChange/importExcel'
        }
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    created(){
      this.initDictConfig()
    },
    methods: {
      initDictConfig() {
        initDictOptions('flagState').then((res) => {
          if (res.success) {
            this.flagStateOptions = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>