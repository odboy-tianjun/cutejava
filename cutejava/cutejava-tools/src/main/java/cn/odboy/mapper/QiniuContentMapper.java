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

    QiniuContent selectByName(@Param("name") String name);

    IPage<QiniuContent> selectByPage(@Param("criteria") QiniuQueryCriteria criteria, Page<Object> page);

    List<QiniuContent> selectByPage(QiniuQueryCriteria criteria);
}
