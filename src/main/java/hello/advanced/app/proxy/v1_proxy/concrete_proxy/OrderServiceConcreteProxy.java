package hello.advanced.app.proxy.v1_proxy.concrete_proxy;

import hello.advanced.app.notproxy.trace.TraceStatus;
import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.proxy.v2.OrderServiceV2;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null); // 부모 필드값 초기화하지 않음. 인터페이스가 아니라 클래스 상속이기 때문에
        // 부모의 생성자도 반드시 호출해야 하는데 null 을 넘겨하는 것은 어쩔 수 없는 부분이다,
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepositoryConcreteProxy.orderItem()");
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
