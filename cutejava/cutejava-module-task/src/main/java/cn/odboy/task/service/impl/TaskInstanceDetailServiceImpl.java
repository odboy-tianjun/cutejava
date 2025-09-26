package cn.odboy.task.service.impl;

import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.mysql.TaskInstanceDetailMapper;
import cn.odboy.task.service.TaskInstanceDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务实例明细 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-09-26
 */
@Service
public class TaskInstanceDetailServiceImpl extends ServiceImpl<TaskInstanceDetailMapper, TaskInstanceDetailTb> implements TaskInstanceDetailService {

}
