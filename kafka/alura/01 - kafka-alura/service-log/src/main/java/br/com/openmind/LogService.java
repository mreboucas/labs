package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Classe atua como consumidor, olhando para todos os tópicos que recebem as msgs pelo produtor: NewOrderMain
 */
public class LogService {

    public static void main(String[] args) throws InterruptedException, IOException {
        var logService = new LogService();
        try (var service = new KafkaService<>(LogService.class.getSimpleName(),
                Pattern.compile(EnumTopico.ALL_TOPICS.getTopico()),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
                )) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Email> record) {
        System.out.println("=========================================");
        System.out.println("LOG - Tópico: " + record.topic());
        System.out.println();
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }
}
