package cn.odboy.system.dal.model;

import cn.odboy.base.CsObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SystemProductLineTreeVo extends CsObject {
    private String value;
    private String label;
    private List<SystemProductLineTreeVo> children;
}
