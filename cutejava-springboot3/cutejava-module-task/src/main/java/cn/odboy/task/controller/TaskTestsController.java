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
package cn.odboy.task.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.annotation.AnonymousAccess;
import cn.odboy.task.constant.TaskChangeTypeEnum;
import cn.odboy.task.core.TaskManage;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.model.TaskInstanceInfoVo;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskTestsController {
    @Autowired
    private TaskManage taskManage;

    @AnonymousAccess
    @GetMapping(value = "/testCreate")
    public ResponseEntity<?> testCreate() {
        TaskInstanceInfoTb instanceInfo = taskManage.createJob("cutejava", TaskChangeTypeEnum.AppContainerDeploy, "java", "daily", "cutejava", "功能测试", null);
        log.info("任务创建成功，实例为：{}", JSON.toJSONString(instanceInfo));
        return ResponseEntity.ok(instanceInfo);
    }

    @AnonymousAccess
    @GetMapping(value = "/testCreateAfterStop")
    public ResponseEntity<?> testCreateAfterStop() {
        TaskInstanceInfoTb instanceInfo = taskManage.createJob("cutejava", TaskChangeTypeEnum.AppContainerDeploy, "java", "daily", "cutejava", "功能测试", null);
        log.info("任务创建成功，实例为：{}", JSON.toJSONString(instanceInfo));
        Thread.startVirtualThread(() -> {
            ThreadUtil.safeSleep(5000);
            taskManage.stopJob(instanceInfo.getId());
        });
        return ResponseEntity.ok(null);
    }

    @AnonymousAccess
    @GetMapping(value = "/testRetry")
    public ResponseEntity<?> testRetry(@RequestParam Long instanceId, @RequestParam String retryNodeCode) {
        TaskInstanceInfoTb instanceInfo = taskManage.retryJob(instanceId, retryNodeCode);
        log.info("任务重试成功，实例为：{}", JSON.toJSONString(instanceInfo));
        return ResponseEntity.ok(null);
    }

    @AnonymousAccess
    @GetMapping(value = "/last")
    public ResponseEntity<?> testLast() {
        String contextName = "cutejava";
        String language = "java";
        String changeType = TaskChangeTypeEnum.AppContainerDeploy.getCode();
        String envAlias = "daily";
        TaskInstanceInfoVo instanceInfo = taskManage.getLastInfo(contextName, language, envAlias, changeType);
        return ResponseEntity.ok(instanceInfo);
    }
}
