<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div class="head-container">
      <el-form
        ref="searchForm"
        :inline="true"
        :model="form.model"
        label-position="right"
        label-width="80px"
        :rules="form.rules"
      >
        <el-form-item label="模糊查询" prop="blurry">
          <el-input v-model="form.model.blurry" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onFormSubmit('searchForm')">查询</el-button>
          <el-button @click="onFormReset('searchForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 表格渲染 -->
    <!-- fetch: 获取远程数据 -->
    <!-- params-transform: 组装查询参数 -->
    <!-- responseTransform: 组装表格对象。对响应结果做处理 -->
    <!-- primary-key: 主键 -->
    <!-- data-source: 数据源。当data-source不为空时，以data-source的数据为主 -->
    <!-- mode: 模式。不传默认，传multi为多选 -->
    <!-- @selection-change: 当且仅当mode为multi时启用 -->
    <cute-drag-table
      ref="instance"
      :fetch="curd.fetch"
      :params-transform="curd.paramsTransform"
      primary-key="id"
      :page-props="curd.pageProps"
    >
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" :formatter="FormatRowDateTimeStr" />
      <el-table-column prop="createBy" label="创建人" />
      <el-table-column label="操作" width="150" min-width="150" fixed="right">
        <template slot-scope="scope">
          <div>
            <el-button type="text" @click="onTableEditClick(scope.row)">编辑</el-button>
            <el-button type="text" @click="onTableDeleteClick(scope.row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </cute-drag-table>
  </div>
</template>

<script>
import DictService from '@/api/system/dict'
import CuteDragTable from '@/views/components/dev/CuteDragTable.vue'
import { FormatRowDateTimeStr } from '@/utils/CsUtil'

export default {
  name: 'CuteDragTableDemo',
  components: { CuteDragTable },
  data() {
    return {
      form: {
        model: {
          blurry: ''
        },
        rules: {}
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
          return DictService.queryPageDict(queryParams)
        },
        pageProps: {
          current: 1,
          pageSize: 10,
          total: 0
        }
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    FormatRowDateTimeStr,
    initData() {
      this.$refs.instance.refresh()
    },
    onFormSubmit(formName) {
      const that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.$refs.instance.refresh()
        } else {
          return false
        }
      })
    },
    onFormReset(formName) {
      try {
        this.$refs[formName].resetFields()
      } catch (e) {
        console.error(e)
      }
    },
    onTableEditClick(row) {
      console.log('编辑', row)
    },
    onTableDeleteClick(row) {
      console.log('删除', row)
    }
  }
}
</script>

