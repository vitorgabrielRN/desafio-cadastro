package br.desafio.prodiga.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DataFaturas(
    int mes, 
    int ano, 
    LocalDate dataVencimento, 
    LocalDateTime dataGeracao, 
    LocalDateTime dataPagamento) {


       

}
