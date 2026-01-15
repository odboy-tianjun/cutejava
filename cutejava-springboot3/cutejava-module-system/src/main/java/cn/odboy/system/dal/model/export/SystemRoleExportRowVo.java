package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class SystemRoleExportRowVo extends KitObject {

  @ExcelProperty("角色名称")
  private String name;
  @ExcelProperty("角色级别")
  private Integer level;
  @ExcelProperty("描述")
  private String description;
  @ExcelProperty("角色名称")
  private Date createTime;
}
