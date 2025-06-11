package br.desafio.prodiga.BancoApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/boletos")
public class webhookBancoController {

    @Autowired
    private FaturaService faturaService;

    @PostMapping("/status-boleto")
    public ResponseEntity<Void> receberStatusBoleto(@RequestBody WebhookRequest request ) {
    
     try{
        faturaService.webhook(request);
        
        return new ResponseEntity<>(HttpStatus.OK);
     } catch(RuntimeException e){
        System.err.println("Não deu " + request.idFatura() + e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

     }
    }
    

}
