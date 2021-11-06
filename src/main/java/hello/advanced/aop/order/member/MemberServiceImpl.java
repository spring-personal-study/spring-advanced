package hello.advanced.aop.order.member;

import hello.advanced.aop.order.member.annotation.ClassAop;
import hello.advanced.aop.order.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value!!!!")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "internal ok";
    }
}
