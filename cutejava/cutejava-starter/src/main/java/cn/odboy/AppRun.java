package cn.odboy;

import cn.odboy.framework.context.CsBootApplication;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;

@Api(hidden = true)
@SpringBootApplication
public class AppRun extends CsBootApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication springApplication = new SpringApplication(AppRun.class);
        inited(springApplication.run(args));
    }
}
