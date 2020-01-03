<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    :confirmLoading="confirmLoading"
    @close="handleCancel"
    :visible="visible"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a v-has="'order:admin'" @click="chooseAssurer(record)">选择</a>
        </span>
      </a-table>
    </a-spin>
  </a-drawer>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { postAction } from '@/api/manage'

  export default {
    mixins: [JeecgListMixin],
    data() {
      return {
        title: '分配承兑商',
        col: 1,
        visible: false,
        drawerWidth: 700,
        model: {},
        confirmLoading: false,
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
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/order/orderAssurer/list',
          dispatchAssurer: '/order/orderMain/dispatchOrder'
        }
      }
    },
    methods: {
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleCancel() {
        this.close()
      },
      handleOk() {
        this.$emit('ok')
      },
      dispatchList(record) {
        this.queryParam.canUseLimit_begin = record.targetCurrencyMoney
        this.queryParam.assurerState = 'normal'
        this.queryParam.onlineState = 1
        this.queryParam.delFlag = 0
        this.isorter = {
          column: 'assurerRate',
          order: 'desc'
        }
        this.searchQuery()
        this.resetScreenSize()
        this.model = Object.assign({}, record)
        this.visible = true
      },
      chooseAssurer(record) {
        let params = {
          orderId: this.model.id,
          assurerId: record.id
        }
        this.$confirm({
          title: '选择承兑商',
          content: '确定要选择承兑商【'+record.assurerName+'】吗？',
          onOk: () => {
            this.loading = true
            postAction(this.url.dispatchAssurer, params).then((res) => {
              this.loading = false
              if (res.code !== 200) {
                this.$warning({
                  title: res.message
                })
              }else{
                this.$success({
                  title: res.message
                })
                this.handleOk()
                this.visible = false
              }
            })
          }
        })
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize() {
        let screenWidth = document.body.clientWidth
        if (screenWidth < 500) {
          this.drawerWidth = screenWidth
        } else {
          this.drawerWidth = 700
        }
      }
    }
  }
</script>

<style scoped>

</style>