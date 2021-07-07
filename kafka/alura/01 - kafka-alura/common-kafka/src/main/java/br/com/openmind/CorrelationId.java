package br.com.openmind;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId(String name) {
        this.id = name + "(" + UUID.randomUUID().toString() + ")";
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(id + "-" + title);
    }
}