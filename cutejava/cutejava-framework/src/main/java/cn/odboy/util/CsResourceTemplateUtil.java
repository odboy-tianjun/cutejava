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

package cn.odboy.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import lombok.experimental.UtilityClass;

/**
 * Resources模版工具
 *
 * @author odboy
 * @date 2025-06-22
 */
@UtilityClass
public class CsResourceTemplateUtil {
    public static String render(String moduleName, String filename, Dict params) {
        String path = StrUtil.isBlank(moduleName) ? "template" : "template/" + moduleName;
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(path, TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate(filename);
        return template.render(params);
    }
}
