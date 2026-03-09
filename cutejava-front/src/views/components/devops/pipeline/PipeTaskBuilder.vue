<template>
  <el-row>
    <el-col :span="12">
      <CutePage>
        <el-divider content-position="left">节点演示</el-divider>
        <div style="padding-left: 15px">
          <pipe-task
            :name="`${name}(Pending状态)`"
            :code="code"
            :click="click"
            :retry="retry"
            :detail-type="detailType"
            :button-list="buttonList"
            status="pending"
            status-desc="待执行"
            desc="待执行"
            :duration-millis="0"
          />
          <pipe-task
            :name="`${name}(Running状态)`"
            :code="code"
            :click="click"
            :retry="retry"
            :detail-type="detailType"
            :button-list="buttonList"
            status="running"
            status-desc="执行中"
            desc="执行中"
            :start-time-millis="1767237071000"
          />
          <pipe-task
            :name="`${name}(Success状态)`"
            :code="code"
            :click="click"
            :retry="retry"
            :detail-type="detailType"
            :button-list="buttonList"
            status="success"
            status-desc="执行成功"
            desc="执行成功"
            :duration-millis="1767237071"
          />
          <pipe-task
            :name="`${name}(Failed状态)`"
            :code="code"
            :click="click"
            :retry="retry"
            :detail-type="detailType"
            :button-list="buttonList"
            status="failed"
            status-desc="执行失败"
            desc="执行失败"
            :duration-millis="1767237071"
          />
        </div>
      </CutePage>
      <CutePage>
        <el-divider content-position="left">节点参数设置</el-divider>
        <el-form label-width="140px">
          <el-form-item label="节点名称">
            <el-input v-model="name" placeholder="请输入节点中文名称。例如：初始化" />
          </el-form-item>
          <el-form-item label="节点编码">
            <el-input v-model="code" placeholder="请输入节点英文名称。例如：initial" />
          </el-form-item>
          <el-form-item label="是否支持点击">
            <el-switch v-model="click" />
          </el-form-item>
          <el-form-item v-if="click" label="点击明细类型">
            <el-select
              v-model="detailType"
              placeholder="请选择明细类型"
              clearable
              allow-create
              filterable
            >
              <el-option label="Gitlab" value="gitlab" />
              <el-option label="Jenkins" value="jenkins" />
            </el-select>
          </el-form-item>
          <el-form-item label="是否支持重试">
            <el-switch v-model="retry" />
          </el-form-item>
          <el-form-item label="是否可交互(运行态)">
            <el-switch v-model="hasButtonList" />
          </el-form-item>
          <el-form-item v-if="hasButtonList" label="按钮组">
            <el-table :data="buttonList">
              <el-table-column prop="text" label="按钮名称" width="180">
                <template v-slot="scope">
                  <el-input v-model="scope.row.text" placeholder="请输入按钮名称" />
                </template>
              </el-table-column>
              <el-table-column prop="text" label="触发条件" width="180">
                <template v-slot="scope">
                  <el-select
                    v-model="scope.row.condition"
                    placeholder="请选择触发条件"
                    clearable
                  >
                    <el-option label="所有" value="all" />
                    <el-option label="待执行" value="pending" />
                    <el-option label="执行中" value="running" />
                    <el-option label="执行成功" value="success" />
                    <el-option label="执行失败" value="failed" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="按钮类型" width="160">
                <template v-slot="scope">
                  <el-select
                    v-model="scope.row.type"
                    placeholder="请选择按钮类型"
                    clearable
                  >
                    <el-option label="执行Service" value="execute" />
                    <el-option label="打开链接" value="link" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="执行动作">
                <template v-slot="scope">
                  <el-table v-if="scope.row.type === 'link'" :data="[scope.row]">
                    <el-table-column prop="blank" label="是否新开窗口" width="100">
                      <template v-slot="innerScope">
                        <el-switch v-model="innerScope.row.blank" />
                      </template>
                    </el-table-column>
                    <el-table-column prop="linkUrl" label="打开链接">
                      <template v-slot="innerScope">
                        <el-input v-model="innerScope.row.linkUrl" placeholder="请输入or黏贴第三方链接" />
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-table v-if="scope.row.type === 'execute'" :data="[scope.row]">
                    <el-table-column prop="method" label="请求类型" width="160">
                      <template v-slot="innerScope">
                        <el-select
                          v-model="innerScope.row.method"
                          placeholder="请选择请求类型"
                          clearable
                        >
                          <el-option label="Get" value="get" />
                          <el-option label="Post" value="post" />
                        </el-select>
                      </template>
                    </el-table-column>
                    <el-table-column prop="requestUrl" label="请求地址">
                      <template v-slot="innerScope">
                        <el-input v-model="innerScope.row.requestUrl" placeholder="请输入or黏贴请求地址。这里应为系统内部地址，具体参考 src/api 文件夹" />
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-table-column>
            </el-table>
            <el-button style="width: 100%" type="primary" plain @click="buttonList.push({})">添加按钮</el-button>
          </el-form-item>
          <el-form-item label="节点参数">
            <el-table :data="parameterList">
              <el-table-column prop="name" label="参数名称">
                <template v-slot="nodeParamScope">
                  <el-input v-model="nodeParamScope.row.name" placeholder="参数中文名" />
                </template>
              </el-table-column>
              <el-table-column prop="code" label="参数编码">
                <template v-slot="nodeParamScope">
                  <el-input v-model="nodeParamScope.row.code" placeholder="参数英文名" />
                </template>
              </el-table-column>
              <el-table-column prop="dataSource" label="参数可选项">
                <template v-slot="nodeParamScope">
                  <el-input
                    v-for="(item, index) in nodeParamScope.row.dataSource"
                    :key="index"
                    v-model="item.value"
                    style="margin-bottom: 2px"
                    clearable
                    placeholder="请输入参数选项"
                    @input="(value) => onParameterDataSourceInput(nodeParamScope, index, value)"
                    @clear="onParameterDataSourceClear(nodeParamScope, index)"
                  />
                  <el-button
                    style="width: 100%"
                    type="primary"
                    plain
                    @click="onParameterDataSourceAppendClick(nodeParamScope)"
                  >添加可选项
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button style="width: 100%" type="primary" plain @click="parameterList.push({})">添加参数</el-button>
          </el-form-item>
        </el-form>
      </CutePage>
    </el-col>
    <el-col :span="12">
      <CutePage>
        <el-divider content-position="left">节点选项(预览)</el-divider>
        <el-form ref="form" :model="parameters" label-width="160px">
          <el-form-item
            v-for="(paramCode, index) in Object.keys(parameters)"
            :key="`paramKey_${index}`"
            :label="`${parameters[paramCode].name}(${paramCode})`"
          >
            <el-select
              v-model="parameters[paramCode].value"
              :placeholder="`请选择${parameters[paramCode].name}`"
              clearable
              filterable
            >
              <el-option v-for="(item, index2) in parameters[paramCode].dataSource" :key="`item_${index2}`" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-form>
      </CutePage>
      <CutePage>
        <el-divider content-position="left">节点配置(预览)</el-divider>
        <div style="padding-left: 15px">
          <el-input
            v-model="taskConfig"
            type="textarea"
            :rows="25"
          />
        </div>
      </CutePage>
    </el-col>
  </el-row>
</template>

<script>

import PipeTask from '@/views/components/devops/pipeline/PipeTask.vue'
import CutePage from '@/views/components/dev/CutePage.vue'

export default {
  name: 'PipeTaskBuilder',
  components: { CutePage, PipeTask },
  data() {
    return {
      name: '',
      code: '',
      click: false,
      retry: false,
      detailType: null,
      buttonList: [
        // {
        //   condition: 'running',
        //   method: 'get',
        //   requestUrl: `api/devops/ci/doCheck?id=#pipelineInstanceNodeId`,
        //   text: '确认合并',
        //   type: 'execute'
        // }
      ],
      hasButtonList: false,
      parameters: {
        jdkVersion: {
          name: 'Jdk版本',
          dataSource: [
            { label: 'jdk8', value: 'jdk8' },
            { label: 'jdk11', value: 'jdk11' },
            { label: 'jdk21', value: 'jdk21' }
          ]
        }
      },
      parameterList: [
        // {
        //   name: 'Jdk版本',
        //   code: 'jdkVersion',
        //   dataSource: [
        //     { label: 'jdk8', value: 'jdk8' },
        //     { label: 'jdk11', value: 'jdk11' },
        //     { label: 'jdk21', value: 'jdk21' }
        //   ]
        // }
      ],
      taskConfig: ''
    }
  },
  computed: {
  },
  watch: {
    name: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: newVal,
          code: this.code,
          click: this.click,
          retry: this.retry,
          detailType: this.detailType,
          buttonList: this.buttonList,
          parameters: tempParameters
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true
    },
    code: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: newVal,
          click: this.click,
          retry: this.retry,
          detailType: this.detailType,
          buttonList: this.buttonList,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true
    },
    click: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: this.code,
          click: newVal,
          retry: this.retry,
          detailType: this.detailType,
          buttonList: this.buttonList,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true
    },
    retry: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: this.code,
          click: this.click,
          retry: newVal,
          detailType: this.detailType,
          buttonList: this.buttonList,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true
    },
    detailType: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: this.code,
          click: this.click,
          retry: this.retry,
          detailType: newVal,
          buttonList: this.buttonList,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true
    },
    buttonList: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in this.parameters) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: this.code,
          click: this.click,
          retry: this.retry,
          detailType: this.detailType,
          buttonList: newVal,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true,
      deep: true
    },
    parameters: {
      handler(newVal) {
        const tempParameters = {}
        for (const key in newVal) {
          tempParameters[key] = ''
        }
        const tempParameterMap = {}
        for (const key in newVal) {
          tempParameterMap[key] = newVal[key].dataSource
        }
        const config = {
          name: this.name,
          code: this.code,
          click: this.click,
          retry: this.retry,
          detailType: this.detailType,
          buttonList: this.buttonList,
          parameters: tempParameters,
          parameterMap: tempParameterMap
        }
        this.taskConfig = JSON.stringify(config, null, 2)
      },
      immediate: true,
      deep: true
    },
    /**
     * 用于刷新 parameters
     */
    parameterList: {
      handler: function(newList, oldList) {
        const newParameters = {}
        for (const param of newList) {
          if (param.code) {
            newParameters[param.code] = {
              name: param.name,
              dataSource: param.dataSource || []
            }
          }
        }
        this.parameters = newParameters
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
  },
  methods: {
    onParameterDataSourceInput(nodeParamScope, index, value) {
      const iii = nodeParamScope.row.dataSource[index]
      iii.label = value
      iii.value = value
      nodeParamScope.row.dataSource[index] = iii
    },
    onParameterDataSourceClear(nodeParamScope, index) {
      nodeParamScope.row.dataSource.splice(index, 1)
    },
    onParameterDataSourceAppendClick(nodeParamScope) {
      // m1.这种写法vue2无法响应式的检测到变化
      // const ds = nodeParamScope.row.dataSource || []
      // ds.push({label: '', value: ''})
      // nodeParamScope.row.dataSource = ds
      // m2.使用 this.$set（推荐用于 Vue 2）
      if (!nodeParamScope.row.dataSource) {
        this.$set(nodeParamScope.row, 'dataSource', [])
      }
      nodeParamScope.row.dataSource.push({ label: '', value: '' })
    }
  }
}
</script>

