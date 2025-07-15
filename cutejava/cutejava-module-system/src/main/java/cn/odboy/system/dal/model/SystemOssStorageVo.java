package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemOssStorageVo extends SystemOssStorageTb {
    private String fileSizeDesc;
}
