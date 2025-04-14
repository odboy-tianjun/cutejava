package cn.odboy.config.model;

import lombok.Data;
/**
 * 密码加密传输，前端公钥加密，后端私钥解密
 */
@Data
public class ContentRsaEncodeSettingModel {
    public String privateKey;
}
