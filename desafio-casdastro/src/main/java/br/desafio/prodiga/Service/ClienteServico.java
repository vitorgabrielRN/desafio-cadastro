package br.desafio.prodiga.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Endereco;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.dto.DadosClientes;

@Service
public class ClienteServico {

    @Autowired
    ClienteRepository repository;

    public Cliente cadastrar(DadosClientes dados) {

        try {
            Cliente cliente = new Cliente();

            cliente.setNome(dados.nome());
            cliente.setCpf(dados.cpf());
            cliente.setEmail(dados.email());
            cliente.setEndereco(new Endereco(dados.endereco()));
            return repository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Falha ao cadastrar o cliente", e);
        }
    }
    
    public Cliente salvarCliente(Cliente cliente){
        return repository.save(cliente);
    }

    public  List<Cliente> listarTodos(){
        return repository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id){
        return repository.findById(id);
    }

    public boolean excluirCliente(Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}