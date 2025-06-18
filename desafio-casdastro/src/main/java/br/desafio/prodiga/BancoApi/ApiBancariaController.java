
package br.desafio.prodiga.BancoApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.dto.BoletoRequest;
import br.desafio.prodiga.dto.BoletoResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/boletos")
public class ApiBancariaController {

    @PostMapping
    public ResponseEntity<BoletoResponse> registrarBoleto(@RequestBody BoletoRequest request) {
        simularProcessamentoExterno(); 

        String boletoId = "BOL-" + System.currentTimeMillis() + "-" + request.getFaturaId();
        return ResponseEntity.ok(new BoletoResponse(boletoId));
    }

    private void simularProcessamentoExterno() {
        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }
}