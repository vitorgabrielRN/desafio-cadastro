package br.desafio.prodiga.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import br.desafio.prodiga.dto.Fatura.DadosCadastroFatura;
import br.desafio.prodiga.dto.Fatura.Situacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "faturas")

public class Fatura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numerofatura;
   
    private int mes;

    
    private int ano;

    
    private Double valor;

    
    private LocalDate dataVencimento;

    
    private String codigoBoleto;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    private LocalDateTime dataPagamento;

    private LocalDateTime dataGeracao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
     
    public void gerarNumFatura(Random random) {
        this.numerofatura = String.format("FAT-%d%02d-%06d", 
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonthValue(),
            random.nextInt(10000));
    }
    public void gerarCodigoBoleto(Random random){
        this.codigoBoleto = String.format("FAT-%d%02d-%06d",
        LocalDateTime.now().getYear(), LocalDateTime.now().getDayOfMonth(),
        random.nextInt(10000));
    }

     public Fatura(DadosCadastroFatura dados){
        this.id = dados.id();
        this.ano = dados.ano();
        this.mes = dados.mes();
        this.valor = dados.valor();
        this.dataVencimento = dados.dataVencimento();
        this.codigoBoleto = dados.codigoBoleto();
        this.situacao = dados.situacao();
        this.dataPagamento = dados.dataPagamento();
        this.dataGeracao = dados.dataGeracao();
        this.cliente = dados.cliente();
     }


     
     
}