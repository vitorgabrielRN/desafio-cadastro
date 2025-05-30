package br.desafio.prodiga.dto.Cliente;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.EnderecoDTO;
public record ClienteDTO(
    Long id,
    String nome,
    String cpf,
    String email,
    String telefone,
    EnderecoDTO endereco
) {
    public ClienteDTO(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getEmail(),
            cliente.getTelefone(),
            new EnderecoDTO(cliente.getEndereco())
        );
    }

     }