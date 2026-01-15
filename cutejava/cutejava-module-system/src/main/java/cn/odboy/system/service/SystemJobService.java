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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.export.SystemJobExportRowVo;
import cn.odboy.system.dal.model.request.SystemCreateJobArgs;
import cn.odboy.system.dal.model.request.SystemQueryJobArgs;
import cn.odboy.system.dal.mysql.SystemJobMapper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SystemJobService {

    @Autowired
    private SystemJobMapper systemJobMapper;
    @Autowired
    private SystemUserJobService systemUserJobService;

    /**
     * 创建 -> TestPassed
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(SystemCreateJobArgs args) {
        SystemJobTb job = this.getJobByName(args.getName());
        if (job != null) {
            throw new BadRequestException("职位名称已存在");
        }
        SystemJobTb systemJobTb = KitBeanUtil.copyToClass(args, SystemJobTb.class);
        systemJobMapper.insert(systemJobTb);
    }

    /**
     * 编辑 -> TestPassed
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateJobById(SystemJobTb args) {
        SystemJobTb job = systemJobMapper.selectById(args.getId());
        SystemJobTb old = this.getJobByName(args.getName());
        if (old != null && !old.getId().equals(args.getId())) {
            throw new BadRequestException("职位名称已存在");
        }
        args.setId(job.getId());
        systemJobMapper.updateById(args);
    }

    /**
     * 删除 -> TestPassed
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Set<Long> ids) {
        // 验证是否被用户关联
        this.verifyBindRelationByIds(ids);
        systemJobMapper.deleteByIds(ids);
    }

    /**
     * 分页查询 -> TestPassed
     *
     * @param args 条件
     * @param page 分页参数
     */
    public KitPageResult<SystemJobTb> searchJobByArgs(SystemQueryJobArgs args, Page<SystemJobTb> page) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        this.injectQueryParams(args, wrapper);
        Page<SystemJobTb> selectPage = systemJobMapper.selectPage(page, wrapper);
        return KitPageUtil.toPage(selectPage);
    }

    /**
     * 查询全部数据 -> TestPassed
     */
    public List<SystemJobTb> queryJobByArgs(SystemQueryJobArgs args) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        this.injectQueryParams(args, wrapper);
        return systemJobMapper.selectList(wrapper);
    }

    /**
     * 验证是否被用户关联 -> TestPassed
     *
     * @param ids /
     */
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserJobService.countUserByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联, 请解除关联再试！");
        }
    }

    public SystemJobTb getJobByName(String name) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemJobTb::getName, name);
        return systemJobMapper.selectOne(wrapper);
    }

    public void injectQueryParams(SystemQueryJobArgs args, LambdaQueryWrapper<SystemJobTb> wrapper) {
        if (args != null) {
            wrapper.like(StrUtil.isNotBlank(args.getName()), SystemJobTb::getName, args.getName());
            wrapper.eq(args.getEnabled() != null, SystemJobTb::getEnabled, args.getEnabled());
            if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
                wrapper.between(SystemJobTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
            }
        }
        wrapper.orderByDesc(SystemJobTb::getJobSort, SystemJobTb::getId);
    }

    public void exportJobXlsx(HttpServletResponse response, SystemQueryJobArgs args) {
        List<SystemJobTb> systemJobTbs = this.queryJobByArgs(args);
        List<SystemJobExportRowVo> rowVos = new ArrayList<>();
        for (SystemJobTb dataObject : systemJobTbs) {
            SystemJobExportRowVo rowVo = new SystemJobExportRowVo();
            rowVo.setName(dataObject.getName());
            rowVo.setEnabled(dataObject.getEnabled() ? "启用" : "停用");
            rowVo.setCreateTime(dataObject.getCreateTime());
            rowVos.add(rowVo);
        }
        KitExcelExporter.exportSimple(response, "岗位数据", SystemJobExportRowVo.class, rowVos);
    }

    public List<SystemJobTb> listByIds(Set<Long> jobIds) {
        return systemJobMapper.selectByIds(jobIds);
    }
}
