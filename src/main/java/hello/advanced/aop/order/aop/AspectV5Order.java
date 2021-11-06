package hello.advanced.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

// 어드바이스 순서 지정
@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("hello.advanced.aop.order.aop.PointCuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1) // 트랜잭션에 대한 어드바이스가 먼저 실행됨 (숫자가 낮을수록 높은 실행 우선순위)
    public static class TransactionAspect {
        @Around("hello.advanced.aop.order.aop.PointCuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                // @Before
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());

                Object result = joinPoint.proceed();

                // @AfterReturning
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;

            } catch (Exception e) {

                // @AfterThrowing
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;

            } finally {

                // @After
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());

            }
        }
    }
}
