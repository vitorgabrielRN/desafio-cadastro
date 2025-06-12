package br.desafio.prodiga.BancoApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController  // POST /atualizacao-boletos/status-boleto
@RequestMapping("/atualizacao-boletos")
public class WebhookController {
    
    @Autowired
    private FaturaService  faturaService;

    @PostMapping( "/boletos")
    public ResponseEntity<String> callback(@RequestBody FaturaWebhookCallbackRequest request) {
        System.out.println("resposta é  " + request);
         try {
            faturaService.atualizarFaturViaWebHook(
                request.getFaturaid(),
                request.getSituacaoFatura(),
                request.getDataEvento()
                );
                return new ResponseEntity<>("Webhook concluido", HttpStatus.OK);
         } catch (IllegalArgumentException e) {
            System.err.println("O WebHoock não achou a fatura" + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         } catch (Exception e){
            System.err.println("Deu problema no WEbHook" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
        
    }
    

}
