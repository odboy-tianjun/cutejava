/*
 * Copyright 2021-2026 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.system.framework.mybatisplus;

import cn.hutool.core.date.DateTime;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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
      username = KitSecurityHelper.getCurrentUsername();
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
      username = KitSecurityHelper.getCurrentUsername();
    } catch (Exception ignored) {
    }
    this.strictUpdateFill(metaObject, "updateBy", String.class, username);
  }
}
