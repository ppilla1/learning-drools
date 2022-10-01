package io.learning.rocksdb;

import io.learning.webscrapper.repository.KVRepository;
import io.learning.webscrapper.repository.RocksDBRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class TestRocksDb {

    private KVRepository<String, Object> kvRepository;

    @BeforeEach
    public void init() {
        kvRepository = new RocksDBRepository("test-rocksdb");
    }

    @AfterEach
    public void tear() {
        kvRepository = null;
    }

    @Test
    public void test_save_find_delete() {
        log.info("Db file location {}", System.getProperty("java.io.tmpdir"));
        Map<String, String> data = new HashMap<>();
        data.put("bar", "baz");
        String key = "foo";

        log.info("Saving data with key {} : {}", key, data);
        assertTrue(kvRepository.save(key, data));

        data = kvRepository.find(key)
                .map(val -> (Map<String, String>) val)
                .orElse(new HashMap<>());

        log.info("Retrived data for key {} : {}", key, data);
        assertTrue(!data.isEmpty());

        log.info("Removing data for key {}", key);
        assertTrue(kvRepository.delete(key));
    }

}
