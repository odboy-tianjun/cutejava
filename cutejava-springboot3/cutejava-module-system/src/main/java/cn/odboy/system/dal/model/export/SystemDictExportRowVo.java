package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class SystemDictExportRowVo extends KitObject {

  @ExcelProperty("字典名称")
  private String name;
  @ExcelProperty("字典描述")
  private String description;
  @ExcelProperty("字典标签")
  private String label;
  @ExcelProperty("字典值")
  private String value;
  @ExcelProperty("创建日期")
  private Date createTime;
}
