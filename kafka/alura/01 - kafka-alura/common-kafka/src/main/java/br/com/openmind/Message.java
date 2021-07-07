package br.com.openmind;

public class Message<T> {

    private CorrelationId id;
    private final T payload;

    public Message(CorrelationId correlationId, T payload) {
        this.id = correlationId;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "correlationId=" + id +
                ", payload=" + payload +
                '}';
    }

    public T getPayload() {
        return payload;
    }

    public CorrelationId getId() {
        return id;
    }
}