package br.desafio.prodiga.dto;

public record DadosClientes(
    String nome,
    String email,
    DadosEndereco endereco,
    String cpf,
    String telefone
) {}