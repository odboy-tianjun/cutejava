<!--
 * Markdown编辑器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 * @description 基于MavonEditor https://github.com/hinesboy/mavonEditor
 -->
<template>
  <mavon-editor
    ref="md"
    v-model="innerValue"
    :style="`height: + ${height};overflow-y: hidden`"
    :ishljs="true"
    @imgAdd="imgAdd"
    @change="onChange"
    @save="onSave"
  />
</template>

<script>
import { mapGetters } from 'vuex'
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import { UploadFile } from '@/utils/KitDomUtil'

const dynamicHeight = 160
export default {
  name: 'CuteMarkdown',
  components: {
    mavonEditor
  },
  props: {
    value: {
      type: String,
      required: true,
      default: ''
    }
  },
  data() {
    return {
      height: document.documentElement.clientHeight - dynamicHeight + 'px',
      innerValue: ''
    }
  },
  computed: {
    ...mapGetters([
      'imagesUploadApi',
      'baseApi'
    ])
  },
  watch: {
    value(newVal) {
      this.innerValue = newVal
    }
  },
  mounted() {
    const that = this
    window.onresize = () => {
      that.height = document.documentElement.clientHeight - dynamicHeight + 'px'
    }
  },
  methods: {
    imgAdd(pos, $file) {
      const that = this
      UploadFile(that.imagesUploadApi, $file).then(res => {
        const data = res.data
        const url = that.baseApi + '/file/' + data.type + '/' + data.realName
        that.$refs.md.$img2Url(pos, url)
      })
    },
    onChange(value, render) {
      this.$emit('change', value, render)
      this.$emit('input', value)
    },
    onSave(value, render) {
      this.$emit('save', value, render)
      this.$emit('change', value)
      this.$emit('input', value)
    },
    resetField() {
      this.innerValue = ''
      this.$emit('change', '')
      this.$emit('input', '')
    }
  }
}
</script>

<style scoped>
</style>
