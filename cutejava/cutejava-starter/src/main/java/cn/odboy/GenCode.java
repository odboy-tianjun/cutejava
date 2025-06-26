package cn.odboy;


import cn.odboy.framework.mybatisplus.core.CsMpCmdGenUtil;

import java.util.List;

/**
 * 代码生成入口
 *
 * @date 2024-04-27
 */
public class GenCode {
    private static final String ADDR = "localhost";
    private static final Integer PORT = 3306;
    private static final String DATABASE_NAME = "cutejava";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PWD = "root123456";
    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", ADDR, PORT, DATABASE_NAME);

    public static void main(String[] args) {
        CsMpCmdGenUtil generator = new CsMpCmdGenUtil();
        generator.setDatabaseUrl(URL);
        generator.setDriverClassName("com.mysql.cj.jdbc.Driver");
        generator.setDatabaseUsername(DATABASE_USER);
        generator.setDatabasePassword(DATABASE_PWD);
        genPipeline(generator);
    }

    private static void genPipeline(CsMpCmdGenUtil generator) {
        generator.gen("", List.of(
                "pipeline_template",
                "pipeline_instance",
                "pipeline_instance_node_detail"
        ));
    }
}
