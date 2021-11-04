package hello.advanced.app.config.v1_proxy.interface_proxy;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.proxy.v1.OrderRepositoryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepositoryInterfaceProxy.request()");
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }
}
