package br.desafio.prodiga.desafio.casdastro.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class  ConsumoApi {
    
    private String endereco;
    
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null; {
                try {
                    response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                
        
            }
                        // caso seja necessario!!!! lembrar de declarar. 
        //    String  Json = response.body();
        //      return Json;
}