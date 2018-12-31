package com.demo.swapnil.guice;

import com.demo.swapnil.helper.configuration.Configuration;
import com.demo.swapnil.helper.configuration.ConfigurationImpl;
import com.demo.swapnil.helper.http.HttpClient;
import com.demo.swapnil.helper.http.HttpClientImpl;
import com.demo.swapnil.helper.avro.ObjectToAvroSchemaMapper;
import com.demo.swapnil.helper.avro.ObjectToAvroSchemaMapperImpl;
import com.google.inject.AbstractModule;

public class GuiceMapper extends AbstractModule {
  @Override
  protected void configure() {
    bind(HttpClient.class).to(HttpClientImpl.class);
    bind(ObjectToAvroSchemaMapper.class).to(ObjectToAvroSchemaMapperImpl.class);
    bind(Configuration.class).to(ConfigurationImpl.class);
  }
}
