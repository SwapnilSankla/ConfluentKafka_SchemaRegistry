package com.demo.swapnil.helper.confluent;

import com.demo.swapnil.guice.GuiceMapper;
import com.demo.swapnil.helper.configuration.Configuration;
import com.demo.swapnil.producer.model.Name;
import com.demo.swapnil.producer.model.Student;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {
  @Test
  public void integration_consumerReceivesRecord_sentByProducer() throws UnknownHostException, JsonMappingException,
      InterruptedException {
    Name name = new Name("Swapnil", "Prakash", "Sankla");
    Student student = new Student(1, name, Optional.of(50), Optional.of("C"));

    Injector injector = Guice.createInjector(new GuiceMapper());
    ProducerHelper producerHelper = new ProducerHelper(injector.getInstance(Configuration.class));

    Future<RecordMetadata> send = producerHelper.send(student, (metadata, exception) -> {
      assertTrue(metadata.hasOffset());
      assertNull(exception);
    });

    while(!send.isDone()) {
      Thread.sleep(1000);
    }

    ConsumerHelper consumerHelper = new ConsumerHelper(injector.getInstance(Configuration.class));
    List<com.demo.swapnil.consumer.model.Student> students = consumerHelper.receive();
    Assert.assertThat(students.size(), is(1));
    Assert.assertThat(students.get(0).getId(),is(1));
    Assert.assertThat(students.get(0).getName().getFirstName(),is("Swapnil"));
    Assert.assertThat(students.get(0).getName().getMiddleName(),is("Prakash"));
    Assert.assertThat(students.get(0).getName().getLastName(),is("Sankla"));
    Assert.assertThat(students.get(0).getScore().get(),is(50));
    Assert.assertThat(students.get(0).getGrade().get(),is("C"));
  }
}
