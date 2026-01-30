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

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 计算类
 */
@UtilityClass
public final class KitBigDecimalUtil {

  /**
   * 将对象转换为 BigDecimal
   *
   * @param obj 输入对象
   * @return 转换后的 BigDecimal
   */
  private static BigDecimal toBigDecimal(Object obj) {
    return switch (obj) {
      case BigDecimal result -> result;
      case Long result -> BigDecimal.valueOf(result);
      case Integer result -> BigDecimal.valueOf(result.longValue());
      case Short result -> BigDecimal.valueOf(result.longValue());
      case Byte result -> BigDecimal.valueOf(result.longValue());
      case Double result -> BigDecimal.valueOf(result);
      case Float result -> BigDecimal.valueOf(result.doubleValue());
      case String result -> new BigDecimal(result);
      case Number result -> BigDecimal.valueOf(result.doubleValue());
      case null, default -> {
        assert obj != null;
        throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
      }
    };
  }

  /**
   * 加法
   *
   * @param a 加数
   * @param b 加数
   * @return 两个加数的和,
   */
  public static BigDecimal add(Object a, Object b) {
    BigDecimal bdA = toBigDecimal(a);
    BigDecimal bdB = toBigDecimal(b);
    return bdA.add(bdB);
  }

  /**
   * 减法
   *
   * @param a 被减数
   * @param b 减数
   * @return 两数的差,
   */
  public static BigDecimal subtract(Object a, Object b) {
    BigDecimal bdA = toBigDecimal(a);
    BigDecimal bdB = toBigDecimal(b);
    return bdA.subtract(bdB);
  }

  /**
   * 乘法
   *
   * @param a 乘数
   * @param b 乘数
   * @return 两个乘数的积, 保留6位小数
   */
  public static BigDecimal multiply(Object a, Object b) {
    BigDecimal bdA = toBigDecimal(a);
    BigDecimal bdB = toBigDecimal(b);
    return bdA.multiply(bdB);
  }

  /**
   * 除法
   *
   * @param a 被除数
   * @param b 除数
   * @return 两数的商
   */
  public static BigDecimal divide(Object a, Object b) {
    BigDecimal bdA = toBigDecimal(a);
    BigDecimal bdB = toBigDecimal(b);
    return bdA.divide(bdB, 6, RoundingMode.DOWN);
  }

  /**
   * 分转元
   *
   * @param obj 分的金额
   * @return 转换后的元
   */
  public static BigDecimal centsToYuan(Object obj) {
    BigDecimal cents = toBigDecimal(obj);
    return cents.divide(BigDecimal.valueOf(100), 6, RoundingMode.DOWN);
  }

  /**
   * 元转分
   *
   * @param obj 元的金额
   * @return 转换后的分
   */
  public static long yuanToCents(Object obj) {
    BigDecimal yuan = toBigDecimal(obj);
    return yuan.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_EVEN).longValue();
  }

  /**
   * 取整，采用银行家算法
   *
   * @param obj 金额
   * @return /
   */
  public static BigDecimal toRound(Object obj) {
    return toBigDecimal(obj).setScale(0, RoundingMode.HALF_EVEN);
  }

  /**
   * 取整，采用银行家算法
   *
   * @param obj 金额
   * @return /
   */
  public static long toRoundLong(Object obj) {
    return toBigDecimal(obj).setScale(0, RoundingMode.HALF_EVEN).longValue();
  }

  /**
   * 格式化为 xxx,xxx,xxx 格式
   *
   * @param obj 金额
   * @return /
   */
  public static String formatGroupingAmount(Object obj) {
    if (obj == null) {
      return "0";
    }
    BigDecimal bigDecimal = toBigDecimal(obj);
    NumberFormat nf = NumberFormat.getInstance();
    // 启用千位分隔符
    nf.setGroupingUsed(true);
    return nf.format(bigDecimal.setScale(0, RoundingMode.HALF_UP));
  }

  public static void main(String[] args) {
    BigDecimal num1 = new BigDecimal("10.123");
    BigDecimal num2 = new BigDecimal("2.456");
    System.out.println("加法结果: " + add(num1, num2));
    System.out.println("减法结果: " + subtract(num1, num2));
    System.out.println("乘法结果: " + multiply(num1, num2));
    System.out.println("除法结果: " + divide(num1, num2));
    Long cents = 12345L;
    System.out.println("分转元结果: " + centsToYuan(cents));
    BigDecimal yuan = new BigDecimal("123.45");
    System.out.println("元转分结果: " + yuanToCents(yuan));
    BigDecimal toRo = new BigDecimal("123.1548456");
    System.out.println("取整: " + toRound(toRo));
    BigDecimal toRoLong = new BigDecimal("123.1548456");
    System.out.println("取整转long: " + toRoundLong(toRoLong));
    BigDecimal formatGa = new BigDecimal("1231548456123123");
    System.out.println("格式化千分位: " + formatGroupingAmount(formatGa));
  }
}
