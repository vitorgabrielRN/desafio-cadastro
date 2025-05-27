package br.desafio.prodiga.dto;

import java.time.LocalDate;

public record DadosPagamentoFatura(
    LocalDate dataPagamento
) {}