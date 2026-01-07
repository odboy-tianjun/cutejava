package cn.odboy.system.dal.model;

import cn.odboy.base.KitBaseUserTimeTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemMenuVo extends KitBaseUserTimeTb {

  @NotNull(groups = {Update.class})
  @ApiModelProperty(value = "ID", hidden = true)
  private Long id;
  @ApiModelProperty(value = "菜单标题")
  private String title;
  @ApiModelProperty(value = "菜单组件名称")
  private String componentName;
  @ApiModelProperty(value = "排序")
  private Integer menuSort = 999;
  @ApiModelProperty(value = "组件路径")
  private String component;
  @ApiModelProperty(value = "路由地址")
  private String path;
  @ApiModelProperty(value = "菜单类型，目录、菜单、按钮")
  private Integer type;
  @ApiModelProperty(value = "权限标识")
  private String permission;
  @ApiModelProperty(value = "菜单图标")
  private String icon;
  @ApiModelProperty(value = "缓存")
  private Boolean cache;
  @ApiModelProperty(value = "是否隐藏")
  private Boolean hidden;
  @ApiModelProperty(value = "上级菜单")
  private Long pid;
  @ApiModelProperty(value = "子节点数目", hidden = true)
  private Integer subCount = 0;
  @ApiModelProperty(value = "外链菜单")
  private Boolean iFrame;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMenuVo menu = (SystemMenuVo) o;
    return Objects.equals(id, menu.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
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
    return title;
  }

  /**
   * 附加属性
   */
  @ApiModelProperty(value = "菜单角色")
  private Set<SystemRoleTb> roles;
  @ApiModelProperty(value = "子菜单")
  private List<SystemMenuVo> children;
}
