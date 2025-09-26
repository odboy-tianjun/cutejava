<template>
  <div class="app-container">
    <!--    <cute-yaml-editor :content="content" @change="onChange" />-->
    <cute-code-editor :content="content" mode="yaml" @change="onChange" />
  </div>
</template>

<script>
// 旧组件
// import CuteYamlEditor from '@/views/components/dev/CuteYamlEditor'
// 新组件
import CuteCodeEditor from '@/views/components/dev/CuteCodeEditor'

export default {
  name: 'CuteYamlEditorDemo',
  // components: { CuteYamlEditor },
  components: { CuteCodeEditor },
  data() {
    return {
      content: 'server:\n' +
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
        '  shutdown: graceful\n' +
        'spring:\n' +
        '  application:\n' +
        '    name: cutejava\n' +
        '    title: CuteJava\n' +
        '  lifecycle:\n' +
        '    # 采用java.time.Duration格式的值,如果在这个时间内，优雅停机没有停掉应用，超过这个时间就会强制停止应用\n' +
        '    timeout-per-shutdown-phase: 30s\n' +
        '  freemarker:\n' +
        '    # 是否检查模板位置是否存在\n' +
        '    check-template-location: false\n' +
        '    # 模板编码\n' +
        '    charset: utf-8\n' +
        '  cache:\n' +
        '    # 解决缓存异常 https://blog.csdn.net/jikeyeka/article/details/125804049\n' +
        '    type: redis\n' +
        '  data:\n' +
        '    redis:\n' +
        '      repositories:\n' +
        '        # 是否启用Redis存储(关闭防止出现 Multiple Spring Data modules found, entering strict repository configuration mode)\n' +
        '        enabled: false\n' +
        '  datasource:\n' +
        '    druid:\n' +
        '      db-type: com.alibaba.druid.pool.DruidDataSource\n' +
        '      driverClassName: com.p6spy.engine.spy.P6SpyDriver\n' +
        '      url: jdbc:p6spy:mysql://192.168.100.128:23306/cutejava?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\n' +
        '      username: root\n' +
        '      password: lm,101208..,.\n' +
        '      # 初始连接数，建议设置为与最小空闲连接数相同\n' +
        '      initial-size: 2\n' +
        '      # 最小空闲连接数，保持足够的空闲连接以应对请求\n' +
        '      min-idle: 2\n' +
        '      # 最大连接数，根据并发需求适当增加\n' +
        '      max-active: 8\n' +
        '      # 获取连接超时时间（毫秒），调整以满足响应时间要求\n' +
        '      max-wait: 3000\n' +
        '      # 启用KeepAlive机制，保持长连接\n' +
        '      keep-alive: true\n' +
        '      # 连接有效性检测间隔时间（毫秒），定期检查连接的健康状态\n' +
        '      time-between-eviction-runs-millis: 60000\n' +
        '      # 连接在池中最小生存时间（毫秒），确保连接在池中至少存在一段时间\n' +
        '      min-evictable-idle-time-millis: 300000\n' +
        '      # 连接在池中最大生存时间（毫秒），防止连接在池中停留过长\n' +
        '      max-evictable-idle-time-millis: 900000\n' +
        '      # 指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除\n' +
        '      test-while-idle: true\n' +
        '      # 指明是否在从池中取出连接前进行检验,如果检验失败, 则从池中去除连接并尝试取出另一个\n' +
        '      test-on-borrow: true\n' +
        '      # 是否在归还到池中前进行检验\n' +
        '      test-on-return: false\n' +
        '      # 停用 com_ping 探活机制\n' +
        '      use-ping-method: false\n' +
        '      # 检测连接是否有效\n' +
        '      validation-query: SELECT 1\n' +
        '      # 配置监控统计\n' +
        '      web-stat-filter:\n' +
        '        enabled: true\n' +
        '        url-pattern: /*\n' +
        '        # 开启session统计功能\n' +
        '        session-stat-enable: true\n' +
        '        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"\n' +
        '        # 缺省sessionStatMaxCount是1000个\n' +
        '        session-stat-max-count: 1000\n' +
        '      stat-view-servlet:\n' +
        '        enabled: true\n' +
        '        url-pattern: /druid/*\n' +
        '        reset-enable: false\n' +
        '      filter:\n' +
        '        stat:\n' +
        '          enabled: true\n' +
        '          # 记录慢SQL\n' +
        '          log-slow-sql: true\n' +
        '          slow-sql-millis: 2000\n' +
        '          merge-sql: true\n' +
        '        wall:\n' +
        '          config:\n' +
        '            multi-statement-allow: true\n' +
        '      aop-patterns: "cn.odboy.mapper.*,cn.odboy.service.*,cn.odboy.*.mapper.*,cn.odboy.*.service.*"\n' +
        '  redis:\n' +
        '    database: ${REDIS_DB:1}\n' +
        '    host: ${REDIS_HOST:192.168.100.128}\n' +
        '    port: ${REDIS_PORT:26379}\n' +
        '    password: ${REDIS_PWD:lm,101208..,.}\n' +
        '    # 连接超时时间\n' +
        '    timeout: 5000\n' +
        '    # 连接池配置\n' +
        '    lettuce:\n' +
        '      pool:\n' +
        '        # 连接池最大连接数\n' +
        '        max-active: 30\n' +
        '        # 连接池最大阻塞等待时间（毫秒），负值表示没有限制\n' +
        '        max-wait: -1\n' +
        '        # 连接池中的最大空闲连接数\n' +
        '        max-idle: 20\n' +
        '        # 连接池中的最小空闲连接数\n' +
        '        min-idle: 1\n' +
        'mybatis-plus:\n' +
        '  configuration:\n' +
        '    # 开启 Mybatis 二级缓存，默认为 true\n' +
        '    cache-enabled: false\n' +
        '    # 设置本地缓存作用域, Mybatis 一级缓存, 默认为 SESSION\n' +
        '    # 同一个 session 相同查询语句不会再次查询数据库\n' +
        '    # 微服务中, 建议设置为STATEMENT, 即关闭一级缓存\n' +
        '    local-cache-scope: STATEMENT\n' +
        '    # 是否开启自动驼峰命名规则（camel case）映射, 即从经典数据库列名 A_COLUMN（下划线命名） 到经典 Java 属性名 aColumn（驼峰命名） 的类似映射\n' +
        '    map-underscore-to-camel-case: true\n' +
        '    # Sql日志\n' +
        '    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n' +
        '  # 启动时是否检查 MyBatis XML 文件的存在, 默认不检查\n' +
        '  check-config-location: true\n' +
        '  # MyBatis Mapper 所对应的 XML 文件位置\n' +
        '  # Maven 多模块项目的扫描路径需以 classpath*: 开头 （即加载多个 jar 包下的 XML 文件）\n' +
        '  mapper-locations: classpath*:/mapper/**/*.xml\n' +
        '  # MyBatis-Plus 全局策略中的 DB 策略配置\n' +
        '  global-config:\n' +
        '    db-config:\n' +
        '      # 逻辑已删除值(逻辑删除下有效)\n' +
        '      logic-delete-value: 0\n' +
        '      # 逻辑未删除值(逻辑删除下有效)\n' +
        '      logic-not-delete-value: 1\n' +
        '      # 全局默认主键类型, 这里为自增主键\n' +
        '      id-type: auto\n' +
        '      # 表名是否使用驼峰转下划线命名,只对表名生效\n' +
        '      table-underline: true\n' +
        '    # 是否控制台 print mybatisplus-plus 的 LOGO\n' +
        '    banner: true\n' +
        'app:\n' +
        '  # 密码加密传输，前端公钥加密，后端私钥解密\n' +
        '  rsa:\n' +
        '    private-key: MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\n' +
        '  # 鉴权\n' +
        '  jwt:\n' +
        '    # 必须使用最少88位的Base64对该令牌进行编码\n' +
        '    base64-secret: ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\n' +
        '    # 令牌过期时间 此处单位/毫秒 ，默认4小时，可在此网站生成 https://www.convertworld.com/zh-hans/time/milliseconds.html\n' +
        '    token-validity-in-seconds: 14400000\n' +
        '    # token 续期检查时间范围（默认30分钟，单位毫秒），在token即将过期的一段时间内用户操作了，则给用户的token续期\n' +
        '    detect: 1800000\n' +
        '    # 续期时间范围，默认1小时，单位毫秒\n' +
        '    renew: 3600000\n' +
        '  # 登录\n' +
        '  login:\n' +
        '    # 是否限制单用户登录\n' +
        '    single: false\n' +
        '    # 验证码\n' +
        '    captchaSetting:\n' +
        '      # 验证码类型配置 查看 LoginProperties 类\n' +
        '      code-type: arithmetic\n' +
        '      # 登录图形验证码有效时间/分钟\n' +
        '      expiration: 2\n' +
        '      # 验证码高度\n' +
        '      width: 111\n' +
        '      # 验证码宽度\n' +
        '      height: 36\n' +
        '      # 内容长度\n' +
        '      length: 2\n' +
        '      # 字体名称，为空则使用默认字体\n' +
        '      font-name:\n' +
        '      # 字体大小\n' +
        '      font-size: 25\n' +
        '  # 是否开启 swagger-ui\n' +
        '  swagger:\n' +
        '    enabled: true\n' +
        '  # 针对@Async\n' +
        '  async-task-pool:\n' +
        '    # 核心线程池大小\n' +
        '    core-pool-size: 10\n' +
        '    # 最大线程数\n' +
        '    max-pool-size: 30\n' +
        '    # 活跃时间\n' +
        '    keep-alive-seconds: 60\n' +
        '    # 队列容量\n' +
        '    queue-capacity: 50\n' +
        '  pipeline-task-pool:\n' +
        '    # 核心线程池大小\n' +
        '    core-pool-size: 10\n' +
        '    # 最大线程数\n' +
        '    max-pool-size: 200\n' +
        '    # 活跃时间\n' +
        '    keep-alive-seconds: 60\n' +
        '    # 队列容量\n' +
        '    queue-capacity: 300\n' +
        '  # oss\n' +
        '  oss:\n' +
        '    max-size: 100\n' +
        '    minio:\n' +
        '      endpoint: http://192.168.100.128:9000\n' +
        '      bucketName: cutejava\n' +
        '      accessKey: 8o5oVzqvKby5TzaGcfmx\n' +
        '      secretKey: 8BiK5UbmvXGJNVQ98CGiohHzT1N1FMRZqHRdJllj\n' +
        '  captcha:\n' +
        '    expireTime: 300\n' +
        '## 启用debug\n' +
        '#debug: true\n' +
        '#logging:\n' +
        '#  level:\n' +
        '#    root: debug'
    }
  },
  methods: {
    onChange(value) {
      console.log('onChange', value)
    }
  }
}
</script>

