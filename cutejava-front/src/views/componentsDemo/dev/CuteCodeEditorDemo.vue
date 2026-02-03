<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>需求输入各种代码数据</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）@Odboy（设计）</li>
    </ul>
    <h4>Java编辑器</h4>
    <cute-code-editor :content="javaContent" mode="java" @change="onJavaChange" />
    <h4>Yaml编辑器</h4>
    <cute-code-editor :content="yamlContent" mode="yaml" @change="onYamlChange" />
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import CuteCodeEditor from '@/views/components/dev/CuteCodeEditor.vue'

export default {
  name: 'CuteRichTextEditorDemo',
  components: { CuteCodeEditor },
  data() {
    return {
      javaContent: 'package cn.odboy;\n' +
        '\n' +
        'import cn.odboy.framework.context.CsBootApplication;\n' +
        'import io.swagger.annotations.Api;\n' +
        'import org.springframework.boot.SpringApplication;\n' +
        'import org.springframework.boot.autoconfigure.SpringBootApplication;\n' +
        '\n' +
        'import java.net.UnknownHostException;\n' +
        '\n' +
        '@Api(hidden = true)\n' +
        '@SpringBootApplication\n' +
        'public class AppRun extends CsBootApplication {\n' +
        '\n' +
        '    public static void main(String[] args) throws UnknownHostException {\n' +
        '        SpringApplication springApplication = new SpringApplication(AppRun.class);\n' +
        '        inited(springApplication.run(args));\n' +
        '    }\n' +
        '}\n',
      yamlContent: 'server:\n' +
        '  port: 8000\n' +
        '  http2:\n' +
        '    # 启用 HTTP/2 支持，提升传输效率\n' +
        '    enabled: true\n' +
        '  undertow:\n' +
        '    threads:\n' +
        '      # 工作线程数，默认设置为io-threads * 8。如果你的应用程序有很多同步阻塞操作，可以适当增加这个值\n' +
        '      worker: 8\n' +
        '      # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个线程\n' +
        '      # 阻塞任务线程池, 当执行类似servlet请求阻塞操作, undertow会从这个线程池中取得线程,它的值设置取决于系统的负载\n' +
        '      io: 4\n' +
        '    # 设置为true以使用直接内存（堆外内存）来存储缓冲区。这可以减少垃圾回收的开销。\n' +
        '    direct-buffers: true\n' +
        '    # 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作,有点类似netty的池化内存管理\n' +
        '    # 每块buffer的空间大小,越小的空间被利用越充分\n' +
        '    buffer-size: 1024\n' +
        '    accesslog:\n' +
        '      # 关闭日志\n' +
        '      enabled: false\n' +
        '  compression:\n' +
        '    # 启用 GZIP 压缩，减少传输数据量\n' +
        '    enabled: true\n' +
        '    # 需要压缩的 MIME 类型\n' +
        '    mime-types: text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\n' +
        '    # 最小压缩响应大小（字节）\n' +
        '    min-response-size: 1024\n' +
        '  # 启用优雅停机，并遵守spring.lifecycle.timeout-per-shutdown-phase属性中给出的超时\n' +
        '  # GRACEFUL (优雅): 当应用程序以"GRACEFUL"模式关闭时，它不会接受新的请求且会尝试完成所有正在进行的请求和处理，然后才会终止。这种方式适用于那些可能需要一些时间来清理资源或完成正在进行的任务的场景。\n' +
        '  # IMMEDIATE(立即): 当应用程序以"IMMEDIATE"模式关闭时，它会立即终止，而不管当前是否有任何活动任务或请求。这种方式适用于那些可以立即停止而不会造成严重问题的情况。\n' +
        '  shutdown: graceful\n',
      apiData: [
        { name: 'content', remark: '内容', type: 'string', defaultValue: '-', required: '是' },
        { name: 'height', remark: '编辑框高度', type: 'string', defaultValue: '500px', required: '否' },
        { name: 'readonly', remark: '是否只读', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'mode', remark: '语法模式。支持：yaml | java | go | swift | dockerfile | groovy | lua | perl | python | ruby | sql | xml | vue', type: 'string', defaultValue: 'yaml', required: '否' },
        { name: 'change', remark: '内容变化回调', type: '(value) => {}', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    onJavaChange(value) {
      console.log('onJavaChange', value)
    },
    onYamlChange(value) {
      console.log('onYamlChange', value)
    }
  }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
