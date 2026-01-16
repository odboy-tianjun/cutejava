package cn.odboy.system.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.odboy.framework.context.KitRequestHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.mysql.SystemLocalStorageMapper;
import cn.odboy.util.KitFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@Slf4j
@Service
public class CuteFileUploadService {

  @Autowired
  private SystemLocalStorageMapper systemLocalStorageMapper;
  @Autowired
  private KitFileLocalUploadHelper fileUploadPathHelper;
  @Autowired
  private AppProperties properties;

  @Transactional(rollbackFor = Exception.class)
  public String uploadLocal(MultipartFile multipartFile) {
    long size = multipartFile.getSize();
    KitFileUtil.checkSize(properties.getOss().getMaxSize(), size);
    String suffix = KitFileUtil.getSuffix(multipartFile.getOriginalFilename());
    String type = KitFileUtil.getFileType(suffix);
    String uploadDateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
    File file = KitFileUtil.upload(multipartFile, fileUploadPathHelper.getPath() + uploadDateStr + File.separator);
    if (file == null) {
      throw new BadRequestException("上传失败");
    }
    try {
      String formatSize = KitFileUtil.getSize(size);
      String prefixName = KitFileUtil.getPrefix(multipartFile.getOriginalFilename(), null);
      SystemLocalStorageTb localStorage = new SystemLocalStorageTb();
      localStorage.setRealName(file.getName());
      localStorage.setName(prefixName);
      localStorage.setSuffix(suffix);
      localStorage.setPath(file.getPath());
      localStorage.setType(type);
      localStorage.setSize(formatSize);
      localStorage.setDateGroup(uploadDateStr);
      systemLocalStorageMapper.insert(localStorage);
      // 构建上传路径 /file/20260117/odboycn-20260117055246428.png
      HttpServletRequest httpServletRequest = KitRequestHolder.getHttpServletRequest();
      StringBuffer requestURL = httpServletRequest.getRequestURL();
      String[] splits = requestURL.toString().split("/api/component/CuteFileUpload");
      String requestAddress = splits[0];
      return String.format("%s/file/%s/%s", requestAddress, uploadDateStr, localStorage.getRealName());
    } catch (Exception e) {
      log.error("上传文件失败", e);
      KitFileUtil.del(file);
      throw e;
    }
  }
}
