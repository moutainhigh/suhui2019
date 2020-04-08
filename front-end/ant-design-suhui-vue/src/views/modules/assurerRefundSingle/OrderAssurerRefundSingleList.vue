<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="账户类型">
              <j-dict-select-tag v-model="queryParam.accountType" placeholder="请选择账户类型" dictCode="accountType"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag v-model="queryParam.refundSingleState" placeholder="请选择状态" dictCode="paySingleState"/>
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
      <a-button type="primary" @click="uploadVouch()">上传付款凭证</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('退款单')">导出</a-button>
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
          <!--<a @click="handleEdit(record)">编辑</a>-->
          <!--<a-divider type="vertical"/>-->
         <a @click="showDetail(record)">查看</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <OrderAssurerRefundSingleDetail ref="detailRefundSingleForm" @ok="modalFormOk"></OrderAssurerRefundSingleDetail>
    <UploadFileModal ref="uploadModal" :visible="uploadVisible" @ok="uploadFileBack" @close="uploadCancelBack"></UploadFileModal>

  </a-card>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { postAction } from '@/api/manage'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  import OrderAssurerRefundSingleDetail from './modules/OrderAssurerRefundSingleDetail'
  import UploadFileModal from './modules/UploadFileModal'

  export default {
    name: 'OrderAssurerRefundSingleList',
    mixins: [JeecgListMixin],
    components: {
      OrderAssurerRefundSingleDetail,
      UploadFileModal
    },
    data() {
      return {
        description: '退款单管理页面',
        uploadVisible: false,
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
            title: '状态',
            align: 'center',
            dataIndex: 'refundSingleState',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.paySingleStateDictOptions, text + '')
            }
          },
          {
            title: '账户类型',
            align: 'center',
            dataIndex: 'accountType',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.accountTypeDictOptions, text + '')
            }
          },
          {
            title: '账号',
            align: 'center',
            dataIndex: 'accountNo'
          },
          {
            title: '承兑商名称',
            align: 'center',
            dataIndex: 'assurerName'
          },
          {
            title: '退款金额',
            align: 'center',
            dataIndex: 'refundMoney'
          }, {
            title: '退款金额类型',
            align: 'center',
            dataIndex: 'moneyType'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/order/orderAssurerRefundSingle/list',
          delete: '/order/orderAssurerRefundSingle/delete',
          deleteBatch: '/order/orderAssurerRefundSingle/deleteBatch',
          exportXlsUrl: 'order/orderAssurerRefundSingle/exportXls',
          importExcelUrl: 'order/orderAssurerRefundSingle/importExcel',
          changeState: 'order/orderAssurerRefundSingle/changeState',
          uploadRefundVoucher: 'order/orderAssurerRefundSingle/uploadRefundVoucher'
        }
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    created() {
      this.initDictConfig()
    },
    methods: {
      uploadVouch() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        this.uploadVisible = true
      },
      uploadCancelBack() {
        this.uploadVisible = false
      },
      uploadFileBack(fileList) {
        this.uploadVisible = false
        this.loading = true
        let params = {
          id: this.selectedRowKeys[0],
          fileList: fileList.join(',')
        }
        postAction(this.url.uploadRefundVoucher, params).then(res => {
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
      },
      // openDispatch(record) {
      //   this.$refs.choiceAssurerForm.dispatchList(record);
      // },
      // openAdd(record) {
      //   this.handleAddRefundSingle(record);
      // },
      passState() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          ids: this.selectedRowKeys.join(','),
          state: 'pass'
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
      rejectState() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          ids: this.selectedRowKeys.join(','),
          state: 'reject'
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
      showDetail(record) {
        this.$refs.detailRefundSingleForm.detailShow(record)
      },
      initDictConfig() {
        initDictOptions('accountType').then((res) => {
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