package cn.odboy.framework.mybatisplus.core;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义任意对象查询工具
 *
 * @author odboy
 * @date 2025-05-15
 */
public class CsMpAnyQUtil {
    public static <T> T selectOne(BaseMapper<T> baseMapper, Object queryParams) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        return baseMapper.selectOne(queryWrapper);
    }

    public static <T, M> M selectOne(BaseMapper<T> baseMapper, Object queryParams, Class<M> mapperClazz) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        T mapperTarget = baseMapper.selectOne(queryWrapper);
        if (mapperTarget == null) {
            return null;
        }
        return BeanUtil.copyProperties(mapperTarget, mapperClazz);
    }

    public static <T> List<T> selectList(BaseMapper<T> baseMapper, Object queryParams) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        return baseMapper.selectList(queryWrapper);
    }

    public static <T, M> List<M> selectList(BaseMapper<T> baseMapper, Object queryParams, Class<M> mapperClazz) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        List<T> mapperTarget = baseMapper.selectList(queryWrapper);
        if (mapperTarget == null) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(mapperTarget, mapperClazz);
    }

    public static <T> Long selectCount(BaseMapper<T> baseMapper, Object queryParams) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        return baseMapper.selectCount(queryWrapper);
    }

    public static <T> Page<T> selectPage(BaseMapper<T> baseMapper, Page<T> page, Object queryParams) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        return baseMapper.selectPage(page, queryWrapper);
    }

    public static <T, M> Page<M> selectPage(BaseMapper<T> baseMapper, Page<T> page, Object queryParams, Class<M> mapperClazz) {
        QueryWrapper<T> queryWrapper = CsMpQUtil.build(queryParams);
        Page<T> tPage = baseMapper.selectPage(page, queryWrapper);
        Page<M> mapperTarget = new Page<>();
        mapperTarget.setRecords(BeanUtil.copyToList(tPage.getRecords(), mapperClazz));
        mapperTarget.setTotal(tPage.getTotal());
        mapperTarget.setSize(tPage.getSize());
        mapperTarget.setCurrent(tPage.getCurrent());
        return mapperTarget;
    }
}
