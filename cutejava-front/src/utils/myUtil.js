import store from '@/store'

const PermissionUtil = {
  /**
   * 权限校验
   * @param value
   * @returns {boolean|*}
   */
  validate: function(value) {
    if (value && value instanceof Array && value.length > 0) {
      // 拥有的权限
      const roles = store.getters && store.getters.roles
      // 需要的权限
      const permissionRoles = value
      return roles.some(role => {
        return permissionRoles.includes(role)
      })
    } else {
      console.error(`need roles! Like v-permission="['admin','editor']"`)
      return false
    }
  }
}

const StringUtil = {
  /**
   * 字符串转数字
   * @param value
   * @returns {Number|*}
   */
  toNumber: function(value) {
    if (value) {
      return Number(value)
    }
    return 0
  },
  /**
   * 保留几位小数
   * @param num Number
   * @param accuracy 精度
   * @returns {string|number}
   */
  retailNum: function(num, accuracy) {
    return num instanceof Number ? num.toFixed(accuracy) : 0
  }
}

const ObjectUtil = {
  /**
   * 浅拷贝
   * @param source 源对象
   * @param target 目标对象
   * @returns {*}
   */
  copy: function(source, target) {
    return Object.assign(target, source)
  }
}

const LabelUtil = {
  /**
   * 根据value获取label
   * @param options /
   * @param value /
   * @returns {*}
   */
  getLabel: function(options, value) {
    if (options instanceof Array) {
      const find = options.find(f => f.value === value)
      if (find) {
        return find.label
      }
      return ''
    }
  }
}

const SizeUtil = {
  formatBytes: function(value) {
    if (typeof value !== 'number' || isNaN(value) || value <= 0) {
      return '0 Bytes'
    }
    const units = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
    const index = Math.floor(Math.log(value) / Math.log(1024))
    const unit = units[Math.min(index, units.length - 1)]
    const result = (value / Math.pow(1024, index)).toFixed(2)
    return `${result} ${unit}`
  }
}

const DateUtil = {
  formatDate: function(originVal) {
    if (originVal === undefined) {
      return ''
    }
    const dt = new Date(originVal)
    const y = dt.getFullYear()
    const m = (dt.getMonth() + 1 + '').padStart(2, '0')
    const d = (dt.getDate() + '').padStart(2, '0')
    return `${y}-${m}-${d}`
  },
  formatDateTime: function(originVal) {
    if (originVal === undefined) {
      return ''
    }
    const dt = new Date(originVal)
    const y = dt.getFullYear()
    const m = (dt.getMonth() + 1 + '').padStart(2, '0')
    const d = (dt.getDate() + '').padStart(2, '0')
    const hh = (dt.getHours() + '').padStart(2, '0')
    const mm = (dt.getMinutes() + '').padStart(2, '0')
    const ss = (dt.getSeconds() + '').padStart(2, '0')
    return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
  },
  formatRowDateTime: function(row, column, cellValue, index) {
    if (cellValue === undefined) {
      return ''
    }
    const dt = new Date(cellValue)
    const y = dt.getFullYear()
    const m = (dt.getMonth() + 1 + '').padStart(2, '0')
    const d = (dt.getDate() + '').padStart(2, '0')
    const hh = (dt.getHours() + '').padStart(2, '0')
    const mm = (dt.getMinutes() + '').padStart(2, '0')
    const ss = (dt.getSeconds() + '').padStart(2, '0')
    return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
  }
}

export {
  PermissionUtil,
  StringUtil,
  ObjectUtil,
  LabelUtil,
  DateUtil,
  SizeUtil
}
