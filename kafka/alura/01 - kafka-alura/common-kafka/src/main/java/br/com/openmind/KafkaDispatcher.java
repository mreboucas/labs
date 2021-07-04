package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, T> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        //Essa propriedade vai repercutir no send.get(). Ele vai esperar por todas as réplicas do líder
        //serem atualizadas (ISR) dando confiabilidade (realability) a replicação das informações.
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        return properties;
    }

    public void send(EnumTopico enumTopico, String key, T value) throws ExecutionException, InterruptedException {

        var record = new ProducerRecord<>(enumTopico.getTopico(), key, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando: " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        //get torna processo síncrono.
        RecordMetadata recordMetadata = producer.send(record, callback).get();
        System.out.println(recordMetadata.offset());
//
        //Assíncrono
//        Future<RecordMetadata> kafkaFuture = producer.send(record, callback);
//        while(!kafkaFuture.isDone()) {
//            System.out.println(kafkaFuture.isDone());
//        }
//        System.out.println(kafkaFuture.isDone());
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
