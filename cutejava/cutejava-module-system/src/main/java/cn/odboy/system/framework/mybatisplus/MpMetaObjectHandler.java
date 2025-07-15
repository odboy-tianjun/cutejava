package cn.odboy.system.framework.mybatisplus;

import cn.hutool.core.date.DateTime;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MpMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        DateTime now = DateTime.now();
        /* 创建时间 */
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
        /* 操作人 */
        String username = "System";
        try {
            username = CsSecurityHelper.getCurrentUsername();
        } catch (Exception ignored) {
        }
        this.strictInsertFill(metaObject, "createBy", String.class, username);
        this.strictInsertFill(metaObject, "updateBy", String.class, username);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        /* 更新时间 */
        this.strictUpdateFill(metaObject, "updateTime", Date.class, DateTime.now());
        /* 操作人 */
        String username = "System";
        try {
            username = CsSecurityHelper.getCurrentUsername();
        } catch (Exception ignored) {
        }
        this.strictUpdateFill(metaObject, "updateBy", String.class, username);
    }
}

