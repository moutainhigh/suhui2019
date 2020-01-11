<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px;"
  >
    <template slot="title">
      <div style="width: 100%;">
        <span>{{ title }}</span>
        <span style="display:inline-block;width:calc(100% - 51px);padding-right:10px;text-align: right">
          <a-button @click="toggleScreen" icon="appstore" style="height:20px;width:20px;border:0px"></a-button>
        </span>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="货币名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入货币名称" v-decorator="['currencyName', validatorRules.currencyName]" />
        </a-form-item>
        <a-form-item label="货币代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入货币代码" v-decorator="['currencyCode', validatorRules.currencyCode]" />
        </a-form-item>
        <a-form-item label="货币符号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入货币符号" v-decorator="['currencySymbol', validatorRules.currencySymbol]" />
        </a-form-item>
        <a-form-item label="货币单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入货币单位" v-decorator="['currencyUnit', validatorRules.currencyUnit]" />
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['status', validatorRules.status]" placeholder="请选择状态">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">停用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入备注" v-decorator="['remark', {}]" />
        </a-form-item>
        <!-- <a-form-item label="工作流引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            v-decorator="['activitiSync', {}]"
            placeholder="请选择是否同步工作流引擎"
            :type="'radio'"
            :triggerChange="true"
            dictCode="activiti_sync"
          />
        </a-form-item> -->
      </a-form>
    </a-spin>

    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="confirmLoading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import pick from 'lodash.pick';
import Vue from 'vue';
import { ACCESS_TOKEN } from "@/store/mutation-types";
// import { getAction, putAction } from '@/api/manage';
import { addCurrency, editCurrency, checkCurrencyName } from '@/api/api';
import { disabledAuthFilter } from "@/utils/authFilter";

export default {
  name: "CurrencyModal",
  components: {},
  data() {
    return {
      modalWidth: 800,
      drawerWidth: 700,
      modaltoggleFlag: true,
      confirmDirty: false,
      // currencyId: '', //保存货币id
      disableSubmit: false,
      // dateFormat: "YYYY-MM-DD",
      validatorRules: {
        currencyName: {
          rules: [
            { required: true, message: '请输入货币名称!' },
            { validator: this.validateCurrencyName }
          ]
        },
        currencyCode: {
          rules: [{ required: true, message: '请输入货币代码!' }]
        },
        currencySymbol: {
          rules: [{ required: true, message: '请输入货币符号!' }]
        },
        currencyUnit: {
          rules: [{ required: true, message: '请输入货币单位!' }]
        },
        status: {
          rules: [{
            required: true, message: '请选择状态!'
          }]
        }
      },
      title: "操作",
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
      // uploadLoading: false,
      confirmLoading: false,
      headers: {},
      form: this.$form.createForm(this),
      url: {
        // imgerver: window._CONFIG['domianURL'] + '/sys/common/view'
        // syncCurrencyById: '/process/extActProcess/...' // 同步货币到工作流
      }
    };
  },

  created() {
    const token = Vue.ls.get(ACCESS_TOKEN);
    this.headers = { 'X-Access-Token': token };
  },

  computed: {},

  methods: {
    // disabledAuth(code) {
    //   return disabledAuthFilter(code);
    // },

    //窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth;
      } else {
        this.modalWidth = 800;
      }
      this.modaltoggleFlag = !this.modaltoggleFlag;
    },

    // refresh() {
    //   this.currencyId = '';
    // },

    add() {
      // this.refresh();
      this.edit({});
    },

    edit(record) {
      this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      const that = this;
      that.form.resetFields();
      // that.currencyId = record.id;
      that.visible = true;
      that.model = Object.assign({}, record);
      that.$nextTick(() => {
        that.form.setFieldsValue(pick(this.model, 'currencyName', 'currencyCode', 'currencySymbol', 'currencyUnit', 'status', 'remark'));
      });
    },

    close() {
      this.$emit('close');
      this.visible = false;
      this.disableSubmit = false;
    },

    handleSubmit() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          if (!values.remark) {
            values.remark = '';
          }
          const newData = Object.assign(this.model, values);
          const formData = new FormData();
          formData.append('id', newData.id);
          formData.append('currencyname', newData.currencyName);
          formData.append('currencycode', newData.currencyCode);
          formData.append('currencysymbol', newData.currencySymbol);
          formData.append('currencyunit', newData.currencyUnit);
          formData.append('status', newData.status);
          formData.append('remark', newData.remark);
          let obj;
          if (!this.model.id) {
            obj = addCurrency(formData);
          } else {
            obj = editCurrency(formData);
          }
          obj.then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
            that.close();
          });
        }
      });
    },

    handleCancel() {
      this.close();
    },

    //check if currency name is unique
    validateCurrencyName(rule, value, callback) {
      callback();
      // const params = {
      //   id: this.model.id,
      //   currencyName: value
      // };
      // checkCurrencyName(params).then((res) => {
      //   if (res.success) {
      //     callback();
      //   } else {
      //     callback('货币已存在！');
      //   }
      // });
    },

    // handleConfirmBlur(e) {
    //   const value = e.target.value
    //   this.confirmDirty = this.confirmDirty || !!value
    // },

    // 根据屏幕变化,设置抽屉尺寸
    resetScreenSize() {
      let screenWidth = document.body.clientWidth;
      if (screenWidth < 500) {
        this.drawerWidth = screenWidth;
      } else {
        this.drawerWidth = 700;
      }
    },
  }
};
</script>

<style scoped>
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 49px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 10px;
  padding-bottom: 10px;
}

.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
</style>