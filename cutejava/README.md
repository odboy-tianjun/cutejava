<h1 style="text-align: center">CuteJava 后台管理系统</h1>

#### 项目简介

一个基于 Spring Boot 2.7.18 、 Mybatis-Plus、 JWT、Spring Security、Redis、Vue2的前后端分离的后台管理系统

**账号密码：** `admin / 123456`

#### 项目源码

https://github.com/odboy-tianjun/cutejava

#### 编码规范

- 和数据库表一一对应的类, 以`Tb`结尾
- 前端请求参数类名, 以`Args`结尾
- 返回给前端的数据类名, 以`Vo`结尾
- 事件驱动类名，以`Event`结尾
- 消息队列/Socket传输类名，以`Message`结尾
- 原框架以外的所有辅助类的类名, 以`Cs`开头
- 查询分页、查询列表的方法，一律为同一个方法名
    - 查询分页：queryUserListByArgs(args, page)
    - 查询列表：queryUserListByArgs(args)
    - MapperXml：这样mapper.xml文件中的实现方法只需要写一个queryUserListByArgs
- Mapper层查询方法，统一使用以下命名规则
    - 分页、列表：query{TableName}ListByArgs
    - 精确查询：get{TableName}{By FieldName}
    - 统计数量：get{具体语义}Count{By FieldName}
- 枚举类名，以`Enum`结尾

#### 主要特性

- 使用最新技术栈，社区资源丰富。
- 高效率开发，代码生成器可一键生成前后端代码
- 支持数据字典，可方便地对一些状态进行管理
- 支持接口限流，避免恶意请求导致服务层压力过大
- 支持接口级别的功能权限与数据权限，可自定义操作
- 自定义权限注解与匿名接口注解，可快速对接口拦截与放行
- 对一些常用地前端组件封装：表格数据请求、数据字典等
- 前后端统一异常拦截处理，统一输出异常，避免繁琐的判断
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

#### 系统功能

- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位
- 字典管理：可维护常用一些固定的数据，如：状态，性别等
- 系统日志：记录用户操作日志与异常日志，方便开发人员定位排错
- SQL监控：采用druid 监控数据库访问性能，默认用户名admin，密码123456
- 定时任务：整合Quartz做定时任务，加入任务日志，任务运行情况一目了然
- 邮件工具：配合富文本，发送html格式的邮件
- 七牛云存储：可同步七牛云存储的数据到系统，无需登录七牛云直接操作云数据
- 服务监控：监控服务器的负载情况

#### 特别鸣谢

- 感谢 [PanJiaChen](https://github.com/PanJiaChen/vue-element-admin) 大佬提供的前端模板

- 感谢 [Moxun](https://github.com/moxun1639) 大佬提供的前端 Curd 通用组件

- 感谢 [zhy6599](https://gitee.com/zhy6599) 大佬提供的后端运维管理相关功能

- 感谢 [j.yao.SUSE](https://github.com/everhopingandwaiting) 大佬提供的匿名接口与Redis限流等功能
