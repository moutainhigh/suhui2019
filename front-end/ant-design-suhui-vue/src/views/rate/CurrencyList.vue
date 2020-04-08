<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="货币代码">
              <a-select v-model="queryParam.source_currency_code" placeholder="请选择货币代码查询">
                <a-select-option value="">请选择货币代码</a-select-option>
                <a-select-option v-for="currency in currencyList" :value="currency.currencyCode" :key="currency.id">
                  {{ currency.currencyCode }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="货币名称">
              <a-select v-model="queryParam.target_currency_code" placeholder="请选择货币名称查询">
                <a-select-option value="">请选择货币名称</a-select-option>
                <a-select-option v-for="currency in currencyList" :value="currency.currencyName" :key="currency.id">
                  {{ currency.currencyName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <a-select v-model="queryParam.status" placeholder="请选择货币状态查询">
                <a-select-option value="">请选择货币状态</a-select-option>
                <a-select-option value="0">已停用</a-select-option>
                <a-select-option value="1">已启用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" v-has="'user:add'" type="primary" icon="plus">添加货币</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('用户信息')">导出</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel" />
            删除
          </a-menu-item>
          <a-menu-item key="2">
            <a-icon type="lock" @click="batchFrozen('1')" />
            停用
          </a-menu-item>
          <a-menu-item key="3">
            <a-icon type="unlock" @click="batchFrozen('0')" />
            启用
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择&nbsp;
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>
        项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-if="record.status === 1">
                <a-popconfirm title="确定停用吗?" @confirm="() => handleFrozen(record.id, 0)">
                  <a>停用</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-if="record.status === 0">
                <a-popconfirm title="确定启用吗?" @confirm="() => handleFrozen(record.id, 1)">
                  <a>启用</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 新增/编辑弹出窗口区域 -->
    <currency-modal ref="modalForm" @ok="modalFormOk"></currency-modal>
  </a-card>
</template>

<script>
import CurrencyModal from './modules/CurrencyModal';
import { putAction } from '@/api/manage';
import { frozenBatch } from '@/api/api';
import { ListMixin } from '@/mixins/ListMixin';

export default {
  name: "CurrencyList",
  mixins: [ListMixin],
  components: {
    CurrencyModal
  },

  data() {
    return {
      description: '这是货币管理页面',
      queryParam: {},
      columns: [
        {
          title: '货币名称',
          align: "center",
          width: 100,
          dataIndex: 'currencyName'
        },
        {
          title: '货币代码',
          align: "center",
          width: 60,
          dataIndex: 'currencyCode'
        },
        {
          title: '货币符号',
          align: "center",
          width: 60,
          dataIndex: 'currencySymbol'
        },
        {
          title: '货币单位',
          align: "center",
          width: 60,
          dataIndex: 'currencyUnit'
        },
        {
          title: '状态',
          align: "center",
          width: 60,
          dataIndex: 'statusName'
        },
        {
          title: '备注',
          align: 'center',
          width: 120,
          dataIndex: 'remark'
        },
        {
          title: '操作',
          align: "center",
          width: 100,
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        imgerver: window._CONFIG['domianURL'] + "/sys/common/view",
        // syncUser: "/process/extActProcess/doSyncUser",
        list: "/api/login/payCurrencyType/getCurrencyTypeList",
        delete: '/api', // 删除货币API
        deleteBatch: '/api', // 批量删除货币API
        exportXlsUrl: '/api', //导出货币列表API
        // importExcelUrl: "sys/user/importExcel"
      },
    };
  },

  computed: {
    // importExcelUrl: function () {
    //   return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    // }
  },

  methods: {
    batchFrozen: function (status) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return false;
      } else {
        let ids = '';
        let that = this;
        that.selectedRowKeys.forEach(function (val) {
          ids += val + ",";
        });
        that.$confirm({
          title: '确认操作',
          content: '是否' + (status == 0 ? '启用' : '停用') + '选中货币？',
          onOk: function () {
            frozenBatch({ ids: ids, status: status }).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            });
          }
        });
      }
    },

    handleMenuClick(e) {
      if (e.key == 1) {
        this.batchDel();
      } else if (e.key == 2) {
        this.batchFrozen(2);
      } else if (e.key == 3) {
        this.batchFrozen(1);
      }
    },

    handleFrozen(id, status) {
      const that = this;
      frozenBatch({ ids: id, status: status }).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>