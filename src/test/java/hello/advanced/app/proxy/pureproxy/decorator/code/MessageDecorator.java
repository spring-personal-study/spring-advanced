package hello.advanced.app.proxy.pureproxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageDecorator implements Component {

    private final Component component;
    private String cache;


    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        // 부가기능 추가(데코레이터)
        String result = component.operation();
        String decoratedResult = " !! " + result + " !! ";

        log.info("MessageDecorator 꾸미기 적용 전={}, 적용 후={}", result, decoratedResult);

        return decoratedResult;
    }
}
