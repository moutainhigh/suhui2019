<template>

  <div>
    {{ msg }}
  </div>
</template>

<script>
  import {getAction} from '@/api/manage'
  export default {
    data () {
      return {
        msg: ""
      }
    },
    methods: {
      hello () {
        var url = "/test/daweiDemo/hello"
        getAction(url).then((res) => {
          console.log("#########################")
          console.log(res)
          if (res.success) {
            this.msg = res.result;
          }
        })
      }
    },
    created() {
      this.hello();
    }
  }
</script>