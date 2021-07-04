package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:service-users/target/users_database.db";
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

        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(),
                EnumTopico.ECOMMERCE_NEW_ORDER,
                createUserService::parse,
                Order.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("=========================================");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value().toString());
        Order order = record.value();

        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        } else {
            System.out.println("User already inserted");
        }
    }

    private void insertNewUser(String email) throws SQLException {
        var insert = this.connection.prepareStatement("insert into Users (uuid, email) values (?, ?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário uuid e " + email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        System.out.println(email);
        var exists = this.connection.prepareStatement("select uuid from Users where email like ? limit 1");
        exists.setString(1,email);
        var results =  exists.executeQuery();
        return !results.next();
    }
}
