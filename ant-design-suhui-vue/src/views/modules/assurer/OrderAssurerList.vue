<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="承兑商状态">
              <j-dict-select-tag v-model="queryParam.assurerState" placeholder="请选择承兑商状态" dictCode="assurer_state"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="承兑商名称">
              <a-input placeholder="请输入承兑商名称" v-model="queryParam.assurerName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="在线状态">
                <j-dict-select-tag v-model="queryParam.onlineState" placeholder="请选择在线状态" dictCode="online_state"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :span="8">
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
      <a-button @click="handleAdd()" type="primary" icon="plus">添加</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('承兑商管理')">导出</a-button>
      <a-button type="primary" v-has="'audit:pass'" @click="auditPass()">审核通过</a-button>
      <a-button type="primary" @click="changeAssurerMoney('add','ensure')">增加保证金</a-button>
      <a-button type="primary" @click="changeAssurerMoney('sub','ensure')">减少保证金</a-button>
      <a-button type="primary" @click="changeAssurerMoney('add','lease')">增加租赁金</a-button>
      <a-button type="primary" @click="changeAssurerMoney('sub','lease')">减少租赁金</a-button>
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
    <a-modal :visible="modalVisable" @cancel="cancelModal" @ok="handelOkModal">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="金额">
              <a-input-number placeholder="请输入金额" style="width: 100%" v-decorator="[ 'changeMoney', validatorRules.changeMoney ]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="说明">
              <a-textarea placeholder="请输入说明" v-decorator="[ 'changeText', validatorRules.changeText ]"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script>
  const VALIDATE_NO_PASSED = Symbol()
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OrderAssurerModal from './modules/OrderAssurerModal'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  import { postAction } from '@/api/manage'
  import ATextarea from 'ant-design-vue/es/input/TextArea'

  export default {
    name: 'OrderAssurerList',
    mixins: [JeecgListMixin],
    components: {
      ATextarea,
      OrderAssurerModal
    },
    data() {
      return {
        description: '',
        form: this.$form.createForm(this),
        onlineStateDictOptions: [],
        assurerStateDictOptions: [],
        modalVisable: false,
        classChange:'',
        typeChange:'',
        validatorRules: {
          changeMoney: { rules: [{ required: true, message: '请输入金额!' }] },
          changeText: { rules: [{ required: true, message: '请输入说明!' }] }
        },
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
            title: '在线状态',
            align: 'center',
            dataIndex: 'onlineState',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.onlineStateDictOptions, text + '')
            }
          },
          {
            title: '承兑商状态',
            align: 'center',
            dataIndex: 'assurerState',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.assurerStateDictOptions, text)
            }
          },
          {
            title: '承兑商名称',
            align: 'center',
            dataIndex: 'assurerName'
          },
          {
            title: '国家码',
            align: 'center',
            dataIndex: 'countryCode'
          },
          {
            title: '费率',
            align: 'center',
            dataIndex: 'assurerRate'
          },
          {
            title: '可用额度',
            align: 'center',
            dataIndex: 'canUseLimit'
          },
          {
            title: '已用额度',
            align: 'center',
            dataIndex: 'usedLimit'
          },
          {
            title: '总额度',
            align: 'center',
            dataIndex: 'totalLimit'
          },
          {
            title: '支付锁定金额',
            align: 'center',
            dataIndex: 'payLockMoney'
          },{
            title: '保证金比例',
            align: 'center',
            dataIndex: 'ensureProportion'
          },{
            title: '保证金',
            align: 'center',
            dataIndex: 'ensureMoney'
          },{
            title: '租赁金',
            align: 'center',
            dataIndex: 'leaseMoney'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        // 请求参数
        url: {
          list: '/order/orderAssurer/list',
          delete: '/order/orderAssurer/delete',
          deleteBatch: '/order/orderAssurer/deleteBatch',
          exportXlsUrl: 'order/orderAssurer/exportXls',
          importExcelUrl: 'order/orderAssurer/importExcel',
          auditPass: 'order/orderAssurer/auditPass',
          changeAssurerMoney: 'order/orderAssurer/changeAssurerMoney'
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
      auditPass() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请选择一条数据'
          })
        }
        let params = {
          assurerIds: this.selectedRowKeys.join(',')
        }
        this.$confirm({
          title: '确认已收款',
          content: '确定要对所选中的 ' + this.selectedRowKeys.length + ' 条数据执行审核通过操作吗?',
          onOk: () => {
            this.loading = true
            postAction(this.url.auditPass, params).then(res => {
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
      changeAssurerMoney(pType, pClass) {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          return this.$warning({
            title: '请选择一条数据'
          })
        }
        this.classChange = pClass;
        this.typeChange = pType;
        this.modalVisable = true
      },
      cancelModal() {
        this.classChange = '';
        this.typeChange = '';
        this.form.resetFields()
        this.modalVisable = false
      },
      handelOkModal() {
        this.form.validateFields((err, values) => {
          if(!err){
            let params = {
              assurerId: this.selectedRowKeys[0],
              classChange:this.classChange,
              typeChange:this.typeChange,
              changeMoney:values.changeMoney,
              changeText:values.changeText,
            }
            this.$confirm({
              title: '提示',
              content: '确定要执行该操作吗?',
              onOk: () => {
                this.loading = true
                this.cancelModal();
                postAction(this.url.changeAssurerMoney, params).then(res => {
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
          }
        })
      },
      initDictConfig() {
        initDictOptions('online_state').then((res) => {
          if (res.success) {
            this.onlineStateDictOptions = res.result
          }
        })
        initDictOptions('assurer_state').then((res) => {
          if (res.success) {
            this.assurerStateDictOptions = res.result
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>