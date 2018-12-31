package com.demo.swapnil.helper.avro;

import com.fasterxml.jackson.databind.JsonMappingException;

public interface ObjectToAvroSchemaMapper {
  String map(final Object object) throws JsonMappingException;
}
