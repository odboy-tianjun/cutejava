/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.SystemCreateDictArgs;
import cn.odboy.system.dal.model.SystemDictDetailVo;
import cn.odboy.system.dal.model.SystemQueryDictArgs;
import cn.odboy.system.dal.mysql.SystemDictMapper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemDictService {

    @Autowired private SystemDictMapper systemDictMapper;
    @Autowired private SystemDictDetailService systemDictDetailService;

    /**
     * 创建
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDict(SystemCreateDictArgs args) {
        systemDictMapper.insert(BeanUtil.copyProperties(args, SystemDictTb.class));
    }

    /**
     * 编辑
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDictById(SystemDictTb args) {
        SystemDictTb dict = systemDictMapper.selectById(args.getId());
        dict.setName(args.getName());
        dict.setDescription(args.getDescription());
        systemDictMapper.insertOrUpdate(dict);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictByIds(Set<Long> ids) {
        // 删除字典
        systemDictMapper.deleteByIds(ids);
        // 删除字典详情
        systemDictDetailService.deleteDictDetailByDictIds(ids);
    }

    /**
     * 导出数据
     *
     * @param dicts    待导出的数据
     * @param response /
     * @throws IOException /
     */
    public void exportDictExcel(List<SystemDictTb> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemDictTb dict : dicts) {
            List<SystemDictDetailVo> dictDetails = systemDictDetailService.listDictDetailByName(dict.getName());
            if (CollectionUtil.isNotEmpty(dictDetails)) {
                for (SystemDictDetailTb dictDetail : dictDetails) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dict.getName());
                    map.put("字典描述", dict.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("字典名称", dict.getName());
                map.put("字典描述", dict.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dict.getCreateTime());
                list.add(map);
            }
        }
        KitFileUtil.downloadExcel(list, response);
    }

    /**
     * 分页查询
     *
     * @param args 条件
     * @param page 分页参数
     * @return /
     */
    public KitPageResult<SystemDictTb> searchDict(SystemQueryDictArgs args, Page<SystemDictTb> page) {
        LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
        injectQueryParams(args, wrapper);
        Page<SystemDictTb> selectPage = systemDictMapper.selectPage(page, wrapper);
        return KitPageUtil.toPage(selectPage);
    }

    private void injectQueryParams(SystemQueryDictArgs args, LambdaQueryWrapper<SystemDictTb> wrapper) {
        if (args != null) {
            wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemDictTb::getName, args.getBlurry()).or()
                .like(SystemDictTb::getDescription, args.getBlurry()));
        }
    }

    public List<SystemDictTb> queryDictByArgs(SystemQueryDictArgs args) {
        LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
        injectQueryParams(args, wrapper);
        return systemDictMapper.selectList(wrapper);
    }
}
