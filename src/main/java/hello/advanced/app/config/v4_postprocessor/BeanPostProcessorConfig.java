package hello.advanced.app.config.v4_postprocessor;

import hello.advanced.app.config.AppV1Config;
import hello.advanced.app.config.AppV2Config;
import hello.advanced.app.config.v3_proxyfactory.LogTraceAdvice;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTracePostProcessor packageLogTracePostProcessor() {
        return new PackageLogTracePostProcessor("hello.advanced.app", getAdvisor(logTrace()));
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
