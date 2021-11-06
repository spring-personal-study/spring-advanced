package hello.advanced.aop.order.exam;

import hello.advanced.aop.order.exam.annotation.Retry;
import hello.advanced.aop.order.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번 중 1번은 반드시 실패하는 요청, 실패하면 기본 3번까지는 재시도한다.
     */
    @Trace
    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
