package hello.advanced.app.config.v3_proxyfactory;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

    @Bean
    public OrderControllerV1 orderControllerV1() {
        OrderControllerImplV1 orderController = new OrderControllerImplV1(orderServiceV1());
        ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderController.getClass());

        return (OrderControllerV1) proxyFactory.getProxy();
    }


    @Bean
    public OrderServiceV1 orderServiceV1() {
        OrderServiceImplV1 orderService = new OrderServiceImplV1(orderRepositoryV1());
        ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderService.getClass());

        return (OrderServiceV1) proxyFactory.getProxy();
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        OrderRepositoryImplV1 orderRepository = new OrderRepositoryImplV1();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvisor(getAdvisor(logTrace()));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy().getClass(), orderRepository.getClass());

        return (OrderRepositoryV1) proxyFactory.getProxy();
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
