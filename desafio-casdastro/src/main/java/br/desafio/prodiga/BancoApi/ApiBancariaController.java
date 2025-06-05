package br.desafio.prodiga.BancoApi;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boletos")
public class ApiBancariaController {

    @PostMapping
    public ResponseEntity<BoletoResponse> registrarBoleto(@RequestBody BoletoRequest request) {
        String boletoCodigo = "BOL" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        
        return ResponseEntity.ok(new BoletoResponse(boletoId));
    }
}