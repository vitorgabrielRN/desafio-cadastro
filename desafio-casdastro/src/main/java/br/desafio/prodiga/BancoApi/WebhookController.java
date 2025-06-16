package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.Service.FaturaService;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController // POST /atualizacao-boletos/status-boleto
@RequestMapping("/atualizacao-boletos")
public class WebhookController {

   @Autowired
   private FaturaService faturaService;

   @Autowired
   private FaturaRepository faturaRepository;

   @PostMapping("/boletos")
   public ResponseEntity<String> callback(@RequestBody FaturaWebhookCallbackRequest request) {
      System.out.println("resposta é  " + request);
      try {
         faturaService.atualizarFaturaViaWebhook(
               request.getFaturaId(),
               request.getSituacaoFatura(),
               request.getDataEvento());
         return new ResponseEntity<>("Webhook concluido", HttpStatus.OK);
      } catch (IllegalArgumentException e) {
         System.err.println("O WebHoock não achou a fatura" + e.getMessage());
         return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
      } catch (Exception e) {
         System.err.println("Deu problema no WEbHook" + e.getMessage());
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   @Transactional
   public void atualizarFaturaViaWebhook(Long faturaId, SituacaoFatura novaSituacao, LocalDate dataEvento) {
      Fatura fatura = faturaRepository.findById(faturaId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Fatura com ID " + faturaId + " não encontrada para atualização via webhook."));

      System.out.println("Fatura ID " + faturaId + ": Recebido pedido de atualização via Webhook. Situação de "
            + fatura.getSituacao() + " para " + novaSituacao);

      fatura.setSituacao(novaSituacao);

      if (novaSituacao == SituacaoFatura.PAGA) {
         fatura.setDataPagamento(dataEvento != null ? dataEvento : LocalDate.now());
         System.err.println("Fatura PAGA");
      }

      faturaRepository.save(fatura);
      System.out.println("Fatura ID " + faturaId + " atualizada com sucesso para: " + novaSituacao);
   }

}
