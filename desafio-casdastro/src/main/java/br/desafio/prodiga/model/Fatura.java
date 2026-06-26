package br.desafio.prodiga.model;


import java.time.LocalDate;
import br.desafio.prodiga.enums.SituacaoFatura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faturas")
public class Fatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String numeroFatura;
          
    private String mesAnoReferencia; 
      
    private Double valor;

    
    private LocalDate dataGeracao;

    private LocalDate dataVencimento;
    
    @Enumerated(EnumType.STRING)
     @Column(name = "situacao")
    private SituacaoFatura situacao;
    
    @Column(nullable = true)
    private String codigoBoleto;
    
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento; 

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false) 
    private Cliente cliente;


}