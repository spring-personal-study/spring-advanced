package hello.advanced.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {
    @Around("hello.advanced.aop.order.aop.PointCuts.allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    // 전역적으로 사용 가능 (before + afterReturning + afterThrowing + after. 특이사항: 반환값 조작 가능, 조인포인트 실행할지 말지 선택가능, try catch 사용가능)
    // 단점: 어떤 부분에서 실행할지 명확하지 않아 실수가 발생될 여지가 (다른 어드바이스-AfterReturning 등-에 비해) 상대적으로 높음
    @Around("hello.advanced.aop.order.aop.PointCuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable { // 반드시 ProceedingJoinPoint 사용
        try {
            // @Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed(); // 호출 안하면 조인포인트가 실행되지 않음, 하지만 호출하지 않으면 타겟이 호출되지 않음 (체이닝이 안되어서) (다시 말해 버그처럼 작용함)
            // @AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result; // 다른 것을 반환할 수도 있음
        } catch (Exception e) {
            // @AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    // 조인 포인트 실행 전
    @Before("hello.advanced.aop.order.aop.PointCuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[메서드 인수] {}, [프록시 객체] {}, [조언되는 방법 설명] {}, [대상 객체] {}, [조언되는 메서드 설명] {} [kind] {} [staticPart] {} [toShortString] {} [toLongString] {}",
                joinPoint.getArgs(),
                joinPoint.getThis(),
                joinPoint.toString(),
                joinPoint.getTarget(),
                joinPoint.getSignature(),
                joinPoint.getKind(),
                joinPoint.getStaticPart(),
                joinPoint.toShortString(),
                joinPoint.toLongString());

        log.info("[before] {}", joinPoint.getSignature());
    }

    // 메서드 실행이 정상적으로 반환될 때
    @AfterReturning(value = "hello.advanced.aop.order.aop.PointCuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    // 에러가 반환되었을 때
    @AfterThrowing(value = "hello.advanced.aop.order.aop.PointCuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    // 결과에 상관없이 조인포인트가 실행된 후
    @After(value = "hello.advanced.aop.order.aop.PointCuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }

    // 메서드 실행이 정상적으로 반환될 때
    @AfterReturning(value = "hello.advanced.aop.order.aop.PointCuts.allOrder()", returning = "result")
    public void doReturn2(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

}
