/*
 * Copyright 2021-2025 Odboy
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

import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.exception.BadRequestException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import lombok.experimental.UtilityClass;

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

  public static <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, Default.class);
    if (!violations.isEmpty()) {
      throw new BadRequestException(
          violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
    }
  }

  public static void notNull(Object value) {
    if (value == null) {
      throw new BadRequestException("参数必填");
    }
  }

  public static void isNull(Object value) {
    if (value != null) {
      throw new BadRequestException("参数非必填");
    }
  }

  public static void notNull(Object value, @NotNull String name, @NotNull String code) {
    if (value != null) {
      throw new BadRequestException(String.format("参数 %s(%s) 必填", name, code));
    }
  }

  public static void isBlank(CharSequence value, @NotNull String name, @NotNull String code) {
    if (StrUtil.isNotBlank(value)) {
      throw new BadRequestException(String.format("参数 %s(%s) 非必填", name, code));
    }
  }

  public static void notBlank(CharSequence value, @NotNull String name, @NotNull String code) {
    if (StrUtil.isBlank(value)) {
      throw new BadRequestException(String.format("参数 %s(%s) 必填", name, code));
    }
  }
}