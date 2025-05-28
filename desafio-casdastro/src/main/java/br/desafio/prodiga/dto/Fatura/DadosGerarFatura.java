package br.desafio.prodiga.dto.Fatura;

public record DadosGerarFatura(
    int ano,
    int mes,
    Long clienteId
) {}