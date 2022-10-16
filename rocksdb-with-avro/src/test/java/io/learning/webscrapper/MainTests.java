package io.learning.webscrapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MainTests {

    @Test
    void secondOfTheDay() {

        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth(),
                0, 0, 0, 0);

        ZonedDateTime zdt = localDateTime.atZone(ZoneId.of("UTC+05:30"));
        long deltaMs = Instant.now().toEpochMilli() - zdt.toInstant().toEpochMilli();

        long delta = TimeUnit.MILLISECONDS.toSeconds(deltaMs);


        final String userHome = System.getProperty("java.io.tmpdir");

        log.info("\nCurrent temp directory :{}\nSeconds passed midnight: {}", userHome, delta);

        assertTrue(true);
    }

}
