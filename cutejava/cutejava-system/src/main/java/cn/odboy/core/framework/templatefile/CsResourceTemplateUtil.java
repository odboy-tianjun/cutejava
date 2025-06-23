package cn.odboy.core.framework.templatefile;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;

/**
 * Resources模版工具
 *
 * @author odboy
 * @date 2025-06-22
 */
public class CsResourceTemplateUtil {
    public static String render(String moduleName, String filename, Dict params) {
        String path = StrUtil.isBlank(moduleName) ? "template" : "template/" + moduleName;
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(path, TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate(filename);
        return template.render(params);
    }
}
