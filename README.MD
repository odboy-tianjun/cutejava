<h1 style="text-align: center">CuteJava 后台管理系统</h1>

#### 项目简介

一个基于 Spring Boot 2.7.18 、 Mybatis-Plus、 JWT、Spring Security、Redis、Vue2的前后端分离的后台管理系统

**账号密码：** `admin / 123456`

#### 项目源码

https://github.com/odboy-tianjun/cutejava

#### 编码规范

- 和数据库表一一对应的类, 以`Tb`结尾
- API请求参数类名, 以`Args`结尾
- API响应参数类名, 以`Vo`结尾
- 事件驱动类名, 以`Event`结尾
- 消息队列/Socket传输类名，以`Message`结尾
- 常用的辅助类的类名, 以`Cs`开头
- 查询分页、查询列表的方法，一律为同一个方法名
    - 查询分页：queryUser(args, page)
    - 查询列表：queryUser(args)
    - MapperXml：这样mapper.xml文件中的实现方法只需要写一个queryUser
- 枚举类名，以`Enum`结尾
- 拒绝过度包装

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
- SQL监控：采用druid 监控数据库访问性能，默认用户名admin，密码123456
- 定时任务：整合Quartz做定时任务，加入任务日志，任务运行情况一目了然
- 邮件工具：配合富文本，发送html格式的邮件
- 存储管理：本地存储、Minio存储
- 服务监控：监控服务器的负载情况

#### 前端基础组件

> 组件所在路径：cutejava-front/src/views/components/dev

- CuteSimpleTable 简单表格
- CuteDragTable 支持拖拽的表格
- CuteFormDialog 支持表单的对话框
- CuteFormDrawer 支持表单的抽屉
- CuteRichTextEditor 富文本编辑器
- CuteOneKeyCopy 一键复制
- CuteMarkdown Markdown编辑器
- CuteJavaEditor Java编辑器（已弃用）
- CuteYamlEditor Yaml编辑器（已弃用）
- CuteCodeEditor 多语言编辑器（支持yaml、java、go、swift、dockerfile、groovy、lua、perl、python、ruby、sql、xml、vue）

#### 前端业务组件

> 组件所在路径：cutejava-front/src/views/components/business

- CuteDeptTree 部门树
- CuteUserSelect 用户多选框
- CuteFileUpload 上传文件到OSS, 必须结合Minio使用（上传完成后必须销毁组件）
- CuteFileDragUpload 支持拖拽上传文件到OSS, 必须结合Minio使用（上传完成后必须销毁组件）

#### 系统模块

- cutejava-framework：系统基础框架。提供核心功能，如：权限、数据权限、异常处理、缓存、工具类、接口限流、接口防重、接口幂等。
- cutejava-system：系统功能模块。提供用户、角色、菜单、部门、岗位、字典、定时任务、邮件、Minio存储、服务监控等功能
- cutejava-devops：devops模块，已实现流水线功能。参考阿里云云效，提供流水线功能，可配置流水线任务，并支持任务依赖、任务参数、任务参数校验、任务参数回填。

#### 特别鸣谢

- 感谢 [elunez](https://github.com/elunez/eladmin-mp) 大佬提供的基础框架
