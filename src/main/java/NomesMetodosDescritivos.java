import java.util.ArrayList;
import java.util.List;

public class NomesMetodosDescritivos {

    /**
     * @INFO Não utilize
     */
    public void salvar() {}

    public void verificar() {}

    public void consultarPedido(String cod) {}

    /**
     * @INFO Utilize
     */

    //Método responsável por salvar um pedido
    public void salvarPedido() {}

    public void verificarStatusPedido() {}

    public void consultarPedidoPeloCodidoPedido(String codigoPedido) {}



    /**
     * @INFO Não utilize
     */
    public void SaveCustomer(String street, String number, String neighborhood, String city, String state, String country, String zipCode){}

    /**
     * @INFO Melhorado
     */
    public void SaveCustomer(Address address) { }

    /**
     * @INFO: consulta dados de relatório de vendas para
     * ser exportado para a AWS, objetivando a análise pela
     * equipe de B.I.
     *
     * @return List
     */
    public List<RelatorioVendas> consultarDadosRelatorioVendas() {
        return new ArrayList();
    }




}
