package com.demo.swapnil.producer;

import com.demo.swapnil.helper.configuration.ConfigurationImpl;
import com.demo.swapnil.helper.confluent.ProducerHelper;
import com.demo.swapnil.producer.model.Name;
import com.demo.swapnil.producer.model.Student;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

public class ProducerApp {

  public static void main(String[] args) throws IOException {
    final ConfigurationImpl configuration = new ConfigurationImpl();
    final ProducerHelper producerHelper = new ProducerHelper(configuration);

    IntStream.range(1, 101).forEach(i -> {
      Name name = new Name("Swapnil", "Prakash", "Sankla");
      Student student = new Student(i, name, Optional.of(50), Optional.of("B"));
      try {
        producerHelper.send(student, (metadata, exception) -> System.out.println(metadata.offset()));
      } catch (JsonMappingException e) {
        e.printStackTrace();
      }
    });
  }
}
