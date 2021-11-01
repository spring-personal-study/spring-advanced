package hello.advanced.app.notproxy.trace.logtrace;

import hello.advanced.app.notproxy.trace.TraceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
/*
@SpringBootTest
@ExtendWith(SpringExtension.class)
class FieldLogTraceTest {

    @Autowired
    private LogTrace trace;

    @Test
    void begin_end() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        TraceStatus status3 = trace.begin("hello3");
        trace.end(status3);
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        TraceStatus status3 = trace.begin("hello3");
        trace.exception(status3, new IllegalStateException());
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }


}

 */