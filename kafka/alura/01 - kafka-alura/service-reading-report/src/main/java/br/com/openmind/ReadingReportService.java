package br.com.openmind;

import br.com.openmind.consumer.ConsumerService;
import br.com.openmind.consumer.KafkaService;
import br.com.openmind.consumer.ServiceRunner;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Intelijj: Cria nova instância do Reading report service : [LSE] combobo > Edit configurations > cria uma nova instância com novo nome
 * Essa edição servirá para se conectar a mais de uma partição.
 */
public class ReadingReportService implements ConsumerService<User> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        new ServiceRunner<>(ReadingReportService::new).start(5);
    }
    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("=========================================");
        System.out.println("Processing report for: " + record.value());
        var message = record.value();
        var user = message.getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created " + target.getAbsolutePath());

    }

    @Override
    public EnumTopico getTopico() {
        return EnumTopico.ECOMMERCE_USER_GENERATE_READING_REPORT;
    }

    @Override
    public String getConsumerGroup() {
        return getClass().getSimpleName();
    }

//    private boolean isFraud(Order order) {
//        return order.getAmount().compareTo(new BigDecimal(5000)) > 0;
//    }
//
//    private static Properties properties() {
//        var properties = new Properties();
//        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
//        //Consumer necessita dessa informação do grupo caso queria receber todas as mensagens
//        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ReadingReportService.class.getSimpleName());
//        //Definindo um nome para o CLIENT-ID (ver na linha de comando)
//        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, ReadingReportService.class.getSimpleName() + "-" + UUID.randomUUID());
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
