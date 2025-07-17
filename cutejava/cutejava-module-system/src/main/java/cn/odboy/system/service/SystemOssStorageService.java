package cn.odboy.system.service;

import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * OSS存储 服务类
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
public interface SystemOssStorageService extends IService<SystemOssStorageTb> {
    CsResultVo<List<SystemOssStorageVo>> queryOssStorage(SystemQueryStorageArgs criteria, Page<SystemOssStorageTb> page);

    List<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs criteria);

    void exportOssStorageExcel(List<SystemOssStorageVo> list, HttpServletResponse response) throws IOException;

    String uploadFile(MultipartFile file);

    void removeFileByIds(Long[] ids);

    SystemOssStorageTb getByMd5(String md5);
}
