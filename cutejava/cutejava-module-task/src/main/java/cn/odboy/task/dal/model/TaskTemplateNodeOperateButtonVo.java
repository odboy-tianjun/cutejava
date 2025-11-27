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

import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 流水线节点控制按钮
 *
 * @author odboy
 */
@Getter
@Setter
public class TaskTemplateNodeOperateButtonVo extends KitObject {
    /**
     * 按钮类型（service:调用某个服务并传递参数 link:带参跳转）
     */
    private String type;
    /**
     * 按钮标题
     */
    private String title;
    /**
     * 按钮传递的参数
     */
    private String code;
    /**
     * 默认附加参数
     */
    private Map<String, String> parameters = new HashMap<>();
}