package cn.odboy.mapper;

import cn.odboy.model.tools.domain.QiniuContent;
import cn.odboy.model.tools.dto.QiniuQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QiniuContentMapper extends BaseMapper<QiniuContent> {

    QiniuContent findByKey(@Param("name") String name);

    IPage<QiniuContent> findAll(@Param("criteria") QiniuQueryCriteria criteria, Page<Object> page);

    List<QiniuContent> findAll(QiniuQueryCriteria criteria);
}
