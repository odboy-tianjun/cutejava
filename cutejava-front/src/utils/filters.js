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

/**
 * 身份证号掩码
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.idCard | MaskIdCard }}</el-tag>
 */
export function MaskIdCard(value) {
  if (!value) {
    return '-'
  }
  const str = String(value)
  if (str.length > 8) {
    return str.substr(0, 6) + '********' + str.substr(str.length - 4)
  }
  return value
}

/**
 * 银行卡号掩码
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.bankCard | MaskBankCard }}</el-tag>
 */
export function MaskBankCard(value) {
  if (!value) {
    return '-'
  }
  const str = String(value)
  if (str.length > 8) {
    return str.substr(0, 4) + ' **** **** ' + str.substr(str.length - 4)
  }
  return value
}

/**
 * 百分比格式化
 * @param value /
 * @param dot 小数位，默认2位
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.rate | FormatPercent(2) }}</el-tag>
 */
export function FormatPercent(value, dot = 2) {
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  const num = Number(value)
  if (isNaN(num)) {
    return '-'
  }
  return (num * 100).toFixed(dot) + '%'
}

/**
 * 文件名截取
 * @param value 文件名
 * @param maxLength 最大长度，默认20
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.fileName | TruncateFileName(20) }}</el-tag>
 */
export function TruncateFileName(value, maxLength = 20) {
  if (!value) {
    return '-'
  }
  const str = String(value)
  if (str.length <= maxLength) {
    return str
  }
  const extIndex = str.lastIndexOf('.')
  if (extIndex > 0) {
    const name = str.substr(0, extIndex)
    const ext = str.substr(extIndex)
    if (name.length > maxLength) {
      return name.substr(0, maxLength - 3) + '...' + ext
    }
    return name + ext
  }
  return str.substr(0, maxLength - 3) + '...'
}

/**
 * 文本截取
 * @param value 文本
 * @param maxLength 最大长度，默认50
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.content | TruncateText(50) }}</el-tag>
 */
export function TruncateText(value, maxLength = 50) {
  if (!value) {
    return '-'
  }
  const str = String(value)
  if (str.length <= maxLength) {
    return str
  }
  return str.substr(0, maxLength - 3) + '...'
}

/**
 * 首字母大写
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.name | Capitalize }}</el-tag>
 */
export function Capitalize(value) {
  if (!value) {
    return '-'
  }
  const str = String(value)
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase()
}

/**
 * 转换为大写
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.code | ToUpperCase }}</el-tag>
 */
export function ToUpperCase(value) {
  if (!value) {
    return '-'
  }
  return String(value).toUpperCase()
}

/**
 * 转换为小写
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.code | ToLowerCase }}</el-tag>
 */
export function ToLowerCase(value) {
  if (!value) {
    return '-'
  }
  return String(value).toLowerCase()
}

/**
 * 相对时间格式化
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.createTime | FormatRelativeTime }}</el-tag>
 */
export function FormatRelativeTime(value) {
  if (!value) {
    return '-'
  }
  const now = dayjs()
  const target = dayjs(value)
  const diff = now.diff(target, 'second')

  if (diff < 60) {
    return '刚刚'
  } else if (diff < 3600) {
    return Math.floor(diff / 60) + '分钟前'
  } else if (diff < 86400) {
    return Math.floor(diff / 3600) + '小时前'
  } else if (diff < 2592000) {
    return Math.floor(diff / 86400) + '天前'
  } else if (diff < 31536000) {
    return Math.floor(diff / 2592000) + '个月前'
  } else {
    return Math.floor(diff / 31536000) + '年前'
  }
}

/**
 * 星期几格式化
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.date | FormatWeekday }}</el-tag>
 */
export function FormatWeekday(value) {
  if (!value) {
    return '-'
  }
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return weekdays[dayjs(value).day()]
}

/**
 * 状态值转换为布尔文本
 * @param value /
 * @param trueText 真值文本，默认"是"
 * @param falseText 假值文本，默认"否"
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.status | FormatBoolean }}</el-tag>
 */
export function FormatBoolean(value, trueText = '是', falseText = '否') {
  if (value === null || value === undefined) {
    return '-'
  }
  return value ? trueText : falseText
}

/**
 * 状态值转换为开关文本
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.enabled | FormatSwitch }}</el-tag>
 */
export function FormatSwitch(value) {
  return FormatBoolean(value, '开启', '关闭')
}

/**
 * 状态值转换为启用文本
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.status | FormatEnabled }}</el-tag>
 */
export function FormatEnabled(value) {
  return FormatBoolean(value, '已启用', '已禁用')
}

/**
 * 秒数转换为时长格式
 * @param value 秒数
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.duration | FormatDuration }}</el-tag>
 */
export function FormatDuration(value) {
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  const seconds = Number(value)
  if (isNaN(seconds)) {
    return '-'
  }

  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60

  if (hours > 0) {
    return `${hours}小时${minutes}分${secs}秒`
  } else if (minutes > 0) {
    return `${minutes}分${secs}秒`
  } else {
    return `${secs}秒`
  }
}

/**
 * 数组转字符串
 * @param value /
 * @param separator 分隔符，默认", "
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.tags | ArrayToString(', ') }}</el-tag>
 */
export function ArrayToString(value, separator = ', ') {
  if (!value) {
    return '-'
  }
  if (Array.isArray(value)) {
    return value.join(separator)
  }
  return String(value)
}

/**
 * JSON对象格式化为字符串
 * @param value /
 * @param space 缩进空格数，默认2
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.config | JsonToString(2) }}</el-tag>
 */
export function JsonToString(value, space = 2) {
  if (value === null || value === undefined) {
    return '-'
  }
  try {
    return JSON.stringify(value, null, space)
  } catch (e) {
    return String(value)
  }
}

/**
 * 数字转中文
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.index | NumberToChinese }}</el-tag>
 */
export function NumberToChinese(value) {
  if (value === null || value === undefined) {
    return '-'
  }
  const num = Number(value)
  if (isNaN(num)) {
    return '-'
  }

  const digits = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九']
  const units = ['', '十', '百', '千', '万']

  if (num === 0) {
    return digits[0]
  }

  let str = ''
  const numStr = Math.abs(num).toString()

  for (let i = 0; i < numStr.length; i++) {
    const digit = parseInt(numStr[i])
    const pos = numStr.length - i - 1

    if (digit !== 0) {
      str += digits[digit] + units[pos]
    } else {
      // 处理连续的零
      if (str[str.length - 1] !== digits[0] && pos !== 0) {
        str += digits[0]
      }
    }
  }

  // 移除末尾的零
  str = str.replace(/零+$/, '')

  // 处理"一十"的情况
  str = str.replace(/^一十/, '十')

  return num < 0 ? '负' + str : str
}

/**
 * URL链接格式化
 * @param value /
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.url | FormatUrl }}</el-tag>
 */
export function FormatUrl(value) {
  if (!value) {
    return '-'
  }
  let url = String(value)
  if (!url.startsWith('http://') && !url.startsWith('https://')) {
    url = 'http://' + url
  }
  return url
}

/**
 * 货币格式化
 * @param value /
 * @param currency 货币代码，默认CNY
 * @returns {string}
 * @例子： <el-tag>{{ scope.row.price | FormatCurrency('CNY') }}</el-tag>
 */
export function FormatCurrency(value, currency = 'CNY') {
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  const num = Number(value)
  if (isNaN(num)) {
    return '-'
  }
  const formatter = new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: currency
  })
  return formatter.format(num)
}
