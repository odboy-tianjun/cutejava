/*
 * Copyright 2021-2026 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关工具
 *
 * @author odboy
 * @date 2025-10-01
 */
@UtilityClass
public class KitDateUtil {

  /**
   * 获取当前时间毫秒数 yyyyMMddHHmmssSSS
   *
   * @return /
   */
  public static String getNowDateTimeMsStr() {
    return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
  }

  /**
   * 获取当前时间,但不包括毫秒数 yyyyMMddHHmmss
   *
   * @return /
   */
  public static String getNowDateTimeStr() {
    return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
  }

  /**
   * 获取当前时间,但不包括毫秒数 yyyyMMdd
   *
   * @return /
   */
  public static String getNowDateStr() {
    return DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
  }

  /**
   * 格式化到毫秒
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @return 格式化后的时间，X天X时X分X秒
   */
  public static String formatMillsDuration(Date startTime, Date endTime) {
    if (startTime == null || endTime == null) {
      // 什么都不返回
      return "";
    }
    long betweenSeconds = DateUtil.between(startTime, endTime, cn.hutool.core.date.DateUnit.SECOND);
    long days = betweenSeconds / (24 * 60 * 60);
    long hours = (betweenSeconds % (24 * 60 * 60)) / (60 * 60);
    long minutes = (betweenSeconds % (60 * 60)) / 60;
    long seconds = betweenSeconds % 60;
    StringBuilder result = new StringBuilder();
    if (days > 0) {
      result.append(days).append("天");
    }
    if (hours > 0) {
      result.append(hours).append("时");
    }
    if (minutes > 0) {
      result.append(minutes).append("分");
    }
    if (seconds > 0 || result.length() == 0) {
      result.append(seconds).append("秒");
    }
    return result.toString();
  }

  /**
   * 格式化到秒
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @return 格式化后的时间，X天X时X分X秒
   */
  public static String formatSecondsDuration(Date startTime, Date endTime) {
    if (startTime == null || endTime == null) {
      // 什么都不返回
      return "";
    }
    long betweenMs = DateUtil.betweenMs(startTime, endTime);
    long days = betweenMs / (24 * 60 * 60 * 1000);
    long hours = (betweenMs % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
    long minutes = (betweenMs % (60 * 60 * 1000)) / (60 * 1000);
    long seconds = (betweenMs % (60 * 1000)) / 1000;
    long milliseconds = betweenMs % 1000;
    StringBuilder result = new StringBuilder();
    if (days > 0) {
      result.append(days).append("天");
    }
    if (hours > 0) {
      result.append(hours).append("时");
    }
    if (minutes > 0) {
      result.append(minutes).append("分");
    }
    if (seconds > 0) {
      result.append(seconds).append("秒");
    }
    if (milliseconds > 0) {
      result.append(milliseconds).append("毫秒");
    }
    // 如果所有单位都是0，则显示0毫秒
    if (result.length() == 0) {
      result.append("0毫秒");
    }
    return result.toString();
  }

  /**
   * 获得当天是周几
   */
  public static String getWeekDay() {
    String[] weekDays = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) {
      w = 0;
    }
    return weekDays[w];
  }

  public static void main(String[] args) {
    System.err.println(getWeekDay());
  }
}
