package hello.advanced.app.proxy.pureproxy.proxy;

import hello.advanced.app.proxy.pureproxy.proxy.code.CacheProxy;
import hello.advanced.app.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.advanced.app.proxy.pureproxy.proxy.code.RealSubject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient proxyPatternClient = new ProxyPatternClient(realSubject);
        proxyPatternClient.execute();
        proxyPatternClient.execute();
        proxyPatternClient.execute();
    }
    
    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        log.info("최초 호출");
        client.execute();
        log.info("두번째 호출");
        client.execute();
        log.info("세번째 호출");
        client.execute();
    }
}
