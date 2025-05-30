package br.desafio.prodiga.dto.Fatura;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.desafio.prodiga.Model.Fatura;

public record FaturaDTO(
    Long id,
    String numeroFatura,
    int mes,
    int ano,
    Double valor,
    LocalDate dataVencimento,
    String codigoBoleto,
    Situacao situacao,
    LocalDateTime dataPagamento,
    LocalDateTime dataGeracao,
    Long clienteId
) {
    public FaturaDTO(Fatura fatura) {
        this(
            fatura.getId(),
            fatura.getNumeroFatura(),
            fatura.getMes(),
            fatura.getAno(),
            fatura.getValor(),
            fatura.getDataVencimento(),
            fatura.getCodigoBoleto(),
            fatura.getSituacao(),
            fatura.getDataPagamento(),
            fatura.getDataGeracao(),
            fatura.getCliente().getId()
        );
    }
}