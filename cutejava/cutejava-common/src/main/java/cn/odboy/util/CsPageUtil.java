package cn.odboy.util;

import cn.odboy.base.CsResultVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具
 *
 * @author odboy
 */
public final class CsPageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static <T> List<T> softPaging(int page, int size, List<T> list) {
        int pageIndex = page - 1;
        int fromIndex = pageIndex * size;
        int toIndex = pageIndex * size + size;
        if (fromIndex > list.size()) {
            return Collections.emptyList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * Page 数据处理
     */
    public static <T> CsResultVo<List<T>> toPage(IPage<T> page) {
        return new CsResultVo<>(page.getRecords(), page.getTotal());
    }

    /**
     * 自定义分页
     */
    public static <T> CsResultVo<List<T>> toPage(List<T> list) {
        return new CsResultVo<>(list, list.size());
    }

    /**
     * 返回空数据
     */
    public static <T> CsResultVo<List<T>> emptyListData() {
        return new CsResultVo<>(new ArrayList<>(), 0);
    }

    /**
     * 返回空数据
     */
    public static <T> CsResultVo<T> emptyData() {
        return new CsResultVo<>(null, 0);
    }

    /**
     * 自定义分页
     */
    public static <T> CsResultVo<List<T>> toPage(List<T> list, long totalElements) {
        return new CsResultVo<>(list, totalElements);
    }

    public static <T> Page<T> getCount(BaseMapper<T> baseMapper) {
        return new Page<>(1, baseMapper.selectCount(null));
    }
}
