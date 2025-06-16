package br.desafio.prodiga.BancoApi;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.dto.FaturaWebhookCallbackRequest;

@Service
public class SimulacaoDoCallback {

    @Value("${app.callback.url}") // A URL base da sua aplicação (seu sistema)
    private String appCallbackUrl;

    private final RestTemplate restTemplate;

    // Injeta o RestTemplate configurado na PrincipalApplication
    public SimulacaoDoCallback(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async // Permite que este método seja executado em uma thread separada
    public void enviarCallbackPagamento(Long faturaId, String codigoBoleto) {
        System.out.println("Simulação: Simulando processamento de pagamento para fatura " + faturaId + "...");
        try {
            Thread.sleep(5000); // Simula 5 segundos para o pagamento ser "processado" no banco
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Boa prática para threads interrompidas
            System.err.println("Simulação: Simulação de atraso interrompida.");
            return; // Sai do método se a thread for interrompida
        }

        // Prepara os dados do callback (simulando que o banco informa que foi pago)
        FaturaWebhookCallbackRequest callbackRequest = new FaturaWebhookCallbackRequest(
            faturaId,
            codigoBoleto,
            SituacaoFatura.PAGA, // O banco "decide" que foi pago
            LocalDate.now() // Data do "pagamento"
        );

        String fullCallbackUrl = appCallbackUrl + "/atualizacao-boletos/boletos"; 
        System.out.println("Simulação: Enviando callback para: " + fullCallbackUrl + " com dados: " + callbackRequest);

        try {
            // Envia a requisição POST para o seu WebhookController
            ResponseEntity<String> response = restTemplate.postForEntity(
                fullCallbackUrl,
                callbackRequest,
                String.class
            );
            System.out.println("Simulação: Callback enviado. Status: " + response.getStatusCode());
        } catch (ResourceAccessException e) {
            System.err.println("Simulação: ERRO de conexão ao enviar callback: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Simulação: ERRO inesperado ao enviar callback: " + e.getMessage());
            e.printStackTrace();
        }
    }
}