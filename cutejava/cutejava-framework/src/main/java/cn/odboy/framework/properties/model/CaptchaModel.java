package cn.odboy.framework.properties.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaModel extends CsObject {
    /**
     * 验证码有效时间, 单位: 秒
     */
    private Long expireTime;
}
