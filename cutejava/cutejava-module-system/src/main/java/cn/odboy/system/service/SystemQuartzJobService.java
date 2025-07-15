package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.redis.RedisHelper;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.SystemQueryQuartzJobArgs;
import cn.odboy.system.dal.model.SystemUpdateQuartzJobArgs;
import cn.odboy.system.dal.mysql.SystemQuartzJobMapper;
import cn.odboy.system.dal.mysql.SystemQuartzLogMapper;
import cn.odboy.system.framework.quartz.QuartzManage;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SystemQuartzJobService {
    private final SystemQuartzJobMapper systemQuartzJobMapper;
    private final SystemQuartzLogMapper systemQuartzLogMapper;
    private final QuartzManage quartzManage;
    private final RedisHelper redisHelper;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void createJob(SystemQuartzJobTb resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        systemQuartzJobMapper.insert(resources);
        quartzManage.addJob(resources);
    }

    /**
     * 修改任务并重新调度
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyQuartzJobResumeCron(SystemUpdateQuartzJobArgs resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        if (StringUtil.isNotBlank(resources.getSubTask())) {
            List<String> tasks = Arrays.asList(resources.getSubTask().split("[,，]"));
            if (tasks.contains(resources.getId().toString())) {
                throw new BadRequestException("子任务中不能添加当前任务ID");
            }
        }
        SystemQuartzJobTb quartzJob = BeanUtil.copyProperties(resources, SystemQuartzJobTb.class);
        systemQuartzJobMapper.insertOrUpdate(quartzJob);
        quartzManage.updateJobCron(quartzJob);
    }

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */

    @Transactional(rollbackFor = Exception.class)
    public void switchQuartzJobStatus(SystemQuartzJobTb quartzJob) {
        // 置换暂停状态
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        systemQuartzJobMapper.insertOrUpdate(quartzJob);
    }

    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */

    public void startQuartzJob(SystemQuartzJobTb quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    /**
     * 删除任务
     *
     * @param ids /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeJobByIds(Set<Long> ids) {
        for (Long id : ids) {
            SystemQuartzJobTb quartzJob = systemQuartzJobMapper.selectById(id);
            quartzManage.deleteJob(quartzJob);
            systemQuartzJobMapper.deleteById(quartzJob);
        }
    }

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws InterruptedException /
     */

    @Transactional(rollbackFor = Exception.class)
    public void startSubQuartJob(String[] tasks) throws InterruptedException {
        for (String id : tasks) {
            if (StrUtil.isBlank(id)) {
                // 如果是手动清除子任务id, 会出现id为空字符串的问题
                continue;
            }
            SystemQuartzJobTb quartzJob = systemQuartzJobMapper.selectById(Long.parseLong(id));
            if (quartzJob == null) {
                // 防止子任务不存在
                continue;
            }
            // 执行任务
            String uuid = IdUtil.simpleUUID();
            quartzJob.setUuid(uuid);
            // 执行任务
            startQuartzJob(quartzJob);
            // 获取执行状态, 如果执行失败则停止后面的子任务执行
            Boolean result = redisHelper.get(uuid, Boolean.class);
            while (result == null) {
                // 休眠5秒, 再次获取子任务执行情况
                Thread.sleep(5000);
                result = redisHelper.get(uuid, Boolean.class);
            }
            if (!result) {
                redisHelper.del(uuid);
                break;
            }
        }
    }

    /**
     * 导出定时任务
     *
     * @param quartzJobs 待导出的数据
     * @param response   /
     * @throws IOException /
     */

    public void exportQuartzJobExcel(List<SystemQuartzJobTb> quartzJobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemQuartzJobTb quartzJob : quartzJobs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzJob.getJobName());
            map.put("Bean名称", quartzJob.getBeanName());
            map.put("执行方法", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("表达式", quartzJob.getCronExpression());
            map.put("状态", quartzJob.getIsPause() ? "暂停中" : "运行中");
            map.put("描述", quartzJob.getDescription());
            map.put("创建日期", quartzJob.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 导出定时任务日志
     *
     * @param queryAllLog 待导出的数据
     * @param response    /
     * @throws IOException /
     */

    public void exportQuartzLogExcel(List<SystemQuartzLogTb> queryAllLog, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemQuartzLogTb quartzLog : queryAllLog) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", quartzLog.getIsSuccess() ? "成功" : "失败");
            map.put("创建日期", quartzLog.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsResultVo<List<SystemQuartzJobTb>> queryQuartzJobByArgs(SystemQueryQuartzJobArgs criteria, Page<SystemQuartzJobTb> page) {
        return CsPageUtil.toPage(systemQuartzJobMapper.selectQuartzJobByArgs(criteria, page));
    }

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsResultVo<List<SystemQuartzLogTb>> queryQuartzLogByArgs(SystemQueryQuartzJobArgs criteria, Page<SystemQuartzLogTb> page) {
        return CsPageUtil.toPage(systemQuartzLogMapper.selectQuartzLogByArgs(criteria, page));
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */

    public List<SystemQuartzJobTb> queryQuartzJobByArgs(SystemQueryQuartzJobArgs criteria) {
        return systemQuartzJobMapper.selectQuartzJobByArgs(criteria, CsPageUtil.getCount(systemQuartzJobMapper)).getRecords();
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */

    public List<SystemQuartzLogTb> queryQuartzLogByArgs(SystemQueryQuartzJobArgs criteria) {
        return systemQuartzLogMapper.selectQuartzLogByArgs(criteria, CsPageUtil.getCount(systemQuartzLogMapper)).getRecords();
    }

    public SystemQuartzJobTb getQuartzJobById(Long id) {
        return systemQuartzJobMapper.selectById(id);
    }
}
