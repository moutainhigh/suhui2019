<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-card :bordered="false">
        <detail-list title="订单信息" :col="col">
          <detail-list-item term="订单编号">{{ model.orderCode }}</detail-list-item>
          <detail-list-item term="状态">{{ model.orderState }}</detail-list-item>
          <detail-list-item term="源币种">{{ model.sourceCurrency }}</detail-list-item>
          <detail-list-item term="源币种金额">{{ model.sourceCurrencyMoney }}</detail-list-item>
          <detail-list-item term="汇率">{{ model.exchangeRate }}</detail-list-item>
          <detail-list-item term="目标币种">{{ model.targetCurrency }}</detail-list-item>
          <detail-list-item term="目标币种金额">{{ model.targetCurrencyMoney }}</detail-list-item>
          <detail-list-item term="自动分配状态">{{ model.autoDispatchState }}</detail-list-item>
          <detail-list-item v-if="model.autoDispatchState === 0" term="自动分配失败说明">{{ model.autoDispatchText }}
          </detail-list-item>
          <detail-list-item v-if="model.orderText" term="备注">{{ model.orderText }}</detail-list-item>
        </detail-list>
        <detail-list title="用户信息" :col="col">
          <detail-list-item term="用户姓名">{{ model.userName }}</detail-list-item>
          <detail-list-item term="联系方式">{{ model.userContact }}</detail-list-item>
          <detail-list-item term="支付方式">{{ model.userPayMethod }}</detail-list-item>
          <detail-list-item term="确认支付时间">{{ model.userPayTime }}</detail-list-item>
          <detail-list-item term="收款方式">{{ model.userCollectionMethod }}</detail-list-item>
          <detail-list-item term="收款账号">{{ model.userCollectionAccount }}</detail-list-item>
          <detail-list-item term="确认收款时间">{{ model.userCollectionTime }}</detail-list-item>
        </detail-list>
        <detail-list title="承兑商信息" :col="col" v-if="model.assurerId">
          <detail-list-item term="承兑商名称">{{ model.assurerName }}</detail-list-item>
          <detail-list-item term="收款方式">{{ model.assurerCollectionMethod }}</detail-list-item>
          <detail-list-item term="收款账号">{{ model.assurerCollectionAccount }}</detail-list-item>
          <detail-list-item term="确认收款时间">{{ model.assurerCollectionTime }}</detail-list-item>
          <detail-list-item term="兑付方式">{{ model.assurerPayMethod }}</detail-list-item>
          <detail-list-item term="兑付账号">{{ model.assurerPayAccount }}</detail-list-item>
          <detail-list-item term="确认兑付时间">{{ model.assurerPayTime }}</detail-list-item>
        </detail-list>
      </a-card>
    </a-spin>
  </a-drawer>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'

  const DetailListItem = DetailList.Item
  import pick from 'lodash.pick'
  import moment from 'moment'

  export default {
    components: {
      DetailList,
      DetailListItem
    },
    data() {
      return {
        title: '查看详情',
        col: 1,
        visible: false,
        drawerWidth: 700,
        model: {},
        // form: this.$form.createForm(this),
        confirmLoading: false
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
      edit(record) {
        // this.form.resetFields();
        this.resetScreenSize()
        this.model = Object.assign({}, record)
        console.log(this.model)
        this.visible = true
        // this.$nextTick(() => {
        //   this.form.setFieldsValue(pick(this.model,'orderCode','orderState','userNo','userName','userContact','sourceCurrency','targetCurrency','sourceCurrencyMoney','targetCurrencyMoney','exchangeRate','userPayMethod','userCollectionMethod','userCollectionAccount','assurerId','assurerName','assurerCollectionMethod','assurerCollectionAccount','assurerPayMethod','assurerPayAccount','orderText','delFlag','autoDispatchState','autoDispatchText'))
        //   //时间格式化
        //   this.form.setFieldsValue({userPayTime:this.model.userPayTime?moment(this.model.userPayTime):null})
        //   this.form.setFieldsValue({userCollectionTime:this.model.userCollectionTime?moment(this.model.userCollectionTime):null})
        //   this.form.setFieldsValue({assurerCollectionTime:this.model.assurerCollectionTime?moment(this.model.assurerCollectionTime):null})
        //   this.form.setFieldsValue({assurerPayTime:this.model.assurerPayTime?moment(this.model.assurerPayTime):null})
        //   console.log(this.form);
        //   console.log(this.model);
        // });
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

<style lang="scss" scoped>
  .title {
    color: rgba(0, 0, 0, .85);
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 16px;
  }
</style>