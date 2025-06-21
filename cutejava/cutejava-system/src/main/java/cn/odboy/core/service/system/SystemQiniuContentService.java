package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface SystemQiniuContentService extends IService<SystemQiniuContentTb> {
    /**
     * 上传文件
     *
     * @param file 文件
     * @return QiniuContent
     */
    SystemQiniuContentTb uploadFile(MultipartFile file);

    /**
     * 创建文件预览链接
     *
     * @param content 文件信息
     * @return String
     */
    String createFilePreviewUrl(SystemQiniuContentTb content);

    /**
     * 同步数据
     */
    void synchronize();

    /**
     * 删除文件
     *
     * @param ids 文件ID数组
     */
    void removeFileByIds(Long[] ids);

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<SystemQiniuContentTb> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 删除文件
     *
     * @param id 文件id
     */
    void removeFileById(Long id);
    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQiniuContentTb>> queryQiniuContentPage(QuerySystemQiniuArgs criteria, Page<SystemQiniuContentTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQiniuContentTb> queryQiniuContentList(QuerySystemQiniuArgs criteria);
}
