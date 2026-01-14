package cn.odboy.system.dal.model.export;

import cn.idev.excel.annotation.ExcelProperty;
import cn.odboy.base.KitObject;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class SystemOssStorageExportRowVo extends KitObject {

  @ExcelProperty("服务类型")
  private String serviceType;
  @ExcelProperty("服务地址")
  private String endpoint;
  @ExcelProperty("存储桶名称")
  private String bucketName;
  @ExcelProperty("文件名称")
  private String fileName;
  @ExcelProperty("文件大小")
  private String fileSizeDesc;
  @ExcelProperty("文件类型")
  private String fileMime;
  @ExcelProperty("创建人")
  private String createBy;
  @ExcelProperty("创建日期")
  private Date createTime;
}
