package cn.odboy.util;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.odboy.domain.dto.EmailDto;
import cn.odboy.mapper.QuartzLogMapper;
import cn.odboy.domain.QuartzJob;
import cn.odboy.domain.QuartzLog;
import cn.odboy.service.QuartzJobService;
import cn.odboy.service.EmailService;
import cn.odboy.context.SpringBeanHolder;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class ExecutionJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 此处仅供参考，可根据任务执行情况自定义线程池参数
    private final ThreadPoolTaskExecutor executor = SpringBeanHolder.getBean("taskAsync");

    @Override
    public void executeInternal(JobExecutionContext context) {
        // 获取任务
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        // 获取spring bean
        QuartzLogMapper quartzLogMapper = SpringBeanHolder.getBean(QuartzLogMapper.class);
        QuartzJobService quartzJobService = SpringBeanHolder.getBean(QuartzJobService.class);
        RedisUtil redisUtil = SpringBeanHolder.getBean(RedisUtil.class);

        String uuid = quartzJob.getUuid();

        QuartzLog log = new QuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = executor.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            if (StringUtil.isNotBlank(uuid)) {
                redisUtil.set(uuid, true);
            }
            // 任务状态
            log.setIsSuccess(true);
            logger.info("任务执行成功，任务名称：{}, 执行时间：{}毫秒", quartzJob.getJobName(), times);
            // 判断是否存在子任务
            if (StringUtil.isNotBlank(quartzJob.getSubTask())) {
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                // 执行子任务
                quartzJobService.executionSubJob(tasks);
            }
        } catch (Exception e) {
            if (StringUtil.isNotBlank(uuid)) {
                redisUtil.set(uuid, false);
            }
            logger.error("任务执行失败，任务名称：{}", quartzJob.getJobName());
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                //更新状态
                quartzJobService.updateIsPause(quartzJob);
            }
            if (quartzJob.getEmail() != null) {
                EmailService emailService = SpringBeanHolder.getBean(EmailService.class);
                // 邮箱报警
                if (StringUtil.isNoneBlank(quartzJob.getEmail())) {
                    EmailDto emailDto = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
                    emailService.send(emailDto, emailService.find());
                }
            }
        } finally {
            quartzLogMapper.insert(log);
        }
    }

    private EmailDto taskAlarm(QuartzJob quartzJob, String msg) {
        EmailDto emailDto = new EmailDto();
        emailDto.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("taskAlarm.ftl");
        emailDto.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        emailDto.setTos(emails);
        return emailDto;
    }
}
