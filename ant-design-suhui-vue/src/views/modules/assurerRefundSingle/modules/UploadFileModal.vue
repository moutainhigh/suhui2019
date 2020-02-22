<template>
  <a-modal
    :title="title"
    :width="400"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <div class="clearfix">
      <a-upload
        action="http://3.93.15.101:3333/order/orderMain/upload"
        listType="picture-card"
        :fileList="fileList"
        :headers="headers"
        @preview="handlePreview"
        @change="handleChange"
      >
        <div v-if="fileList.length < 3">
          <a-icon type="plus" />
          <div class="ant-upload-text">Upload</div>
        </div>
      </a-upload>
      <a-modal :visible="previewVisible" :footer="null" @cancel="cancelPreview">
        <img alt="example" style="width: 100%" :src="previewImage" />
      </a-modal>
    </div>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    name: "uploadFileModal",
    data () {
      return {
        title:"上传付款凭证",
        previewVisible: false,
        previewImage: '',
        fileList: [],
        confirmLoading: false,
        headers: {}
      }
    },
    props: {
      visible: {
        type: Boolean,
        default: false
      }
    },
    created() {
      console.log(this);
      this.headers = {
        // 'X-Access-Token': this.$ls.get(ACCESS_TOKEN)
        'X-Access-Token':'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODM0Njc3OTYsInVzZXJuYW1lIjoiMTM2MDAwNzgwMDYifQ.KdSfrlrkzTEj8G2Ac5Y5JNfL_NVqXn_9ugzzE_t8HH4'

      }
    },
    methods: {
      cancelPreview () {
        this.previewVisible = false
      },
      handlePreview (file) {
        this.previewImage = file.url || file.thumbUrl;
        this.previewVisible = true
      },
      handleOk () {
        // 判断文件长度
        if (this.fileList.length) {
          let fileList = [];
          let flag = true;
          for(let item of this.fileList) {
            flag = 200 === item.response.code && flag;
            if (item.response.result) {
              fileList.push(item.response.result.url);
            }
          }
          if (flag) {
            this.fileList = [];
            this.$emit('ok', fileList);
          } else {
            this.$message.warning("部分文件上传失败，请删除并重新上传");
          }
        } else {
          this.$message.warning("请上传付款凭证");
        }
      },
      handleCancel () {
        this.fileList = [];
        this.$emit('close');
      },
      handleChange ({ fileList }) {
        this.fileList = fileList
      }
    }
  }
</script>

<style lang="less" scoped>
  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>