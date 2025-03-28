package cn.odboy.application.tools.mapper;

import cn.odboy.model.tools.domain.QiniuContent;
import cn.odboy.model.tools.request.QiniuQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface QiniuContentMapper extends BaseMapper<QiniuContent> {
    QiniuContent getQiniuContentByName(@Param("name") String name);

    IPage<QiniuContent> queryQiniuContentPage(@Param("criteria") QiniuQueryCriteria criteria, Page<Object> page);
}