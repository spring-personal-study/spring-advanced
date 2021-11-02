package hello.advanced.app.proxy.pureproxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ComponentCacheProxy implements Component {

    private final Component component;
    private String cachedData;

    @Override
    public String operation() {
        log.info("프록시 객체 호출");
        if (cachedData == null) { // 캐싱 프록시
            cachedData = component.operation();
        }
        return cachedData;
    }
}
