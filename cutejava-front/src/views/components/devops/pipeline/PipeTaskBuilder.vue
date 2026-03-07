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
    </el-col>
    <el-col :span="12">
      <CutePage>
        <el-divider content-position="left">节点参数设置</el-divider>
        <el-form label-width="140px">
          <el-form-item label="节点名称">
            <el-input v-model="name" />
          </el-form-item>
          <el-form-item label="节点编码">
            <el-input v-model="code" />
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
                  <el-input v-model="scope.row.text" />
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
                  <el-table v-if="scope.row.type === 'link'" :data="buttonList">
                    <el-table-column prop="blank" label="是否新开窗口" width="100">
                      <template v-slot="innerScope">
                        <el-switch v-model="innerScope.row.blank" />
                      </template>
                    </el-table-column>
                    <el-table-column prop="linkUrl" label="打开链接">
                      <template v-slot="innerScope">
                        <el-input v-model="innerScope.row.linkUrl" />
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-table v-if="scope.row.type === 'execute'" :data="buttonList">
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
                        <el-input v-model="innerScope.row.requestUrl" />
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
                  <el-input v-model="nodeParamScope.row.name" />
                </template>
              </el-table-column>
              <el-table-column prop="code" label="参数编码">
                <template v-slot="nodeParamScope">
                  <el-input v-model="nodeParamScope.row.code" />
                </template>
              </el-table-column>
              <el-table-column prop="dataSource" label="参数可选项">
                <template v-slot="nodeParamScope">
                  <el-select
                    placeholder="请输入参数可选项"
                    clearable
                    filterable
                    allow-create
                  >
                    <el-option
                      v-for="(item, index) in nodeParamScope.row.dataSource"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
            </el-table>
            <el-button style="width: 100%" type="primary" plain @click="parameterList.push({})">添加参数</el-button>
          </el-form-item>
        </el-form>
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
      name: '环境准备',
      code: 'release',
      click: false,
      retry: false,
      detailType: null,
      buttonList: [
        {
          condition: 'running',
          method: 'get',
          requestUrl: `api/devops/ci/doCheck?id=#pipelineInstanceNodeId`,
          text: '确认合并',
          type: 'execute'
        }
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
        {
          name: 'Jdk版本',
          code: 'jdkVersion',
          dataSource: [
            { label: 'jdk8', value: 'jdk8' },
            { label: 'jdk11', value: 'jdk11' },
            { label: 'jdk21', value: 'jdk21' }
          ]
        }
      ]
    }
  },
  computed: {},
  watch: {},
  mounted() {},
  methods: {}
}
</script>

