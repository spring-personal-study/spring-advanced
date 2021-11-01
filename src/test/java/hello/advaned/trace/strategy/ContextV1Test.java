package hello.advaned.trace.strategy;

import hello.advaned.trace.strategy.code.strategy.ContextV1;
import hello.advaned.trace.strategy.code.strategy.Strategy;
import hello.advaned.trace.strategy.code.strategy.StrategyLogic1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
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
    @DisplayName("전략 패턴 적용")
    void strategyV1() {
        Strategy strategy = new StrategyLogic1();
        ContextV1 contextV1 = new ContextV1(strategy);
        contextV1.execute();
    }

    @Test
    void strategyV2() {
        Strategy strategy = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        ContextV1 context = new ContextV1(strategy);
        log.info("strategyLogic1={}", strategy.getClass());
        context.execute();
    }

    @Test
    void strategyV3() {
        Strategy strategy = () -> log.info("비즈니스 로직1 실행");
        ContextV1 context = new ContextV1(strategy);
        log.info("strategyLogic1={}", strategy.getClass());
        context.execute();
    }

    @Test
    void strategyV4() {
        ContextV1 context = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        context.execute();
    }

}
