package hello.advanced.app.config.v2_dynamicProxy;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    // 패턴에 맞는 메서드가 오는 경우에만 로그를 남기도록 설정
    private final String[] patterns;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        // 메서드 이름 필터
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }
        // save, request, requ*, *est 등의 패턴에 일치할때만 로그를 찍게 된다.

        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            //로직 호출
            Object request = method.invoke(target, args);
            logTrace.end(status);
            return request;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }
}
