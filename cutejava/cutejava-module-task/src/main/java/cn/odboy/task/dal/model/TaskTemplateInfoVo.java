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
package cn.odboy.task.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTemplateInfoVo extends CsObject {
    private String changeType;
    private String name;
    private String description;
    private String envAlias;
    private String language;
    private String contextName;
    private String templateInfo;
}
