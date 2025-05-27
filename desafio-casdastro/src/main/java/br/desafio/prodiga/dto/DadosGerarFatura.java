package br.desafio.prodiga.dto;

public record DadosGerarFatura(
    int ano,
    int mes,
    Long clienteId
) {}