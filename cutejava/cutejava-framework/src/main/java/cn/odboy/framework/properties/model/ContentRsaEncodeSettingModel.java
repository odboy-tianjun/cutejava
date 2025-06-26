package cn.odboy.framework.properties.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 密码加密传输，前端公钥加密，后端私钥解密
 */
@Getter
@Setter
public class ContentRsaEncodeSettingModel {
    public String privateKey;
}
