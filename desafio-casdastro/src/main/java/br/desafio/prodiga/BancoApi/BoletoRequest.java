package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

public record BoletoRequest(
    String nomeCliente,
    Double Valor,
    LocalDate dataVencimento,
    Long idFaturaAssociada
) {

}
