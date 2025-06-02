package br.desafio.prodiga.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }
}