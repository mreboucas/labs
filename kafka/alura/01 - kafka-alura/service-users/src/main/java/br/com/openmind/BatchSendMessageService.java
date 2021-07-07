package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;

    public BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            this.connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch(SQLException e) {
            //Be careful, the sql could be wrong, be really careful.
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {

        var batchService = new BatchSendMessageService();
        try (var service = new KafkaService<>(BatchSendMessageService.class.getSimpleName(),
                EnumTopico.ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS,
                batchService::parse,
                Map.of())) {
            service.run();
        }
    }
    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<String>> record) throws ExecutionException, InterruptedException, SQLException {

        System.out.println("=========================================");
        System.out.println("Processing new batch");
        var message = record.value();
        if (true)
            throw new RuntimeException("Deu um erro que forcei");
        System.out.println(record.value().toString());
        EnumTopico enumTopico = EnumTopico.findByName(message.getPayload());
        System.out.println("Topic " + enumTopico);
        for (User user : getAllUsers()) {
            userDispatcher.sendAsync(enumTopico, user.getUuid(), message.getId().continueWith(getClass().getSimpleName()), user);
            System.out.println("Acho que enviei para " + user);
        }

    }

    private List<User> getAllUsers() throws SQLException {
        var results = this.connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();
        while(results.next()) {
            users.add(new User(results.getString("uuid")));
        }
        return users;
    }
}
