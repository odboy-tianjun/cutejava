package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemUserVo extends SystemUserTb {

  @NotNull(groups = Update.class)
  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @ApiModelProperty(hidden = true)
  private Long deptId;
  @NotBlank
  @ApiModelProperty(value = "用户名称")
  private String username;
  @NotBlank
  @ApiModelProperty(value = "用户昵称")
  private String nickName;
  @Email
  @NotBlank
  @ApiModelProperty(value = "邮箱")
  private String email;
  @NotBlank
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
  @NotNull
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
