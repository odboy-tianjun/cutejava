package cn.odboy.core.framework.properties.model;

import cn.odboy.constant.SystemConst;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传配置
 */
@Getter
@Setter
public class FileUploadSettingModel {
    /**
     * 文件大小限制
     */
    private Long fileMaxSize;
    /**
     * 头像大小限制
     */
    private Long avatarMaxSize;

    private ElPath mac;

    private ElPath linux;

    private ElPath windows;

    public ElPath getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(SystemConst.WIN)) {
            return windows;
        } else if (os.toLowerCase().startsWith(SystemConst.MAC)) {
            return mac;
        }
        return linux;
    }

    @Data
    public static class ElPath {

        private String path;

        private String avatar;
    }
}
