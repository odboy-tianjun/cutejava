// /*
//  * Copyright 2021-2025 Odboy
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  * http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//
// package cn.odboy.framework.exception.thirdapi;
//
// import cn.hutool.core.date.TimeInterval;
// import cn.odboy.framework.exception.thirdapi.vo.KubernetesApiExceptionVo;
// import cn.odboy.framework.exception.web.BadRequestException;
// import cn.odboy.util.CsReturnValueHandleUtil;
// import cn.odboy.util.LogFmtUtil;
// import com.alibaba.fastjson2.JSON;
// import com.aliyun.tea.TeaException;
// import io.kubernetes.client.openapi.ApiException;
// import lombok.extern.slf4j.Slf4j;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.gitlab4j.api.GitLabApiException;
// import org.springframework.stereotype.Component;
//
// /**
//  * DingtalkApi调用异常
//  *
//  * @author odboy
//  * @date 2025-05-07
//  */
// @Slf4j
// @Aspect
// @Component
// public class ThirdExceptionCatchAspect {
//   @Around("@annotation(thirdApiExceptionCatch)")
//   public Object aroundDingtalkApiExceptionCatch(ProceedingJoinPoint joinPoint, ThirdApiExceptionCatch thirdApiExceptionCatch) throws Throwable {
//     return handleThirdApiException(joinPoint, thirdApiExceptionCatch);
//   }
//
//   /**
//    * 三方服务调用异常处理
//    */
//   private Object handleThirdApiException(ProceedingJoinPoint joinPoint, ThirdApiExceptionCatch annotation) {
//     if ("dingtalk".equals(annotation.modelName())) {
//       return handleDingtalkApiException(joinPoint, annotation);
//     }
//     if ("gitlab".equals(annotation.modelName())) {
//       return handleGitlabApiException(joinPoint, annotation);
//     }
//     if ("kubernetes".equals(annotation.modelName())) {
//       return handleKubernetesApiException(joinPoint, annotation);
//     }
//     return null;
//   }
//
//   private Object handleDingtalkApiException(ProceedingJoinPoint joinPoint, ThirdApiExceptionCatch annotation) {
//     TimeInterval timeInterval = new TimeInterval();
//     try {
//       Object result = joinPoint.proceed();
//       log.info("接口 [{}] 执行耗时: {} ms", annotation.description(), timeInterval.intervalMs());
//       return result;
//     } catch (TeaException teaException) {
//       if (!com.aliyun.teautil.Common.empty(teaException.code) && !com.aliyun.teautil.Common.empty(teaException.message)) {
//         String exceptionMessage = annotation.description() + "失败, code={}, message={}";
//         log.error(exceptionMessage, teaException.code, teaException.message, teaException);
//         if (annotation.throwException()) {
//           throw new BadRequestException(LogFmtUtil.format(exceptionMessage, teaException.code, teaException.message));
//         }
//       }
//       String exceptionMessage = annotation.description() + "失败";
//       log.error(exceptionMessage, teaException);
//       if (annotation.throwException()) {
//         throw new BadRequestException(exceptionMessage);
//       }
//     } catch (Throwable exception) {
//       TeaException err = new TeaException(exception.getMessage(), exception);
//       if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//         String exceptionMessage = annotation.description() + "失败, code={}, message={}";
//         log.error(exceptionMessage, err.code, err.message, err);
//         if (annotation.throwException()) {
//           throw new BadRequestException(LogFmtUtil.format(exceptionMessage, err.code, err.message));
//         }
//       }
//       String exceptionMessage = annotation.description() + "失败";
//       log.error(exceptionMessage, exception);
//       if (annotation.throwException()) {
//         throw new BadRequestException(exceptionMessage);
//       }
//     }
//     return CsReturnValueHandleUtil.getDefaultValue(joinPoint);
//   }
//
//   private Object handleGitlabApiException(ProceedingJoinPoint joinPoint, ThirdApiExceptionCatch annotation) {
//     TimeInterval timeInterval = new TimeInterval();
//     try {
//       Object result = joinPoint.proceed();
//       log.info("接口 [{}] 执行耗时: {} ms", annotation.description(), timeInterval.intervalMs());
//       return result;
//     } catch (GitLabApiException gitLabApiException) {
//       String exceptionMessage = annotation.description() + "失败，原因：" + gitLabApiException.getMessage();
//       log.error(exceptionMessage, gitLabApiException);
//       if (annotation.throwException()) {
//         throw new BadRequestException(exceptionMessage);
//       }
//     } catch (Throwable exception) {
//       String exceptionMessage = annotation.description() + "失败";
//       log.error(exceptionMessage, exception);
//       if (annotation.throwException()) {
//         throw new BadRequestException(exceptionMessage);
//       }
//     }
//     return CsReturnValueHandleUtil.getDefaultValue(joinPoint);
//   }
//
//   private Object handleKubernetesApiException(ProceedingJoinPoint joinPoint, ThirdApiExceptionCatch annotation) {
//     TimeInterval timeInterval = new TimeInterval();
//     try {
//       Object result = joinPoint.proceed();
//       log.info("接口 [{}] 执行耗时: {} ms", annotation.description(), timeInterval.intervalMs());
//       return result;
//     } catch (ApiException e) {
//       String responseBody = e.getResponseBody();
//       log.error("接口 [{}] 执行异常，耗时: {} ms，异常信息: {}", annotation.description(), timeInterval.intervalMs(), responseBody, e);
//       if (annotation.throwException()) {
//         KubernetesApiExceptionVo actionExceptionBody = JSON.parseObject(responseBody, KubernetesApiExceptionVo.class);
//         if (actionExceptionBody != null) {
//           throw new BadRequestException(annotation.description() + "失败, 原因：" + actionExceptionBody.getReason());
//         }
//         throw new BadRequestException(annotation.description() + "失败");
//       }
//     } catch (Throwable e) {
//       log.error("接口 [{}] 执行异常，耗时: {} ms", annotation.description(), timeInterval.intervalMs(), e);
//       if (annotation.throwException()) {
//         throw new BadRequestException(annotation.description() + "失败");
//       }
//     }
//     return CsReturnValueHandleUtil.getDefaultValue(joinPoint);
//   }
// }
