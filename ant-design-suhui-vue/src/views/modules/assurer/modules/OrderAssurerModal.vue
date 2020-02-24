<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form" v-if="title === '编辑'">
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="用户编号">
              <a-input placeholder="请输入用户编号" disabled v-decorator="['userNo',{}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="承兑商名称">
              <a-input placeholder="请输入承兑商名称" v-decorator="['assurerName', validatorRules.assurerName ]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="费率">
              <a-input-number placeholder="请输入费率" style="width:100%" v-decorator="[ 'assurerRate', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="保证金比例">
              <a-input-number  placeholder="请输入保证金比例" style="width:100%" v-decorator="['ensureProportion',  validatorRules.ensureProportion ]"/>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12" :gutter="8">-->
          <!--<a-form-item-->
          <!--:labelCol="labelCol"-->
          <!--:wrapperCol="wrapperCol"-->
          <!--label="在线状态">-->
          <!--&lt;!&ndash;<a-input placeholder="请输入在线状态" disabled style="width:100%" v-decorator="[ 'onlineState', {}]"/>&ndash;&gt;-->
          <!--</a-form-item>-->
          <!--</a-col>-->
        </a-row>
        <!--<a-row>-->
        <!--<a-col :span="12" :gutter="8">-->
        <!--<a-form-item-->
        <!--:labelCol="labelCol"-->
        <!--:wrapperCol="wrapperCol"-->
        <!--label="承兑商状态">-->
        <!--&lt;!&ndash;<a-input placeholder="请输入承兑商状态" v-decorator="['assurerState', validatorRules.assurerState ]"/>&ndash;&gt;-->
        <!--</a-form-item>-->
        <!--</a-col>-->
        <!--</a-row>-->
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="可用额度">
              <a-input placeholder="请输入可用额度" disabled style="width:100%"
                       v-decorator="[ 'canUseLimit',{} ]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="已用额度">
              <a-input placeholder="请输入已用额度" disabled style="width:100%"
                       v-decorator="[ 'usedLimit', {} ]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="总限额">
              <a-input placeholder="请输入总限额" style="width:100%"
                       v-decorator="[ 'totalLimit', validatorRules.totalLimit ]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="承兑商国家码">
              <a-input placeholder="请输入承兑商国家码" v-decorator="['countryCode', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <!--<a-row>-->
        <!--<a-col :span="12" :gutter="8">-->
        <!--<a-form-item-->
        <!--:labelCol="labelCol"-->
        <!--:wrapperCol="wrapperCol"-->
        <!--label="策略">-->
        <!--<a-input placeholder="请输入策略"  v-decorator="['assurerStrategy', {}]"/>-->
        <!--</a-form-item>-->
        <!--</a-col>-->
        <!--</a-row>-->
      </a-form>

      <a-form :form="form" v-if="title === '新增'">
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="区号">
              <a-select size="large" v-decorator="['areacode']">
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
          </a-col>
          <a-col  :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="手机号">
              <a-input placeholder="请输入手机号" v-decorator="['phone', validatorRules.phone ]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="登录密码">
              <a-input placeholder="请输入登录密码" v-decorator="['password', validatorRules.password ]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="承兑商名称">
              <a-input placeholder="请输入承兑商名称" v-decorator="['assurerName', validatorRules.assurerName ]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="保证金比例">
              <a-input-number  placeholder="请输入保证金比例" style="width:100%" v-decorator="['ensureProportion', validatorRules.ensureProportion]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="费率">
              <a-input-number placeholder="请输入费率" style="width:100%" v-decorator="[ 'assurerRate', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="总限额">
              <a-input placeholder="请输入总限额" style="width:100%"
                       v-decorator="[ 'totalLimit', validatorRules.totalLimit ]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="承兑商国家码">
              <a-input placeholder="请输入承兑商国家码" v-decorator="['countryCode', {}]"/>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12" :gutter="8">-->
          <!--<a-form-item-->
          <!--:labelCol="labelCol"-->
          <!--:wrapperCol="wrapperCol"-->
          <!--label="策略">-->
          <!--<a-input placeholder="请输入策略"  v-decorator="['assurerStrategy', {}]"/>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="账户明细" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="orderAssurerAccountTable.loading"
            :columns="orderAssurerAccountTable.columns"
            :dataSource="orderAssurerAccountTable.dataSource"
            :maxHeight="350"
            :rowNumber="true"
            :rowSelection="false"
            :actionButton="false"/>


        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import moment from 'moment'
  import pick from 'lodash.pick'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import STable from '@/components/table/'

  export default {
    name: 'OrderAssurerModal',
    mixins: [JEditableTableMixin],
    data() {
      return {
        accountTypeDictOptions: [],
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          assurerName: { rules: [{ required: true, message: '请输入承兑商名称!' }] },
          areacode: { rules: [{ required: true, message: '请输入区号!' }] },
          phone: { rules: [{ required: true, message: '请输入手机号!' }] },
          password: { rules: [{ required: true, message: '请输入登录密码!' }] },
          totalLimit: { rules: [{ required: true, message: '请输入总额度!' }] },
          ensureProportion: { rules: [{ required: true, message: '请输入保证金比例!' }] }
        },
        refKeys: ['orderAssurerAccount'],
        activeKey: 'orderAssurerAccount',
        // 客户明细
        orderAssurerAccountTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '账户类型',
              key: 'accountType',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '账户',
              key: 'accountNo',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '区号',
              key: 'areaCode',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            }, {
              title: '开户行',
              key: 'openBank',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            }, {
              title: '开户网点',
              key: 'openBankBranch',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '真实姓名',
              key: 'realName',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            // {
            //   title: '使用方式',
            //   key: 'useType',
            //   type: FormTypes.normal,
            //   defaultValue: '',
            //   placeholder: '请输入${title}',
            //   validateRules: [{ required: true, message: '${title}不能为空' }],
            // },
            {
              title: '每日支付限额',
              key: 'payLimit',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '支付已用额度',
              key: 'payUsedLimit',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '支付可用额度',
              key: 'payCanUseLimit',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '收款已用额度',
              key: 'collectionUsedLimit',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            },
            {
              title: '支付锁定金额',
              key: 'payLockMoney',
              type: FormTypes.normal,
              defaultValue: '',
              placeholder: '请输入${title}'
            }
          ]
        },
        url: {
          add: '/order/orderAssurer/addAssurer',
          edit: '/order/orderAssurer/edit',
          orderAssurerAccount: {
            list: '/order/orderAssurer/queryOrderAssurerAccountByMainId'
          }
        }
      }
    },
    components: {
      STable
    },
    filters: {
      accountTypeFilter(status) {
        const statusMap = {
          'processing': '进行中',
          'success': '完成',
          'failed': '失败'
        }
        return statusMap[status]
      }
    },
    methods: {
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'userNo', 'assurerName', 'openBank', 'openBankBranch', 'countryCode', 'onlineState', 'assurerState', 'assurerRate', 'canUseLimit', 'usedLimit', 'totalLimit', 'assurerStrategy', 'payLockMoney', 'delFlag'))
          // 时间格式化
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.orderAssurerAccount.list, params, this.orderAssurerAccountTable)
        }
      },

      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        //时间格式化
        return {
          ...main, // 展开
          orderAssurerAccountList: allValues.tablesValue[0].values
        }
      }
    }
  }
</script>

<style scoped>
</style>