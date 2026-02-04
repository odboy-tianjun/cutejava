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
        size="mini"
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
          <el-button type="primary" @click="onDialogProClick">演示:表单对话框Pro</el-button>
          <el-button type="primary" @click="onDrawerClick">演示:表单抽屉</el-button>
        </el-form-item>
      </el-form>
    </div>
    <cute-form-dialog
      ref="dialogForm"
      :model="createFormModel"
      :rules="createFormRules"
      title="演示:表单对话框"
      @submit="onDialogFormSubmit"
    >
      <el-form-item label="活动名称" prop="name" label-width="100px">
        <el-input v-model="createFormModel.name" placeholder="请输入" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="100px">
        <el-select v-model="createFormModel.region" placeholder="请选择" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
    </cute-form-dialog>
    <cute-form-drawer
      ref="drawerFrom"
      :model="createFormModel"
      :rules="createFormRules"
      title="演示:表单抽屉"
      width="45%"
      @submit="onDrawerFormSubmit"
    >
      <el-form-item label="活动名称" prop="name" label-width="100px">
        <el-input v-model="createFormModel.name" placeholder="请输入" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="100px">
        <el-select v-model="createFormModel.region" placeholder="请选择" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="活动模式" prop="modeList" label-width="100px">
        <cute-transfer v-model="createFormModel.modeList" :data-source="modeDataSource">
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="description" label="备注" />
        </cute-transfer>
      </el-form-item>
      <el-form-item label="可编辑表格" prop="editValues" label-width="100px">
        <cute-edit-table v-model="editTableDataSource" primary-key="id" :schema="editTableSchema" />
      </el-form-item>
    </cute-form-drawer>
    <cute-form-pro-dialog
      ref="dialogProForm"
      :model="createFormProModel"
      :schema="createFormProSchema"
      title="演示:表单对话框Pro"
      @submit="onDialogFormSubmit"
    />
  </div>
</template>

<script>
import CuteFormDialog from '@/views/components/advanced/CuteFormDialog.vue'
import CuteFormDrawer from '@/views/components/advanced/CuteFormDrawer.vue'
import { FormatRowDateTimeStr } from '@/utils/KitUtil'
import CuteTransfer from '@/views/components/dev/CuteTransfer.vue'
import CuteEditTable from '@/views/components/advanced/CuteEditTable.vue'
import CuteFormDialogPro from '@/views/components/advanced/CuteFormDialogPro.vue'

export default {
  name: 'CuteSimpleTableDemo',
  components: { CuteFormProDialog: CuteFormDialogPro, CuteEditTable: CuteEditTable, CuteTransfer, CuteFormDrawer, CuteFormDialog },
  data() {
    return {
      modeDataSource: [
        { id: 1, name: '小明', description: '小明牛P' },
        { id: 4, name: '小绿', description: '小绿牛P' },
        { id: 5, name: '小黄', description: '小黄牛P' },
        { id: 6, name: '小紫', description: '小紫牛P' },
        { id: 17, name: '小郑', description: '小郑牛P' },
        { id: 18, name: '小王', description: '小王牛P' }
      ],
      editTableSchema: [
        { name: 'name', title: '姓名', type: 'input' },
        { name: 'sex', title: '性别', type: 'select', dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'description', title: '描述', type: 'input' }
      ],
      editTableDataSource: [
        { id: 1, name: '小明', sex: 'mm', description: '小明牛P' },
        { id: 4, name: '小绿', sex: 'nn', description: '小绿牛P' },
        { id: 5, name: '小黄', sex: 'nn', description: '小黄牛P' },
        { id: 6, name: '小紫', sex: 'nn', description: '小紫牛P' },
        { id: 17, name: '小郑', sex: 'mm', description: '小郑牛P' },
        { id: 18, name: '小王', sex: 'nn', description: '小王牛P' }
      ],
      form: {
        model: {
          blurry: ''
        },
        rules: {}
      },
      // CuteFormDialog
      createFormModel: {
        name: '',
        region: '',
        modeList: []
      },
      createFormRules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择活动区域', trigger: 'change' }
        ]
      },
      // CuteFormDialogPro
      createFormProModel: {},
      createFormProSchema: [
        { name: 'name', title: '姓名', type: 'input', required: true },
        { name: 'sex', title: '性别', type: 'select', required: true, dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'createDate', title: '创建日期', type: 'date', required: false },
        { name: 'createTime', title: '创建时间', type: 'datetime', required: false }
      ]
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
    onDialogProClick() {
      this.$refs.dialogProForm.show()
    },
    onDialogFormSubmit(values) {
      console.log('onDialogFormSubmit', values)
    },
    onDrawerClick() {
      this.$refs.drawerFrom.show()
    },
    onDrawerFormSubmit(values) {
      console.log('onDrawerFormSubmit', values)
    }
  }
}
</script>

