package hello.advanced.aop;

import hello.advanced.aop.order.OrderRepository;
import hello.advanced.aop.order.OrderService;
import hello.advanced.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
//@Import({AspectV5Order.LogAspect.class, AspectV5Order.TransactionAspect.class})
@Import(AspectV6Advice.class)
public class AopTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderRepository));
        assertThat(AopUtils.isAopProxy(orderService)).isTrue();
        assertThat(AopUtils.isAopProxy(orderRepository)).isTrue();
        orderService.orderItem("itemA");
    }

    @Test
    void success() {
        OrderService mock = Mockito.mock(OrderService.class);
        doNothing().when(mock).orderItem(isA(String.class));
        mock.orderItem("itemA"); // void test
        verify(mock, times(1)).orderItem("itemA");
    }

    @Test
    void exception() {
        OrderService mock = Mockito.mock(OrderService.class);
        doThrow(new IllegalStateException()).when(mock).orderItem("ex");
        assertThatThrownBy(() -> mock.orderItem("ex")).isInstanceOf(IllegalStateException.class);
    }


}
