package com.jmp.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserMicroserviceApplicationTests {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void mainBeanIsPresent() {
        assertThat(context.getBean(UserMicroserviceApplication.class)).isNotNull();
    }
}

class UserMicroserviceMainMethodTest {
    @Test
    void testMain() {
        assertDoesNotThrow(() -> UserMicroserviceApplication.main(new String[]{}));
    }
}