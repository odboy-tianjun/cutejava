package cn.odboy.core.dal.model.system;

import cn.odboy.base.CsSerializeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateSystemQuartzJobArgs extends CsSerializeObject {
    @NotNull
    private Long id;
    private String uuid;
    private String jobName;
    @NotBlank
    private String beanName;
    @NotBlank
    private String methodName;
    private String params;
    @NotBlank
    private String cronExpression;
    private Boolean isPause = false;
    private String personInCharge;
    private String email;
    private String subTask;
    private Boolean pauseAfterFailure;
    @NotBlank
    private String description;
}
