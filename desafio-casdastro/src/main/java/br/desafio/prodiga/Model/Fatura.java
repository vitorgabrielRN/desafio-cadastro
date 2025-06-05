package br.desafio.prodiga.Model;


import java.time.LocalDate;
import java.util.Random;

import br.desafio.prodiga.Enums.SituacaoFatura;
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
    
    private LocalDate dataVencimento;
    
    @Enumerated(EnumType.STRING)
     @Column(name = "situacao")
    private SituacaoFatura situacao;
    
    private String codigoBoleto;
    
    private LocalDate dataPagamento; 

    @ManyToOne
    @JoinColumn(name = "cliente_id") 
    private Cliente cliente;

    public static Double gerarValorAleatorio() {
        Random random = new Random();
        return 10.0 + (100.0 - 10.0) * random.nextDouble();
    }

    public static String gerarCodigoBoleto() {
        return "BOLETO-" + System.currentTimeMillis();
    }
}