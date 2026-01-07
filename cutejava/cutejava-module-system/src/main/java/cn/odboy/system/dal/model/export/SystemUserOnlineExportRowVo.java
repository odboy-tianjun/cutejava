package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemUserOnlineExportRowVo extends KitObject {

  @ExcelProperty("用户名")
  private String userName;
  @ExcelProperty("部门")
  private String dept;
  @ExcelProperty("登录IP")
  private String ip;
  @ExcelProperty("登录地点")
  private String address;
  @ExcelProperty("浏览器")
  private String browser;
  @ExcelProperty("登录日期")
  private String loginTime;
}