package hello.advanced.aop.order.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV0 {

    // 내부호출에도 AOP 를 적용할 수 있도록 해결하는 방법 (여러가지 방법 중에서 한가지를 선택해서 사용하면 된다.)
    // *1. 자기자신 주입
    // 참고: 자기 자신을 생성자 주입하는 것은 순환 사이클을 만들기 때문에 실패한다. 필드 주입 또는 Setter 주입 방식 으로 해결해야 한다.
    //@Autowired
    private CallServiceV0 callServiceV0;

    @Autowired
    public void setCallServiceV0(CallServiceV0 callServiceV0) {
        log.info("callServiceV0 setter={}", callServiceV0.getClass());
        this.callServiceV0 = callServiceV0;
    }

    // 내부호출에도 AOP 를 적용할 수 있도록 해결하는 방법 (여러가지 방법 중에서 한가지를 선택해서 사용하면 된다.)
    // *2. ApplicationContext 생성자 주입하여 bean 을 꺼내 internal 호출
    private final ApplicationContext applicationContext;

    // 내부호출에도 AOP 를 적용할 수 있도록 해결하는 방법 (여러가지 방법 중에서 한가지를 선택해서 사용하면 된다.)
    // *3. ObjectProvider 생성자 주입하여 bean 을 꺼내 internal 호출:
    // applicationContext 는 기능이 너무 많은 무거운 객체이므로 상대적으로 덜 무거운 객체를 사용하는 방법이다.
    // (ObjectProvider 는 빈 객체를 스프링 컨테이너에서 꺼내는 것에만 특화된 객체이다.)
    private final ObjectProvider<CallServiceV0> callServiceV0Provider;

    // 내부호출에도 AOP 를 적용할 수 있도록 해결하는 방법 (여러가지 방법 중에서 한가지를 선택해서 사용하면 된다.)
    // *4. 구조 변경 (중첩 클래스를 사용해도 되고, 클래스를 완전히 분리해도 된다. (이 프로젝트 내에서는 아래의 중첩 클래스만 만들어 사용했다.))
    private final InternalService internalService;

    @Component
    static class InternalService {
        public void internal() {
            log.info("call internal\n");
        }
    }

    public void external() {
        int seq = 0;

        log.info("call external\n");
        log.info("aop 적용 실패 case {}: this.internal()", ++seq);
        this.internal(); // *0. 문제점 발생: 내부 메서드 호출하는데, aop 호출이 안 됨.
                         // 이유: this 는 CallServiceV0를 뜻하며, 다시말해 this 가 스프링 빈으로 등록된 CallServiceV0$$EnhancerBySpringCGLIB 프록시 객체가 아니기 때문이다.
                         // CallServiceV0는 프록시 객체가 아니기 때문에 AOP 작업도 수행되지 않으며 어드바이스도 적용이 불가능해진다.

        log.info("aop 적용 성공 case {}: callServiceV0.internal()", ++seq);
        // *1-1. internal() 호출: AOP 적용가능: 사실상 외부에서 internal()을 호출하는 것과 동일.
        // (외부에서 aop 프록시로 등록된 callServiceV0$EnhancerBySpringCGLIB 의 internal()을 호출하는 방식이다.)
        callServiceV0.internal();

        // *2-1. 애플리케이션 컨텍스트에서 꺼내서 사용: AOP 적용가능:
        // @Aspect 활성화시 스프링 빈으로 등록되는 것은 실제객체인 CallServiceV0가 아니라  CallServiceV0$$EnhancerBySpringCGLIB 프록시 객체에 가능한 것이다..
        CallServiceV0 bean = applicationContext.getBean(CallServiceV0.class);
        log.info("aop 적용 성공 case {}: bean.internal()", ++seq);
        bean.internal();

        // *3-1. ObjectProvider 에서 꺼내서 사용: AOP 적용가능:
        CallServiceV0 object = callServiceV0Provider.getObject();
        log.info("aop 적용 성공 case {}: object.internal()", ++seq);
        object.internal();

        // *4-1. 구조 변경 (internal() 메서드를 다른 클래스로 분화시켜 주입 받게 한다.)
        log.info("aop 적용 성공 case {}: internalService.internal()", ++seq);
        internalService.internal();
    }

    public void internal() {
        log.info("call internal\n");
    }
}
