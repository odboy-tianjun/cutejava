package cn.odboy.util;

import cn.odboy.context.PipelineManage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PipelineTests {
    @Autowired
    private PipelineManage pipelineManage;

    @Test
    public void contextLoads() {
        pipelineManage.execute();
    }
}

