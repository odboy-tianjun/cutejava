<!--
 * 代码编辑器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 * @description 基于Yaml编辑器 https://github.com/codemirror/CodeMirror，主题预览地址 https://codemirror.net/demo/theme.html#idea
 * @参数mode取值范围 yaml、java、go、swift、dockerfile、groovy、lua、perl、python、ruby、sql、xml、vue
 -->
<template>
  <textarea ref="textarea" />
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
// 替换主题这里需修改名称
// 主题预览地址 https://blog.csdn.net/qq_41694291/article/details/106429772
import 'codemirror/theme/darcula.css'
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/yaml/yaml'
import 'codemirror/mode/go/go'
import 'codemirror/mode/swift/swift'
import 'codemirror/mode/dockerfile/dockerfile'
import 'codemirror/mode/groovy/groovy'
import 'codemirror/mode/lua/lua'
import 'codemirror/mode/perl/perl'
import 'codemirror/mode/python/python'
import 'codemirror/mode/ruby/ruby'
import 'codemirror/mode/sql/sql'
import 'codemirror/mode/vue/vue'
import 'codemirror/mode/xml/xml'
import KitMessage from '@/utils/elementui/KitMessage'

const SupportModeList = ['yaml', 'java', 'go', 'swift', 'dockerfile', 'groovy', 'lua', 'perl', 'python', 'ruby', 'sql', 'xml', 'vue']
export default {
  name: 'CuteCodeEditor',
  props: {
    value: {
      type: String,
      required: false,
      default: ''
    },
    height: {
      type: String,
      required: false,
      default: '500px'
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
    mode: {
      type: String,
      required: false,
      default: 'yaml'
    }
  },
  data() {
    return {
      editor: false
    }
  },
  watch: {
    value(newVal, oldVal) {
      const editorValue = this.editor.getValue()
      if (newVal !== editorValue) {
        this.editor.setValue(newVal)
      }
    },
    height(newVal, oldVal) {
      this.editor.setSize('auto', newVal)
    }
  },
  mounted() {
    if (this.mode && SupportModeList.includes(this.mode)) {
      let mode = 'yaml'
      if (this.mode === 'java' || this.mode === 'javascript') {
        mode = 'javascript'
      } else {
        mode = this.mode
      }
      this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
        mode: mode,
        autoRefresh: true,
        lineNumbers: true,
        lint: true,
        lineWrapping: true,
        tabSize: 2,
        cursorHeight: 1,
        // 替换主题这里需修改名称
        theme: 'darcula'
      })
      this.editor.setSize('auto', this.height)
      if (this.value) {
        this.editor.setValue(this.value)
      }
      this.editor.setOption('readOnly', this.readonly)
      this.editor.on('change', cm => {
        const value = cm.getValue()
        this.$emit('change', value)
        this.$emit('input', value)
      })
      return
    }
    KitMessage.Error('不支持的mode')
  },
  methods: {
    getValue() {
      if (this.editor == null) {
        return ''
      }
      return this.editor.getValue()
    },
    setValue(val) {
      if (this.editor == null) {
        return
      }
      this.editor.setValue(val)
    },
    resetField() {
      this.editor.setValue('')
      this.$emit('change', '')
      this.$emit('input', '')
    }
  }
}
</script>
