<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>展示结构化数据列表，并支持排序、分页</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <!-- 表格渲染 -->
    <!-- fetch: 获取远程数据 -->
    <!-- params-transform: 组装查询参数 -->
    <!-- responseTransform: 组装表格对象。对响应结果做处理 -->
    <!-- primary-key: 主键 -->
    <!-- showSelect: 是否多选。不传默认false，传true为多选 -->
    <!-- @selection-change: 当且仅当mode为multi时启用 -->
    <!-- @order-change: 当排序字段被单击，el-table-column 需添加 sortable="custom" 属性 -->
    <cute-simple-table
      ref="instance"
      :fetch="curd.fetch"
      :params-transform="curd.paramsTransform"
      primary-key="id"
      :page-props="curd.pageProps"
      show-select
    >
      <template v-slot:batchArea>
        <cute-button type="primary" @click="refreshData">批量绑定</cute-button>
      </template>
      <template v-slot:toolArea>
        <cute-button type="primary" @click="refreshData">导出</cute-button>
      </template>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" sortable="custom" :formatter="FormatRowDateTimeStr" />
      <el-table-column prop="createBy" label="创建人" />
      <el-table-column label="操作" width="150" min-width="150" fixed="right">
        <template v-slot="scope">
          <div>
            <el-button type="text" @click="onTableEditClick(scope.row)">编辑</el-button>
            <el-button type="text" @click="onTableDeleteClick(scope.row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </cute-simple-table>
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
    <h4>方法</h4>
    <el-table :data="methodData">
      <el-table-column prop="name" label="函数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="inputArgs" label="入参" />
      <el-table-column prop="outArgs" label="出参" />
    </el-table>
  </div>
</template>
<script>
import DictService from '@/api/system/dict'
import CuteButton from '@/views/components/dev/CuteButton.vue'
import CuteSimpleTable from '@/views/components/advanced/CuteSimpleTable.vue'
import { FormatRowDateTimeStr } from '@/utils/KitUtil'

export default {
  name: 'CuteSimpleTableDemo',
  components: { CuteSimpleTable, CuteButton },
  data() {
    return {
      form: {
        model: ''
      },
      curd: {
        paramsTransform: (pageProps) => {
          const values = this.form.model
          return {
            page: pageProps.current,
            size: pageProps.pageSize,
            args: {
              ...values
            }
          }
        },
        fetch: async(queryParams) => {
          return DictService.searchDict(queryParams)
        },
        columns: [
          { prop: 'name', label: '名称' },
          { prop: 'description', label: '描述' },
          { prop: 'createTime', label: '创建时间', formatter: FormatRowDateTimeStr },
          { prop: 'createBy', label: '创建人' }
        ],
        pageProps: {
          current: 1,
          pageSize: 10,
          total: 0
        }
      },
      apiData: [
        { name: 'primary-key', remark: '对象中的哪个字段设置为主键', type: 'string', defaultValue: 'id', required: '否' },
        { name: 'fetch', remark: '请求数据的API接口', type: 'async(queryParams) => {}', defaultValue: '-', required: '否' },
        { name: 'paramsTransform', remark: '组装请求参数的方法', type: '(pageProps) => {}', defaultValue: '-', required: '否' },
        { name: 'responseTransform', remark: '组装响应结果的方法', type: '(response) => {}', defaultValue: '-', required: '否' },
        { name: 'showSelect', remark: '是否支持多选', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'paging', remark: '是否显示分页组件', type: 'boolean', defaultValue: 'true', required: '否' },
        { name: 'pageProps', remark: '分页参数', type: 'object', defaultValue: '{current: 1,pageSize: 10,total: 0}', required: '否' },
        { name: 'customHeight', remark: '自定义高度', type: 'number', defaultValue: 'null', required: '否' },
        { name: 'selection-change', remark: '选中列表项发生改变的时候触发的回调', type: '(array) => {}', defaultValue: '-', required: '否' },
        { name: 'order-change', remark: '排序列发生改变的时候触发的回调。el-table-column 需添加 sortable="custom" 属性', type: '(column, orderType) => {}', defaultValue: '-', required: '否' },
        { name: 'page-change', remark: '分页数据发生改变的时候触发的回调', type: '(currentPage, pageSize) => {}', defaultValue: '-', required: '否' }
      ],
      methodData: [
        { name: 'getDataSource', remark: '获取当前表格的数据', inputArgs: '-', outArgs: 'array' },
        { name: 'refresh', remark: '刷新当前表格的数据，可以理解为重新执行查询方法。', inputArgs: '-', outArgs: '-' }
      ]
    }
  },
  mounted() {
    this.refreshData()
  },
  methods: {
    FormatRowDateTimeStr,
    refreshData() {
      this.$refs.instance.refresh()
    },
    onTableEditClick(row) {
      console.log('onTableEditClick', row)
    },
    onTableDeleteClick(row) {
      console.log('onTableDeleteClick', row)
    }
  }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
