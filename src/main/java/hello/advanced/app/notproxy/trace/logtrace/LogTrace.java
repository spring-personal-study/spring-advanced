package hello.advanced.app.notproxy.trace.logtrace;

import hello.advanced.app.notproxy.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);

}
