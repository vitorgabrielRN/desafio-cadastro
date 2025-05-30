package br.desafio.prodiga.dto.Fatura;

public record GerarFaturaDTO(
    int mes,
    int ano,
    Long clienteId
) {}