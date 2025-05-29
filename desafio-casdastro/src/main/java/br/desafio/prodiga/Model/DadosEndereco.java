package br.desafio.prodiga.Model;

public record DadosEndereco(
    String logradouro,
    String cep,
    String bairro,
    String uf,
    String cidade
) {}