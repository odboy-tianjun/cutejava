package cn.odboy.system.framework.quartz;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.quartz.core.QuartzRunnable;
import cn.odboy.framework.redis.RedisHelper;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.SystemSendEmailArgs;
import cn.odboy.system.dal.mysql.SystemQuartzLogMapper;
import cn.odboy.system.service.SystemEmailService;
import cn.odboy.system.service.SystemQuartzJobService;
import cn.odboy.util.CsStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
public class ExecutionJobBean extends QuartzJobBean {
    /**
     * 此处仅供参考，可根据任务执行情况自定义线程池参数
     */
    private final ThreadPoolTaskExecutor executor = CsSpringBeanHolder.getBean("taskAsync");

    @Override
    public void executeInternal(JobExecutionContext context) {
        // 获取任务
        SystemQuartzJobTb quartzJob = (SystemQuartzJobTb) context.getMergedJobDataMap().get(SystemQuartzJobTb.JOB_KEY);
        // 获取spring bean
        SystemQuartzLogMapper quartzLogMapper = CsSpringBeanHolder.getBean(SystemQuartzLogMapper.class);
        SystemQuartzJobService systemQuartzJobService = CsSpringBeanHolder.getBean(SystemQuartzJobService.class);
        RedisHelper redisHelper = CsSpringBeanHolder.getBean(RedisHelper.class);

        String uuid = quartzJob.getUuid();

        SystemQuartzLogTb quartzLog = new SystemQuartzLogTb();
        quartzLog.setJobName(quartzJob.getJobName());
        quartzLog.setBeanName(quartzJob.getBeanName());
        quartzLog.setMethodName(quartzJob.getMethodName());
        quartzLog.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        quartzLog.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = executor.submit(task);
            // 忽略任务执行结果
            future.get();
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            if (CsStringUtil.isNotBlank(uuid)) {
                redisHelper.set(uuid, true);
            }
            // 任务状态
            quartzLog.setIsSuccess(true);
            log.info("任务执行成功，任务名称：{}, 执行时间：{}毫秒", quartzJob.getJobName(), times);
            // 判断是否存在子任务
            if (CsStringUtil.isNotBlank(quartzJob.getSubTask())) {
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                // 执行子任务
                systemQuartzJobService.startSubQuartJob(tasks);
            }
        } catch (Exception e) {
            if (CsStringUtil.isNotBlank(uuid)) {
                redisHelper.set(uuid, false);
            }
            log.error("任务执行失败，任务名称：{}", quartzJob.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            // 任务状态 0：成功 1：失败
            quartzLog.setIsSuccess(false);
            quartzLog.setExceptionDetail(ExceptionUtil.stacktraceToString(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                // 更新状态
                systemQuartzJobService.switchQuartzJobStatus(quartzJob);
            }
            if (quartzJob.getEmail() != null) {
                SystemEmailService emailService = CsSpringBeanHolder.getBean(SystemEmailService.class);
                // 邮箱报警
                if (CsStringUtil.isNoneBlank(quartzJob.getEmail())) {
                    SystemSendEmailArgs sendEmailRequest = taskAlarm(quartzJob, ExceptionUtil.stacktraceToString(e));
                    emailService.sendEmail(sendEmailRequest);
                }
            }
        } finally {
            quartzLogMapper.insert(quartzLog);
        }
    }

    private SystemSendEmailArgs taskAlarm(SystemQuartzJobTb quartzJob, String msg) {
        SystemSendEmailArgs sendEmailRequest = new SystemSendEmailArgs();
        sendEmailRequest.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("SystemQuartzJobTaskAlarmTemplate.ftl");
        sendEmailRequest.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        sendEmailRequest.setTos(emails);
        return sendEmailRequest;
    }
}
