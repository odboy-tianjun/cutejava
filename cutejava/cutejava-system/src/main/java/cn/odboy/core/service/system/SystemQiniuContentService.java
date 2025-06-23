package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;
import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
import cn.odboy.core.dal.mysql.system.SystemQiniuContentMapper;
import cn.odboy.core.util.QiniuUtil;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemQiniuContentService {
    private final SystemQiniuContentMapper systemQiniuContentMapper;
    private final SystemQiniuConfigService systemQiniuConfigService;
    @Value("${app.oss.qiniu.max-size}")
    private Long maxSize;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return QiniuContent
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemQiniuContentTb uploadFile(MultipartFile file) {
        FileUtil.checkSize(maxSize, file.getSize());
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigService.getLastQiniuConfig();
        if (qiniuConfig.getId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiniuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            if (systemQiniuContentMapper.getQiniuContentByName(key) != null) {
                key = QiniuUtil.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            String fileNameNoExt = FileUtil.getPrefix(putRet.key);
            SystemQiniuContentTb content = systemQiniuContentMapper.getQiniuContentByName(fileNameNoExt);
            if (content == null) {
                // 存入数据库
                SystemQiniuContentTb qiniuContent = new SystemQiniuContentTb();
                qiniuContent.setSuffix(FileUtil.getSuffix(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setKey(fileNameNoExt);
                qiniuContent.setUrl(qiniuConfig.getHost() + "/" + putRet.key);
                qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(String.valueOf(file.getSize()))));
                systemQiniuContentMapper.insert(qiniuContent);
            }
            return content;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * 创建文件预览链接
     *
     * @param content 文件信息
     * @return String
     */
    public String createFilePreviewUrl(SystemQiniuContentTb content) {
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigService.getLastQiniuConfig();
        String finalUrl;
        String type = "公开";
        if (type.equals(content.getType())) {
            finalUrl = content.getUrl();
        } else {
            Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
            // 1小时，可以自定义链接过期时间
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    /**
     * 删除文件
     *
     * @param id 文件id
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFileById(Long id) {
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigService.getLastQiniuConfig();
        SystemQiniuContentTb qiniuContent = systemQiniuContentMapper.selectById(id);
        if (qiniuContent == null) {
            throw new BadRequestException("文件不存在");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiniuUtil.getRegion(qiniuConfig.getZone()));
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(qiniuContent.getBucket(), qiniuContent.getKey() + "." + qiniuContent.getSuffix());
        } catch (QiniuException ex) {
            log.error("七牛云删除文件失败", ex);
        } finally {
            systemQiniuContentMapper.deleteById(qiniuContent);
        }
    }

    /**
     * 同步数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void synchronize() {
        SystemQiniuConfigTb qiniuConfig = systemQiniuConfigService.getLastQiniuConfig();
        if (qiniuConfig.getId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiniuUtil.getRegion(qiniuConfig.getZone()));
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qiniuConfig.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            SystemQiniuContentTb qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                String filename = FileUtil.getPrefix(item.key);
                String suffix = FileUtil.getSuffix(item.key);
                if (systemQiniuContentMapper.getQiniuContentByName(filename) == null) {
                    qiniuContent = new SystemQiniuContentTb();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(String.valueOf(item.fsize))));
                    qiniuContent.setSuffix(suffix);
                    qiniuContent.setKey(filename);
                    qiniuContent.setType(qiniuConfig.getType());
                    qiniuContent.setBucket(qiniuConfig.getBucket());
                    qiniuContent.setUrl(qiniuConfig.getHost() + "/" + item.key);
                    systemQiniuContentMapper.insert(qiniuContent);
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param ids 文件ID数组
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeFileByIds(Long[] ids) {
        for (Long id : ids) {
            SystemQiniuContentTb qiniuContent = systemQiniuContentMapper.selectById(id);
            if (qiniuContent != null) {
                removeFileById(id);
            }
        }
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    public void exportQiniuContentExcel(List<SystemQiniuContentTb> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemQiniuContentTb content : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", content.getKey());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucket());
            map.put("文件大小", content.getSize());
            map.put("空间类型", content.getType());
            map.put("创建日期", content.getUpdateTime());
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
    public CsResultVo<List<SystemQiniuContentTb>> queryQiniuContentByArgs(QuerySystemQiniuArgs criteria, Page<SystemQiniuContentTb> page) {
        return CsPageUtil.toPage(systemQiniuContentMapper.selectQiniuContentByArgs(criteria, page));
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<SystemQiniuContentTb> queryQiniuContentByArgs(QuerySystemQiniuArgs criteria) {
        return systemQiniuContentMapper.selectQiniuContentByArgs(criteria, CsPageUtil.getCount(systemQiniuContentMapper)).getRecords();
    }

    public SystemQiniuContentTb getQiniuContentById(Long id) {
        return systemQiniuContentMapper.selectById(id);
    }
}
