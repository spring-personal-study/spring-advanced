package hello.advanced.app.proxy.v2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
