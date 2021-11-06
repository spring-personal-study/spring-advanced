package hello.advanced.aop.order.exam;

import hello.advanced.aop.order.exam.annotation.Retry;
import hello.advanced.aop.order.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    @Trace
    @Retry(4) // 네번까지 재시도하도록 값 설정
    public void request(String itemId) {
        examRepository.save(itemId);
    }
}
