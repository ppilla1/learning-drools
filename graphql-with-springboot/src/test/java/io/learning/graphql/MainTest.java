package io.learning.graphql;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class MainTest {

    @Test
    public void test() {
        log.info("Hello, GraphQl!!");
    }
}
