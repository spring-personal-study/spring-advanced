package hello.advanced;

import hello.advanced.app.proxy.v1.config.AppV1Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(AppV1Config.class)
@SpringBootApplication(scanBasePackages = "hello.advanced.app.proxy") // 주의
public class SpringAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAdvancedApplication.class, args);
    }

}
