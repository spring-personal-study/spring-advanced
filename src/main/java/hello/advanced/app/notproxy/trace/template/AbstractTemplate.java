package hello.advanced.app.notproxy.trace.template;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            // 비즈니스 로직 호출
            T result = call();

            trace.end(status);
            return result;

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
