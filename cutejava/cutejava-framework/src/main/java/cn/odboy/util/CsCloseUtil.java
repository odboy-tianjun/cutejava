package cn.odboy.util;

import lombok.experimental.UtilityClass;

import java.io.Closeable;

/**
 * 用于关闭各种连接, 缺啥补啥
 */
@UtilityClass
public final class CsCloseUtil {

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    public static void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }
}
