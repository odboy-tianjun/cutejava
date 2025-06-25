package cn.odboy.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CsResultVo<T> extends CsSerializeObject {
    private T content;
    private long totalElements;

    public static <T> CsResultVo<List<T>> emptyListData() {
        CsResultVo<List<T>> result = new CsResultVo<>();
        result.setTotalElements(0);
        result.setContent(new ArrayList<>());
        return result;
    }
}
