package com.demo.swapnil.helper.confluent;

import com.demo.swapnil.helper.http.HttpClient;
import com.demo.swapnil.helper.avro.ObjectToAvroSchemaMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchemaRegistryHelper {

  private static final String NEW_SCHEMA_URL_TEMPLATE = "http://localhost:8081/subjects/{ClassName}/versions";
  private static final String CONTENT_TYPE = "application/vnd.schemaregistry.v1+json";
  private final HttpClient httpClient;
  private final ObjectToAvroSchemaMapper objectToAvroSchemaMapper;

  @Inject
  public SchemaRegistryHelper(HttpClient httpClient,
                              ObjectToAvroSchemaMapper objectToAvroSchemaMapper) {
    this.httpClient = httpClient;
    this.objectToAvroSchemaMapper = objectToAvroSchemaMapper;
  }

  public String post(final Object object) throws IOException {
    final String schema = createSchema(object);
    final String url = NEW_SCHEMA_URL_TEMPLATE.replace("{ClassName}",
        object.getClass().getSimpleName());

    return httpClient.post(url, CONTENT_TYPE, schema);
  }

  private String createSchema(Object object) throws JsonProcessingException {
    final Map<String,String> map = new HashMap<>();
    map.put("schema", objectToAvroSchemaMapper.map(object));
    return new ObjectMapper().writeValueAsString(map);
  }
}
