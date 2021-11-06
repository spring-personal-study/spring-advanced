package hello.advanced.aop.pointcut;

import hello.advanced.aop.order.OrderService;
import hello.advanced.aop.order.member.MemberService;
import hello.advanced.aop.order.member.MemberServiceImpl;
import hello.advanced.aop.order.member.annotation.ClassAop;
import hello.advanced.aop.order.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@Import({AopMethodTest.BeanAspect.class, AopMethodTest.ParameterAspect.class})
@SpringBootTest
public class AopMethodTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void isAopExactMatch() {
        pointcut.setExpression("execution(public java.lang.String hello.advanced.aop.order.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void patternMatch() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nonono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch() {
        pointcut.setExpression("execution(* *.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* hello.advanced.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.advanced.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.advanced.aop.order.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.advanced.aop.order.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternalFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.advanced.aop.order.member.MemberService.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internal, MemberServiceImpl.class)).isFalse(); // 부모타입에 선언한 메서드까지만 됨
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.advanced.aop.order.member.MemberServiceImpl.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internal, MemberServiceImpl.class)).isTrue();
    }


    // String 타입의 파라매터 허용
    // (String)
    @Test
    void argsMatch() {
        pointcut.setExpression("execution (* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 정확히 하나의 파라미터 허용, 대신 모든 타입 허용
    // 파라매터가 없어야 함
    // ()
    @Test
    void noArgsMatch() {
        pointcut.setExpression("execution (* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 정확히 하나의 파라미터 허용, 대신 모든 타입 허용
    // (Xxx)
    @Test
    void OneArgsMatch() {
        pointcut.setExpression("execution (* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String 타입으로 시작, 숫자와 무관하게 모든 파라매터, 모든 타입 허용
    // (String), (String, Xxx), (String, Xxx, Xxx)
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution (* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * within: "타입"만 매칭 검사, 부모타입 매칭 못함
     */
    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.advanced.aop.order.member.MemberServiceImpl)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 부모타입 매칭 못함
     */
    @Test
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.advanced.aop.order.member.MemberService)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.advanced.aop.order.member.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * within 은 매칭 검사시 부모의 경우 적용이 안되지만
     * target 은 부모까지 매칭해준다. (execution 과의 다른 점은 within 과 마찬가지로 타입 매칭에만 사용된다는 것)
     */
    @Test
    public void targetTest() {
        pointcut.setExpression("target(hello.advanced.aop.order.member.MemberService)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * args: 런타임에 들어온 객체에 대한 타입 매칭 검사
     * execution: 컴파일 타임에 타입 매칭 검사
     */
    @Test
    void argsVsExecution() {

       // Args: Matching Resolve in Runtime
       assertThat(pointcut("args(String)")
               .matches(helloMethod, MemberServiceImpl.class)).isTrue();
       assertThat(pointcut("args(java.io.Serializable)")
               .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // Execution: Matching Resolve in Compile
        assertThat(pointcut("execution(* *(String))")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(java.io.Serializable))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse(); // 매칭 실패
        assertThat(pointcut("execution(* *(Object))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse(); // 매칭 실패
    }

    private AspectJExpressionPointcut pointcut(String match) {
        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression(match);
        return pc;
    }

    /**
     * @annotation 지시자: 애너테이션이 있는 클래스 / 메서드 등을 매칭하여 aop 를 실행할 수 있다
     */
    @Test
    public void annotationMatch() {
        pointcut.setExpression("@annotation(hello.advanced.aop.order.member.annotation.MethodAop)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
    @Slf4j
    @Aspect
    static class AtAnnotationAspect {
        @Around("@annotation(hello.advanced.aop.order.member.annotation.ClassAop)")
        public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@annotation] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
    */


    @Autowired
    private OrderService orderService;
    /**
     * bean 의 이름으로 매칭 검사를 하여 aop 를 적용한다. 스프링에서만 가능하다.
     * // @Import(AopMethodTest.BeanAspect.class) 요구됨 - 빈으로 등록해서 사용
     */

    @Autowired
    private MemberService memberService;

    @Test
    void successBeanAspect() {
        orderService.orderItem("itemA");
    }

    @Test
    void successArgsAndTargetAndThis() {
        memberService.hello("hello ~");
    }

    @Aspect
    static class BeanAspect {
        @Around("bean(orderService) || bean(*Repository)")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[bean] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.advanced.aop..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[arg1] {}, [args1]={}", arg1, joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg123, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg123) throws Throwable {
            log.info("[arg2] {}, [args2]={}", arg123, joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg1234, ..)")
        public void logArgs3(String arg1234) {
            log.info("[arg3] {}", arg1234);
        }

        /**
         * target vs this:
         *
         * target()은 스프링 빈 객체, 다시 말해 aop 프록시가 가리키는 실제 객체를 대상으로 aop 를 수행한다.
         */
        @Around("allMember() && target(obj456)")
        public Object targetArgs(ProceedingJoinPoint joinPoint, MemberService obj456) throws Throwable {
            // target.obj: 실제 객체
            log.info("[target] {} obj={}", joinPoint.getSignature(), obj456.getClass());
            return joinPoint.proceed();
        }

        /**
         * this()는 반드시 적용 타입 하나를 정확하게 지정해야 한다. (아래의 경우 MemberService 가 지정됨)
         * 그 이유는 AOP 가 활성화된 프로젝트는 시 프록시 객체들이 스프링 빈 객체로 등록되기 때문이다.
         * this()는 스프링 빈 객체, 다시 말해 aop 프록시를 대상으로 aop 를 수행한다.
         */
        @Before("allMember() && this(obj123)") // @Before가 중요한 것이 아님: this / target 차이가 중요
        public void thisArgs(JoinPoint joinPoint, MemberService obj123) {
            // this.obj: 프록시 객체
            log.info("[this]{} obj={}", joinPoint.getSignature(), obj123.getClass());
        }
        @Around("allMember() && @target(annotation11)")
        public Object atTargetArgs(ProceedingJoinPoint joinPoint, ClassAop annotation11) throws Throwable {
            log.info("[@target]{}, obj={}", joinPoint.getSignature(), annotation11);
            return joinPoint.proceed();
        }

        @Before("allMember() && @within(annotation11)")
        public void atWithInArgs(JoinPoint joinPoint, ClassAop annotation11) {
            log.info("[@within]{}, obj={}", joinPoint.getSignature(), annotation11);
        }

        @Before("allMember() && @annotation(annotation11)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation11) {
            log.info("[@annotation]{}, annotationValue={}", joinPoint.getSignature(), annotation11.value());
        }

    }


}
