package hello.advaned.trace.threadlocal;

import hello.advaned.trace.threadlocal.code.FieldService;
import hello.advaned.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService threadLocalService = new ThreadLocalService();

    @Test
    @DisplayName("동시성 문제가 발생하는 테스트")
    void field1() {
        log.info("main start");
        Runnable userA = () -> threadLocalService.logic("userA");
        Runnable userB = () -> threadLocalService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        threadLocalService.sleep(100); // 동시성 문제 발생하지 않음
        threadB.start();
        threadLocalService.sleep(2000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }

    @Test
    @DisplayName("동시성 문제가 발생하지 않는 테스트")
    void field2() {
        log.info("main start");
        Runnable userA = () -> threadLocalService.logic("userA");
        Runnable userB = () -> threadLocalService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadA.setName("thread-B");

        threadA.start();
        threadLocalService.sleep(2000); // 동시성 문제 발생하지 않음
        threadB.start();
        threadLocalService.sleep(2000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }
}
