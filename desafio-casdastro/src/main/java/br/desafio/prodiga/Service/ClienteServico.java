package br.desafio.prodiga.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Endereco;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.dto.Cliente.DadosClientes;

@Service
public class ClienteServico {

    @Autowired
    private ClienteRepository repository;

    public Cliente cadastrarCliente(DadosClientes dados) {
        Cliente cliente = new Cliente();
        cliente.setNome(dados.nome());
        cliente.setEmail(dados.email());
        cliente.setCpf(dados.cpf());
        cliente.setTelefone(dados.telefone());
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dados.endereco().logradouro());
        endereco.setCep(dados.endereco().cep());
        endereco.setBairro(dados.endereco().bairro());
        endereco.setUf(dados.endereco().uf());
        endereco.setCidade(dados.endereco().cidade());
        
        cliente.setEndereco(endereco);
        
        return repository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, DadosClientes dados) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        cliente.setNome(dados.nome());
        cliente.setEmail(dados.email());
        cliente.setTelefone(dados.telefone());
        
        Endereco endereco = cliente.getEndereco();
        endereco.setLogradouro(dados.endereco().logradouro());
        endereco.setCep(dados.endereco().cep());
        endereco.setBairro(dados.endereco().bairro());
        endereco.setUf(dados.endereco().uf());
        endereco.setCidade(dados.endereco().cidade());
        
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public void excluirCliente(Long id) {
        repository.deleteById(id);
    }

    public void salvar(Cliente cliente) {
    repository.save(cliente);
    }

    public void atualizar(Cliente cliente) {
       if (cliente.getId() == null) {
            throw new IllegalArgumentException("Id não pode ser nulo para atualização");
        }
        repository.save(cliente);
    }
}