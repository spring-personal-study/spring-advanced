package hello.advanced.app.config.v2_dynamicProxy;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v1.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderControllerV1 orderControllerV1() {
        OrderControllerImplV1 target = new OrderControllerImplV1(orderServiceV1());
        return (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceFilterHandler(target, logTrace(), PATTERNS));
        // LogTraceFilterHandler 는 각각 따로 넣어줘야 한다. target 이 모두 달라서 빈 등록이 안된다.
        // logTrace()는 스프링 빈으로 이미 등록했기 때문에 가져다 쓴다.
    }

    @Bean
    public OrderServiceV1 orderServiceV1() {
        OrderServiceImplV1 target = new OrderServiceImplV1(orderRepositoryV1());
        return (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceFilterHandler(target, logTrace(), PATTERNS));
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        OrderRepositoryImplV1 target = new OrderRepositoryImplV1();
        return (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceFilterHandler(target, logTrace(), PATTERNS));
    }

    //@Bean
    //public LogTraceFilterHandler logTraceFilterHandler(Object target, LogTrace trace) { // <-- target이 여러개 빈(orderServiceImplV1, orderRepositoryImpV1, ...으로 등록될 수 있어 에러가 발생한다.
    //    return new LogTraceFilterHandler(target, trace, PATTERNS);
    //}

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}