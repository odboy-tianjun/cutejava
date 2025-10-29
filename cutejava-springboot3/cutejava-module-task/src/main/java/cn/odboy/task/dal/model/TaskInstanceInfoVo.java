package cn.odboy.task.dal.model;

import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 任务实例数据
 *
 * @author odboy
 * @date 2025-10-01
 */
@Getter
@Setter
public class TaskInstanceInfoVo extends TaskInstanceInfoTb {
    /**
     * 进行中
     */
    private List<TaskInstanceNodeVo> current;
    /**
     * 历史已完成或异常
     */
    private List<TaskInstanceNodeVo> history;
}
