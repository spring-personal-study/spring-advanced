package hello.advanced.app.config.v1_proxy.concrete_proxy;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.proxy.v2.OrderControllerV2;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        super(null);  // 부모 필드값 초기화하지 않음. 인터페이스가 아니라 클래스 상속이기 때문에
        // 부모의 생성자도 반드시 호출해야 하는데 null 을 넘겨하는 것은 어쩔 수 없는 부분이다,
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepositoryConcreteProxy.request()");
            String request = target.request(itemId);
            logTrace.end(status);
            return request;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
