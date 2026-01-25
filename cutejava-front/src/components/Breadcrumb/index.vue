<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.redirect==='noredirect'||index==levelList.length-1" class="no-redirect">{{
          item.meta.title
        }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route(route) {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith('/redirect/')) {
        return
      }
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      // only show routes with meta.title
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]

      if (!this.isDashboard(first)) {
        matched = [{ path: '/dashboard', meta: { title: '首页' }}].concat(matched)
      }

      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
    },
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/variables";
@import "~@/assets/styles/variables.technology3";

.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  ::v-deep .el-breadcrumb__item {
    .el-breadcrumb__inner {
      color: #FFFFFF !important;
      font-weight: 500;
      transition: all 0.3s ease;

      &.is-link {
        color: #FFFFFF !important;
        text-shadow: 0 0 4px rgba(102, 255, 204, 0.3);

        &:hover {
          color: $menuActiveText !important;
          filter: drop-shadow(0 0 6px rgba(102, 255, 204, 0.4));
        }
      }

      &:hover {
        color: $menuActiveText !important;
        filter: drop-shadow(0 0 6px rgba(102, 255, 204, 0.4));
      }
    }

    .el-breadcrumb__separator {
      color: rgba(102, 255, 204, 0.5);
    }
  }

  a {
    color: #FFFFFF !important;
    font-weight: 500;
    text-shadow: 0 0 4px rgba(102, 255, 204, 0.3);

    &:hover {
      color: $menuActiveText !important;
      filter: drop-shadow(0 0 6px rgba(102, 255, 204, 0.4));
    }
  }

  .no-redirect {
    color: #FFFFFF !important;
    cursor: text;
    font-weight: 500;
    text-shadow: 0 0 4px rgba(102, 255, 204, 0.3);
  }
}
</style>
