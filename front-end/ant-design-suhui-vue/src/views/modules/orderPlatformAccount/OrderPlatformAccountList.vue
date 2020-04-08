<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="账号类型">
              <j-dict-select-tag v-model="queryParam.accountType" placeholder="请选择账号类型" dictCode="channel_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag v-model="queryParam.accountState" placeholder="请选择状态" dictCode="enableStop"/>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" @click="enableAccount()">启用</a-button>
      <a-button type="primary" @click="stopAccount()">停用</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('平台账户管理')">导出</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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
    <orderPlatformAccount-modal ref="modalForm" @ok="modalFormOk"></orderPlatformAccount-modal>
  </a-card>
</template>

<script>
  import OrderPlatformAccountModal from './modules/OrderPlatformAccountModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  import { postAction } from '@/api/manage'
  export default {
    name: 'OrderPlatformAccountList',
    mixins: [JeecgListMixin],
    components: {
      OrderPlatformAccountModal
    },
    data() {
      return {
        description: '平台账户管理管理页面',
        accountTypeDictOptions: [],
        accountStateDictOptions: [],
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
            dataIndex: 'accountState',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.accountStateDictOptions, text + '')
            }
          },
          {
            title: '账号类型',
            align: 'center',
            dataIndex: 'accountType',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.accountTypeDictOptions, text + '')
            }
          },
          {
            title: '区号',
            align: 'center',
            dataIndex: 'areaCode'
          },
          {
            title: '账户',
            align: 'center',
            dataIndex: 'accountNo'
          }, {
            title: '真实姓名',
            align: 'center',
            dataIndex: 'realName'
          }, {
            title: '余额',
            align: 'center',
            dataIndex: 'accountMoney'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/order/platformAccount/list',
          delete: '/order/platformAccount/delete',
          deleteBatch: '/order/platformAccount/deleteBatch',
          exportXlsUrl: 'order/platformAccount/exportXls',
          importExcelUrl: 'order/platformAccount/importExcel',
          changeState:'order/platformAccount/changeState'
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
      enableAccount(){
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          accountIds: this.selectedRowKeys.join(','),
          type:'enable'
        }
        this.$confirm({
          title: '启用账户',
          content: '确定要启用所选中的 ' + this.selectedRowKeys.length + ' 条数据吗?',
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
      stopAccount(){
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请至少选择一条数据'
          })
        }
        let params = {
          accountIds: this.selectedRowKeys.join(','),
          type:'stop'
        }
        this.$confirm({
          title: '停用账户',
          content: '确定要停用所选中的 ' + this.selectedRowKeys.length + ' 条数据吗?',
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
      initDictConfig() {
        initDictOptions('channel_type').then((res) => {
          if (res.success) {
            this.accountTypeDictOptions = res.result
          }
        })
        initDictOptions('enableStop').then((res) => {
          if (res.success) {
            this.accountStateDictOptions = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>