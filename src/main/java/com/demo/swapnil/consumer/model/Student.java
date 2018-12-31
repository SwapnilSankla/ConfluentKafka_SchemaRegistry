package com.demo.swapnil.consumer.model;

import avro.shaded.com.google.common.base.Optional;
import lombok.Value;

@Value
public class Student {
  private final int id;
  private final Name name;
  private final Optional<Integer> score;
  private final Optional<String> grade;
}

