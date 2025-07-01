package com.mycompany.kafkademo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class StudentConsumer {

    private final static String TOPIC = "java";
    private final static String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    private final static String GROUP_ID = "student-consumer-group";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(TOPIC));
        Gson gson = new Gson();

        System.out.println("Consumer başlatıldı, mesajlar bekleniyor...");

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                String json = record.value();
                try {
                    Student student = gson.fromJson(json, Student.class);
                    System.out.println("Gelen öğrenci verisi: " + student);
                } catch (JsonSyntaxException e) {
                    System.out.println("Geçersiz JSON mesajı alındı: " + json);
                }
            }
        }
    }
}
