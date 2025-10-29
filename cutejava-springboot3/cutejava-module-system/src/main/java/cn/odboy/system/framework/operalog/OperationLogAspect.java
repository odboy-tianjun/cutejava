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

package cn.odboy.system.framework.operalog;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.context.CsRequestHolder;
import cn.odboy.system.dal.dataobject.SystemOperationLogTb;
import cn.odboy.system.dal.mysql.SystemOperationLogMapper;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.util.CsBrowserUtil;
import cn.odboy.util.CsIPUtil;
import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 捕捉操作日志
 *
 * @author odboy
 * @date 2025-05-12
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private SystemOperationLogMapper systemOperationLogMapper;

    @Around("@annotation(operationLog)")
    public Object operationLogCatch(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        return handleLog(joinPoint, operationLog);
    }

    private Object handleLog(ProceedingJoinPoint joinPoint, OperationLog annotation) throws Throwable {
        TimeInterval timeInterval = new TimeInterval();
        try {
            Object result = joinPoint.proceed();
            SystemOperationLogTb record = getOperationLogTb(joinPoint, annotation, timeInterval);
            ThreadUtil.execAsync(() -> {
                try {
                    systemOperationLogMapper.insert(record);
                } catch (Exception e) {
                    // 忽略
                }
            });
            return result;
        } catch (Throwable exception) {
            SystemOperationLogTb record = getOperationLogTb(joinPoint, annotation, timeInterval);
            record.setExceptionDetail(ExceptionUtil.stacktraceToString(exception));
            ThreadUtil.execAsync(() -> {
                try {
                    systemOperationLogMapper.insert(record);
                } catch (Exception e) {
                    // 忽略
                }
            });
            throw exception;
        }
    }

    private SystemOperationLogTb getOperationLogTb(ProceedingJoinPoint joinPoint, OperationLog annotation, TimeInterval timeInterval) {
        long executeTime = timeInterval.intervalMs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String bizName = annotation.bizName();
        if (StrUtil.isBlank(bizName)) {
            Method method = signature.getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                bizName = apiOperation.value();
            }
        }
        if (StrUtil.isBlank(bizName)) {
            bizName = "默认业务";
        }
        String method = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args);
        HttpServletRequest request = CsRequestHolder.getHttpServletRequest();
        String requestIp = CsBrowserUtil.getIp(request);
        String browserInfo = CsBrowserUtil.getVersion(request);
        String address = CsIPUtil.getCityInfo(requestIp);
        String username = CsSecurityHelper.getCurrentUsername();
        SystemOperationLogTb record = new SystemOperationLogTb();
        record.setBizName(bizName);
        record.setMethod(method);
        record.setParams(params);
        record.setRequestIp(requestIp);
        record.setExecuteTime(executeTime);
        record.setUsername(username);
        record.setAddress(address);
        record.setBrowserInfo(browserInfo);
        return record;
    }
}
