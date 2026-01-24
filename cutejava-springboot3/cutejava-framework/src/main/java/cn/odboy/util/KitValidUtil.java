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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.exception.BadRequestException;
import lombok.experimental.UtilityClass;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean验证工具
 *
 * @author odboy
 * @date 2025-01-15
 */
@UtilityClass
public final class KitValidUtil {

  private static final Validator VALIDATOR;

  static {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      VALIDATOR = factory.getValidator();
    }
  }

  /**
   * 验证实体是否通过校验 <br/> 实体内通过 javax.validation.constraints 中的表达式限制参数 <br/>
   *
   * @param object /
   * @param <T>    /
   */
  public static <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, Default.class);
    if (!violations.isEmpty()) {
      throw new BadRequestException(
          violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
    }
  }

  /**
   * 参数为null，抛异常
   *
   * @param value 参数值
   */
  public static void isNull(Object value) {
    if (value == null) {
      throw new BadRequestException("参数必填");
    }
  }

  /**
   * 参数为空，抛异常
   *
   * @param value 参数值
   * @param name  参数名称
   * @param code  参数编码
   */
  public static void isNull(Object value, @NotNull String name, @NotNull String code) {
    if (value == null) {
      throw new BadRequestException(String.format("参数 %s(%s) 必填", name, code));
    }
  }

  /**
   * 参数为空，抛异常
   *
   * @param value   参数值
   * @param message 异常信息
   */
  public static void isNull(Object value, @NotNull String message) {
    if (value == null) {
      throw new BadRequestException(message);
    }
  }

  /**
   * 参数为空，抛异常
   *
   * @param value 参数值
   * @param code  参数编码
   */
  public static void isNullF(Object value, @NotNull String code) {
    if (value == null) {
      throw new BadRequestException(String.format("参数 %s 必填", code));
    }
  }

  /**
   * 参数为空字符串，抛异常
   *
   * @param value 参数值
   * @param name  参数名称
   * @param code  参数编码
   */
  public static void isBlank(CharSequence value, @NotNull String name, @NotNull String code) {
    if (StrUtil.isBlank(value)) {
      throw new BadRequestException(String.format("参数 %s(%s) 必填", name, code));
    }
  }

  /**
   * 集合为空，抛异常
   *
   * @param data 集合值
   * @param code 参数编码
   */
  public static void isEmpty(List<?> data, @NotNull String code) {
    if (CollUtil.isEmpty(data)) {
      throw new BadRequestException(String.format("参数 %s 必填", code));
    }
  }

  /**
   * 集合为空，抛异常
   *
   * @param data 集合值
   * @param name 参数名称
   * @param code 参数编码
   */
  public static void isEmpty(List<?> data, @NotNull String name, @NotNull String code) {
    if (CollUtil.isEmpty(data)) {
      throw new BadRequestException(String.format("参数 %s(%s) 必填", name, code));
    }
  }

  /**
   * 表达式为true，抛异常
   *
   * @param condition 表达式
   * @param message   异常信息
   */
  public static void isTrue(boolean condition, String message) {
    if (condition) {
      throw new BadRequestException(message);
    }
  }

  /**
   * 表达式为false，抛异常
   *
   * @param condition 表达式
   * @param message   异常信息
   */
  public static void isFalse(boolean condition, String message) {
    if (!condition) {
      throw new BadRequestException(message);
    }
  }

  /**
   * 参数为空字符串，抛异常
   *
   * @param value   参数值
   * @param message 异常信息
   */
  public static void isBlank(String value, String message) {
    if (StrUtil.isBlank(value)) {
      throw new BadRequestException(message);
    }
  }
}
