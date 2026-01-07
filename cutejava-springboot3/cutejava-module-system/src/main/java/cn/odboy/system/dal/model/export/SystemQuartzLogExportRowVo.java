package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemQuartzLogExportRowVo extends KitObject {

  @ExcelProperty("任务名称")
  private String jobName;
  @ExcelProperty("Bean名称")
  private String beanName;
  @ExcelProperty("执行方法")
  private String methodName;
  @ExcelProperty("参数")
  private String params;
  @ExcelProperty("表达式")
  private String cronExpression;
  @ExcelProperty("异常详情")
  private String exceptionDetail;
  @ExcelProperty("耗时/毫秒")
  private Long time;
  @ExcelProperty("状态")
  private String status;
  @ExcelProperty("创建日期")
  private Date createTime;
}
