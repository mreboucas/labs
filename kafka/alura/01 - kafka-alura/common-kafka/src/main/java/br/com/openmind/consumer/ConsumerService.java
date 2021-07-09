package br.com.openmind.consumer;

import br.com.openmind.Message;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    public void parse(ConsumerRecord<String, Message<T>> record) throws Exception;

    public EnumTopico getTopico();

    public String getConsumerGroup();
}