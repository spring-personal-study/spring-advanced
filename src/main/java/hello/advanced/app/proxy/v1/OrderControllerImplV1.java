package hello.advanced.app.proxy.v1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerImplV1 implements OrderControllerV1 {

    private final OrderServiceV1 orderServiceV1;

    @Override
    public String request(String itemId) {
        orderServiceV1.orderItem(itemId);
        return "ok";
    }

    @Override
    public String noLog() {
        return "no-log ok";
    }
}
