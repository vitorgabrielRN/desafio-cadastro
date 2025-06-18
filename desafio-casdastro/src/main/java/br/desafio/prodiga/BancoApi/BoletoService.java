package br.desafio.prodiga.BancoApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.dto.BoletoRequest;
import br.desafio.prodiga.dto.BoletoResponse;

@Service
public class BoletoService {

    @Value("${bank.api.url}")
    private String bankApiUrl;

    private final RestTemplate restTemplate;

    public BoletoService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public BoletoResponse registrarBoleto(Fatura fatura) {
        BoletoRequest request = criarBoletoRequest(fatura);
        String fullUrl = bankApiUrl + "/api/boletos";

        try {
            ResponseEntity<BoletoResponse> response = restTemplate.postForEntity(
                    fullUrl,
                    request,
                    BoletoResponse.class
            );
            return processarRespostaBoleto(response);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro da API bancária ao registrar boleto: " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Falha de comunicação com a API bancária.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao tentar registrar boleto.", e);
        }
    }

    private BoletoRequest criarBoletoRequest(Fatura fatura) {
        return new BoletoRequest(
                fatura.getCliente().getNome(),
                fatura.getValor(),
                fatura.getDataVencimento(),
                fatura.getId()
        );
    }

    private BoletoResponse processarRespostaBoleto(ResponseEntity<BoletoResponse> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            String errorMessage = "Falha ao registrar boleto na API bancária. Status: " + response.getStatusCode();
            if (response.hasBody() && response.getBody() instanceof BoletoResponse) {
                errorMessage += " Corpo da Resposta: " + response.getBody();
            }
            throw new RuntimeException(errorMessage);
        }
    }
}