const pipeline = {
  /**
   * 全局常量
   */
  state: {
    statusConst: {
      pending: { code: 'pending', label: '未开始', color: '#C0C4CC', icon: 'el-icon-time' },
      running: { code: 'running', label: '运行中', color: '#409EFF', icon: 'el-icon-loading' },
      success: { code: 'success', label: '执行成功', color: '#67C23A', icon: 'el-icon-success' },
      fail: { code: 'fail', label: '执行失败', color: '#F56C6C', icon: 'el-icon-error' }
    }
  }
}

export default pipeline
