package br.com.openmind;

import br.com.openmind.consumer.ConsumerService;
import br.com.openmind.consumer.ServiceRunner;
import br.com.openmind.database.LocalDataBase;
import br.com.openmind.enumeration.EnumTopico;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDataBase database;

    private CreateUserService() throws SQLException {
        this.database = new LocalDataBase("users_database");
        this.database.createIfNotExists("create table Users (" +
                "uuid varchar(200) primary key," +
                "email varchar(200))");
    }

    public static void main(String[] args) {
        new ServiceRunner<>(CreateUserService::new).start(1);
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("=========================================");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value().toString());
        var order = record.value().getPayload();

        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        } else {
            System.out.println("User already inserted");
        }
    }

    @Override
    public EnumTopico getTopico() {
        return EnumTopico.ECOMMERCE_NEW_ORDER;
    }

    @Override
    public String getConsumerGroup() {
        return getClass().getSimpleName();
    }

    private void insertNewUser(String email) throws SQLException {
        String id = UUID.randomUUID().toString();
        database.update("insert into Users (uuid, email) values (?, ?)", id, email);
        System.out.println("Usuário uuid " + id + " e email: " + email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        System.out.println(email);
        var results = database.query("select uuid from Users where email like ? limit 1", email);
        return !results.next();
    }
}
