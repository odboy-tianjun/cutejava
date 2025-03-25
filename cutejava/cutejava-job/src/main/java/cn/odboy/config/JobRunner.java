package cn.odboy.config;

import cn.odboy.mapper.QuartzJobMapper;
import cn.odboy.model.job.domain.QuartzJob;
import cn.odboy.util.QuartzManage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        List<QuartzJob> quartzJobs = quartzJobMapper.findActiveQuartzJob();
        quartzJobs.forEach(quartzManage::addJob);
        log.info("Timing task injection complete");
    }
}
