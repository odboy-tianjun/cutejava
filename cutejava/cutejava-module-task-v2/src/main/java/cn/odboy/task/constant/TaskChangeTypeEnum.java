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
package cn.odboy.task.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务变更类型(固定值)
 *
 * @author odboy
 * @date 2025-09-26
 */
@Getter
@AllArgsConstructor
public enum TaskChangeTypeEnum {
  AppApply("AppApply", "申请应用"), AppContainerRestart("AppContainerRestart", "容器应用重启"),
  AppContainerDeploy("AppContainerDeploy", "容器应用部署"), AppOffline("AppOffline", "应用下线"),
  AppChangeReplicas("AppChangeReplicas", "应用扩缩容"), AppUpgrade("AppUpgrade", "应用升配"),
  AppDowngrade("AppDowngrade", "应用降配"), AppHostRestart("AppHostRestart", "主机应用重启"),
  AppHostDeploy("AppHostDeploy", "主机应用部署"), AppContainerRollback("AppContainerRollback", "容器应用回滚"),
  AppHostRollback("AppHostRollback", "主机应用回滚"), ResourceApply("ResourceApply", "申请资源"),
  ResourceOffline("ResourceOffline", "资源下线"), DataChange("DataChange", "数据变更"),
  HostnameApply("HostnameApply", "申请域名"), HostnameChange("HostnameChange", "域名变更");
  private final String code;
  private final String name;
}
