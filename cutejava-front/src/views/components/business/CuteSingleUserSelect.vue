<!--
 * 单用户选择组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-07-29
 -->
<template>
  <el-select
    v-model="innerValue"
    style="width: 100%"
    filterable
    remote
    clearable
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
  name: 'CuteSingleUserSelect',
  props: {
    value: {
      type: String,
      required: false,
      default: null
    }
  },
  data() {
    return {
      innerValue: null,
      loading: false,
      options: []
    }
  },
  watch: {
    value(newVal) {
      this.innerValue = newVal
    }
  },
  async created() {
    const that = this
    if (that.value) {
      that.options = await CuteUserSelectApi.listMetadataByUsernames([that.value])
      that.innerValue = that.value
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
    },
    resetField() {
      this.innerValue = null
      this.$emit('change', null)
      this.$emit('input', null)
    }
  }
}
</script>

