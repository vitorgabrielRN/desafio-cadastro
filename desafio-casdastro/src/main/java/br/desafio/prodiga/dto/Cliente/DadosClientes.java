package br.desafio.prodiga.dto.Cliente;

import br.desafio.prodiga.Model.DadosEndereco;

public record DadosClientes(
    String nome,
    String email,
    DadosEndereco endereco,
    String cpf,
    String telefone
) {}