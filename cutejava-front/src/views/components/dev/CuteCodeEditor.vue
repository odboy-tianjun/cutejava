<!--Yaml编辑器 基于-->
<!--<a href="https://github.com/codemirror/CodeMirror" target="_blank">CodeMirror</a>，-->
<!--主题预览地址 <a href="https://codemirror.net/demo/theme.html#idea" target="_blank">Theme</a>-->
<!--</p>-->
<!-- mode取值如下：yaml、java、go、swift、dockerfile、groovy、lua、perl、python、ruby、sql、xml、vue -->
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
import { Message } from 'element-ui'

const SupportModeList = ['yaml', 'java', 'go', 'swift', 'dockerfile', 'groovy', 'lua', 'perl', 'python', 'ruby', 'sql', 'xml', 'vue']
export default {
  name: 'CuteCodeEditor',
  props: {
    content: {
      type: String,
      required: true,
      default: ''
    },
    height: {
      type: String,
      required: false,
      default: '500px'
    },
    readOnly: {
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
    content(newVal, oldVal) {
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
      if (this.content) {
        this.editor.setValue(this.content)
      }
      this.editor.setOption('readOnly', this.readOnly)
      this.editor.on('change', cm => {
        this.$emit('change', cm.getValue())
      })
      return
    }
    Message.error('不支持的mode')
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
    }
  }
}
</script>
