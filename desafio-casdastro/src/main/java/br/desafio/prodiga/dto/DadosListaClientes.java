package br.desafio.prodiga.dto;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Endereco;

public record DadosListaClientes(
 Long id, 
 String nome,
 String email,
 String cpf,
 String telefone,
 Endereco endereco
) {

    public DadosListaClientes(Cliente cliente){
        this(cliente.getId(),cliente.getNome(),cliente.getEmail(),cliente.getCpf(),cliente.getTelefone(),cliente.getEndereco());
    }

}
