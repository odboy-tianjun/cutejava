package cn.odboy;


import cn.odboy.core.framework.mybatisplus.core.CsMpCmdGenUtil;

import java.util.List;

/**
 * 代码生成入口
 *
 * @date 2024-04-27
 */
public class GenCode {
    public static void main(String[] args) {
        CsMpCmdGenUtil generator = new CsMpCmdGenUtil();
        generator.setDatabaseUrl(String.format("jdbc:mysql://%s:%s/%s", "localhost", 3306, "cutejava20250321"));
        generator.setDriverClassName("com.mysql.cj.jdbc.Driver");
        generator.setDatabaseUsername("root");
        generator.setDatabasePassword("123456");
        genCareer(generator);
    }

    private static void genCareer(CsMpCmdGenUtil generator) {
        generator.gen("tool_", List.of(
//                "tool_qiniu_content",
        ));
    }
}
