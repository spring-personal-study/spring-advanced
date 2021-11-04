package hello.advanced.app.proxy.advisor;

import hello.advanced.app.proxy.common.advice.TimeAdvice;
import hello.advanced.app.proxy.common.service.ServiceImpl;
import hello.advanced.app.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    @DisplayName("직접 만든 어드바이스 적용")
    void advisorTest1() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
        log.info("advisor advice={}", advisor.getAdvice());
        log.info("advisor pointcut={}", advisor.getPointcut());
        log.info("proxyFactory={}", proxyFactory.getProxy());
        log.info("proxyClass={}", proxy.getClass());

    }

    @Test
    @DisplayName("직접 만든 포인트컷 사용")
    void advisorTest2() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointCut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save(); // 로그 찍힘 (포인트컷 matches true)
        proxy.find(); // 로그 안찍힘 (포인트컷 matches false)
        log.info("advisor advice={}", advisor.getAdvice());
        log.info("advisor pointcut={}", advisor.getPointcut());
        log.info("proxyFactory={}", proxyFactory.getProxy());
        log.info("proxyClass={}", proxy.getClass());

    }

    static class MyPointCut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);// 구현
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() { // 성능을 고려해 어떤 메서드를 실행할지 구분하는 메서드
            return false; // false 이면 matches(Method method, Class<?> targetClass)가 호출됨 (정적인 정보 사용, 캐싱 가능)
            // true 이면 matches(Method method, Class<?> targetClass, Object... args)가 호출됨 (동적인 정보 사용, 캐싱 불가)
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false; // 무시
        }
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷 사용")
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("save"); // 메서드명이 save인 경우에만 포인트컷 실행
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save(); // 로그 찍힘 (포인트컷 matches true)
        proxy.find(); // 로그 안찍힘 (포인트컷 matches false)
        log.info("pointcut={}", pointcut.getClass());
        log.info("pointcut getMethodMatcher={}", pointcut.getMethodMatcher());
        log.info("proxyFactory={}", proxyFactory.getProxy());
        log.info("proxyClass={}", proxy.getClass());

    }

    @Test
    @DisplayName("여러 프록시 사용")
    void multiAdvisorTest4() {
        // client -> proxy2(advisor2) -> proxy1(advisor1) -> target

        ServiceInterface target = new ServiceImpl();

        // 프록시1 생성
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        //프록시2 생성, target 으로 proxy1 지정
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(new MyPointCut(), new Advice2());
        proxyFactory2.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        // 실행
        // client -> proxy2(advisor2) -> proxy1(advisor1) -> target
        proxy2.save();


        // 이 방식의 문제점: 프록시를 2번 생성해야 해야 함. 즉 만약 적용해야 하는 어드바이저가 10개라면 10개의 프록시를 생성해야 한다.
        // 하나의 프록시에 여러 어드바이저를 적용할 수 있다면? multiAdvisorTest5() 참조
    }

    @Slf4j
    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    @Slf4j
    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }

    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    public void multiAdvisorTest5() {
        // proxy -> advisor2 -> advisor1 -> target
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor2);
        proxyFactory.addAdvisor(advisor1);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        //실행
        proxy.save();
    }
}

