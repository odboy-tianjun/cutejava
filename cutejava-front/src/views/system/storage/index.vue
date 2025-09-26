<template>
  <el-tabs v-model="activeName" style="padding-left: 8px;" @tab-click="tabClick">
    <el-tab-pane label="本地存储" name="local">
      <Local ref="local" />
    </el-tab-pane>
    <el-tab-pane label="OSS存储" name="oss">
      <Oss ref="oss" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import Local from './local/index.vue'
import Oss from './oss/index.vue'

export default {
  name: 'Storage',
  components: { Local, Oss },
  data() {
    return {
      activeName: 'local'
    }
  },
  methods: {
    tabClick(name) {
      if (this.activeName === 'local') {
        this.$refs.local.crud.toQuery()
      } else {
        this.$refs.oss.crud.toQuery()
      }
    }
  }
}
</script>

<style scoped>
</style>
