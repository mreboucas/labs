package srp;

import srp.repository.ClienteRepository;
import srp.repository.DiscontoRepository;
import srp.repository.EstoqueRepository;
import srp.repository.PedidoRepository;

public class RealizarPedido {

    private final ClienteRepository clienteRepository;
    private final DiscontoRepository discontoRepository;
    private final EstoqueRepository estoqueRepository;
    private final PedidoRepository pedidoRepository;

    public RealizarPedido(ClienteRepository clienteRepository, DiscontoRepository discontoRepository, EstoqueRepository estoqueRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.discontoRepository = discontoRepository;
        this.estoqueRepository = estoqueRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public void realizarPedido(Object cliente) {

        Object clienteAux = clienteRepository.buscarClientePorCpf(cliente.toString());
        if (clienteAux == null) {
            clienteRepository.salvarCliente(cliente);
        }
    }
}