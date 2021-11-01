package hello.advanced.app.proxy.v3;

import hello.advanced.app.proxy.v2.OrderRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
