package cn.odboy.system.dal.model.response;

import cn.odboy.base.KitBaseUserTimeTb;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class SystemUserVo extends KitBaseUserTimeTb {

  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @ApiModelProperty(hidden = true)
  private Long deptId;
  @ApiModelProperty(value = "用户名称")
  private String username;
  @ApiModelProperty(value = "用户昵称")
  private String nickName;
  @ApiModelProperty(value = "邮箱")
  private String email;
  @ApiModelProperty(value = "电话号码")
  private String phone;
  @ApiModelProperty(value = "用户性别")
  private String gender;
  @ApiModelProperty(value = "头像真实名称", hidden = true)
  private String avatarName;
  @ApiModelProperty(value = "头像存储的路径", hidden = true)
  private String avatarPath;
  @ApiModelProperty(value = "密码")
  private String password;
  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;
  @ApiModelProperty(value = "是否为admin账号", hidden = true)
  private Boolean isAdmin = false;
  @ApiModelProperty(value = "最后修改密码的时间", hidden = true)
  private Date pwdResetTime;

  /**
   * 扩展字段
   */
  @ApiModelProperty(value = "用户部门")
  private SystemDeptVo dept;
  @ApiModelProperty(value = "用户角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "用户岗位")
  private Set<SystemJobTb> jobs;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemUserVo user = (SystemUserVo) o;
    return Objects.equals(id, user.id) && Objects.equals(username, user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

}
