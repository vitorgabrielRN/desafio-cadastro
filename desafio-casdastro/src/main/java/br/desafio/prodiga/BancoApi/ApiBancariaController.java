
package br.desafio.prodiga.BancoApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.dto.BoletoRequest;
import br.desafio.prodiga.dto.BoletoResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController  //  POST /api/boletos
@RequestMapping("/api/boletos")
public class ApiBancariaController {

  @Autowired
  private SimulacaoDoCallback simulacaoDoCallback;



    @PostMapping
    public ResponseEntity<BoletoResponse> registrarBoleto(@RequestBody BoletoRequest request) {
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        
        String boletoId = "BOL-" + System.currentTimeMillis() +  request.getFaturaId();

        System.out.println("API Bancária Simulada: Boleto para Fatura ID " + request.getFaturaId() + " registrado. Código: " + boletoId);

        
        return ResponseEntity.ok(new BoletoResponse(boletoId));
    }

}
