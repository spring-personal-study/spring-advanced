package hello.advanced.app.notproxy.trace.threadlocal;

import hello.advanced.app.notproxy.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    @DisplayName("동시성 문제를 확인하는 테스트")
    void field1() {
        log.info("main start");
        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        fieldService.sleep(100); // 동시성 문제 발생 (start()는 1초대기인데 sleep()은 0.1초 대기)
        threadB.start(); // threadA.start()에서 nameStore를 조회할때는 userA가 나와야하는데, treadB.start()가 중간에 끼어들어 userA를 지워버리고 userB로 덮어씌우는 문제가 발생함.
        fieldService.sleep(2000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }

    @Test
    @DisplayName("동시성 문제가 발생하지 않는 테스트")
    void field2() {
        log.info("main start");
        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        fieldService.sleep(2000); // 동시성 문제 발생하지 않음 (threadA.start()는 1초대기인데 2초동안 충분히 기다렸다가 theadB.start()를 실행하므로)
        threadB.start();
        fieldService.sleep(2000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }
}
