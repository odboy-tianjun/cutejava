package cn.odboy.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Bean拷贝相关
 *
 * @author odboy
 * @date 2026-01-06
 */
@UtilityClass
public class KitBeanUtil {

  public static <T> T copyToClass(Object source, Class<T> tClass) {
    return BeanUtil.copyProperties(source, tClass);
  }

  public static void copyToTarget(Object source, Object target) {
    BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true));
  }

  public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType) {
    List<T> ts = BeanUtil.copyToList(collection, targetType);
    if (ts == null) {
      return new ArrayList<>(0);
    }
    return ts;
  }
}
