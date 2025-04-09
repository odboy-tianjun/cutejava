package cn.odboy.application.system.mapper;

import cn.odboy.model.system.domain.DictDetail;
import cn.odboy.model.system.request.QueryDictDetailRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;


@Mapper
public interface DictDetailMapper extends BaseMapper<DictDetail> {
    List<DictDetail> queryDictDetailListByDictName(@Param("name") String name);

    IPage<DictDetail> queryDictDetailPageByArgs(@Param("criteria") QueryDictDetailRequest criteria, Page<Object> page);

    void deleteDictDetailByDictIds(@Param("dictIds") Set<Long> dictIds);
}