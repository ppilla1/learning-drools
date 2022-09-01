package io.learning.webscrapper.service;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class AvroHttpRequestManagerService {

    private final String AVRO_SCHEMA;

    public AvroHttpRequestManagerService() {
        AVRO_SCHEMA = buildSchema();
    }

    public Schema schemaFromString() {
        return new Schema.Parser().parse(AVRO_SCHEMA);
    }

    public Schema schemaFromFile() throws IOException {
        return new Schema.Parser().parse(this.getClass().getResourceAsStream("/AvroHttpRequest.avsc"));
    }

    private String buildSchema() {
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

        return avroHttpRequest.toString();
    }
}
