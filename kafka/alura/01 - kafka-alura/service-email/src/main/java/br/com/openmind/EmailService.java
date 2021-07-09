package br.com.openmind;

import br.com.openmind.consumer.ConsumerService;
import br.com.openmind.consumer.ServiceRunner;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @PATTERN - FACTORY
 */
public class EmailService implements ConsumerService<String> {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        new ServiceRunner<>(EmailService::new).start(5);
    }
    public String getConsumerGroup() {
        return getClass().getSimpleName();
    }
    public EnumTopico getTopico() {
        return EnumTopico.ECOMMERCE_SEND_EMAIL;
    }

    public void parse(ConsumerRecord<String, Message<String>> record) {
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
