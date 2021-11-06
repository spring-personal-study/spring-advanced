package hello.advanced.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;


public class PointCuts {

    // hello.advanced.aop.order 패키지 및 하위 패키지 모두에 적용
    @Pointcut("execution(* hello.advanced.aop.order..*(..))")
    public void allOrder() {} // pointcut signature

    // hello.advanced.aop.order 패키지 및 하위 패키지에 있는 클래스 중에서,
    // 이름 패턴이 *Service 클래스 하위의 모든 메서드에 적용
    @Pointcut("execution(* hello.advanced..aop.order..*Service.*(..))")
    public void allService(){}

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}

}
