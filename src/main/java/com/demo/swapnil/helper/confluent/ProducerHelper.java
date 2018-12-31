package com.demo.swapnil.helper.confluent;

import com.demo.swapnil.helper.avro.ObjectToAvroSchemaMapperImpl;
import com.demo.swapnil.helper.configuration.Configuration;
import com.demo.swapnil.producer.model.Student;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.inject.Inject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class ProducerHelper {

  private final String topicName;
  private final KafkaProducer<String, GenericData.Record> kafkaProducer;

  @Inject
  public ProducerHelper(Configuration configuration) {
    topicName = configuration.getProp("topic.name");
    kafkaProducer = configureKafkaProducer(configuration);
  }

  public Future<RecordMetadata> send(Student userRecord, Callback callback) throws JsonMappingException {
    final GenericData.Record nameRecord = createNameRecord(userRecord);
    final GenericData.Record studentRecord = createStudentRecord(userRecord, nameRecord);
    final ProducerRecord<String, GenericData.Record> producerRecord = new ProducerRecord<>(topicName, studentRecord);
    final Future<RecordMetadata> future = kafkaProducer.send(producerRecord, callback);
    kafkaProducer.flush();
    return future;
  }

  private KafkaProducer<String, GenericData.Record> configureKafkaProducer(Configuration configuration) {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, configuration.getProp("bootstrap.server.url"));
    props.put(CLIENT_ID_CONFIG, configuration.getProp("producer.client.id.config"));
    props.put(KEY_SERIALIZER_CLASS_CONFIG, configuration.getProp("producer.key.serializer.class.config"));
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, configuration.getProp("producer.value.serializer.class.config"));
    props.put(SCHEMA_REGISTRY_URL_CONFIG, configuration.getProp("schema.registry.url"));
    return new KafkaProducer<>(props);
  }

  private GenericData.Record createStudentRecord(Student userRecord,
                                                 GenericData.Record nameRecord) throws JsonMappingException {
    final Schema studentSchema = getStudentSchema(userRecord);
    final GenericData.Record studentRecord = new GenericData.Record(studentSchema);
    studentRecord.put("id", userRecord.getId());
    studentRecord.put("name", nameRecord);
    studentRecord.put("score", userRecord.getScore());
    studentRecord.put("grade", userRecord.getGrade());
    return studentRecord;
  }

  private GenericData.Record createNameRecord(Student userRecord) throws JsonMappingException {
    final Schema nameSchema = getNameSchema(userRecord);
    final GenericData.Record nameRecord = new GenericData.Record(nameSchema);
    nameRecord.put("firstName", userRecord.getName().getFirstName());
    nameRecord.put("lastName", userRecord.getName().getLastName());
    nameRecord.put("middleName", userRecord.getName().getMiddleName());
    return nameRecord;
  }

  private Schema getNameSchema(Student userRecord) throws JsonMappingException {
    final String nameSchemaString = new ObjectToAvroSchemaMapperImpl().map(userRecord.getName());
    return new Schema.Parser().parse(nameSchemaString);
  }

  private Schema getStudentSchema(Student userRecord) throws JsonMappingException {
    final String studentSchemaString = new ObjectToAvroSchemaMapperImpl().map(userRecord);
    return new Schema.Parser().parse(studentSchemaString);
  }
}
