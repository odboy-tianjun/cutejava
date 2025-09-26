
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 返回值处理 工具
 *
 * @author odboy
 * @date 2025-05-08
 */
public class CsReturnValueHandleUtil {
    public static Object getDefaultValue(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取目标方法的返回值类型
        Class<?> returnType = method.getReturnType();
        if (returnType.isArray()) {
            return getEmptyArray(returnType);
        } else if (List.class.isAssignableFrom(returnType)) {
            return Collections.emptyList();
        } else if (Set.class.isAssignableFrom(returnType)) {
            return Collections.emptySet();
        } else if (Collection.class.isAssignableFrom(returnType)) {
            // 默认返回空 List
            return Collections.emptyList();
        } else {
            // 普通对象返回 null
            return null;
        }
    }

    /**
     * 根据数组类型返回空数组
     */
    private static Object getEmptyArray(Class<?> arrayType) {
        if (arrayType == int[].class) {
            return new int[0];
        } else if (arrayType == long[].class) {
            return new long[0];
        } else if (arrayType == double[].class) {
            return new double[0];
        } else if (arrayType == String[].class) {
            return new String[0];
        } else {
            // 其他引用类型数组
            return java.lang.reflect.Array.newInstance(arrayType.getComponentType(), 0);
        }
    }
}
