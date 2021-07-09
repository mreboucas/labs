package br.com.openmind.consumer;

import br.com.openmind.Message;
import br.com.openmind.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer implements Deserializer<Message> {

//  private final Gson gson = new GsonBuilder().create();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

    //Como está serealizando/deserealizando um objeto Message, esse bloco a seguir será desnecessário
//    public static final String TYPE_CONFIG = "br.com.openmind.type_config";
//    private Class<T> type;

//    @Override
//    public void configure(Map configs, boolean isKey) {
//        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
//        try {
//            this.type = (Class<T>) Class.forName(typeName);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("Type for deserialization does not exists in the classpath", e);
//        }
//    }

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), Message.class);
    }
}
