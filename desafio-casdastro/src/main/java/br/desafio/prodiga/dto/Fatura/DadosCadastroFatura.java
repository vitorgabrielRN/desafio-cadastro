package br.desafio.prodiga.dto.Fatura;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.desafio.prodiga.Model.Cliente;

public record DadosCadastroFatura(
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
    
    Cliente cliente) {}
