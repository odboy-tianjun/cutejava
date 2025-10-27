<!--
 * 拖拽表格组件：封装自定义逻辑与参数，表格支持拖拽排序
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <div>
    <el-table
      ref="table"
      v-loading="crud.loading"
      stripe
      empty-text="暂无数据"
      :row-key="crud.primaryKey"
      fit
      :data="crud.dataSource"
      style="width: 100%;"
      :height="height"
      :max-height="height"
      @selection-change="onTableSelectionChange"
    >
      <el-table-column v-if="mode === 'multi'" type="selection" width="55" />
      <slot />
      <el-pagination
        v-if="paging"
        :current-page="pageProps.current"
        :page-sizes="[10, 20, 50]"
        :page-size="pageProps.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="pageProps.total"
        @size-change="(size) => crud.onPageChange(pageProps.current, size, pageProps.total)"
        @current-change="(current) => crud.onPageChange(current, pageProps.pageSize, pageProps.total)"
      />
    </el-table></div>
</template>

<script>
import Sortable from 'sortablejs'

export default {
  name: 'CuteDragTable',
  props: {
    primaryKey: {
      type: String,
      required: true,
      default: null
    },
    fetch: {
      type: Function,
      required: false,
      default: null
    },
    paramsTransform: {
      type: Function,
      required: false,
      default: null
    },
    responseTransform: {
      type: Function,
      required: false,
      default: null
    },
    mode: {
      type: String,
      required: false,
      default: 'none'
    },
    paging: {
      type: Boolean,
      required: false,
      default: true
    },
    pageProps: {
      type: Object,
      required: true,
      default: function() {
        return {
          current: 1,
          pageSize: 10,
          total: 0
        }
      }
    }
  },
  data() {
    return {
      crud: {
        loading: false,
        primaryKey: this.primaryKey ? this.primaryKey : 'id',
        fetchUrl: null,
        dataSource: [],
        onPageChange: (currentPage, pageSize) => {
          this.pageProps.current = currentPage
          this.pageProps.pageSize = pageSize
          console.log('on-page-change', currentPage, pageSize)
          // this.$emit('onPageChange', currentPage, pageSize, total)
          this.refresh()
        }
      }
    }
  },
  computed: {
    height() {
      return document.documentElement.clientHeight - 300
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    refresh() {
      this.initData()
      this.initDragTable()
    },
    async initData() {
      let params = {}
      // 自定义请求参数
      if (this.paramsTransform) {
        params = this.paramsTransform(this.pageProps)
      }
      if (this.fetch) {
        try {
          this.crud.loading = true
          // 请求远程数据
          const response = await this.fetch(params)
          // console.error('response', response)
          // { content: [], totalElements: 0 }
          // 转换为表格所需要的数据
          if (this.responseTransform) {
            return this.responseTransform(response)
          }
          if (response && response.hasOwnProperty('totalElements') && response.hasOwnProperty('content')) {
            this.pageProps.total = response.totalElements
            this.crud.dataSource = response.content
          }
          return response
        } finally {
          this.crud.loading = false
        }
      }
    },
    onTableSelectionChange(selection) {
      this.$emit('selection-change', selection)
    },
    initDragTable() {
      const that = this
      const table = that.$refs.table.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      Sortable.create(table, {
        animation: 1000,
        onEnd({ newIndex, oldIndex }) {
          // 拖拽排序数据
          that.crud.dataSource.splice(newIndex, 0, that.crud.dataSource.splice(oldIndex, 1)[0])
          const newArray = that.crud.dataSource.slice(0)
          that.crud.dataSource = [] // 必须有此步骤，不然拖拽后回弹
          that.$nextTick(function() {
            that.crud.dataSource = newArray // 重新赋值，用新数据来刷新视图
            this.updateOrderNum(that.crud.dataSource)// 更改列表中的序号，使序号1.2.3.4.....显示，不然就是行拖拽后乱序显示如:3.2.4.1...
          })
        }
      })
    },
    updateOrderNum(data) {
      data.forEach((item, index) => {
        item.orderNum = index + 1
      })
    },
    getDataSource() {
      return this.crud.dataSource
    }
  }
}
</script>

