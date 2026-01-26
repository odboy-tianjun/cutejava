# cutejava-ui

## 前端模板

初始模板基于： [https://github.com/PanJiaChen/vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)

模板文档： [https://panjiachen.github.io/vue-element-admin-site/zh/guide/](https://panjiachen.github.io/vue-element-admin-site/zh/guide/)

## 快速开始

#### 环境要求

**推荐 node 版本：16**

#### 安装依赖

``` bash
# 安装依赖（依赖python）
npm install --registry https://registry.npmmirror.com
```

#### 启动服务

```bash
# 启动服务 localhost:8013
npm run dev

# (或) 启动服务 localhost:8013
IDEA -> Current File -> Edit Configurations... -> Add New Configuration -> npm -> Script选dev -> Apply -> Ok

# 构建生产环境
npm run build:prod

# 本地预览生产环境
npm run preview
```

## 命令说明

#### 开发相关

```shell
# 启动本地开发服务器，默认端口 8013，支持热重载
npm run dev 

# Windows 系统启动开发服务器，设置 OpenSSL 兼容性选项
npm run dev-win

# Linux 系统启动开发服务器，设置 OpenSSL 兼容性选项
npm run dev-linux
```

#### 构建相关

```shell
# 构建开发环境版本，输出到 dist 目录
npm run build:dev

# 构建预发布环境版本，输出到 dist 目录
npm run build:stage

# 构建生产环境版本，输出到 dist 目录
npm run build:prod

# 构建生产环境版本（无特定模式）
npm run build:production
```

#### 辅助命令

```shell
# 预览构建后的静态文件，启动本地服务器查看构建结果
npm run preview

# 检查 src 目录下 .js 和 .vue 文件的代码规范
npm run lint

# 运行单元测试，先清除缓存再执行测试
npm run test:unit

# 优化 src/assets/icons/svg 目录下的 SVG 文件
npm run svgo

# 使用 plop 创建新文件模板
npm run new
```

## 常见问题

1、linux 系统在安装依赖的时候会出现 node-sass 无法安装的问题

解决方案：

```
1. 单独安装：npm install --unsafe-perm node-sass 
2. 直接使用：npm install --unsafe-perm
```

2、加速node-sass安装

https://www.ydyno.com/archives/1219.html
