package hello.advanced.app.proxy.pureproxy.decorator;

import hello.advanced.app.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    @DisplayName("데코레이터 없이 코드 사용")
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    @Test
    @DisplayName("데코레이터 하나만 사용하기")
    void decorator1() {
        RealComponent component = new RealComponent();
        MessageDecorator decorator = new MessageDecorator(component);
        DecoratorPatternClient client = new DecoratorPatternClient(decorator);
        client.execute();
    }

    @Test
    @DisplayName("여러개 데코레이터 적용하기")
    void decorator2() {
        RealComponent component = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(component);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }

    @Test
    @DisplayName("여러개 데코레이터와 프록시 함께 사용하기")
    void decoratorWithProxy() {
        RealComponent component = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(component);   // 데코레이터1 생성
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);     // 데코레이터2 생성
        ComponentCacheProxy cacheProxy = new ComponentCacheProxy(timeDecorator); // 프록시 생성
        DecoratorPatternClient client = new DecoratorPatternClient(cacheProxy);
        // 프록시 사용 (최종 결과값을 캐싱하고 싶다면 프록시는 끝에서 전달)
        client.execute();
        client.execute();
        client.execute();
    }
}
