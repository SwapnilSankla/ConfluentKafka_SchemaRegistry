package com.demo.swapnil.helper.avro;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;

public class ObjectToAvroSchemaMapperImpl implements ObjectToAvroSchemaMapper {
  public String map(final Object object) throws JsonMappingException {
    final AvroMapper avroMapper = new AvroMapper(new AvroFactory());
    final AvroSchema avroSchema = avroMapper.schemaFor(object.getClass());
    return avroSchema.getAvroSchema().toString(true);
  }
}
