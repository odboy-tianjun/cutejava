package cn.odboy;

import cn.odboy.framework.mybatisplus.core.CsMpCmdGenUtil;

import java.util.List;

/**
 * 代码生成入口
 */
public class GenCode {
    private static final String ADDR = "192.168.100.128";
    private static final Integer PORT = 23306;
    private static final String DATABASE_NAME = "cutejava";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PWD = "lm,101208..,.";
    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", ADDR, PORT, DATABASE_NAME);

    public static void main(String[] args) {
        CsMpCmdGenUtil generator = new CsMpCmdGenUtil();
        generator.setDatabaseUrl(URL);
        generator.setDriverClassName("com.mysql.cj.jdbc.Driver");
        generator.setDatabaseUsername(DATABASE_USER);
        generator.setDatabasePassword(DATABASE_PWD);
        //        genPipeline(generator);
        genSystem(generator);
    }

    private static void genPipeline(CsMpCmdGenUtil generator) {
        generator.gen("devops", "", List.of("pipeline_template", "pipeline_instance", "pipeline_instance_node_detail"));
    }

    private static void genSystem(CsMpCmdGenUtil generator) {
        generator.gen("system", "", List.of("system_oss_storage"));
    }
}
