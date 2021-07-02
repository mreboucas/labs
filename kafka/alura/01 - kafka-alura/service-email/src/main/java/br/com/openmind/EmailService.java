package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class EmailService {

    public static void main(String[] args) throws IOException {
        var emailService = new EmailService();
        try (var service = new KafkaService<>(EmailService.class.getSimpleName(),
                EnumTopico.ECOMMERCE_SEND_EMAIL,
                emailService::parse,
                String.class,
                Map.of()))
        {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("=========================================");
        System.out.println("Sending Email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //Consumer necessita dessa informação para receber todas as mensagens
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());

//        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,);
//        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,);

        return properties;
    }
}
