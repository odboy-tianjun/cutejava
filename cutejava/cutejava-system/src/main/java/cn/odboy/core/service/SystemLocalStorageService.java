package cn.odboy.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.core.dal.model.QuerySystemLocalStorageArgs;
import cn.odboy.core.dal.mysql.SystemLocalStorageMapper;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SystemLocalStorageService {
    private final SystemLocalStorageMapper systemLocalStorageMapper;
    private final AppProperties properties;

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
        FileUtil.checkSize(properties.getFile().getFileMaxSize(), size);
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getFile().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            String formatSize = FileUtil.getSize(size);
            name = StringUtil.isBlank(name) ? FileUtil.getPrefix(multipartFile.getOriginalFilename()) : name;
            SystemLocalStorageTb localStorage = new SystemLocalStorageTb(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    formatSize
            );
            systemLocalStorageMapper.insert(localStorage);
            return localStorage;
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    /**
     * 编辑
     *
     * @param resources 文件信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyLocalStorageById(SystemLocalStorageTb resources) {
        SystemLocalStorageTb localStorage = systemLocalStorageMapper.selectById(resources.getId());
        localStorage.copy(resources);
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
            FileUtil.del(storage.getPath());
            systemLocalStorageMapper.deleteById(storage);
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
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public CsResultVo<List<SystemLocalStorageTb>> queryLocalStorage(QuerySystemLocalStorageArgs criteria, Page<SystemLocalStorageTb> page) {
        return CsPageUtil.toPage(systemLocalStorageMapper.selectLocalStorageByArgs(criteria, page));
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<SystemLocalStorageTb> queryLocalStorage(QuerySystemLocalStorageArgs criteria) {
        return systemLocalStorageMapper.selectLocalStorageByArgs(criteria, CsPageUtil.getCount(systemLocalStorageMapper)).getRecords();
    }
}
