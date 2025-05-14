package br.desafio.prodiga.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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


@Table(name = "/faturas")
public class Fatura  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numfatura;
    
    private int mes;

    private int ano;
    
    private Double valor;

    private String situacao;

    private LocalDate dataVencimento;

    @Column(nullable = false,  unique = true)
    private String codigoBoleto;

    private LocalDateTime datapPagamento;

    private LocalDateTime dataGeracao = LocalDateTime.now();
    @ManyToOne
    private Cliente cliente;
    
    public void GerarNumeroBoleto(){
        this.numfatura = "BOL-" + LocalDateTime.now().getYear() + "-" + 
        String.format("06d",(int)(Math.random() + 999999)); 
    }

    public void GerarNumFatura(){
        this.numfatura = "FAT-" + LocalDateTime.now().getYear() + LocalDateTime.now().getMonth() +
        "-" + String.format("%06d", new Random().nextInt(10000));
    }

    public void setDatapPagamento(LocalDate dataPagamento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDatapPagamento'");
    }

    public void setAnoReferencia(int ano2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAnoReferencia'");
    }

    public void setMesReferencia(int mes2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMesReferencia'");
    }
}
