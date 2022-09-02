package io.learning.webscrapper.repository;

import io.learning.webscrapper.Util;
import lombok.extern.log4j.Log4j2;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.stereotype.Repository;
import org.springframework.util.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Log4j2
@Repository
public class AvroRocksDBRepository implements GenericKVRepository<Long>{

    private RocksDB rocksDB;

    public AvroRocksDBRepository(String dbname) {
        RocksDB.loadLibrary();
        File baseDir = new File(System.getProperty("java.io.tmpdir"), dbname);

        final Options options = new Options();
        options.setCreateIfMissing(true);

        try {
            Files.createDirectories(baseDir.getParentFile().toPath());
            Files.createDirectories(baseDir.getAbsoluteFile().toPath());
            rocksDB = RocksDB.open(options, baseDir.getAbsolutePath());
        } catch (IOException | RocksDBException e) {
            log.error("{}\n", e.getMessage());
        }
    }

    @Override
    public boolean save(Long key, byte[] value) {
        log.info("saving value for key '{}'", key);
        try {
            rocksDB.put(Util.bytes(key), value);
        } catch (RocksDBException e) {
            log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Optional<Byte[]> find(Long key) {
        log.info("fetching value for key '{}'", key);
        Byte[] value = null;
        try {
            byte[] bytes = rocksDB.get(Util.bytes(key));

            if (null != bytes) {
                int i=0;
                value = new Byte[bytes.length];
                for(byte data : bytes) {
                    value[i++] = data;
                }
            }
        } catch (RocksDBException e) {
            log.error(
                    "Error retrieving the entry with key: {}, cause: {}, message: {}",
                    key,
                    e.getCause(),
                    e.getMessage()
            );
        }

        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean delete(Long key) {
        log.info("deleting key '{}'", key);
        try {
            rocksDB.delete(Util.bytes(key));
        } catch (RocksDBException e) {
            log.error("Error deleting entry, cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }

        return true;
    }
}
