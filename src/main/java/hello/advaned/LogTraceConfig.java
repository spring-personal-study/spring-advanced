package hello.advaned;

import hello.advaned.trace.logtrace.FieldLogTrace;
import hello.advaned.trace.logtrace.LogTrace;
import hello.advaned.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
