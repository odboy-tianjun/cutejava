package cn.odboy.system.dal.dataobject;

import cn.odboy.base.CsBaseUserTimeTb;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@TableName("system_dept")
public class SystemDeptTb extends CsBaseUserTimeTb {

    @NotNull(groups = Update.class)
    @TableId(value = "dept_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @ApiModelProperty(value = "角色")
    private Set<SystemRoleTb> roles;

    @TableField(exist = false)
    @ApiModelProperty(value = "子部门")
    private List<SystemDeptTb> children;

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
        SystemDeptTb dept = (SystemDeptTb) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
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
}