package hello.advanced.app.notproxy.config;

import hello.advanced.app.notproxy.trace.logtrace.LogTrace;
import hello.advanced.app.notproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
