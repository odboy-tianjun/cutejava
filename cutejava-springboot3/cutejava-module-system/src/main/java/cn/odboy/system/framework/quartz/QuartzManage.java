/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.framework.quartz;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.model.SystemQuartzJobVo;
import java.util.Date;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuartzManage {

  private static final String JOB_NAME = "TASK_";
  @Resource
  private Scheduler scheduler;

  public void addJob(SystemQuartzJobVo quartzJob) {
    try {
      // 构建job信息
      JobDetail jobDetail =
          JobBuilder.newJob(ExecutionJobBean.class).withIdentity(JOB_NAME + quartzJob.getId()).build();
      // 通过触发器名和cron 表达式创建 Trigger
      Trigger trigger = TriggerBuilder.newTrigger().withIdentity(JOB_NAME + quartzJob.getId()).startNow()
          .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression())).build();
      trigger.getJobDataMap().put(SystemQuartzJobTb.JOB_KEY, quartzJob);
      // 重置启动时间
      ((CronTriggerImpl) trigger).setStartTime(new Date());
      // 执行定时任务，如果是持久化的，这里会报错，捕获输出
      try {
        scheduler.scheduleJob(jobDetail, trigger);
      } catch (ObjectAlreadyExistsException e) {
        log.warn("定时任务已存在，跳过加载");
      }
      // 暂停任务
      if (quartzJob.getIsPause()) {
        pauseJob(quartzJob);
      }
    } catch (Exception e) {
      log.error("创建定时任务失败", e);
      throw new BadRequestException("创建定时任务失败");
    }
  }

  /**
   * 更新job cron表达式
   *
   * @param quartzJob /
   */
  public void updateJobCron(SystemQuartzJobVo quartzJob) {
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      // 如果不存在则创建一个定时任务
      if (trigger == null) {
        addJob(quartzJob);
        trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      }
      CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
      trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
      // 重置启动时间
      ((CronTriggerImpl) trigger).setStartTime(new Date());
      trigger.getJobDataMap().put(SystemQuartzJobTb.JOB_KEY, quartzJob);
      scheduler.rescheduleJob(triggerKey, trigger);
      // 暂停任务
      if (quartzJob.getIsPause()) {
        pauseJob(quartzJob);
      }
    } catch (Exception e) {
      log.error("更新定时任务失败", e);
      throw new BadRequestException("更新定时任务失败");
    }
  }

  /**
   * 删除一个job
   *
   * @param quartzJob /
   */
  public void deleteJob(SystemQuartzJobTb quartzJob) {
    try {
      JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
      scheduler.pauseJob(jobKey);
      scheduler.deleteJob(jobKey);
    } catch (Exception e) {
      log.error("删除定时任务失败", e);
      throw new BadRequestException("删除定时任务失败");
    }
  }

  /**
   * 恢复一个job
   *
   * @param quartzJob /
   */
  public void resumeJob(SystemQuartzJobVo quartzJob) {
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      // 如果不存在则创建一个定时任务
      if (trigger == null) {
        addJob(quartzJob);
      }
      JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
      scheduler.resumeJob(jobKey);
    } catch (Exception e) {
      log.error("恢复定时任务失败", e);
      throw new BadRequestException("恢复定时任务失败");
    }
  }

  /**
   * 立即执行job
   *
   * @param quartzJob /
   */
  public void runJobNow(SystemQuartzJobVo quartzJob) {
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      // 如果不存在则创建一个定时任务
      if (trigger == null) {
        addJob(quartzJob);
      }
      JobDataMap dataMap = new JobDataMap();
      dataMap.put(SystemQuartzJobTb.JOB_KEY, quartzJob);
      JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
      scheduler.triggerJob(jobKey, dataMap);
    } catch (Exception e) {
      log.error("定时任务执行失败", e);
      throw new BadRequestException("定时任务执行失败");
    }
  }

  /**
   * 暂停一个job
   *
   * @param quartzJob /
   */
  public void pauseJob(SystemQuartzJobVo quartzJob) {
    try {
      JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
      scheduler.pauseJob(jobKey);
    } catch (Exception e) {
      log.error("定时任务暂停失败", e);
      throw new BadRequestException("定时任务暂停失败");
    }
  }
}
