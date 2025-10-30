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

package cn.odboy.system.framework.bizlock;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.redis.CsRedisHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class BizLockAspect {
    private final CsRedisHelper redisHelper;
    private final ExpressionParser parser = new SpelExpressionParser();

    public BizLockAspect(CsRedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Around("@annotation(bizLock)")
    public Object around(ProceedingJoinPoint joinPoint, BizLock bizLock) throws Throwable {
        // 解析SpEL表达式生成最终的lockKey
        String lockKey = parseLockKey(joinPoint, bizLock);
        Boolean success = redisHelper.setIfAbsent(lockKey, lockKey, bizLock.leaseTime(), bizLock.timeUnit());
        if (success == null || !success) {
            throw new BadRequestException(bizLock.errorMessage());
        }
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            redisHelper.del(lockKey);
        }
    }

    private String parseLockKey(ProceedingJoinPoint joinPoint, BizLock bizLock) {
        // 如果key为空，使用默认格式: bizName:方法名
        if (bizLock.key().isEmpty()) {
            return String.format("%s:%s", bizLock.bizName(), ((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        }

        // 解析SpEL表达式
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 创建SpEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 设置方法参数
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
            // 支持#p0,#p1格式
            context.setVariable("p" + i, args[i]);
        }

        // 设置方法相关信息
        context.setVariable("methodName", method.getName());
        context.setVariable("targetClass", joinPoint.getTarget().getClass().getName());

        // 解析表达式
        Expression expression = parser.parseExpression(bizLock.key());
        String value = expression.getValue(context, String.class);

        // 组合最终的key: bizName:解析后的key
        return String.format("%s:%s", bizLock.bizName(), value);
    }
}