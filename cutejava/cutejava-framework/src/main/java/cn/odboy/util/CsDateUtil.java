package cn.odboy.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class CsDateUtil {
    /**
     * 获取当前时间毫秒数
     *
     * @return /
     */
    public static String getNowDateTimeMsStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
    }

    /**
     * 获取当前时间,但不包括毫秒数
     *
     * @return /
     */
    public static String getNowDateTimeStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取当前时间,但不包括毫秒数
     *
     * @return /
     */
    public static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
    }
}
