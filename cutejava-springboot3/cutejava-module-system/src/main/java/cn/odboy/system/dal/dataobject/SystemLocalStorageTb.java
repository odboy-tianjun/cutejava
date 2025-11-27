/*
 * Copyright 2021-2025 Odboy
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

package cn.odboy.system.dal.dataobject;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.odboy.base.KitBaseUserTimeTb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("system_local_storage")
public class SystemLocalStorageTb extends KitBaseUserTimeTb {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "ID", hidden = true)
    private Long id;

    @Schema(name = "真实文件名")
    private String realName;

    @Schema(name = "文件名")
    private String name;

    @Schema(name = "后缀")
    private String suffix;

    @Schema(name = "路径")
    private String path;

    @Schema(name = "类型")
    private String type;

    @Schema(name = "大小")
    private String size;

    @Schema(name = "日期分组")
    private String dateGroup;

    public void copy(SystemLocalStorageTb source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}