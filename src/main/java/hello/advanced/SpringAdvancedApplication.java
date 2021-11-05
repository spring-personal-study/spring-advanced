package hello.advanced;

import hello.advanced.app.config.v6_aop.AopConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Slf4j
//@Import({AutoProxyConfig.class}) // 컴포넌트 스캔시 자동 등록하지 않는 설정파일을 스프링 빈으로 등록할 때 사용: 수동 스프링빈 등록
@Import(AopConfig.class)
@SpringBootApplication(scanBasePackages = "hello.advanced.app.config.v6_aop.aspect")
public class SpringAdvancedApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAdvancedApplication.class, args);

        /*
            PersonOuterClass.Person p = PersonOuterClass.Person.newBuilder()
                                                .setActive(true)
                                                .setName("Charlie").build();
            System.out.println(p);
        */
    }
}
