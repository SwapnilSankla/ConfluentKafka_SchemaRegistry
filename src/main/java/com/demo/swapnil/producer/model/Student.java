package com.demo.swapnil.producer.model;

import lombok.Value;
import org.apache.avro.reflect.AvroDefault;

import java.util.Optional;

@Value
public class Student {
  private final int id;
  private final Name name;
  @AvroDefault("null")
  private final Optional<Integer> score;
  @AvroDefault("null")
  private final Optional<String> grade;

  public Integer getScore() {
    return score.get();
  }

  public String getGrade() {
    return grade.get();
  }
}

