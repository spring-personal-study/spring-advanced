package hello.advanced;

import hello.advanced.app.config.AppV1Config;
import hello.advanced.app.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class) // 컴포넌트 스캔시 자동 등록하지 않는 설정파일을 스프링 빈으로 등록할 때 사용: 수동 스프링빈 등록
@Import({AppV2Config.class})
@SpringBootApplication(scanBasePackages = "hello.advanced.app.proxy")
public class SpringAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAdvancedApplication.class, args);
    }

}
