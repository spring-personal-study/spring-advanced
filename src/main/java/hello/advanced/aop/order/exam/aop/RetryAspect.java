package hello.advanced.aop.order.exam.aop;

import hello.advanced.aop.order.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Objects;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry11)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry11) throws Throwable {
        log.info("[retry] {} args={}", joinPoint.getSignature(), retry11);

        int maxRetry = retry11.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw Objects.requireNonNull(exceptionHolder);
    }
}
