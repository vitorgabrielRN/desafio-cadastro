package br.desafio.prodiga.dto.Cliente;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.dto.DadosEndereco;


public record DadosListaClientes(
    Long id,
    String nome,
    String email,
    String cpf,
    String telefone,
    DadosEndereco endereco
) {
    public DadosListaClientes(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getCpf(),
            cliente.getTelefone(),
            new DadosEndereco(
                cliente.getEndereco().getLogradouro(),
                cliente.getEndereco().getCep(),
                cliente.getEndereco().getBairro(),
                cliente.getEndereco().getUf(),
                cliente.getEndereco().getCidade()
            )
        );
    }
}
