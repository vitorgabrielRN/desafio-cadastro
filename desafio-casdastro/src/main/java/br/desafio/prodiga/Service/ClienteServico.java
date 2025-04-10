package br.desafio.prodiga.Service;





import org.springframework.beans.factory.annotation.Autowired;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Repository.ClienteRepositorio;

public class ClienteServico {

    public static void salvarCliente(Cliente cliente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public Cliente cadastrarCliente(String nome, String cpf, String email, String endereco, String telefone, String senha ) {

        
        Cliente cliente = new Cliente();
        
        
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);
        cliente.setSenha(senha); 
        return clienteRepositorio.save(cliente);
    }

    

    
    


    

}
