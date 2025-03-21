package cn.odboy;

import cn.odboy.annotation.rest.AnonymousGetMapping;
import cn.odboy.context.BootApplication;
import cn.odboy.context.SpringBeanHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(hidden = true)
@SpringBootApplication
public class AppRun extends BootApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AppRun.class);
        initd(springApplication.run(args));
    }



    /**
     * 访问首页提示
     *
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }
}
