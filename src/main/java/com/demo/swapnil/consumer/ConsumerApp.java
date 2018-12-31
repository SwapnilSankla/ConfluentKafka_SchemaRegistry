package com.demo.swapnil.consumer;

import com.demo.swapnil.helper.configuration.ConfigurationImpl;
import com.demo.swapnil.helper.confluent.ConsumerHelper;
import com.demo.swapnil.consumer.model.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConsumerApp {
  public static void main(String[] args)  {
    try {
      final ConsumerHelper consumerHelper = new ConsumerHelper(new ConfigurationImpl());
      while(true) {
        final List<Student> students = consumerHelper.receive();
        students.stream().forEach(student -> log.info(student.toString()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
