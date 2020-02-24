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
        <detail-list title="----------------------------账户信息----------------------------" :col="col">
          <detail-list-item term="账号">{{ model.platformAccountNo }}</detail-list-item>
          <detail-list-item term="真实姓名">{{ model.realName }}</detail-list-item>
          <detail-list-item term="开户行">{{ model.openBank }}</detail-list-item>
          <detail-list-item term="开户网点">{{ model.openBankBranch }}</detail-list-item>
          <detail-list-item term="国际电汇代码">{{ model.swiftCode }}</detail-list-item>
          <detail-list-item term="创建时间">{{ model.createTime }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------缴费信息----------------------------" :col="col"
                     v-if="model.payVoucher">
          <detail-list-item term="缴费金额类型">{{ model.moneyType }}</detail-list-item>
          <detail-list-item term="缴费金额">{{ model.payMoney }}</detail-list-item>
          <detail-list-item term="缴费说明">{{ model.payText }}</detail-list-item>
        </detail-list>
        <detail-list title="----------------------------付款凭证----------------------------" :col="col"
                     v-if="model.payVoucher">
          <div class="img-model">
            <img v-for="(item,i) in model.payVoucher.split(',')" @click="handlePreview(item)" :src="item"/>
          </div>
        </detail-list>
        <a-modal :visible="previewVisible" :footer="null" @cancel="cancelPreview">
          <img alt="example" style="width: 100%" :src="previewImage"/>
        </a-modal>
      </a-card>
    </a-spin>
  </a-drawer>
</template>

<script>
  import DetailList from '@/components/tools/DetailList'

  const DetailListItem = DetailList.Item
  import moment from 'moment'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'

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
        previewVisible: false,
        previewImage: '',
        drawerWidth: 500,
        model: {},
        confirmLoading: false
      }
    },
    methods: {
      cancelPreview() {
        this.previewVisible = false
      },
      handlePreview(url) {
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
        this.resetScreenSize()
        this.model = Object.assign({}, record)
        this.visible = true
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