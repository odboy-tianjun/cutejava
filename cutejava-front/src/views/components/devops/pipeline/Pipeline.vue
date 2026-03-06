<template>
  <cute-horizontal-scroll-bar :width="maxWidth">
    <i v-for="(item, index) in taskNodes" :key="index">
      <!-- 最后一个元素如何渲染 -->
      <i v-if="index === taskNodes.length - 1">
        <pipe-task v-bind="item" />
      </i>
      <!-- 其他元素如何渲染 -->
      <i v-else class="pipeline-card">
        <pipe-task v-bind="item" />
        <div class="pipeline-link" />
      </i>
    </i>
  </cute-horizontal-scroll-bar>
</template>

<script>

import PipeTask from '@/views/components/devops/pipeline/PipeTask.vue'
import CuteHorizontalScrollBar from '@/views/components/dev/CuteHorizontalScrollBar.vue'

export default {
  name: 'Pipeline',
  components: { CuteHorizontalScrollBar, PipeTask },
  props: {
    /**
     * 流水线全局唯一实例ID
     */
    instanceId: {
      type: String,
      required: true
    },
    /**
     * 流水线任务节点明细
     */
    taskNodes: {
      type: Array,
      required: true
    }
  },
  data() {
    return {}
  },
  computed: {
    maxWidth() {
      return `${document.body.clientWidth - 310}px`
    }
  },
  watch: {},
  mounted() {
  },
  methods: {}
}
</script>
<style lang="scss" scoped>
.pipeline-card {
  width: 240px;
  max-width: 240px;
}

.pipeline-link {
  height: 3px;
  width: 30px;
  display: inline-block;
  background-color: #E4E7ED;
  margin-bottom: 3em;
  margin-right: 4px;
}
</style>

