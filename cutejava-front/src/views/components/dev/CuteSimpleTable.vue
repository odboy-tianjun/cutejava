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
      height="450"
      max-height="450"
      @selection-change="onTableSelectionChange"
    >
      <el-table-column v-if="mode === 'multi'" type="selection" width="55" />
      <el-table-column
        v-for="(column, index) in crud.columns"
        :key="index"
        :label="column.label"
        :prop="column.prop"
        :formatter="column.formatter ? column.formatter : null"
      />
      <el-table-column
        v-if="crud.operateColumn"
        :width="crud.operateColumn.width"
        label="操作"
        :formatter="crud.operateColumn.formatter"
        :fixed="crud.operateColumn.fixed"
      />
    </el-table>
    <el-pagination
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

export default {
  name: 'CuteSimpleTable',
  props: {
    primaryKey: {
      type: String,
      required: true,
      default: null
    },
    columns: {
      type: Array,
      required: true,
      default: null
    },
    operateColumn: {
      type: Object,
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
        columns: this.columns && this.columns.length > 0 ? this.columns : [],
        operateColumn: this.operateColumn,
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
  mounted() {
    this.initData()
  },
  methods: {
    refresh() {
      this.initData()
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
    }
  }
}
</script>

