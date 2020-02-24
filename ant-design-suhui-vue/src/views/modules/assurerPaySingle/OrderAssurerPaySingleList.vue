<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="账户类型">
              <j-dict-select-tag v-model="queryParam.platformAccountType" placeholder="请选择账户类型" dictCode="channel_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag v-model="queryParam.paySingleState" placeholder="请选择状态" dictCode="paySingleState"/>

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
      <a-button type="primary" @click="passState()">审核通过</a-button>
      <a-button type="primary" @click="rejectState()">审核拒绝</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('缴费单')">导出</a-button>
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">-->
        <!--<a-menu slot="overlay">-->
          <!--<a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>-->
        <!--</a-menu>-->
        <!--<a-button style="margin-left: 8px">-->
          <!--批量操作 <a-icon type="down" />-->
        <!--</a-button>-->
      <!--</a-dropdown>-->
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
  import { postAction } from '@/api/manage'
  export default {
    name: "OrderAssurerPaySingleList",
    mixins: [JeecgListMixin],
    components: {
      OrderAssurerPaySingleDetail,
    },
    data() {
      return {
        description: '缴费单管理页面',
        accountTypeDictOptions:[],
        paySingleStateDictOptions:[],
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
            title: '状态',
            align: "center",
            dataIndex: 'paySingleState',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.paySingleStateDictOptions, text + '')
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
          },{
            title: '缴费金额类型',
            align: "center",
            dataIndex: 'moneyType'
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
          changeState: "order/PaySingle/changeState",
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
      passState(){
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          ids: this.selectedRowKeys.join(','),
          state:'pass'
        }
        this.$confirm({
          title: '审核通过',
          content: '确定要对所选中的 ' + this.selectedRowKeys.length + ' 条数据审核通过吗?',
          onOk: () => {
            this.loading = true
            postAction(this.url.changeState, params).then(res => {
              if (res.code !== 200) {
                this.loading = false
                this.$warning({
                  title: res.message
                })
              } else {
                this.$success({
                  title: res.message
                })
                this.searchQuery()
                this.onClearSelected()
              }
            })
          }
        })
      },
      rejectState(){
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          ids: this.selectedRowKeys.join(','),
          state:'reject'
        }
        this.$confirm({
          title: '审核拒绝',
          content: '确定要对所选中的 ' + this.selectedRowKeys.length + ' 条数据审核拒绝吗?',
          onOk: () => {
            this.loading = true
            postAction(this.url.changeState, params).then(res => {
              if (res.code !== 200) {
                this.loading = false
                this.$warning({
                  title: res.message
                })
              } else {
                this.$success({
                  title: res.message
                })
                this.searchQuery()
                this.onClearSelected()
              }
            })
          }
        })
      },
      showDetail(record){
        this.$refs.detailForm.edit(record)
      },
      initDictConfig() {
        initDictOptions('channel_type').then((res) => {
          if (res.success) {
            this.accountTypeDictOptions = res.result
          }
        })
        initDictOptions('paySingleState').then((res) => {
          if (res.success) {
            this.paySingleStateDictOptions = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>