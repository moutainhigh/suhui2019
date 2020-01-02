<template>
  <a-drawer
      :title="title"
      :width="800"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单编号">
          <a-input placeholder="请输入订单编号" v-decorator="['orderCode', validatorRules.orderCode ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户收款5、已完成6、已作废0)">
          <a-input placeholder="请输入订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户收款5、已完成6、已作废0)" v-decorator="['orderState', validatorRules.orderState ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="userNo">
          <a-input placeholder="请输入userNo" v-decorator="['userNo', validatorRules.userNo ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户姓名">
          <a-input placeholder="请输入用户姓名" v-decorator="['userName', validatorRules.userName ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户联系方式">
          <a-input placeholder="请输入用户联系方式" v-decorator="['userContact', validatorRules.userContact ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="源币种">
          <a-input placeholder="请输入源币种" v-decorator="['sourceCurrency', validatorRules.sourceCurrency ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="目标币种">
          <a-input placeholder="请输入目标币种" v-decorator="['targetCurrency', validatorRules.targetCurrency ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="源币种金额">
          <a-input-number v-decorator="[ 'sourceCurrencyMoney', validatorRules.sourceCurrencyMoney ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="目标币种兑付金额">
          <a-input-number v-decorator="[ 'targetCurrencyMoney', validatorRules.targetCurrencyMoney ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="汇率">
          <a-input-number v-decorator="[ 'exchangeRate', validatorRules.exchangeRate ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户支付方式(支付宝alipay、银行卡bank_card)">
          <a-input placeholder="请输入用户支付方式(支付宝alipay、银行卡bank_card)" v-decorator="['userPayMethod', validatorRules.userPayMethod ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户支付时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'userPayTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户收款方式(支付宝alipay、银行卡bank_card)">
          <a-input placeholder="请输入用户收款方式(支付宝alipay、银行卡bank_card)" v-decorator="['userCollectionMethod', validatorRules.userCollectionMethod ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户收款账号">
          <a-input placeholder="请输入用户收款账号" v-decorator="['userCollectionAccount', validatorRules.userCollectionAccount ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户确认收款时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'userCollectionTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商ID">
          <a-input placeholder="请输入承兑商ID" v-decorator="['assurerId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商名称">
          <a-input placeholder="请输入承兑商名称" v-decorator="['assurerName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商收款方式(支付宝alipay、银行卡bank_card)">
          <a-input placeholder="请输入承兑商收款方式(支付宝alipay、银行卡bank_card)" v-decorator="['assurerCollectionMethod', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商收款账号">
          <a-input placeholder="请输入承兑商收款账号" v-decorator="['assurerCollectionAccount', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商确认收款时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'assurerCollectionTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商支付方式(支付宝alipay、银行卡bank_card)">
          <a-input placeholder="请输入承兑商支付方式(支付宝alipay、银行卡bank_card)" v-decorator="['assurerPayMethod', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商支付账号">
          <a-input placeholder="请输入承兑商支付账号" v-decorator="['assurerPayAccount', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="承兑商兑付时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'assurerPayTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-input placeholder="请输入备注" v-decorator="['orderText', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="删除状态（0，正常，1已删除）">
          <a-input placeholder="请输入删除状态（0，正常，1已删除）" v-decorator="['delFlag', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="自动分配承兑商状态 0 成功 1失败 ">
          <a-input-number v-decorator="[ 'autoDispatchState', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="自动分配失败说明">
          <a-input placeholder="请输入自动分配失败说明" v-decorator="['autoDispatchText', {}]" />
        </a-form-item>

      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "OrderMainModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        orderCode:{rules: [{ required: true, message: '请输入订单编号!' }]},
        orderState:{rules: [{ required: true, message: '请输入订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户收款5、已完成6、已作废0)!' }]},
        userNo:{rules: [{ required: true, message: '请输入userNo!' }]},
        userName:{rules: [{ required: true, message: '请输入用户姓名!' }]},
        userContact:{rules: [{ required: true, message: '请输入用户联系方式!' }]},
        sourceCurrency:{rules: [{ required: true, message: '请输入源币种!' }]},
        targetCurrency:{rules: [{ required: true, message: '请输入目标币种!' }]},
        sourceCurrencyMoney:{rules: [{ required: true, message: '请输入源币种金额!' }]},
        targetCurrencyMoney:{rules: [{ required: true, message: '请输入目标币种兑付金额!' }]},
        exchangeRate:{rules: [{ required: true, message: '请输入汇率!' }]},
        userPayMethod:{rules: [{ required: true, message: '请输入用户支付方式(支付宝alipay、银行卡bank_card)!' }]},
        userCollectionMethod:{rules: [{ required: true, message: '请输入用户收款方式(支付宝alipay、银行卡bank_card)!' }]},
        userCollectionAccount:{rules: [{ required: true, message: '请输入用户收款账号!' }]},
        },
        url: {
          add: "/order/orderMain/add",
          edit: "/order/orderMain/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'orderCode','orderState','userNo','userName','userContact','sourceCurrency','targetCurrency','sourceCurrencyMoney','targetCurrencyMoney','exchangeRate','userPayMethod','userCollectionMethod','userCollectionAccount','assurerId','assurerName','assurerCollectionMethod','assurerCollectionAccount','assurerPayMethod','assurerPayAccount','orderText','delFlag','autoDispatchState','autoDispatchText'))
		  //时间格式化
          this.form.setFieldsValue({userPayTime:this.model.userPayTime?moment(this.model.userPayTime):null})
          this.form.setFieldsValue({userCollectionTime:this.model.userCollectionTime?moment(this.model.userCollectionTime):null})
          this.form.setFieldsValue({assurerCollectionTime:this.model.assurerCollectionTime?moment(this.model.assurerCollectionTime):null})
          this.form.setFieldsValue({assurerPayTime:this.model.assurerPayTime?moment(this.model.assurerPayTime):null})
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.userPayTime = formData.userPayTime?formData.userPayTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.userCollectionTime = formData.userCollectionTime?formData.userCollectionTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.assurerCollectionTime = formData.assurerCollectionTime?formData.assurerCollectionTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.assurerPayTime = formData.assurerPayTime?formData.assurerPayTime.format('YYYY-MM-DD HH:mm:ss'):null;

            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>