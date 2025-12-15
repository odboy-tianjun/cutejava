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

import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.JobDataMap;

import java.util.List;

/**
 * <p>
 * 任务实例明细 服务类
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
public interface TaskInstanceDetailService extends IService<TaskInstanceDetailTb> {
    void fastFailWithInfo(Long instanceId, String code, String message);

    void fastSuccess(Long instanceId, String code);

    void fastSuccessWithInfo(Long instanceId, String code, String executeInfo);

    void fastStart(Long instanceId, String code, JobDataMap dataMap);

    List<TaskInstanceDetailTb> queryByInstanceIdAndBizCodeList(Long instanceId, List<String> bizCodes);

    List<TaskInstanceDetailTb> queryByInstanceId(Long instanceId);
}
