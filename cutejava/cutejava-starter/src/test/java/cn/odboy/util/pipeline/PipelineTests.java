package cn.odboy.util.pipeline;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.odboy.base.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PipelineTests {
    @Getter
    @Setter
    public static class PipelineNodeVo extends MyObject {
        /**
         * Service Code
         */
        private String code;
        /**
         * 节点名称
         */
        private String name;
        /**
         * 节点明细（执行成功，失败之类的）
         */
        private String desc;
        /**
         * 节点状态（wait:未开始 running:运行中 success:执行成功 fail:执行失败）
         */
        private StatusEnum status;
        /**
         * 是否支持点击查看详情
         */
        private boolean click = false;
        /**
         * 明细详情（click=true时，判断并做出渲染方式；比如构建的时候需要查看 gitlab的构建日志，这里可以为 gitlab）
         */
        private String detailType;
        /**
         * 开始时间
         */
        private long startTimeMillis;
        /**
         * 持续时间
         */
        private long durationMillis;
        /**
         * 是否属于检查点：需要流程审批 或 人工手动确认
         */
        private boolean checkPoint = false;
        /**
         * 扩展按钮
         */
        private List<ButtonVo> buttonList;

        @Getter
        public static class ButtonVo extends MyObject {
            /**
             * 按钮类型（get:带参get请求 link:带参跳转）
             */
            private String type;
            /**
             * 按钮标题
             */
            private String title;
            /**
             * link类型:路由地址和参数
             */
            private String linkUrl;
            /**
             * link类型:是否新窗口打开
             */
            private boolean linkBlank = true;
        }

        @Getter
        @AllArgsConstructor
        public enum StatusEnum {
            WAIT("wait", "#C0C4CC", "未开始"),
            RUNNING("running", "#409EFF", "运行中"),
            SUCCESS("success", "#67C23A", "执行成功"),
            FAIL("fail", "#F56C6C", "执行失败");
            private final String code;
            private final String color;
            private final String desc;
        }
    }

    public static class ImmediateJob implements Job, InterruptableJob {
        private volatile boolean isStop = false;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            List<Object> nodeOperationList = new ArrayList<>();
            nodeOperationList.add(new Object());
            nodeOperationList.add(new Object());
            int startIndex = 0;
            int endIndex = nodeOperationList.size();
            while (!isStop) {
                try {
                    System.err.println("====== 任务节点 执行中 ======");
                    Object nodeOperation = nodeOperationList.get(startIndex);
                    if (startIndex % 2 == 0) {
                        int i = 1 / 0;
                    }
                    ThreadUtil.safeSleep(RandomUtil.randomInt(1, 5) * 1000L);
                    System.err.println("====== 任务节点 执行成功 更新执行节点子操作状态 ======");
                    startIndex++;
                    if (startIndex >= endIndex) {
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("====== 任务节点 执行失败 更新执行节点子操作状态 ======");
                    ThreadUtil.safeSleep(RandomUtil.randomInt(1, 5) * 1000L);
                }
            }
            if (isStop) {
                System.err.println("====== 任务节点强制结束 ======");
            }
        }

        @Override
        public void interrupt() throws UnableToInterruptJobException {
            this.isStop = true;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        List<PipelineNodeVo> pipelineNodes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            PipelineNodeVo pipelineNode = new PipelineNodeVo();
            pipelineNode.setCode("ServiceCode" + i);
            pipelineNode.setName("初始化" + i);
            pipelineNode.setDesc(PipelineNodeVo.StatusEnum.WAIT.getDesc());
            pipelineNode.setStatus(PipelineNodeVo.StatusEnum.WAIT);
            pipelineNode.setClick(true);
            pipelineNode.setDetailType(null);
            pipelineNode.setStartTimeMillis(DateTime.now().getTime());
            pipelineNode.setDurationMillis(0);
            pipelineNode.setCheckPoint(false);
            pipelineNode.setButtonList(null);
            pipelineNodes.add(pipelineNode);
        }


        String groupName = "cutejava";
        // 顺序执行任务
        for (PipelineNodeVo pipelineNode : pipelineNodes) {
            String instanceId = IdUtil.getSnowflakeNextIdStr();

            // 参数
            Map<String, Object> jobParams = new HashMap<>();
            jobParams.put("instanceId", instanceId);

            // 描述任务
            String jobName = String.format("job-%s-%s", pipelineNode.getCode(), instanceId);
            String triggerName = String.format("trigger-%s-%s", pipelineNode.getCode(), instanceId);
            JobKey jobKey = JobKey.jobKey(jobName, groupName);
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, groupName);
            JobDetail jobDetail = JobBuilder.newJob(ImmediateJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(new JobDataMap(jobParams))
                    .build();
            // 触发器：如何执行
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .build();
            // 调度任务
            scheduler.scheduleJob(jobDetail, trigger);
            // 持续监听任务状态
            while (true) {
//                JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
                PipelineNodeVo pipelineNodeVo = getPipelineNodeInfoByArgs(instanceId, jobName, triggerName);
                if (pipelineNodeVo == null) {
                    ThreadUtil.safeSleep(2000);
                    System.err.println("====== 节点记录未生成，正在重试 ======");
                    continue;
                }
                boolean isForceStop = false;
                if (isForceStop) {
                    // 停止任务
                    System.err.println("====== 停止任务 start ======");
                    scheduler.pauseJob(jobKey);
                    scheduler.deleteJob(jobKey);
                    scheduler.interrupt(jobKey);
                    System.err.println("====== 停止任务 finish ======");
                    break;
                }

                if (pipelineNodeVo.isCheckPoint()) {
                    // 查询库，判断检查点是否成功（异步回调）
                    System.err.println("====== 获取检查点状态 ======");
                    boolean checkPointStatus = getCheckPointStatusByInstanceId(instanceId);
                    if (checkPointStatus) {
                        pipelineNodeVo.setStatus(PipelineNodeVo.StatusEnum.SUCCESS);
                        pipelineNodeVo.setDesc("检查点通过");
                        System.err.println("====== 检查点通过 ======");
                    }
                } else {
                    if (PipelineNodeVo.StatusEnum.SUCCESS.equals(pipelineNodeVo.getStatus())) {
                        pipelineNodeVo.setStatus(PipelineNodeVo.StatusEnum.SUCCESS);
                        pipelineNodeVo.setDesc(PipelineNodeVo.StatusEnum.SUCCESS.getDesc());
                        System.err.println("====== 节点执行成功 ======");
                    } else if (PipelineNodeVo.StatusEnum.FAIL.equals(pipelineNodeVo.getStatus())) {
                        pipelineNodeVo.setStatus(PipelineNodeVo.StatusEnum.FAIL);
                        pipelineNodeVo.setDesc(PipelineNodeVo.StatusEnum.FAIL.getDesc());
                        System.err.println("====== 节点执行失败 ======");
                    }
                }
                pipelineNodeVo.setDurationMillis(DateTime.now().getTime() - pipelineNodeVo.getStartTimeMillis());
                updateNodeInfo(pipelineNodeVo);
                System.err.println("====== 更新节点执行时间间隔 ======");
                ThreadUtil.safeSleep(2000);
            }
        }
    }

    private static PipelineNodeVo getPipelineNodeInfoByArgs(String instanceId, String jobName, String triggerName) {
        return null;
    }

    private static void updateNodeInfo(PipelineNodeVo nodeInfo) {
        // 更新节点信息
    }

    private static boolean getCheckPointStatusByInstanceId(String instanceId) {
        ThreadUtil.safeSleep(2000);
        return true;
    }
}

