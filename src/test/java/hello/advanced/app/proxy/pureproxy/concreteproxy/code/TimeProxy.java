package hello.advanced.app.proxy.pureproxy.concreteproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeProxy extends ConcreteLogic {

    private final ConcreteLogic logic;

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = logic.operation();
        long endTime = System.currentTimeMillis() - startTime;
        log.info("component.operation() 실행에 걸린 시간: {} ms", endTime);
        return result;
    }
}
