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
          label="承兑商名称">
          <a-input placeholder="请输入承兑商名称" disabled v-decorator="['assurerName', {}]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="电话">
          <a-input placeholder="请输入电话" disabled v-decorator="['assurerPhone', {}]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="变动金额">
          <a-input-number v-decorator="[ 'changeMoney', {}]" :disabled="true"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="变动种类">
          <a-input placeholder="" v-decorator="['changeClass', {}]" disabled/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="类型">
          <j-dict-select-tag v-decorator="['changeType',{}]" :disabled="true" :triggerChange="true" placeholder=""
                             dictCode="changeType"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="执行状态">
          <j-dict-select-tag v-decorator="['flag',{}]" :triggerChange="true" :disabled="true"  placeholder=""
                             dictCode="flagState"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="说明">
          <a-input placeholder="请输入说明" v-decorator="['changeText', {}]" disabled/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单编号">
          <a-input placeholder="请输入订单编号" v-decorator="['orderNo', {}]" disabled/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="报错异常">
          <a-input placeholder="报错异常" v-decorator="['errorText', {}]" disabled/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from 'moment'

  export default {
    name: 'OrderAssurerMoneyChangeModal',
    data() {
      return {
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
        },
        url: {
          add: '/order/assurerMoneyChange/add',
          edit: '/order/assurerMoneyChange/edit'
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'assurerId', 'assurerName', 'assurerPhone', 'changeMoney', 'changeType', 'changeClass', 'flag', 'delFlag', 'changeText', 'orderId', 'orderNo', 'errorText'))
          //时间格式化
        })

      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            //时间格式化

            console.log(formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })


          }
        })
      },
      handleCancel() {
        this.close()
      }


    }
  }
</script>

<style lang="less" scoped>

</style>