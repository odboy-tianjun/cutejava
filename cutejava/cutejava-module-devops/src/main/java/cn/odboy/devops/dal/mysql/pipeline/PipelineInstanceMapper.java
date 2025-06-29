package cn.odboy.devops.dal.mysql.pipeline;

import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 流水线实例 Mapper 接口
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Mapper
public interface PipelineInstanceMapper extends BaseMapper<PipelineInstanceTb> {
    void updateStatusByInstanceId(@Param("status") String status, @Param("pipelineInstanceId") long pipelineInstanceId);

    void updateCurrentNodeByInstanceId(@Param("code") String code, @Param("status") String status, @Param("pipelineInstanceId") long pipelineInstanceId);
}
