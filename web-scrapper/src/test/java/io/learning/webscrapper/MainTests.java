package io.learning.webscrapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MainTests {

    @Test
    void contextLoads() {
        final String userHome = System.getProperty("user.home");
        log.info("Hello {}, World SpringTest", userHome);
        assertTrue(true);
    }

}
