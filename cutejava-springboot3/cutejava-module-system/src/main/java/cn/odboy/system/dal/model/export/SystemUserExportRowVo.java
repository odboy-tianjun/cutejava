package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemUserExportRowVo extends KitObject {

  @ExcelProperty("用户名")
  private String username;
  @ExcelProperty("角色")
  private String roles;
  @ExcelProperty("部门")
  private String dept;
  @ExcelProperty("岗位")
  private String jobs;
  @ExcelProperty("邮箱")
  private String email;
  @ExcelProperty("状态")
  private String status;
  @ExcelProperty("手机号码")
  private String mobile;
  @ExcelProperty("修改密码的时间")
  private Date updatePwdTime;
  @ExcelProperty("创建日期")
  private Date createTime;
}
