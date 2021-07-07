package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, Message<T>> consumer;
    private final ConsumerFunction<T> parse;

    KafkaService(String groupId, EnumTopico enumTopico, ConsumerFunction<T> parse, Map<String,String> properties) {
        this(parse, groupId, properties);
        consumer.subscribe(Collections.singletonList(enumTopico.getTopico()));
    }

    KafkaService(String groupId, Pattern topic, ConsumerFunction<T> parse, Map<String,String> properties) {
        this(parse, groupId, properties);
        consumer.subscribe(topic);
    }

    private KafkaService(ConsumerFunction<T> parse, String groupId, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(getProperties(groupId, properties));
    }

    void run() throws ExecutionException, InterruptedException {
        try(var deadLetter = new KafkaDispatcher<>()) {
            while (true) {
                /**
                 * A cada 100 ms o KafkaService fica enviando a comunicação ao broker do kafka
                 * avisando de que ele está vivo. A partir daí é que ele fica recebendo as mensagens (consumindo).
                 * COm isso ele consome e o servidor sabe que ele (consumidor) está vivo.
                 */
                var records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    System.out.println("Encontrei " + records.count() + " registros");
                    for (var record : records) {
                        try {
                            parse.consume(record);
                        } catch (Exception e) {
                            //so far, just logging  the exception for this message
                            //Depois ver uma forma de tratar, por retentativa por exemplo.
                            e.printStackTrace();
                            var message = record.value();
                            deadLetter.send(EnumTopico.ECOMMERCE_DEADLETTER, message.getId().toString(),
                                    message.getId().continueWith("DeadeLetter"),
                                    new GsonSerializer().serialize("", message));
                        }
                    }
                }
            }
        }
    }

    private Properties getProperties(String groupId, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        //Garantir que a cada mensagem lida, ela seja comitada e diminiu o problema de commit por conta do rebalanceamento quando se
        //consome um número elevado de mensagens. Ex.: se deixar para comitar depois de processa um número elevado de msg: 50 por exemplo.
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
