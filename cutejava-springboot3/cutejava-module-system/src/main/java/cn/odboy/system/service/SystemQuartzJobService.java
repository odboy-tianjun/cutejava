/*
 * Copyright 2021-2026 Odboy
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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.context.KitSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.dal.dataobject.SystemQuartzJobTb;
import cn.odboy.system.dal.dataobject.SystemQuartzLogTb;
import cn.odboy.system.dal.model.export.SystemQuartzJobExportRowVo;
import cn.odboy.system.dal.model.export.SystemQuartzLogExportRowVo;
import cn.odboy.system.dal.model.request.SystemQueryQuartzJobArgs;
import cn.odboy.system.dal.model.request.SystemUpdateQuartzJobArgs;
import cn.odboy.system.dal.model.response.SystemQuartzJobVo;
import cn.odboy.system.dal.mysql.SystemQuartzJobMapper;
import cn.odboy.system.dal.mysql.SystemQuartzLogMapper;
import cn.odboy.system.framework.quartz.QuartzManage;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
   * 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
   *
   * @param beanName Bean名称
   */
  private void checkBean(String beanName) {
    // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
    // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
    if (!KitSpringBeanHolder.getAllServiceBeanName().contains(beanName)) {
      throw new BadRequestException("非法的 Bean，请重新输入！");
    }
  }

  /**
   * 创建 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void createJob(SystemQuartzJobTb args) {
    if (args.getId() != null) {
      throw new BadRequestException("无效参数id");
    }
    // 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
    checkBean(args.getBeanName());
    if (!CronExpression.isValidExpression(args.getCronExpression())) {
      throw new BadRequestException("cron表达式格式错误");
    }
    systemQuartzJobMapper.insert(args);
    SystemQuartzJobVo quartzJobVo = KitBeanUtil.copyToClass(args, SystemQuartzJobVo.class);
    quartzManage.addJob(quartzJobVo);
  }

  /**
   * 修改任务并重新调度 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateQuartzJobResumeCron(SystemUpdateQuartzJobArgs args) {
    // 验证Bean是不是合法的, 合法的定时任务 Bean 需要用 @Service 定义
    checkBean(args.getBeanName());
    if (!CronExpression.isValidExpression(args.getCronExpression())) {
      throw new BadRequestException("cron表达式格式错误");
    }
    if (StrUtil.isNotBlank(args.getSubTask())) {
      List<String> tasks = Arrays.asList(args.getSubTask().split("[,，]"));
      if (tasks.contains(args.getId().toString())) {
        throw new BadRequestException("子任务中不能添加当前任务ID");
      }
    }
    SystemQuartzJobTb jobTb = KitBeanUtil.copyToClass(args, SystemQuartzJobTb.class);
    systemQuartzJobMapper.updateById(jobTb);
    quartzManage.updateJobCron(jobTb);
  }

  /**
   * 更改定时任务状态 -> TestPassed
   *
   * @param quartzJobVo /
   */
  @Transactional(rollbackFor = Exception.class)
  public void switchQuartzJobStatus(SystemQuartzJobVo quartzJobVo) {
    // 置换暂停状态
    if (quartzJobVo.getIsPause()) {
      quartzManage.resumeJob(quartzJobVo);
      quartzJobVo.setIsPause(false);
    } else {
      quartzManage.pauseJob(quartzJobVo);
      quartzJobVo.setIsPause(true);
    }
    SystemQuartzJobTb record = KitBeanUtil.copyToClass(quartzJobVo, SystemQuartzJobTb.class);
    systemQuartzJobMapper.updateById(record);
  }

  /**
   * 立即执行定时任务 -> TestPassed
   *
   * @param quartzJob /
   */
  public void startQuartzJob(SystemQuartzJobVo quartzJob) {
    quartzManage.runJobNow(quartzJob);
  }

  /**
   * 删除任务 -> TestPassed
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteJobByIds(Set<Long> ids) {
    for (Long id : ids) {
      SystemQuartzJobTb quartzJob = systemQuartzJobMapper.selectById(id);
      quartzManage.deleteJob(quartzJob);
      systemQuartzJobMapper.deleteById(quartzJob);
    }
  }

  /**
   * 执行子任务 -> TestPassed
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
      SystemQuartzJobTb quartzJobTb = systemQuartzJobMapper.selectById(id);
      if (quartzJobTb == null) {
        // 防止子任务不存在
        continue;
      }
      SystemQuartzJobVo quartzJobVo = KitBeanUtil.copyToClass(quartzJobTb, SystemQuartzJobVo.class);
      // 执行任务
      String uuid = IdUtil.simpleUUID();
      quartzJobVo.setUuid(uuid);
      startQuartzJob(quartzJobVo);
      // 查询执行状态, 如果执行失败则停止后面的子任务执行
      Boolean result = redisHelper.get(uuid, Boolean.class);
      while (result == null) {
        // 休眠5秒, 再次查询子任务执行情况
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
   * 分页查询 -> TestPassed
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemQuartzJobTb> searchQuartzJobByArgs(SystemQueryQuartzJobArgs args, Page<SystemQuartzJobTb> page) {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzJobQueryParams(args, wrapper);
    return KitPageUtil.toPage(systemQuartzJobMapper.selectPage(page, wrapper));
  }

  private void injectQuartzJobQueryParams(SystemQueryQuartzJobArgs args, LambdaQueryWrapper<SystemQuartzJobTb> wrapper) {
    if (args != null) {
      wrapper.like(StrUtil.isNotBlank(args.getJobName()), SystemQuartzJobTb::getJobName, args.getJobName());
      if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
        wrapper.between(SystemQuartzJobTb::getUpdateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemQuartzJobTb::getId);
  }

  private void injectQuartzLogQueryParams(SystemQueryQuartzJobArgs args, LambdaQueryWrapper<SystemQuartzLogTb> wrapper) {
    if (args != null) {
      wrapper.like(StrUtil.isNotBlank(args.getJobName()), SystemQuartzLogTb::getJobName, args.getJobName());
      wrapper.eq(args.getIsSuccess() != null, SystemQuartzLogTb::getIsSuccess, args.getIsSuccess());
      if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
        wrapper.between(SystemQuartzLogTb::getCreateTime, args.getCreateTime().get(0),
            args.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemQuartzLogTb::getId);
  }

  /**
   * 分页查询日志 -> TestPassed
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemQuartzLogTb> searchQuartzLogByArgs(SystemQueryQuartzJobArgs args, Page<SystemQuartzLogTb> page) {
    LambdaQueryWrapper<SystemQuartzLogTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzLogQueryParams(args, wrapper);
    return KitPageUtil.toPage(systemQuartzLogMapper.selectPage(page, wrapper));
  }

  /**
   * 查询全部 -> TestPassed
   *
   * @param args 条件
   * @return /
   */
  public List<SystemQuartzJobTb> queryQuartzJobByArgs(SystemQueryQuartzJobArgs args) {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzJobQueryParams(args, wrapper);
    return systemQuartzJobMapper.selectList(wrapper);
  }

  /**
   * 查询全部 -> TestPassed
   *
   * @param args 条件
   * @return /
   */
  public List<SystemQuartzLogTb> queryQuartzLogByArgs(SystemQueryQuartzJobArgs args) {
    LambdaQueryWrapper<SystemQuartzLogTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQuartzLogQueryParams(args, wrapper);
    return systemQuartzLogMapper.selectList(wrapper);
  }

  /**
   * 查询启用的定时任务 -> TestPassed
   */
  public List<SystemQuartzJobTb> listEnableQuartzJob() {
    LambdaQueryWrapper<SystemQuartzJobTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemQuartzJobTb::getIsPause, 0);
    return systemQuartzJobMapper.selectList(wrapper);
  }

  /**
   * 导出定时任务数据 -> TestPassed
   */
  public void exportQuartzJobXlsx(HttpServletResponse response, SystemQueryQuartzJobArgs args) {
    List<SystemQuartzJobTb> systemQuartzJobTbs = this.queryQuartzJobByArgs(args);
    List<SystemQuartzJobExportRowVo> rowVos = new ArrayList<>();
    for (SystemQuartzJobTb dataObject : systemQuartzJobTbs) {
      SystemQuartzJobExportRowVo rowVo = new SystemQuartzJobExportRowVo();
      rowVo.setJobName(dataObject.getJobName());
      rowVo.setBeanName(dataObject.getBeanName());
      rowVo.setMethodName(dataObject.getMethodName());
      rowVo.setParams(dataObject.getParams());
      rowVo.setCronExpression(dataObject.getCronExpression());
      rowVo.setIsPause(dataObject.getIsPause() ? "暂停中" : "运行中");
      rowVo.setDescription(dataObject.getDescription());
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "定时任务数据", SystemQuartzJobExportRowVo.class, rowVos);
  }

  /**
   * 导出定时任务日志数据 -> TestPassed
   */
  public void exportQuartzLogXlsx(HttpServletResponse response, SystemQueryQuartzJobArgs args) {
    List<SystemQuartzLogTb> systemQuartzLogTbs = this.queryQuartzLogByArgs(args);
    List<SystemQuartzLogExportRowVo> rowVos = new ArrayList<>();
    for (SystemQuartzLogTb dataObject : systemQuartzLogTbs) {
      SystemQuartzLogExportRowVo rowVo = new SystemQuartzLogExportRowVo();
      rowVo.setJobName(dataObject.getJobName());
      rowVo.setBeanName(dataObject.getBeanName());
      rowVo.setMethodName(dataObject.getMethodName());
      rowVo.setParams(dataObject.getParams());
      rowVo.setCronExpression(dataObject.getCronExpression());
      rowVo.setExceptionDetail(dataObject.getExceptionDetail());
      rowVo.setTime(dataObject.getTime());
      rowVo.setStatus(dataObject.getIsSuccess() ? "成功" : "失败");
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "定时任务日志数据", SystemQuartzLogExportRowVo.class, rowVos);
  }

  /**
   * 根据任务id查询定时任务 -> TestPassed
   */
  public SystemQuartzJobTb getQuartzJobById(Long id) {
    return systemQuartzJobMapper.selectById(id);
  }
}
