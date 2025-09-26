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
package cn.odboy.task.service.impl;

import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.mysql.TaskInstanceInfoMapper;
import cn.odboy.task.service.TaskInstanceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务实例 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Service
public class TaskInstanceInfoServiceImpl extends ServiceImpl<TaskInstanceInfoMapper, TaskInstanceInfoTb> implements TaskInstanceInfoService {

}
