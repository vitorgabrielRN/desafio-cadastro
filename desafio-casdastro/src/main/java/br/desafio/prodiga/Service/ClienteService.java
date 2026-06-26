package br.desafio.prodiga.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id).map(clienteExistente -> {
            clienteExistente.setNome(clienteAtualizado.getNome());
            clienteExistente.setCpf(clienteAtualizado.getCpf());
            clienteExistente.setEmail(clienteAtualizado.getEmail());
            clienteExistente.setEndereco(clienteAtualizado.getEndereco());
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
            return clienteRepository.save(clienteExistente);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));
    }

    public void removerCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}