package br.com.openmind.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws  Exception;
}
