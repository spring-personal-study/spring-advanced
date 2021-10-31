package hello.advaned.app.v3;

import hello.advaned.trace.TraceId;
import hello.advaned.trace.TraceStatus;
import hello.advaned.trace.hellotrace.HelloTraceV2;
import hello.advaned.trace.logtrace.FieldLogTrace;
import hello.advaned.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;

        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }


    }
}
