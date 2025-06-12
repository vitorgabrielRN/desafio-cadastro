package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;



import br.desafio.prodiga.Enums.SituacaoFatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaturaWebhookCallbackRequest {
    private Long faturaid;
    private String codigoBoleto;
    private SituacaoFatura situacaoFatura;
    private LocalDate dataEvento;


}
