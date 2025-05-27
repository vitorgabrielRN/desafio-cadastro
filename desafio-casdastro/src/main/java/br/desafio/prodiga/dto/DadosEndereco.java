package br.desafio.prodiga.dto;

public record DadosEndereco(
    String logradouro,
    String cep,
    String bairro,
    String uf,
    String cidade
) {}