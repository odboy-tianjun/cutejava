package cn.odboy.framework.properties.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OSSConfigModel extends CsObject {
    /**
     * 服务地址
     */
    private String endpoint;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * Access Key
     */
    private String accessKey;
    /**
     * Secret Key
     */
    private String secretKey;
}
