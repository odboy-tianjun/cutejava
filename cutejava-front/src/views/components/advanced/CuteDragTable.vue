<!--
 * 拖拽表格组件：封装自定义逻辑与参数，表格支持拖拽排序
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <div>
    <el-row>
      <el-col v-if="showSelect" :span="2" class="selection-box">
        已选 <font class="selection-count">{{ crud.selection && crud.selection.length }}</font> 条
      </el-col>
      <el-col :span="showSelect ? 11 : 12" style="text-align: left">
        <!-- 通过  <template v-slot:batchArea> 插槽，数据表格上方左侧 添加 批量操作入口 -->
        <slot name="batchArea" />
      </el-col>
      <el-col :span="showSelect ? 11 : 12" style="text-align: left">
        <!-- 通过  <template v-slot:toolArea> 插槽，数据表格上方右侧 添加 工具入口，比如导出 -->
        <slot name="toolArea" />
      </el-col>
    </el-row>
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
      highlight-current-row
      highlight-selection-row
      @sort-change="onTableSortChange"
      @selection-change="onTableSelectionChange"
    >
      <el-table-column v-if="showSelect" type="selection" width="55" />
      <slot />
    </el-table>
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
  </div>
</template>

<script>
import Sortable from 'sortablejs'

export default {
  name: 'CuteDragTable',
  props: {
    primaryKey: {
      type: String,
      required: true,
      default: 'id'
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
    showSelect: {
      type: Boolean,
      required: false,
      default: false
    },
    paging: {
      type: Boolean,
      required: false,
      default: true
    },
    pageProps: {
      type: Object,
      required: false,
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
          this.$emit('page-change', currentPage, pageSize)
          this.refresh()
        },
        orderBy: '',
        selection: []
      }
    }
  },
  computed: {
    height() {
      return document.documentElement.clientHeight - 300
    }
  },
  mounted() {
    this.refresh()
  },
  methods: {
    refresh() {
      this.crud.orderBy = ''
      this.crud.selection = []
      this.initData()
      this.initDragTable()
    },
    async initData() {
      let params = {}
      // 自定义请求参数
      if (this.paramsTransform) {
        params = this.paramsTransform(this.pageProps)
      }
      // 排序字段
      if (this.crud.orderBy) {
        params.orderBy = this.crud.orderBy
      }
      if (this.fetch) {
        try {
          this.crud.loading = true
          // 请求远程数据
          let transformData = await this.fetch(params)
          // 转换为表格所需要的数据
          if (this.responseTransform) {
            transformData = this.responseTransform(transformData)
          }
          // if (response && response.hasOwnProperty('totalElements') && response.hasOwnProperty('content')) {
          this.pageProps.total = transformData.totalElements
          this.crud.dataSource = transformData.content
        } catch (e) {
          console.error('CuteDragTable请求远程数据异常', e)
          this.pageProps.total = 0
          this.crud.dataSource = []
        } finally {
          this.crud.loading = false
        }
      }
    },
    onTableSelectionChange(selection) {
      this.crud.selection = selection
      this.$emit('selection-change', selection)
    },
    getDataSource() {
      return this.crud.dataSource
    },
    onTableSortChange(sortProps) {
      const prop = sortProps.prop
      const order = sortProps.order
      let result
      if (order === 'ascending') {
        result = { column: prop, orderType: 'asc' }
      } else if (order === 'descending') {
        result = { column: prop, orderType: 'desc' }
      } else {
        result = { column: prop, orderType: 'desc' }
      }
      this.$emit('order-change', result.column, result.orderType)
      this.crud.orderBy = result
      this.refresh()
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
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/variables";

.selection-box {
  text-align: left;
  font-size: 12px;
  padding-left: 10px;
  min-height: 26.7px;
  max-height: 26.7px;
  padding-top: 7px;
}

.selection-count {
  color: $menuActiveText;
}
</style>
