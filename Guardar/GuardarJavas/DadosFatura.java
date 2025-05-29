package br.desafio.prodiga.dto.Fatura;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.desafio.prodiga.Model.Fatura;

public record DadosFatura(
    Long id,
    String numFatura,
    int mes,
    int ano,
    double valor,
    LocalDate dataVencimento,
    String codigoBoleto,
    Situacao situacao,
    LocalDateTime dataPagamento,
    LocalDateTime dataGeracao,
    Long clienteId,
    String clienteNome
) {
    public DadosFatura(Fatura fatura) {
        this(
            fatura.getId(),
            fatura.getNumfatura(),
            fatura.getMes(),
            fatura.getAno(),
            fatura.getValor(),
            fatura.getDataVencimento(),
            fatura.getCodigoBoleto(),
            fatura.getSituacao(),
            fatura.getDataPagamento(),
            fatura.getDataGeracao(),
            fatura.getCliente() != null ? fatura.getCliente().getId() : null,
            fatura.getCliente() != null ? fatura.getCliente().getNome() : null
        );
    }
}