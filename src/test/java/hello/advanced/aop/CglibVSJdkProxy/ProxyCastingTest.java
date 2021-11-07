package hello.advanced.aop.CglibVSJdkProxy;

import hello.advanced.aop.order.member.MemberService;
import hello.advanced.aop.order.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.objenesis.SpringObjenesis;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

// difference between cglib and jdk proxy
@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() { // 한계점
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시 사용 하도록 설정

        // 프록시를 인터페이스로 캐스팅 시도: 성공:
        // JDK 동적 프록시는 인터페이스 기반으로 생성되기 때문에 인터페이스가 존재하는 경우 프록시 객체라 해도 캐스팅할 수 있음.
        MemberService jdkProxy = (MemberService) proxyFactory.getProxy();

        log.info("jdkProxy class={}", jdkProxy.getClass());

        // 프록시를 구체 클래스로 캐스팅 시도: 실패(에러): ClassCastException
        // 프록시 객체는 MemberService 인터페이스를 따로 구현한 클래스이므로 MemberServiceImpl 과는 어떤 상관관계도 없다. 따라서 (당연히) 실패.
        assertThatThrownBy(() -> { MemberServiceImpl castingMemberService = (MemberServiceImpl) jdkProxy; })
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("cannot be cast");
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB 프록시 사용하도록 설정.

        // 프록시를 인터페이스로 캐스팅 시도: 성공:
        // CGLIB 는 MemberServiceImpl 을 상속받은 객체를 프록시로 만들기 때문에,
        // MemberServiceImpl 이 (MemberService 를 구현하고 있으므로) (당연히) MemberService 인터페이스로도 캐스팅이 성공한다.
        MemberService cglibProxy = (MemberService) proxyFactory.getProxy();

        log.info("cglibProxy class={}", cglibProxy.getClass());

        // 프록시를 구체 클래스로 캐스팅 시도: 성공
        // 위에서도 언급했듯 CGLIB 는 MemberServiceImpl 을 상속받은 객체를 프록시로 만들기 때문에 구체클래스로도 캐스팅 가능.
        MemberServiceImpl castingMemberService = (MemberServiceImpl) cglibProxy;
    }

    // 여기까지만 보면 cglib 가 장점이 더 많은 것 같아보인다. 그러나 cglib 도 단점이 존재한다.
    // (실제로, 인터페이스 기반의 프록시 생성--다시 말해 jdk proxy 방식의 프록시 생성 방식--이 DI 관점에서 더 옳다는 점도 고려해야 한다.)
    // 단점은 간략하게 아래와 같이 기술하겠다.
    // CGLIB 구체 클래스 기반 프록시 문제점:
    // 1. 타겟클래스(구체부모클래스)에서 기본 생성자 제공이 필수
    // 예시 코드는 아래와 같다.
    /*
        public class 구체부모클래스 {
            private String 어떤필드변수;

            public 구체부모클래스() {}
            public 구체부모클래스(String 어떤필드변수) {
                this.어떤필드변수 = 어떤필드변수;
            }
        }

        public class CGLIB_프록시클래스 extends 구체부모클래스 {
            CGLIB_프록시클래스() {
                super(); // 기본 생성자 호출함
            }
        }
     */

    // 2. 생성자 두번 호출 문제 (구체 클래스를 상속받아서 만들어지는 프록시 객체이므로, 프록시의 생성자 호출시 부모 클래스의 생성자 또한 호출되어야 한다.
    // 예시 코드는 아래와 같다.
    /*
        public class 구체부모클래스 {
            private String 어떤필드변수;

            구체부모클래스(String 어떤필드변수) {
                this.어떤필드변수 = 어떤필드변수;
            }
        }

        public class CGLIB_프록시클래스 extends 구체부모클래스 {

            private String 어떤필드변수;
            private String 어쩌구;

            CGLIB_프록시클래스(String 어떤필드변수, String 어쩌구) {
                super(어떤필드변수); // CGLIB 프록시 클래스 생성자 안에서 구체부모클래스의 생성자가 반드시 먼저 호출되어야 한다는 제약이 있다.
                this.어떤필드변수 = 어떤필드변수;
                this.어쩌구 = 어쩌구;
            }
        }
     */

    // 3. 타겟 클래스(구체부모클래스)는 final 키워드 적용이 불가능



    /*@Test
    void test() {
        SpringObjenesis objenesis = new SpringObjenesis();
        부모클래스 부모클래스 = objenesis.newInstance(부모클래스.class); // 기본생성자 없이도 초기화가 된다..???
        log.info("부모클래스.어떤필드변수의 값: {}", 부모클래스.어떤필드변수); // log: 부모클래스.어떤필드변수의 값: null
    }*/
}
