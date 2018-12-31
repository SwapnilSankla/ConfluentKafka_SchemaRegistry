package com.demo.swapnil.helper.confluent;

import avro.shaded.com.google.common.base.Optional;
import com.demo.swapnil.consumer.model.Name;
import com.demo.swapnil.consumer.model.Student;
import com.demo.swapnil.helper.configuration.Configuration;
import com.google.inject.Inject;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ConsumerHelper {

  private final String topicName;
  private final KafkaConsumer<String, GenericData.Record> kafkaConsumer;

  @Inject
  public ConsumerHelper(Configuration configuration) throws UnknownHostException {
    topicName = configuration.getProp("topic.name");
    kafkaConsumer = configureKafkaConsumer(configuration);
    kafkaConsumer.subscribe(Collections.singletonList(topicName));
  }

  public List<Student> receive() {
    List<Student> students = new ArrayList<>();
    for (int i=0; i<10; i++) {
      ConsumerRecords<String, GenericData.Record> studentRecords = kafkaConsumer.poll(10);
      studentRecords.forEach(record -> students.add(mapConsumerRecordToStudent(record)));
      if(studentRecords.count() > 0) {
        kafkaConsumer.commitSync();
        break;
      }
    }
    return students;
  }

  private KafkaConsumer<String, GenericData.Record> configureKafkaConsumer(Configuration configuration) throws UnknownHostException {
    Properties props = new Properties();
    props.put("client.id", InetAddress.getLocalHost().getHostName());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        configuration.getProp("consumer.key.deserializer.class.config"));
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, configuration.getProp("consumer.value.deserializer.class.config"));
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getProp("bootstrap.server.url"));
    props.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getProp("consumer.group.id.config"));
    props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, configuration.getProp("schema.registry.url"));

    return new KafkaConsumer<>(props);
  }

  private Student mapConsumerRecordToStudent(ConsumerRecord<String, GenericData.Record> record) {
    int id = (Integer) record.value().get("id");
    GenericData.Record name = (GenericData.Record)record.value().get("name");
    String firstName = name.get("firstName").toString();
    String middleName = name.get("middleName").toString();
    String lastName = name.get("lastName").toString();
    Integer score = (Integer) record.value().get("score");
    String grade = record.value().get("grade").toString();
    return new Student(id,
        new Name(firstName, middleName, lastName),
        Optional.fromNullable(score),
        Optional.fromNullable(grade));
  }
}
