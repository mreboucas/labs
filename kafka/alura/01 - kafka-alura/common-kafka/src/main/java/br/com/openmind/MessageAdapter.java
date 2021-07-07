package br.com.openmind;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer {
    @Override
    public JsonElement serialize(Message message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", message.getPayload().getClass().getName());
        jsonObject.add("payload", context.serialize(message.getPayload()));
        jsonObject.add("correlationId", context.serialize(message.getId()));
        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = jsonElement.getAsJsonObject();
        var payloadType = obj.get("type").getAsString();
        var correlationId = (CorrelationId) context.deserialize(obj.get("correlationId"), CorrelationId.class);
        try {
            //Maybe you want to use a "accept list"
            var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));

            return new Message(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
