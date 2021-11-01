package hello.advanced.app.notproxy.trace.hellotrace;

import hello.advanced.app.notproxy.trace.TraceId;
import hello.advanced.app.notproxy.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        // 로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2에서 추가
    public TraceStatus beginSync(TraceId previousTraceId, String message) {
        TraceId nextId = previousTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        // 로그 출력
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }

    // level=0:    none
    // level=1:     |-->
    // level=2:     |   |-->
    // level=2 ex:  |   |<X-
    // level=1 ex:  |<X-
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= level; i++) {
            sb.append((i == level) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

}
