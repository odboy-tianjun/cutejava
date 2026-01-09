package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

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
