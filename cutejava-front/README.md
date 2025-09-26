# cutejava-ui

#### 前端模板

初始模板基于： [https://github.com/PanJiaChen/vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)

模板文档： [https://panjiachen.github.io/vue-element-admin-site/zh/guide/](https://panjiachen.github.io/vue-element-admin-site/zh/guide/)

## 快速开始

### 环境要求

**推荐 node 版本：12-16 LTS**

### 安装依赖

``` bash
# 安装依赖（依赖python）
npm install --registry https://registry.npmmirror.com
```

### 启动服务

```bash
# 启动服务 localhost:8013
npm run dev

# (或) 启动服务 localhost:8013
IDEA -> Current File -> Edit Configurations... -> Add New Configuration -> npm -> Script选dev -> Apply -> Ok

# 构建生产环境
npm run build:prod
```

#### 常见问题

1、linux 系统在安装依赖的时候会出现 node-sass 无法安装的问题

解决方案：
```
1. 单独安装：npm install --unsafe-perm node-sass 
2. 直接使用：npm install --unsafe-perm
```

2、加速node-sass安装

https://www.ydyno.com/archives/1219.html

#### 特别鸣谢

- 感谢 [elunez](https://github.com/elunez/eladmin-mp) 大佬提供的基础框架
