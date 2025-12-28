package cn.odboy.system.dal.model;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemLocalStorageExportRowVo extends KitObject {

  @ExcelProperty(value = "文件名")
  private String realName;
  @ExcelProperty(value = "备注名")
  private String name;
  @ExcelProperty(value = "文件类型")
  private String type;
  @ExcelProperty(value = "文件大小")
  private String size;
  @ExcelProperty(value = "创建者")
  private String createBy;
  @ExcelProperty(value = "创建日期")
  private Date createTime;
}
