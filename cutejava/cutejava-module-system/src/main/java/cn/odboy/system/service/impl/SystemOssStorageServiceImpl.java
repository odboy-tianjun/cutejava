package cn.odboy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.mysql.SystemOssStorageMapper;
import cn.odboy.system.framework.storage.minio.MinioRepository;
import cn.odboy.system.service.SystemOssStorageService;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * OSS存储 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
@Service
@RequiredArgsConstructor
public class SystemOssStorageServiceImpl extends ServiceImpl<SystemOssStorageMapper, SystemOssStorageTb> implements SystemOssStorageService {
    private final MinioRepository minioRepository;

    @Override
    public CsResultVo<List<SystemOssStorageVo>> queryOssStorage(SystemQueryStorageArgs criteria, Page<SystemOssStorageTb> page) {
        IPage<SystemOssStorageTb> ossStorageTbs = baseMapper.selectOssStorageByArgs(criteria, page);
        IPage<SystemOssStorageVo> convert = ossStorageTbs.convert(c -> {
            SystemOssStorageVo storageVo = BeanUtil.copyProperties(c, SystemOssStorageVo.class);
            storageVo.setFileSizeDesc(FileUtil.getSize(storageVo.getFileSize()));
            return storageVo;
        });
        return CsPageUtil.toPage(convert);
    }

    @Override
    public List<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs criteria) {
        // 防止刷数据
        Page<SystemOssStorageTb> page = new Page<>(1, 500);
        return queryOssStorage(criteria, page).getContent();
    }

    @Override
    public void exportOssStorageExcel(List<SystemOssStorageVo> ossStorages, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemOssStorageVo ossStorage : ossStorages) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("服务类型", ossStorage.getServiceType());
            map.put("服务地址", ossStorage.getEndpoint());
            map.put("存储桶名称", ossStorage.getBucketName());
            map.put("文件名称", ossStorage.getFileName());
            map.put("文件大小", ossStorage.getFileSizeDesc());
            map.put("文件类型", ossStorage.getFileMime());
            map.put("创建人", ossStorage.getCreateBy());
            map.put("创建日期", ossStorage.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadFile(String name, MultipartFile file) {
        SystemOssStorageTb storageTb = minioRepository.upload(file);
        if (storageTb == null) {
            throw new BadRequestException("文件上传失败");
        }
        save(storageTb);
    }

    /**
     * 多选删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFileByIds(Long[] ids) {
        for (Long id : ids) {
            SystemOssStorageTb storage = getById(id);
            minioRepository.remove(storage.getObjectName());
            removeById(storage);
        }
    }
}
