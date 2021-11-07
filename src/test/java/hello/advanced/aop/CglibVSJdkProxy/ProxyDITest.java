package hello.advanced.aop.CglibVSJdkProxy;

import hello.advanced.aop.CglibVSJdkProxy.code.ProxyDIAspect;
import hello.advanced.aop.order.member.MemberService;
import hello.advanced.aop.order.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // false 는 jdk 동적 프록시 사용, true 는 cglib 사용
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

//    @Autowired
//    MemberServiceImpl memberServiceImpl; // <--  jdk 동적 프록시 설정을 사용할 때는 DI가 실패한다. : spring.aop.proxy-target-class=false
    // 이유: memberServiceImpl 에 주입이 시도되는 객체는,
    // MemberService 인터페이스를 구현한 JDK 동적프록시 객체이다. 그런데 이 프록시객체와 MemberServiceImpl 사이에는 어떠한 관련도 없다.
    // 따라서 실패.
    // 주의점: 반드시 jdk 동적 프록시 사용시에는 인터페이스로만 DI 주입을 받아야 한다.
    // 예외 메시지 번역: memberServiceImpl 에 주입되길 기대하는 타입은 MemberServiceImpl 이지만
    // 실제 넘어온 타입은 com.sun.proxy$60이다. 따라서 타입 예외가 발생했다.

    @Autowired
    MemberServiceImpl memberServiceImpl; // <--  cglib 프록시 설정 사용시에는 DI가 성공한다. : spring.aop.proxy-target-class=true
    // 이유: cglib 프록시는 구체 클래스인 MemberServiceImpl 를  상속받은 객체로써 생성되기 때문이다.
    // 또한 당연하게도, MemberServiceImpl 는 MemberService 를 구현한 클래스이므로
    // memberServiceImpl 는 MemberService 로도 캐스팅이 가능하다.
    // 여기까지만 보면 cglib 가 장점이 더 많은 것 같아보인다. 그러나 cglib 도 단점이 존재한다.
    // (실제로, 인터페이스 기반의 프록시 생성이 DI 관점에서 더 옳다는 점도 고려해야 한다.)

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello it's memberServiceImpl");
    }

}
