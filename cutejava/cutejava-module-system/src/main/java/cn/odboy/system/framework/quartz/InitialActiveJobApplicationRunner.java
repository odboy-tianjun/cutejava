package cn.odboy.system.framework.quartz;

import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.mysql.SystemQuartzJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InitialActiveJobApplicationRunner implements ApplicationRunner {
    @Autowired
    private SystemQuartzJobMapper quartzJobMapper;
    @Autowired
    private QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        List<SystemQuartzJobTb> quartzJobs = quartzJobMapper.selectAllEnableQuartzJob();
        quartzJobs.forEach(quartzManage::addJob);
        log.info("Timing task injection complete");
    }
}
