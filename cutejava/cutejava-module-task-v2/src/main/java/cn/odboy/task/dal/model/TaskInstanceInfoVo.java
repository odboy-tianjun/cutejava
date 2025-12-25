package cn.odboy.task.dal.model;

import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
