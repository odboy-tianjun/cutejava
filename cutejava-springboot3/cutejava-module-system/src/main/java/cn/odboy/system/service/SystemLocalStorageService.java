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

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.CsFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.mysql.SystemLocalStorageMapper;
import cn.odboy.util.CsFileUtil;
import cn.odboy.util.CsPageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SystemLocalStorageService {
    @Autowired
    private SystemLocalStorageMapper systemLocalStorageMapper;
    @Autowired
    private CsFileLocalUploadHelper fileUploadPathHelper;
    @Autowired
    private AppProperties properties;

    /**
     * 上传
     *
     * @param name          文件名称
     * @param multipartFile 文件
     * @return /
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemLocalStorageTb uploadFile(String name, MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        CsFileUtil.checkSize(properties.getOss().getMaxSize(), size);
        String suffix = CsFileUtil.getSuffix(multipartFile.getOriginalFilename());
        String type = CsFileUtil.getFileType(suffix);
        String uploadDateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        File file = CsFileUtil.upload(multipartFile, fileUploadPathHelper.getPath() + uploadDateStr + File.separator);
        if (file == null) {
            throw new BadRequestException("上传失败");
        }
        try {
            String formatSize = CsFileUtil.getSize(size);
            String prefixName = StrUtil.isBlank(name) ? CsFileUtil.getPrefix(multipartFile.getOriginalFilename()) : name;
            SystemLocalStorageTb localStorage = new SystemLocalStorageTb();
            localStorage.setRealName(file.getName());
            localStorage.setName(prefixName);
            localStorage.setSuffix(suffix);
            localStorage.setPath(file.getPath());
            localStorage.setType(type);
            localStorage.setSize(formatSize);
            localStorage.setDateGroup(uploadDateStr);
            systemLocalStorageMapper.insert(localStorage);
            return localStorage;
        } catch (Exception e) {
            CsFileUtil.del(file);
            throw e;
        }
    }

    /**
     * 编辑
     *
     * @param args 文件信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyLocalStorageById(SystemLocalStorageTb args) {
        SystemLocalStorageTb localStorage = systemLocalStorageMapper.selectById(args.getId());
        localStorage.copy(args);
        systemLocalStorageMapper.insertOrUpdate(localStorage);
    }

    /**
     * 多选删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFileByIds(Long[] ids) {
        for (Long id : ids) {
            SystemLocalStorageTb storage = systemLocalStorageMapper.selectById(id);
            try {
                CsFileUtil.del(storage.getPath());
                systemLocalStorageMapper.deleteById(storage);
            } catch (IORuntimeException e) {
                throw new BadRequestException("删除文件 " + storage.getName() + " 失败");
            }
        }
    }

    /**
     * 导出数据
     *
     * @param localStorages 待导出的数据
     * @param response      /
     * @throws IOException /
     */
    public void exportLocalStorageExcel(List<SystemLocalStorageTb> localStorages, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemLocalStorageTb localStorage : localStorages) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorage.getRealName());
            map.put("备注名", localStorage.getName());
            map.put("文件类型", localStorage.getType());
            map.put("文件大小", localStorage.getSize());
            map.put("创建者", localStorage.getCreateBy());
            map.put("创建日期", localStorage.getCreateTime());
            list.add(map);
        }
        CsFileUtil.downloadExcel(list, response);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public CsPageResult<SystemLocalStorageTb> queryLocalStorage(SystemQueryStorageArgs criteria, Page<SystemLocalStorageTb> page) {
        return CsPageUtil.toPage(systemLocalStorageMapper.selectLocalStorageByArgs(criteria, page));
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<SystemLocalStorageTb> queryLocalStorage(SystemQueryStorageArgs criteria) {
        return systemLocalStorageMapper.selectLocalStorageByArgs(criteria, CsPageUtil.getCount(systemLocalStorageMapper)).getRecords();
    }
}
