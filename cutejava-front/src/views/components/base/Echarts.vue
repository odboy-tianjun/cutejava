<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <heat-map />
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <radar-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <sunburst />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <gauge />
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <div class="chart-wrapper">
            <rich />
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-wrapper">
            <theme-river />
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <graph />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <sankey />
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <div class="chart-wrapper">
            <scatter />
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-wrapper">
            <point />
          </div>
        </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <div class="chart-wrapper">
          <category />
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import RadarChart from '@/components/Echarts/RadarChart.vue'
import HeatMap from '@/components/Echarts/HeatMap.vue'
import Gauge from '@/components/Echarts/Gauge.vue'
import Rich from '@/components/Echarts/Rich.vue'
import ThemeRiver from '@/components/Echarts/ThemeRiver.vue'
import Sunburst from '@/components/Echarts/Sunburst.vue'
import Graph from '@/components/Echarts/Graph.vue'
import Sankey from '@/components/Echarts/Sankey.vue'
import Scatter from '@/components/Echarts/Scatter.vue'
import Category from '@/components/Echarts/Category.vue'
import Point from '@/components/Echarts/Point.vue'

export default {
  name: 'Echarts',
  components: {
    Point,
    Category,
    Graph,
    HeatMap,
    RadarChart,
    Sunburst,
    Gauge,
    Rich,
    ThemeRiver,
    Sankey,
    Scatter
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 18px 22px 22px 22px;
  background-color: rgb(240, 242, 245);

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
</style>
