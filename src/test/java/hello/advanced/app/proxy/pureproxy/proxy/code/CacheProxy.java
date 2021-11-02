package hello.advanced.app.proxy.pureproxy.proxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CacheProxy implements Subject {

    private final Subject target; // 프록시는 실제 객체에 대한 참조를 가지고 있어야 한다.
    private String cacheValue;

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) { // 캐싱 준비
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
