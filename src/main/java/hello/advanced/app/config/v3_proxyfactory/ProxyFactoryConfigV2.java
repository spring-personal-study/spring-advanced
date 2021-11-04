package hello.advanced.app.config.v3_proxyfactory;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v2.OrderControllerV2;
import hello.advanced.app.proxy.v2.OrderRepositoryV2;
import hello.advanced.app.proxy.v2.OrderServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderControllerV2 orderControllerV2() {
        OrderControllerV2 orderController = new OrderControllerV2(orderServiceV2());
        ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderController.getClass());

        return (OrderControllerV2) proxyFactory.getProxy();
    }

    @Bean
    public OrderServiceV2 orderServiceV2() {
        OrderServiceV2 orderService = new OrderServiceV2(orderRepositoryV2());
        ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderService.getClass());

        return (OrderServiceV2) proxyFactory.getProxy();
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2() {
        OrderRepositoryV2 orderRepository = new OrderRepositoryV2();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderRepository.getClass());

        return (OrderRepositoryV2) proxyFactory.getProxy();
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
