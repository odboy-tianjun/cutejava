package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemLocalStorageTb;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface SystemLocalStorageService extends IService<SystemLocalStorageTb> {
    /**
     * 上传
     *
     * @param name 文件名称
     * @param file 文件
     * @return /
     */
    SystemLocalStorageTb uploadFile(String name, MultipartFile file);

    /**
     * 编辑
     *
     * @param resources 文件信息
     */
    void modifyLocalStorageById(SystemLocalStorageTb resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void removeFileByIds(Long[] ids);

    /**
     * 导出数据
     *
     * @param localStorages 待导出的数据
     * @param response      /
     * @throws IOException /
     */
    void downloadExcel(List<SystemLocalStorageTb> localStorages, HttpServletResponse response) throws IOException;
}