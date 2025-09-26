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
