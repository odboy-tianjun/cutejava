package cn.odboy.application.job.config;

import cn.odboy.application.job.context.QuartzManage;
import cn.odboy.application.job.mapper.QuartzJobMapper;
import cn.odboy.model.job.domain.QuartzJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        List<QuartzJob> quartzJobs = quartzJobMapper.selectActiveQuartzJob();
        quartzJobs.forEach(quartzManage::addJob);
        log.info("Timing task injection complete");
    }
}
