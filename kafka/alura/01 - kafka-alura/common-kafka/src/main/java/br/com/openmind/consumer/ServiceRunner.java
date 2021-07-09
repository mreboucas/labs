package br.com.openmind.consumer;

import java.util.concurrent.Executors;

public class ServiceRunner<T> {
    private final ServiceProdiver<Object> provider;

    public ServiceRunner(ServiceFactory<T> factory) {
        this.provider = new ServiceProdiver(factory);
    }

    public void start(int threadCount) {
        var poll = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i <= threadCount; i++) {
            poll.submit(this.provider);
        }
    }
}
