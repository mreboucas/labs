package br.com.openmind;

import br.com.openmind.consumer.ConsumerService;
import br.com.openmind.consumer.KafkaService;
import br.com.openmind.consumer.ServiceRunner;
import br.com.openmind.database.LocalDataBase;
import br.com.openmind.dispatcher.KafkaDispatcher;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Intelijj: Cria nova instância do fraude detector service : [LSE] combobo > Edit configurations > cria uma nova instância com novo nome
 * Essa edição servirá para se conectar a mais de uma partição.
 */
public class FraudDetectorService implements ConsumerService<Order> {

    private final LocalDataBase database;

    private FraudDetectorService() throws SQLException {
        this.database = new LocalDataBase("frauds_database");
        this.database.createIfNotExists("create table Orders (" +
                "uuid varchar(200) primary key," +
                "is_fraud boolean)");
    }

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("=========================================");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var message = record.value();
        Order order = message.getPayload();
        if (wasProcessed(order)) {
            System.out.println("Order com id " +  order.getOrderId() +"  was already processed!");
            return;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isFraud(order)) {
            this.database.update("insert into Orders (uuid, is_fraud) values (?, true)", order.getOrderId());
            System.out.println("Order is a fraude!!! " + order.toString());
            orderDispatcher.send(EnumTopico.ECOMMERCE_ORDER_REJECTED, order.getEmail(),
                    message.getId().continueWith(getClass().getSimpleName())
                    , order);
        } else {
            System.out.println("Approved " + order.toString());
            this.database.update("insert into Orders (uuid, is_fraud) values (?, false)", order.getOrderId());
            orderDispatcher.send(EnumTopico.ECOMMERCE_ORDER_APPROVED, order.getEmail(), new CorrelationId(getClass().getSimpleName()), order);

        }

        System.out.println("Order processed");
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var results = this.database.query("select uuid from Orders where uuid = ?", order.getOrderId());
        return results.next();
    }

    @Override
    public EnumTopico getTopico() {
        return EnumTopico.ECOMMERCE_NEW_ORDER;
    }

    @Override
    public String getConsumerGroup() {
        return getClass().getSimpleName();
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal(5000)) > 0;
    }

    //Transferido para o Kafka service
//    private static Properties properties() {
//        var properties = new Properties();
//        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
//        //Consumer necessita dessa informação do grupo caso queria receber todas as mensagens
//        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,FraudDetectorService.class.getSimpleName());
//        //Definindo um nome para o CLIENT-ID (ver na linha de comando)
//        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID());
//        //Garantir que a cada mensagem lida, ela seja comitada e diminiu o problema de commit por conta do rebalanceamento quando se
//        //consome um número elevado de mensagens. Ex.: se deixar para comitar depois de processa um número elevado de msg: 50 por exemplo.
//        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
//
//
////        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,);
////        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,);
//
//        return properties;
//    }
}
