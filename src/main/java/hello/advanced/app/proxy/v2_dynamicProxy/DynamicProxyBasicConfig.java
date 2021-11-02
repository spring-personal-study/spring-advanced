package hello.advanced.app.proxy.v2_dynamicProxy;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v1.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

/*
// 해당 설정을 사용하려면 DynamicProxyFilterConfig 를 비활성화하세요.
@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    public OrderControllerV1 orderControllerV1() {
        OrderControllerImplV1 target = new OrderControllerImplV1(orderServiceV1());
        return (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceBasicHandler(target, logTrace()));
        // LogTraceBasicHandler 는 각각 따로 넣어줘야 한다. target 이 모두 달라서 빈 등록이 안된다.
        // logTrace()는 스프링 빈으로 이미 등록했기 때문에 가져다 쓴다.
    }

    @Bean
    public OrderServiceV1 orderServiceV1() {
        OrderServiceImplV1 target = new OrderServiceImplV1(orderRepositoryV1());
        return (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceBasicHandler(target, logTrace()));
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        OrderRepositoryImplV1 target = new OrderRepositoryImplV1();
        return (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceBasicHandler(target, logTrace()));
    }

    //@Bean
    //public LogTraceBasicHandler logTraceBasicHandler(Object target, LogTrace trace) { // <-- target이 여러개 빈(orderServiceImplV1, orderRepositoryImpV1, ...으로 등록될 수 있어 에러가 발생한다.
    //    return new LogTraceBasicHandler(target, trace);
    //}

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
*/