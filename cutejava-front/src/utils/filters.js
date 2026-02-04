import dayjs from 'dayjs'

/**
 * 日期格式化
 * @param value /
 * @returns {string}
 * @例子: <el-tag>{{ scope.row.createTime | FormatDate }}</el-tag>
 */
export function FormatDate(value) {
  if (!value) {
    return '-'
  }
  return dayjs(value).format('YY-MM-DD')
}

/**
 * 日期时间格式化
 * @param value /
 * @returns {string}
 * @例子: <el-tag>{{ scope.row.createTime | FormatDateTime }}</el-tag>
 */
export function FormatDateTime(value) {
  if (!value) {
    return '-'
  }
  return dayjs(value).format('YY-MM-DD HH:mm:ss')
}

/**
 * 金额格式化，并保留2位小数
 * @param value /
 * @returns {string}
 * @例子: <el-tag>{{ scope.row.totalAmount | FormatAmount2 }}</el-tag>
 */
export function FormatAmount2(value) {
  if (!value) {
    return '-'
  }
  if (value && value instanceof Number) {
    return value.toFixed(2)
  }
}

/**
 * 金额格式化，并保留1位小数
 * @param value /
 * @returns {string}
 * @例子: <el-tag>{{ scope.row.totalAmount | FormatAmount1 }}</el-tag>
 */
export function FormatAmount1(value) {
  if (!value) {
    return '-'
  }
  if (value && value instanceof Number) {
    return value.toFixed(1)
  }
}

/**
 * 金额格式化，并保留dot位小数
 * @param value /
 * @param dot 参数1。小数位
 * @returns {string}
 * @例子: <el-tag>{{ scope.row.totalAmount | FormatAmount(0) }}</el-tag>
 */
export function FormatAmount(value, dot) {
  if (!value) {
    return '-'
  }
  if (value && value instanceof Number) {
    return value.toFixed(dot)
  }
}

/**
 * 千分位数值格式化，并保留dot位小数
 * @param value /
 * @param dot 参数1。小数位
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.totalAmount | FormatNumber(2) }}</el-tag>
 */
export function FormatNumber(value, dot) {
  if (!value) {
    return '-'
  }
  const formatter = new Intl.NumberFormat('zh-CN', {
    minimumFractionDigits: dot || 0, maximumFractionDigits: dot || 0
  })
  return formatter.format(Number(value))
}

/**
 * 格式化字节数字符串
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.total | FormatBytes }}</el-tag>
 */
export function FormatBytes(value) {
  if (typeof value !== 'number' || isNaN(value) || value <= 0) {
    return '0 Bytes'
  }
  const units = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  const index = Math.floor(Math.log(value) / Math.log(1024))
  const unit = units[Math.min(index, units.length - 1)]
  const result = (value / Math.pow(1024, index)).toFixed(2)
  return `${result} ${unit}`
}

/**
 * 根据value获取label
 * @param value /
 * @param options 参数1。选项数据源
 * @returns {*|string}
 * @例子： <el-tag>{{ scope.row.sex | FormatOptionsLabel(sexOptions) }}</el-tag>
 */
export function FormatOptionsLabel(value, options) {
  if (!value) {
    return '-'
  }
  if (options instanceof Array) {
    const find = options.find(f => f.value === value)
    if (find) {
      return find.label
    }
  }
}

/**
 * 手机号掩码
 * @param value /
 * @returns {*|string}
 * @例子： <el-tag>{{ scope.row.mobile | MaskMobile }}</el-tag>
 */
export function MaskMobile(value) {
  if (!value) {
    return '-'
  }
  if (value.length > 7) {
    return value.substr(0, 3) + '****' + value.substr(7)
  }
  return value
}

/**
 * 邮箱掩码
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.email | MaskEmail }}</el-tag>
 */
export function MaskEmail(value) {
  if (!value) {
    return '-'
  }
  if (String(value).indexOf('@') > 0) {
    const str = value.split('@')
    let _s = ''
    if (str[0].length > 3) {
      for (let i = 0; i < str[0].length - 3; i++) {
        _s += '*'
      }
    }
    return str[0].substr(0, 3) + _s + '@' + str[1]
  }
  return value
}
