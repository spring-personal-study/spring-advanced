package hello.advanced.app.proxy.proxyfactory;

import hello.advanced.app.proxy.common.advice.TimeAdvice;
import hello.advanced.app.proxy.common.service.ConcreteService;
import hello.advanced.app.proxy.common.service.ServiceImpl;
import hello.advanced.app.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시를 사용하는지 검증")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.find();
        proxy.save();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // cglib 든 jdk 동적 프록시든 프록시팩토리로 만들어졌으면 true
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 를 사용하는지 검증")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고, 클래스 기반의 프록시를 사용하는지 검증")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // class 기반으로 프록시 생성하기
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.find();
        proxy.save();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // cglib 든 jdk 동적 프록시든 프록시팩토리로 만들어졌으면 true
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
    }

}
