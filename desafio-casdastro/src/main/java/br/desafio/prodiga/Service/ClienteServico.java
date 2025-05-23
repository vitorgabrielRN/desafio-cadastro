package br.desafio.prodiga.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.DadosClientes;
import br.desafio.prodiga.Model.Endereco;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;

@Service
public class ClienteServico {

    @Autowired
    ClienteRepository repository;
    private DadosClientes clientes;
    private Fatura fatura;

    public ClienteServico(DadosClientes clientes, Fatura fatura) {
        this.clientes = clientes;
        this.fatura = fatura;
    }

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
//TODO GERAR TESTES

}
