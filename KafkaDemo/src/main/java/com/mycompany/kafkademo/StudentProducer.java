package com.mycompany.kafkademo;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class StudentProducer {

    private final static String TOPIC = "java";
    private final static String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        Gson gson = new Gson();

        Student student = new Student(34, "İstanbul");

        // Nesneyi JSON'a çevir
        String studentJson = gson.toJson(student);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, Integer.toString(student.getId()), studentJson);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Mesaj gönderildi: " + record.value());
            } else {
                exception.printStackTrace();
            }
        });

        producer.flush();
        producer.close();
    }
}
