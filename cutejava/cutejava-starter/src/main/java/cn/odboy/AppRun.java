package cn.odboy;

import cn.odboy.framework.context.CsBootApplication;
import com.anwen.mongo.config.MongoPlusAutoConfiguration;
import com.anwen.mongo.config.OverrideMongoConfiguration;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.net.UnknownHostException;

@Api(hidden = true)
@SpringBootApplication(exclude = OverrideMongoConfiguration.class)
@Import(MongoPlusAutoConfiguration.class)
public class AppRun extends CsBootApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication springApplication = new SpringApplication(AppRun.class);
        inited(springApplication.run(args));
    }
}
