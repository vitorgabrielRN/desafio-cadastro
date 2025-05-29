package br.desafio.prodiga.dto.Fatura;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;

public record ListaFatura(
    Long id,

    String numeroFatura,

    int mes,

    int ano,

    Double valor,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataVencimento,

    String codigoBoleto,

    Situacao situacao,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime dataPagamento,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime dataGeracao,
    
    Cliente cliente
) {

    public ListaFatura(Fatura fatura, DadosCadastroFatura dados){
        this(fatura.getId(),fatura.getNumerofatura(),fatura.getMes(),fatura.getAno(),fatura.getValor(),
            fatura.getDataVencimento(),fatura.getCodigoBoleto(),
            fatura.getSituacao(),fatura.getDataPagamento(),fatura.getDataGeracao(),fatura.getCliente());
    }
}

