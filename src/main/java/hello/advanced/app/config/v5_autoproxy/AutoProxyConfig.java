package hello.advanced.app.config.v5_autoproxy;

import hello.advanced.app.config.AppV1Config;
import hello.advanced.app.config.AppV2Config;
import hello.advanced.app.config.v3_proxyfactory.LogTraceAdvice;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig { // starter-aop lib (AnnotationAwareAspectJAutoProxyCreator) registers all advisor automatically

    //@Bean // advisor1 대신 advisor2 사용
    public Advisor advisor1(LogTrace logTrace) {

        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.advanced.app..*(..)) && !execution(* hello.advanced.app..noLog(..))");
                                       // *: 모든 반환 타입
                                       // hello.advanced.app..: 패키지와 그 하위 패키지
                                       // *: 모든 메서드
                                       // (..): 파라매터 상관없이 적용

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
