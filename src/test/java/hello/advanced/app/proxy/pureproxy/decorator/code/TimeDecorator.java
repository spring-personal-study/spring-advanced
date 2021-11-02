package hello.advanced.app.proxy.pureproxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeDecorator implements Component {

    private final Component component;

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = component.operation();
        long endTime = System.currentTimeMillis() - startTime;
        log.info("component.operation() 실행에 걸린 시간");
        return result;
    }
}
