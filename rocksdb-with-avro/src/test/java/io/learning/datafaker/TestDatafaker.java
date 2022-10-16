package io.learning.datafaker;

import lombok.extern.log4j.Log4j2;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.RandomService;
import org.junit.jupiter.api.Test;

import java.util.Locale;

@Log4j2
public class TestDatafaker {
    @Test
    void test_letterify() {
        FakeValuesService fvs = new FakeValuesService(Locale.US, new RandomService());
    }
}
