<!--<p class="warn-content">-->
<!--Markdown 基于-->
<!--<el-link type="primary" href="" target="_blank">MavonEditor</el-link>-->
<!--</p>-->
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
    v-model="content"
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
import { UploadFile } from '@/utils/CsDomUtil'

const dynamicHeight = 160
export default {
  name: 'CuteMarkdown',
  components: {
    mavonEditor
  },
  props: {
    content: {
      type: String,
      required: true,
      default: ''
    }
  },
  data() {
    return {
      height: document.documentElement.clientHeight - dynamicHeight + 'px'
    }
  },
  computed: {
    ...mapGetters([
      'imagesUploadApi',
      'baseApi'
    ])
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
    },
    onSave(value, render) {
      this.$emit('save', value, render)
    },
    resetField() {
      this.content = ''
    }
  }
}
</script>

<style scoped>
</style>
