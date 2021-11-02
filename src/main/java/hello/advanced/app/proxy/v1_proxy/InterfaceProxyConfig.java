package hello.advanced.app.proxy.v1_proxy;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v1.*;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    /**
     * 주의: 프록시를 빈으로 등록하되, 실제 객체는 빈으로 등록하지 말아야 한다.
     * @return 프록시 객체 빈으로 등록
     */
    @Bean
    public OrderControllerV1 orderController() {
        OrderControllerImplV1 controllerImpl = new OrderControllerImplV1(orderService());
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace());
    }

    @Bean
    public OrderServiceV1 orderService() {
        OrderServiceImplV1 serviceImpl = new OrderServiceImplV1(orderRepository());
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace());
    }

    @Bean
    public OrderRepositoryV1 orderRepository() {
        OrderRepositoryImplV1 repositoryImpl = new OrderRepositoryImplV1();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace());
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
