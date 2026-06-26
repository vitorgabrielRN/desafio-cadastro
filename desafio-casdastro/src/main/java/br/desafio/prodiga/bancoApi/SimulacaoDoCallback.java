package br.desafio.prodiga.bancoApi;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.enums.SituacaoFatura;
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
        System.out.println("Simulação: Simulando processamento de pagamento para fatura " + faturaId + "...");
        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Simulação: Simulação de atraso interrompida.");
            return; 
        }

       
        FaturaWebhookCallbackRequest callbackRequest = new FaturaWebhookCallbackRequest(
            faturaId,
            codigoBoleto,
            SituacaoFatura.PAGA, 
            LocalDate.now() 
        );

        String fullCallbackUrl = appCallbackUrl + "/atualizacao-boletos/boletos"; 
        System.out.println("Simulação: Enviando callback para: " + fullCallbackUrl + " com dados: " + callbackRequest);

        try {
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                fullCallbackUrl,
                callbackRequest,
                String.class
            );
            System.out.println("Simulação: Callback enviado. Status: " + response.getStatusCode());
        } catch (ResourceAccessException e) {
            System.err.println("Simulação: ERRO de conexão ao enviar callback: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Simulação: ERRO inesperado ao enviar callback: " + e.getMessage());
            e.printStackTrace();
        }
    }
}