package cn.odboy.system.dal.model;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemMenuExportRowVo extends KitObject {

  @ExcelProperty("菜单标题")
  private String title;
  @ExcelProperty("菜单类型")
  private String type;
  @ExcelProperty("权限标识")
  private String permission;
  @ExcelProperty("外链菜单")
  private String iFrame;
  @ExcelProperty("菜单可见")
  private String hidden;
  @ExcelProperty("是否缓存")
  private String cache;
  @ExcelProperty("创建日期")
  private Date createTime;
}
