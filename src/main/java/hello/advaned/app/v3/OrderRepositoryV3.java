package hello.advaned.app.v3;

import hello.advaned.trace.TraceId;
import hello.advaned.trace.TraceStatus;
import hello.advaned.trace.hellotrace.HelloTraceV1;
import hello.advaned.trace.hellotrace.HelloTraceV2;
import hello.advaned.trace.logtrace.FieldLogTrace;
import hello.advaned.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId) {

        TraceStatus status = null;

        try {
            status = trace.begin("OrderRepository.save()");

            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("에러 발생!");
            }
            sleep(1000);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
