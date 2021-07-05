package br.com.openmind;

import br.com.openmind.enumeration.EnumTopico;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportServlet extends HttpServlet {

    private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
     //Abordagem com fast delegate - não legal esse for
     // Tem que transferir para ser processada em outra camada...
//            Substituída pela abordagem batch do microservice que faz acesso ao base de dados
//            for (User user : users) {
//                batchDispatcher.send(EnumTopico.USER_GENERATE_READING_REPORT, user.getUuid(), user);
//            }
            //O 'for' vai ser a cargo do serviço que vai consumir esse tópico que acessa a base de dados (responsabilidade no local certo).
            batchDispatcher.send(EnumTopico.SEND_MESSAGE_TO_ALL_USERS, EnumTopico.USER_GENERATE_READING_REPORT.getTopico(), EnumTopico.USER_GENERATE_READING_REPORT.getTopico());
            System.out.println("Sent generate report to all users");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Reports request generated");
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
}
}
