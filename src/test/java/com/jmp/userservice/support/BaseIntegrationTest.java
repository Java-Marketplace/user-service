package com.jmp.userservice.support;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import com.jmp.userservice.support.config.UtilConfig;
import com.jmp.userservice.support.initializer.KeycloakInitializer;
import com.jmp.userservice.support.initializer.PostgresInitializer;
import com.jmp.userservice.support.util.AuthUtil;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;


@Tag("integration")
@RunWith(SpringRunner.class)
@Testcontainers
@ContextConfiguration(initializers = {
        PostgresInitializer.class, KeycloakInitializer.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UtilConfig.class)
@DBRider
@DBUnit(caseSensitiveTableNames = true, schema = "public")
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected AuthUtil authUtil;

    @Autowired
    protected WebTestClient webClient;
}
