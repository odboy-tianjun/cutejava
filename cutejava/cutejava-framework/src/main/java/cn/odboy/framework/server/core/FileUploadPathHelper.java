package cn.odboy.framework.server.core;

import cn.odboy.constant.SystemConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 简化文件上传路径配置
 *
 * @author odboy
 */
@Component
public class FileUploadPathHelper {
    /**
     * 单位MB
     */
    private static final int AVATAR_MAX_SIZE = 5;
    private static final String PATH_WINDOWS = "C:\\${appName}\\file\\";
    private static final String PATH_MAC = "/Users/odboy/${appName}/file/";
    private static final String PATH_LINUX = "/home/${appName}/file/";
    @Value("${spring.application.name}")
    private String appName;

    public String getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(SystemConst.WIN)) {
            return PATH_WINDOWS.replace("${appName}", appName);
        } else if (os.toLowerCase().startsWith(SystemConst.MAC)) {
            return PATH_MAC.replace("${appName}", appName);
        }
        return PATH_LINUX.replace("${appName}", appName);
    }

    public int getAvatarMaxSize() {
        return AVATAR_MAX_SIZE;
    }
}
