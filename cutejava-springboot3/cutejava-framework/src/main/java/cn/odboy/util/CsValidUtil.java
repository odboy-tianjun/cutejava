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

import cn.odboy.framework.exception.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean验证工具
 *
 * @author odboy
 * @date 2025-01-15
 */
@UtilityClass
public final class CsValidUtil {
    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, Default.class);
        if (!violations.isEmpty()) {
            throw new BadRequestException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
        }
    }
}