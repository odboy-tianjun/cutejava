package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemJobExportRowVo extends KitObject {

  @ExcelProperty("岗位名称")
  private String name;
  @ExcelProperty("岗位状态")
  private String enabled;
  @ExcelProperty("创建日期")
  private Date createTime;
}
