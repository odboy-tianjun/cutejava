package cn.odboy.framework.properties.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Jwt参数配置
 */
@Getter
@Setter
public class JwtAuthSettingModel {
    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Long tokenValidityInSeconds;

    /**
     * token 续期检查
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;
}
