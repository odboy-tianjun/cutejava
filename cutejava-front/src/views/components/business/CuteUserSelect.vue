<!--
 * 多用户选择组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-07
 -->
<template>
  <el-select
    v-model="users"
    style="width: 100%"
    multiple
    filterable
    remote
    reserve-keyword
    placeholder="支持用户姓名、手机号、邮箱、用户名查询"
    :remote-method="remoteMethod"
    :loading="loading"
    @change="onChange"
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<script>

import CuteUserSelectApi from '@/api/application/cute-user-select'

export default {
  name: 'CuteUserSelect',
  props: {
    value: {
      type: Array,
      required: false,
      default: null
    }
  },
  data() {
    return {
      users: [],
      loading: false,
      options: []
    }
  },
  watch: {
    value(newVal) {
      this.users = newVal
    }
  },
  async mounted() {
    const that = this
    if (that.value && that.value.length > 0) {
      that.options = await CuteUserSelectApi.listMetadataByUsernames(that.value)
      that.users = that.value
    }
  },
  methods: {
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true
        setTimeout(() => {
          this.loading = false
          this.listMetadata(query)
        }, 200)
      } else {
        this.options = []
      }
    },
    listMetadata(query, options) {
      const that = this
      CuteUserSelectApi.listMetadata(query).then(res => {
        that.options = res
      })
    },
    onChange(value) {
      // 绑定change事件
      this.$emit('change', value)
      // 绑定form value
      this.$emit('input', value)
    }
  }
}
</script>

