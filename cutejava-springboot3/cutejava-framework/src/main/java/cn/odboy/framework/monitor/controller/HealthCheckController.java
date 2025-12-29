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

package cn.odboy.framework.monitor.controller;

import cn.odboy.annotation.AnonymousGetMapping;
import cn.odboy.framework.monitor.service.KitHealthCheckPointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口
 */
@RestController
@RequestMapping("/healthCheck")
@Tag(name = "系统：健康检查接口")
public class HealthCheckController {
    @Autowired private KitHealthCheckPointService kitHealthCheckPointService;

    /**
     * 就绪检查
     */
    @AnonymousGetMapping(value = "/readiness")
    public ResponseEntity<?> doReadiness() {
        return kitHealthCheckPointService.doReadiness();
    }

    /**
     * 存活检查
     */
    @AnonymousGetMapping(value = "/liveness")
    public ResponseEntity<?> doLiveness() {
        return kitHealthCheckPointService.doLiveness();
    }

    //    /**
    //     * 访问首页提示
    //     */
    //    @AnonymousGetMapping("/")
    //    public String index() {
    //        return "success";
    //    }
}
