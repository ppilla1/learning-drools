package io.learning.webscrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;

public class Util {

    public static String prettyJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object jsonObj = mapper.readValue(json, Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
    }

    public static String prettyJson(Object json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public static byte[] bytes(long number) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(number);
        return buffer.array();
    }

    public static byte[] bytes(Byte[] bytes) {
        int i = 0;
        byte[] data = new byte[bytes.length];
        for(byte b : bytes) {
            data[i++] = b;
        }
        return data;
    }
}
