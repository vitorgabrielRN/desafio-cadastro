package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

import br.desafio.prodiga.Enums.SituacaoFatura;

public record WebhookRequest(
    Long idFatura,
    String codigoBoleto,
    SituacaoFatura novaSituacao,
    LocalDate dataEvento
) {}
