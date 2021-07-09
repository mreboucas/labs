package br.com.openmind.consumer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProdiver<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;

    public ServiceProdiver(ServiceFactory<T> factory) {
        this.factory = factory;
    }

    public Void call() throws Exception {
        //Method reference -> create
        var myService = factory.create();
        try (var service = new KafkaService(myService.getConsumerGroup(),
                myService.getTopico(),
                myService::parse,
                Map.of())) {
            service.run();
            //Coloquei essa exception para ver os erros do properties do kafka: read_committed que eu havia colocado;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
