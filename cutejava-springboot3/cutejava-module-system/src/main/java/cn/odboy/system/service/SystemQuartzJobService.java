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
package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.SystemQueryQuartzJobArgs;
import cn.odboy.system.dal.model.SystemUpdateQuartzJobArgs;
import cn.odboy.system.dal.mysql.SystemQuartzJobMapper;
import cn.odboy.system.dal.mysql.SystemQuartzLogMapper;
import cn.odboy.system.framework.quartz.QuartzManage;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.http.HttpServletResponse;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemQuartzJobService {

  @Autowired
  private SystemQuartzJobMapper systemQuartzJobMapper;
  @Autowired
  private SystemQuartzLogMapper systemQuartzLogMapper;
  @Autowired
  private QuartzManage quartzManage;
  @Autowired
  private KitRedisHelper redisHelper;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void createJob(SystemQuartzJobTb args) {
    if (!CronExpression.isValidExpression(args.getCronExpression())) {
      throw new BadRequestException("cron表达式格式错误");
    }
    systemQuartzJobMapper.insert(args);
    quartzManage.addJob(args);
  }

  /**
   * 修改任务并重新调度
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void modifyQuartzJobResumeCron(SystemUpdateQuartzJobArgs args) {
    if (!CronExpression.isValidExpression(args.getCronExpression())) {
      throw new BadRequestException("cron表达式格式错误");
    }
    if (StrUtil.isNotBlank(args.getSubTask())) {
      List<String> tasks = Arrays.asList(args.getSubTask().split("[,，]"));
      if (tasks.contains(args.getId().toString())) {
        throw new BadRequestException("子任务中不能添加当前任务ID");
      }
    }
    SystemQuartzJobTb quartzJob = BeanUtil.copyProperties(args, SystemQuartzJobTb.class);
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
  public void exportQuartzJobExcel(List<SystemQuartzJobTb> quartzJobs, HttpServletResponse response)
      throws IOException {
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
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 导出定时任务日志
   *
   * @param queryAllLog 待导出的数据
   * @param response    /
   * @throws IOException /
   */
  public void exportQuartzLogExcel(List<SystemQuartzLogTb> queryAllLog, HttpServletResponse response)
      throws IOException {
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
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param page     分页参数
   * @return /
   */
  public KitPageResult<SystemQuartzJobTb> queryQuartzJobByArgs(SystemQueryQuartzJobArgs criteria,
      Page<SystemQuartzJobTb> page) {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzJobQueryParams(criteria, wrapper);
    return KitPageUtil.toPage(systemQuartzJobMapper.selectPage(page, wrapper));
  }

  private void injectQuartzJobQueryParams(SystemQueryQuartzJobArgs criteria,
      LambdaQueryWrapper<SystemQuartzJobTb> wrapper) {
    if (criteria != null) {
      wrapper.like(StrUtil.isNotBlank(criteria.getJobName()), SystemQuartzJobTb::getJobName,
          criteria.getJobName());
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemQuartzJobTb::getUpdateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemQuartzJobTb::getId);
  }

  private void injectQuartzLogQueryParams(SystemQueryQuartzJobArgs criteria,
      LambdaQueryWrapper<SystemQuartzLogTb> wrapper) {
    if (criteria != null) {
      wrapper.like(StrUtil.isNotBlank(criteria.getJobName()), SystemQuartzLogTb::getJobName,
          criteria.getJobName());
      wrapper.eq(criteria.getIsSuccess() != null, SystemQuartzLogTb::getIsSuccess, criteria.getIsSuccess());
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemQuartzLogTb::getCreateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemQuartzLogTb::getId);
  }

  /**
   * 分页查询日志
   *
   * @param criteria 条件
   * @param page     分页参数
   * @return /
   */
  public KitPageResult<SystemQuartzLogTb> queryQuartzLogByArgs(SystemQueryQuartzJobArgs criteria,
      Page<SystemQuartzLogTb> page) {
    LambdaQueryWrapper<SystemQuartzLogTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzLogQueryParams(criteria, wrapper);
    return KitPageUtil.toPage(systemQuartzLogMapper.selectPage(page, wrapper));
  }

  /**
   * 查询全部
   *
   * @param criteria 条件
   * @return /
   */
  public List<SystemQuartzJobTb> queryQuartzJobByArgs(SystemQueryQuartzJobArgs criteria) {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzJobQueryParams(criteria, wrapper);
    return systemQuartzJobMapper.selectList(wrapper);
  }

  /**
   * 查询全部
   *
   * @param criteria 条件
   * @return /
   */
  public List<SystemQuartzLogTb> queryQuartzLogByArgs(SystemQueryQuartzJobArgs criteria) {
    LambdaQueryWrapper<SystemQuartzLogTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzLogQueryParams(criteria, wrapper);
    return systemQuartzLogMapper.selectList(wrapper);
  }

  public SystemQuartzJobTb getQuartzJobById(Long id) {
    return systemQuartzJobMapper.selectById(id);
  }

  public List<SystemQuartzJobTb> queryEnableQuartzJob() {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemQuartzJobTb::getIsPause, 0);
    return systemQuartzJobMapper.selectList(wrapper);
  }
}
