<template>
  <div class="app-container">
    <!--    <cute-java-editor :content="content" @change="onChange" />-->
    <cute-code-editor :content="content" mode="java" @change="onChange" />
  </div>
</template>

<script>
// 旧组件
// import CuteYamlEditor from '@/views/components/dev/CuteYamlEditor'
// 新组件
import CuteCodeEditor from '@/views/components/dev/CuteCodeEditor'

export default {
  name: 'CuteJavaEditorDemo',
  // components: { CuteJavaEditor },
  components: { CuteCodeEditor },
  data() {
    return {
      content: 'package cn.odboy;\n' +
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
        '}\n'
    }
  },
  methods: {
    onChange(value) {
      console.log('onChange', value)
    }
  }
}
</script>

