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
               // var email = "marcelo@email.com";
                for (int i = 0; i < 10; i++) {

                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);
                    var email = Math.random() + "@email.com";
                    var order = new Order(orderId, amount, email);
                    dispatcher.send(EnumTopico.ECOMMERCE_NEW_ORDER, email, order);

                    var emailCode = "Thank you for order!! We are processing your order!";
                    emailDispatcher.send(EnumTopico.ECOMMERCE_SEND_EMAIL, email, emailCode);

                }
            }
        }
    }
}