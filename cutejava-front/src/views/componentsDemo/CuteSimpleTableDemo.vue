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
        <el-form-item>
          <el-button type="primary" @click="onDialogClick">演示:表单对话框</el-button>
          <el-button type="primary" @click="onDrawerClick">演示:表单抽屉</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 表格渲染 -->
    <!-- fetch: 获取远程数据 -->
    <!-- params-transform: 组装查询参数 -->
    <!-- responseTransform: 组装表格对象。对响应结果做处理 -->
    <!-- primary-key: 主键 -->
    <!-- columns: 表格列 -->
    <!-- operate-column: 表格操作列 -->
    <!-- mode: 模式。不传默认，传multi为多选 -->
    <!-- @selection-change: 当且仅当mode为multi时启用 -->
    <cute-simple-table
      ref="instance"
      :fetch="curd.fetch"
      :params-transform="curd.paramsTransform"
      primary-key="id"
      :columns="curd.columns"
      :operate-column="curd.operateColumn"
      :page-props="curd.pageProps"
    />
    <cute-form-dialog ref="dialogForm" :form="createForm" title="演示:表单对话框" @submit="onDialogFormSubmit">
      <el-form-item label="活动名称" prop="name" label-width="100px">
        <el-input v-model="createForm.model.name" placeholder="请输入" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="100px">
        <el-select v-model="createForm.model.region" placeholder="请选择" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
    </cute-form-dialog>
    <cute-form-drawer ref="drawerFrom" :form="createForm" title="演示:表单抽屉" @submit="onDrawerFormSubmit">
      <el-form-item label="活动名称" prop="name" label-width="100px">
        <el-input v-model="createForm.model.name" placeholder="请输入" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="100px">
        <el-select v-model="createForm.model.region" placeholder="请选择" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
    </cute-form-drawer>
  </div>
</template>

<script>
import CuteSimpleTable from '@/views/components/dev/CuteSimpleTable'
import DictService from '@/api/system/dict'
import CuteFormDialog from '@/views/components/dev/CuteFormDialog'
import CuteFormDrawer from '@/views/components/dev/CuteFormDrawer'
import { FormatRowDateTimeStr } from '@/utils/CsUtil'

export default {
  name: 'CuteSimpleTableDemo',
  components: { CuteFormDrawer, CuteFormDialog, CuteSimpleTable },
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
        columns: [
          { prop: 'name', label: '名称' },
          { prop: 'description', label: '描述' },
          { prop: 'createTime', label: '创建时间', formatter: FormatRowDateTimeStr },
          { prop: 'createBy', label: '创建人' }
        ],
        operateColumn: {
          width: '150',
          fixed: 'right',
          formatter: (row, column, cellValue, index) => {
            return (<div>
              <el-button type='text' onClick={() => {
                console.log('编辑')
              }}>编辑
              </el-button>
              <el-button type='text' onClick={() => {
                console.log('删除')
              }}>删除
              </el-button>
            </div>)
          }
        },
        pageProps: {
          current: 1,
          pageSize: 10,
          total: 0
        }
      },
      createForm: {
        model: {
          name: '',
          region: ''
        },
        rules: {
          name: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          region: [
            { required: true, message: '请选择活动区域', trigger: 'change' }
          ]
        }
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
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
    onDialogClick() {
      this.$refs.dialogForm.show()
    },
    onDialogFormSubmit(values) {
      console.error('onDialogFormSubmit', values)
    },
    onDrawerClick() {
      this.$refs.drawerFrom.show()
    },
    onDrawerFormSubmit(values) {
      console.error('onDrawerFormSubmit', values)
    }
  }
}
</script>

