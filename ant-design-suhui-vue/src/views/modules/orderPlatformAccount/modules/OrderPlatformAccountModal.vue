<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="账号类型">
          <j-dict-select-tag :triggerChange="true" dictCode="channel_type" v-decorator="['accountType', validatorRules.accountType ]" placeholder="请选择账号类型" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="区号">
          <a-select  v-decorator="['areaCode']" placeholder="请选择区号">
            <a-select-option value="+86">+86</a-select-option>
            <a-select-option value="+852">+852</a-select-option>
            <a-select-option value="+82">+82</a-select-option>
            <a-select-option value="+63">+63</a-select-option>
            <a-select-option value="+61">+61</a-select-option>
            <a-select-option value="+1">+1</a-select-option>
            <a-select-option value="+84">+84</a-select-option>
            <a-select-option value="+81">+81</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="账户">
          <a-input placeholder="请输入账户" v-decorator="['accountNo', validatorRules.accountNo ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="余额">
          <a-input-number placeholder="请输入余额" style="width:100%"  v-decorator="['accountMoney', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="真实姓名">
          <a-input placeholder="请输入真实姓名" v-decorator="['realName', validatorRules.realName ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="国际电汇代码">
          <a-input placeholder="请输入国际电汇代码" v-decorator="['swiftCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开户行">
          <a-input placeholder="请输入开户行" v-decorator="['openBank', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开户网点">
          <a-input placeholder="请输入开户网点" v-decorator="['openBankBranch', {}]" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "OrderPlatformAccountModal",
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
        accountNo:{rules: [{ required: true, message: '请输入账户!' }]},
        accountType:{rules: [{ required: true, message: '请输入账号类型!' }]},
        realName:{rules: [{ required: true, message: '请输入真实姓名!' }]},
        },
        url: {
          add: "/order/platformAccount/add",
          edit: "/order/platformAccount/edit",
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
          this.form.setFieldsValue(pick(this.model,'accountNo','openBank','swiftCode','openBankBranch','accountMoney','accountType','accountState','areaCode','realName','delFlag'))
		  //时间格式化
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

</style>