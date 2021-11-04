package hello.advanced.app.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    // target 필요없음 (실제 객체 필요 없음)

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");

        long startTime = System.currentTimeMillis();

        // target 클래스의 정보는, 프록시 팩토리로 프록시를 생성하는 단계에서 전달받기 때문에 바로 사용할 수 있다.
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("TimeAdvice 종료 resultTime={}", resultTime);

        return result;
    }
}
