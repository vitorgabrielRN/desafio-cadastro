package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoletoRequest {

    private String nomeCliente;
    private Double valor;
    private LocalDate DataVencimento;
    private Long faturaId;


}
