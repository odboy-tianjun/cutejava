<!--
 * 富文本编辑器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <div ref="editor" style="border: 1px solid #ccc;">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editor"
      :default-config="toolbarConfig"
      :mode="editMode"
    />
    <Editor
      v-model="editValue"
      :style="`height: 500px;overflow-y: hidden`"
      :default-config="editorConfig"
      :mode="editMode"
      @onCreated="onCreated"
      @onChange="onChange"
      @onDestroyed="onDestroyed"
      @onFocus="onFocus"
      @onBlur="onBlur"
      @customAlert="customAlert"
      @customPaste="customPaste"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { UploadFile } from '@/utils/KitDomUtil'
import KitMessage from '@/utils/elementui/KitMessage'

export default {
  name: 'CuteRichTextEditor',
  components: { Toolbar, Editor },
  props: {
    readOnly: {
      type: Boolean,
      required: false,
      default: false
    },
    content: {
      type: String,
      required: false,
      default: ''
    }
  },
  data() {
    const that = this
    return {
      toolbarConfig: {},
      editorConfig: {
        placeholder: '请输入内容...',
        readOnly: that.readOnly,
        autoFocus: true,
        scroll: false,
        // 会造成卡顿
        // maxLength: this.maxLength
        MENU_CONF: {
          'uploadImage': {
            // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
            allowedFileTypes: ['image/*'],
            // 自定义上传
            async customUpload(file, insertFn) { // JS 语法
              UploadFile(that.imagesUploadApi, file).then(res => {
                const data = res.data
                const url = that.baseApi + '/file/' + data.dateGroup + '/' + data.realName
                // 最后插入图片
                insertFn(url, '', '')
              })
            }
          }
        }
      },
      // 'default' or 'simple'
      editMode: 'simple',
      editor: null,
      editValue: ''
    }
  },
  computed: {
    ...mapGetters([
      'imagesUploadApi',
      'baseApi'
    ])
  },
  methods: {
    /**
     * 编辑器创建完毕时的回调函数
     * @param editor
     */
    onCreated(editor) {
      // 一定要用 Object.seal(), 否则会报错
      this.editor = Object.seal(editor)
      this.editValue = this.content
    },
    /**
     * 编辑器内容、选区变化时的回调函数
     * @param editor
     */
    onChange(editor) {
      if (editor == null) {
        return
      }
      // 非格式化的 html
      const html = editor.getHtml()
      // 纯文本内容
      const text = editor.getText()
      this.$emit('change', html, text)
    },
    /**
     * 编辑器销毁时的回调函数。调用 editor.destroy() 即可销毁编辑器
     * @param editor
     */
    onDestroyed(editor) {
      if (editor == null) {
        return
      }
      editor.destroy()
    },
    /**
     * 编辑器内容达到最大长度时的回调函数(会导致卡顿)
     * @param editor
     */
    onMaxLength(editor) {
      if (editor == null) {
        return
      }
    },
    /**
     * 编辑器 focus 时的回调函数
     * @param editor
     */
    onFocus(editor) {
      if (editor == null) {
        return
      }
      this.$emit('focus', editor)
    },
    /**
     * 编辑器 blur 时的回调函数
     * @param editor
     */
    onBlur(editor) {
      if (editor == null) {
        return
      }
      this.$emit('blur', editor)
    },
    /**
     * 自定义编辑器 alert 。如想用 ElementUI 的 Message 功能
     * @param editor
     */
    customAlert(info, type) {
      switch (type) {
        case 'success':
          KitMessage.Error(info)
          break
        case 'info':
          KitMessage.Info(info)
          break
        case 'warning':
          KitMessage.Warning(info)
          break
        case 'error':
          KitMessage.Error(info)
          break
        default:
          KitMessage.Info(info)
          break
      }
    },
    /**
     * 自定义粘贴。可阻止编辑器的默认粘贴，实现自己的粘贴逻辑
     * @param editor
     */
    customPaste(editor, event, callback) {
      if (editor == null) {
        callback(true)
      }
      // console.log('ClipboardEvent 粘贴事件对象', event)
      // // const html = event.clipboardData.getData('text/html') // 获取粘贴的 html
      // // const text = event.clipboardData.getData('text/plain') // 获取粘贴的纯文本
      // // const rtf = event.clipboardData.getData('text/rtf') // 获取 rtf 数据（如从 word wsp 复制粘贴）
      //
      // // 自定义插入内容
      // editor.insertText('xxx')
      //
      // // 返回 false ，阻止默认粘贴行为
      // event.preventDefault()
      // callback(false) // 返回值（注意，vue 事件的返回值，不能用 return）
      //
      // // 返回 true ，继续默认的粘贴行为
      // // callback(true)
      callback(true)
    },
    /**
     * 设置编辑器html内容
     * @param html
     * <br/>
     * 如果是 editor.getHtml() 获取的 HTML 格式，可以完美解析。
     * 如果是其他的 HTML 格式，则不能保证语义正确 —— dangerously 。
     */
    setHtml(html) {
      if (this.editor == null) {
        return
      }
      this.editor.dangerouslyInsertHtml(html)
    },
    /**
     * 获取编辑器html内容
     */
    getHtml() {
      if (this.editor == null) {
        return ''
      }
      this.editor.getHtml()
    },
    /**
     * 清空编辑器内容
     */
    clear() {
      if (this.editor == null) {
        return
      }
      this.editor.clear()
    },
    /**
     * 撤销
     */
    undo() {
      if (this.editor == null) {
        return
      }
      this.editor.undo()
    },
    /**
     * 重做
     */
    redo() {
      if (this.editor == null) {
        return
      }
      this.editor.redo()
    },
    /**
     * 获取选中的文本
     */
    getSelectionText() {
      if (this.editor == null) {
        return ''
      }
      return this.editor.getSelectionText()
    },
    /**
     * 删除选中的内容
     */
    deleteFragment() {
      if (this.editor == null) {
        return
      }
      this.editor.deleteFragment()
    },
    /**
     * 获取选中的内容。JSON格式
     */
    getFragment() {
      if (this.editor == null) {
        return {}
      }
      return this.editor.getFragment()
    },
    /**
     * 获取文本内容
     */
    getText() {
      if (this.editor == null) {
        return ''
      }
      return this.editor.getText()
    },
    /**
     * 在选区插入文本
     * @param text
     */
    insertText(text) {
      if (this.editor == null) {
        return
      }
      this.editor.insertText(text)
    },
    /**
     * el-form重置联动
     */
    resetField() {
      this.setHtml('')
    }
  }
}
</script>
