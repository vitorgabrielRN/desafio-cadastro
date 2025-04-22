package br.desafio.prodiga.Model;


import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor


@Table(name = "/Fatura'")
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fatura;

    private int mesReferencia;

    private int anoReferencia;

    private Double valor;

    private String situacao;

    private int dataVencimento;

    private String codigoBoleto;

    private LocalDateTime datapPagamento;

}
