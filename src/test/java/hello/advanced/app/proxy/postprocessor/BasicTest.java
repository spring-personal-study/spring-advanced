package hello.advanced.app.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BasicTest {

    @Test
    @DisplayName("기본 설정했을 때")
    void basicConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);
        A beanA = applicationContext.getBean("beanA", A.class);
        beanA.helloA();

        // B beanB = applicationContext.getBean("beanB", B.class); // 빈으로 등록하지 않아서 에러 (B을 못 찾음)
        // beanB.helloB();
        assertThatThrownBy(() -> applicationContext.getBean("beanB", B.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class)
                .hasMessageContaining("No bean named 'beanB' available");
    }

    @Test
    @DisplayName("A가 아니라 B로 바꿔치기해 빈으로 등록하기")
    void registerB() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PostProcessorConfig.class);
        B beanA = applicationContext.getBean("beanA", B.class); // beanA 이름으로 A를 빈등록하려 했으나 실제로는 B가 스프링빈으로 등록 됨 (beanA에 B.class)
        beanA.helloB();

        assertThatThrownBy(() -> applicationContext.getBean("beanA", A.class))
                .isInstanceOf(BeanNotOfRequiredTypeException.class)
                .hasMessage("Bean named 'beanA' is expected to be of type " +
                        "'hello.advanced.app.proxy.postprocessor.BasicTest$A' " +
                        "but was actually of type 'hello.advanced.app.proxy.postprocessor.BasicTest$B'");
    }


    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        /*
            //B는 빈으로 등록하지 않는다. basicConfig() 테스트
            @Bean(name = "beanB")
            public B b() {
                return new B();
            }
        */
    }

    @Slf4j
    @Configuration
    static class PostProcessorConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor aToBPostProcessor() { // A를 B로 바꿔치기해 빈으로 등록하기.
            return new AToBPostProcessor();
        }
    }


    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }


    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={}, bean={}", beanName, bean);
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
}
