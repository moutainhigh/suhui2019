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
        <detail-list title="----------------------------订单信息----------------------------" :col="col">
          <detail-list-item term="订单编号">{{ model.orderCode }}</detail-list-item>
          <!--<detail-list-item term="状态">{{ model.orderState }}</detail-list-item>-->
          <detail-list-item term="源币种">{{ model.sourceCurrency }}</detail-list-item>
          <detail-list-item term="源币种金额">{{ model.sourceCurrencyMoney }}</detail-list-item>
          <detail-list-item term="汇率">{{ model.exchangeRate }}</detail-list-item>
          <detail-list-item term="目标币种">{{ model.targetCurrency }}</detail-list-item>
          <detail-list-item term="目标币种金额">{{ model.targetCurrencyMoney }}</detail-list-item>
          <detail-list-item term="自动分配状态">
            <span v-if="model.autoDispatchState === 0">
              成功
            </span>
            <span v-if="model.autoDispatchState === 1">
              失败
            </span>
          </detail-list-item>
          <detail-list-item v-if="model.autoDispatchState === 1" term="自动分配失败说明">{{ model.autoDispatchText }}
          </detail-list-item>
          <detail-list-item v-if="model.orderText" term="备注">{{ model.orderText }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------用户信息----------------------------" :col="col">
          <detail-list-item term="用户姓名">{{ model.userName }}</detail-list-item>
          <detail-list-item term="联系方式">{{ model.userContact }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------用户支付信息----------------------------" :col="col">
          <detail-list-item term="支付方式">
              <span v-if="model.userPayMethod ==='alipay'">
              支付宝
            </span>
            <span v-if="model.userPayMethod ==='bank_card'">
              银行卡
            </span>
          </detail-list-item>
          <detail-list-item term="账号户名">{{ model.userPayAccountUser }}</detail-list-item>
          <detail-list-item term="账号">{{ model.userPayAccount }}</detail-list-item>
          <detail-list-item term="金额">{{ model.sourceCurrencyMoney  }}  {{model.sourceCurrency}}</detail-list-item>
          <detail-list-item term="账户所属区号">{{ model.userPayAreaCode }}</detail-list-item>
          <detail-list-item term="账户开户行" v-if="model.userPayMethod !=='alipay'">{{ model.userPayBank }}</detail-list-item>
          <detail-list-item term="账户开户网点" v-if="model.userPayMethod !=='alipay'">{{ model.userPayBankBranch }}</detail-list-item>
          <detail-list-item term="确认支付时间">{{ model.userPayTime }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------用户支付凭证----------------------------" :col="col" v-if="model.userPayVoucher">
          <div class="img-model">
            <img v-for="(item,i) in model.userPayVoucher.split(',')" @click="handlePreview(item)" :src="item"/>
          </div>
        </detail-list>
        <detail-list title="----------------------------用户收款信息----------------------------" :col="col">
          <detail-list-item term="收款方式">
              <span v-if="model.userCollectionMethod ==='alipay'">
              支付宝
            </span>
            <span v-if="model.userCollectionMethod ==='bank_card'">
              银行卡
            </span>
          </detail-list-item>
          <detail-list-item term="收款账号">{{ model.userCollectionAccount }}</detail-list-item>
          <detail-list-item term="收款户名">{{ model.userCollectionAccountUser }}</detail-list-item>
          <detail-list-item term="收款金额">{{model.targetCurrencyMoney}}  {{ model.targetCurrency }} </detail-list-item>
          <detail-list-item term="账户所属区号">{{ model.userCollectionAreaCode }}</detail-list-item>
          <detail-list-item term="账户开户行" v-if="model.userCollectionMethod !=='alipay'">{{ model.userCollectionBank }}</detail-list-item>
          <detail-list-item term="账户开户网点" v-if="model.userCollectionMethod !=='alipay'">{{ model.userCollectionBankBranch }}</detail-list-item>
          <detail-list-item term="确认收款时间">{{ model.userCollectionTime }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------承兑商信息----------------------------" :col="col" v-if="model.assurerId">
          <detail-list-item term="承兑商名称">{{ model.assurerName }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------承兑商兑付信息----------------------------" :col="col" v-if="model.assurerId">
          <detail-list-item term="兑付方式">
             <span v-if="model.assurerPayMethod ==='alipay'">
              支付宝
            </span>
            <span v-if="model.assurerPayMethod ==='bank_card'">
              银行卡
            </span>
          </detail-list-item>
          <detail-list-item term="兑付账号">{{ model.assurerPayAccount }}</detail-list-item>
          <detail-list-item term="账号户名">{{ model.assurerPayAccountUser }}</detail-list-item>
          <detail-list-item term="金额">{{model.targetCurrencyMoney}}   {{ model.targetCurrency }}</detail-list-item>
          <detail-list-item term="账户开户行" v-if="model.assurerPayMethod !=='alipay'">{{ model.assurerPayBank }}</detail-list-item>
          <detail-list-item term="账户开户网点" v-if="model.assurerPayMethod !=='alipay'">{{ model.assurerPayBankBranch }}</detail-list-item>
          <detail-list-item term="确认兑付时间">{{ model.assurerPayTime }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------承兑商兑付凭证----------------------------" :col="col" v-if="model.assurerPayVoucher">
          <div class="img-model">
            <img v-for="(item,i) in model.assurerPayVoucher.split(',')" @click="handlePreview(item)" :src="item"/>
          </div>
        </detail-list>
        <detail-list title="----------------------------承兑商收款信息----------------------------" :col="col" v-if="model.assurerId">
          <detail-list-item term="收款方式">
             <span v-if="model.assurerCollectionMethod ==='alipay'">
              支付宝
            </span>
            <span v-if="model.assurerCollectionMethod ==='bank_card'">
              银行卡
            </span>
          </detail-list-item>
          <detail-list-item term="收款账号">{{ model.assurerCollectionAccount }}</detail-list-item>
          <detail-list-item term="账号户名">{{ model.assurerCollectionAccountUser }}</detail-list-item>
          <detail-list-item term="金额">{{ model.sourceCurrencyMoney  }}  {{model.sourceCurrency}}</detail-list-item>
          <detail-list-item term="账户开户行" v-if="model.assurerCollectionMethod !=='alipay'">{{ model.assurerCollectionBank }}</detail-list-item>
          <detail-list-item term="账户开户网点" v-if="model.assurerCollectionMethod !=='alipay'">{{ model.assurerCollectionBankBranch }}</detail-list-item>
          <detail-list-item term="确认收款时间">{{ model.assurerCollectionTime }}</detail-list-item>
        </detail-list>
        <a-modal :visible="previewVisible" :footer="null" @cancel="cancelPreview">
          <img alt="example" style="width: 100%" :src="previewImage" />
        </a-modal>
      </a-card>
    </a-spin>
  </a-drawer>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'

  const DetailListItem = DetailList.Item
  import pick from 'lodash.pick'
  import moment from 'moment'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'

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
        previewVisible: false,
        previewImage: '',
        model: {},
        // form: this.$form.createForm(this),
        confirmLoading: false
      }
    },
    methods: {
      cancelPreview () {
        this.previewVisible = false
      },
      handlePreview (url) {
        this.previewImage = url;
        this.previewVisible = true
      },
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
  .img-model{
    margin-bottom: 10px;
  }
  .img-model  img{
    display: inline-block;
    max-width: 150px;
    max-height: 150px;
    margin-right: 8px;
    cursor: pointer;
  }
</style>