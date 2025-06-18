package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.dto.FaturaWebhookCallbackRequest;

@Service
public class SimulacaoDoCallback {

    @Value("${app.callback.url}")
    private String appCallbackUrl;

    private final RestTemplate restTemplate;

    public SimulacaoDoCallback(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async 
    public void enviarCallbackPagamento(Long faturaId, String codigoBoleto) {
        simularProcessamento(); 

        FaturaWebhookCallbackRequest callbackRequest = criarCallbackRequest(faturaId, codigoBoleto);
        String fullCallbackUrl = appCallbackUrl + "/atualizacao-boletos/boletos";

        enviarRequestCallback(fullCallbackUrl, callbackRequest);
    }

    private void simularProcessamento() {
        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
          
        }
    }

    private FaturaWebhookCallbackRequest criarCallbackRequest(Long faturaId, String codigoBoleto) {
        return new FaturaWebhookCallbackRequest(
                faturaId,
                codigoBoleto,
                SituacaoFatura.PAGA,
                LocalDate.now()
        );
    }

    private void enviarRequestCallback(String url, FaturaWebhookCallbackRequest request) {
        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (ResourceAccessException e) {
          
            throw new RuntimeException("Falha na comunicação ao enviar callback para a aplicação: " + e.getMessage(), e);
        } catch (Exception e) {
          
            throw new RuntimeException("Erro inesperado ao enviar callback para a aplicação: " + e.getMessage(), e);
        }
    }
}