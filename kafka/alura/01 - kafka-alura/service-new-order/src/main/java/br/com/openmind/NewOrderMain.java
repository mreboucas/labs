package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Classe principal que executa o produtor para as classes consumidoras receberem a mensagem: LogService, EmailService, FraudDetector
 */
public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        try (var dispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {
                for (int i = 0; i < 10; i++) {

                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);
                    var order = new Order(userId, orderId, amount);
                    dispatcher.send(EnumTopico.ECOMMERCE_NEW_ORDER, userId, order);

                    var email = "Thank you for order!! We are processing your order!";
                    emailDispatcher.send(EnumTopico.ECOMMERCE_SEND_EMAIL, userId, email);

                }
            }
        }
    }
}