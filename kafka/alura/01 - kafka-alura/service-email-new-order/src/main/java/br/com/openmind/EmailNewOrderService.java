package br.com.openmind;

import br.com.openmind.consumer.ConsumerService;
import br.com.openmind.consumer.KafkaService;
import br.com.openmind.consumer.ServiceRunner;
import br.com.openmind.dispatcher.KafkaDispatcher;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Intelijj: Cria nova instância do fraude detector service : [LSE] combobo > Edit configurations > cria uma nova instância com novo nome
 * Essa edição servirá para se conectar a mais de uma partição.
 */
public class EmailNewOrderService implements ConsumerService<Order> {

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        new ServiceRunner<>(EmailNewOrderService::new).start(1);
    }

    private final KafkaDispatcher<String> emailDispacther = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("=========================================");
        System.out.println("Processing new order, preparing email");
        System.out.println(record.key());
        System.out.println(record.value());
        var order = record.value().getPayload();
        var emailCode = "Thank you for order!! We are processing your order!";
        Message<Order> message = record.value();
        System.out.println(message);
        var id = message.getId().continueWith(getClass().getSimpleName());
        emailDispacther.send(EnumTopico.ECOMMERCE_SEND_EMAIL, order.getEmail(), id,emailCode);
    }

    @Override
    public EnumTopico getTopico() {
        return EnumTopico.ECOMMERCE_NEW_ORDER;
    }

    @Override
    public String getConsumerGroup() {
        return getClass().getSimpleName();
    }
}
