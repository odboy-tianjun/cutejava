package cn.odboy.framework.properties.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageOSSModel {
    /**
     * 上传大小, 单位: M
     */
    private Long maxSize;
    private OSSConfigModel minio;
}
