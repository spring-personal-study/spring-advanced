package hello.advanced.app.proxy.v1_proxy;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import hello.advanced.app.proxy.v1.*;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.advanced.app.proxy.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
@Configuration
public class InterfaceProxyConfig {

    /**
     * 주의: 프록시를 빈으로 등록하되, 실제 객체는 빈으로 등록하지 말아야 한다.
     * @return 프록시 객체 빈으로 등록
     */
    /*
      // 활성화하려면 DynamicProxyBasicConfig 쪽을 제거하거나 비활성화하세요.
      // ComponentScan 범위를 다르게 지정하거나, DynamicProxyBasicConfig 를 Scan 대상에서 제외하는 방법도 있습니다.
    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerImplV1 controllerImpl = new OrderControllerImplV1(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceImplV1 serviceImpl = new OrderServiceImplV1(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryImplV1 repositoryImpl = new OrderRepositoryImplV1();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }

}
*/
