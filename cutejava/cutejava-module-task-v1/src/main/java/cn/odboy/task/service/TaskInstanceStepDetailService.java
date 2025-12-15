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
package cn.odboy.task.service;

import cn.odboy.task.dal.dataobject.TaskInstanceStepDetailTb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 任务实例步骤明细 服务类
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
public interface TaskInstanceStepDetailService extends IService<TaskInstanceStepDetailTb> {
    /**
     * 步骤执行成功
     *
     * @param instanceDetailId 任务实例明细id
     * @param stepDesc         步骤描述
     */
    void success(Long instanceDetailId, String stepDesc);

    /**
     * 步骤执行失败
     *
     * @param instanceDetailId 任务实例明细id
     * @param stepDesc         步骤描述
     */
    void fail(Long instanceDetailId, String stepDesc);

    /**
     * 根据明细ID删除步骤明细
     *
     * @param instanceDetailIds 任务实例明细id集合
     */
    void removeByInstanceDetailIds(List<Long> instanceDetailIds);
}
