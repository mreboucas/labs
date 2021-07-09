package br.com.openmind;

import br.com.openmind.consumer.KafkaService;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Classe atua como consumidor, olhando para todos os tópicos que recebem as msgs pelo produtor: NewOrderMain
 */
public class LogService {

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        var logService = new LogService();
        try (var service = new KafkaService(LogService.class.getSimpleName(),
                //Se atentar, se o log service estiver rodando, ele não escutará novos tópicos/subjects que por
                //ventura possam vir a surgir. Tem que dar um stop/start no log service, ai ele pegará as informações
                //dos novos tópicos.
                Pattern.compile(EnumTopico.ALL_TOPICS.getTopico()),
                logService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
                )) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("=========================================");
        System.out.println("LOG - Tópico: " + record.topic());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }
}
