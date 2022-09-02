package io.learning.webscrapper.repository;

import java.util.Optional;

public interface GenericKVRepository <K>{
    boolean save(K key, byte[] value);
    Optional<Byte[]> find(K key);
    boolean delete(K key);
}
