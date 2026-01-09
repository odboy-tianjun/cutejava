import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAg975XAOL1zg5EOcSSGPIcxwx5DxcvPD2rBRQa0/o1HTF25cnsU9uSHfzP+qohkDG76kDk+1FA/cuQda4CDAmK2fWW' +
  'q7KgglKd08hRkByjLuRsXu5WVApAAG15ItNM0CrXI7P47aOan+6YPU0Ni1+4WYQnwxfRYdUaKgY5G5gb3S+hx6VO1iN1KuRMBJ7y5FMh1WNJOM9bQb3X1pvaJ73drTpy1wJHAvfP80iG2+sSh0FfcOxCoRNEff' +
  'CUsU2bYNH6dG6mXIDvo6XZStspwUfSitfgOER207uvBCCVyFlas+2Xs09yyAoJNjaWmeCPfA7uJvJrXUft/SMy8JNIXBY40vlwk4d4FE6OKOG9Wn0oRlofRKZ7NGvpUxEA5zOW6g2LsHKUUXpgOfu59M6ZHxviGM' +
  't26WxJiq/ifVKJE3yVgx0MVIGEFTrG+spOCVsDYMSHnVsPEUdL6g5FV2YLyqNHljBqUqwyOEfUinp7hPRxrLJDMyX6GWy7y48Djg0wyh2hXro/ygVrZ6jcSd7MUAd2lYueUKK8Bey7OzIcOJ6GveHdLzGrspbbkn' +
  'DuRsbcInR5bD6l7YA4vpnLsbiOH2wbKJWXNnxRwN5KuWSpwHDHhARB8f4wWrewUKIOxcFI/eJ6ke6D52JMOKhiGeZKn+2/giHwrPGkTREe47Ose6UvTMCAwEAAQ=='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

