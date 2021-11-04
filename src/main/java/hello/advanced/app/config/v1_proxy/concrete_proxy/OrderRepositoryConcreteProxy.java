package hello.advanced.app.config.v1_proxy.concrete_proxy;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.proxy.v2.OrderRepositoryV2;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {

    private final OrderRepositoryV2 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepositoryConcreteProxy.request()");
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
