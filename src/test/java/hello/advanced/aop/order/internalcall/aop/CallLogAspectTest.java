package hello.advanced.aop.order.internalcall.aop;

import hello.advanced.aop.order.internalcall.CallServiceV0;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallLogAspectTest {

    @Autowired
    private CallServiceV0 callServiceV0;

    @Test
    void external() {
        log.info("callServiceV0={}\n", callServiceV0.getClass()); // CGLIB 프록시 객체 프린트. (@Import 안하면 실제 객체 반환)
        callServiceV0.external(); // external 에서 internal 을 호출하지만 external() 실행시에만 aop 가 적용되고 internal()에는 aop 가 적용되지 않는다.
                                    // internal()은 CallServiceV0에 있는 것으로 호출되는 것이지 CGLIB 프록시 객체의 internal() 을 호출하는 것이 아니기 떄문이다.
                                    // 따라서 어드바이스도 동작하지 않는다.
    }

    @Test
    void internal() {
        callServiceV0.internal(); // aop 적용됨. (당연하다... external() 내부에서 internal()을 호출한 것이 아니라, 외부에서 internal()을 호출했기 때문이다.)
                                  // 어쨌든 지금은 프록시 방식의 AOP 의 경우, 메서드 내부 호출에 프록시를 적용할 수 없다는 문제점이 있다는 것만 알아두자.
                                    // (프록시 방식의 AOP 에 조작을 가해 메서드 내부 호출에도 프록시를 적용할 수 있도록 해결하는 방법은 존재한다.)
                                    // 해결방법들을 *기수 방식으로(*1. *2. *3. ... ) 실제코드 상에 주석으로 적어두었다.
    }

}