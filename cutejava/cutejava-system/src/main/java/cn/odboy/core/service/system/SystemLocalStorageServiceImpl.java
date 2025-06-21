package cn.odboy.core.service.system;

import cn.hutool.core.util.ObjectUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemLocalStorageTb;
import cn.odboy.core.dal.model.system.QuerySystemLocalStorageArgs;
import cn.odboy.core.dal.mysql.system.SystemLocalStorageMapper;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SystemLocalStorageServiceImpl extends ServiceImpl<SystemLocalStorageMapper, SystemLocalStorageTb> implements SystemLocalStorageService {
    private final SystemLocalStorageMapper systemLocalStorageMapper;
    private final AppProperties properties;

    @Override
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
            save(localStorage);
            return localStorage;
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyLocalStorageById(SystemLocalStorageTb resources) {
        SystemLocalStorageTb localStorage = getById(resources.getId());
        localStorage.copy(resources);
        saveOrUpdate(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFileByIds(Long[] ids) {
        for (Long id : ids) {
            SystemLocalStorageTb storage = getById(id);
            FileUtil.del(storage.getPath());
            removeById(storage);
        }
    }

    @Override
    public void downloadExcel(List<SystemLocalStorageTb> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemLocalStorageTb localStorage : queryAll) {
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

    @Override
    public CsResultVo<List<SystemLocalStorageTb>> describeLocalStoragePage(QuerySystemLocalStorageArgs criteria, Page<SystemLocalStorageTb> page) {
        return PageUtil.toPage(systemLocalStorageMapper.queryLocalStoragePageByArgs(criteria, page));
    }

    @Override
    public List<SystemLocalStorageTb> describeLocalStorageList(QuerySystemLocalStorageArgs criteria) {
        return systemLocalStorageMapper.queryLocalStoragePageByArgs(criteria, PageUtil.getCount(systemLocalStorageMapper)).getRecords();
    }
}
