package br.desafio.prodiga.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@Table(name = "faturas")
@Entity
public class Fatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFatura;
    private String mesReferencia;
    private Double valor;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private SituacaoFatura situacao = SituacaoFatura.GERADA;

    private String codigoBoleto;
    private LocalDate dataPagamento;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Fatura() {

    }
}