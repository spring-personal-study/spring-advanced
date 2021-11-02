package hello.advanced.app.proxy.pureproxy.concreteproxy;

import hello.advanced.app.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.advanced.app.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.advanced.app.proxy.pureproxy.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {

    @Test
    public void noProxy() {
        ConcreteLogic logic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(logic);
        client.execute();
    }

    @Test
    public void proxy1() {
        ConcreteLogic logic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(logic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();
    }
}
