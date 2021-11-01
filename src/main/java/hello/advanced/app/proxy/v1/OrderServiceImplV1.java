package hello.advanced.app.proxy.v1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceImplV1 implements OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
