package br.desafio.prodiga.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import br.desafio.prodiga.dto.Fatura.DataFaturas;
import br.desafio.prodiga.dto.Fatura.Situacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "faturas")
public class Fatura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numfatura;

    private int mes;

    private int ano;

    private Double valor;

    private LocalDate dataVencimento;

    // @Column(nullable = false, unique = true)
    private String codigoBoleto;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    private LocalDateTime dataPagamento;

    private LocalDateTime dataGeracao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Construtor personalizado com foco nas datas das faturas
    public Fatura(DataFaturas dados) {
        this.ano = dados.ano();
        this.mes = dados.mes();
        this.dataGeracao = dados.dataGeracao();
        this.dataPagamento = dados.dataPagamento();
        this.dataVencimento = dados.dataVencimento();
    }

    public void gerarNumFatura() {
        this.numfatura = "FAT-" + LocalDateTime.now().getYear() +
                LocalDateTime.now().getMonthValue() +
                "-" + String.format("%06d", new Random().nextInt(10000));
    }

    public void setDatapPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento.atStartOfDay();
    }

    public void setAnoReferencia(int ano) {
        this.ano = ano;
    }

    public void setMesReferenia(int mes) {
        this.mes = mes;
    }
}
