package br.desafio.prodiga.dto.Fatura;

import java.time.LocalDate;

public record DadosPagamentoFatura(
    Long clienteId,
    LocalDate dataPagamento
) {}