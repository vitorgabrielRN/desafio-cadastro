package br.desafio.prodiga.BancoApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Model.Fatura;

@Service
public class BoletoService {

    @Value("${bank.api.url}")
    private String bankApiUrl;

    private final RestTemplate restTemplate;

    public BoletoService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public BoletoResponse registrarBoleto(Fatura fatura) {
        BoletoRequest request = new BoletoRequest(
            fatura.getCliente().getNome(),
            fatura.getValor(),
            fatura.getDataVencimento(),
            fatura.getId()
        );

        ResponseEntity<BoletoResponse> response = restTemplate.postForEntity(
            bankApiUrl + "/api/boletos",
            request,
            BoletoResponse.class
        );

        return response.getBody();
    }
}