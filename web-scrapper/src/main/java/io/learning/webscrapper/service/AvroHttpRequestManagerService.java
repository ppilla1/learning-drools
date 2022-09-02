package io.learning.webscrapper.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.learning.webscrapper.Util;
import io.learning.webscrapper.domain.AvroHttpRequest;
import io.learning.webscrapper.repository.GenericKVRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class AvroHttpRequestManagerService {

    private final String AVRO_SCHEMA;
    private final GenericKVRepository<Long> repository;

    public AvroHttpRequestManagerService(GenericKVRepository<Long> repository) {
        AVRO_SCHEMA = buildSchema();
        this.repository = repository;
    }

    public Schema schemaFromString() {
        return new Schema.Parser().parse(AVRO_SCHEMA);
    }

    public Schema schemaFromFile() throws IOException {
        return new Schema.Parser().parse(this.getClass().getResourceAsStream("/AvroHttpRequest.avsc"));
    }

    public long save(AvroHttpRequest request) throws IOException {
        boolean status = repository.save(request.getRequestTime(), serialize(request));
        return status ? request.getRequestTime() : -1l;
    }

    public boolean delete(long id) {
        return repository.delete(id);
    }

    public Optional<AvroHttpRequest> find(long id) throws IOException {
        byte[] resp = repository.find(id)
                        .map(bytes -> Util.bytes(bytes))
                        .orElse(new byte[0]);

        AvroHttpRequest avroHttpRequest = deserialize(resp);
        return null != avroHttpRequest ? Optional.of(avroHttpRequest) : Optional.empty();
    }

    public byte[] serialize(AvroHttpRequest request) throws IOException {
        byte[] serializedData = new byte[0];

        Schema schema = schemaFromString();

        GenericRecord avroHttpReq = new GenericData.Record(schema);
        avroHttpReq.put("requestTime", request.getRequestTime());

        Schema clientIdentifierSchema = schema.getField("clientIdentifier").schema();
        GenericRecord clientIdentifier = new GenericData.Record(clientIdentifierSchema);
        clientIdentifier.put("hostname", request.getClientIdentifier().getHostname());
        clientIdentifier.put("ipAddress", request.getClientIdentifier().getIpAddress());
        avroHttpReq.put("clientIdentifier", clientIdentifier);

        Schema activeEnumSchema = schema.getField("active").schema();
        GenericData.EnumSymbol active = new GenericData.EnumSymbol(activeEnumSchema, request.getActive().name());
        avroHttpReq.put("active", active);

        Schema employeeNamesSchema = schema.getField("employeeNames").schema();
        GenericData.Array<String> employeeNames = new GenericData.Array<>(employeeNamesSchema, request.getEmployeeNames());
        avroHttpReq.put("employeeNames", employeeNames);

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
        datumWriter.write(avroHttpReq, encoder);
        encoder.flush();
        serializedData = stream.toByteArray();

        return serializedData;
    }

    public AvroHttpRequest deserialize(byte[] requestOut) throws IOException {
        AvroHttpRequest avroHttpReq = null;

        if (requestOut.length > 0) {
            Schema schema = schemaFromString();
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(requestOut, null);
            GenericRecord avroHttpReqOut = new GenericData.Record(schema);
            datumReader.read(avroHttpReqOut, decoder);

            ObjectMapper mapper = new ObjectMapper();
            avroHttpReq = mapper.readValue(avroHttpReqOut.toString(), AvroHttpRequest.class);
        }

        return avroHttpReq;
    }

    public Map<String, Object> genericDeserialize(byte[] requestOut) throws IOException {
        AvroHttpRequest avroHttpReq = null;

        Schema schema = schemaFromString();
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(requestOut, null);
        GenericRecord avroHttpReqOut = new GenericData.Record(schema);
        datumReader.read(avroHttpReqOut, decoder);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> avroMap = mapper.readValue(avroHttpReqOut.toString(), new TypeReference<HashMap<String, Object>>() {});

        return avroMap;
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
