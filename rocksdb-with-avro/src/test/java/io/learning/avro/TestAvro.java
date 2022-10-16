package io.learning.avro;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.learning.webscrapper.Util;
import io.learning.webscrapper.domain.Active;
import io.learning.webscrapper.domain.AvroHttpRequest;
import io.learning.webscrapper.domain.ClientIdentifier;
import io.learning.webscrapper.repository.AvroRocksDBRepository;
import io.learning.webscrapper.service.AvroHttpRequestManagerService;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class TestAvro {

    @Test
    public void test_AvroSchema_Generation() throws JsonProcessingException {
        Schema clientIdentifier = SchemaBuilder.record("ClientIdentifier")
                                        .namespace("io.learning.webscraper.domain")
                                        .fields()
                                            .requiredString("hostname")
                                            .requiredString("ipAddress")
                                    .endRecord();

        Schema avroHttpRequest = SchemaBuilder.record("AvroHttpRequest")
                                        .namespace("io.learning.webscraper.domain")
                                        .fields()
                                            .requiredLong("requestTime")
                                        .name("clientIdentifier")
                                        .type(clientIdentifier)
                                        .noDefault()
                                        .name("employeeNames")
                                        .type()
                                        .array()
                                        .items()
                                        .stringType()
                                        .arrayDefault(new ArrayList<>())
                                        .name("active")
                                        .type()
                                        .enumeration("Active")
                                        .symbols("YES", "NO")
                                        .noDefault()
                                    .endRecord();

        String generatedAvroSchema = avroHttpRequest.toString();
        assertNotNull(generatedAvroSchema);
        log.info("Avro Schema:\n{}", Util.prettyJson(generatedAvroSchema));
    }

    @Test
    public void test_avroSchema_Fetch() throws IOException {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService(new AvroRocksDBRepository("avro-store"));

        Schema schema = service.schemaFromString();
        assertNotNull(schema);
        log.info("Schema from string {}", Util.prettyJson(schema.toString()));

        schema = service.schemaFromFile();
        assertNotNull(schema);
        log.info("Schema from string {}", Util.prettyJson(schema.toString()));
    }

    @Test
    public void test_avro_Serialize_and_DeSerialize_AvroHttpRequest() throws IOException {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService(new AvroRocksDBRepository("avro-store"));

        AvroHttpRequest request = new AvroHttpRequest();
        request.setRequestTime(System.nanoTime());
        ClientIdentifier clientIdentifier = new ClientIdentifier();
        clientIdentifier.setHostname("localhost");
        clientIdentifier.setIpAddress("127.0.0.1");
        request.setClientIdentifier(clientIdentifier);
        request.setEmployeeNames(Arrays.asList("Abc", "Xyz"));
        request.setActive(Active.YES);

        byte[] payload = service.serialize(request);
        assertNotNull(payload);
        assertTrue(payload.length > 0);
        log.info("[size: {} bytes] Avro binary serialized {}:\n{}", payload.length,request, payload);

        AvroHttpRequest requestDeserialize = service.deserialize(payload);
        log.info("[size: {} bytes] Avro deserialize {} from:\n{}", payload.length,requestDeserialize, payload);
        assertNotNull(requestDeserialize);
        assertTrue(requestDeserialize.equals(request));

        Map<String, Object> requestDeserializeGeneric = service.genericDeserialize(payload);
        log.info("[size: {} bytes] Avro generic deserialize {} from:\n{}", payload.length,requestDeserializeGeneric, payload);
        assertNotNull(requestDeserializeGeneric);
        assertTrue(!requestDeserializeGeneric.isEmpty());
    }

    //@Test
    public void test_save_find_delete_AvroHttpRequest_in_RocksDB() throws IOException {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService(new AvroRocksDBRepository("avro-store"));
        long id = System.nanoTime();

        AvroHttpRequest request = new AvroHttpRequest();
        request.setRequestTime(id);
        ClientIdentifier clientIdentifier = new ClientIdentifier();
        clientIdentifier.setHostname("localhost");
        clientIdentifier.setIpAddress("127.0.0.1");
        request.setClientIdentifier(clientIdentifier);
        request.setEmployeeNames(Arrays.asList("Abc", "Xyz"));
        request.setActive(Active.YES);

        long idResp = service.save(request);
        log.info("Avro object saved for id {}\n{}", id, request);
        assertTrue(id == idResp);

        Optional<AvroHttpRequest> resp = service.find(id);

        request = resp.orElse(null);
        log.info("Avro object fetched for id {}\n{}", id, request);
        assertTrue(null!= request);
        assertTrue(id == request.getRequestTime());

        boolean status = service.delete(id);
        assertTrue(status);
        log.info("Avro object deleted for id {}\n{}", id, request);
    }

    //@Test
    public void test_findAvroHttpRequest_in_RocksDB() throws IOException {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService(new AvroRocksDBRepository("avro-store"));

        long id = 1076885361456750l;
        Optional<AvroHttpRequest> resp = service.find(id);

        AvroHttpRequest request = resp.orElse(null);

        assertTrue(null!= request);
        assertTrue(id == request.getRequestTime());
        log.info("Avro object fetched for id {}\n{}", id, request);
    }

}
