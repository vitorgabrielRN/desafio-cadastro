package br.desafio.prodiga.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Repository.ClienteRepository;

@Service
public class ClienteServico {

    @Autowired
    private static ClienteRepository clienteRepositorio;

    public Cliente cadastrarCliente(String nome, String cpf, String email, String endereco, String telefone) {

        Cliente cliente = new Cliente();

        try {
            System.out.println("cadastrando");
            cliente.setNome(nome);
            cliente.setCpf(cpf);
            cliente.setEmail(email);
            cliente.setEndereco(endereco);
            cliente.setTelefone(telefone);
            return clienteRepositorio.save(cliente);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    public Optional<Cliente> buscarClienteId(Long id) {
        return clienteRepositorio.findById(id);
    }

    public static List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    public boolean excluirCliente(Long id) {
        if (clienteRepositorio.existsById(id)) {
            clienteRepositorio.deleteById(id);
            return true;
        }
        return false;
    }

}
