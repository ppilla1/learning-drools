package io.learning.webscrapper.repository;

import lombok.extern.log4j.Log4j2;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Log4j2
@Repository
public class RocksDBRepository implements KVRepository<String, Object>{
    private RocksDB rocksDB;

    public RocksDBRepository(@Value("${rocksdb.name.testdb}") String dbname) {
        RocksDB.loadLibrary();
        File baseDir = new File(System.getProperty("java.io.tmpdir"), dbname);

        final Options options = new Options();
        options.setCreateIfMissing(true);

        try {
            Files.createDirectories(baseDir.getParentFile().toPath());
            Files.createDirectories(baseDir.getAbsoluteFile().toPath());
            rocksDB = RocksDB.open(options, baseDir.getAbsolutePath());
        } catch (IOException|RocksDBException e) {
            log.error("{}\n", e.getMessage());
        }
    }

    @Override
    public boolean save(String key, Object value) {
        log.info("saving value '{}' with key '{}'", value, key);
        try {
            rocksDB.put(key.getBytes(), SerializationUtils.serialize(value));
        } catch (RocksDBException e) {
            log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Optional<Object> find(String key) {
        Object value = null;
        try {
            byte[] bytes = rocksDB.get(key.getBytes());
            if (bytes != null) value = SerializationUtils.deserialize(bytes);
        } catch (RocksDBException e) {
            log.error(
                    "Error retrieving the entry with key: {}, cause: {}, message: {}",
                    key,
                    e.getCause(),
                    e.getMessage()
            );
        }
        log.info("finding key '{}' returns '{}'", key, value);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean delete(String key) {
        log.info("deleting key '{}'", key);
        try {
            rocksDB.delete(key.getBytes());
        } catch (RocksDBException e) {
            log.error("Error deleting entry, cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }

        return true;
    }
}
