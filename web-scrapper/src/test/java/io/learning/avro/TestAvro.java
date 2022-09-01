package io.learning.avro;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.learning.webscrapper.service.AvroHttpRequestManagerService;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class TestAvro {

    @Test
    public void test_AvroSchema_Generation() {
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
        log.info("Avro Schema:\n{}", generatedAvroSchema);
    }

    @Test
    public void test_avroSchema_Fetch() throws IOException {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService();

        Schema schema = service.schemaFromString();
        assertNotNull(schema);
        log.info("Schema from string {}", schema.toString());

        schema = service.schemaFromFile();
        assertNotNull(schema);
        log.info("Schema from string {}", schema.toString());
    }

    @Test
    public void test_DynamicAvroObject_generation_from_schema() {
        AvroHttpRequestManagerService service = new AvroHttpRequestManagerService();
        Schema schema = service.schemaFromString();

        GenericRecord avroHttpReq = new GenericData.Record(schema);
        avroHttpReq.put("requestTime", System.currentTimeMillis());

        Schema clientIdentifierSchema = schema.getField("clientIdentifier").schema();
        GenericRecord clientIdentifier = new GenericData.Record(clientIdentifierSchema);
        clientIdentifier.put("hostname", "localhost");
        clientIdentifier.put("ipAddress", "127.0.0.1");
        avroHttpReq.put("clientIdentifier", clientIdentifier);

        Schema activeEnumSchema = schema.getField("active").schema();
        GenericData.EnumSymbol activeYES = new GenericData.EnumSymbol(activeEnumSchema, "YES");
        avroHttpReq.put("active", activeYES);

        Schema employeeNamesSchema = schema.getField("employeeNames").schema();
        GenericData.Array<String> employeeNames = new GenericData.Array<>(employeeNamesSchema, Arrays.asList("Abc", "Xyz"));
        avroHttpReq.put("employeeNames", employeeNames);

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);

        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, stream);
            datumWriter.write(avroHttpReq, jsonEncoder);
            jsonEncoder.flush();


            log.info("Json format data :\n{}", stream.toString());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            datumWriter.write(avroHttpReq, encoder);
            encoder.flush();
            data = out.toByteArray();
            log.info("Byte format data :\n{}", data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(data.length > 0);
    }
}
