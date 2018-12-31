package com.demo.swapnil.helper.confluent;

import com.demo.swapnil.guice.GuiceMapper;
import com.demo.swapnil.producer.model.Name;
import com.demo.swapnil.producer.model.Student;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

@Slf4j
public class SchemaRegistryHelperTest {
  @Test
  public void post_returnsSchemaVersionCreationResponse_forGivenObject() throws IOException {
    Name name = new Name("Swapnil", "Prakash", "Sankla");
    Student student = new Student(1, name, Optional.of(50), Optional.of("C"));

    Injector injector = Guice.createInjector(new GuiceMapper());
    SchemaRegistryHelper schemaRegistryHelper = injector.getInstance(SchemaRegistryHelper
        .class);

    String response = schemaRegistryHelper.post(student);
    log.info(response);

    Assert.assertThat(response, is("{\"id\":5}"));
  }
}