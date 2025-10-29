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
package cn.odboy.task.dal.mysql;

import cn.odboy.task.dal.dataobject.TaskTemplateInfoTb;
import cn.odboy.task.dal.model.TaskTemplateInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 任务模板 Mapper 接口
 * </p>
 *
 * @author codegen
 * @since 2025-09-28
 */
@Mapper
public interface TaskTemplateInfoMapper extends BaseMapper<TaskTemplateInfoTb> {
    /**
     * @param envAlias    环境别称
     * @param contextName 应用名称、资源类型
     * @param language    语言、版本
     * @param changeType  变更类型
     * @return /
     */
    TaskTemplateInfoVo selectTemplateInfoByECL(
            @Param("envAlias") String envAlias,
            @Param("contextName") String contextName,
            @Param("language") String language,
            @Param("changeType") String changeType
    );
}
