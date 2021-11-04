package hello.advanced.app.config.v1_proxy;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.advanced.app.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.advanced.app.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.advanced.app.proxy.v2.OrderControllerV2;
import hello.advanced.app.proxy.v2.OrderRepositoryV2;
import hello.advanced.app.proxy.v2.OrderServiceV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

    /**
     * 주의: 프록시를 빈으로 등록하되, 실제 객체는 빈으로 등록하지 말아야 한다.
     *
     * @return 프록시 객체 빈으로 등록
     */
    @Bean
    public OrderControllerV2 orderController2() {
        OrderControllerV2 controllerV2 = new OrderControllerV2(orderService2());
        return new OrderControllerConcreteProxy(controllerV2, logTrace2());
    }

    @Bean
    public OrderServiceV2 orderService2() {
        OrderServiceV2 serviceV2 = new OrderServiceV2(orderRepository2());
        return new OrderServiceConcreteProxy(serviceV2, logTrace2());
    }

    @Bean
    public OrderRepositoryV2 orderRepository2() {
        OrderRepositoryV2 repositoryV2 = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(repositoryV2, logTrace2());
    }

    @Bean
    public LogTrace logTrace2() {
        return new ThreadLocalLogTrace();
    }
}
