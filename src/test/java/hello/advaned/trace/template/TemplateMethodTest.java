package hello.advaned.trace.template;

import hello.advaned.trace.template.code.AbstractTemplate;
import hello.advaned.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행"); // 변하는 부분
        //비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }

    @Test
    @DisplayName("템플릿 메서드 패턴 적용")
    void templateMethodV1() {
        AbstractTemplate template = new SubClassLogic2();
        template.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();

    }

    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직3 수행");
            }
        };
        log.info("클래스 이름1={}", template1.getClass()); // 익명클래스이므로 이름이 없고 $1 등으로 정해짐
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직4 수행");
            }
        };
        log.info("클래스 이름2={}", template2.getClass());
        template2.execute();
    }
}
