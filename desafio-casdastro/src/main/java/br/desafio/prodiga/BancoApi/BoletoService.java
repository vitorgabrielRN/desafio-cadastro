package br.desafio.prodiga.BancoApi;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Model.Fatura;

@Service
public class BoletoService {
  
  
  @Value("${banck.api.url}")  
  private String banckApiUrl;


    private RestTemplate restTemplate;

    public BoletoService(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }



    public String registrarBoleto(Fatura fatura){
        BoletoRequest request = new BoletoRequest(
            fatura.getCliente().getNome(),
            fatura.getValor(),
            fatura.getDataVencimento(),
            fatura.getId()
        );

        ResponseEntity<BoletoResponse> response =  restTemplate.postForEntity(
            banckApiUrl  + "/api/boletos",
            request,
            BoletoResponse.class
        );

          if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().codigoBoleto();
        } else {
            
            throw new RuntimeException("Falha ao registrar boleto na API bancária. Status: " + response.getStatusCode());
        }
    }
    
}


