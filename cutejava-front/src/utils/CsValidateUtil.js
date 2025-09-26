/**
 * 是否链接
 * @param {string} path
 * @returns {Boolean}
 */
export function IsExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

// /**
//  * 是否有效用户名
//  * @param {string} str
//  * @returns {Boolean}
//  */
// export function IsValidUsername(str) {
//   const validMap = ['admin', 'editor']
//   return validMap.indexOf(str.trim()) >= 0
// }

/**
 * 是否有效链接
 * @param {string} url
 * @returns {Boolean}
 */
export function IsValidURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * 是否小写字母
 * @param {string} str
 * @returns {Boolean}
 */
export function IsValidLowerCaseLetter(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * 是否大写字母
 * @param {string} str
 * @returns {Boolean}
 */
export function IsValidUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * 是否包含大、小写字母
 * @param {string} str
 * @returns {Boolean}
 */
export function IsValidLetter(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * 是否邮箱
 * @param {string} email
 * @returns {Boolean}
 */
export function IsValidEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

// /**
//  * 是否手机号
//  * @param phone
//  * @returns {boolean}
//  * @constructor
//  */
// export function IsValidPhone(phone) {
//   const reg = /^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/
//   return reg.test(phone)
// }

/**
 * 是否是字符串
 * @param {string} str
 * @returns {Boolean}
 */
export function IsString(str) {
  return typeof str === 'string' || str instanceof String
}

/**
 * 是否数组
 * @param {Array} arg
 * @returns {Boolean}
 */
export function IsArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/**
 * 是否合法IP地址
 * @param rule
 * @param value
 * @param callback
 */
export function IsValidIP(rule, value, callback) {
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的IP地址'))
    } else {
      callback()
    }
  }
}

/* 是否手机号码或者固话*/
export function IsValidPhoneTwo(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}))$/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的电话号码或者固话号码'))
    } else {
      callback()
    }
  }
}

/* 是否固话*/
export function IsValidTelephone(rule, value, callback) {
  const reg = /0\d{2}-\d{7,8}/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的固话（格式：区号+号码,如010-1234567）'))
    } else {
      callback()
    }
  }
}

/* 是否手机号码*/
export function IsValidPhone(value) {
  const reg = /^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/
  if (value === '' || value === undefined || value == null) {
    return false
  } else {
    return !((!reg.test(value)) && value !== '')
  }
}

/* 是否身份证号码*/
export function IsValidIdNumber(rule, value, callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  if (value === '' || value === undefined || value == null) {
    callback()
  } else {
    if ((!reg.test(value)) && value !== '') {
      callback(new Error('请输入正确的身份证号码'))
    } else {
      callback()
    }
  }
}
