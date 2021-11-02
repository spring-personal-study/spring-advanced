package hello.advanced.app.proxy.jdkDynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 대상이 다름, 동적 처리 필요
        log.info("result={}", result1);
        //공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 대상이 다름, 동적 처리 필요, target에 메서드가 존재하지 않으면 컴파일시간에 에러 발생
        log.info("result={}", result2);
        //공통 로직2 종료
    }

    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 클래스 정보
        Class<?> classHello = Class.forName("hello.advanced.app.proxy.jdkDynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA 메서드 정보
        Method callA = classHello.getMethod("callA"); // 런타임에 어떤 메소드를 사용할지 문자열로 전달가능, 대신 메서드가 존재하지 않으면 런타임시간에 에러 발생
        Object result1 = callA.invoke(target);
        log.info("result1={}", result1);

        // callB 메서드 정보
        Method callB = classHello.getMethod("callB");
        Object result2 = callB.invoke(target);
        log.info("result2={}", result2);
    }

    @Test
    void reflection2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> classHello = Class.forName("hello.advanced.app.proxy.jdkDynamic.ReflectionTest$Hello");
        Hello target = new Hello();
        dynamicCall(classHello.getMethod("callA"), target);
        dynamicCall(classHello.getMethod("callB"), target);
    }

    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
