
package br.desafio.prodiga.BancoApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController  //  POST /api/boletos
@RequestMapping("/api/boletos")
public class ApiBancariaController {

    @PostMapping
    public ResponseEntity<BoletoResponse> registrarBoleto(@RequestBody BoletoRequest request) {
        String boletoId = "BOL-" + System.currentTimeMillis();

        System.out.println("API Bancária Simulada: Recebido pedido para registrar boleto:");
        System.out.println("  Cliente: " + request.getNomeCliente());
        System.out.println("  Valor: " + request.getValor());
        System.out.println("  Vencimento: " + request.getDataVencimento());
        System.out.println("  ID da Fatura (Origem): " + request.getFaturaId());
        System.out.println("  Gerado Código Boleto: " + boletoId);
        
        return ResponseEntity.ok(new BoletoResponse(boletoId));
    }
    

}
