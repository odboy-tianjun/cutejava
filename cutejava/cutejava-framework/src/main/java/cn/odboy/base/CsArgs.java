package cn.odboy.base;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 通用参数
 *
 * @author odboy
 * @date 2025-07-23
 */
public class CsArgs {
    @Getter
    @Setter
    public static class FindByLongId extends CsObject {
        @NotNull(message = "id必填")
        private Long id;
    }
}
