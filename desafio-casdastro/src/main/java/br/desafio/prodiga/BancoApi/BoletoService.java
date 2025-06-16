package br.desafio.prodiga.BancoApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.desafio.prodiga.Model.Fatura;

    @Service
    public class BoletoService {

        @Value("${bank.api.url}")
        private String bankApiUrl;


        private final RestTemplate restTemplate;

        public BoletoService(RestTemplateBuilder builder){
            this.restTemplate  = builder.build();
        }

        public BoletoResponse registrarBoleto(Fatura fatura) { 
            BoletoRequest request = new BoletoRequest(
                fatura.getCliente().getNome(),
                fatura.getValor(),
                fatura.getDataVencimento(),
                fatura.getId()
            );
        
            String fullUrl = bankApiUrl + "/api/boletos";
            System.out.println("Teste para verificar se está funcionando: Chamando " + fullUrl + " com request " + request);

            try {
                ResponseEntity<BoletoResponse> response = restTemplate.postForEntity(
                    fullUrl, 
                    request, 
                    BoletoResponse.class 
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    System.out.println("BoletoService: Boleto registrado com sucesso. Código: " + response.getBody().getCodigoBoleto());
                    return response.getBody(); 
                } else {
                
                    String errorMessage = "Falha ao registrar boleto na API bancária. Status: " + response.getStatusCode();
                    if (response.hasBody()) {
                        errorMessage =  " Nãooo "   +  response.getBody();
                    }
                    System.err.println("BoletoService: " + errorMessage);
                    throw new RuntimeException(errorMessage); 
                }       
            } catch (HttpClientErrorException e) {
                System.err.println("BoletoService: Erro HTTP ao registrar boleto: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
                throw new RuntimeException("Erro da API bancária ao registrar boleto: " + e.getResponseBodyAsString(), e);
            } catch (ResourceAccessException e) {
                
                System.err.println("BoletoService: Erro de rede ao conectar com a API bancária: " + e.getMessage());
                throw new RuntimeException("Falha de comunicação com a API bancária.", e);
            } catch (Exception e) {
                
                System.err.println("BoletoService: Erro inesperado ao registrar boleto: " + e.getMessage());
                e.printStackTrace(); 
                throw new RuntimeException("Erro interno ao tentar registrar boleto.", e);
            }
        
            

            
        }

    }