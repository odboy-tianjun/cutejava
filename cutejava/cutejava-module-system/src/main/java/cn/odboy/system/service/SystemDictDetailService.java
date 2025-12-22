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
import cn.hutool.core.collection.CollUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import cn.odboy.system.dal.mysql.SystemDictDetailMapper;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemDictDetailService {
    @Autowired private SystemDictDetailMapper systemDictDetailMapper;

    /**
     * 创建
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDictDetail(SystemCreateDictDetailArgs args) {
        SystemDictDetailTb dictDetail = BeanUtil.copyProperties(args, SystemDictDetailTb.class);
        dictDetail.setDictId(args.getDict().getId());
        systemDictDetailMapper.insert(dictDetail);
    }

    /**
     * 编辑
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictDetailById(SystemDictDetailTb args) {
        SystemDictDetailTb dictDetail = systemDictDetailMapper.selectById(args.getId());
        args.setId(dictDetail.getId());
        systemDictDetailMapper.insertOrUpdate(args);
    }

    /**
     * 删除
     *
     * @param id /
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeDictDetailById(Long id) {
        systemDictDetailMapper.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param args 条件
     * @param page 分页参数
     * @return /
     */
    public KitPageResult<SystemDictDetailTb> queryDictDetailByArgs(SystemQueryDictDetailArgs args, Page<SystemDictDetailTb> page) {
        return KitPageUtil.toPage(systemDictDetailMapper.selectDictDetailByArgs(args, page));
    }

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    public List<SystemDictDetailTb> queryDictDetailByName(String name) {
        SystemQueryDictDetailArgs args = new SystemQueryDictDetailArgs();
        args.setDictName(name);
        return systemDictDetailMapper.selectDictDetailByArgs(args);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDictDetailByDictIds(Set<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            LambdaQueryWrapper<SystemDictDetailTb> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SystemDictDetailTb::getDictId, ids);
            systemDictDetailMapper.delete(wrapper);
        }
    }
}
