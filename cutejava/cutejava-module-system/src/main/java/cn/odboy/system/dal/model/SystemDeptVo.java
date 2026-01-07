package cn.odboy.system.dal.model;

import cn.odboy.base.KitBaseUserTimeTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDeptVo extends KitBaseUserTimeTb {

  @NotNull(groups = Update.class)
  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @ApiModelProperty(value = "排序")
  private Integer deptSort;
  @NotBlank
  @ApiModelProperty(value = "部门名称")
  private String name;
  @NotNull
  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;
  @ApiModelProperty(value = "上级部门")
  private Long pid;
  @ApiModelProperty(value = "子节点数目", hidden = true)
  private Integer subCount = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemDeptVo dept = (SystemDeptVo) o;
    return Objects.equals(id, dept.id) && Objects.equals(name, dept.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @ApiModelProperty(value = "是否有子节点")
  public Boolean getHasChildren() {
    return subCount > 0;
  }

  @ApiModelProperty(value = "是否为叶子")
  public Boolean getLeaf() {
    return subCount <= 0;
  }

  @ApiModelProperty(value = "标签名称")
  public String getLabel() {
    return name;
  }

  /**
   * 扩展字段
   */
  @ApiModelProperty(value = "角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "子部门")
  private List<SystemDeptVo> children;
}
