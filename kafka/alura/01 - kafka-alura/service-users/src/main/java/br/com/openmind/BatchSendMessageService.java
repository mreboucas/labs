package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public static void main(String[] args) throws SQLException {

        var batchService = new BatchSendMessageService();
        try (var service = new KafkaService<>(BatchSendMessageService.class.getSimpleName(),
                EnumTopico.SEND_MESSAGE_TO_ALL_USERS,
                batchService::parse,
                String.class,
                Map.of())) {
            service.run();
        }
    }
    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, String> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("=========================================");
        System.out.println("Processing new batch");
        System.out.println(record.value().toString());
        EnumTopico enumTopico = EnumTopico.findByName(record.value());
        for (User user : getAllUsers()) {
            userDispatcher.send(enumTopico, user.getUuid(), user);
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
